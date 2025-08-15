package com.kodebug.firebasenoteapp.presentation.sceens

import android.app.AlertDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.kodebug.firebasenoteapp.R
import com.kodebug.firebasenoteapp.data.model.Note
import com.kodebug.firebasenoteapp.navigation.NavRoutes
import com.kodebug.firebasenoteapp.presentation.viewModel.NoteViewModel
import com.kodebug.firebasenoteapp.ui.theme.colorBlack
import com.kodebug.firebasenoteapp.ui.theme.colorGray
import com.kodebug.firebasenoteapp.ui.theme.colorLightGray
import com.kodebug.firebasenoteapp.ui.theme.colorRed

@Composable
fun NoteScreen(
    navController: NavHostController,
    viewModel: NoteViewModel = hiltViewModel()
) {

    val notesList by viewModel.notes.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()


//    // create an instance of fire base data base
//    val firebaseDB = FirebaseFirestore.getInstance()
//
//    // create an instance of collection or document
//    val collectionReference = firebaseDB.collection("notes")
//
//    // create an instance for collection items
//    val noteList = remember {
//        mutableStateListOf<Note>()
//    }
//
//    // loading
//    val isDataFetched = remember {
//        mutableStateOf(false)
//    }
//
//    LaunchedEffect(Unit) {
//        collectionReference.addSnapshotListener { value, error ->
//            if (error == null) {
//                // create an instance of value of object
//                val data = value?.toObjects(Note::class.java)
//
//                // clear noteList if any exist
//                noteList.clear()
//
//                // them add data into list from db
//                noteList.addAll(data!!)
//
//                // loading
//                isDataFetched.value = true
//            } else {
//                isDataFetched.value = false
//            }
//        }
//    }


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(NavRoutes.AddNoteScreen.route)
                },
                shape = CircleShape,
                containerColor = colorRed,
                contentColor = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.plus),
                    contentDescription = "add note"
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorBlack)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Notes",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        color = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))
                LazyColumn {

                    if (isLoading) {
                        item {
                            LinearProgressIndicator(
                                modifier = Modifier.fillMaxWidth(),
                                color = colorRed,
                                trackColor = Color.White
                            )
                        }
                    } else {
                        items(notesList) { note ->

                            NoteItemCard(
                                note = note,
                                onUpdate = {
                                    navController.navigate(NavRoutes.UpdateNoteScreen.route + "/${note.id}")
                                },
                                onDelete = {
                                    viewModel.deleteNote(note.id)
                                }
                            )

//                            NoteItem(
//                                note = notes,
//                                collectionRef = collectionReference,
//                                navController = navController
//                            )
                        }
                    }

//                    if (isDataFetched.value) {
//                        items(noteList) { notes ->
//                            NoteItem(
//                                note = notes,
//                                collectionRef = collectionReference,
//                                navController = navController
//                            )
//                        }
//                    } else {
//                        item {
//                            LinearProgressIndicator(
//                                modifier = Modifier.fillMaxWidth(),
//                                color = colorRed,
//                                trackColor = Color.White
//                            )
//                        }
//                    }
                }
            }
        }
    }
}


// with Mvvm
@Composable
fun NoteItemCard(note: Note, onUpdate: () -> Unit, onDelete: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(colorGray)
    ) {

        val context = LocalContext.current
        var expanded by remember { mutableStateOf(false) }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier = Modifier.background(Color.White),
            properties = PopupProperties(clippingEnabled = true),
            offset = DpOffset(x = (-28).dp, y = (-40).dp),
            shape = RoundedCornerShape(12.dp)
        ) {


            DropdownMenuItem(
                text = {
                    Text(
                        text = "Update",
                        style = TextStyle(
                            color = colorBlack,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }, onClick = {
                    expanded = false
                    onUpdate()
                },
                modifier = Modifier.height(24.dp)// shrink to text height
            )
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = Color.LightGray)
            Spacer(modifier = Modifier.height(8.dp))
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Remove",
                        style = TextStyle(
                            color = colorBlack,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }, onClick = {
                    expanded = false

                    // show alert dialog
                    AlertDialog.Builder(context)
                        .setTitle("Delete Note")
                        .setMessage("Are you sure you want to delete this note?")
                        .setPositiveButton("Yes") { dialog, _ ->
                            onDelete()
                            dialog.dismiss()
                        }
                        .setNegativeButton("No") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()

                },
                modifier = Modifier.height(24.dp)
            )
        }

        IconButton(
            onClick = {
                expanded = true
            },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White
            ),
            modifier = Modifier
                .align(Alignment.TopEnd)
        ) {
            Icon(
                Icons.Default.MoreVert,
                contentDescription = "more",
            )
        }

        Column(
            modifier = Modifier
                .padding(vertical = 20.dp, horizontal = 16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = note.title,
                style = TextStyle(
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )

            if (note.description.isNotEmpty()) {
                Text(
                    text = note.description,
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        fontWeight = FontWeight.Normal,
                        color = colorLightGray.copy(alpha = .8f)
                    )
                )
            }
        }
    }
}

// without Mvvm
@Composable
fun NoteItem(note: Note, collectionRef: CollectionReference, navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(colorGray)
    ) {

        val context = LocalContext.current
        var expanded by remember { mutableStateOf(false) }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier = Modifier.background(Color.White),
            properties = PopupProperties(clippingEnabled = true),
            offset = DpOffset(x = (-28).dp, y = (-40).dp),
            shape = RoundedCornerShape(12.dp)
        ) {


            DropdownMenuItem(
                text = {
                    Text(
                        text = "Update",
                        style = TextStyle(
                            color = colorBlack,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }, onClick = {
                    navController.navigate(NavRoutes.UpdateNoteScreen.route + "/${note.id}")
                },
                modifier = Modifier.height(24.dp)// shrink to text height
            )
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = Color.LightGray)
            Spacer(modifier = Modifier.height(8.dp))
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Remove",
                        style = TextStyle(
                            color = colorBlack,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }, onClick = {
                    val noteId = note.id

                    // show alert dialog
                    val alertDialog = AlertDialog.Builder(context)
                    alertDialog.setTitle("Delete Note")
                    alertDialog.setMessage("Are you sure you want to delete this note?")
                    alertDialog.setPositiveButton("Yes") { dialog, which ->
                        collectionRef.document(noteId).delete()
                    }

                    alertDialog.setNegativeButton("No") { dialog, which ->
                        dialog.dismiss()
                    }

                    alertDialog.show()

                    expanded = false
                },
                modifier = Modifier.height(24.dp)
            )
        }

        IconButton(
            onClick = {
                expanded = true
            },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White
            ),
            modifier = Modifier
                .align(Alignment.TopEnd)
        ) {
            Icon(
                Icons.Default.MoreVert,
                contentDescription = "more",
            )
        }

        Column(
            modifier = Modifier
                .padding(vertical = 20.dp, horizontal = 16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = note.title,
                style = TextStyle(
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )

            if (note.description.isNotEmpty()) {
                Text(
                    text = note.description,
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        fontWeight = FontWeight.Normal,
                        color = colorLightGray.copy(alpha = .8f)
                    )
                )
            }
        }
    }
}