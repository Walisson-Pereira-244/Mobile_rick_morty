package org.example.project.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.example.project.data.model.Character
import org.example.project.presentation.UiState
import org.example.project.ui.components.CharacterCard


@Composable
fun CharactersScreen(
    uiState: UiState<List<Character>>,
    query: String,
    onQueryChange: (String) -> Unit,
    onRetry: () -> Unit,
    onCharacterClick: (Character) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxSize()) {

        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            singleLine = true,
            label = { Text("Buscar personagem") },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }) { Text("✕") }
                }
            },
        )


        when (uiState) {
            UiState.Loading -> CenteredBox {
                CircularProgressIndicator()
            }

            is UiState.Error -> CenteredBox {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = uiState.message,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(horizontal = 24.dp),
                    )
                    Button(onClick = onRetry) { Text("Tentar novamente") }
                }
            }

            is UiState.Success -> {
                if (uiState.data.isEmpty()) {
                    CenteredBox { Text("Nenhum personagem encontrado 🛸") }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(items = uiState.data, key = { it.id }) { character ->
                            CharacterCard(
                                character = character,
                                onClick = { onCharacterClick(character) },
                            )
                        }
                    }
                }
            }
        }

    }
}

@Composable
private fun CenteredBox(content: @Composable () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { content() }
}
