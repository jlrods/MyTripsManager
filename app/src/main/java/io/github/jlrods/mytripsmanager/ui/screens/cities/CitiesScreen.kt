package io.github.jlrods.mytripsmanager.ui.screens.cities

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.github.jlrods.mytripsmanager.database.CityWithCountry
import io.github.jlrods.mytripsmanager.ui.utils.getFlagRes

//@Composable
//fun CitiesScreen(
//    viewModel: CitiesViewModel,
//    modifier: Modifier = Modifier
//) {
//    val cities by viewModel.cities.collectAsState()
//
//    LazyColumn(
//        modifier = modifier.fillMaxSize()
//    ) {
//        items(cities) { city ->
//            Text(text = city.name)
//        }
//    }
//}

@Composable
fun CitiesScreen(
    viewModel: CitiesViewModel,
    modifier: Modifier = Modifier
) {
    val cities by viewModel.cities.collectAsState()
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(cities) { item ->
            CityRow(item)
        }
    }

}

@Composable
fun CityRow(item: CityWithCountry) {

    val context = LocalContext.current
    val flagResId = getFlagRes(item.country.flag) {

        context.resources.getIdentifier(
            item.country.flag,
            "drawable",
            context.packageName
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        if (flagResId != 0) {
            Icon(
                painter = painterResource(id = flagResId),
                contentDescription = item.country.name,
                modifier = Modifier.size(32.dp)
            )
        } else {
            Text("❌")
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(text = item.city.name)
    }
}


