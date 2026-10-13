/**
 * Exemplo 20 - Metaprogramacao (Reflection e Annotations)
 *
 * Cobre (metaprogramacao):
 * - [x] Reflexao: inspecionar e manipular classes em tempo de execucao
 * - [x] Criar instancias e invocar metodos dinamicamente
 * - [x] Acessar campo privado (quebrando encapsulamento)
 * - [x] Anotacoes personalizadas + leitura via reflexao
 * - [x] Processamento de anotacoes (APT) - conceito
 *
 * ------------------------------------------------------------------
 * NOTAS:
 * ------------------------------------------------------------------
 * 1) Metaprogramacao = o programa manipula a SI MESMO (ou outras classes)
 *    como dado, em tempo de execucao (reflection) ou de compilacao (APT).
 * 2) Reflexao permite descobrir metodos/campos, instanciar e invocar sem
 *    conhecer a classe em tempo de compilacao. Preco: e mais lenta e menos
 *    segura (erros so aparecem em execucao).
 * 3) Anotacoes (@interface) adicionam metadados. Com @Retention(RUNTIME)
 *    podem ser lidas por reflexao; com SOURCE/CLASS sao usadas por
 *    processadores de anotacoes (APT) em tempo de compilacao.
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Exemplo20_Metaprogramacao {

    // Anotacao personalizada, disponivel em tempo de execucao.
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.METHOD})
    @interface Info {
        String autor();
        String versao() default "1.0";
    }

    @Info(autor = "Equipe Compiladores", versao = "2026")
    static class Calculadora {
        private int memoria = 0;

        public int somar(int a, int b) {
            return a + b;
        }

        @Info(autor = "Equipe Compiladores")
        public int multiplicar(int a, int b) {
            return a * b;
        }

        private String segredo() {
            return "valor secreto";
        }
    }

    public static void main(String[] args) throws Exception {

        System.out.println("==================================================");
        System.out.println(" EXEMPLO 20 - METAPROGRAMACAO");
        System.out.println("==================================================\n");

        Class<?> clazz = Calculadora.class;

        // -----------------------------------------------------------------
        // [1] Inspecionar a classe.
        // -----------------------------------------------------------------
        System.out.println("[1] Reflection - inspecao:");
        System.out.println("  nome       : " + clazz.getName());
        System.out.print("  metodos    : ");
        for (Method m : clazz.getDeclaredMethods()) {
            System.out.print(m.getName() + " ");
        }
        System.out.println();
        System.out.print("  campos     : ");
        for (Field f : clazz.getDeclaredFields()) {
            System.out.print(f.getType().getSimpleName() + " " + f.getName() + " ");
        }
        System.out.println();

        System.out.println();

        // -----------------------------------------------------------------
        // [2] Instanciacao e invocacao dinamicas.
        // -----------------------------------------------------------------
        System.out.println("[2] Instanciar e invocar sem conhecer a classe em compilacao:");
        Constructor<?> ctor = clazz.getDeclaredConstructor();
        Object instancia = ctor.newInstance();

        Method somar = clazz.getMethod("somar", int.class, int.class);
        Object r1 = somar.invoke(instancia, 7, 5);
        System.out.println("  somar(7, 5) via reflection = " + r1);

        Method mult = clazz.getDeclaredMethod("multiplicar", int.class, int.class);
        Object r2 = mult.invoke(instancia, 6, 9);
        System.out.println("  multiplicar(6, 9)          = " + r2);

        System.out.println();

        // -----------------------------------------------------------------
        // [3] Acessar membros privados.
        // -----------------------------------------------------------------
        System.out.println("[3] Acessando membros privados:");
        Method segredo = clazz.getDeclaredMethod("segredo");
        segredo.setAccessible(true); // contorna o encapsulamento
        System.out.println("  segredo() = " + segredo.invoke(instancia));

        Field memoria = clazz.getDeclaredField("memoria");
        memoria.setAccessible(true);
        memoria.setInt(instancia, 42);
        System.out.println("  memoria (privada) definida para " + memoria.getInt(instancia));

        System.out.println();

        // -----------------------------------------------------------------
        // [4] Leitura de anotacoes via reflexao.
        // -----------------------------------------------------------------
        System.out.println("[4] Anotacoes lidas em tempo de execucao:");
        if (clazz.isAnnotationPresent(Info.class)) {
            Info info = clazz.getAnnotation(Info.class);
            System.out.println("  classe @Info: autor=" + info.autor() + ", versao=" + info.versao());
        }
        for (Method m : clazz.getDeclaredMethods()) {
            Info info = m.getAnnotation(Info.class);
            if (info != null) {
                System.out.println("  metodo " + m.getName() + " @Info: autor=" + info.autor()
                        + ", versao=" + info.versao());
            }
        }

        System.out.println();

        // -----------------------------------------------------------------
        // [5] APT (processamento de anotacoes em tempo de compilacao).
        // -----------------------------------------------------------------
        System.out.println("[5] Annotation Processing (APT):");
        System.out.println("  anotacoes com @Retention(SOURCE) sao lidas por processadores");
        System.out.println("  durante a compilacao (ex.: gerar codigo, validar). Lombok e um exemplo.");

        System.out.println("\n==================================================");
        System.out.println(" FIM DO EXEMPLO 20");
        System.out.println("==================================================");
    }
}
