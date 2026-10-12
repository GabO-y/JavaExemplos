/**
 * Exemplo 02 - Ponteiros e Referencias
 *
 * Exemplo aplicado: um cracha de funcionario compartilhado por varias
 * variaveis, mostrando como as referencias apontam para o mesmo objeto no heap.
 */

// Classe descomplicada para representar um cracha.
class Cracha {
    String nome;

    Cracha(String nome) {
        this.nome = nome;
    }

    void redefinirNome(String novo) {
        this.nome = novo;
    }

    @Override
    public String toString() {
        return "Cracha{" + nome + "}";
    }
}

public class Exemplo02_PonteirosEReferencias {

    public static void main(String[] args) {
        System.out.println("=== REFERENCIAS (NAO PONTEIROS EXPLICITOS) ===");

        // Uma unica referencia no heap, compartilhada por duas variaveis.
        Cracha crachaOriginal = new Cracha("Daniel");
        Cracha outraReferencia = crachaOriginal;

        System.out.println("crachaOriginal   = " + crachaOriginal);
        System.out.println("outraReferencia = " + outraReferencia);

        // Alterar pela segunda variavel reflete na primeira: apontam para o MESMO objeto.
        outraReferencia.redefinirNome("Daniel Souza");
        System.out.println("apos mutar por outraReferencia, crachaOriginal = " + crachaOriginal);

        // Comparar com == compara as referencias (enderecos logicos), nao o conteudo.
        System.out.println("crachaOriginal == outraReferencia ? " + (crachaOriginal == outraReferencia));

        // Um cracha novo com mesmo nome e um objeto diferente.
        Cracha copia = new Cracha("Daniel Souza");
        System.out.println("copia == outraReferencia ? " + (copia == outraReferencia));
        System.out.println("copia.equals(outraReferencia) ? " + copia.equals(outraReferencia));

        System.out.println();
        System.out.println("=== SEM ARITMETICA DE PONTEIROS ===");

        // Em C/C++ seria possivel fazer "ptr + 2" para caminhar na memoria.
        // Em Java isso NAO compila: nao existe aritmetica sobre referencias.
        // Cracha resultado = crachaOriginal + 1;  // <- ERRO DE COMPILACAO
        // Cracha proximo = crachaOriginal++;      // <- ERRO DE COMPILACAO
        // A unica "operacao" possivel com a referencia e atribuir, comparar (==) e navegar pela.
        System.out.println("Operacoes validas com referencia: atribuicao, comparacao (==) e acesso ao objeto.");
        System.out.println("Aritmetica de enderecos (ptr + n, ptr++) nao existe em Java.");

        System.out.println();
        System.out.println("=== REFERENCIAS NULAS (null) ===");

        // null e a ausencia de referencia; valor padrao de variaveis de instancia.
        Cracha vazio = null;
        System.out.println("vazio = " + vazio);
        System.out.println("comparacao null == null ? " + (vazio == null));

        // Chamar um metodo sobre null lanca NullPointerException.
        try {
            vazio.redefinirNome("Erro");
        } catch (NullPointerException e) {
            System.out.println("chamar metodo em null lanca -> " + e.getClass().getSimpleName());
        }

        // Tipos primitivos NAO aceitam null: isso nao compila.
        // int numero = null;   // <- ERRO DE COMPILACAO

        System.out.println();
        System.out.println("=== GERENCIAMENTO DE MEMORIA (GARBAGE COLLECTOR) ===");

        // Memorias usadas antes.
        System.out.println("Memoria livre no inicio: " + Runtime.getRuntime().freeMemory() / 1024 + " KB");

        // Cria muitos crachas que ficam sem referencia -> elegiveis para o GC.
        for (int i = 0; i < 1_000_000; i++) {
            new Cracha("Temporario " + i); // objeto descartado: nenhuma referencia o alcanca
        }
        System.out.println("1 milhao de crachas criados sem referencia...");

        // O programador nao libera memoria manualmente; apenas pede ao GC.
        System.gc(); // apenas uma SUGESTAO ao garbage collector

        System.out.println("Memoria livre depois: " + Runtime.getRuntime().freeMemory() / 1024 + " KB");

        // Enquanto existir referencia, o objeto permanece vivo.
        Cracha fixo = new Cracha("Permanente");
        System.gc();
        System.out.println("Objeto 'fixo' ainda acessivel apos System.gc(): " + fixo);

        // Referencia nula torna o objeto alcancavel? Nao: sem referencia ele fica elegivel.
        fixo = null;
        System.out.println("'fixo' agora e null -> o Cracha anterior pode ser coletado.");
    }
}
