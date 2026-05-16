package org.analizador;
public class Token {
    public TipoToken tipo;
    public String valor;
    public int linha;
    public int coluna;

    public Token(TipoToken tipo, String valor, int linha, int coluna) {
        this.tipo = tipo;
        this.valor = valor;
        this.linha = linha;
        this.coluna = coluna;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getTipo() {
        return tipo.toString();
    }

    public void setTipo(TipoToken tipo) {
        this.tipo = tipo;
    }

    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }

    public int getColuna() {
        return coluna;
    }

    public void setColuna(int coluna) {
        this.coluna = coluna;
    }

    @Override
    public String toString() {
        return String.format("Token(%s, '%s', linha %d, col %d)", tipo, valor, linha, coluna);
    }
}
