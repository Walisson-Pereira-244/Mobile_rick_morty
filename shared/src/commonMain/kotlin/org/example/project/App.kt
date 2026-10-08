package org.example.project

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.example.project.di.AppContainer
import org.example.project.presentation.CharactersViewModel
import org.example.project.ui.PlatformBackHandler
import org.example.project.ui.screens.CharactersScreen
import org.example.project.ui.screens.DetailScreen
import org.example.project.ui.screens.ProfileScreen
import org.example.project.ui.theme.AppTheme

private const val TAB_CHARACTERS = 0
private const val TAB_PROFILE = 1


@Composable
fun App() {

    val systemDark = isSystemInDarkTheme()
    var darkTheme by rememberSaveable { mutableStateOf(systemDark) }
    var tab by rememberSaveable { mutableStateOf(TAB_CHARACTERS) }

    AppTheme(darkTheme) {

        val viewModel = viewModel { CharactersViewModel(AppContainer.characterRepository) }


        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val query by viewModel.query.collectAsStateWithLifecycle()
        val selected by viewModel.selected.collectAsStateWithLifecycle()


        PlatformBackHandler(enabled = selected != null) { viewModel.onBack() }

        val detail = selected
        Scaffold(
            topBar = {
                AppTopBar(
                    title = when {
                        detail != null -> detail.name
                        tab == TAB_CHARACTERS -> "Rick & Morty"
                        else -> "Perfil / Sobre"
                    },
                    onBack = if (detail != null) viewModel::onBack else null,
                )
            },
            bottomBar = {
                if (detail == null) AppBottomBar(selectedTab = tab, onTabSelected = { tab = it })
            },
        ) { innerPadding ->
            Box(Modifier.fillMaxSize().padding(innerPadding)) {
                when {
                    detail != null -> DetailScreen(detail)

                    tab == TAB_CHARACTERS -> CharactersScreen(
                        uiState = uiState,
                        query = query,
                        onQueryChange = viewModel::onQueryChange,
                        onRetry = viewModel::retry,
                        onCharacterClick = viewModel::onCharacterClick,
                    )

                    else -> ProfileScreen(
                        darkTheme = darkTheme,
                        onDarkThemeChange = { darkTheme = it },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppTopBar(title: String, onBack: (() -> Unit)?) {
    TopAppBar(
        title = { Text(title, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        navigationIcon = {
            if (onBack != null) {
                IconButton(onClick = onBack) { Text("←", style = MaterialTheme.typography.titleLarge) }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
    )
}

@Composable
private fun AppBottomBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedTab == TAB_CHARACTERS,
            onClick = { onTabSelected(TAB_CHARACTERS) },
            icon = { Text("☰") },
            label = { Text("Personagens") },
        )
        NavigationBarItem(
            selected = selectedTab == TAB_PROFILE,
            onClick = { onTabSelected(TAB_PROFILE) },
            icon = { Text("☺") },
            label = { Text("Perfil") },
        )
    }
}
