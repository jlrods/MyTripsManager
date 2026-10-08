package io.github.jlrods.mytripsmanager.ui.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> SearchablePickerDialog(

    title: String,

    items: List<T>,

    searchText: (T) -> String,

    onItemSelected: (T) -> Unit,

    onDismiss: () -> Unit,

    itemContent: @Composable RowScope.(T) -> Unit

) {

    var searchQuery by remember {
        mutableStateOf("")
    }

    val filteredItems = items
        .filter {
            searchText(it).contains(
                searchQuery,
                ignoreCase = true
            )
        }

    AlertDialog(

        onDismissRequest = onDismiss,

        confirmButton = {},

        title = {
            Text(title)
        },

        text = {

            Column {

                OutlinedTextField(

                    value = searchQuery,

                    onValueChange = {
                        searchQuery = it
                    },

                    label = {
                        Text("Search")
                    },

                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                LazyColumn {

                    items(filteredItems) { item ->

                        Row(

                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onItemSelected(item)
                                }
                                .padding(
                                    vertical = 10.dp,
                                    horizontal = 8.dp
                                ),

                            verticalAlignment = Alignment.CenterVertically

                        ) {

                            itemContent(item)

                        }
                    }
                }
            }
        }
    )
}