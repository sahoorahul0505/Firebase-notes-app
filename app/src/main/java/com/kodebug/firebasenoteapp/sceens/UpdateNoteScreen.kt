package com.kodebug.firebasenoteapp.sceens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import com.kodebug.firebasenoteapp.R
import com.kodebug.firebasenoteapp.model.Note
import com.kodebug.firebasenoteapp.ui.theme.colorBlack
import com.kodebug.firebasenoteapp.ui.theme.colorGray
import com.kodebug.firebasenoteapp.ui.theme.colorLightGray
import com.kodebug.firebasenoteapp.ui.theme.colorRed

@Composable
fun UpdateNoteScreen(navController: NavHostController, noteId: String?) {
    val context = LocalContext.current
    val firebaseDB = FirebaseFirestore.getInstance()
    val collectionReference = firebaseDB.collection("notes")

    val title = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }


    LaunchedEffect(Unit) {
        if (noteId != null){
            collectionReference.document(noteId.toString()).get().addOnSuccessListener {
                val singleData = it.toObject(Note::class.java) // convert collect data to object
                title.value = singleData?.title.toString()
                description.value = singleData?.description.toString()
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (title.value.isEmpty()) {
                        Toast.makeText(context, "Please enter title and description", Toast.LENGTH_SHORT).show()
                    } else {
                        val oldNoteId = noteId.toString()
                        val updateNoteData = Note(
                            id = oldNoteId,
                            title = title.value,
                            description = description.value
                        )

                        collectionReference.document(oldNoteId).set(updateNoteData).addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(
                                    context, "Note added successfully",
                                    Toast.LENGTH_SHORT
                                ).show()

                                navController.popBackStack()

                            } else {
                                Toast.makeText(
                                    context,
                                    "Something went wrong\nPlease check your internet connection",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                },
                shape = CircleShape,
                containerColor = colorRed,
                contentColor = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_tick),
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
                    .padding(16.dp)
            ) {
                Text(
                    text = "Add note",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        color = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))
                TextField(
                    value = title.value,
                    onValueChange = {
                        title.value = it
                    },
                    label = {
                        Text(
                            text = "Title"
                        )
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent,
                        focusedLabelColor = colorLightGray,
                        unfocusedLabelColor = colorLightGray,
                        unfocusedContainerColor = colorGray,
                        focusedContainerColor = colorGray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = description.value,
                    onValueChange = {
                        description.value = it
                    },
                    label = {
                        Text(
                            text = "Description"
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent,
                        focusedLabelColor = colorLightGray,
                        unfocusedLabelColor = colorLightGray,
                        unfocusedContainerColor = colorGray,
                        focusedContainerColor = colorGray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(.6f)
                )
            }
        }
    }
}