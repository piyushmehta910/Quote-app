package com.example.quoteapp.data

object FontCatalog {

    enum class FontCategory(val displayName: String) {
        SERIF("Serif"),
        SANS_SERIF("Sans Serif"),
        DISPLAY("Display"),
        HANDWRITING("Handwriting"),
        MONOSPACE("Monospace"),
        DECORATIVE("Decorative")
    }

    data class FontEntry(
        val displayName: String,
        val googleFontName: String,
        val category: FontCategory
    )

    val fonts: List<FontEntry> = listOf(
        FontEntry("Playfair Display", "Playfair Display", FontCategory.SERIF),
        FontEntry("Merriweather", "Merriweather", FontCategory.SERIF),
        FontEntry("Lora", "Lora", FontCategory.SERIF),
        FontEntry("PT Serif", "PT Serif", FontCategory.SERIF),
        FontEntry("Cormorant Garamond", "Cormorant Garamond", FontCategory.SERIF),
        FontEntry("EB Garamond", "EB Garamond", FontCategory.SERIF),
        FontEntry("Libre Baskerville", "Libre Baskerville", FontCategory.SERIF),

        FontEntry("Montserrat", "Montserrat", FontCategory.SANS_SERIF),
        FontEntry("Raleway", "Raleway", FontCategory.SANS_SERIF),
        FontEntry("Poppins", "Poppins", FontCategory.SANS_SERIF),
        FontEntry("Inter", "Inter", FontCategory.SANS_SERIF),
        FontEntry("Oswald", "Oswald", FontCategory.SANS_SERIF),
        FontEntry("Lato", "Lato", FontCategory.SANS_SERIF),
        FontEntry("Nunito", "Nunito", FontCategory.SANS_SERIF),
        FontEntry("Work Sans", "Work Sans", FontCategory.SANS_SERIF),
        FontEntry("Rubik", "Rubik", FontCategory.SANS_SERIF),
        FontEntry("Quicksand", "Quicksand", FontCategory.SANS_SERIF),
        FontEntry("Josefin Sans", "Josefin Sans", FontCategory.SANS_SERIF),
        FontEntry("Barlow", "Barlow", FontCategory.SANS_SERIF),
        FontEntry("Archivo", "Archivo", FontCategory.SANS_SERIF),

        FontEntry("Lobster", "Lobster", FontCategory.DISPLAY),
        FontEntry("Abril Fatface", "Abril Fatface", FontCategory.DISPLAY),
        FontEntry("Righteous", "Righteous", FontCategory.DISPLAY),
        FontEntry("Bebas Neue", "Bebas Neue", FontCategory.DISPLAY),
        FontEntry("Passion One", "Passion One", FontCategory.DISPLAY),
        FontEntry("Bungee", "Bungee", FontCategory.DISPLAY),

        FontEntry("Dancing Script", "Dancing Script", FontCategory.HANDWRITING),
        FontEntry("Pacifico", "Pacifico", FontCategory.HANDWRITING),
        FontEntry("Caveat", "Caveat", FontCategory.HANDWRITING),
        FontEntry("Satisfy", "Satisfy", FontCategory.HANDWRITING),
        FontEntry("Great Vibes", "Great Vibes", FontCategory.HANDWRITING),
        FontEntry("Sacramento", "Sacramento", FontCategory.HANDWRITING),

        FontEntry("Fira Code", "Fira Code", FontCategory.MONOSPACE),
        FontEntry("Space Mono", "Space Mono", FontCategory.MONOSPACE),
        FontEntry("JetBrains Mono", "JetBrains Mono", FontCategory.MONOSPACE),

        FontEntry("Permanent Marker", "Permanent Marker", FontCategory.DECORATIVE),
        FontEntry("Press Start 2P", "Press Start 2P", FontCategory.DECORATIVE),
        FontEntry("Orbitron", "Orbitron", FontCategory.DECORATIVE),
        FontEntry("Russo One", "Russo One", FontCategory.DECORATIVE),
        FontEntry("Black Ops One", "Black Ops One", FontCategory.DECORATIVE),
    )

    fun getByCategory(category: FontCategory): List<FontEntry> =
        fonts.filter { it.category == category }

    fun search(query: String): List<FontEntry> {
        if (query.isBlank()) return fonts
        val lower = query.lowercase()
        return fonts.filter {
            it.displayName.lowercase().contains(lower) ||
                it.googleFontName.lowercase().contains(lower) ||
                it.category.displayName.lowercase().contains(lower)
        }
    }

    fun getByName(name: String): FontEntry? = fonts.find { it.googleFontName == name }

    fun getAll(): List<FontEntry> = fonts
}
