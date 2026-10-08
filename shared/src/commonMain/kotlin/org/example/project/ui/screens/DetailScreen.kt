package org.example.project.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.example.project.data.model.Character
import org.example.project.ui.components.CharacterImage
import org.example.project.ui.components.InfoRow
import org.example.project.ui.components.StatusBadge

/** TELA 2 — Detalhes do personagem selecionado (o botão ← fica na TopAppBar do App.kt). */
@Composable
fun DetailScreen(character: Character, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CharacterImage(
            url = character.image,
            contentDescription = character.name,
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 420.dp)       // no Desktop a imagem não estoura a janela
                .aspectRatio(1f)
                .clip(RoundedCornerShape(20.dp)),
        )

        Spacer(Modifier.height(16.dp))
        Text(character.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        StatusBadge(character.status)
        Spacer(Modifier.height(16.dp))

        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                InfoRow("Espécie", character.species)
                HorizontalDivider()
                InfoRow("Gênero", character.gender)
                if (character.type.isNotBlank()) {
                    HorizontalDivider()
                    InfoRow("Tipo", character.type)
                }
                HorizontalDivider()
                InfoRow("Origem", character.origin.name)
                HorizontalDivider()
                InfoRow("Última localização", character.location.name)
                HorizontalDivider()
                InfoRow("Aparece em", "${character.episode.size} episódios")
                if (character.created.length >= 10) {
                    HorizontalDivider()
                    InfoRow("Registrado em", character.created.take(10))
                }
            }
        }
    }
}
