package com.example.projectfinal.di

import com.example.projectfinal.data.repository.FavoritoRepository
import com.example.projectfinal.data.repository.FavoritoRepositoryImp
import com.example.projectfinal.data.repository.ReservaRepository
import com.example.projectfinal.data.repository.ReservaRepositoyImp
import com.example.projectfinal.data.repository.RestauranteRepository
import com.example.projectfinal.data.repository.RestauranteRepositoryImp
import com.example.projectfinal.data.repository.UsuarioRepository
import com.example.projectfinal.data.repository.UsuarioRepositoryImp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
        database: FirebaseFirestore
    ): ReservaRepository {
        return ReservaRepositoyImp(database)
    }

    @Provides
    @Singleton
    fun provideAutghRepository(
        auth: FirebaseAuth
    ): UsuarioRepository {
        return UsuarioRepositoryImp(auth)
    }

    @Provides
    fun provideRepositoryFavorito(database: FirebaseFirestore): FavoritoRepository {
        return FavoritoRepositoryImp(database)
    }

    @Provides
    fun provideRepositoryRestaurante(database: FirebaseFirestore): RestauranteRepository {
        return RestauranteRepositoryImp(database)
    }
}