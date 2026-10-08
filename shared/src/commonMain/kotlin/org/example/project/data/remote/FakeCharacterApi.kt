package org.example.project.data.remote

import kotlinx.coroutines.delay
import org.example.project.data.model.Character
import org.example.project.data.model.CharacterResponse
import org.example.project.data.model.PageInfo
import org.example.project.data.model.Place

/**
 * PLANO B da demo: dados locais, sem internet.
 * Ative em AppContainer (useFakeApi = true). As imagens ficam no placeholder offline.
 */
class FakeCharacterApi : CharacterApi {

    override suspend fun getCharacters(name: String?): CharacterResponse {
        delay(700) // simula latência de rede para o loading aparecer
        val filtered = data.filter { name.isNullOrBlank() || it.name.contains(name.trim(), ignoreCase = true) }
        return CharacterResponse(PageInfo(filtered.size, 1), filtered)
    }

    private fun avatar(id: Int) = "https://rickandmortyapi.com/api/character/avatar/$id.jpeg"
    private val earth = Place("Earth (C-137)")
    private val citadel = Place("Citadel of Ricks")

    private val data = listOf(
        Character(1, "Rick Sanchez", "Alive", "Human", "", "Male", earth, citadel, avatar(1), List(51) { "ep$it" }, "2017-11-04T18:48:46.250Z"),
        Character(2, "Morty Smith", "Alive", "Human", "", "Male", earth, Place("Earth (Replacement Dimension)"), avatar(2), List(51) { "ep$it" }, "2017-11-04T18:50:21.651Z"),
        Character(3, "Summer Smith", "Alive", "Human", "", "Female", Place("Earth (Replacement Dimension)"), Place("Earth (Replacement Dimension)"), avatar(3), List(42) { "ep$it" }, "2017-11-04T19:09:56.428Z"),
        Character(4, "Beth Smith", "Alive", "Human", "", "Female", Place("Earth (Replacement Dimension)"), Place("Earth (Replacement Dimension)"), avatar(4), List(42) { "ep$it" }, "2017-11-04T19:22:43.665Z"),
        Character(5, "Jerry Smith", "Alive", "Human", "", "Male", Place("Earth (Replacement Dimension)"), Place("Earth (Replacement Dimension)"), avatar(5), List(39) { "ep$it" }, "2017-11-04T19:26:56.301Z"),
        Character(8, "Adjudicator Rick", "Dead", "Human", "", "Male", Place("unknown"), Place("Citadel of Ricks"), avatar(8), List(1) { "ep$it" }, "2017-11-04T20:03:34.737Z"),
        Character(19, "Antenna Rick", "unknown", "Human", "Human with antennae", "Male", Place("unknown"), Place("unknown"), avatar(19), List(1) { "ep$it" }, "2017-11-04T22:28:13.756Z"),
    )
}
