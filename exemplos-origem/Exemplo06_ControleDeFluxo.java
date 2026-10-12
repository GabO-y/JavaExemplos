/**
 * Exemplo 06 - Estruturas de Controle e Desvios de Fluxo
 *
 * Cobre (Todo.md #6):
 * - [x] break, continue, return
 * - [x] break/continue rotulados
 * - [x] yield (switch expressions)
 *   tags: mind break
 *
 * ------------------------------------------------------------------
 * NOTAS SOBRE AS FEATURES DEMONSTRADAS:
 * ------------------------------------------------------------------
 *
 * 1) `break`
 *    - Interrompe (sai) do loop ou switch MAIS INTERNO onde aparece.
 *    - Sem rótulo, sai apenas do laço atual.
 *
 * 2) `continue`
 *    - Pula o restante da iteração atual e vai para a PRÓXIMA iteração
 *      do loop MAIS INTERNO.
 *
 * 3) `return`
 *    - Encerra o método atual e devolve (opcionalmente) um valor ao
 *      chamador. Em `void`, apenas encerra o método.
 *
 * 4) rótulos (labels) em break/continue
 *    - Um rótulo é um identificador seguido de ":" antes de um laço.
 *    - `break rotulo;`   => sai COMPLETAMENTE do laço rotulado
 *                           (útil para escapar de laços aninhados).
 *    - `continue rotulo;` => salta para a PRÓXIMA iteração do laço
 *                           rotulado (não apenas o mais interno).
 *    - Sem rótulo você só consegue afetar o laço mais interno.
 *
 * 5) `yield` (Java 14+)
 *    - Usado SOMENTE dentro de um bloco `{ ... }` de um `case` de uma
 *      SWITCH EXPRESSION para devolver um valor.
 *    - Em corpos de uma linha (case X -> "valor";) não precisa de yield.
 *    - Diferente de Thread.yield(), que é outra coisa (dica ao escalonador).
 */
public class Exemplo06_ControleDeFluxo {

    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println(" EXEMPLO 06 - CONTROLE DE FLUXO");
        System.out.println("==================================================\n");

        // -----------------------------------------------------------------
        // 1) break SIMPLES (sem rótulo) - sai apenas do laço mais interno
        // -----------------------------------------------------------------
        System.out.println("[1] break simples (sem rótulo):");
        for (int i = 0; i < 5; i++) {
            if (i == 3) {
                System.out.println("  break em i=" + i + " -> sai do for");
                break; // sai do laço atual
            }
            System.out.println("  i = " + i);
        }

        // -----------------------------------------------------------------
        // 2) continue SIMPLES - pula para a próxima iteração do laço atual
        // -----------------------------------------------------------------
        System.out.println("\n[2] continue simples (sem rótulo):");
        for (int i = 0; i < 5; i++) {
            if (i % 2 == 0) {
                System.out.println("  i=" + i + " (par) -> continue, pula o print abaixo");
                continue;
            }
            System.out.println("  i=" + i + " (ímpar) processado");
        }

        // -----------------------------------------------------------------
        // 3) return - encerra o método
        //    Aqui demonstramos em um método auxiliar (nao no main, senao
        //    o programa terminaria). Veja get_one() no final do arquivo.
        // -----------------------------------------------------------------
        System.out.println("\n[3] return em método auxiliar:");
        int valor = get_one();
        System.out.println("  get_one() retornou: " + valor);

        // -----------------------------------------------------------------
        // 4) break ROTULADO - sai de TODOS os laços rotulados de uma vez
        // -----------------------------------------------------------------
        System.out.println("\n[4] break rotulado (quebra os dois fors de uma vez):");

        outerBreak:
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i == 1 && j == 1) {
                    System.out.println("  i=" + i + ", j=" + j + " -> break outerBreak");
                    break outerBreak; // sai de TODOS os laços
                }
                System.out.println("  i=" + i + ", j=" + j);
            }
        }
        System.out.println("  -> após o break rotulado (fora de todos os laços)");

        // -----------------------------------------------------------------
        // 5) continue ROTULADO - pula para a próxima iteração do laço EXTERNO
        // -----------------------------------------------------------------
        System.out.println("\n[5] continue rotulado (pula iteração do for externo):");

        outerContinue:
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (j == 1) {
                    System.out.println("  i=" + i + ", j=" + j +
                            " -> continue outerContinue (abandona j e vai p/ próximo i)");
                    continue outerContinue;
                }
                System.out.println("  i=" + i + ", j=" + j);
            }
        }

        // -----------------------------------------------------------------
        // 6) yield em switch expression
        //    Aqui usamos um valor variável para produzir saídas diferentes.
        // -----------------------------------------------------------------
        System.out.println("\n[6] yield em switch expression:");

        for (int k = 0; k <= 2; k++) {
            String nome = nomear(k);
            System.out.println("  valor=" + k + " -> nome='" + nome + "'");
        }

        System.out.println("\n==================================================");
        System.out.println(" FIM DO EXEMPLO 06");
        System.out.println("==================================================");
    }

    /**
     * Método auxiliar que demonstra `return`.
     * Devolve sempre 1 (usado para o exemplo de switch expression).
     */
    public static int get_one() {
        // `return` encerra o método e devolve o valor ao chamador.
        return 1;
    }

    /**
     * Demonstra `yield` dentro de uma switch expression (Java 14+).
     * Cada `case` com bloco `{ ... }` precisa terminar com `yield valor;`
     * para produzir o resultado da expressão.
     */
    public static String nomear(int v) {
        return switch (v) {
            case 1 -> {
                // bloco multi-linha => precisa de yield
                String s = "Jonh Galli A";
                yield s;
            }
            case 0 -> {
                yield "Formaggio";
            }
            default ->  {
                yield "";
            }
        };
    }
}
