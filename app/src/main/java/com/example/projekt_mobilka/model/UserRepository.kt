package com.example.projekt_mobilka.model

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.io.File
import java.io.FileOutputStream

class UserRepository(private val userDao: UserDao, private val context: Context) {
    val user: Flow<User?> = userDao.getUser()

    suspend fun updateUsername(username: String) {
        val currentUser = user.first() ?: User()
        userDao.insertUser(currentUser.copy(username = username))
    }

    suspend fun updateProfilePicture(uri: Uri) {
        val currentUser = user.first() ?: User()
        val fileName = "profile_picture_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)

        context.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }

        // Delete old picture if it exists
        currentUser.profilePicturePath?.let { path ->
            File(path).delete()
        }

        userDao.insertUser(currentUser.copy(profilePicturePath = file.absolutePath))
    }

    suspend fun incrementWins() {
        val currentUser = user.first() ?: return
        userDao.insertUser(currentUser.copy(wins = currentUser.wins + 1))
    }

    suspend fun incrementLosses() {
        val currentUser = user.first() ?: return
        userDao.insertUser(currentUser.copy(losses = currentUser.losses + 1))
    }
}
