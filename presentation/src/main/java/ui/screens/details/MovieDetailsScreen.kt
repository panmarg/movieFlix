package ui.screens.details

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.presentation.R
import model.Cast
import model.MovieDetails
import model.Review
import model.SimilarMovie
import org.koin.compose.viewmodel.koinViewModel
import ui.ContentState
import ui.components.AsyncImageWithPlaceholder
import ui.components.ErrorSection
import ui.components.VoteProgressBar
import ui.components.details.FullScreenLoader
import utils.toUiDate

@Composable
fun MovieDetailsScreen(
    movieId: Int?,
    onBack: () -> Unit,
    viewModel: MovieDetailsViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(movieId) {
        if(movieId != null){
            viewModel.onIntent(MovieDetailsIntent.SetMovieId(movieId))
        }else{
            viewModel.onIntent(MovieDetailsIntent.OnReceiveEmptyMovieId)
        }

    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MovieDetailsEffect.GoBack -> {
                    onBack.invoke()
                }

                is MovieDetailsEffect.GoToShareMovie -> {
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, state.movieDetails.homepage)
                        type = "text/plain"
                    }
                    try {
                        context.startActivity(Intent.createChooser(sendIntent, "Share via"))
                    }catch (e: Exception){
                        snackbarHostState.showSnackbar(
                            e.message.toString()
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(state) {
        Log.d(
            "MovieDetailsScreen",
            "Screen movieIdFromArgs=${state.movieIdFromArgs}, \n" +
                    "movieUrl=${state.movieUrl}, \n" +
                    "contentState=${state.contentState}, \n" +
                    "id=${state.movieDetails.id}, \n" +
                    "overview=${state.movieDetails.overview}, \n" +
                    "runtime=${state.movieDetails.runtime}, \n" +
                    "cast=${state.movieDetails.cast}, \n" +
                    "reviews=${state.movieDetails.reviews}, \n" +
                    "similarMovies=${state.movieDetails.similarMovies}, \n" +
                    "genres=${state.movieDetails.genres}, \n"
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize().padding(paddingValues)){
            when(state.contentState){
                is ContentState.Error -> ErrorSection(
                    message = (state.contentState as ContentState.Error).message,
                    onRetry = {
                        viewModel.onIntent(MovieDetailsIntent.OnGoBackClick)
                    }
                )
                is ContentState.Loading -> FullScreenLoader()
                is ContentState.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .verticalScroll(rememberScrollState())
                    ) {
                        if(state.showHeaderSection){
                            MovieHeader(
                                movie = state.movieDetails,
                                goBack = { viewModel.onIntent(MovieDetailsIntent.OnGoBackClick) },
                                onFavoriteClick = {
                                    viewModel.onIntent(
                                        MovieDetailsIntent.ToggleFavorite(
                                            state.movieIdFromArgs ?: 0
                                        )
                                    )
                                }
                            )
                        }
                        Column(modifier = Modifier.padding(16.dp)) {
                            if (state.showShareSection) {
                                ShareSection(onShareClick = {
                                    viewModel.onIntent(
                                        MovieDetailsIntent.OnShareClick
                                    )
                                })
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                            if (state.showOverviewSection){
                                Text(
                                    "Overview",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    state.movieDetails.overview ?: "",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                            if (state.showCastSection){
                                Spacer(modifier = Modifier.height(16.dp))
                                CastSection(state.movieDetails.cast ?: listOf())
                            }
                            if (state.showReviewsSection){
                                Spacer(modifier = Modifier.height(16.dp))
                                ReviewsSection(state.movieDetails.reviews ?: listOf())
                            }
                            if (state.showSimilarMoviesSection){
                                Spacer(modifier = Modifier.height(16.dp))
                                SimilarMoviesSection(state.movieDetails.similarMovies ?: listOf())
                            }
                        }
                    }
                }
                else -> Unit
            }

        }
    }
}

@Composable
fun ShareSection(onShareClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onShareClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_share),
                contentDescription = "Share",
                tint = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Share",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun MovieHeader(
    movie: MovieDetails,
    goBack: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Box(modifier = Modifier.height(300.dp)) {
        AsyncImageWithPlaceholder(
            imageUrl = movie.posterPath,
            contentDescription = movie.title,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.8f)
                        )
                    )
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { goBack.invoke() }) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.ic_back_arrow),
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                IconButton(onClick = { onFavoriteClick.invoke() }) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(if (movie.isFavorite) R.drawable.ic_heart_fill else R.drawable.ic_heart),
                        contentDescription = "Favorite",
                        tint = if (movie.isFavorite) MaterialTheme.colorScheme.primary else Color.White
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        movie.title ?: "",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            movie.releaseDate?.toUiDate().orEmpty(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                        movie.runtime?.let { runtime ->
                            if(runtime != 0){
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    "$runtime mins",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White
                                )
                            }
                        }

                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        movie.genres?.forEach { genre ->
                            Card(
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                                )
                            ) {
                                Text(
                                    genre,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
                movie.voteAverage?.let {
                    VoteProgressBar(vote = it, votePercentageColor = Color.White)
                }
            }
        }
    }
}

@Composable
fun CastSection(cast: List<Cast>) {
    Column {
        Text(
            "Cast", style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(cast) { actor ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AsyncImageWithPlaceholder(
                        imageUrl = actor.profilePath,
                        contentDescription = actor.name,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(actor.name, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground)
                }
            }
        }
    }
}

@Composable
fun ReviewsSection(reviews: List<Review>) {
    Column {
        Text("Reviews", style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.height(8.dp))
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            reviews.forEach { review ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AsyncImageWithPlaceholder(
                                imageUrl = review.avatarPath,
                                contentDescription = review.author,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                review.author,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            review.rating?.let {
                                VoteProgressBar(
                                    vote = it
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(review.content, style = MaterialTheme.typography.bodyMedium, maxLines = 3, overflow = TextOverflow.Ellipsis)
                    }
                }
            }
        }
    }
}

@Composable
fun SimilarMoviesSection(movies: List<SimilarMovie>) {
    Column {
        Text(
            "Similar Movies",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(movies) { movie ->
                Card(
                    shape = RoundedCornerShape(8.dp), modifier = Modifier.width(130.dp)
                ) {
                    Column {
                        AsyncImageWithPlaceholder(
                            imageUrl = movie.posterPath,
                            contentDescription = movie.title,
                            modifier = Modifier
                                .height(180.dp)
                                .fillMaxWidth(),
                        )
                        Text(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth(),
                            text = movie.title,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
