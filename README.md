Rick and Morty - Kotlin Multiplatform

Projeto que fiz para um workshop sobre Kotlin Multiplatform. É um app que lista os personagens da série usando a API https://rickandmortyapi.com, com busca, tela de detalhes e uma tela de perfil. O mesmo código roda no Android e no Desktop (o iOS também está no projeto, mas só compila em Mac).

O que tem no app
Lista de personagens com campo de busca
Tela de detalhes ao clicar em um personagem
Tela de perfil com avatar, botões, cards e switches (o switch de tema escuro funciona de verdade)
Tela de carregando e de erro, com botão para tentar de novo
Tecnologias
Kotlin Multiplatform e Compose Multiplatform
Ktor para chamar a API
kotlinx.serialization para converter o JSON
Coil para carregar as imagens
ViewModel com StateFlow (arquitetura MVVM)
Como o código está organizado

Quase tudo fica na pasta shared/src/commonMain, que é o código compartilhado:

data: modelo, chamada da API e repositório
presentation: ViewModel e os estados da tela (carregando, sucesso, erro)
ui: telas, componentes e tema
di: monta as dependências

As pastas androidMain, iosMain e jvmMain têm só o que muda em cada plataforma, como o motor de rede.

Como rodar

Precisa do JDK 21 e do Android Studio.

Abra no Android Studio a pasta que tem o arquivo settings.gradle.kts e espere o Gradle sincronizar. Na primeira vez demora.
Android: escolha androidApp, selecione um emulador e clique em Run.
Desktop: rode no terminal ./gradlew :desktopApp:run (no Windows: gradlew.bat :desktopApp:run).
iOS: só em Mac, abrindo a pasta iosApp no Xcode.
Sem internet

Se estiver sem internet, dá para usar dados de exemplo. No arquivo AppContainer.kt, mude useFakeApi para true. As imagens não carregam nesse modo.

Créditos

Dados da Rick and Morty API. O projeto começou do template oficial do Kotlin Multiplatform.
