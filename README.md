# ModaDeBolsa

Aplicação Android para organização de moldes de costura, tecidos e cores.

## Tecnologias
- Java
- Android
- Room (SQLite)
- Gradle com Kotlin DSL

## Base de dados

A base de dados local utiliza Room e inclui seis entidades:

- `CategoriaMolde` e `Molde`
- `Tecido` e `CaracteristicasTecido`
- `CategoriaCor` e `Cor` (Extra)

Cada entidade tem um DAO para gerir as operações de consulta e alteração dos dados. A base de dados inclui relações entre entidades, chaves estrangeiras e dados iniciais.

## Testes

O teste `DadosIniciaisTest` verifica a criação das quatro categorias iniciais de moldes.

Para executar os testes de instrumentação, com um emulador ou dispositivo ligado:

```bash
./gradlew connectedDebugAndroidTest
```

No Windows, utiliza `gradlew.bat` em vez de `./gradlew`.

## Estado do projeto

A base de dados Room está implementada e os testes dos dados iniciais foram executados com sucesso. A interface da aplicação encontra-se por desenvolver.

