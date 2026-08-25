package com.example.quoteapp.renderer

import android.content.Context
import android.graphics.*
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.example.quoteapp.model.*
import kotlin.math.cos
import kotlin.math.sin

object QuoteRenderer {

    private val bitmapCache = mutableMapOf<String, Bitmap>()

    fun renderToBitmap(
        state: EditorState,
        width: Int,
        height: Int,
        context: Context? = null
    ): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        drawBackground(canvas, state.background, width, height, state, context)

        if (state.overlay.opacity > 0f) {
            val paint = Paint().apply {
                color = state.overlay.color.toInt()
                alpha = (state.overlay.opacity * 255).toInt()
            }
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        }

        drawDecorations(canvas, state.decorations, width, height)

        if (state.quote.isNotBlank()) {
            val autoQuoteStyle = if (state.quoteStyle.autoFit) {
                autoFitFontSize(state.quote, state.quoteStyle, width, height)
            } else {
                state.quoteStyle
            }
            drawTextOnCanvas(canvas, state.quote, autoQuoteStyle, width, height)
        }

        if (state.author.isNotBlank()) {
            drawTextOnCanvas(canvas, state.author, state.authorStyle, width, height)
        }

        if (state.source.isNotBlank()) {
            val sourceStyle = state.authorStyle.copy(
                fontSize = state.authorStyle.fontSize * 0.85f,
                positionY = (state.authorStyle.positionY + 0.08f).coerceAtMost(0.95f)
            )
            drawTextOnCanvas(canvas, state.source, sourceStyle, width, height)
        }

