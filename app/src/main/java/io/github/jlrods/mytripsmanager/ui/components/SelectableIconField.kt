package io.github.jlrods.mytripsmanager.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


@Composable
fun SelectableIconField(

    label: String,

    text: String,

    iconRes: Int?,

    modifier: Modifier = Modifier,

    onClick: () -> Unit

) {


    Column(
        modifier = modifier.fillMaxWidth()
    ) {


        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium
        )


        Spacer(
            modifier = Modifier.height(4.dp)
        )


        Row(

            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onClick()
                }
                .padding(12.dp),

            verticalAlignment = Alignment.CenterVertically

        ) {


            if (iconRes != null) {

                Image(

                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(
                        MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.size(28.dp)
                )


                Spacer(
                    modifier = Modifier.width(12.dp)
                )
            }


            Text(

                text = text,

                style = MaterialTheme.typography.bodyLarge

            )

        }


        Divider()

    }
}