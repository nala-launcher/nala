import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicText
import androidx.compose.ui.text.TextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.isSystemInDarkTheme
import ui.style.Colors

@Composable
fun Screen() {
    val palette = if (isSystemInDarkTheme()) Colors.dark else Colors.light
    MaterialTheme() {
        Column(
            modifier = Modifier.fillMaxSize().background(palette.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) { BasicText("nala", style = TextStyle(color = palette.foreground)) }
    }
}