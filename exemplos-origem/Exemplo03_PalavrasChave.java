/**
 * Exemplo 03 - Palavras-Chave e Reservadas
 *
 * Cobre (Todo.md #3):
 * - [x] Listar palavras-chave (abstract, final, static, synchronized, volatile, transient, etc.)
 * - [x] Mostrar exemplos de uso das principais palavras-chave
 * - [x] Palavras reservadas mas nao usadas (goto, const)
 * - [x] Literais especiais (true, false, null)
 *
 * Exemplo aplicado: um pequeno sistema de estoque que usa muitas palavras-chave
 * na pratica, seguido da listagem completa das palavras reservadas do Java.
 */

import java.util.Arrays;

// abstract: classe que nao pode ser instanciada, serve de modelo.
abstract class ProdutoBase {
    protected final String nome;          // final: valor imutavel apos atribuicao
    protected double preco;

    // final: metodo que nao pode ser sobrescrito.
    final String descricao() {
        return nome + " custa R$" + preco;
    }

    // abstract: metodo sem corpo, obrigatorio implementar nas subclasses.
    abstract String categoria();

    ProdutoBase(String nome, double preco) {
        this.nome = nome;                  // this: referencia ao proprio objeto
        this.preco = preco;
    }
}

// final: classe que NAO pode ter subclasses.
final class Produto extends ProdutoBase {
    Produto(String nome, double preco) {
        super(nome, preco);                // super: chama o construtor da superclasse
    }

    @Override                              // anotacao, nao palavra-chave
    String categoria() {
        return "produto";
    }
}

// interface + implements: contrato de metodos.
interface ItemDescontavel {
    // public static final: constantes de interface sao implicitamente assim.
    double DESCONTO_MAXIMO = 0.5;

    double aplicarDesconto(double fator);
}

// implements: obriga o Produto a implementar o contrato da interface.
final class ProdutoComDesconto extends ProdutoBase implements ItemDescontavel {
    private final double margem;           // private: acesso restrito a classe

    // static: metodo pertence a classe, nao a instancias.
    static ProdutoComDesconto criar(String nome, double preco, double margem) {
        return new ProdutoComDesconto(nome, preco, margem);
    }

    private ProdutoComDesconto(String nome, double preco, double margem) {
        super(nome, preco);
        this.margem = margem;
    }

    @Override
    public double aplicarDesconto(double fator) {
        return preco * (1 - Math.min(fator, DESCONTO_MAXIMO));
    }

    @Override
    String categoria() {
        return "produto com desconto";
    }
}

// Classe com campos que usam palavras-chave de concorrencia/persistencia.
class Estoque {
    volatile int exclusoes;                // volatile: leitura/escrita visivel entre threads
    transient double calculoCache;         // transient: campo nao serializado
    private int total = 0;

    // synchronized: somente uma thread por vez entra no metodo.
    synchronized int adicionar() {
        return ++total;
    }

    int total() {
        return total;                     // getter com acesso default (package-private)
    }
}

public class Exemplo03_PalavrasChave {

    public static void main(String[] args) {
        System.out.println("=== LITERAIS ESPECIAIS: true, false, null ===");

        boolean ativo = true;
        boolean inativo = false;
        Produto nenhum = null;              // null representa ausencia de referencia

        System.out.println("boolean ativo = " + ativo);
        System.out.println("boolean inativo = " + inativo);
        System.out.println("Produto nenhum = " + nenhum);

        // Literais podem ser usados em expressoes.
        if (ativo && !inativo) {
            System.out.println("'true' e 'false' funcionam como expressoes logicas.");
        }
        // null so "aceita" tipos de referencia, nunca primitivos:
        // int x = null; // <- ERRO DE COMPILACAO

        System.out.println();
        System.out.println("=== PALAVRAS-CHAVE EM USO (estoque) ===");

        // class, new, var (tipo contextual).
        var cafe = new Produto("Cafe", 18.90);          // var: inferencia de tipo local
        var promo = ProdutoComDesconto.criar("Tablet", 1200.0, 0.2);

        System.out.println(cafe.descricao() + " [" + cafe.categoria() + "]");
        System.out.println(promo.descricao() + " [" + promo.categoria() + "]");
        System.out.println("tablet com desconto de 20%: R$" + promo.aplicarDesconto(0.2));

        // final: reatribuir causa erro de compilacao -> tentativa em comentario.
        // cafe = null;           // permite (var nao e final)
        // cafe.nome = "Outro";   // nome e final: ERRO DE COMPILACAO

        Estoque estoque = new Estoque();
        for (int i = 0; i < 3; i++) {
            estoque.adicionar();
        }
        System.out.println("estoque total (int) = " + estoque.total() + " | exclusoes (volatile) = " + estoque.exclusoes);

        // instanceof: teste de tipo em tempo de execucao.
        System.out.println("cafe instanceof Produto ? " + (cafe instanceof Produto));
        System.out.println("promo instanceof ItemDescontavel ? " + (promo instanceof ItemDescontavel));

        System.out.println();
        System.out.println("=== PALAVRAS RESERVADAS MAS NAO USADAS: goto e const ===");

        // goto e const existem como palavras reservadas, mas NAO sao usadas.
        // int goto = 5;   // <- ERRO DE COMPILACAO: goto e reservada
        // const float PI = 3.14f;  // <- ERRO: em Java usa-se 'final'
        System.out.println("'goto' e 'const' sao reservadas, porem proibidas;");
        System.out.println("o papel de 'const' e feito por 'final' e o de 'goto' por estruturas de controle.");

        System.out.println();
        System.out.println("=== LISTA COMPLETA DE PALAVRAS RESERVADAS ===");

        String[] essenciais = {
            "abstract", "assert", "boolean", "break", "byte", "case", "catch",
            "char", "class", "continue", "default", "do", "double", "else",
            "enum", "extends", "final", "finally", "float", "for", "if",
            "implements", "import", "instanceof", "int", "interface", "long",
            "native", "new", "package", "private", "protected", "public",
            "return", "short", "static", "strictfp", "super", "switch",
            "synchronized", "this", "throw", "throws", "transient", "try",
            "void", "volatile", "while"
        };
        String[] proibidas = { "goto", "const" };
        String[] literais = { "true", "false", "null" };
        String[] contextual = {
            "var", "record", "yield", "sealed", "permits", "non-sealed",
            "module", "open", "requires", "exports", "opens", "uses", "provides", "with"
        };

        System.out.println("Essenciais (" + essenciais.length + "): " + Arrays.toString(essenciais));
        System.out.println("Proibidas (" + proibidas.length + "): " + Arrays.toString(proibidas));
        System.out.println("Literais (" + literais.length + "): " + Arrays.toString(literais));
        System.out.println("Contextuais (" + contextual.length + "): " + Arrays.toString(contextual));
        System.out.println("Total: " + (essenciais.length + proibidas.length + literais.length + contextual.length));
    }
}
