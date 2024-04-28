package com.example.projectfinal.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.projectfinal.data.model.Restaurante

@Database(entities = [Restaurante::class], version = 1, exportSchema = false)
abstract class RestaurantesBD : RoomDatabase() {
    abstract fun getAppDao(): DAO

    companion object {
        private var DB_INSTANCE: RestaurantesBD? = null

        fun getAppDBInstance(context: Context): RestaurantesBD {
            if (DB_INSTANCE == null) {
                DB_INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    RestaurantesBD::class.java,
                    "restaurantesBD"
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return DB_INSTANCE!!
        }
    }
}