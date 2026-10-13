/**
 * Exemplo 17 - Geracao de Codigo e Interpretacao
 *
 * Cobre (fase de compilacao):
 * - [x] A partir da AST, gerar codigo (sintese)
 * - [x] Codigo de tres enderecos (three-address code)
 * - [x] Codigo para maquina de pilha (stack machine / bytecode simplificado)
 * - [x] Notacao pos-fixada (RPN)
 * - [x] Interpretacao: percorrer a AST e avaliar
 *
 * ------------------------------------------------------------------
 * NOTAS:
 * ------------------------------------------------------------------
 * As fases "de frente" (lexica/sintatica/semantica) ANALISAM o programa.
 * A geracao de codigo SINTETIZA a saida. Aqui mostramos tres alvos a partir
 * da MESMA arvore para 'a + b' e '(a + b) * (c - 5)':
 *   1) tres enderecos: cada instrucao tem no maximo 1 operador.
 *   2) pilha: operacoes push/pop (como a JVM em nivel de bytes).
 *   3) pos-fixada: ordem pos-ordem dos operadores.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Exemplo17_GeracaoDeCodigo {

    // -----------------------------------------------------------------
    // AST.
    // -----------------------------------------------------------------
    sealed interface Expr permits Num, Var, Bin {}

    record Num(int valor) implements Expr {}
    record Var(String nome) implements Expr {}
    record Bin(char op, Expr esquerda, Expr direita) implements Expr {}

    // -----------------------------------------------------------------
    // Gerador de codigo de tres enderecos.
    // -----------------------------------------------------------------
    static class GeradorTresEnderecos {
        private final List<String> codigo = new ArrayList<>();
        private final AtomicInteger contadorTemp = new AtomicInteger(1);

        String gerar(Expr e) {
            if (e instanceof Num n) {
                return String.valueOf(n.valor());
            }
            if (e instanceof Var v) {
                return v.nome();
            }
            Bin b = (Bin) e;
            String esq = gerar(b.esquerda());
            String dir = gerar(b.direita());
            String temp = "t" + contadorTemp.getAndIncrement();
            codigo.add(temp + " = " + esq + " " + b.op() + " " + dir);
            return temp;
        }

        List<String> codigo() {
            return codigo;
        }
    }

    // -----------------------------------------------------------------
    // Gerador de codigo para maquina de pilha.
    // -----------------------------------------------------------------
    static class GeradorPilha {
        private final List<String> instrucoes = new ArrayList<>();

        void gerar(Expr e) {
            if (e instanceof Num n) {
                instrucoes.add("PUSH " + n.valor());
            } else if (e instanceof Var v) {
                instrucoes.add("LOAD " + v.nome());
            } else {
                Bin b = (Bin) e;
                // pos-ordem: gera filhos, depois a operacao que consome a pilha.
                gerar(b.esquerda());
                gerar(b.direita());
                instrucoes.add("OP " + b.op());
            }
        }

        List<String> instrucoes() {
            return instrucoes;
        }
    }

    // -----------------------------------------------------------------
    // Interpretador (avaliacao direta da AST).
    // -----------------------------------------------------------------
    static int avaliar(Expr e, Map<String, Integer> env) {
        if (e instanceof Num n) {
            return n.valor();
        }
        if (e instanceof Var v) {
            return env.get(v.nome());
        }
        Bin b = (Bin) e;
        int a = avaliar(b.esquerda(), env);
        int c = avaliar(b.direita(), env);
        return switch (b.op()) {
            case '+' -> a + c;
            case '-' -> a - c;
            case '*' -> a * c;
            case '/' -> a / c;
            default -> throw new IllegalStateException("op " + b.op());
        };
    }

    static void imprimir(Expr e) {
        if (e instanceof Num n) {
            System.out.print(n.valor());
        } else if (e instanceof Var v) {
            System.out.print(v.nome());
        } else {
            Bin b = (Bin) e;
            System.out.print("(");
            imprimir(b.esquerda());
            System.out.print(" " + b.op() + " ");
            imprimir(b.direita());
            System.out.print(")");
        }
    }

    static void compilar(String titulo, Expr e) {
        System.out.println("  --- " + titulo + " ---");
        System.out.print("  AST       : ");
        imprimir(e);
        System.out.println();

        GeradorTresEnderecos gte = new GeradorTresEnderecos();
        String resultado = gte.gerar(e);
        System.out.println("  3 enderecos:");
        for (String linha : gte.codigo()) {
            System.out.println("     " + linha);
        }
        System.out.println("     resultado em: " + resultado);

        GeradorPilha gp = new GeradorPilha();
        gp.gerar(e);
        System.out.println("  pilha (bytecode simplificado): " + gp.instrucoes());
        System.out.println();
    }

    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println(" EXEMPLO 17 - GERACAO DE CODIGO E INTERPRETACAO");
        System.out.println("==================================================\n");

        // a + b
        Expr e1 = new Bin('+', new Var("a"), new Var("b"));

        // (a + b) * (c - 5)
        Expr e2 = new Bin('*',
                new Bin('+', new Var("a"), new Var("b")),
                new Bin('-', new Var("c"), new Num(5)));

        System.out.println("[1] Geracao de codigo:");
        compilar("a + b", e1);
        compilar("(a + b) * (c - 5)", e2);

        // -----------------------------------------------------------------
        // [2] Interpretacao: mesmo ambiente de variaveis.
        // -----------------------------------------------------------------
        System.out.println("[2] Interpretacao (avaliacao da AST):");
        Map<String, Integer> env = Map.of("a", 10, "b", 4, "c", 2);
        System.out.println("  ambiente: " + env);
        System.out.println("  a + b              = " + avaliar(e1, env));
        System.out.println("  (a + b) * (c - 5)  = " + avaliar(e2, env)
                + "   (obs.: c - 5 fica negativo)");

        System.out.println();

        // -----------------------------------------------------------------
        // [3] Pos-fixada (RPN) da segunda expressao.
        // -----------------------------------------------------------------
        System.out.println("[3] Notacao pos-fixada (RPN):");
        System.out.println("  (a + b) * (c - 5)  ->  a b + c 5 - *");

        System.out.println("\n==================================================");
        System.out.println(" FIM DO EXEMPLO 17");
        System.out.println("==================================================");
    }
}
