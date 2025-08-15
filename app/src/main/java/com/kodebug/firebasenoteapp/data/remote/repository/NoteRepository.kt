package com.kodebug.firebasenoteapp.data.remote.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.kodebug.firebasenoteapp.data.model.Note
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(
    fireStoreDB: FirebaseFirestore
) {

    private val collectionReference = fireStoreDB.collection("notes")
    val newNoteId = collectionReference.document().id

    fun getNotes(): Flow<List<Note>> = callbackFlow {
        val listener = collectionReference.addSnapshotListener { value, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            val notes = value?.toObjects(Note::class.java) ?: emptyList()
            trySend(notes)

        }
        awaitClose {
            listener.remove()
        }
    }

    suspend fun addNote(note: Note): Boolean {
        return try {
            collectionReference.document(note.id).set(note).await()
            true
        } catch (ex: Exception) {
            Log.d("errorAddNote", ex.message.toString())
            false
        }
    }

    suspend fun updateNote(note: Note): Boolean {
        return try {
            collectionReference.document(note.id).set(note).await()
            true
        }catch (ex: Exception){
            Log.d("errorUpdateNote", ex.message.toString())
            false
        }
    }

    suspend fun deleteNote(noteId: String): Boolean {
        return try {
            collectionReference.document(noteId).delete().await()
            true
        }catch (ex: Exception){
            Log.d("errorDeleteNote", ex.message.toString())
            false
        }
    }

    suspend fun getNoteById(noteId: String): Note? {
        return try {
            val snapshot = collectionReference.document(noteId).get().await()
            snapshot.toObject(Note::class.java)
        }catch (ex: Exception){
            Log.i("errorGetNoteById", ex.message.toString())
            null
        }
    }
}