/**
 * Exemplo 05 - Expressoes
 *
 * Exemplo aplicado: uma calculadora de contas de restaurante que avalia
 * expressoes aritmeticas e logicas.
 */

// Classe de apoio: nao ha como sobrecarregar operadores, mas podemos criar
// formas nomeadas (metodos) que fazem o papel das operacoes com objetos.
class Conta {

    /**
     * Representa os valores monetarios: cada unidade inteira equivale a
     * R$ 1,00. A classe NAO pode usar '+' com objetos, entao usamos metodos.
     */
    static double total(int[] itens, double imposto, double gorjetaPercentual) {
        double subtotal = 0;
        for (int preco : itens) {
            subtotal = subtotal + preco;              // '+': adicao (associativa a esquerda)
        }
        double valorGorjeta = subtotal * (gorjetaPercentual / 100.0);
        double valorImposto = subtotal * (imposto / 100.0);
        return subtotal + valorGorjeta + valorImposto;
    }

    static void tentarComOperadores() {
        // Com strings, '+' e o UNICO operador sobrecarregado do Java (concatenacao).
        String nome = "Conta ";
        String detalhe = nome + "do dia " + 2026;
        System.out.println("concatenação com '+': " + detalhe);

        // Para primitivos, cada tipo tem seu proprio '+' interno.
        double x = 3 + 4 * 2.0;                       // int + int*double usando precedencia
        System.out.println("3 + 4 * 2.0 = " + x);
    }

    // Nao compila (seria sobrecarga de operador): apenas ilustrativo.
    // public static Conta operator+(Conta outra) { ... }  // <- NAO EXISTE EM JAVA
}

public class Exemplo05_Expressoes {

    public static void main(String[] args) {
        System.out.println("=== PRECEDENCIA DE OPERADORES ===");

        // Precedencia usual: * e / antes de + e -.
        System.out.println("2 + 3 * 4       = " + (2 + 3 * 4) + "   (esperado 14)");
        System.out.println("(2 + 3) * 4     = " + ((2 + 3) * 4) + " (parênteses mudam)");
        System.out.println("10 - 4 - 3      = " + (10 - 4 - 3) + "   (mesma precedencia, esq->dir)");
        System.out.println("20 / 5 / 2      = " + (20 / 5 / 2) + "   (mesma precedencia, esq->dir)");
        System.out.println("1 + 2 % 3       = " + (1 + 2 % 3) + "   (% vem antes de +)");
        System.out.println("true || false && false = " + (true || false && false) + " (&& vence ||)");
        System.out.println("1 << 2 + 1      = " + (1 << 2 + 1) + "   (deslocamento SO apos soma)");

        System.out.println();
        System.out.println("=== ASSOCIATIVIDADE ESQUERDA-DIREITA ===");

        // Adicao e subtracao: associativa a ESQUERDA.
        System.out.println("100 - 30 - 20 (esq->dir: (100-30)-20) = " + (100 - 30 - 20));
        System.out.println("com parênteses agrupando a direita: 100 - (30 - 20) = " + (100 - (30 - 20)));
        System.out.println("8 / 4 / 2 (esq->dir: (8/4)/2) = " + (8 / 4 / 2));
        System.out.println("8 / (4 / 2) (direita primeiro) = " + (8 / (4 / 2)));

        // Atribuicao: associativa a DIREITA (encadeamento).
        int a, b, c;
        a = b = c = 5;                       // avalia c=5, b=5, a=5 (da direita p/ esquerda)
        System.out.println("a = b = c = 5 -> a=" + a + ", b=" + b + ", c=" + c);

        // Operadores unitarios (prefixo e posfixo).
        int contador = 5;
        System.out.println("contador++ = " + (contador++) + " (usa e depois incrementa)");
        System.out.println("agora contador = " + contador);
        System.out.println("++(contador) = " + (++contador) + " (incrementa e depois usa)");

        System.out.println();
        System.out.println("=== SOBRECARGA DE OPERADOR (STRINGS) ===");

        // '+' com String: converte o outro operando e concatena.
        String s1 = "Somando 2 + 3 = " + 5;
        String s2 = "Lista: " + java.util.List.of("a", "b") + " fim";
        System.out.println(s1);
        System.out.println(s2);

        // Cuidado com associatividade em concatenacoes: sempre da esquerda pra direita.
        System.out.println("1 + 2 + \"3\" = " + (1 + 2 + "3"));         // primeiro soma, depois concatena
        System.out.println("\"1\" + 2 + 3 = " + ("1" + 2 + 3));        // vira "123"

        System.out.println();
        System.out.println("=== AUSENCIA DE SOBRECARGA PARA OBJETOS ===");

        // Para objetos de classes definidas por nos, NAO ha '+' : nao compila.
        // Conta c1 = new Conta(...); Conta c2 = new Conta(...);
        // Conta soma = c1 + c2;          // <- ERRO DE COMPILACAO

        Conta.tentarComOperadores();       // so funciona com primitivos e String
        System.out.println("total do jantar (R$): " + Conta.total(new int[] {25, 40, 18}, 10, 10));
    }
}
