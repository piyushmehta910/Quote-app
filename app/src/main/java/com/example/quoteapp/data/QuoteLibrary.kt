package com.example.quoteapp.data

import com.example.quoteapp.model.Quote
import com.example.quoteapp.model.QuoteCategory

object QuoteLibrary {

    private val allQuotes: List<Quote> = listOf(
        // MOTIVATION
        Quote("q1", "The only way to do great work is to love what you do.", "Steve Jobs", QuoteCategory.MOTIVATION, false),
        Quote("q2", "It does not matter how slowly you go as long as you do not stop.", "Confucius", QuoteCategory.MOTIVATION, false),
        Quote("q3", "Everything you've ever wanted is on the other side of fear.", "George Addair", QuoteCategory.MOTIVATION, false),
        Quote("q4", "Success is not final, failure is not fatal: it is the courage to continue that counts.", "Winston Churchill", QuoteCategory.MOTIVATION, false),
        Quote("q5", "Believe you can and you're halfway there.", "Theodore Roosevelt", QuoteCategory.MOTIVATION, false),
        Quote("q6", "The future belongs to those who believe in the beauty of their dreams.", "Eleanor Roosevelt", QuoteCategory.MOTIVATION, false),
        Quote("q7", "Don't watch the clock; do what it does. Keep going.", "Sam Levenson", QuoteCategory.MOTIVATION, false),
        Quote("q8", "What lies behind us and what lies before us are tiny matters compared to what lies within us.", "Ralph Waldo Emerson", QuoteCategory.MOTIVATION, false),
        Quote("q9", "The only impossible journey is the one you never begin.", "Tony Robbins", QuoteCategory.MOTIVATION, false),

        // SUCCESS
        Quote("q10", "Success usually comes to those who are too busy to be looking for it.", "Henry David Thoreau", QuoteCategory.SUCCESS, false),
        Quote("q11", "Don't be afraid to give up the good to go for the great.", "John D. Rockefeller", QuoteCategory.SUCCESS, false),
        Quote("q12", "I find that the harder I work, the more luck I seem to have.", "Thomas Jefferson", QuoteCategory.SUCCESS, false),
        Quote("q13", "Success is walking from failure to failure with no loss of enthusiasm.", "Winston Churchill", QuoteCategory.SUCCESS, false),
        Quote("q14", "The secret of success is to do the common thing uncommonly well.", "John D. Rockefeller Jr.", QuoteCategory.SUCCESS, false),
        Quote("q15", "Success is not the key to happiness. Happiness is the key to success.", "Albert Schweitzer", QuoteCategory.SUCCESS, false),
        Quote("q16", "Try not to become a man of success, but rather try to become a man of value.", "Albert Einstein", QuoteCategory.SUCCESS, false),
        Quote("q17", "Success is getting what you want. Happiness is wanting what you get.", "Dale Carnegie", QuoteCategory.SUCCESS, false),
        Quote("q18", "The way to get started is to quit talking and begin doing.", "Walt Disney", QuoteCategory.SUCCESS, false),

        // LIFE
        Quote("q19", "In the middle of difficulty lies opportunity.", "Albert Einstein", QuoteCategory.LIFE, false),
        Quote("q20", "Life is what happens when you're busy making other plans.", "John Lennon", QuoteCategory.LIFE, false),
        Quote("q21", "Get busy living or get busy dying.", "Stephen King", QuoteCategory.LIFE, false),
        Quote("q22", "You have power over your mind, not outside events. Realize this, and you will find strength.", "Marcus Aurelius", QuoteCategory.LIFE, false),
        Quote("q23", "The purpose of our lives is to be happy.", "Dalai Lama", QuoteCategory.LIFE, false),
        Quote("q24", "Many of life's failures are people who did not realize how close they were to success when they gave up.", "Thomas Edison", QuoteCategory.LIFE, false),
        Quote("q25", "Life is really simple, but we insist on making it complicated.", "Confucius", QuoteCategory.LIFE, false),
        Quote("q26", "The unexamined life is not worth living.", "Socrates", QuoteCategory.LIFE, false),
        Quote("q27", "Turn your wounds into wisdom.", "Oprah Winfrey", QuoteCategory.LIFE, false),

        // CONFIDENCE
        Quote("q28", "No one can make you feel inferior without your consent.", "Eleanor Roosevelt", QuoteCategory.CONFIDENCE, false),
        Quote("q29", "To be yourself in a world that is constantly trying to make you something else is the greatest accomplishment.", "Ralph Waldo Emerson", QuoteCategory.CONFIDENCE, false),
        Quote("q30", "Confidence comes not from always being right but from not fearing to be wrong.", "Peter T. McINTyre", QuoteCategory.CONFIDENCE, false),
        Quote("q31", "You yourself, as much as anybody in the entire universe, deserve your love and affection.", "Buddha", QuoteCategory.CONFIDENCE, false),
        Quote("q32", "Act as if what you do makes a difference. It does.", "William James", QuoteCategory.CONFIDENCE, false),
        Quote("q33", "With confidence, you have won even before you have started.", "Marcus Tullius Cicero", QuoteCategory.CONFIDENCE, false),
        Quote("q34", "It is confidence in our bodies, minds, and spirits that allows us to keep looking for new adventures.", "Oprah Winfrey", QuoteCategory.CONFIDENCE, false),

        // STUDY
        Quote("q35", "Live as if you were to die tomorrow. Learn as if you were to live forever.", "Mahatma Gandhi", QuoteCategory.STUDY, false),
        Quote("q36", "An investment in knowledge pays the best interest.", "Benjamin Franklin", QuoteCategory.STUDY, false),
        Quote("q37", "The more that you read, the more things you will know. The more that you learn, the more places you'll go.", "Dr. Seuss", QuoteCategory.STUDY, false),
        Quote("q38", "Education is the most powerful weapon which you can use to change the world.", "Nelson Mandela", QuoteCategory.STUDY, false),
        Quote("q39", "The expert in anything was once a beginner.", "Helen Hayes", QuoteCategory.STUDY, false),
        Quote("q40", "I have no special talents. I am only passionately curious.", "Albert Einstein", QuoteCategory.STUDY, false),
        Quote("q41", "The beautiful thing about learning is that nobody can take it away from you.", "B.B. King", QuoteCategory.STUDY, false),

        // FITNESS
        Quote("q42", "Take care of your body. It's the only place you have to live.", "Jim Rohn", QuoteCategory.FITNESS, false),
        Quote("q43", "The body achieves what the mind believes.", "Napoleon Hill", QuoteCategory.FITNESS, false),
        Quote("q44", "Fitness is not about being better than someone else. It's about being better than you used to be.", "Khloe Kardashian", QuoteCategory.FITNESS, false),
        Quote("q45", "The only bad workout is the one that didn't happen.", "Unknown", QuoteCategory.FITNESS, false),
        Quote("q46", "Strive for progress, not perfection.", "Unknown", QuoteCategory.FITNESS, false),
        Quote("q47", "Your body can stand almost anything. It's your mind that you have to convince.", "Unknown", QuoteCategory.FITNESS, false),
        Quote("q48", "The pain you feel today will be the strength you feel tomorrow.", "Arnold Schwarzenegger", QuoteCategory.FITNESS, false),

        // BUSINESS
        Quote("q49", "Your most unhappy customers are your greatest source of learning.", "Bill Gates", QuoteCategory.BUSINESS, false),
        Quote("q50", "Business has only two functions – marketing and innovation.", "Peter Drucker", QuoteCategory.BUSINESS, false),
        Quote("q51", "The biggest risk is not taking any risk.", "Mark Zuckerberg", QuoteCategory.BUSINESS, false),
        Quote("q52", "Innovation distinguishes between a leader and a follower.", "Steve Jobs", QuoteCategory.BUSINESS, false),
        Quote("q53", "Quality is not an act, it is a habit.", "Aristotle", QuoteCategory.BUSINESS, false),
        Quote("q54", "The way to get started is to quit talking and begin doing.", "Walt Disney", QuoteCategory.BUSINESS, false),
        Quote("q55", "Don't be afraid to give up the good to go for the great.", "John D. Rockefeller", QuoteCategory.BUSINESS, false),

        // LOVE
        Quote("q56", "The best thing to hold onto in life is each other.", "Audrey Hepburn", QuoteCategory.LOVE, false),
        Quote("q57", "I have decided to stick with love. Hate is too great a burden to bear.", "Martin Luther King Jr.", QuoteCategory.LOVE, false),
        Quote("q58", "Where there is love there is life.", "Mahatma Gandhi", QuoteCategory.LOVE, false),
        Quote("q59", "Love is composed of a single soul inhabiting two bodies.", "Aristotle", QuoteCategory.LOVE, false),
        Quote("q60", "The greatest thing you'll ever learn is just to love and be loved in return.", "Eden Ahbez", QuoteCategory.LOVE, false),
        Quote("q61", "We are most alive when we're in love.", "John Updike", QuoteCategory.LOVE, false),
        Quote("q62", "Love is not about how many days, months, or years you've been together. It's all about how much you love each other every day.", "Unknown", QuoteCategory.LOVE, false),

        // FRIENDSHIP
        Quote("q63", "A real friend is one who walks in when the rest of the world walks out.", "Walter Winchell", QuoteCategory.FRIENDSHIP, false),
        Quote("q64", "Friendship is born at that moment when one person says to another, 'What! You too? I thought I was the only one.'", "C.S. Lewis", QuoteCategory.FRIENDSHIP, false),
        Quote("q65", "A friend is someone who knows all about you and still loves you.", "Elbert Hubbard", QuoteCategory.FRIENDSHIP, false),
        Quote("q66", "True friendship comes when the silence between two people is comfortable.", "David Tyson", QuoteCategory.FRIENDSHIP, false),
        Quote("q67", "The language of friendship is not words but meanings.", "Henry David Thoreau", QuoteCategory.FRIENDSHIP, false),
        Quote("q68", "Friendship is the only cement that will ever hold the world together.", "Woodrow Wilson", QuoteCategory.FRIENDSHIP, false),
        Quote("q69", "A single soul dwelling in two bodies.", "Aristotle", QuoteCategory.FRIENDSHIP, false),
        Quote("q70", "There is nothing I would not do for those who are really my friends.", "Jane Austen", QuoteCategory.FRIENDSHIP, false)
    )

    fun getByCategory(category: QuoteCategory): List<Quote> {
        return allQuotes.filter { it.category == category }
    }

    fun getRandom(): Quote {
        return allQuotes.random()
    }

    fun getAll(): List<Quote> {
        return allQuotes
    }

    fun search(query: String): List<Quote> {
        val lowerCaseQuery = query.lowercase()
        return allQuotes.filter {
            it.text.lowercase().contains(lowerCaseQuery) ||
                    it.author.lowercase().contains(lowerCaseQuery)
        }
    }

    fun getFavorites(quotes: List<Quote>): List<Quote> {
        return quotes.filter { it.isFavorite }
    }
}
