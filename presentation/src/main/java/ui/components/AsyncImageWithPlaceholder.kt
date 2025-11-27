package ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.example.presentation.R

@Composable
fun AsyncImageWithPlaceholder(
    imageUrl: String?,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    contentScale : ContentScale = ContentScale.Crop,
) {
    val model = imageUrl.takeIf { !it.isNullOrEmpty() } ?: R.drawable.ic_placeholder

    SubcomposeAsyncImage(
        modifier = modifier,
        model = model,
        contentDescription = contentDescription,

        loading = {
            Box(modifier = modifier.fillMaxSize().shimmerEffect())
        },
        error = {
            ImageErrorSection()
        },
        success = {
            if(imageUrl.isNullOrEmpty()){
                ImageErrorSection()
            }else{
                SubcomposeAsyncImageContent(
                    contentScale = contentScale
                )
            }
        }

    )
}


@Composable
private fun ImageErrorSection(contentDescription: String? = null){
    Box(
        modifier = Modifier.size(48.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(48.dp),
            painter = painterResource(R.drawable.ic_placeholder),
            contentDescription = contentDescription
        )
    }
}
