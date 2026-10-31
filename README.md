# ModaDeBolsa

Aplicação Android para organização de moldes de costura, tecidos e cores.

## Tecnologias
- Java
- Android
- Room (SQLite)
- Gradle com Kotlin DSL

## Comandos essenciais Git

Fluxo habitual:
```bash
git status
git add .
git commit -m "Descrição das alterações"
git push
```

## Base de dados

A base de dados local utiliza Room e inclui seis entidades:

- `CategoriaMolde` e `Molde`
- `Tecido` e `CaracteristicasTecido`
- `CategoriaCor` e `Cor` (Extra)

Cada entidade tem um DAO e um Repository para gerir as operações de consulta e alteração dos dados. A base de dados inclui relações entre entidades, chaves estrangeiras e dados iniciais.


No Windows, utiliza `gradlew.bat` em vez de `./gradlew`.

## Estado do projeto
A base de dados Room está implementada e os testes dos dados iniciais foram executados com sucesso. Da interface, estão concluídos (mas devem ser melhorados) o ecrã de categorias de moldes e a lista de moldes por categoria (com pesquisa), assim como a barra de navegação inferior; os restantes ecrãs estão por desenvolver.
