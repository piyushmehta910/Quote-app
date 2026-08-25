package com.example.quoteapp.data

import com.example.quoteapp.model.QuoteBackground

object PngBackgroundLibrary {

    data class PngEntry(
        val id: String,
        val name: String,
        val category: String,
        val assetPath: String,
        val tags: List<String> = emptyList()
    )

    private val backgrounds = listOf(
        PngEntry("marble_light", "Light Marble", "Texture", "backgrounds/marble_light.png", listOf("marble", "light", "elegant")),
        PngEntry("marble_dark", "Dark Marble", "Texture", "backgrounds/marble_dark.png", listOf("marble", "dark", "luxury")),
        PngEntry("paper_warm", "Warm Paper", "Texture", "backgrounds/paper_warm.png", listOf("paper", "warm", "vintage")),
        PngEntry("paper_cool", "Cool Paper", "Texture", "backgrounds/paper_cool.png", listOf("paper", "cool", "clean")),
        PngEntry("linen", "Linen", "Texture", "backgrounds/linen.png", listOf("linen", "fabric", "soft")),
        PngEntry("concrete", "Concrete", "Texture", "backgrounds/concrete.png", listOf("concrete", "urban", "raw")),
        PngEntry("wood_light", "Light Wood", "Texture", "backgrounds/wood_light.png", listOf("wood", "light", "natural")),
        PngEntry("wood_dark", "Dark Wood", "Texture", "backgrounds/wood_dark.png", listOf("wood", "dark", "rich")),

        PngEntry("bokeh_warm", "Warm Bokeh", "Abstract", "backgrounds/bokeh_warm.png", listOf("bokeh", "warm", "soft")),
        PngEntry("bokeh_cool", "Cool Bokeh", "Abstract", "backgrounds/bokeh_cool.png", listOf("bokeh", "cool", "soft")),
        PngEntry("abstract_wave", "Abstract Wave", "Abstract", "backgrounds/abstract_wave.png", listOf("abstract", "wave", "flow")),
        PngEntry("abstract_lines", "Abstract Lines", "Abstract", "backgrounds/abstract_lines.png", listOf("abstract", "lines", "geometric")),
        PngEntry("geometric_pattern", "Geometric Pattern", "Abstract", "backgrounds/geometric_pattern.png", listOf("geometric", "pattern", "modern")),
        PngEntry("gradient_mesh", "Gradient Mesh", "Abstract", "backgrounds/gradient_mesh.png", listOf("gradient", "mesh", "colorful")),

        PngEntry("sunset_clouds", "Sunset Clouds", "Nature", "backgrounds/sunset_clouds.png", listOf("sunset", "clouds", "warm")),
        PngEntry("ocean_waves", "Ocean Waves", "Nature", "backgrounds/ocean_waves.png", listOf("ocean", "waves", "blue")),
        PngEntry("forest_mist", "Forest Mist", "Nature", "backgrounds/forest_mist.png", listOf("forest", "mist", "green")),
        PngEntry("starry_sky", "Starry Sky", "Nature", "backgrounds/starry_sky.png", listOf("stars", "night", "sky")),
        PngEntry("mountain_fog", "Mountain Fog", "Nature", "backgrounds/mountain_fog.png", listOf("mountain", "fog", "moody")),
        PngEntry("cherry_blossoms", "Cherry Blossoms", "Nature", "backgrounds/cherry_blossoms.png", listOf("cherry", "blossoms", "pink")),

        PngEntry("dark_grain", "Dark Grain", "Dark", "backgrounds/dark_grain.png", listOf("dark", "grain", "minimal")),
        PngEntry("noir_texture", "Noir Texture", "Dark", "backgrounds/noir_texture.png", listOf("noir", "dark", "cinematic")),
        PngEntry("dark_urban", "Dark Urban", "Dark", "backgrounds/dark_urban.png", listOf("dark", "urban", "moody")),
        PngEntry("midnight_stars", "Midnight Stars", "Dark", "backgrounds/midnight_stars.png", listOf("midnight", "stars", "space")),

        PngEntry("pastel_gradient", "Pastel Gradient", "Gradient", "backgrounds/pastel_gradient.png", listOf("pastel", "soft", "colorful")),
        PngEntry("warm_gradient", "Warm Gradient", "Gradient", "backgrounds/warm_gradient.png", listOf("warm", "orange", "gradient")),
        PngEntry("cool_gradient", "Cool Gradient", "Gradient", "backgrounds/cool_gradient.png", listOf("cool", "blue", "gradient")),
        PngEntry("neon_glow", "Neon Glow", "Gradient", "backgrounds/neon_glow.png", listOf("neon", "glow", "vibrant")),

        PngEntry("minimal_white", "Minimal White", "Minimal", "backgrounds/minimal_white.png", listOf("minimal", "white", "clean")),
        PngEntry("minimal_gray", "Minimal Gray", "Minimal", "backgrounds/minimal_gray.png", listOf("minimal", "gray", "subtle")),
        PngEntry("minimal_black", "Minimal Black", "Minimal", "backgrounds/minimal_black.png", listOf("minimal", "black", "simple")),
        PngEntry("clean_lines", "Clean Lines", "Minimal", "backgrounds/clean_lines.png", listOf("clean", "lines", "structured")),
    )

    fun getAll(): List<PngEntry> = backgrounds
    fun getByCategory(category: String): List<PngEntry> = backgrounds.filter { it.category == category }
    fun getCategories(): List<String> = backgrounds.map { it.category }.distinct()
    fun getById(id: String): PngEntry? = backgrounds.find { it.id == id }
    fun search(query: String): List<PngEntry> {
        if (query.isBlank()) return backgrounds
        val lower = query.lowercase()
        return backgrounds.filter {
            it.name.lowercase().contains(lower) ||
                it.category.lowercase().contains(lower) ||
                it.tags.any { tag -> tag.lowercase().contains(lower) }
        }
    }
}
