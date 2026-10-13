/**
 * Exemplo 10 - Concorrencia
 *
 * Exemplo aplicado: bilheteria de um show vendendo ingressos de forma
 * concorrente (caixas vendendo do mesmo estoque ao mesmo tempo).
 */

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Exemplo10_Concorrencia {

    // Contador compartilhado SEM sincronizacao (mostra race condition).
    static int ingressosColetor = 0;

    // Contador protegido por synchronized, Lock e Atomic.
    static final AtomicInteger ingressosAtomicos = new AtomicInteger(0);

    // Influencia a ordem de execucao para deixar a demonstracao mais legivel.
    static void pausar(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== [1] THREADS: Thread + Runnable (race condition) ===");

        // Runnable: tarefa que sera executada por uma Thread.
        Runnable venderSemLock = () -> {
            for (int i = 0; i < 250_000; i++) {
                ingressosColetor++;              // le-ajusta-escreve NAO atomico
            }
        };

        List<Thread> caixas = new java.util.ArrayList<>();
        for (int c = 0; c < 4; c++) {
            Thread t = new Thread(venderSemLock, "Caixa-" + c);
            t.start();
            caixas.add(t);
        }
        for (Thread t : caixas) t.join();

        // Esperado 1.000.000, mas perde incrementos por acesso concorrente.
        System.out.println("4 threads vendendo 250.000 cada (int puro) -> " + ingressosColetor + " (esperado 1.000.000)");
        System.out.println("Perda de incrementos: RACE CONDITION!");

        System.out.println();
        System.out.println("=== [2] SINCRONIZACAO COM synchronized ===");

        BilheteriaSinc ba = new BilheteriaSinc();
        Thread c1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) ba.vender();
        }, "C1");
        Thread c2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) ba.vender();
        }, "C2");
        c1.start();
        c2.start();
        c1.join();
        c2.join();
        System.out.println("com synchronized -> " + ba.getTotal() + " (correto)");

        System.out.println();
        System.out.println("=== [3] SINCRONIZACAO COM Lock (ReentrantLock) ===");

        ReentrantLock trava = new ReentrantLock();
        int[] lancados = {0};
        Thread l1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                trava.lock();                    // entra na regiao critica
                lancados[0]++;
                trava.unlock();                  // sai da regiao critica
            }
        });
        Thread l2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                trava.lock();
                lancados[0]++;
                trava.unlock();
            }
        });
        l1.start();
        l2.start();
        l1.join();
        l2.join();
        System.out.println("com ReentrantLock -> " + lancados[0] + " (correto)");

        System.out.println();
        System.out.println("=== [4] SINCRONIZACAO COM Semaphore ===");

        Semaphore guiche = new Semaphore(2);     // 2 pagamentos simultaneos
        AtomicInteger pagamentosAtivos = new AtomicInteger();
        AtomicInteger pagamentosMax = new AtomicInteger();

        Runnable pagar = () -> {
            try {
                guiche.acquire();                 // pede um permiso
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            int ativos = pagamentosAtivos.incrementAndGet();
            pagamentosMax.updateAndGet(m -> Math.max(m, ativos));
            pausar(5);                            // simulacao de pagamento
            pagamentosAtivos.decrementAndGet();
            guiche.release();                     // devolve o permiso
        };

        List<Thread> pagadores = new java.util.ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Thread t = new Thread(pagar, "Pagador-" + i);
            t.start();
            pagadores.add(t);
        }
        for (Thread t : pagadores) t.join();
        System.out.println("6 pagamentos com limite 2 -> pico simultaneo de " + pagamentosMax.get());

        System.out.println();
        System.out.println("=== [5] ATOMICOS (AtomicInteger) ===");

        Thread a1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) ingressosAtomicos.incrementAndGet();
        });
        Thread a2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) ingressosAtomicos.incrementAndGet();
        });
        a1.start();
        a2.start();
        a1.join();
        a2.join();
        System.out.println("atomic incrementAndGet -> " + ingressosAtomicos.get() + " (correto, sem locks)");

        System.out.println();
        System.out.println("=== [6] EXECUTORS / THREAD POOLS ===");

        ExecutorService pool = Executors.newFixedThreadPool(3); // pool reutiliza threads
        AtomicInteger vendasPool = new AtomicInteger(0);
        for (int i = 0; i < 6; i++) {
            pool.submit(() -> {
                vendasPool.incrementAndGet();      // tarefa entregue ao pool
                pausar(5);
            });
        }
        pool.shutdown();                           // para de aceitar tarefas
        pool.awaitTermination(5, TimeUnit.SECONDS); // aguarda o fim das atuais
        System.out.println("pool de 3 threads processou 6 tarefas -> vendas = " + vendasPool.get());

        System.out.println();
        System.out.println("=== [7] MEMORIA COMPARTILHADA E volatile ===");

        // volatile garante visibilidade entre threads (sem cache local "velho").
        PararSinal sinal = new PararSinal();
        Thread produtor = new Thread(() -> {
            while (!sinal.parar && sinal.itens < 10) {
                sinal.itens++;
                System.out.println("  produzindo item " + sinal.itens);
                pausar(5);
            }
        }, "Produtor");

        produtor.start();
        pausar(30);                             // da tempo para rodar um pouco
        sinal.parar = true;                     // outra thread liga o sinal (volatile)
        produtor.join();
        System.out.println("volatile 'parar' interrompeu o produtor, que produziu " + sinal.itens + " itens");

        System.out.println();
        System.out.println("=== [8] COMPLETABLEFUTURE (assincrono) ===");

        // Tarefas assincronas que se combinam sem gerenciar threads na mao.
        CompletableFuture<Integer> vendaLoteA = CompletableFuture.supplyAsync(() -> { pausar(10); return 300; });
        CompletableFuture<Integer> vendaLoteB = CompletableFuture.supplyAsync(() -> { pausar(20); return 450; });

        CompletableFuture<Integer> totalFuturo = vendaLoteA
                .thenCombine(vendaLoteB, Integer::sum)   // soma quando os dois terminam
                .thenApply(total -> total + 250);         // encadeia outro passo

        System.out.println("ingressos vendidos (async, via CompletableFuture) = " + totalFuturo.join());

        System.out.println();
        System.out.println("=== [9] VIRTUAL THREADS (Java 21+) ===");

        AtomicInteger processadas = new AtomicInteger(0);
        List<Thread> virtuais = new java.util.ArrayList<>();
        for (int i = 0; i < 1000; i++) {         // milhares de threads baratas
            Thread vt = Thread.ofVirtual().start(() -> {
                processadas.incrementAndGet();
                Thread.yield();
            });
            virtuais.add(vt);
        }
        for (Thread vt : virtuais) vt.join();
        System.out.println("1000 virtual threads executaram -> processadas = " + processadas.get());

        System.out.println();
        System.out.println("=== [10] COLECOES CONCORRENTES (ConcurrentHashMap) ===");

        ConcurrentHashMap<String, Integer> vendasPorRegiao = new ConcurrentHashMap<>();
        ExecutorService poolRegioes = Executors.newFixedThreadPool(4);
        for (int r = 0; r < 8; r++) {
            final String regiao = "regiao-" + (r % 2);
            poolRegioes.submit(() -> {
                vendasPorRegiao.merge(regiao, 1, Integer::sum); // acumula com seguranca
                pausar(3);
            });
        }
        poolRegioes.shutdown();
        poolRegioes.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("vendas por regiao = " + vendasPorRegiao);

        System.out.println();
        System.out.println("=== RESUMO ===");
        System.out.println("int puro      -> " + ingressosColetor + " (perdeu incrementos)");
        System.out.println("synchronized  -> 2000");
        System.out.println("Lock          -> 2000");
        System.out.println("AtomicInteger -> " + ingressosAtomicos.get());
    }
}

// Contador sincronizado: metodo inteiro protegido.
class BilheteriaSinc {
    private int total = 0;

    synchronized void vender() {
        total++;
    }

    synchronized int getTotal() {
        return total;
    }
}

// Sinal compartilhado: a flag volatile e visivel a todas as threads.
class PararSinal {
    volatile boolean parar = false;
    volatile int itens = 0;
}
