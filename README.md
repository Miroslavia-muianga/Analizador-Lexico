# Analisador Léxico para Mini-Pascal

## Descrição

Este projecto consiste na implementação de um **Analisador Léxico** (Lexer) para um subconjunto da linguagem de programação Pascal, denominado **Mini-Pascal**, conforme a especificação de Welsh & McKeag (1980).

O analisador léxico é a primeira fase de um compilador. A sua função é ler o código fonte caractere a caractere e agrupá-los em unidades significativas denominadas **tokens**, classificando-os de acordo com a gramática léxica da linguagem.

Este trabalho foi desenvolvido no âmbito da cadeira de **Linguagens de Programação e Compiladores** do 3º ano de Licenciatura em Informática — Faculdade de Ciências, Departamento de Matemática e Informática, ano lectivo de 2026.

---

## Funcionalidades

- Leitura e análise de código fonte Mini-Pascal escrito directamente no editor ou carregado a partir de um ficheiro `.pas` ou `.txt`
- Identificação e classificação dos tokens nas seguintes categorias:
  - `SIMBOLO_ESPECIAL` — palavras reservadas e operadores (ex: `begin`, `end`, `:=`, `<=`)
  - `IDENTIFICADOR` — nomes de variáveis, funções e procedimentos definidos pelo utilizador
  - `CONSTANTE_INTEIRA` — sequências de dígitos (ex: `42`, `100`)
  - `CHARACTER_CONSTANT` — constantes de caractere entre aspas simples `'x'` ou aspas duplas `"texto"`
  - `LEXEMA_NAO_RECONHECIDO` — símbolos inválidos que não pertencem à gramática do Mini-Pascal
  - `EOF` — marcador de fim de ficheiro
- Detecção e recuperação de erros léxicos, permitindo que a análise continue mesmo após encontrar símbolos inválidos
- Exibição dos tokens numa tabela com valor, tipo, linha e coluna
- Contagem de tokens, identificadores, símbolos especiais e erros na barra de estado
- Suporte a comentários nos formatos `{ ... }` e `(* ... *)`
- Interface gráfica profissional com tema escuro e tema claro alternáveis

---

## Gramática Léxica Suportada

```
<identifier>         ::= <letter> { <letter or digit> }
<integer constant>   ::= <digit> { <digit> }
<character constant> ::= '<letter or digit>'  |  "<letter or digit> { <letter or digit> }"
<letter>             ::= a | b | ... | z | A | B | ... | Z
<digit>              ::= 0 | 1 | 2 | ... | 9
<special symbol>     ::= + | - | * | = | <> | < | > | <= | >= | ( | ) | [ | ] |
                         := | . | , | ; | : | .. | div | or | and | not | if |
                         then | else | of | while | do | begin | end | read |
                         write | var | array | function | procedure | program |
                         true | false | char | integer | boolean
```

---

## Estrutura do Projecto

```
src/
└── main/
    ├── java/
    │   └── org/analizador/
    │       ├── MainApp.java           — ponto de entrada da aplicação JavaFX
    │       ├── MainController.java    — controlador da interface gráfica
    │       ├── Lexer.java             — núcleo do analisador léxico
    │       ├── Token.java             — representação de um token
    │       └── TipoToken.java         — enumeração dos tipos de token
    └── resources/
        └── org/analizador/
            ├── main.fxml              — layout da interface gráfica
            ├── dark-theme.css         — tema escuro
            └── light-theme.css        — tema claro
```

---

## Tecnologias Utilizadas

| Tecnologia | Versão | Finalidade |
|---|---|---|
| Java | 17 | Linguagem de programação principal |
| JavaFX | 21 | Interface gráfica |
| Maven | 3.x | Gestão de dependências e build |
| Scene Builder | — | Design visual do layout FXML |
| IntelliJ IDEA | 2025 | Ambiente de desenvolvimento |

---

## Como Executar

### Pré-requisitos

- Java 17 ou superior instalado
- Maven instalado ou IntelliJ IDEA com suporte a Maven

### Execução pelo IntelliJ IDEA

1. Abre o projecto no IntelliJ IDEA
2. Aguarda o Maven descarregar as dependências
3. No painel Maven, navega até **Plugins → javafx → javafx:run**
4. Faz duplo clique em `javafx:run`

### Execução pela linha de comandos

```bash
mvn javafx:run
```

---

## Como Utilizar

1. Escreve código Mini-Pascal directamente no editor à esquerda, ou clica em **📂 Abrir** para carregar um ficheiro existente
2. Clica em **▶ Analisar** para iniciar a análise léxica
3. Os tokens identificados aparecem na tabela à direita, com o respectivo tipo, linha e coluna
4. Os erros léxicos detectados aparecem no painel inferior direito
5. A barra de estado na parte inferior mostra o total de tokens, identificadores, símbolos especiais e erros
6. Usa o botão **☀ Tema** para alternar entre o tema escuro e o tema claro
7. Clica em **✕ Limpar** para limpar o editor e os resultados

---

## Exemplo de Código Mini-Pascal

```pascal
program exemplo;
var
  x, y : integer;
  nome : char;
begin
  read(x, y);
  if x > y then
    write(x)
  else
    write(y);
  x := x + 1;
  while x > 0 do
  begin
    y := y * x;
    x := x - 1
  end
end.
```

---

## Autores

**Miroslavia Muianga**
**Edna Maibaze**
**Elias Silva**
Licenciatura em Informática — 3º Ano
Faculdade de Ciências, Departamento de Matemática e Informática
Ano Lectivo: 2026
