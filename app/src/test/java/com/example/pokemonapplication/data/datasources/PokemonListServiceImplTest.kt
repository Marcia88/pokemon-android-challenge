package com.example.pokemonapplication.data.datasources

import com.example.pokemonapplication.data.dto.PokemonListResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PokemonListServiceImplTest {

    private val jsonConfig = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    private fun createClient(mockEngine: MockEngine) = HttpClient(mockEngine) {
        install(ContentNegotiation) {
            json(jsonConfig)
        }
    }

    @Test
    fun `getPokemonList returns success when server returns 200`() = runTest {
        val fakeJson = """
            {
                "count": 2,
                "results": [
                    { "name": "bulbasaur", "url": "https://pokeapi.co/api/v2/pokemon/1/" },
                    { "name": "ivysaur", "url": "https://pokeapi.co/api/v2/pokemon/2/" }
                ]
            }
        """.trimIndent()

        val mockEngine = MockEngine { request ->
            val url = request.url.toString()
            assertTrue(url.contains("limit=10"))
            assertTrue(url.contains("offset=0"))

            respond(
                content = fakeJson,
                status = HttpStatusCode.OK,
                headers = headersOf("Content-Type", "application/json")
            )
        }

        val client = createClient(mockEngine)
        val service = PokemonListServiceImpl(client)
        val result = service.getPokemonList()
        assertTrue(result.isSuccess)

        val data = result.getOrNull()
        assertEquals(2, data?.count)
        assertEquals("bulbasaur", data?.results?.first()?.name)
    }

    @Test
    fun `getPokemonList returns failure when server returns 500`() = runTest {
        val mockEngine = MockEngine {
            respondError(HttpStatusCode.InternalServerError)
        }

        val client = createClient(mockEngine)
        val service = PokemonListServiceImpl(client)
        val result = service.getPokemonList()
        assertTrue(result.isFailure)
    }

    @Test
    fun `getPokemonList returns failure when JSON is malformed`() = runTest {
        val mockEngine = MockEngine {
            respond(
                content = "{ bad json ]",
                status = HttpStatusCode.OK,
                headers = headersOf("Content-Type", "application/json")
            )
        }

        val client = createClient(mockEngine)
        val service = PokemonListServiceImpl(client)
        val result = service.getPokemonList()
        assertTrue(result.isFailure)
    }
}
