package org.example.project.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.project.data.model.Character
import org.example.project.data.repository.CharacterRepository

class CharactersViewModel(
    private val repository: CharacterRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Character>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Character>>> = _uiState.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()


    private val _selected = MutableStateFlow<Character?>(null)
    val selected: StateFlow<Character?> = _selected.asStateFlow()

    private var loadJob: Job? = null

    init {
        load(query = "", debounce = false)
    }



    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
        load(newQuery, debounce = true)
    }

    fun retry() = load(_query.value, debounce = false)

    fun onCharacterClick(character: Character) {
        _selected.value = character
    }

    fun onBack() {
        _selected.value = null
    }


    private fun load(query: String, debounce: Boolean) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            if (debounce) delay(400)
            _uiState.value = UiState.Loading
            _uiState.value = try {
                UiState.Success(repository.searchCharacters(query))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                UiState.Error(
                    "Não foi possível carregar os personagens." +
                        (e.message?.let { "\n($it)" } ?: "")
                )
            }
        }
    }
    // <<< LIVE 2 (fim)
}
