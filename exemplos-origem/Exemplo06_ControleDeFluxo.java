/**
 * Exemplo 06 - Estruturas de Controle e Desvios de Fluxo
 *
 * Exemplo aplicado: um controle de jornada de trabalho em uma escala,
 * usando os desvios de fluxo para percorrer turnos e registrar metas.
 */

import java.util.List;

public class Exemplo06_ControleDeFluxo {

    public static void main(String[] args) {
        System.out.println("=== break: INTERROMPE O LACO ===");

        // Encontra o primeiro numero multiplo de 7 e encerra o loop.
        for (int n = 1; ; n++) {
            if (n % 7 == 0) {
                System.out.println("primeiro multiplo de 7 encontrado: " + n);
                break;                          // sai do for
            }
        }

        // break tambem encerra o switch tradicional.
        int dia = 3;
        switch (dia) {
            case 1: System.out.println("segunda"); break;
            case 2: System.out.println("terca");   break;
            case 3: System.out.println("quarta");  break;
            default: System.out.println("outro dia");
        }

        System.out.println();
        System.out.println("=== continue: PULA PARA A PROXIMA ITERACAO ===");

        // Imprime apenas os turnos pares da semana.
        for (int turno = 1; turno <= 6; turno++) {
            if (turno % 2 != 0) {
                continue;                       // pula os impares
            }
            System.out.println("registrando turno par: " + turno);
        }

        // continue em while: cuidado com o contador (aqui incrementa antes).
        int hora = 0;
        while (hora < 10) {
            hora++;
            if (hora == 3) {
                continue;                       // ignora o intervalo das 3
            }
            System.out.println("hora marcada: " + hora);
        }

        System.out.println();
        System.out.println("=== return: ENCERRA O METODO ===");

        System.out.println("maiorTurno(3, 7) = " + maiorTurno(3, 7));
        System.out.println("classificarPontos(15) = " + classificarPontos(15));

        // return sem valor encerra o metodo agradecendo.
        mostrarAviso(false);

        System.out.println();
        System.out.println("=== break ROTULADO: SAI DE LACOS ANINHADOS ===");

        // Rotulo 'externo:' identifica o laco externo.
        externo:
        for (int linha = 1; linha <= 3; linha++) {
            for (int coluna = 1; coluna <= 3; coluna++) {
                if (linha * coluna == 9) {
                    System.out.println("break rotulado em linha=" + linha + ", coluna=" + coluna);
                    break externo;              // sai dos DOIS lacos
                }
                System.out.println("  par (" + linha + "," + coluna + ")");
            }
        }
        System.out.println("fluxo continua apos o laco externo");

        System.out.println();
        System.out.println("=== continue ROTULADO: PULA O LACO EXTERNO ===");

        externo:
        for (int semana = 1; semana <= 2; semana++) {
            for (int diaUt = 1; diaUt <= 5; diaUt++) {
                if (diaUt == 3) {
                    continue externo;          // proxima semana direto
                }
                System.out.println("semana " + semana + " dia " + diaUt);
            }
        }

        System.out.println();
        System.out.println("=== yield: VALOR RETORNADO PELA SWITCH EXPRESSION ===");

        // switch EXPRESSION (Java 14+): produz um valor, sem fall-through.
        String turno = "noite";
        String periodo = switch (turno) {
            case "manha" -> "turno da manha";
            case "tarde" -> "turno da tarde";
            case "noite" -> "turno da noite";
            default -> "desconhecido";
        };
        System.out.println("turno=" + turno + " -> " + periodo);

        // yield permite logica dentro dos casos da switch expression.
        int ptos = 82;
        String conceito = switch (ptos) {
            case 50 -> { yield "recuperacao"; }
            default -> {
                if (ptos > 70) yield "aprovado";
                else yield "reprovado";
            }
        };
        System.out.println("ptos=" + ptos + " -> " + conceito);

        // yield tambem funciona na forma antiga (com break 'valor').
        List<String> frutas = List.of("maca", "banana", "uva");
        for (String fruta : frutas) {
            String categoria = switch (fruta) {
                case "maca" -> "vermelha";
                default -> {
                    yield "outra";
                }
            };
            System.out.println(fruta + ": " + categoria);
        }
    }

    // return com valor: termina o metodo e devolve resultado.
    static int maiorTurno(int a, int b) {
        if (a > b) {
            return a;
        }
        return b;
    }

    // return sem valor: termina prematuramente.
    static String classificarPontos(int pontos) {
        if (pontos >= 10) {
            return "meta atingida";
        }
        return "meta pendente";
    }

    static void mostrarAviso(boolean exibir) {
        if (!exibir) {
            System.out.println("(aviso desativado; return sem valor executado)");
            return;                            // encerra aqui
        }
        System.out.println("aviso na tela");
    }
}
