import static java.lang.IO.println;

/**
 * Exemplo 07 - Comandos Condicionais
 *
 * Cobre (Todo.md #7):
 * - [x] if / else if / else
 * - [x] switch (tradicional e expressao)
 * - [x] Operador ternario
 * - [x] Pattern matching (instanceof, switch)
 *
 * ------------------------------------------------------------------
 * NOTAS SOBRE AS FEATURES DEMONSTRADAS:
 * ------------------------------------------------------------------
 *
 * 1) if / else if / else
 *    - Estrutura condicional classica.
 *    - Compara com `==`. ATENCAO: para Strings o correto e usar
 *      `.equals(...)`, pois `==` compara REFERENCIA, nao conteudo.
 *      Aqui o exemplo usa `==` de proposito didatico.
 *
 * 2) switch tradicional vs switch expression
 *    - `switch (x) { case ... -> ...; }` e um switch com setas
 *      (Java 14+), mais enxuto que o `case ...: break;` classico.
 *    - Quando o switch PRECISA devolver um valor, ele vira uma
 *      SWITCH EXPRESSION: `var x = switch (v) { ... };`
 *
 * 3) Operador ternario
 *    - `condicao ? valorSeVerdadeiro : valorSeFalso`
 *    - Atalho para um if/else que devolve valor.
 *
 * 4) Pattern matching
 *    - `instanceof`: alem de testar o tipo, ja cria uma VARIAVEL
 *      tipada quando o teste e verdadeiro:
 *          if (obj instanceof String s) { ... s ja e String ... }
 *    - `switch` pattern (Java 21): cada `case Tipo var ->` testa o
 *      tipo e ja vincula a variavel.
 *
 * 5) `yield`
 *    - Usado DENTRO de blocos `{ ... }` de uma switch expression para
 *      devolver o valor daquele case.
 */

public class Exemplo07_Condicionais {

    public static void main(String[] args) {

        // -----------------------------------------------------------------
        // Variavel base do exemplo. Todos os testes usam ela.
        // -----------------------------------------------------------------
        String freeDay = "Childrens Day";

        System.out.println("==================================================");
        System.out.println(" EXEMPLO 07 - COMANDOS CONDICIONAIS");
        System.out.println("==================================================");
        System.out.println("freeDay = \"" + freeDay + "\"\n");

        // -----------------------------------------------------------------
        // [1] if / else if / else (com `==` em Strings - didatico)
        // -----------------------------------------------------------------
        // Observacao: `freeDay == "Childrens Day"` compara REFERENCIA.
        // Funciona aqui porque "Childrens Day" e um literal internado
        // (string pool) e `freeDay` tambem aponta para o mesmo literal.
        // Em codigo real use: freeDay.equals("Childrens Day").
        System.out.println("[1] if / else if / else:");

        if (freeDay == "Childrens Day") {

            println("yay");
        } else if (freeDay == "Womans Day"){

            println("lets goo");


        } else {

            println("I gotta work man");
        }

        // -----------------------------------------------------------------
        // [2] switch com setas (Java 14+) - versao STATEMENT
        //     Nao devolve valor; apenas executa um efeito (println).
        // -----------------------------------------------------------------
        System.out.println("\n[2] switch (statement, com setas):");

        switch (freeDay) {
            case "Childrens Day" -> println("yay");
            case "Womans Day" -> println("lets goo");
            default -> println("I gotta work man");
        }

        // -----------------------------------------------------------------
        // [3] switch EXPRESSION - devolve valor para `whatToDo`
        //     Cada `case` com bloco `{ ... }` precisa de `yield` para
        //     devolver o valor daquele ramo.
        // -----------------------------------------------------------------
        System.out.println("\n[3] switch expression (com yield):");

        String whatToDo = switch (freeDay) {
            case "Childrens Day" -> {
                yield "chill";
            }
            case "Womans Day" -> {
                yield "kiss wife";
            }
            default -> {
                yield "cry";
            }
        };

        System.out.println("whatToDo = \"" + whatToDo + "\"");

        // -----------------------------------------------------------------
        // [4] Operador ternario: condicao ? valorV : valorF
        //     Equivale a um if/else que devolve String.
        // -----------------------------------------------------------------
        System.out.println("\n[4] operador ternario:");

        println(freeDay == "Childrens Day" ? "yay" : "nay");

        // -----------------------------------------------------------------
        // [5] Pattern matching com `instanceof`
        //     Aqui `whatToDo` e declarado como String, entao o teste
        //     sempre sera verdadeiro. Serve para mostrar a sintaxe.
        // -----------------------------------------------------------------
        System.out.println("\n[5] instanceof pattern matching:");

        if (whatToDo instanceof String) {
            println("whatToDo is a String");
        } else {
            println("whatToDo is not a String");
        }

        // -----------------------------------------------------------------
        // [6] Pattern matching em `switch` (Java 21)
        //     Cada `case Tipo var ->` testa o tipo e vincula a variavel
        //     `var` ja tipada. Note o cast para `(Object)` para que o
        //     compilador aceite testar outros tipos.
        // -----------------------------------------------------------------
        System.out.println("\n[6] switch pattern matching (Java 21):");

        switch ((Object)whatToDo) {

            case String s -> {
                println("switch string");
            }
            case Integer i -> {
                println("switch is a int");
            }
            default -> println("is something else");
        }

        System.out.println("\n==================================================");
        System.out.println(" FIM DO EXEMPLO 07");
        System.out.println("==================================================");
    }
}
