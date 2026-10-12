/**
 * Exemplo 14 - Analise Lexica
 *
 * Cobre (fase de compilacao):
 * - [x] Papel do analisador lexico (scanner): transformar caracteres em TOKENS
 * - [x] Categorias de token: identificador, palavra-chave, numero, operador,
 *     separador, string literal
 * - [x] Tabela de simbolos para identificadores
 * - [x] Deteccao de erro lexico (caractere invalido)
 *
 * ------------------------------------------------------------------
 * NOTAS:
 * ------------------------------------------------------------------
 * A analise lexica e a PRIMEIRA fase do compilador. Ela le o fluxo de
 * caracteres e produz uma sequencia de tokens que a analise sintatica
 * consome. Lexemas iguais podem virar tokens diferentes conforme o contexto
 * (ex.: "if" e palavra-chave, "iff" e identificador).
 */

import java.util.ArrayList;
import java.util.List;

public class Exemplo14_AnaliseLexica {

    enum Tipo {
        IDENTIFICADOR, PALAVRA_CHAVE, NUMERO, OPERADOR, SEPARADOR, TEXTO, FIM
    }

    record Token(Tipo tipo, String lexema, int posicao) {
        @Override
        public String toString() {
            return String.format("%-14s '%s'", tipo, lexema);
        }
    }

    // Palavras reservadas da linguagem em analise.
    static final List<String> PALAVRAS_CHAVE =
            List.of("if", "else", "while", "int", "return", "def");

    static class Lexer {
        private final String fonte;
        private int pos = 0;

        Lexer(String fonte) {
            this.fonte = fonte;
        }

        List<Token> tokenizar() {
            List<Token> tokens = new ArrayList<>();
            while (pos < fonte.length()) {
                char c = fonte.charAt(pos);

                if (Character.isWhitespace(c)) {
                    pos++;
                } else if (Character.isLetter(c)) {
                    tokens.add(lerIdentificador());
                } else if (Character.isDigit(c)) {
                    tokens.add(lerNumero());
                } else if (c == '"') {
                    tokens.add(lerTexto());
                } else if ("+-*/=<>!".indexOf(c) >= 0) {
                    tokens.add(new Token(Tipo.OPERADOR, String.valueOf(c), pos++));
                } else if ("(){};,.".indexOf(c) >= 0) {
                    tokens.add(new Token(Tipo.SEPARADOR, String.valueOf(c), pos++));
                } else {
                    throw new ErroLexico("caractere invalido '" + c + "' na posicao " + pos);
                }
            }
            tokens.add(new Token(Tipo.FIM, "<EOF>", pos));
            return tokens;
        }

        private Token lerIdentificador() {
            int inicio = pos;
            while (pos < fonte.length() && Character.isLetterOrDigit(fonte.charAt(pos))) {
                pos++;
            }
            String lexema = fonte.substring(inicio, pos);
            Tipo tipo = PALAVRAS_CHAVE.contains(lexema) ? Tipo.PALAVRA_CHAVE : Tipo.IDENTIFICADOR;
            return new Token(tipo, lexema, inicio);
        }

        private Token lerNumero() {
            int inicio = pos;
            while (pos < fonte.length() && Character.isDigit(fonte.charAt(pos))) {
                pos++;
            }
            return new Token(Tipo.NUMERO, fonte.substring(inicio, pos), inicio);
        }

        private Token lerTexto() {
            int inicio = pos;
            pos++; // consome a aspa inicial
            StringBuilder sb = new StringBuilder();
            while (pos < fonte.length() && fonte.charAt(pos) != '"') {
                sb.append(fonte.charAt(pos));
                pos++;
            }
            if (pos >= fonte.length()) {
                throw new ErroLexico("texto nao fechado a partir da posicao " + inicio);
            }
            pos++; // consome a aspa final
            return new Token(Tipo.TEXTO, sb.toString(), inicio);
        }
    }

    static class ErroLexico extends RuntimeException {
        ErroLexico(String msg) {
            super(msg);
        }
    }

    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println(" EXEMPLO 14 - ANALISE LEXICA");
        System.out.println("==================================================\n");

        String fonte = "int soma = a + 42 * (b - 7);\n"
                + "if soma > 10 { return \"ok\"; }";

        System.out.println("[1] Codigo-fonte analisado:");
        System.out.println("--------------------------------------------------");
        System.out.println(fonte);
        System.out.println("--------------------------------------------------");
        System.out.println();

        System.out.println("[2] Sequencia de tokens produzida pelo scanner:");
        List<Token> tokens = new Lexer(fonte).tokenizar();
        for (Token t : tokens) {
            System.out.println("  " + t);
        }
        System.out.println("  total de tokens = " + tokens.size());

        System.out.println();

        // -----------------------------------------------------------------
        // [3] Tabela de simbolos: identificadores distintos encontrados.
        // -----------------------------------------------------------------
        System.out.println("[3] Tabela de simbolos (identificadores):");
        List<String> simbolos = new ArrayList<>();
        for (Token t : tokens) {
            if (t.tipo() == Tipo.IDENTIFICADOR && !simbolos.contains(t.lexema())) {
                simbolos.add(t.lexema());
            }
        }
        System.out.println("  identificadores: " + simbolos);

        System.out.println();

        // -----------------------------------------------------------------
        // [4] Erro lexico: caractere invalido.
        // -----------------------------------------------------------------
        System.out.println("[4] Deteccao de erro lexico:");
        try {
            new Lexer("int x = 3 $ 4;").tokenizar();
        } catch (ErroLexico e) {
            System.out.println("  ErroLexico -> " + e.getMessage());
        }

        System.out.println("\n==================================================");
        System.out.println(" FIM DO EXEMPLO 14");
        System.out.println("==================================================");
    }
}
