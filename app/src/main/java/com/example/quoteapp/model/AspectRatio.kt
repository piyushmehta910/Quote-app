package com.example.quoteapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CanvasSize(
    val width: Int,
    val height: Int
) : Parcelable {
    val aspectRatio: Float get() = width.toFloat() / height.toFloat()

    companion object {
        fun fromAspectRatio(ratio: AspectRatio, baseSize: Int = 1080): CanvasSize {
            val w = (baseSize * ratio.widthRatio / ratio.gcd).toInt()
            val h = (baseSize * ratio.heightRatio / ratio.gcd).toInt()
            return CanvasSize(w, h)
        }
    }
}

@Parcelize
data class AspectRatio(
    val id: String,
    val displayName: String,
    val widthRatio: Int,
    val heightRatio: Int,
    val category: RatioCategory,
    val presetName: String? = null
) : Parcelable {
    val gcd: Int get() = gcd(widthRatio, heightRatio)
    val label: String get() = "$widthRatio:$heightRatio"

    private fun gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)
}

enum class RatioCategory(val displayName: String) {
    SOCIAL("Social"),
    VIDEO("Video"),
    CUSTOM("Custom")
}

object AspectRatios {
    val instagramPost = AspectRatio("ig_post", "Instagram Post", 1, 1, RatioCategory.SOCIAL, "Instagram Post")
    val instagramPortrait = AspectRatio("ig_portrait", "Instagram Portrait", 4, 5, RatioCategory.SOCIAL, "Instagram Portrait")
    val instagramStory = AspectRatio("ig_story", "Instagram Story", 9, 16, RatioCategory.SOCIAL, "Instagram Story")
    val instagramReel = AspectRatio("ig_reel", "Instagram Reel", 9, 16, RatioCategory.VIDEO, "Instagram Reel")
    val youtubeThumbnail = AspectRatio("yt_thumb", "YouTube Thumbnail", 16, 9, RatioCategory.VIDEO, "YouTube Thumbnail")
    val youtubeShorts = AspectRatio("yt_shorts", "YouTube Shorts", 9, 16, RatioCategory.VIDEO, "YouTube Shorts")
    val facebookPost = AspectRatio("fb_post", "Facebook Post", 4, 3, RatioCategory.SOCIAL, "Facebook Post")
    val facebookStory = AspectRatio("fb_story", "Facebook Story", 9, 16, RatioCategory.SOCIAL, "Facebook Story")
    val whatsappStatus = AspectRatio("wa_status", "WhatsApp Status", 9, 16, RatioCategory.SOCIAL, "WhatsApp Status")
    val tiktok = AspectRatio("tiktok", "TikTok", 9, 16, RatioCategory.VIDEO, "TikTok")
    val xPost = AspectRatio("x_post", "X Post", 16, 9, RatioCategory.SOCIAL, "X Post")
    val linkedinPost = AspectRatio("linkedin", "LinkedIn Post", 4, 5, RatioCategory.SOCIAL, "LinkedIn Post")

    val square = AspectRatio("1_1", "1:1", 1, 1, RatioCategory.SOCIAL)
    val portrait45 = AspectRatio("4_5", "4:5", 4, 5, RatioCategory.SOCIAL)
    val portrait34 = AspectRatio("3_4", "3:4", 3, 4, RatioCategory.SOCIAL)
    val landscape43 = AspectRatio("4_3", "4:3", 4, 3, RatioCategory.SOCIAL)
    val landscape169 = AspectRatio("16_9", "16:9", 16, 9, RatioCategory.VIDEO)
    val portrait916 = AspectRatio("9_16", "9:16", 9, 16, RatioCategory.VIDEO)
    val landscape32 = AspectRatio("3_2", "3:2", 3, 2, RatioCategory.SOCIAL)
    val portrait23 = AspectRatio("2_3", "2:3", 2, 3, RatioCategory.SOCIAL)
    val landscape54 = AspectRatio("5_4", "5:4", 5, 4, RatioCategory.SOCIAL)
    val ultrawide21 = AspectRatio("21_9", "21:9", 21, 9, RatioCategory.VIDEO)
    val ultrawide921 = AspectRatio("9_21", "9:21", 9, 21, RatioCategory.VIDEO)

    val presets: List<AspectRatio> = listOf(
        instagramPost, instagramPortrait, instagramStory, instagramReel,
        youtubeThumbnail, youtubeShorts, facebookPost, facebookStory,
        whatsappStatus, tiktok, xPost, linkedinPost
    )

    val ratios: List<AspectRatio> = listOf(
        square, portrait45, portrait34,
        portrait916, landscape169, landscape43,
        landscape32, portrait23, landscape54,
        ultrawide21, ultrawide921
    )

    val all: List<AspectRatio> = presets + ratios.distinctBy { it.label }

    fun default() = instagramPost

    fun fromCanvasSize(size: CanvasSize): AspectRatio {
        val w = size.width
        val h = size.height
        val g = gcd(w, h)
        val rw = w / g
        val rh = h / g
        return all.find { it.widthRatio == rw && it.heightRatio == rh }
            ?: AspectRatio("custom_${w}_$h", "${rw}:${rh}", rw, rh, RatioCategory.CUSTOM)
    }

    private fun gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)
}
