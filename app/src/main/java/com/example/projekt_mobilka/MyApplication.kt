package com.example.projekt_mobilka

import android.app.Application
import com.example.projekt_mobilka.model.AppDatabase
import com.example.projekt_mobilka.model.UserRepository

class MyApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { UserRepository(database.userDao(), database.gameResultDao(), this) }
}
