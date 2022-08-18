package com.abedfattal.foodie.framework.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.abedfattal.foodie.framework.webservice.Food2ForkEndpoints
import com.abedfattal.foodie.framework.webservice.Food2ForkService
import com.abedfattal.foodie.helpers.EnqueueMockWebService
import com.abedfattal.foodie.models.ProcessState
import com.abedfattal.foodie.rules.WebServicesMockingRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecipesRepositoryTest : EnqueueMockWebService by Food2ForkEnqueueServer {

    @get:Rule
    var webServicesMockingRule = WebServicesMockingRule(Food2ForkService::class.java)

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val server :MockWebServer get() = webServicesMockingRule.server

    //Subject under test.
    private lateinit var sut: RecipesRepository

    @Before
    fun setup() {
        sut = RecipesRepository(webServicesMockingRule.api) //Web api.
    }


    @Test
    fun `getRecipeWithIngredient should fetch recipe ingredient correctly given 200 response`() = runTest {
        //When the response code is 200.
        server.enqueueResponse(Food2ForkEndpoints.recipeWithIngredient, 200)

        val result = sut.getRecipeWithIngredient("35107").take(2).toList()

        assertTrue(result.first() is ProcessState.Loading<*>)
        assertTrue(result.last() is ProcessState.Success<*>)
    }

    @Test
    fun `getRecipesByCategory should fetch recipes list given 200 response`() = runTest {
        //When the response code is 200.
        server.enqueueResponse(Food2ForkEndpoints.recipeByCategory, 200)

        val result = sut.getRecipesByCategory("beef").take(2).toList()

        assertTrue(result.first() is ProcessState.Loading<*>)
        assertTrue(result.last() is ProcessState.Success<*>)
    }


}