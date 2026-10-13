/**
 * Exemplo 11 - Tratamento de Excecoes
 * 
 * Exemplo aplicado: um sistema de saque bancario que precisa validar os dados
 * e tratar os erros que podem acontecer durante o processo.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

// Excecao PERSONALIZADA (checked): obriga o chamador a tratar ou propagar.
class SaldoInsuficienteException extends Exception {
    SaldoInsuficienteException(String mensagem) {
        super(mensagem);
    }
}

// Excecao personalizada do tipo UNCHECKED (extends RuntimeException).
class ValorInvalidoException extends RuntimeException {
    ValorInvalidoException(String mensagem) {
        super(mensagem);
    }
}

// Recurso que implementa AutoCloseable para poder usar no try-with-resources.
class Extrato implements AutoCloseable {
    private final StringBuilder conteudo = new StringBuilder();

    void anotar(String linha) {
        conteudo.append(linha).append("\n");
    }

    @Override
    public void close() {
        System.out.println("  (Extrato fechado automaticamente)");
    }

    StringBuilder conteudo() {
        return conteudo;
    }
}

public class Exemplo11_Excecoes {

    static double saldoDaConta = 3000.0;

    public static void main(String[] args) {
        System.out.println("=== CHECKED EXCEPTIONS (verificadas em compilacao) ===");

        // Tentar fazer o saque sem tratar a excecao "nao compila".
        // saqueSemTratar(100);   // <- ERRO: precisa try/catch ou throws

        // Com try/catch, o codigo compila.
        try {
            realizarSaque(500);
        } catch (SaldoInsuficienteException e) {
            System.out.println("catch de checked: " + e.getMessage());
        }
        System.out.println("saldo atual = R$" + saldoDaConta);

        System.out.println();
        System.out.println("=== DELEGACAO COM throws ===");

        // A chamada delega (throws) para quem chamar; aqui tratamos.
        try {
            delegarSaqueComRisco(99999);
        } catch (SaldoInsuficienteException e) {
            System.out.println("delegado via throws: " + e.getMessage());
        }

        System.out.println();
        System.out.println("=== UNCHECKED EXCEPTIONS (RuntimeException) ===");

        try {
            sacar(2_000_000);                    // estoura sem precisar de throws
        } catch (ValorInvalidoException e) {
            System.out.println("unchecked capturada: " + e.getMessage());
        }

        // RuntimeException comum do proprio Java.
        try {
            int[] vetor = new int[3];
            vetor[10] = 1;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("unchecked nativa: " + e.getClass().getSimpleName());
        }

        System.out.println();
        System.out.println("=== ERROS TRATAVEIS vs. INTRATAVEIS (Error) ===");

        // Error: indica problema grave da JVM/recursos. Nao e bom tratar
        // (recuperacao duvidosa). Mostramos so o mecanismo, em um caso artificial.
        try {
            throw new StackOverflowError("exemplo didatico");
        } catch (StackOverflowError e) {
            System.out.println("Error capturado (NAO recomendado): " + e.getMessage());
        }
        // Na pratica: Error (OutOfMemoryError, StackOverflowError, NoClassDefFoundError)
        // deve ser deixado propagar e nao usado para fluxo de controle.

        System.out.println();
        System.out.println("=== EXCECAO PERSONALIZADA ===");

        // Nossa SaldoInsuficienteException guarda uma mensagem propria.
        try {
            sacarSobrescrevendoCustom(1_000_000);
        } catch (SaldoInsuficienteException e) {
            System.out.println("excecao personalizada: " + e.getMessage());
        }

        System.out.println();
        System.out.println("=== TRY / CATCH / FINALLY ===");

        try {
            System.out.println("  tentando divisao...");
            int resultado = 10 / 0;
            System.out.println("resultado = " + resultado);
        } catch (ArithmeticException e) {
            System.out.println("  catch: " + e);
        } finally {
            System.out.println("  finally: executado SEMPRE (com ou sem excecao)");
        }

        System.out.println();
        System.out.println("=== MULTI-CATCH ===");

        // Um unico bloco trata varios tipos de excecao.
        String[] entradas = {"42", "abc", null};
        for (String entrada : entradas) {
            try {
                int numero = Integer.parseInt(entrada);
                System.out.println("  " + entrada + " -> " + numero);
            } catch (NumberFormatException | NullPointerException e) {
                System.out.println("  multi-catch pegou: " + e.getClass().getSimpleName() + " em '" + entrada + "'");
            }
        }

        System.out.println();
        System.out.println("=== TRY-WITH-RESOURCES (AutoCloseable) ===");

        // Recurso e fechado sozinho ao final do try (close() chamado).
        try (Extrato extrato = new Extrato()) {
            extrato.anotar("saque de R$100");
            extrato.anotar("deposito de R$50");
            System.out.println(extrato.conteudo());
        } // aqui o close() roda automaticamente, sem finally

        // Tambem funciona com recursos do proprio Java.
        try (BufferedReader br = new BufferedReader(new StringReader("linha1\nlinha2"))) {
            System.out.println("  " + br.readLine());
            System.out.println("  " + br.readLine());
        } catch (IOException e) {
            System.out.println("  erro de leitura: " + e.getMessage());
        }
    }

    // throws: declara que o metodo pode lancar esta checked excecao.
    static void realizarSaque(double valor) throws SaldoInsuficienteException {
        if (valor > saldoDaConta) {
            throw new SaldoInsuficienteException("saldo insuficiente: R$" + saldoDaConta);
        }
        saldoDaConta -= valor;
        System.out.println("saque de R$" + valor + " realizado");
    }

    // Delega o risco para frente (quem chamar que trate).
    static void delegarSaqueComRisco(double valor) throws SaldoInsuficienteException {
        realizarSaque(valor);                  // aqui NAO ha try/catch: repassa o throws
    }

    // Excecao unchecked: nem precisa de throws na assinatura.
    static void sacar(double valor) {
        if (valor > saldoDaConta + 1000) {
            throw new ValorInvalidoException("valor acima do limite: R$" + valor);
        }
        saldoDaConta -= valor;
    }

    // Reutiliza a checked exception com dado customizado.
    static void sacarSobrescrevendoCustom(double valor) throws SaldoInsuficienteException {
        if (valor > saldoDaConta) {
            throw new SaldoInsuficienteException("limite diario excedido para R$" + valor);
        }
        saldoDaConta -= valor;
    }
}
