package org.analizador;
public class Main {
    static void main(String[] args) {
        String code = """
                program teste;
                var
                  x y : "areaes";
                begin
                  read(x, y);
                  if x > y then
                    write(x)
                  else
                    write(y)
                end.
                """;

        Lexer lexer = new Lexer(code);
        lexer.tokenize();

        System.out.println("=== TOKENS ===");
        for (Token t : lexer.getTokens()) {
            System.out.println(t);
        }

        System.out.println("\n=== ERROS ===");
        if (lexer.getErros().isEmpty()) {
            System.out.println("Nenhum erro encontrado.");
        } else {
            for (String err : lexer.getErros()) {
                System.out.println(err);
            }
        }
    }
}
