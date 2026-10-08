package org.example.project.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode
import org.example.project.data.model.CharacterResponse
import org.example.project.data.model.PageInfo

/** Contrato da fonte de dados remota (facilita trocar por um Fake em testes/demo offline). */
interface CharacterApi {
    suspend fun getCharacters(name: String?): CharacterResponse
}

/** ApiService real, usando Ktor Client. */
class KtorCharacterApi(private val client: HttpClient) : CharacterApi {


    override suspend fun getCharacters(name: String?): CharacterResponse =
        try {
            client.get("character") {
                if (!name.isNullOrBlank()) parameter("name", name.trim())
            }.body<CharacterResponse>()
        } catch (e: ClientRequestException) {

            if (e.response.status == HttpStatusCode.NotFound) {
                CharacterResponse(info = PageInfo(count = 0, pages = 0), results = emptyList())
            } else {
                throw e
            }
        }

}
