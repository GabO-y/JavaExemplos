/**
 * Exemplo 08 - Comandos de Repeticao
 *
 * Cobre (Todo.md #8):
 * - [x] for tradicional
 * - [x] for-each (enhanced for)
 * - [x] while
 * - [x] do-while
 *
 * Exemplo aplicado: treino de uma maratona organizado em voltas na pista,
 * usando os quatro lacos do Java para diferentes cenarioes.
 */

import java.util.List;
import java.util.Map;

public class Exemplo08_Repeticao {

    public static void main(String[] args) {
        System.out.println("=== for TRADICIONAL (contagem por indice) ===");

        // Conta de 1 a 5 com passo +1.
        for (int volta = 1; volta <= 5; volta++) {
            System.out.print(volta + " ");
        }
        System.out.println();

        // Passo negativo (decremento) para contagem regressiva.
        for (int i = 5; i >= 1; i--) {
            System.out.print(i + " ");
        }
        System.out.println();

        // Passo arbitrário: apenas voltas impares.
        for (int v = 1; v <= 10; v += 2) {
            System.out.print(v + " ");
        }
        System.out.println();

        // Tres secoes do for: inicialização; condicao; atualizacao.
        int totalVoltas = 0;
        for (int v = 0, alvo = 12; v < alvo; v += 3, totalVoltas++) {
            System.out.println("avanco de 3 em 3: " + v + " (iteracao " + totalVoltas + ")");
        }

        System.out.println();
        System.out.println("=== for-each (ENHANCED FOR / for avançado) ===");

        int[] tempos = {42, 39, 45, 40};
        int soma = 0;
        for (int tempo : tempos) {               // percorre o array sem indice
            soma += tempo;
        }
        System.out.println("soma dos tempos " + java.util.Arrays.toString(tempos) + " = " + soma);

        // for-each também funciona com listas, mapas e demais Iterable.
        List<String> compradores = List.of("Ana", "Bia", "Cadu");
        for (String nome : compradores) {
            System.out.println("correndo com: " + nome);
        }

        Map<String, Integer> patrocinadores = Map.of("Cafe", 3, "Padaria", 2, "Academia", 5);
        for (Map.Entry<String, Integer> ent : patrocinadores.entrySet()) {
            System.out.println("patrocinador " + ent.getKey() + " -> " + ent.getValue() + " km");
        }

        System.out.println();
        System.out.println("=== while (condicao no INICIO) ===");

        // while avalia a condicao ANTES de cada execucao.
        int hidratacao = 0;
        while (hidratacao < 3) {
            System.out.println("copa de agua " + (hidratacao + 1) + "/3");
            hidratacao++;
        }

        // Pode nem executar nenhuma vez, se a condicao ja for falsa.
        int distanciaExtra = 10;
        while (distanciaExtra > 20) {            // nunca executa
            System.out.println("nunca chega aqui");
        }
        System.out.println("while com condicao falsa de inicio: zero execucoes");

        // while classico ate achar um valor.
        int energia = 100;
        int gasto = 17;
        while (energia > 0) {
            System.out.print(energia + " ");
            energia -= gasto;                    // 100 -> 83 -> 66 -> 49 -> 32 -> 15
        }
        System.out.println(" | energia final: " + energia);

        System.out.println();
        System.out.println("=== do-while (condicao no FINAL) ===");

        // do-while garante pelo menos UMA execucao.
        int tentativas = 0;
        do {
            tentativas++;
            System.out.println("tentativa " + tentativas + " (rodou ao menos 1x)");
        } while (tentativas < 2);

        // Mesma condicao falsa do while, mas agora executa uma vez.
        int bacias = 0;
        do {
            System.out.println("do-while com condicao falsa executa 1x (bacia " + bacias + ")");
            bacias++;
        } while (bacias > 5);

        System.out.println();
        System.out.println("=== COMPARANDO OS QUATRO EM UM CENARIO ===");

        // Resumo do treino: distancia total por tipo de laco.
        int km = 0;
        for (int lap = 0; lap < 4; lap++) km += 2;          // 8 km (for trad.)
        for (int x : new int[] {2, 4}) km += x;             // 6 km (for-each)
        int w = 0;
        while (w < 3) { km += 2; w++; }                     // 6 km (while)
        int d = 0;
        do { km += 1; d++; } while (d < 2);                 // 2 km (do-while)
        System.out.println("distancia total no treino: " + km + " km");
    }
}
