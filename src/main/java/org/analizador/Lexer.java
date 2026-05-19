package org.analizador;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Lexer {

    private String codigo;    // código fonte completo
    private int pos;          // posição actual no codigo
    private int linha;         // linha actual
    private int coluna;       // coluna actual
    private List<Token> tokens;
    private List<String> erros;

    private static final Set<String> SIMBOLOS_ESPECIAIS = Set.of(
            "program", "begin", "end", "var", "array", "of",
            "if", "then", "else", "while", "do", "read", "write",
            "div", "or", "and", "not", "function", "procedure",
            "true", "false", "char", "integer", "boolean", "+",
            "-", "*", "=", "<>", "<", ">", "<=", ">=", "(", ")",
            "[", "]", ":=", ".", ",", ";", ":" ,".."
    );

public static final Set<Character> DIGITOS =Set.of('0','1','2','3','4','5','6','7','8','9');
public static final Set<Character> LETRAS = Set.of('a','b','c','d','e','f','g','h','i','j','k','l',
        'm','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K',
        'L','M','N','O','P','Q','R', 'S','T','U','V','W','X','Y','Z');

    public Lexer(String codigo) {
        this.codigo = codigo;
        this.pos = 0;
        this.linha = 1;
        this.coluna = 1;
        this.tokens = new ArrayList<>();
        this.erros = new ArrayList<>();
    }

    public List<Token> getTokens() { return tokens; }
    public List<String> getErros() { return erros; }

    // retorna o caractere actual sem avançar
    private char peek() {
        if (pos >= codigo.length()) return '\0';
        return codigo.charAt(pos);
    }

    // retorna o próximo caractere (pos+1) sem avançar
    private char peekNext() {
        if (pos + 1 >= codigo.length()) return '\0';
        return codigo.charAt(pos + 1);
    }

    // avança uma posição e retorna o caractere actual
    private char advance() {
        char c = codigo.charAt(pos++);
        if (c == '\n') { linha++; coluna = 1; }
        else { coluna++; }
        return c;
    }

    // ignora espaços, tabulações e quebras de linha
    private void skipWhitespace() {
        while (pos < codigo.length() && Character.isWhitespace(peek())) {
            advance();
        }
    }

    // ignora comentários { ... } e (* ... *)
    private void skipComment() {
        if (peek() == '{') {
            advance();
            while (pos < codigo.length() && peek() != '}') advance();
            if (pos < codigo.length()) advance(); // consome o '}'
        } else if (peek() == '(' && peekNext() == '*') {
            advance(); advance(); // consome '(' e '*'
            while (pos < codigo.length()) {
                if (peek() == '*' && peekNext() == ')') {
                    advance(); advance(); // consome '*' e ')'
                    break;
                }
                advance();
            }
        }
    }

    private boolean isLetter(char c) {
        return LETRAS.contains(c);
    }

    private boolean isLetterOrDigit(char c) {
        return LETRAS.contains(c)|| DIGITOS.contains(c);
    }

    public boolean isDigit(char c) {
        return DIGITOS.contains(c);
    }

    private Token lerIdentificadorOuSimbolo() {
        int linhaInicial = linha;
        int colunaInicial = coluna;
        StringBuilder sb = new StringBuilder();

        while (pos < codigo.length() && isLetterOrDigit(peek())) {
            sb.append(advance());
        }

        String valor = sb.toString();
        String lower = valor.toLowerCase();

        if (SIMBOLOS_ESPECIAIS.contains(lower)) {
            return new Token(TipoToken.SIMBOLO_ESPECIAL, valor, linhaInicial, colunaInicial);
        }

        return new Token(TipoToken.IDENTIFICADOR, valor, linhaInicial, colunaInicial);
    }

    private Token lerInteiro() {
        int linhaInicial = linha;
        int colunaInicial = coluna;
        StringBuilder sb = new StringBuilder();

        while (pos < codigo.length() && isDigit(peek())) {
            sb.append(advance());
        }

        return new Token(TipoToken.CONSTANTE_INTEIRA, sb.toString(), linhaInicial, colunaInicial);
    }

    private Token lerConstantChar() {
        int startLine = linha;
        int startCol = coluna;
        StringBuilder sb = new StringBuilder();

        if (peek()=='"') {
            sb.append(advance()); // consome o ' inicial

            while (pos < codigo.length() && peek() != '"') {
                sb.append(advance());
            }

            if (pos >= codigo.length()) {
                // chegou ao fim sem fechar a string
                String msg = String.format("Linha %d, col %d: constante de caractere não terminada: %s", startLine, startCol, sb);
                erros.add(msg);
                return new Token(TipoToken.LEXEMA_NAO_RECONHECIDO, sb.toString(), startLine, startCol);
            }
        } else if (peek()=='\'') {
            sb.append(advance()); // consome o ' inicial

            while (pos < codigo.length() && peek() != '\'') {
                sb.append(advance());
            }

            if (pos >= codigo.length()) {
                // chegou ao fim sem fechar a string
                String msg = String.format("Linha %d, col %d: constante de caractere não terminada: %s", startLine, startCol, sb);
                erros.add(msg);
                return new Token(TipoToken.LEXEMA_NAO_RECONHECIDO, sb.toString(), startLine, startCol);
            }
        }
        sb.append(advance()); // consome o ' inicial

        while (pos < codigo.length() && peek() != '\'') {
            sb.append(advance());
        }

        if (pos >= codigo.length()) {
            // chegou ao fim sem fechar a string
            String msg = String.format("Linha %d, col %d: constante de caractere não terminada: %s", startLine, startCol, sb);
            erros.add(msg);
            return new Token(TipoToken.LEXEMA_NAO_RECONHECIDO, sb.toString(), startLine, startCol);
        }

        sb.append(advance()); // consome o ' final
        return new Token(TipoToken.CHARACTER_CONSTANT, sb.toString(), startLine, startCol);
    }

    private Token lerOperadorOuSymbol() {
        int startLine = linha;
        int startCol = coluna;
        char c = advance();

        switch (c) {
            case '+': return new Token(TipoToken.SIMBOLO_ESPECIAL, "+", startLine, startCol);
            case '-': return new Token(TipoToken.SIMBOLO_ESPECIAL, "-", startLine, startCol);
            case '*': return new Token(TipoToken.SIMBOLO_ESPECIAL, "*", startLine, startCol);
            case '=': return new Token(TipoToken.SIMBOLO_ESPECIAL, "=", startLine, startCol);
            case ';': return new Token(TipoToken.SIMBOLO_ESPECIAL, ";", startLine, startCol);
            case ',': return new Token(TipoToken.SIMBOLO_ESPECIAL, ",", startLine, startCol);
            case '(': return new Token(TipoToken.SIMBOLO_ESPECIAL, "(", startLine, startCol);
            case ')': return new Token(TipoToken.SIMBOLO_ESPECIAL, ")", startLine, startCol);
            case '[': return new Token(TipoToken.SIMBOLO_ESPECIAL, "[", startLine, startCol);
            case ']': return new Token(TipoToken.SIMBOLO_ESPECIAL, "]", startLine, startCol);

            case '<':
                if (peek() == '=') { advance(); return new Token(TipoToken.SIMBOLO_ESPECIAL, "<=", startLine, startCol); }
                if (peek() == '>') { advance(); return new Token(TipoToken.SIMBOLO_ESPECIAL, "<>", startLine, startCol); }
                return new Token(TipoToken.SIMBOLO_ESPECIAL, "<", startLine, startCol);

            case '>':
                if (peek() == '=') { advance(); return new Token(TipoToken.SIMBOLO_ESPECIAL, ">=", startLine, startCol); }
                return new Token(TipoToken.SIMBOLO_ESPECIAL, ">", startLine, startCol);

            case ':':
                if (peek() == '=') { advance(); return new Token(TipoToken.SIMBOLO_ESPECIAL, ":=", startLine, startCol); }
                return new Token(TipoToken.SIMBOLO_ESPECIAL, ":", startLine, startCol);

            case '.':
                if (peek() == '.') { advance(); return new Token(TipoToken.SIMBOLO_ESPECIAL, "..", startLine, startCol); }
                return new Token(TipoToken.SIMBOLO_ESPECIAL, ".", startLine, startCol);

            default:
                String msg = String.format("Linha %d, col %d: símbolo inválido '%c'", startLine, startCol, c);
                erros.add(msg);
                return new Token(TipoToken.LEXEMA_NAO_RECONHECIDO, String.valueOf(c), startLine, startCol);
        }
    }
    public List<Token> tokenize() {
        while (pos < codigo.length()) {
            skipWhitespace();

            if (pos >= codigo.length()) break;

            // ignora comentários
            if (peek() == '{' || (peek() == '(' && peekNext() == '*')) {
                skipComment();
                continue;
            }

            // identificadores e palavras-chave
            if (isLetter(peek())) {
                tokens.add(lerIdentificadorOuSimbolo());
                continue;
            }

            // números inteiros
            if (Character.isDigit(peek())) {
                tokens.add(lerInteiro());
                continue;
            }

            // constantes de caracteres
            if (peek() == '\'') {
                tokens.add(lerConstantChar());
                continue;
            }

            // operadores e símbolos — NÃO COMENTAR!
            tokens.add(lerOperadorOuSymbol());
        }

        tokens.add(new Token(TipoToken.EOF, "EOF", linha, coluna));
        return tokens;
    }
}
