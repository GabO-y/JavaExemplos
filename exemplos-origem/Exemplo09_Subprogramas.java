/**
 * Exemplo 09 - Subprogramas
 *
 * Exemplo aplicado: um sistema de pedidos de uma lanchonete onde cada
 * subprograma (metodo) faz uma parte do atendimento.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

// interface Funcional (tem um unico metodo abstrato) usada em lambdas.
@FunctionalInterface
interface Calculadora {
    double calcular(double a, double b);
}

// Classe base com membros protected/private; subclasse sobrescreve.
class Pedido {
    protected double total;
    private int sequencial;              // encapsulado: so a propria classe acessa

    static int contadorGlobal = 0;       // estatico: pertence a classe (class load)

    Pedido(double total) {
        this.total = total;
        contadorGlobal++;
    }

    // metodo publico comum: declaracao + corpo + chamada.
    public void adicionar(double valor) {
        total += valor;
    }

    // metodo private: chamado internamente apenas.
    private void incrementarSequencial() {
        sequencial = contadorGlobal * 10;
    }

    // metodo protected: visivel para subclasses e mesmo pacote.
    protected String etiqueta() {
        incrementarSequencial();
        return "Pedido #" + sequencial;
    }

    // metodo final: nao pode ser sobrescrito.
    public final String resumo() {
        return etiqueta() + " - R$" + total;
    }

    // SOBRECARGA: mesmo nome, assinaturas (parametros) diferentes.
    public double calcularTroco(double pago) {
        return pago - total;
    }

    public double calcularTroco(double pago, double desconto) {
        return pago - (total - desconto);
    }

    public double calcularTroco(double pago, double desconto, boolean emDinheiro) {
        return (pago - (total - desconto)) - (emDinheiro ? 1.5 : 0);
    }

    // metodos estaticos sao chamados sem instancia.
    static String saudacao() {
        return "Bem-vindo a lanchonete!";
    }
}

// Subclasse que SOBRESCREVE um metodo da base.
class PedidoEspecial extends Pedido {
    PedidoEspecial(double total) {
        super(total);
    }

    @Override
    protected String etiqueta() {        // sobrescrita (override)
        return "ESPECIAL (" + super.etiqueta() + ")";
    }
}

// exemplo de CO-ROTINAS, com virtual threads: produz/suspende de forma cooperativa.
class CoRotinaDemo {
    static void tarefa(int id) {
        for (int i = 0; i < 2; i++) {
            System.out.println("co-rotina " + id + " passo " + i);
            Thread.yield();              // "cede a vez" (cooperacao, nao suspensao real)
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

// DELEGACAO: a classe entrega o trabalho para outra (composicao), nao heranca.
class Atendente {
    // delega a conta para um "sistema de pagamento" (funcao) sem implementar a logica.
    private final Function<Double, Boolean> pagamento;

    Atendente(Function<Double, Boolean> pagamento) {
        this.pagamento = pagamento;      // "metodo delegado" recebido de fora
    }

    boolean cobrar(double valor) {
        return pagamento.apply(valor);   // chamada delegada
    }
}

public class Exemplo09_Subprogramas {

    public static void main(String[] args) {
        System.out.println("=== DECLARACAO E CHAMADA DE METODOS ===");

        System.out.println(Pedido.saudacao());           // chamada de metodo estatico

        Pedido p1 = new Pedido(25.0);
        p1.adicionar(12.5);                              // chamada de metodo de instancia
        System.out.println(p1.resumo());

        // metodo sobrescrito via polimorfismo.
        Pedido especial = new PedidoEspecial(80.0);
        System.out.println(especial.resumo());

        System.out.println();
        System.out.println("=== MODIFICADORES (public, private, protected, static, final) ===");

        System.out.println("contadorGlobal (static) = " + Pedido.contadorGlobal);
        System.out.println("resumo() e final: sobrescrita bloqueada.");
        // private: p1.sequencial nao e acessivel daqui (compilaria erro).
        // protected: p1.etiqueta() visivel aqui por estar no mesmo pacote.
        System.out.println("etiqueta protected acessivel no mesmo pacote: " + p1.etiqueta());

        System.out.println();
        System.out.println("=== PASSAGEM DE PARAMETROS: POR VALOR ===");

        int quantia = 100;
        alterarPrimitivo(quantia);                       // copia do valor
        System.out.println("primitivo apos chamada (inalterado): " + quantia);

        Pedido alvo = new Pedido(0);
        alterarObjeto(alvo);                             // copia da REFERENCIA
        System.out.println("objeto apos chamada (alterado): R$" + alvo.total);

        System.out.println("sobrecarga calcularTroco: " + p1.calcularTroco(50.0) +
                " | " + p1.calcularTroco(50.0, 5) + " | " + p1.calcularTroco(50.0, 5, true));

        System.out.println();
        System.out.println("=== SOBRECARGA E SOBRESCRITA ===");

        // Sobrescrita ja vista acima (etiqueta em PedidoEspecial).
        System.out.println("sobrescrita de etiqueta(): " + especial.etiqueta());

        System.out.println();
        System.out.println("=== CO-ROTINAS: NAO NATIVAS, USAMOS (VIRTUAL) THREADS ===");

        // Java nao possui co-rotinas; threads cooperam (yield) de forma parecida.
        Thread t1 = Thread.ofVirtual().start(() -> CoRotinaDemo.tarefa(1));
        Thread t2 = Thread.ofVirtual().start(() -> CoRotinaDemo.tarefa(2));
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("(co-operacao simulada com virtual threads / yield)");

        System.out.println();
        System.out.println("=== FUNCOES LAMBDA (Java 8+) ===");

        Calculadora soma = (a, b) -> a + b;              // lambda guardada em interface funcional
        Calculadora produto = (a, b) -> a * b;
        System.out.println("soma(3,4) = " + soma.calcular(3, 4));
        System.out.println("produto(3,4) = " + produto.calcular(3, 4));

        List<String> itens = new ArrayList<>(List.of("pao", "cafe", "brioche"));
        itens.replaceAll(s -> s.toUpperCase());          // lambda em metodos prontos
        itens.removeIf(s -> s.length() <= 3);            // lambda com predicado
        System.out.println("itens apos lambdas: " + itens);

        System.out.println();
        System.out.println("=== PROGRAMACAO GENERICA (Generics) ===");

        Caixa<String> texto = new Caixa<>("genericos");
        Caixa<Integer> numero = new Caixa<>(42);

        Integer[] nums = {3, 1, 2};
        System.out.println("maior de [3,1,2] = " + MetodosGenericos.maior(nums));

        System.out.println("Caixa<String> = " + texto.get());
        System.out.println("Caixa<Integer> = " + numero.get());
        System.out.println("mesma classe para tipos diferentes: " + (texto.getClass() == numero.getClass()));

        System.out.println();
        System.out.println("=== METODOS DELEGADOS (VIA COMPOSICAO) ===");

        Atendente atendente = new Atendente(v -> {
            System.out.println("(pagamento de R$" + v + " delegado ao sistema externo)");
            return v > 0;
        });
        System.out.println("cobranca aprovada? " + atendente.cobrar(42.0));
    }

    static void alterarPrimitivo(int valor) {
        valor = 999;                                   // muda so a copia local
    }

    static void alterarObjeto(Pedido pedido) {
        pedido.total = 777;                            // copia da referencia => objeto real muda
    }
}

// Classe generica simples (package-private; roda em default package).
class Caixa<T> {
    private final T item;

    Caixa(T item) {
        this.item = item;
    }

    T get() {
        return item;
    }
}

// metodos genericos estaticos: um unico metodo serve para varios tipos.
class MetodosGenericos {
    static <T extends Comparable<T>> T maior(T[] array) {
        T maior = array[0];
        for (T elem : array) {
            if (elem.compareTo(maior) > 0) {
                maior = elem;
            }
        }
        return maior;
    }
}
