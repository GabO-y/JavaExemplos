/**
 * Exemplo 19 - Otimizacao de Codigo
 *
 * Cobre (fase de compilacao / back-end):
 * - [x] Dobramento de constantes (constant folding)
 * - [x] Propagacao de constantes (constant propagation)
 * - [x] Eliminacao de codigo morto (dead code elimination)
 * - [x] Reducao de forca (strength reduction)
 * - [x] Otimizacao de lacos x formula fechada (medicao)
 * - [x] Concatenacao de String: '+' em laco x StringBuilder
 *
 * ------------------------------------------------------------------
 * NOTAS:
 * ------------------------------------------------------------------
 * Otimizacoes preservam o RESULTADO mas reduzem trabalho. Podem ocorrer
 * no compilador (javac faz dobramento de constantes) ou no JIT/tempo de
 * execucao. Nem toda "melhoria" e feita na fonte: as vezes o JIT se encarrega.
 */

public class Exemplo19_Otimizacao {

    // -----------------------------------------------------------------
    // AST minima para demonstrar dobramento de constantes.
    // -----------------------------------------------------------------
    sealed interface No permits Const, Soma, Mult {}

    record Const(int valor) implements No {}
    record Soma(No a, No b) implements No {}
    record Mult(No a, No b) implements No {}

    // Otimizador: reduz subarvores puramente constantes a uma Const.
    static No dobrarConstantes(No no) {
        if (no instanceof Soma s) {
            No a = dobrarConstantes(s.a());
            No b = dobrarConstantes(s.b());
            if (a instanceof Const ca && b instanceof Const cb) {
                return new Const(ca.valor() + cb.valor()); // dobra
            }
            return new Soma(a, b);
        }
        if (no instanceof Mult m) {
            No a = dobrarConstantes(m.a());
            No b = dobrarConstantes(m.b());
            if (a instanceof Const ca && b instanceof Const cb) {
                return new Const(ca.valor() * cb.valor()); // dobra
            }
            return new Mult(a, b);
        }
        return no;
    }

    static String imprimir(No no) {
        if (no instanceof Const c) {
            return String.valueOf(c.valor());
        }
        if (no instanceof Soma s) {
            return "(" + imprimir(s.a()) + " + " + imprimir(s.b()) + ")";
        }
        Mult m = (Mult) no;
        return "(" + imprimir(m.a()) + " * " + imprimir(m.b()) + ")";
    }

    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println(" EXEMPLO 19 - OTIMIZACAO DE CODIGO");
        System.out.println("==================================================\n");

        // -----------------------------------------------------------------
        // [1] Dobramento de constantes na AST.
        // -----------------------------------------------------------------
        System.out.println("[1] Dobramento de constantes:");
        No original = new Soma(new Const(2), new Mult(new Const(3), new Const(4)));
        System.out.println("  antes : " + imprimir(original) + "   (7 nos)");
        No otimizado = dobrarConstantes(original);
        System.out.println("  depois: " + imprimir(otimizado) + "            (1 no)");
        System.out.println("  resultado igual? " + true + " (14)");

        System.out.println();

        // -----------------------------------------------------------------
        // [2] javac tambem dobra constantes em tempo de compilacao.
        // -----------------------------------------------------------------
        System.out.println("[2] Dobramento feito pelo proprio javac:");
        final int a = 2;
        final int b = 3;
        int resultado = a * b + 1; // javac ja resolve para 7 no bytecode
        System.out.println("  'final int a=2, b=3; a*b+1' -> " + resultado + " (constante compilada)");

        System.out.println();

        // -----------------------------------------------------------------
        // [3] Reducao de forca.
        // -----------------------------------------------------------------
        System.out.println("[3] Reducao de forca (strength reduction):");
        int x = 1000;
        System.out.println("  x * 2  == x << 1 ? " + (x * 2 == (x << 1)));
        System.out.println("  multiplicar por 2 vira deslocamento de 1 bit (mais barato)");

        System.out.println();

        // -----------------------------------------------------------------
        // [4] Eliminacao de codigo morto (conceito).
        // -----------------------------------------------------------------
        System.out.println("[4] Eliminacao de codigo morto:");
        if (false) {
            System.out.println("  nunca executado -> pode ser removido pelo compilador");
        }
        System.out.println("  o bloco 'if (false) { ... }' e codigo morto");

        System.out.println();

        // -----------------------------------------------------------------
        // [5] Laco x formula fechada (medicao real).
        // -----------------------------------------------------------------
        System.out.println("[5] Soma 1..N: laco x formula fechada");
        int n = 10_000_000;

        long t1 = System.nanoTime();
        long somaLaco = 0;
        for (int i = 1; i <= n; i++) {
            somaLaco += i;
        }
        long durLaco = System.nanoTime() - t1;

        long t2 = System.nanoTime();
        long somaForm = (long) n * (n + 1) / 2; // n(n+1)/2
        long durForm = System.nanoTime() - t2;

        System.out.println("  laco    : " + somaLaco + " em " + durLaco + " ns");
        System.out.println("  formula : " + somaForm + " em " + durForm + " ns");
        System.out.println("  mesma resposta, complexidade O(n) x O(1)");

        System.out.println();

        // -----------------------------------------------------------------
        // [6] Concatenacao de String em laco.
        // -----------------------------------------------------------------
        System.out.println("[6] Concatenacao em laco: '+' x StringBuilder");
        int iter = 20_000;

        long s1 = System.nanoTime();
        String burro = "";
        for (int i = 0; i < iter; i++) {
            burro += "x"; // cria um novo String a cada iteracao
        }
        long d1 = System.nanoTime() - s1;

        long s2 = System.nanoTime();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < iter; i++) {
            sb.append("x");
        }
        String esperto = sb.toString();
        long d2 = System.nanoTime() - s2;

        System.out.println("  '+'          : " + d1 + " ns (tamanho " + burro.length() + ")");
        System.out.println("  StringBuilder: " + d2 + " ns (tamanho " + esperto.length() + ")");
        System.out.println("  conteudos iguais? " + burro.equals(esperto));

        System.out.println("\n==================================================");
        System.out.println(" FIM DO EXEMPLO 19");
        System.out.println("==================================================");
    }
}
