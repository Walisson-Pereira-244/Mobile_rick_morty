package org.example.project.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.example.project.getPlatform
import org.example.project.ui.components.Avatar
import org.example.project.ui.components.SettingRow
import org.example.project.ui.components.StatCard
import org.example.project.ui.theme.AppTheme


@Composable
fun ProfileScreen(
    darkTheme: Boolean,
    onDarkThemeChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {

    var following by remember { mutableStateOf(false) }
    var notifications by remember { mutableStateOf(true) }
    val platformName = remember { getPlatform().name }   // <- expect/actual em ação

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Avatar(initials = "KMP", size = 112.dp)

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Workshop KMP", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(
                "Kotlin Multiplatform + Compose Multiplatform",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }


        val tight = PaddingValues(horizontal = 8.dp)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { following = !following }, modifier = Modifier.weight(1f), contentPadding = tight) {
                Text(if (following) "Seguindo ✓" else "Seguir")
            }
            OutlinedButton(onClick = {}, modifier = Modifier.weight(1f), contentPadding = tight) { Text("Mensagem") }
            FilledTonalButton(onClick = {}, modifier = Modifier.weight(1f), contentPadding = tight) { Text("Convidar") }
        }


        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatCard("3", "Plataformas", Modifier.weight(1f))
            StatCard("1", "Código", Modifier.weight(1f))
            StatCard("100%", "Kotlin", Modifier.weight(1f))
        }


        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                SettingRow(
                    title = "Tema escuro",
                    subtitle = "Troca o tema do app inteiro em tempo real",
                    checked = darkTheme,
                    onCheckedChange = onDarkThemeChange,
                )
                SettingRow(
                    title = "Notificações",
                    subtitle = "Exemplo de estado local da tela",
                    checked = notifications,
                    onCheckedChange = { notifications = it },
                )
            }
        }

        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Executando em", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                Text(platformName, style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    AppTheme(darkTheme = false) {
        ProfileScreen(darkTheme = false, onDarkThemeChange = {})
    }
}
