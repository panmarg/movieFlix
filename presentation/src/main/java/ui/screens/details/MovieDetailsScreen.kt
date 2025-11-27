package ui.screens.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.presentation.R
import ui.components.AsyncImageWithPlaceholder

data class MovieDetails(
    val title: String,
    val year: Int,
    val rating: Float,
    val duration: String,
    val genres: List<String>,
    val synopsis: String,
    val cast: List<Actor>,
    val reviews: List<Review>,
    val similarMovies: List<SimilarMovie>,
    val posterUrl: String
)

data class Actor(
    val name: String,
    val profileUrl: String
)

data class Review(
    val author: String,
    val avatarUrl: String,
    val rating: Float,
    val content: String
)

data class SimilarMovie(
    val title: String,
    val year: Int,
    val posterUrl: String
)

@Composable
fun MovieDetailsScreen(movie: MovieDetails, movieId: Int, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        MovieHeader(movie)
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Synopsis", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(movie.synopsis, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            CastSection(movie.cast)
            Spacer(modifier = Modifier.height(16.dp))
            ReviewsSection(movie.reviews)
            Spacer(modifier = Modifier.height(16.dp))
            SimilarMoviesSection(movie.similarMovies)
        }
    }
}

@Composable
fun MovieHeader(movie: MovieDetails) {
    Box(modifier = Modifier.height(300.dp)) {
        AsyncImageWithPlaceholder(
            imageUrl = movie.posterUrl,
            contentDescription = movie.title,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { /* Handle back */ }) {
                    Icon(painter = painterResource(R.drawable.ic_back_arrow), contentDescription = "Back", tint = Color.White)
                }
                IconButton(onClick = { /* Handle share */ }) {
                    Icon(painter = painterResource(R.drawable.ic_back_arrow), contentDescription = "Share", tint = Color.White)
                }
            }
            Column {
                Text(movie.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("${movie.year}", style = MaterialTheme.typography.bodyMedium, color = Color.White)
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(painterResource(R.drawable.ic_back_arrow), contentDescription = "Rating", tint = MaterialTheme.colorScheme.tertiary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${movie.rating}", style = MaterialTheme.typography.bodyMedium, color = Color.White)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(movie.duration, style = MaterialTheme.typography.bodyMedium, color = Color.White)
                }
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(movie.genres) { genre ->
                        Card(shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f))) {
                            Text(genre, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), style = MaterialTheme.typography.bodySmall, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CastSection(cast: List<Actor>) {
    Column {
        Text("Cast", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(cast) { actor ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AsyncImageWithPlaceholder(imageUrl = actor.profileUrl, contentDescription = actor.name, modifier = Modifier.size(64.dp).clip(CircleShape))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(actor.name, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
fun ReviewsSection(reviews: List<Review>) {
    Column {
        Text("Reviews", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            reviews.forEach { review ->
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AsyncImageWithPlaceholder(imageUrl = review.avatarUrl, contentDescription = review.author, modifier = Modifier.size(40.dp).clip(CircleShape))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(review.author, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(painterResource(R.drawable.ic_back_arrow), contentDescription = "Rating", tint = MaterialTheme.colorScheme.tertiary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("${review.rating}", style = MaterialTheme.typography.bodyMedium)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(review.content, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
fun SimilarMoviesSection(movies: List<SimilarMovie>) {
    Column {
        Text("Similar Movies", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(movies) { movie ->
                Card(shape = RoundedCornerShape(8.dp)) {
                    Column {
                        AsyncImageWithPlaceholder(imageUrl = movie.posterUrl, contentDescription = movie.title, modifier = Modifier.height(180.dp).width(120.dp))
                        Column(modifier = Modifier.padding(8.dp)) {
                             Text(movie.title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, maxLines = 1)
                             Text("${movie.year}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}

