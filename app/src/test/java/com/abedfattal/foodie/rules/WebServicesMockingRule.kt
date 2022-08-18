package com.abedfattal.foodie.rules

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockWebServer
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class WebServicesMockingRule<T : Any>(private val clazz: Class<T>) :
    TestWatcher() {

    val server: MockWebServer = MockWebServer()

    lateinit var api: T
        private set

    override fun starting(description: Description) {
        super.starting(description)
        api = run {
            val logger =
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .connectTimeout(1, TimeUnit.SECONDS)
                .readTimeout(1, TimeUnit.SECONDS)
                .writeTimeout(1, TimeUnit.SECONDS)
                .build()

            Retrofit.Builder()
                .baseUrl(server.url("/"))
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(clazz)
        }

    }

    override fun finished(description: Description) {
        super.finished(description)
        server.shutdown()
    }
}