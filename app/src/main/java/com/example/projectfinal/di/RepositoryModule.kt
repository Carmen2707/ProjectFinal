package com.example.projectfinal.di

import android.content.SharedPreferences
import com.example.projectfinal.data.repository.ReservaRepository
import com.example.projectfinal.data.repository.ReservaRepositoyImp
import com.example.projectfinal.data.repository.UsuarioRepository
import com.example.projectfinal.data.repository.UsuarioRepositoryImp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {
    @Provides
    @Singleton
    fun provideReservaRepository(
        database: FirebaseFirestore,
        storageReference: StorageReference
    ): ReservaRepository {
        return ReservaRepositoyImp(database,storageReference)
    }



    @Provides
    @Singleton
    fun provideAutghRepository(
        database: FirebaseFirestore,
        auth: FirebaseAuth,
        appPreferences: SharedPreferences,
        gson: Gson
    ): UsuarioRepository {
        return UsuarioRepositoryImp (auth,database,appPreferences,gson)
    }
}