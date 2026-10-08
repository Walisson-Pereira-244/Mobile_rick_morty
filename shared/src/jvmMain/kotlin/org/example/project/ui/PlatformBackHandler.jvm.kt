package org.example.project.ui

import androidx.compose.runtime.Composable

// Desktop: sem botão "voltar" de sistema; usamos o botão ← da TopAppBar.
@Composable
actual fun PlatformBackHandler(enabled: Boolean, onBack: () -> Unit) = Unit
