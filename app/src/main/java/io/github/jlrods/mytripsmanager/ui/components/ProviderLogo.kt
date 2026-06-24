package io.github.jlrods.mytripsmanager.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage

@Composable
fun ProviderLogo(
    logoRes: Int? = null,
    logoUri: String? = null,
    modifier: Modifier = Modifier
) {

    when {

        logoRes != null && logoRes != 0 -> {

            Image(
                painter = painterResource(id = logoRes),
                contentDescription = null,
                modifier = modifier,
                contentScale = ContentScale.Fit
            )

        }

        !logoUri.isNullOrBlank() -> {

            AsyncImage(
                model = logoUri,
                contentDescription = null,
                modifier = modifier,
                contentScale = ContentScale.Fit
            )

        }

        else -> {

            // empty fallback
        }
    }
}