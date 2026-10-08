package org.example.project.data.repository

import org.example.project.data.model.Character
import org.example.project.data.remote.CharacterApi

/**
 * Repository: única porta de entrada de dados para o ViewModel.
 * Esconde de onde vêm os dados (rede, cache, banco...) — o ViewModel não sabe de Ktor.
 */
class CharacterRepository(private val api: CharacterApi) {

    // Cache simples em memória: buscar "rick" de novo não bate na rede.
    // (Evolução natural: trocar por Room/SQLDelight — a assinatura pública não muda.)
    private val cache = mutableMapOf<String, List<Character>>()

    suspend fun searchCharacters(query: String): List<Character> {
        val key = query.trim().lowercase()
        // Só entra no cache se a chamada der certo; erro propaga como exceção.
        return cache.getOrPut(key) { api.getCharacters(key).results }
    }
}
