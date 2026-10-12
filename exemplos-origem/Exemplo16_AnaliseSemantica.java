/**
 * Exemplo 16 - Analise Semantica e Tabela de Simbolos
 *
 * Cobre (fase de compilacao):
 * - [x] Papel da analise semantica (verificacoes apos o parser)
 * - [x] Tabela de simbolos (nome -> tipo), com escopos aninhados
 * - [x] Regra de ESCOPO e resolucao de nomes
 * - [x] Erros semanticos: variavel nao declarada, redeclaracao, tipo incompativel
 *
 * ------------------------------------------------------------------
 * NOTAS:
 * ------------------------------------------------------------------
 * A analise semantica verifica SIGNIFICADO que a gramatica nao captura:
 * se um nome foi declarado antes do uso, se nao ha nomes duplicados no
 * mesmo escopo e se os tipos combinam. O compilador caminha pela AST
 * mantendo uma pilha de escopos (tabela de simbolos).
 */

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.Map;

public class Exemplo16_AnaliseSemantica {

    record Simbolo(String nome, String tipo) {}

    static class ErroSemantico extends RuntimeException {
        ErroSemantico(String msg) {
            super(msg);
        }
    }

    // -----------------------------------------------------------------
    // Tabela de simbolos com pilha de escopos.
    // -----------------------------------------------------------------
    static class TabelaSimbolos {
        private final Deque<Map<String, Simbolo>> escopos = new ArrayDeque<>();

        TabelaSimbolos() {
            entrarEscopo(); // escopo global
        }

        void entrarEscopo() {
            escopos.push(new LinkedHashMap<>());
        }

        void sairEscopo() {
            if (escopos.size() <= 1) {
                throw new ErroSemantico("nao ha escopo para sair");
            }
            escopos.pop();
        }

        // declarar: erro se ja existe no MESMO escopo.
        void declarar(String nome, String tipo) {
            Map<String, Simbolo> atual = escopos.peek();
            if (atual.containsKey(nome)) {
                throw new ErroSemantico("redeclaracao de '" + nome + "' no mesmo escopo");
            }
            atual.put(nome, new Simbolo(nome, tipo));
        }

        // procurar: varre do escopo mais interno para o mais externo.
        Simbolo procurar(String nome) {
            for (Map<String, Simbolo> e : escopos) {
                Simbolo s = e.get(nome);
                if (s != null) {
                    return s;
                }
            }
            return null;
        }

        String tipoDe(String nome) {
            Simbolo s = procurar(nome);
            if (s == null) {
                throw new ErroSemantico("variavel nao declarada: '" + nome + "'");
            }
            return s.tipo();
        }

        int profundidade() {
            return escopos.size();
        }
    }

    // Verifica atribuicao: o tipo do valor precisa casar com o da variavel.
    static void atribuir(TabelaSimbolos t, String nome, String tipoValor) {
        String tipoVar = t.tipoDe(nome);
        if (!tipoVar.equals(tipoValor)) {
            throw new ErroSemantico("tipo incompativel em '" + nome + "': "
                    + tipoVar + " recebe " + tipoValor);
        }
        System.out.println("  ok: " + nome + " (" + tipoVar + ") = valor " + tipoValor);
    }

    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println(" EXEMPLO 16 - ANALISE SEMANTICA E TABELA DE SIMBOLOS");
        System.out.println("==================================================\n");

        // -----------------------------------------------------------------
        // [1] Caso valido: declarar, usar e respeitar tipos.
        // -----------------------------------------------------------------
        System.out.println("[1] Programa valido:");
        TabelaSimbolos t = new TabelaSimbolos();
        t.declarar("idade", "int");
        t.declarar("nome", "String");
        atribuir(t, "idade", "int");
        atribuir(t, "nome", "String");

        System.out.println();

        // -----------------------------------------------------------------
        // [2] Escopos aninhados e resolucao de nomes.
        // -----------------------------------------------------------------
        System.out.println("[2] Escopos aninhados (profundidade " + t.profundidade() + "):");
        t.entrarEscopo();
        t.declarar("idade", "int"); // sombreia a 'idade' externa, mas e outro escopo: ok
        System.out.println("  dentro do bloco, profundidade = " + t.profundidade()
                + ", 'idade' resolvida no escopo interno");
        t.sairEscopo();
        System.out.println("  fora do bloco, profundidade = " + t.profundidade()
                + ", 'idade' volta a resolver no escopo global");
        System.out.println();

        // -----------------------------------------------------------------
        // [3] Variavel nao declarada.
        // -----------------------------------------------------------------
        System.out.println("[3] Erro: variavel nao declarada");
        try {
            t.tipoDe("peso");
        } catch (ErroSemantico e) {
            System.out.println("  ErroSemantico -> " + e.getMessage());
        }

        System.out.println();

        // -----------------------------------------------------------------
        // [4] Redeclaracao no mesmo escopo.
        // -----------------------------------------------------------------
        System.out.println("[4] Erro: redeclaracao");
        try {
            t.declarar("idade", "int");
        } catch (ErroSemantico e) {
            System.out.println("  ErroSemantico -> " + e.getMessage());
        }

        System.out.println();

        // -----------------------------------------------------------------
        // [5] Tipo incompativel.
        // -----------------------------------------------------------------
        System.out.println("[5] Erro: tipos incompativeis");
        try {
            atribuir(t, "idade", "String");
        } catch (ErroSemantico e) {
            System.out.println("  ErroSemantico -> " + e.getMessage());
        }

        System.out.println("\n==================================================");
        System.out.println(" FIM DO EXEMPLO 16");
        System.out.println("==================================================");
    }
}
