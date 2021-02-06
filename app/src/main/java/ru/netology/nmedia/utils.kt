package ru.netology.nmedia

public fun numberToK(number: Int) =
        when {
            (number >= 0 && number < 1_000) -> number.toString()
            (number > 999 && number < 10_000) -> String.format("%.1fK", number / 1_000.0)
            (number > 9_999 && number < 1_000_000) -> String.format("%.0fK", number / 1_000.0)
            (number > 999_999 && number < 1_000_000_000) -> String.format("%.1fM", number / 1_000_000.0)
            else -> "∞"
        }

