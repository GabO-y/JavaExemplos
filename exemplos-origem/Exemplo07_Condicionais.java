/**
 * Exemplo 07 - Comandos Condicionais
 *
 * Exemplo aplicado: bilheteria de um teatro com regras de desconto
 * usando todos os comandos condicionais do Java.
 */

public class Exemplo07_Condicionais {

    record Pedido(double valor, int idade, String tipo) {}

    public static void main(String[] args) {
        System.out.println("=== if / else if / else ===");

        int idade = 17;
        if (idade < 12) {
            System.out.println("meia-entrada: crianca");
        } else if (idade <= 18) {
            System.out.println("meia-entrada: adolescente");
        } else if (idade >= 60) {
            System.out.println("meia-entrada: idoso");
        } else {
            System.out.println("ingresso inteiro");
        }

        // if classico tambem aceita blocos aninhados.
        int notas = 2;
        boolean sozinho = true;
        if (notas > 0) {
            System.out.println("tem " + notas + " notas");
            if (sozinho) {
                System.out.println("e esta sozinho: vale a promocao individual");
            }
        }

        System.out.println();
        System.out.println("=== OPERADOR TERNARIO (condicao ? a : b) ===");

        // Forma compacta de retornar um de dois valores.
        double precoBase = 30.0;
        double preco = (idade < 12) ? precoBase / 2 : precoBase;
        System.out.println("preco para idade " + idade + " = R$" + preco);

        // Ternario pode ser aninhado, mas deve ser usado com cuidado.
        String faixa = idade < 2 ? "bebe"
                     : idade < 13 ? "crianca"
                     : idade < 20 ? "adolescente"
                     : "adulto";
        System.out.println("faixa etaria: " + faixa);

        System.out.println();
        System.out.println("=== switch TRADICIONAL (com case com virgula) ===");

        String dia = "ter";
        switch (dia) {
            case "seg", "ter", "qua", "qui", "sex" -> System.out.println("dia util");
            case "sab", "dom" -> System.out.println("fim de semana");
            default -> System.out.println("dia invalido");
        }

        // Forma antiga com :, break e fall-through (sem break escorrega).
        String mes = "dez";
        switch (mes) {
            case "jan": case "mar": case "mai": case "jul":
            case "ago": case "out": case "dez":
                System.out.println(mes + " tem 31 dias");
                break;
            case "fev":
                System.out.println(mes + " tem 28/29 dias");
                break;
            default:
                System.out.println(mes + " tem 30 dias");
        }

        System.out.println();
        System.out.println("=== switch EXPRESSION (produz valor) ===");

        int assento = 2;
        String zona = switch (assento) {
            case 1, 2 -> "VIP";
            case 3, 4, 5 -> "plateia";
            default -> "arquibancada";
        };
        System.out.println("assento " + assento + " fica na zona: " + zona);

        // Com yield para logica mais elaborada dentro do caso.
        int tempoLivro = 40; // minutos
        String duracao = switch (tempoLivro) {
            case 30 -> { yield "curto"; }
            default -> {
                if (tempoLivro > 60) yield "longo";
                else yield "medio";
            }
        };
        System.out.println("peça de " + tempoLivro + "min: " + duracao);

        System.out.println();
        System.out.println("=== PATTERN MATCHING: instanceof ===");

        Object item = "Camerote";
        // instanceof com padrao (Java 16+): declara 's' dentro do if.
        if (item instanceof String s) {
            System.out.println("item e String em minusculas: " + s.toLowerCase());
        } else {
            System.out.println("item nao e String");
        }

        Object numero = 7;
        if (numero instanceof Integer n) {
            System.out.println("numero inteiro: " + (n * 3));
        }

        System.out.println();
        System.out.println("=== PATTERN MATCHING: switch com padroes (Java 21) ===");

        System.out.println("formatar(\"olá\") = " + formatar("olá"));
        System.out.println("formatar(42) = " + formatar(42));
        System.out.println("formatar(9.5) = " + formatar(9.5));
        System.out.println("formatar(null) = " + formatar(null));

        System.out.println();
        System.out.println("=== API de testes (Pedido) ===");

        // Resumo da bilheteria usando os recursos vistos.
        String resumo = tiqueTaquia(new Pedido(30.0, 65, "pessoa"));
        System.out.println(resumo);
    }

    // switch com pattern matching: domina o tipo de cada caso.
    static String formatar(Object o) {
        return switch (o) {
            case String s -> "String: '" + s + "'";
            case Integer i -> "Inteiro: " + i;
            case Double d -> "Double: " + d;
            case null -> "nulo";
            default -> "outro: " + o.getClass().getSimpleName();
        };
    }

    // if/else combinado com switch para gerar o resumo da venda.
    static String tiqueTaquia(Pedido pedido) {
        double bruto = pedido.valor();
        double liquido = bruto;
        String motivo = "";

        boolean age = pedido.idade() < 12 || pedido.idade() >= 60;
        boolean temCortesia = pedido.tipo().equals("cortesia");

        if (temCortesia) {
            motivo = "cortesia (100%)";
            liquido = 0;
        } else if (age) {
            motivo = "meia-entrada (50%)";
            liquido = bruto / 2;
        } else {
            motivo = "inteira";
        }

        String atracoes = switch (pedido.tipo()) {
            case "pessoa" -> "1 sessao";
            case "grupo" -> "2 sessoes";
            default -> "sob consulta";
        };

        return "Pedido: R$" + bruto + " -> " + motif(motivo, liquido) + " | shows: " + atracoes + " | pagamentos: "
                + (liquido == 0 ? "gratis" : "R$" + liquido);
    }

    static String motif(String motivo, double valor) {
        return valor == 0 ? "gratis" : motivo + " (" + valor + ")";
    }
}
