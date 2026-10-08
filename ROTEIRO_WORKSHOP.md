# Roteiro — Demo KMP + Compose Multiplatform (15 min)

**Mensagem central:** *"Escrevi UMA vez — rede, JSON, ViewModel e UI — e rodou em Android, iOS e Desktop."*

App da demo: lista de personagens da **Rick and Morty API** → detalhes → perfil.
Stack: Ktor Client · kotlinx.serialization · ViewModel + StateFlow · Coil 3 · Material 3.

---

## 1. Preparação (antes do dia)

**Git: duas branches**
- `main` → projeto completo (seu "plano C").
- `workshop` → igual à main, mas com o conteúdo entre os marcadores `// >>> LIVE n (início)` / `// <<< LIVE n (fim)` removido e substituído por `TODO("LIVE n")` (em `LIVE 3`, troque o `when` por `Text("TODO")`). É a partir dela que você apresenta.

**Checklist T-1 dia**
- [ ] `./gradlew :desktopApp:run` funciona (baixa e cacheia todas as dependências).
- [ ] `./gradlew :androidApp:assembleDebug` e rodar no emulador.
- [ ] iOS: abrir `iosApp/` no Xcode, rodar no simulador (primeiro build é lento — **faça antes**).
- [ ] Deixe os 3 apps **já abertos** e posicionados lado a lado (Desktop | Android | iOS).
- [ ] Fonte da IDE em 18–20 pt; tema claro ou alto contraste; esconda notificações.
- [ ] Guarde os snippets da seção 4 num arquivo aberto em outra aba (copiar/colar é aceitável — digitar tudo não cabe em 15 min).

**Checklist T-30 min**
- [ ] Teste a internet e **ligue o hotspot do celular como reserva**.
- [ ] Rode o app uma vez em cada plataforma (aquece o Gradle e os caches).
- [ ] Plano B de rede testado: em `di/AppContainer.kt` troque `useFakeApi = true` e confirme que a lista aparece.

---

## 2. O que já está pronto × o que você faz ao vivo

| Pronto (só mostrar/explicar) | Ao vivo (digitar/colar) |
|---|---|
| Dependências no Gradle e `libs.versions.toml` | **LIVE 1** — chamada Ktor tipada (`KtorCharacterApi`) |
| Models `@Serializable` (`Character.kt`) | **LIVE 2** — `load()` no ViewModel publicando `UiState` |
| `createHttpClient()` (ContentNegotiation) | **LIVE 3** — `when (uiState)` na tela de lista |
| `UiState`, `Repository`, `AppContainer` | Alternar o Switch de tema nas 3 plataformas |
| Componentes (`CharacterCard`, `Avatar`, ...) | |
| Tela de Detalhes, Perfil, `App.kt` (navegação) | |
| `expect/actual` (`Platform`, `PlatformBackHandler`) | |

---

## 3. Linha do tempo (15 min)

> Versão de **12 min**: corte os itens marcados com ✂.

| Tempo | Bloco | O que fazer / dizer |
|---|---|---|
| 0:00–1:00 | **Abertura** | Mostre os 3 apps rodando lado a lado (versão completa da `main`, ou o Fake). *"Tudo isso sai de um único código Kotlin."* |
| 1:00–2:30 | **Tour da estrutura** | Árvore do `shared/src`: `commonMain` (data, presentation, ui, di) vs `androidMain`/`iosMain`/`jvmMain` (só engines HTTP + 1 `actual`). Mostre que `commonMain` tem ~95% do código. |
| 2:30–5:00 | **LIVE 1 — Rede + JSON** | Mostre `Character.kt` (`@Serializable`) e `createHttpClient()` (ContentNegotiation). Cole a chamada `client.get("character").body<CharacterResponse>()`. Destaque: *sem engine no código comum* — o Ktor escolhe pelo Gradle (OkHttp / Darwin / CIO). |
| 5:00–7:00 | **LIVE 2 — ViewModel + UiState** | Mostre `sealed interface UiState` (30 s). Cole `load()`. Explique `MutableStateFlow` privado → `StateFlow` público e `viewModelScope`. |
| 7:00–10:30 | **LIVE 3 — UI** | Cole o `when (uiState)` com `CircularProgressIndicator`, `LazyColumn` e `CharacterCard` (`AsyncImage`). Rode: lista aparece. Digite "rick" na busca ao vivo (debounce + cancelamento). Rode nas 3 plataformas. |
| 10:30–12:00 | **Detalhes + navegação** ✂ | Clique num card → detalhes. Mostre em `App.kt` que a navegação é só estado (`selected != null`) + `PlatformBackHandler`. Mencione Navigation Compose / Navigation 3 para produção. |
| 12:00–13:30 | **Perfil + "wow"** | Aba Perfil: avatar, botões, cards, switches. **Ligue o Tema escuro** → o app inteiro muda; repita no emulador/iOS. |
| 13:30–14:30 | **Escape hatch nativo** | `expect/actual`: `Platform.kt` (mostra "Executando em: Android 34 / iOS / Java 21") e `PlatformBackHandler`. *"Compartilhe o que quiser, vá nativo quando precisar."* |
| 14:30–15:00 | **Fechamento** | 3 takeaways (seção 6) + próximos passos. Abra para perguntas. |

**Dica de ouro (Desktop):** o README do template indica `./gradlew :desktopApp:hotRun --auto` (Compose Hot Reload). Se funcionar no seu ambiente, edite a UI (cor, padding, texto do card) e veja mudar **na hora** — o momento mais impressionante da demo. Teste antes.

---

