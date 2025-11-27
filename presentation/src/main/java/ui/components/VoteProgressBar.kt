package ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import ui.theme.RatingHigh
import ui.theme.RatingLow
import ui.theme.RatingMedium

@Composable
fun VoteProgressBar(vote: Double , votePercentageColor: Color = MaterialTheme.colorScheme.onSurface) {

    val votePercentage = (vote * 10).coerceIn(0.0, 100.0)

    val progressColor = when {
        votePercentage < 33 -> RatingLow
        votePercentage < 66 -> RatingMedium
        else -> RatingHigh
    }

    Box(contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            progress = { votePercentage.toFloat() / 100f },
            modifier = Modifier.size(50.dp),
            color = progressColor,
            strokeWidth = 4.dp,
            strokeCap = StrokeCap.Round,
        )
        Text(
            text = "${votePercentage.toInt()}%",
            style = MaterialTheme.typography.bodySmall,
            color = votePercentageColor
        )
    }
}