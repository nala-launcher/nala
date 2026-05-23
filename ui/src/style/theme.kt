package ui.style

import androidx.compose.ui.graphics.Color

import ui.style.color.Base
import ui.style.color.Red
import ui.style.color.Zinc

data class Palette(
    val background: Color,
    val foreground: Color,
    val primary: Color,
    val primaryForeground: Color,
    val secondary: Color,
    val secondaryForeground: Color,
    val muted: Color,
    val mutedForeground: Color,
    val accent: Color,
    val accentForeground: Color,
    val destructive: Color,
    val destructiveForeground: Color,
    val card: Color,
    val cardForeground: Color,
    val popover: Color,
    val popoverForeground: Color,
    val border: Color,
    val input: Color,
    val ring: Color
)

object Colors {

    val light = Palette(
        background = Base.white,
        foreground = Zinc.shade950,
        primary = Zinc.shade900,
        primaryForeground = Zinc.shade50,
        secondary = Zinc.shade100,
        secondaryForeground = Zinc.shade900,
        muted = Zinc.shade100,
        mutedForeground = Zinc.shade500,
        accent = Zinc.shade100,
        accentForeground = Zinc.shade900,
        destructive = Red.shade500,
        destructiveForeground = Zinc.shade50,
        card = Base.white,
        cardForeground = Zinc.shade950,
        popover = Base.white,
        popoverForeground = Zinc.shade950,
        border = Zinc.shade200,
        input = Zinc.shade200,
        ring = Zinc.shade900
    )

    val dark = Palette(
        background = Zinc.shade950,
        foreground = Zinc.shade50,
        primary = Zinc.shade50,
        primaryForeground = Zinc.shade900,
        secondary = Zinc.shade800,
        secondaryForeground = Zinc.shade50,
        muted = Zinc.shade800,
        mutedForeground = Zinc.shade400,
        accent = Zinc.shade800,
        accentForeground = Zinc.shade50,
        destructive = Red.shade900,
        destructiveForeground = Zinc.shade50,
        card = Zinc.shade950,
        cardForeground = Zinc.shade50,
        popover = Zinc.shade950,
        popoverForeground = Zinc.shade50,
        border = Zinc.shade800,
        input = Zinc.shade800,
        ring = Zinc.shade300
    )
}