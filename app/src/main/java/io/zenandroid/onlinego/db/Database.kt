package io.zenandroid.onlinego.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import io.zenandroid.onlinego.model.local.Challenge
import io.zenandroid.onlinego.model.local.Game
import io.zenandroid.onlinego.model.local.GameNotification
import io.zenandroid.onlinego.model.local.Message

/**
 * Created by 44108952 on 04/06/2018.
 */
@Database(
        entities = [Game::class, Message::class, Challenge::class, GameNotification::class],
        version = 6
)
@TypeConverters(DbTypeConverters::class)
abstract class Database: RoomDatabase() {
    abstract fun gameDao(): GameDao
}