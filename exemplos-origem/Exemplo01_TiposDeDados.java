/**
 * Exemplo 01 - Tipos de Dados
 *
 * Exemplo aplicado: sistema de bolletim escolar para demonstrar os tipos de
 * dados do Java no mundo real.
 */

import java.util.Arrays;

// Record: forma compacta de criar uma classe imutavel de dados (Java 16+).
record Aluno(String nome, int matricula, double[] notas) {}

// Enum: tipo de referencia enumerado com conjunto fixo de valores.
enum Status { APROVADO, RECUPERACAO, REPROVADO }

public class Exemplo01_TiposDeDados {

    // Campos da classe recebem valores padrao automaticamente.
    static byte b;
    static short s;
    static int i;
    static long l;
    static float f;
    static double d;
    static char c;
    static boolean z;

    public static void main(String[] args) {
        System.out.println("=== TIPOS PRIMITIVOS ===");

        byte idade = 22;                    // 8 bits  -> -128 a 127
        short ano = 2026;                   // 16 bits -> -32.768 a 32.767
        int quantidadeAlunos = 40_000;      // 32 bits (separador _ melhora a leitura)
        long populacaoMundial = 8_100_000_000L; // 64 bits, sufixo L
        float mediaPeso = 68.5f;            // 32 bits, ponto flutuante, sufixo f
        double mediaNotas = 7.35;           // 64 bits, ponto flutuante com precisao dupla
        char conceito = 'B';                // 16 bits, caractere Unicode
        boolean aprovado = true;            // 1 bit logico

        System.out.println("byte idade: " + idade);
        System.out.println("short ano: " + ano);
        System.out.println("int quantidadeAlunos: " + quantidadeAlunos);
        System.out.println("long populacaoMundial: " + populacaoMundial);
        System.out.println("float mediaPeso: " + mediaPeso);
        System.out.println("double mediaNotas: " + mediaNotas);
        System.out.println("char conceito: " + conceito);
        System.out.println("boolean aprovado: " + aprovado);

        System.out.println();
        System.out.println("=== TAMANHO EM BITS/BYTES (via classes wrapper) ===");

        System.out.println("byte  : " + Byte.SIZE + " bits = " + Byte.BYTES + " byte");
        System.out.println("short : " + Short.SIZE + " bits = " + Short.BYTES + " bytes");
        System.out.println("int   : " + Integer.SIZE + " bits = " + Integer.BYTES + " bytes");
        System.out.println("long  : " + Long.SIZE + " bits = " + Long.BYTES + " bytes");
        System.out.println("float : " + Float.SIZE + " bits = " + Float.BYTES + " bytes");
        System.out.println("double: " + Double.SIZE + " bits = " + Double.BYTES + " bytes");
        System.out.println("char  : " + Character.SIZE + " bits = " + Character.BYTES + " bytes");
        System.out.println("boolean: tamanho de 1 bit logico (representado em 1 byte)");

        System.out.println();
        System.out.println("=== REPRESENTACAO: PONTO FIXO vs. PONTO FLUTUANTE ===");

        // Inteiros sao ponto fixo: representacao exata.
        int exato = 10 / 3;
        System.out.println("10/3 como int (ponto fixo, sem casas decimais): " + exato);

        // Ponto flutuante tem precisao limitada (erros de arredondamento).
        double naoExato = 0.1 + 0.2;
        System.out.println("0.1 + 0.2 como double (ponto flutuante): " + naoExato);
        System.out.println("0.1 + 0.2 == 0.3 ? " + (naoExato == 0.3));
        System.out.println("float 1.0f/3.0f: " + (1.0f / 3.0f) + " | double 1.0/3.0: " + (1.0 / 3.0));

        System.out.println();
        System.out.println("=== VALORES PADRAO (inicializacao automatica) ===");

        System.out.println("byte    = " + b);
        System.out.println("short   = " + s);
        System.out.println("int     = " + i);
        System.out.println("long    = " + l);
        System.out.println("float   = " + f);
        System.out.println("double  = " + d);
        System.out.println("char    = [" + c + "] (codigo " + (int) c + ")");
        System.out.println("boolean = " + z);

        System.out.println();
        System.out.println("=== TIPOS COMPOSTOS/REFERENCIA ===");

        // Record: tipo composto imutavel. Objeto vive no heap.
        Aluno aluno = new Aluno("Ana", 2026001, new double[] {8.5, 7.0, 9.2});
        System.out.println("Aluno (record): " + aluno);

        // Array: vetor dinamico de tamanho fixo, criado no heap.
        double[] notas = aluno.notas();
        System.out.println("notas: " + Arrays.toString(notas));
        notas[0] = 10.0; // arrays sao mutaveis
        System.out.println("notas apos mutacao: " + Arrays.toString(notas) + " (o record so protege a referencia)");

        // Enum: conjunto fixo de valores.
        Status status = Status.APROVADO;
        Status[] todos = Status.values();
        System.out.println("Status atual: " + status);
        System.out.println("Todos os status: " + Arrays.toString(todos));

        // Classe comum: definida fora? Nao, objetos anonimos usam classes do proprio Java.
        Object qualquer = aluno; // Todo objeto de referencia 'e um Object (java.lang.Object).
        System.out.println("Qualquer objeto 'e um Object: " + (qualquer instanceof Object));

        System.out.println();
        System.out.println("=== ARRAYS: INDEXACAO E FORMATO ===");

        // Indices devem ser int (ou tipos menores: byte/short/char sao promovidos a int).
        int[] notasDaTurma = {5, 7, 8, 10};
        byte indiceByte = 2;
        short indiceShort = 3;
        System.out.println("notasDaTurma[0] = " + notasDaTurma[0]);
        System.out.println("notasDaTurma[indiceByte] = " + notasDaTurma[indiceByte]);
        System.out.println("notasDaTurma[indiceShort] = " + notasDaTurma[indiceShort]);

        // Arranjo multidimensional: notas das materias por aluno.
        double[][] boletim = { {7.5, 8.0}, {6.0, 9.5} };
        for (int linha = 0; linha < boletim.length; linha++) {
            System.out.println("Aluno " + linha + ": " + Arrays.toString(boletim[linha]));
        }

        // Acessar um indice invalido lanca excecao em tempo de execucao.
        try {
            System.out.println(notasDaTurma[10]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("indice fora do limite -> " + e);
        }

        // long nao pode ser indice de array (insistir seria um erro de compilacao).
        // long indiceLong = 1;
        // System.out.println(notasDaTurma[indiceLong]); // COMPILE ERROR

        System.out.println();
        System.out.println("=== CONVERSAO ENTRE TIPOS ===");

        // Implicita (widening): amplia sem perda, int -> long -> double.
        int dia = 25;
        long diaLong = dia;
        double diaDouble = diaLong;
        System.out.println("int " + dia + " -> long " + diaLong + " -> double " + diaDouble + " (implicita)");

        // Explicita (narrowing/casting): pode perder dados, usa-se o cast.
        double media = 8.75;
        int mediaInt = (int) media;
        long big = 5_000_000_000L;
        int bigInt = (int) big;
        System.out.println("(int) 8.75 = " + mediaInt + " (trunca casas decimais)");
        System.out.println("(int) 5000000000L = " + bigInt + " (estoura o limite do int)");

        // Boxing: primitivo -> wrapper (automatico).
        Integer notasBoxed = mediaInt;
        Double notaBoxed = 9.5;
        // Unboxing: wrapper -> primitivo (automatico).
        double notaUnboxed = notaBoxed;
        int soma = notasBoxed + 1;
        System.out.println("boxing: Integer " + notasBoxed + ", Double " + notaBoxed);
        System.out.println("unboxing: double " + notaUnboxed + " e int " + soma);

        System.out.println();
        System.out.println("=== TIPOS BASICOS PADRAO (String, wrappers, Object) ===");

        String nomeCurso = "Compiladores";
        String nomeMaiusculo = nomeCurso.toUpperCase();
        System.out.println("String: '" + nomeCurso + "' em maiusculas: '" + nomeMaiusculo + "'");

        Integer.valueOf(10);          // wrapper factory
        System.out.println("Integer.parseInt(\"2026\") = " + Integer.parseInt("2026"));
        System.out.println("Double.valueOf(7.5).toString() = " + Double.valueOf(7.5).toString());

        Object obj = "sou uma String mas sou um Object";
        System.out.println("Object aceita qualquer tipo: " + obj);
    }
}
