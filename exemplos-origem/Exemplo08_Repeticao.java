/**
 * Exemplo 08 - Comandos de Repeticao
 *
 * Cobre (Todo.md #8):
 * - [x] for tradicional
 * - [x] for-each (enhanced for)
 * - [x] while
 * - [x] do-while
 *
 * ------------------------------------------------------------------
 * NOTAS SOBRE AS FEATURES DEMONSTRADAS:
 * ------------------------------------------------------------------
 *
 * 1) `for` tradicional
 *    - Forma: for (inicializacao; condicao; incremento) { corpo }
 *    - A inicializacao roda UMA vez antes do laco.
 *    - A condicao e checada ANTES de cada iteracao (pre-teste).
 *    - O incremento roda ao FINAL de cada iteracao.
 *    - Ideal quando se sabe o numero de iteracoes ou precisa do indice.
 *
 * 2) `for-each` (enhanced for)
 *    - Forma: for (Tipo var : colecaoOuArray) { corpo }
 *    - Percorre TODOS os elementos, sem indice.
 *    - Funciona com arrays e qualquer coisa que implemente Iterable<T>.
 *    - Mais limpo, mas nao permite pular ou acessar indice diretamente.
 *
 * 3) `while`
 *    - Forma: while (condicao) { corpo }
 *    - Pre-teste: a condicao e avaliada ANTES de entrar no corpo.
 *    - Se a condicao ja for falsa, o corpo NUNCA executa.
 *    - Ideal quando o numero de iteracoes e desconhecido.
 *
 * 4) `do-while`
 *    - Forma: do { corpo } while (condicao);
 *    - Pos-teste: o corpo roda AO MENOS UMA VEZ, depois a condicao e checada.
 *    - ⚠️ O `;` apos o `while (...)` e OBRIGATORIO.
 *    - Uso tipico: menus, validacao de entrada, loop que precisa rodar pelo
 *      menos uma vez.
 *
 * 5) break / continue dentro dos lacos
 *    - `break`    => sai imediatamente do laco.
 *    - `continue` => pula para a proxima iteracao.
 *    - (Ver Exemplo06_ControleDeFluxo para rotulos e yield.)
 *
 * 6) ⚠️ CUIDADO COM LOOP INFINITO
 *    - while(true)  /  for(;;)  => loop infinito intencional (use break).
 *    - Se esquecer de atualizar a variavel de controle, o laco nunca termina.
 */

import java.util.List;
import java.util.Map;

public class Exemplo08_Repeticao {

    public static void main(String[] args) {

        // -----------------------------------------------------------------
        // Acumulador compartilhado entre os 4 lacos.
        // Cada laco altera ele, e o proximo laco parte do resultado anterior.
        // -----------------------------------------------------------------
        Integer acumulator = 0;

        System.out.println("==================================================");
        System.out.println(" EXEMPLO 08 - COMANDOS DE REPETICAO");
        System.out.println("==================================================");
        System.out.println("acumulator inicial = " + acumulator + "\n");

        // -----------------------------------------------------------------
        // [1] for tradicional
        //     Inicializa i=0; enquanto i < 20; incrementa i++.
        //     Roda 20 vezes, somando 1 ao acumulator a cada iteracao.
        //     Resultado: acumulator = 20.
        // -----------------------------------------------------------------
        System.out.println("[1] for tradicional (20 iteracoes somando +1):");

        for (int i = 0; i < 20; i++) {
            acumulator++;
        }

        System.out.println("  acumulator apos o for = " + acumulator);

        // -----------------------------------------------------------------
        // [2] for-each
        //     Percorre o array `values` do inicio ao fim, sem indice.
        //     Soma cada elemento ao acumulator.
        //     values = {1,2,3,4,5,6,7,8,9,0} => soma = 45.
        //     Resultado: acumulator = 20 + 45 = 65.
        // -----------------------------------------------------------------
        System.out.println("\n[2] for-each (soma todos os elementos do array):");

        Integer[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};

        for (Integer i : values) {
            acumulator += i;
        }

        System.out.println("  soma do array values = 45");
        System.out.println("  acumulator apos o for-each = " + acumulator);

        // -----------------------------------------------------------------
        // [3] while (pre-teste)
        //     Enquanto acumulator < 1000, incrementa +1.
        //     Para quando acumulator >= 1000.
        //     Como entra com 65, faz 935 iteracoes ate chegar a 1000.
        //     Resultado: acumulator = 1000.
        // -----------------------------------------------------------------
        System.out.println("\n[3] while (incrementa ate acumulator >= 1000):");

        while (acumulator < 1000 ){

            acumulator++;
        }

        System.out.println("  acumulator apos o while = " + acumulator);

        // -----------------------------------------------------------------
        // [4] do-while (pos-teste)
        //     Executa o corpo AO MENOS UMA VEZ, depois checa a condicao.
        //     Enquanto acumulator < 1600, incrementa +1.
        //     Como 1000 < 1600, faz 600 iteracoes ate chegar a 1600.
        //     Resultado final: acumulator = 1600.
        // -----------------------------------------------------------------
        System.out.println("\n[4] do-while (incrementa ate acumulator >= 1600):");

        do {

            acumulator++;
        } while (acumulator < 1600);

        System.out.println("  acumulator apos o do-while = " + acumulator);

        // -----------------------------------------------------------------
        // [5] Demonstracao extra (nao altera `acumulator`):
        //     diferenca entre while (pre-teste) e do-while (pos-teste).
        //     Aqui a condicao ja nasce FALSA nos dois casos.
        // -----------------------------------------------------------------
        System.out.println("\n[5] while vs do-while com condicao FALSA de inicio:");

        int condFalsa = 0;

        // while: condicao falsa => corpo NUNCA roda
        while (condFalsa > 0) {
            System.out.println("  while  -> executou (nao deveria!)");
        }
        System.out.println("  while   -> corpo NAO executou (pre-teste)");

        // do-while: corpo roda UMA vez, depois a condicao falha
        do {
            System.out.println("  do-while -> corpo executou UMA vez (pos-teste)");
        } while (condFalsa > 0);

        System.out.println("\n==================================================");
        System.out.println(" acumulator FINAL = " + acumulator);
        System.out.println("==================================================");
        System.out.println(" FIM DO EXEMPLO 08");
        System.out.println("==================================================");
    }
}
