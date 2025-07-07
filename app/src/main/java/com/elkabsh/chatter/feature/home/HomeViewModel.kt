package com.elkabsh.chatter.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.elkabsh.chatter.feature.model.Channel
import com.google.firebase.Firebase
import com.google.firebase.database.database
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val firebaseDatabase = Firebase.database
    private val _channelsList = MutableStateFlow<List<Channel>>(emptyList())
    val channelsList = _channelsList.asStateFlow()


    init {
        getChannels()
    }

    private fun getChannels() {
        firebaseDatabase.getReference("channel").get().addOnSuccessListener {
            Log.i("HomeViewModel", "Channels fetched successfully")
            val list = mutableListOf<Channel>()
            it.children.forEach { data ->
                val channel = Channel(data.key!!, data.value.toString())
                list.add(channel)
            }
            _channelsList.value = list
        }
    }

    fun addChannel(name: String) {
        val key = firebaseDatabase.getReference("channel").push().key
        firebaseDatabase.getReference("channel").child(key!!).setValue(name).addOnSuccessListener {
            getChannels()
            Log.i("HomeViewModel", "Channel added successfully: $name")
        }.addOnFailureListener {
            Log.e("HomeViewModel", "Failed to add channel: ${it.message}")
        }
    }
}