        return bitmap
    }

    private fun drawBackground(
        canvas: Canvas,
        background: QuoteBackground,
        width: Int,
        height: Int,
        state: EditorState,
        context: Context? = null
    ) {
        when (background) {
            is QuoteBackground.SolidColor -> {
                val paint = Paint().apply { color = background.color.toInt() }
                canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
            }
            is QuoteBackground.Gradient -> drawGradient(canvas, background, width, height)
            is QuoteBackground.Programmatic -> drawPattern(canvas, background, width, height)
            is QuoteBackground.PngBackground -> drawPngBackground(canvas, background, width, height, context)
            is QuoteBackground.Image -> {
                val paint = Paint().apply { color = Color.DKGRAY }
                canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
            }
        }
    }

    private fun drawGradient(
        canvas: Canvas,
        gradient: QuoteBackground.Gradient,
        width: Int,
        height: Int
    ) {
        val colorStops = gradient.colorStops
        if (colorStops != null && colorStops.isNotEmpty()) {
            val colors = colorStops.map { it.color.toInt() }.toIntArray()
            val positions = colorStops.map { it.position }.toFloatArray()

            drawGradientWithArrays(canvas, colors, positions, gradient.type, gradient.angle, width, height)
            return
        }

        if (gradient.colors.isEmpty()) return

        val colors = gradient.colors.map { it.toInt() }.toIntArray()
        if (colors.size == 1) {
            val paint = Paint().apply { color = colors[0] }
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
            return
        }

        val positions = if (colors.size == 2) null else {
            FloatArray(colors.size) { it.toFloat() / (colors.size - 1) }
        }

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = Math.sqrt((width * width + height * height).toDouble()).toFloat() / 2f

        val shader = when (gradient.type) {
            GradientType.LINEAR -> {
                val rad = Math.toRadians(gradient.angle.toDouble())
                val len = radius
                val x0 = centerX - (len * cos(rad)).toFloat()
                val y0 = centerY - (len * sin(rad)).toFloat()
                val x1 = centerX + (len * cos(rad)).toFloat()
                val y1 = centerY + (len * sin(rad)).toFloat()
                LinearGradient(x0, y0, x1, y1, colors, positions, Shader.TileMode.CLAMP)
            }
            GradientType.RADIAL -> {
                RadialGradient(centerX, centerY, radius, colors, positions, Shader.TileMode.CLAMP)
            }
            GradientType.SWEEP -> {
                SweepGradient(centerX, centerY, colors, positions)
            }
        }

        val paint = Paint().apply { this.shader = shader }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }

    private fun drawGradientWithArrays(
        canvas: Canvas,
        colors: IntArray,
        positions: FloatArray,
        type: GradientType,
        angle: Float,
        width: Int,
        height: Int
    ) {
        if (colors.isEmpty()) return
        if (colors.size == 1) {
            val paint = Paint().apply { color = colors[0] }
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
            return
        }

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = Math.sqrt((width * width + height * height).toDouble()).toFloat() / 2f

        val shader = when (type) {
            GradientType.LINEAR -> {
                val rad = Math.toRadians(angle.toDouble())
                val len = radius
                val x0 = centerX - (len * cos(rad)).toFloat()
                val y0 = centerY - (len * sin(rad)).toFloat()
                val x1 = centerX + (len * cos(rad)).toFloat()
                val y1 = centerY + (len * sin(rad)).toFloat()
                LinearGradient(x0, y0, x1, y1, colors, positions, Shader.TileMode.CLAMP)
            }
            GradientType.RADIAL -> {
                RadialGradient(centerX, centerY, radius, colors, positions, Shader.TileMode.CLAMP)
            }
            GradientType.SWEEP -> {
                SweepGradient(centerX, centerY, colors, positions)
            }
        }

        val paint = Paint().apply { this.shader = shader }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }

    private fun drawPngBackground(
        canvas: Canvas,
        png: QuoteBackground.PngBackground,
        width: Int,
        height: Int,
        context: Context?
    ) {
        if (context == null) {
            val paint = Paint().apply { color = Color.DKGRAY }
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
            return
        }

        try {
            val cacheKey = "${png.assetPath}_${width}_${height}"
            val cached = bitmapCache[cacheKey]
            val bitmap = cached ?: run {
                val options = BitmapFactory.Options().apply {
                    inJustDecodeBounds = true
                }
                context.assets.open(png.assetPath).use { BitmapFactory.decodeStream(it, null, options) }
                val sampleSize = calculateSampleSize(options.outWidth, options.outHeight, width, height)
                val decodeOptions = BitmapFactory.Options().apply {
                    inSampleSize = sampleSize
                }
                val loaded = context.assets.open(png.assetPath).use { BitmapFactory.decodeStream(it, null, decodeOptions) }
                if (loaded != null) {
                    val scaled = Bitmap.createScaledBitmap(loaded, width, height, true)
                    if (scaled !== loaded) loaded.recycle()
                    bitmapCache[cacheKey] = scaled
                    scaled
                } else null
            }

            if (bitmap != null) {
                canvas.drawBitmap(bitmap, 0f, 0f, null)
            } else {
                val paint = Paint().apply { color = Color.DKGRAY }
                canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
            }
        } catch (e: Exception) {
            val paint = Paint().apply { color = Color.DKGRAY }
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        }
    }

    private fun calculateSampleSize(srcWidth: Int, srcHeight: Int, reqWidth: Int, reqHeight: Int): Int {
        var inSampleSize = 1
        if (srcHeight > reqHeight || srcWidth > reqWidth) {
            val halfHeight = srcHeight / 2
            val halfWidth = srcWidth / 2
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    private fun drawPattern(
        canvas: Canvas,
        programmatic: QuoteBackground.Programmatic,
        width: Int,
        height: Int
    ) {
        val basePaint = Paint().apply { color = programmatic.baseColor.toInt() }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), basePaint)

        when (programmatic.pattern) {
            PatternType.NOISE -> drawNoisePattern(canvas, width, height, programmatic.accentColor)
            PatternType.PAPER -> drawPaperPattern(canvas, width, height, programmatic.accentColor)
            PatternType.GEOMETRIC -> drawGeometricPattern(canvas, width, height, programmatic.accentColor)
            PatternType.SOFT_GRADIENT -> {
                val paint = Paint().apply {
                    shader = RadialGradient(
                        width / 2f, height / 2f, width * 0.6f,
                        intArrayOf(programmatic.accentColor.toInt(), programmatic.baseColor.toInt()),
                        null, Shader.TileMode.CLAMP
                    )
                }
                canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
            }
            PatternType.DARK_TEXTURE -> drawDarkTexturePattern(canvas, width, height)
            PatternType.ABSTRACT_GRADIENT -> {
                val paint = Paint().apply {
                    shader = LinearGradient(
                        0f, 0f, width.toFloat(), height.toFloat(),
                        intArrayOf(
                            programmatic.baseColor.toInt(),
                            programmatic.accentColor.toInt(),
                            programmatic.baseColor.toInt()
                        ),
                        null, Shader.TileMode.CLAMP
                    )
                }
                canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
            }
            PatternType.MINIMAL -> {
                val linePaint = Paint().apply {
                    color = programmatic.accentColor.toInt()
                    strokeWidth = 2f
                    alpha = 80
                }
                val step = width / 20f
                for (i in 1..19) {
                    canvas.drawLine(i * step, 0f, i * step, height.toFloat(), linePaint)
                }
                val hStep = height / 20f
                for (i in 1..19) {
                    canvas.drawLine(0f, i * hStep, width.toFloat(), i * hStep, linePaint)
                }
            }
        }
    }

    private fun drawNoisePattern(canvas: Canvas, width: Int, height: Int, color: Long) {
        val paint = Paint().apply {
            this.color = color.toInt()
            alpha = 40
        }
        val random = java.util.Random(42)
        val step = 8
        var y = 0
        while (y < height) {
            var x = 0
            while (x < width) {
                if (random.nextFloat() > 0.5f) {
                    val size = 2 + random.nextInt(4)
                    canvas.drawRect(x.toFloat(), y.toFloat(), (x + size).toFloat(), (y + size).toFloat(), paint)
                }
                x += step
            }
            y += step
        }
    }

    private fun drawPaperPattern(canvas: Canvas, width: Int, height: Int, color: Long) {
        val paint = Paint().apply {
            this.color = color.toInt()
            alpha = 30
        }
        val lineHeight = height / 30f
        for (i in 1..29) {
            val y = i * lineHeight
            canvas.drawLine(0f, y, width.toFloat(), y, paint)
        }
    }

    private fun drawGeometricPattern(canvas: Canvas, width: Int, height: Int, color: Long) {
        val paint = Paint().apply {
            this.color = color.toInt()
            style = Paint.Style.STROKE
            strokeWidth = 2f
            alpha = 60
        }
        val cols = 6
        val rows = 6
        val cellW = width.toFloat() / cols
        val cellH = height.toFloat() / rows
        for (row in 0 until rows) {
            for (col in 0 until cols) {
                val left = col * cellW + cellW * 0.1f
                val top = row * cellH + cellH * 0.1f
                val right = (col + 1) * cellW - cellW * 0.1f
                val bottom = (row + 1) * cellH - cellH * 0.1f
                canvas.drawRect(left, top, right, bottom, paint)
            }
        }
    }

    private fun drawDarkTexturePattern(canvas: Canvas, width: Int, height: Int) {
        val paint = Paint().apply {
            color = Color.WHITE
            alpha = 15
        }
        val random = java.util.Random(42)
        val count = width * height / 500
        for (i in 0 until count) {
            val cx = random.nextFloat() * width
            val cy = random.nextFloat() * height
            val r = 1f + random.nextFloat() * 3f
            canvas.drawCircle(cx, cy, r, paint)
        }
    }

    private fun drawDecorations(
        canvas: Canvas,
        decorations: List<Decoration>,
        width: Int,
        height: Int
    ) {
        for (decoration in decorations) {
            when (decoration) {
                is Decoration.Line -> {
                    val paint = Paint().apply {
                        color = decoration.color.toInt()
                        strokeWidth = decoration.strokeWidth * width / 1080f
                        strokeCap = Paint.Cap.ROUND
                    }
                    val startX = decoration.startX * width
                    val endX = decoration.endX * width
                    val y = decoration.y * height
                    canvas.drawLine(startX, y, endX, y, paint)
                }
                is Decoration.QuoteMarks -> {
                    val paint = Paint().apply {
                        color = decoration.color.toInt()
                        textSize = decoration.size * width / 1080f
                        typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
                        textAlign = Paint.Align.LEFT
                    }
                    canvas.drawText("\u201C", width * 0.08f, height * 0.35f, paint)
                    canvas.drawText("\u201D", width * 0.82f, height * 0.75f, paint)
                }
                is Decoration.Circle -> {
                    val paint = Paint().apply {
                        color = decoration.color.toInt()
                        style = Paint.Style.STROKE
                        strokeWidth = decoration.strokeWidth * width / 1080f
                    }
                    val radius = decoration.radius * minOf(width, height)
                    val cx = decoration.centerX * width
                    val cy = decoration.centerY * height
                    canvas.drawCircle(cx, cy, radius, paint)
                }
                is Decoration.Border -> {
                    val paint = Paint().apply {
                        color = decoration.color.toInt()
                        style = Paint.Style.STROKE
                        strokeWidth = decoration.strokeWidth * width / 1080f
                    }
                    val inset = decoration.inset * minOf(width, height)
                    val rect = RectF(inset, inset, width - inset, height - inset)
                    canvas.drawRect(rect, paint)
                }
            }
        }
    }

    private fun drawTextOnCanvas(
        canvas: Canvas,
        text: String,
        style: TextSettings,
        canvasWidth: Int,
        canvasHeight: Int
    ) {
        val scaledFontSize = style.fontSize * canvasHeight / 1080f

        val typefaceStyle = when {
            style.isBold && style.isItalic -> Typeface.BOLD_ITALIC
            style.isBold -> Typeface.BOLD
            style.isItalic -> Typeface.ITALIC
            else -> Typeface.NORMAL
        }

        val textPaint = TextPaint().apply {
            textSize = scaledFontSize
            color = style.color.toInt()
            alpha = (style.opacity * 255).toInt()
            typeface = style.fontFamily.typeface(typefaceStyle)
            letterSpacing = style.letterSpacing
            isAntiAlias = true

            if (style.shadowEnabled) {
                setShadowLayer(
                    style.shadowRadius * canvasHeight / 1080f,
                    style.shadowDx * canvasHeight / 1080f,
                    style.shadowDy * canvasHeight / 1080f,
                    style.shadowColor.toInt()
                )
            }

            if (style.strokeEnabled) {
                strokeWidth = style.strokeWidth * canvasHeight / 1080f
                this@apply.style = Paint.Style.FILL_AND_STROKE
            }
        }

        val layoutWidth = (canvasWidth * style.textWidth).toInt()

        val alignment = when (style.alignment) {
            TextAlign.LEFT -> Layout.Alignment.ALIGN_NORMAL
            TextAlign.CENTER -> Layout.Alignment.ALIGN_CENTER
            TextAlign.RIGHT -> Layout.Alignment.ALIGN_OPPOSITE
        }

        val layout = StaticLayout.Builder.obtain(text, 0, text.length, textPaint, layoutWidth)
            .setAlignment(alignment)
            .setLineSpacing(0f, style.lineHeight)
            .setIncludePad(false)
            .build()

        val textHeight = layout.height.toFloat()
        val textWidth = layoutWidth.toFloat()

        val centerX = style.positionX * canvasWidth
        val centerY = style.positionY * canvasHeight

        canvas.save()

        if (style.rotation != 0f) {
            canvas.rotate(style.rotation, centerX, centerY)
        }

        if (style.backgroundEnabled) {
            val bgPaint = Paint().apply {
                color = style.backgroundColor.toInt()
            }
            val padding = style.backgroundPadding * canvasHeight / 1080f
            val corners = style.backgroundCorners * canvasHeight / 1080f
            val bgRect = RectF(
                centerX - textWidth / 2f - padding,
                centerY - textHeight / 2f - padding,
                centerX + textWidth / 2f + padding,
                centerY + textHeight / 2f + padding
            )
            canvas.drawRoundRect(bgRect, corners, corners, bgPaint)
        }

        val saveCount = canvas.save()
        canvas.translate(centerX - textWidth / 2f, centerY - textHeight / 2f)
        layout.draw(canvas)
        canvas.restoreToCount(saveCount)

        canvas.restore()
    }

    fun calculateTextHeight(text: String, style: TextSettings, canvasWidth: Int, canvasHeight: Int): Float {
        val scaledFontSize = style.fontSize * canvasHeight / 1080f

        val typefaceStyle = when {
            style.isBold && style.isItalic -> Typeface.BOLD_ITALIC
            style.isBold -> Typeface.BOLD
            style.isItalic -> Typeface.ITALIC
            else -> Typeface.NORMAL
        }

        val textPaint = TextPaint().apply {
            textSize = scaledFontSize
            typeface = style.fontFamily.typeface(typefaceStyle)
            letterSpacing = style.letterSpacing
            isAntiAlias = true
        }

        val layoutWidth = (canvasWidth * style.textWidth).toInt()

        val alignment = when (style.alignment) {
            TextAlign.LEFT -> Layout.Alignment.ALIGN_NORMAL
            TextAlign.CENTER -> Layout.Alignment.ALIGN_CENTER
            TextAlign.RIGHT -> Layout.Alignment.ALIGN_OPPOSITE
        }

        val layout = StaticLayout.Builder.obtain(text, 0, text.length, textPaint, layoutWidth)
            .setAlignment(alignment)
            .setLineSpacing(0f, style.lineHeight)
            .setIncludePad(false)
            .build()

        return layout.height.toFloat()
    }

    fun autoFitFontSize(text: String, style: TextSettings, canvasWidth: Int, canvasHeight: Int): TextSettings {
        val maxHeight = canvasHeight * 0.4f

        val baseStyle = style.copy(
            isBold = style.isBold,
            isItalic = style.isItalic,
            fontFamily = style.fontFamily
        )

        var low = baseStyle.minFontSize
        var high = baseStyle.maxFontSize
        var bestSize = low

        val maxIterations = 20
        var iteration = 0

        while (low <= high && iteration < maxIterations) {
            iteration++
            val mid = (low + high) / 2f
            val testStyle = baseStyle.copy(fontSize = mid)
            val textHeight = calculateTextHeight(text, testStyle, canvasWidth, canvasHeight)

            if (textHeight <= maxHeight) {
                bestSize = mid
                low = mid + 1f
            } else {
                high = mid - 1f
            }
        }

        return baseStyle.copy(fontSize = bestSize)
    }
}