## 4. Snippets para o LIVE (versões enxutas para digitar/colar)

**LIVE 1 — `data/remote/CharacterApi.kt`** (substitui o `TODO("LIVE 1")`)
```kotlin
override suspend fun getCharacters(name: String?): CharacterResponse =
    client.get("character") {
        if (!name.isNullOrBlank()) parameter("name", name.trim())
    }.body<CharacterResponse>()
```
*Depois, mencione a pegadinha:* a API devolve **404** quando a busca não acha nada. A versão completa na `main` trata isso (`ClientRequestException` → lista vazia).

**LIVE 2 — `presentation/CharactersViewModel.kt`**
```kotlin
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
            UiState.Error(e.message ?: "Erro desconhecido")
        }
    }
}
```

**LIVE 3 — `ui/screens/CharactersScreen.kt`** (dentro do `Column`, após o campo de busca)
```kotlin
when (uiState) {
    UiState.Loading -> CenteredBox { CircularProgressIndicator() }
    is UiState.Error -> CenteredBox {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(uiState.message, color = MaterialTheme.colorScheme.error)
            Button(onClick = onRetry) { Text("Tentar novamente") }
        }
    }
    is UiState.Success -> LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(uiState.data, key = { it.id }) { character ->
            CharacterCard(character, onClick = { onCharacterClick(character) })
        }
    }
}
```

---

## 5. Trechos que DEVEM ser destacados (e o que falar)

| # | Trecho | Fala sugerida |
|---|---|---|
| 1 | `@Serializable data class Character(...)` + `ignoreUnknownKeys = true` | "Sem reflexão: o compilador gera o parser. Por isso roda no iOS nativo." |
| 2 | `client.get("character").body<CharacterResponse>()` | "Uma linha: request, JSON e objeto tipado. Idêntico nas 3 plataformas." |
| 3 | Gradle: `androidMain → okhttp`, `iosMain → darwin`, `jvmMain → cio` | "O código comum não sabe qual engine usa. Cada plataforma usa a rede nativa dela." |
| 4 | `sealed interface UiState<out T>` + `when` exaustivo | "Se eu esquecer um estado, o app nem compila." |
| 5 | `private val _uiState` / `val uiState = _uiState.asStateFlow()` | "Só o ViewModel escreve; a UI só observa. Fluxo de dados de mão única." |
| 6 | `class CharactersViewModel : ViewModel()` em `commonMain` + `viewModelScope` | "O mesmo ViewModel do AndroidX, agora multiplataforma." |
| 7 | `loadJob?.cancel()` + `delay(400)` | "Debounce em 3 linhas: se o usuário continua digitando, a busca antiga é cancelada." |
| 8 | `collectAsStateWithLifecycle()` em código comum | "StateFlow vira estado do Compose, respeitando o ciclo de vida." |
| 9 | `LazyColumn` + `Card` + `AsyncImage` | "A mesma lista, nativa e fluida, em Android, iOS e Desktop." |
| 10 | `AppTheme(darkTheme)` + Switch no Perfil | "Um Boolean trocando o visual das 3 plataformas ao vivo." |
| 11 | `expect fun PlatformBackHandler` / `actual` | "Quando a plataforma exige algo próprio, é só `expect/actual`." |

---

## 6. Fechamento — 3 takeaways

1. **Lógica + UI compartilhadas** em um único módulo Kotlin: menos código, menos bugs, mesmo comportamento.
2. **Adoção incremental e acesso nativo**: dá para compartilhar só a lógica ou também a UI, e `expect/actual` cobre o resto.
3. **Ecossistema maduro**: Ktor, kotlinx.serialization, ViewModel, Coil, Compose — tudo multiplataforma.

**Próximos passos (cite, não demonstre):** Navigation Compose / Navigation 3 · DI com Koin · persistência com Room KMP / SQLDelight · testes de ViewModel com `kotlinx-coroutines-test` · separar o módulo `shared` em `data` / `ui`.

---

## 7. Plano de contingência

| Problema | Solução |
|---|---|
| Sem internet | `useFakeApi = true` em `AppContainer` (as imagens ficam no placeholder cinza — explique que é o estado de erro do `AsyncImage`). Ou hotspot. |
| Build do iOS demora/falha | Deixe o app iOS **já instalado e aberto** no simulador; mostre-o rodando e não faça build ao vivo. |
| Erro de compilação ao colar | Volte para a `main` (`git checkout main`) e continue narrando com o código pronto. |
| Atrasou | Corte "Detalhes + navegação" (✂) e fale dela em 20 s durante o Perfil. |
| Ktor/Coil não acha engine | Confira que `androidMain`/`iosMain`/`jvmMain` têm o engine no `shared/build.gradle.kts`. |

---

## 8. Perguntas prováveis

- **"A UI é nativa no iOS?"** Compose Multiplatform renderiza com Skia (desenho próprio), não com UIKit. Dá para embutir SwiftUI/UIKit onde precisar. Se preferir UI 100% nativa, compartilhe só a lógica (ViewModel/Repository) e use SwiftUI.
- **"E performance?"** O código Kotlin compila para nativo no iOS (Kotlin/Native) e JVM no Android/Desktop.
- **"Dá para migrar aos poucos?"** Sim: comece compartilhando uma camada (ex.: rede) em app existente.
- **"Qual biblioteca de navegação/DI/banco?"** Navigation Compose ou Navigation 3, Koin, Room KMP ou SQLDelight.
- **"Por que não `ktor-client-json`?"** Foi descontinuado; hoje é `ktor-client-content-negotiation` + `ktor-serialization-kotlinx-json`.
