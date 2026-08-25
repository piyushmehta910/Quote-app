package com.example.quoteapp.data

import com.example.quoteapp.model.GradientColorStop
import com.example.quoteapp.model.GradientType
import com.example.quoteapp.model.QuoteBackground

object GradientPresets {

    data class GradientPreset(
        val id: String,
        val name: String,
        val category: String,
        val background: QuoteBackground.Gradient
    )

    private val presets = listOf(
        GradientPreset("sunset", "Sunset", "Warm",
            QuoteBackground.Gradient(listOf(0xFFFA709A, 0xFFFEE140), 180f, GradientType.LINEAR)),
        GradientPreset("fire", "Fire", "Warm",
            QuoteBackground.Gradient(listOf(0xFFF12711, 0xFFF5AF19), 135f, GradientType.LINEAR)),
        GradientPreset("peach", "Peach", "Warm",
            QuoteBackground.Gradient(listOf(0xFFED4264, 0xFFFFEDBC), 180f, GradientType.LINEAR)),
        GradientPreset("warmSun", "Warm Sun", "Warm",
            QuoteBackground.Gradient(listOf(0xFFF7971E, 0xFFFFD200), 135f, GradientType.LINEAR)),
        GradientPreset("coral", "Coral", "Warm",
            QuoteBackground.Gradient(listOf(0xFFFF6B6B, 0xFFFFE66D), 180f, GradientType.LINEAR)),
        GradientPreset("autumn", "Autumn", "Warm",
            QuoteBackground.Gradient(listOf(0xFFE65C00, 0xFFF9D423), 135f, GradientType.LINEAR)),

        GradientPreset("ocean", "Ocean", "Cool",
            QuoteBackground.Gradient(listOf(0xFF4FACFE, 0xFF00F2FE), 180f, GradientType.LINEAR)),
        GradientPreset("ice", "Ice", "Cool",
            QuoteBackground.Gradient(listOf(0xFF43E97B, 0xFF38F9D7), 135f, GradientType.LINEAR)),
        GradientPreset("deepBlue", "Deep Blue", "Cool",
            QuoteBackground.Gradient(listOf(0xFF667EEA, 0xFF764BA2), 135f, GradientType.LINEAR)),
        GradientPreset("aqua", "Aqua", "Cool",
            QuoteBackground.Gradient(listOf(0xFF00D2FF, 0xFF3A7BD5), 180f, GradientType.LINEAR)),
        GradientPreset("frost", "Frost", "Cool",
            QuoteBackground.Gradient(listOf(0xFFE0EAFC, 0xFFCFDEF3), 180f, GradientType.LINEAR)),
        GradientPreset("teal", "Teal", "Cool",
            QuoteBackground.Gradient(listOf(0xFF0ABFBC, 0xFFFC00FF), 135f, GradientType.LINEAR)),

        GradientPreset("midnight", "Midnight", "Dark",
            QuoteBackground.Gradient(listOf(0xFF0C0C1D, 0xFF1A1A3E, 0xFF2D1B69), 180f, GradientType.LINEAR)),
        GradientPreset("darkSlate", "Dark Slate", "Dark",
            QuoteBackground.Gradient(listOf(0xFF141E30, 0xFF243B55), 135f, GradientType.LINEAR)),
        GradientPreset("void", "Void", "Dark",
            QuoteBackground.Gradient(listOf(0xFF0F2027, 0xFF2C5364), 180f, GradientType.LINEAR)),
        GradientPreset("noir", "Noir", "Dark",
            QuoteBackground.Gradient(listOf(0xFF1F1C2C, 0xFF928DAB), 135f, GradientType.LINEAR)),
        GradientPreset("shadow", "Shadow", "Dark",
            QuoteBackground.Gradient(listOf(0xFF0B0C10, 0xFF1F1C2C), 180f, GradientType.LINEAR)),
        GradientPreset("obsidian", "Obsidian", "Dark",
            QuoteBackground.Gradient(listOf(0xFF232526, 0xFF414345), 180f, GradientType.LINEAR)),

        GradientPreset("neonPink", "Neon Pink", "Vibrant",
            QuoteBackground.Gradient(listOf(0xFFF093FB, 0xFFF5576C), 135f, GradientType.LINEAR)),
        GradientPreset("electricPurple", "Electric Purple", "Vibrant",
            QuoteBackground.Gradient(listOf(0xFFFC466B, 0xFF3F5EFB), 135f, GradientType.LINEAR)),
        GradientPreset("magenta", "Magenta", "Vibrant",
            QuoteBackground.Gradient(listOf(0xFFBC4E9C, 0xFFF80759), 180f, GradientType.LINEAR)),
        GradientPreset("candy", "Candy", "Vibrant",
            QuoteBackground.Gradient(listOf(0xFFFC5C7D, 0xFF6A82FB), 135f, GradientType.LINEAR)),
        GradientPreset("vivid", "Vivid", "Vibrant",
            QuoteBackground.Gradient(listOf(0xFFF7971E, 0xFFFFD200, 0xFFE44D26), 135f, GradientType.LINEAR)),

        GradientPreset("blush", "Blush", "Pastel",
            QuoteBackground.Gradient(listOf(0xFFFBC2EB, 0xFFA6C1EE), 135f, GradientType.LINEAR)),
        GradientPreset("lavender", "Lavender", "Pastel",
            QuoteBackground.Gradient(listOf(0xFFC9D6FF, 0xFFE2E2E2), 180f, GradientType.LINEAR)),
        GradientPreset("cotton", "Cotton", "Pastel",
            QuoteBackground.Gradient(listOf(0xFFFFDEE9, 0xFFB5FFFC), 135f, GradientType.LINEAR)),
        GradientPreset("dream", "Dream", "Pastel",
            QuoteBackground.Gradient(listOf(0xFFEEDDFF, 0xFFFCB6FF), 180f, GradientType.LINEAR)),
        GradientPreset("rose", "Rose", "Pastel",
            QuoteBackground.Gradient(listOf(0xFFFFB6C1, 0xFFFF69B4), 180f, GradientType.LINEAR)),

        GradientPreset("neonGreen", "Neon Green", "Neon",
            QuoteBackground.Gradient(listOf(0xFF00FF87, 0xFF60EFFF), 135f, GradientType.LINEAR)),
        GradientPreset("cyberPunk", "Cyber Punk", "Neon",
            QuoteBackground.Gradient(listOf(0xFF00F260, 0xFF0575E6), 180f, GradientType.LINEAR)),
        GradientPreset("toxic", "Toxic", "Neon",
            QuoteBackground.Gradient(listOf(0xFFFC00FF, 0xFF00DBDE), 135f, GradientType.LINEAR)),
        GradientPreset("laser", "Laser", "Neon",
            QuoteBackground.Gradient(listOf(0xFFD4AF37, 0xFFE94560), 135f, GradientType.LINEAR)),

        GradientPreset("forest", "Forest", "Nature",
            QuoteBackground.Gradient(listOf(0xFF134E5E, 0xFF71B280), 180f, GradientType.LINEAR)),
        GradientPreset("aurora", "Aurora", "Nature",
            QuoteBackground.Gradient(listOf(0xFF00C9FF, 0xFF92FE9D), 135f, GradientType.LINEAR)),
        GradientPreset("earth", "Earth", "Nature",
            QuoteBackground.Gradient(listOf(0xFF3C1053, 0xFFAD5389), 180f, GradientType.LINEAR)),
        GradientPreset("meadow", "Meadow", "Nature",
            QuoteBackground.Gradient(listOf(0xFF56AB2F, 0xFFA8E063), 135f, GradientType.LINEAR)),
        GradientPreset("sky", "Sky", "Nature",
            QuoteBackground.Gradient(listOf(0xFF2193B0, 0xFF6DD5ED), 180f, GradientType.LINEAR)),

        GradientPreset("radialPurple", "Radial Purple", "Radial",
            QuoteBackground.Gradient(listOf(0xFF667EEA, 0xFF764BA2), 0f, GradientType.RADIAL)),
        GradientPreset("radialSunset", "Radial Sunset", "Radial",
            QuoteBackground.Gradient(listOf(0xFFF093FB, 0xFFF5576C), 0f, GradientType.RADIAL)),
        GradientPreset("radialOcean", "Radial Ocean", "Radial",
            QuoteBackground.Gradient(listOf(0xFF4FACFE, 0xFF00F2FE), 0f, GradientType.RADIAL)),

        GradientPreset("sweepRainbow", "Sweep Rainbow", "Sweep",
            QuoteBackground.Gradient(listOf(0xFFFF0000, 0xFFFF8800, 0xFFFFFF00, 0xFF00FF00, 0xFF0088FF, 0xFF8800FF, 0xFFFF0000), 0f, GradientType.SWEEP)),
    )

    fun getAll(): List<GradientPreset> = presets
    fun getByCategory(category: String): List<GradientPreset> = presets.filter { it.category == category }
    fun getCategories(): List<String> = presets.map { it.category }.distinct()
    fun getById(id: String): GradientPreset? = presets.find { it.id == id }
}
