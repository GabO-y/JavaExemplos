/**
 * Exemplo 18 - Maquina Virtual Java e Bytecode
 *
 * Cobre (fase de compilacao / execucao):
 * - [x] Do .java ao .class: o compilador gera BYTECODE, nao codigo nativo
 * - [x] Carregamento de classe (class loading) e inicializacao
 * - [x] Metadados da classe (superclasse, metodos, campos)
 * - [x] Areas de memoria da JVM: stack x heap
 * - [x] Compilacao JIT (just-in-time) - conceito
 *
 * ------------------------------------------------------------------
 * NOTAS:
 * ------------------------------------------------------------------
 * 1) javac traduz o codigo-fonte para bytecode (arquivo .class), um
 *    conjunto de instrucoes da JVM (ex.: aload, iload, iadd, invokevirtual).
 *    Veja o bytecode real com:  javap -c -p Exemplo18_MaquinaVirtualEBytecode
 * 2) A JVM carrega a classe (Bootstrap -> Platform -> App ClassLoader),
 *    verifica, prepara, resolve e inicializa (estaticos).
 * 3) Codigo Java NAO e especifico de maquina: roda em qualquer SO com JVM.
 * 4) O JIT compila trechos "quentes" de bytecode para codigo nativo.
 * 5) Memoria: objetos no HEAP (gerenciado pelo GC); chamadas na STACK,
 *    em quadros (frames) por thread.
 */

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Exemplo18_MaquinaVirtualEBytecode {

    // Classe alvo para inspecionar metadados.
    static class Conta {
        private int saldo;              // campo de instancia (heap)
        private static final int TAXA = 3; // campo estatico (metodo area)

        Conta(int saldo) {
            this.saldo = saldo;
        }

        int getSaldo() {
            return saldo;
        }

        void depositar(int valor) {
            saldo += valor;
        }
    }

    // Bloco estatico: roda no class load (fase de inicializacao).
    static {
        System.out.println("  [static init] Exemplo18 carregado e inicializado pela JVM");
    }

    public static void main(String[] args) throws Exception {

        System.out.println("==================================================");
        System.out.println(" EXEMPLO 18 - MAQUINA VIRTUAL JAVA E BYTECODE");
        System.out.println("==================================================\n");

        // -----------------------------------------------------------------
        // [1] Identificacao da JVM em execucao.
        // -----------------------------------------------------------------
        System.out.println("[1] Ambiente da JVM:");
        System.out.println("  java.version     = " + System.getProperty("java.version"));
        System.out.println("  java.vm.name     = " + System.getProperty("java.vm.name"));
        System.out.println("  os.arch          = " + System.getProperty("os.arch"));
        System.out.println("  compilador mesmo sistema? " + System.getProperty("java.vm.name").contains("Server"));
        System.out.println("  (o MESMO .class roda em qualquer SO com JVM compativel)");

        System.out.println();

        // -----------------------------------------------------------------
        // [2] Class loading.
        // -----------------------------------------------------------------
        System.out.println("[2] Carregamento de classe (ClassLoader):");
        ClassLoader loader = Exemplo18_MaquinaVirtualEBytecode.class.getClassLoader();
        System.out.println("  ClassLoader desta classe: " + loader);
        System.out.println("  ClassLoader do parent   : " + loader.getParent());
        Class<?> classe = Class.forName("Exemplo18_MaquinaVirtualEBytecode$Conta");
        System.out.println("  Class.forName carregou   : " + classe.getName());

        System.out.println();

        // -----------------------------------------------------------------
        // [3] Metadados: a estrutura que o bytecode descreve.
        // -----------------------------------------------------------------
        System.out.println("[3] Metadados de " + classe.getSimpleName() + ":");
        System.out.println("  superclasse : " + classe.getSuperclass().getName());
        System.out.print("  campos      : ");
        for (Field f : classe.getDeclaredFields()) {
            System.out.print(f.getType().getSimpleName() + " " + f.getName() + "; ");
        }
        System.out.println();
        System.out.print("  metodos     : ");
        for (Method m : classe.getDeclaredMethods()) {
            System.out.print(m.getName() + "(); ");
        }
        System.out.println();
        System.out.println("  (sao exatamente esses membros que o bytecode descreve)");

        System.out.println();

        // -----------------------------------------------------------------
        // [4] Stack x Heap (areas de memoria).
        // -----------------------------------------------------------------
        System.out.println("[4] Areas de memoria:");
        Runtime rt = Runtime.getRuntime();
        long antes = rt.totalMemory() - rt.freeMemory();
        // Cria muitos objetos -> vao para o HEAP.
        Conta[] contas = new Conta[100_000];
        for (int i = 0; i < contas.length; i++) {
            contas[i] = new Conta(i);
        }
        long depois = rt.totalMemory() - rt.freeMemory();
        System.out.println("  heap em uso antes  : " + (antes / 1024) + " KB");
        System.out.println("  heap em uso depois : " + (depois / 1024) + " KB");
        System.out.println("  100.000 objetos Conta alocados no HEAP (variaveis locais na STACK)");
        // mantem 'contas' acessivel ate o fim para o GC nao recolher.
        System.out.println("  contas[99999].getSaldo() = " + contas[99_999].getSaldo());

        System.out.println();

        // -----------------------------------------------------------------
        // [5] Bytecode: exibicao do nome das instrucoes (conceito).
        // -----------------------------------------------------------------
        System.out.println("[5] Bytecode:");
        System.out.println("  o metodo depositar(int) sera compilado para algo como:");
        System.out.println("    aload_0, iload_1, getfield saldo, iadd, putfield saldo, return");
        System.out.println("  inspecione de verdade com: javap -c -p '" + classe.getName() + "'");

        System.out.println("\n==================================================");
        System.out.println(" FIM DO EXEMPLO 18");
        System.out.println("==================================================");
    }
}
