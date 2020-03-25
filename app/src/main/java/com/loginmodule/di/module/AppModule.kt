package com.loginmodule.di.module

import android.content.Context
import androidx.room.Room
import com.loginmodule.BaseApplication
import com.loginmodule.BuildConfig
import com.loginmodule.common.*
import com.loginmodule.data.local.AppDatabase
import com.loginmodule.data.remote.ApiService
import com.loginmodule.di.qualifier.DatabaseInfo
import com.loginmodule.util.datahelper.FakeInterceptor
import com.loginmodule.util.datahelper.LiveDataCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

/**
 * Module for application level dependencies
 */
@Module(includes = [ViewModelModule::class])
class AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(application: BaseApplication): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
        okHttpClient.readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
        okHttpClient.writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
        if (BuildConfig.DEBUG) {
            okHttpClient.addInterceptor(
                FakeInterceptor(
                    application
                )
            )
        }
        okHttpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        return okHttpClient.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(REST_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory.create())
            .client(okHttpClient)
            .build()

        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Named("application_context")
    @Singleton
    fun provideContext(application: BaseApplication): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideAppDatabase(
        @DatabaseInfo dbName: String,
        @Named("application_context") context: Context
    ): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, dbName)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @DatabaseInfo
    fun provideDatabaseName(): String {
        return ROOM_DATABASE_NAME
    }
}