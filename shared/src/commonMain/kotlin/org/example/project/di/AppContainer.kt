package org.example.project.di

import org.example.project.data.remote.CharacterApi
import org.example.project.data.remote.FakeCharacterApi
import org.example.project.data.remote.KtorCharacterApi
import org.example.project.data.remote.createHttpClient
import org.example.project.data.repository.CharacterRepository

/**
 * "DI manual": um objeto que monta o grafo de dependências.
 * Simples de explicar em workshop; em produção troque por Koin / kotlin-inject.
 */
object AppContainer {

    /** PLANO B: true = usa dados locais (sem internet). */
    private val useFakeApi = false

    private val httpClient by lazy { createHttpClient() }

    private val api: CharacterApi by lazy {
        if (useFakeApi) FakeCharacterApi() else KtorCharacterApi(httpClient)
    }

    val characterRepository: CharacterRepository by lazy { CharacterRepository(api) }
}
