/**
 * Exemplo 13 - Paradigmas e Aspectos Adicionais
 *
 * Exemplo aplicado: um sistema de triagem de pacientes que mistura varios
 * paradigmas para analisar a lista de atendimentos.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Flow;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Exemplo13_Paradigmas {

    public static void main(String[] args) throws Exception {
        System.out.println("=== [1] PARADIGMA ORIENTADO A OBJETOS ===");

        // Heranca: Paciente e Filho; Polimorfismo: mesma chamada, comportamentos diferentes.
        Pessoa ana = new Paciente("Ana", 34, 38.2);
        Pessoa bruno = new Paciente("Bruno", 10, 37.0);
        // Encapsulamento: campo private acessado apenas via getter.
        System.out.println("  " + ana.descrever() + " | temperatura=" + ((Paciente) ana).getTemperatura());
        System.out.println("  " + bruno.descrever() + " | temperatura=" + ((Paciente) bruno).getTemperatura());

        List<Pessoa> atendidos = new ArrayList<>(List.of(ana, bruno, new Paciente("Carol", 70, 39.5)));

        System.out.println();
        System.out.println("=== [2] PARADIGMA FUNCIONAL (Streams, Optional, pure funcs) ===");

        // funcao pura: mesmo entrada -> mesma saida, sem efeitos colaterais.
        // Streams nao mutam a fonte (imutabilidade) e usam lambdas.
        List<Pessoa> comFebre = atendidos.stream()
                .filter(p -> p instanceof Paciente pt && pt.getTemperatura() >= 38.0)
                .sorted(Comparator.comparingInt(p -> ((Paciente) p).getIdade()))
                .collect(Collectors.toList());
        System.out.println("  pacientes com febre (ordenados): "
                + comFebre.stream().map(Pessoa::nome).toList());

        // Optional: evita null e encadeia operacoes com seguranca.
        Optional<Pessoa> maisVelho = atendidos.stream()
                .filter(p -> p instanceof Paciente)
                .max(Comparator.comparingInt(p -> ((Paciente) p).getIdade()));
        System.out.println("  mais velho: " + maisVelho.map(Pessoa::nome).orElse("nenhum"));

        // reduce: operacao de reducao sem loop imperativo.
        int totalIdades = atendidos.stream()
                .filter(p -> p instanceof Paciente)
                .mapToInt(p -> ((Paciente) p).getIdade())
                .sum();
        System.out.println("  soma das idades (via Stream): " + totalIdades);

        System.out.println();
        System.out.println("=== [3] PARADIGMA LOGICO / DECLARATIVO (simulado) ===");

        // Java nao tem Prolog; simulamos fatos + regras avaliadas de forma declarativa.
        List<String> fatos = List.of("febre(ana)", "febre(carol)", "idade(ana,34)", "idade(carol,70)");
        System.out.println("  fatos: " + fatos);
        Regras.mostrarQuem("febre", fatos);      // "regra": alguem(X) se febre(X)

        System.out.println();
        System.out.println("=== [4] PARADIGMA GENERICO (bounds, wildcards, erasure) ===");

        // Bounds: T so aceita Comparable.
        System.out.println("  max(9, 4) = " + Util.maxDe(9, 4));

        // Wildcards: aceita List de qualquer subtipo de Number.
        System.out.println("  soma de [1, 2.5, 3L] = " + Util.somar(List.of(1, 2.5, 3L)));

        // Type erasure: o tipo generico e apagado em tempo de execucao.
        System.out.println("  Caixa<Integer> e Caixa<String> sao a MESMA classe: "
                + (new CaixaG<Integer>(1).getClass() == new CaixaG<String>("x").getClass()));

        System.out.println();
        System.out.println("=== [5] PARADIGMA REATIVO (Flow API + backpressure) ===");

        demoReativo();

        System.out.println();
        System.out.println("=== [6] PARALELISMO: Fork/Join e Parallel Streams ===");

        int[] dados = new int[10_000];
        Arrays.parallelSetAll(dados, i -> i);

        // Fork/Join: divide o trabalho e junta os resultados.
        int somaForkJoin = ForkJoinPool.commonPool().invoke(new SomarTarefa(dados, 0, dados.length));
        System.out.println("  Fork/Join soma = " + somaForkJoin);

        // Parallel Stream: o mesmo pipeline com processamento paralelo.
        long somaParallel = Arrays.stream(dados).parallel().sum();
        System.out.println("  Parallel Stream soma = " + somaParallel);

        System.out.println();
        System.out.println("=== [7] METAPROGRAMACAO: ANNOTATIONS + REFLECTION ===");

        Class<Paciente> classePaciente = Paciente.class;
        InfoTabela info = classePaciente.getAnnotation(InfoTabela.class);
        System.out.println("  anotacao @InfoTabela em Paciente -> nome=" + info.nome()
                + ", versao=" + info.versao());

        // Reflection: inspecionar campos e ler um campo private de "fora".
        for (Field campo : classePaciente.getDeclaredFields()) {
            System.out.println("  campo refletido: " + campo.getName() + " (" + campo.getType().getSimpleName() + ")");
        }

        Pessoa p = atendidos.get(0);
        Field campoNome = Pessoa.class.getDeclaredField("nome");
        campoNome.setAccessible(true);                 // vence o private para leitura
        System.out.println("  nome lido via reflection: " + campoNome.get(p));

        // Metodos: invocar um metodo getter descoberto em tempo de execucao.
        Object temp = classePaciente.getMethod("getTemperatura").invoke(atendidos.get(0));
        System.out.println("  getTemperatura() invocado por reflection: " + temp);

        System.out.println();
        System.out.println("=== [8] PROGRAMACAO DISTRIBUIDA: SOCKETS (localhost) ===");

        demoDistribuida();

        System.out.println();
        System.out.println("=== [9] MODULARIZACAO (JPMS) ===");

        // JPMS exige module-info.java + estrutura de pastas:
        //   module br.facul.compiladores { requires java.base; exports br.facul; }
        // Neste ambiente de arquivos simples (sem module-info) o classpath padrao
        // e usado -- o Java 9+ ainda executa em modo "unnamed module".
        System.out.println("  JPMS: ver comentario acima (module-info.java e exigido fora do classpath).");
    }

    // --- Fluxo reativo com backpressure (cliente controla o ritmo) ---
    static void demoReativo() throws InterruptedException {
        SubmissionPublisher<String> publisher = new SubmissionPublisher<>();
        CountDownLatch termino = new CountDownLatch(1);
        int[] recebidos = {0};

        Flow.Subscriber<String> subscriber = new Flow.Subscriber<>() {
            Flow.Subscription sub;

            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                this.sub = subscription;
                sub.request(2);                          // backpressure: pede 2 por vez
            }

            @Override
            public void onNext(String item) {
                recebidos[0]++;
                System.out.println("  onNext[" + recebidos[0] + "]: " + item);
                if (recebidos[0] % 2 == 0) {
                    sub.request(2);                      // pede mais 2 quando consumir 2
                }
            }

            @Override
            public void onError(Throwable t) { termino.countDown(); }

            @Override
            public void onComplete() { System.out.println("  (onComplete)"); termino.countDown(); }
        };

        publisher.subscribe(subscriber);
        for (int i = 1; i <= 5; i++) {
            publisher.submit("leitura-" + i);
        }
        publisher.close();
        termino.await(3, TimeUnit.SECONDS);
        System.out.println("  itens recebidos com backpressure: " + recebidos[0]);
    }

    // --- Dois processos na mesma maquina trocando dados via Socket ---
    static void demoDistribuida() throws Exception {
        try (ServerSocket servidor = new ServerSocket(0)) {
            int porta = servidor.getLocalPort();

            Thread produtor = new Thread(() -> {         // "maquina" B - recebe
                try (Socket conexao = servidor.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(conexao.getInputStream()))) {
                    System.out.println("  servidor recebeu: " + in.readLine());
                } catch (IOException e) {
                    System.out.println("  erro no servidor: " + e.getMessage());
                }
            });

            produtor.start();

            try (Socket cliente = new Socket("localhost", porta);  // "maquina" A - envia
                 PrintWriter out = new PrintWriter(cliente.getOutputStream(), true)) {
                out.println("mensagem distribuida via socket");
            }
            produtor.join();
        }
    }
}

// ====================== CLASSES DE APOIO ======================

// Anotacao customizada (Metaprogramacao).
@Retention(RetentionPolicy.RUNTIME)
@interface InfoTabela {
    String nome();
    int versao() default 1;
}

// Encapsulamento + Heranca + Polimorfismo (OOP).
class Pessoa {
    private final String nome;                     // private: somente via getter

    Pessoa(String nome) {
        this.nome = nome;
    }

    String nome() {
        return nome;
    }

    String descrever() {
        return nome + " (pessoa)";
    }
}

@InfoTabela(nome = "pacientes", versao = 3)
class Paciente extends Pessoa {
    private final int idade;
    private final double temperatura;

    Paciente(String nome, int idade, double temperatura) {
        super(nome);
        this.idade = idade;
        this.temperatura = temperatura;
    }

    public int getIdade() {
        return idade;
    }

    public double getTemperatura() {
        return temperatura;
    }

    @Override
    String descrever() {                           // polimorfismo (override)
        return nome() + " tem " + idade + " anos";
    }
}

// Regras "declarativas" sobre fatos (paradigma logico simulado).
class Regras {
    static void mostrarQuem(String predicado, List<String> fatos) {
        fatos.stream()
                .filter(fato -> fato.startsWith(predicado + "("))
                .forEach(fato -> {
                    String quem = fato.substring(fato.indexOf('(') + 1, fato.indexOf(')'));
                    System.out.println("  regra " + predicado + " aplicada a: " + quem);
                });
    }
}

// Generics: metodo com bound e wildcard.
class Util {
    static <T extends Comparable<T>> T maxDe(T a, T b) {
        return a.compareTo(b) >= 0 ? a : b;
    }

    static double somar(List<? extends Number> numeros) {
        double total = 0;
        for (Number n : numeros) {
            total += n.doubleValue();
        }
        return total;
    }
}

class CaixaG<T> {
    private final T valor;

    CaixaG(T valor) {
        this.valor = valor;
    }
}

// Fork/Join: RecursiveTask que soma um intervalo de um array.
class SomarTarefa extends RecursiveTask<Integer> {
    static final int LIMIAR = 1000;
    private final int[] dados;
    private final int inicio;
    private final int fim;

    SomarTarefa(int[] dados, int inicio, int fim) {
        this.dados = dados;
        this.inicio = inicio;
        this.fim = fim;
    }

    @Override
    protected Integer compute() {
        if (fim - inicio <= LIMIAR) {              // resolve direto
            int soma = 0;
            for (int i = inicio; i < fim; i++) {
                soma += dados[i];
            }
            return soma;
        }
        int meio = (inicio + fim) / 2;             // divide
        SomarTarefa esq = new SomarTarefa(dados, inicio, meio);
        SomarTarefa dir = new SomarTarefa(dados, meio, fim);
        esq.fork();                                // roda em outra thread
        int somaDir = dir.compute();               // compute aqui
        return esq.join() + somaDir;               // junta os resultados
    }
}
