package com.abedfattal.foodie.framework.di

import com.abedfattal.foodie.framework.BASE_URL
import com.abedfattal.foodie.framework.repository.RecipesRepository
import com.abedfattal.foodie.framework.webservice.Food2ForkService
import com.abedfattal.foodie.ui.MainActivityViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object KoinService {

    private val webServices = module {
        single<Food2ForkService> {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .retryOnConnectionFailure(true)
                .build()

            Retrofit.Builder().baseUrl(BASE_URL).client(client)
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(Food2ForkService::class.java)

        }
    }

    private val repositories = module {
        single { RecipesRepository(food2ForkService = get()) }
    }

    private val viewModels = module {
        viewModel {
            MainActivityViewModel(recipesRepository = get())
        }
    }

    val modules = listOf(webServices, viewModels, repositories)

}