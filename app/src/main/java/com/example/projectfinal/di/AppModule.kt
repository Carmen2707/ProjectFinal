package com.example.projectfinal.di

import android.content.Context
import android.content.SharedPreferences
import com.example.projectfinal.data.repository.FavoritoRepository
import com.example.projectfinal.data.repository.FavoritoRepositoryImp
import com.example.projectfinal.room.DAO
import com.example.projectfinal.room.RestaurantesBD
import com.example.projectfinal.util.SharedPrefConstants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPref(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(
            SharedPrefConstants.LOCAL_SHARED_PREF,
            Context.MODE_PRIVATE
        )
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    /**
     * Utiliza la instancia para obtener el DAO asociado.
     */
    @Provides
    @Singleton
    fun getAppDao(restauranteDataBase: RestaurantesBD): DAO {
        return restauranteDataBase.getAppDao()
    }

    /**
     * Utiliza el método estático getAppDBInstance para obtener o crear una instancia única de la base de datos.
     */
    @Provides
    @Singleton
    fun getAppDatabase(@ApplicationContext context: Context): RestaurantesBD {
        return RestaurantesBD.getAppDBInstance(context)
    }

    @Provides
    @Singleton
    fun provideFavoritoRepository(database: FirebaseFirestore): FavoritoRepository {
        return FavoritoRepositoryImp(database)
    }
}