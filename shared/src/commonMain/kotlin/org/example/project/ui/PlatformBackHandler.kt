package org.example.project.ui

import androidx.compose.runtime.Composable

/**
 * Intercepta o botão/gesto "voltar" do sistema.
 * Só o Android tem esse conceito -> cada plataforma fornece sua implementação (expect/actual).
 */
@Composable
expect fun PlatformBackHandler(enabled: Boolean, onBack: () -> Unit)
