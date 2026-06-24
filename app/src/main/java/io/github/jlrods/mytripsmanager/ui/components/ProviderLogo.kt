package io.github.jlrods.mytripsmanager.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage

@Composable
fun ProviderLogo(
    logoRes: Int? = null,
    logoUri: String? = null,
    modifier: Modifier = Modifier
) {

    when {

        logoRes != null -> {
            Image(
                painter = painterResource(id = logoRes),
                contentDescription = null,
                modifier = modifier
            )
        }

        logoUri != null -> {
            AsyncImage(
                model = logoUri,
                contentDescription = null,
                modifier = modifier
            )
        }

        else -> {
            // fallback placeholder
            Box(modifier = modifier)
        }
    }
}