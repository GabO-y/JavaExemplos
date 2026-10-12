/**
 * Exemplo 15 - Analise Sintatica
 *
 * Cobre (fase de compilacao):
 * - [x] Gramatica livre de contexto (EBNF) da linguagem em analise
 * - [x] Parser por DESCIDA RECURSIVA (recursive descent)
 * - [x] Precedencia/associatividade codificadas na hierarquia de regras
 * - [x] Construcao de Arvore Sintatica Abstrata (AST)
 * - [x] Deteccao de erro sintatico (token inesperado)
 *
 * ------------------------------------------------------------------
 * GRAMATICA (EBNF) - a precedencia aumenta conforme se desce:
 * ------------------------------------------------------------------
 *   expressao  -> termo   { ('+' | '-') termo }
 *   termo      -> fator   { ('*' | '/') fator }
 *   fator      -> NUMERO | '(' expressao ')'
 *
 *   * 'termo' chama primeiro o '+', depois '*' (precedencia), e a
 *     repeticao 'while' em cada nivel da associatividade a ESQUERDA.
 */

public class Exemplo15_AnaliseSintatica {

    // -----------------------------------------------------------------
    // Lexer minimo (so numeros e operadores) embutido no exemplo.
    // -----------------------------------------------------------------
    enum Tipo { NUMERO, MAIS, MENOS, VEZES, DIV, ABRE, FECHA, FIM }

    record Token(Tipo tipo, double valor, String texto) {}

    static class Lexer {
        private final String s;
        private int pos = 0;

        Lexer(String s) {
            this.s = s;
        }

        Token proximo() {
            while (pos < s.length() && Character.isWhitespace(s.charAt(pos))) {
                pos++;
            }
            if (pos >= s.length()) {
                return new Token(Tipo.FIM, 0, "<EOF>");
            }
            char c = s.charAt(pos);
            switch (c) {
                case '+': pos++; return new Token(Tipo.MAIS, 0, "+");
                case '-': pos++; return new Token(Tipo.MENOS, 0, "-");
                case '*': pos++; return new Token(Tipo.VEZES, 0, "*");
                case '/': pos++; return new Token(Tipo.DIV, 0, "/");
                case '(': pos++; return new Token(Tipo.ABRE, 0, "(");
                case ')': pos++; return new Token(Tipo.FECHA, 0, ")");
                default:
                    if (Character.isDigit(c) || c == '.') {
                        int inicio = pos;
                        while (pos < s.length()
                                && (Character.isDigit(s.charAt(pos)) || s.charAt(pos) == '.')) {
                            pos++;
                        }
                        String num = s.substring(inicio, pos);
                        return new Token(Tipo.NUMERO, Double.parseDouble(num), num);
                    }
                    throw new ErroSintatico("caractere inesperado: '" + c + "'");
            }
        }
    }

    static class ErroSintatico extends RuntimeException {
        ErroSintatico(String msg) {
            super(msg);
        }
    }

    // -----------------------------------------------------------------
    // Nos da AST.
    // -----------------------------------------------------------------
    sealed interface No permits Numero, BinOp {
        double avaliar();
        String imprimir();
    }

    record Numero(double valor) implements No {
        public double avaliar() { return valor; }
        public String imprimir() { return String.valueOf(valor); }
    }

    record BinOp(char operador, No esquerda, No direita) implements No {
        public double avaliar() {
            double a = esquerda.avaliar();
            double b = direita.avaliar();
            return switch (operador) {
                case '+' -> a + b;
                case '-' -> a - b;
                case '*' -> a * b;
                case '/' -> a / b;
                default -> throw new IllegalStateException("op: " + operador);
            };
        }

        public String imprimir() {
            return "(" + esquerda.imprimir() + " " + operador + " " + direita.imprimir() + ")";
        }
    }

    // -----------------------------------------------------------------
    // Parser por descida recursiva.
    // -----------------------------------------------------------------
    static class Parser {
        private final Lexer lexer;
        private Token atual;

        Parser(Lexer lexer) {
            this.lexer = lexer;
            this.atual = lexer.proximo();
        }

        private void casar(Tipo esperado) {
            if (atual.tipo() == esperado) {
                atual = lexer.proximo();
            } else {
                throw new ErroSintatico("esperado " + esperado + ", encontrado " + atual.tipo());
            }
        }

        No parse() {
            No no = expressao();
            if (atual.tipo() != Tipo.FIM) {
                throw new ErroSintatico("token inesperado: " + atual.texto());
            }
            return no;
        }

        // expressao -> termo { ('+'|'-') termo }
        private No expressao() {
            No no = termo();
            while (atual.tipo() == Tipo.MAIS || atual.tipo() == Tipo.MENOS) {
                char op = atual.tipo() == Tipo.MAIS ? '+' : '-';
                atual = lexer.proximo();
                no = new BinOp(op, no, termo());
            }
            return no;
        }

        // termo -> fator { ('*'|'/') fator }
        private No termo() {
            No no = fator();
            while (atual.tipo() == Tipo.VEZES || atual.tipo() == Tipo.DIV) {
                char op = atual.tipo() == Tipo.VEZES ? '*' : '/';
                atual = lexer.proximo();
                no = new BinOp(op, no, fator());
            }
            return no;
        }

        // fator -> NUMERO | '(' expressao ')'
        private No fator() {
            if (atual.tipo() == Tipo.NUMERO) {
                double v = atual.valor();
                atual = lexer.proximo();
                return new Numero(v);
            }
            if (atual.tipo() == Tipo.ABRE) {
                casar(Tipo.ABRE);
                No no = expressao();
                casar(Tipo.FECHA);
                return no;
            }
            throw new ErroSintatico("fator invalido: " + atual.texto());
        }
    }

    static void analisar(String fonte) {
        System.out.println("  fonte : " + fonte);
        try {
            No ast = new Parser(new Lexer(fonte)).parse();
            System.out.println("  AST   : " + ast.imprimir());
            System.out.println("  valor : " + ast.avaliar());
        } catch (ErroSintatico e) {
            System.out.println("  ErroSintatico -> " + e.getMessage());
        }
        System.out.println();
    }

    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println(" EXEMPLO 15 - ANALISE SINTATICA");
        System.out.println("==================================================\n");

        System.out.println("[1] Expressoes validas (precedencia e parenteses):");
        analisar("2 + 3 * 4");
        analisar("(2 + 3) * 4");
        analisar("10 - 4 - 3");
        analisar("100 / 5 / 2");

        System.out.println("[2] Expressao com erro sintatico:");
        analisar("2 + * 3");

        System.out.println("[3] Expressao com parentese nao fechado:");
        analisar("(2 + 3");

        System.out.println("==================================================");
        System.out.println(" FIM DO EXEMPLO 15");
        System.out.println("==================================================");
    }
}
