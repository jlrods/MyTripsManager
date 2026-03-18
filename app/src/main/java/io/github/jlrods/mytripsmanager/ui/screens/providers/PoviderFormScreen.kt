package io.github.jlrods.mytripsmanager.ui.screens.providers

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import io.github.jlrods.mytripsmanager.R
import io.github.jlrods.mytripsmanager.database.Provider
import io.github.jlrods.mytripsmanager.utils.ImageStorage
import java.io.File
import androidx.compose.foundation.layout.ColumnScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProviderFormScreen(
    viewModel: ProvidersViewModel,
    modifier: Modifier = Modifier,
    providerToEdit: Provider? = null,
    onSave: () -> Unit
) {
    val isEditMode = providerToEdit != null

    var providerName by rememberSaveable(providerToEdit) {
        mutableStateOf(providerToEdit?.name?.trim()?.replaceFirstChar { it.uppercase() } ?: "")
    }

    var selectedLogoRes by rememberSaveable(providerToEdit) {
        mutableStateOf(providerToEdit?.logoRes)
    }

    var selectedLogoUri by rememberSaveable(providerToEdit) {
        mutableStateOf(providerToEdit?.logoUri)
    }
    var showLogoPicker by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val logoPickerLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->

            uri?.let {

                val localPath = ImageStorage.saveCompressedImage(
                    context = context,
                    uri = it
                )

                selectedLogoUri = localPath
                selectedLogoRes = null
            }
        }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val builtInLogos = listOf(
        R.drawable.logo_aa,
        R.drawable.logo_aerlingus,
        R.drawable.logo_aireuropa,
        R.drawable.logo_airfrance,
        R.drawable.logo_allianz,
        R.drawable.logo_americanairlines,
        R.drawable.logo_avis,
        R.drawable.logo_aviva,
        R.drawable.logo_axa,
        R.drawable.logo_booking,
        R.drawable.logo_britishairways,
        R.drawable.logo_buseirean,
        R.drawable.logo_citylink,
        R.drawable.logo_conviasa,
        R.drawable.logo_corkairport,
        R.drawable.logo_copaairlines,
        R.drawable.logo_dublinairport,
        R.drawable.logo_dublinbus,
        R.drawable.logo_dublincoach,
        R.drawable.logo_easyjet,
        R.drawable.logo_enterprise,
        R.drawable.logo_europcar,
        R.drawable.logo_goaheadireland,
        R.drawable.logo_goldcar,
        R.drawable.logo_hertz,
        R.drawable.logo_iberia,
        R.drawable.logo_irishlife,
        R.drawable.logo_kml,
        R.drawable.logo_laya,
        R.drawable.logo_loveholidays,
        R.drawable.logo_lufthansa,
        R.drawable.logo_norwegian,
        R.drawable.logo_ryanair,
        R.drawable.logo_shannonairport,
        R.drawable.logo_sixt,
        R.drawable.logo_turkishairlines,
        R.drawable.logo_vhi
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {

        OutlinedTextField(
            value = providerName,
            onValueChange = { providerName = it },
            label = { Text("Provider Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Logo", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {

            when {
                selectedLogoRes != null -> {
                    Image(
                        painter = painterResource(selectedLogoRes!!),
                        contentDescription = "Selected Logo",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                selectedLogoUri != null -> {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = File(selectedLogoUri.toString())
                        ),
                        contentDescription = "Selected Logo",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                else -> {
                    Text("No Logo")
                }
            }
        }

        Button(
            onClick = { showLogoPicker = true }
        ) {
            Text("Select Logo")
        }
        Button(
            onClick = {
                if (providerName.isNotBlank()) {
                    if (!isEditMode) {
                        //ADD MODE
                        viewModel.insertProvider(

                            name = providerName,
                            logoRes = selectedLogoRes,
                            logoUri = selectedLogoUri,
                            onDuplicate = {
                                Toast.makeText(
                                    context,
                                    "Provider already exists",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onSuccess = {
                                onSave()
                            }
                        )

                    } else {
                        // EDIT MODE
                        providerToEdit?.let {
                            viewModel.updateProvider(
                                id = it.id,
                                name = providerName,
                                logRes = selectedLogoRes,
                                logoUri = selectedLogoUri,
                                onDuplicate = {
                                    Toast.makeText(
                                        context,
                                        "Provider already exists",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                onSuccess = {
                                    onSave()
                                }
                            )
                        }
                    }

                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isEditMode) "Update Provider" else "Save Provider")
        }
        if (isEditMode) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = { showDeleteDialog = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Provider"
                    )
                }
            }
        }

    }


    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Provider") },
            text = { Text("Are you sure you want to delete this provider?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        providerToEdit?.let {
                            viewModel.deleteProvider(
                                Provider(
                                    it.id,
                                    it.name,
                                    it.logoRes,
                                    it.logoUri,
                                )
                            )
                        }
                        showDeleteDialog = false
                        Toast.makeText(
                            context,
                            "Provider deleted",
                            Toast.LENGTH_SHORT
                        ).show()
                        onSave()
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
    if (showLogoPicker) {

        ModalBottomSheet(
            onDismissRequest = { showLogoPicker = false }
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Text(
                    text = "Choose Logo",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "App Logos",
                    style = MaterialTheme.typography.labelLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.height(300.dp)
                ) {

                    items(builtInLogos) { logo ->

                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .size(80.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    selectedLogoRes = logo
                                    selectedLogoUri = null
                                    showLogoPicker = false
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(logo),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider()

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(
                    onClick = {
                        showLogoPicker = false
                        logoPickerLauncher.launch("image/*")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Choose From Gallery")
                }

                TextButton(
                    onClick = { showLogoPicker = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancel")
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

