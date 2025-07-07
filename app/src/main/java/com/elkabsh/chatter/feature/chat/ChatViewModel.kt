package com.elkabsh.chatter.feature.chat


import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.elkabsh.chatter.feature.model.Message
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.storage.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {


    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val message = _messages.asStateFlow()
    private val db = Firebase.database

    fun sendMessage(channelID: String, messageText: String?, image: String? = null) {
        val message = Message(
            db.reference.push().key ?: UUID.randomUUID().toString(),
            Firebase.auth.currentUser?.uid ?: "",
            messageText,
            System.currentTimeMillis(),
            Firebase.auth.currentUser?.displayName ?: "",
            null,
            image
        )

        db.reference.child("messages").child(channelID).push().setValue(message).addOnSuccessListener {
            Log.i("ChatViewModel", "Message sent successfully: $message")
        }.addOnFailureListener { exception ->
            Log.e("ChatViewModel", "Failed to send message: ${exception.message}")
        }
    }

    fun listenForMessages(channelID: String) {
        db.getReference("messages").child(channelID).orderByChild("createdAt")
            .addValueEventListener(
                object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<Message>()
                    snapshot.children.forEach { data ->
                        val message = data.getValue(Message::class.java)
                        message?.let {
                            list.add(it)
                        }
                    }
                    _messages.value = list
                    Log.i("ChatViewModel", "Messages updated: ${list.size} messages")
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            }
            )

    }
    fun sendImageMessage(uri: Uri, channelID: String) {
        val imageRef = Firebase.storage.reference.child("images/${UUID.randomUUID()}")
        imageRef.putFile(uri).continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            val currentUser = Firebase.auth.currentUser
            Log.i("ChatViewModel", "Image message sent successfully: ${task.isSuccessful}")
            if (task.isSuccessful) {
                val downloadUri = task.result
                sendMessage(channelID, null, downloadUri.toString())
                Log.i("ChatViewModel", "Image message sent successfully: $task")

            }
        }
    }

}