/**
 * Exemplo 16 - Analise Semantica e Deteccao de Erros
 *
 * Cobre (fase de compilacao):
 * - [x] Papel da analise semantica (verificacoes apos o parser)
 * - [x] Tabela de simbolos (nome -> tipo), com escopos aninhados
 * - [x] Regra de ESCOPO e resolucao de nomes
 * - [x] Erros semanticos: variavel nao declarada, redeclaracao, tipo incompativel
 * - [x] Erros por fase: lexico, sintatico e semantico
 * - [x] Hierarquia de excecoes do compilador (base comum)
 * - [x] Diagnostico com fase + mensagem e recuperacao (varios erros)
 * - [x] Erro de compilacao x erro de execucao
 *
 * ------------------------------------------------------------------
 * NOTAS:
 * ------------------------------------------------------------------
 * A analise semantica verifica SIGNIFICADO que a gramatica nao captura:
 * se um nome foi declarado antes do uso, se nao ha nomes duplicados no
 * mesmo escopo e se os tipos combinam. O compilador caminha pela AST
 * mantendo uma pilha de escopos (tabela de simbolos).
 *
 * Este exemplo tambem demonstra o TRATAMENTO DE ERROS de compilacao
 * (antes no Exemplo 21): um compilador emite DIAGNOSTICOS. Erros
 * lexico/sintatico/semantico impedem a geracao de codigo; erros de
 * execucao (RuntimeException) so aparecem quando o programa roda.
 */

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Exemplo16_AnaliseSemantica {

    // -----------------------------------------------------------------
    // Hierarquia de erros de compilacao (base comum + fases).
    // -----------------------------------------------------------------
    static abstract class ErroCompilacao extends RuntimeException {
        final String fase;

        ErroCompilacao(String fase, String msg) {
            super(msg);
            this.fase = fase;
        }

        String diagnostico() {
            return "[" + fase + "] " + getMessage();
        }
    }

    static class ErroLexico extends ErroCompilacao {
        ErroLexico(String msg) { super("LEXICO", msg); }
    }

    static class ErroSintatico extends ErroCompilacao {
        ErroSintatico(String msg) { super("SINTATICO", msg); }
    }

    static class ErroSemantico extends ErroCompilacao {
        ErroSemantico(String msg) { super("SEMANTICO", msg); }
    }

    record Simbolo(String nome, String tipo) {}

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

    // -----------------------------------------------------------------
    // Mini-compilador: cada fase lanca o erro correspondente.
    // -----------------------------------------------------------------
    static void faseLexica(String fonte) {
        for (char c : fonte.toCharArray()) {
            if (c == '$') {
                throw new ErroLexico("caractere invalido '$'");
            }
        }
    }

    static void faseSintatica(String fonte) {
        if (!fonte.trim().endsWith(";")) {
            throw new ErroSintatico("faltando ';' no fim da sentenca");
        }
        if (conta(fonte, '(') != conta(fonte, ')')) {
            throw new ErroSintatico("parenteses desbalanceados");
        }
    }

    static void faseSemantica(String fonte) {
        if (fonte.contains("int x = \"")) {
            throw new ErroSemantico("tipo incompativel: int recebendo texto");
        }
    }

    static int conta(String s, char c) {
        int n = 0;
        for (char ch : s.toCharArray()) {
            if (ch == c) {
                n++;
            }
        }
        return n;
    }

    // Compila reportando TODOS os erros (recuperacao simples).
    static List<String> compilar(String fonte) {
        List<String> diagnosticos = new ArrayList<>();
        List<Runnable> fases = List.of(
                () -> faseLexica(fonte),
                () -> faseSintatica(fonte),
                () -> faseSemantica(fonte));
        for (Runnable fase : fases) {
            try {
                fase.run();
            } catch (ErroCompilacao e) {
                diagnosticos.add(e.diagnostico());
            }
        }
        return diagnosticos;
    }

    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println(" EXEMPLO 16 - ANALISE SEMANTICA E DETECCAO DE ERROS");
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
            System.out.println("  " + e.diagnostico());
        }

        System.out.println();

        // -----------------------------------------------------------------
        // [4] Redeclaracao no mesmo escopo.
        // -----------------------------------------------------------------
        System.out.println("[4] Erro: redeclaracao");
        try {
            t.declarar("idade", "int");
        } catch (ErroSemantico e) {
            System.out.println("  " + e.diagnostico());
        }

        System.out.println();

        // -----------------------------------------------------------------
        // [5] Tipo incompativel.
        // -----------------------------------------------------------------
        System.out.println("[5] Erro: tipos incompativeis");
        try {
            atribuir(t, "idade", "String");
        } catch (ErroSemantico e) {
            System.out.println("  " + e.diagnostico());
        }

        System.out.println();

        // -----------------------------------------------------------------
        // [6] Erros por fase (lexico, sintatico, semantico).
        // -----------------------------------------------------------------
        System.out.println("[6] Erro por fase:");
        String[] fontes = {
                "int x = 3;",              // valido
                "int x = 3 $",             // erro lexico
                "int x = (3 + 4",          // erro sintatico
                "int x = \"texto\";"       // erro semantico
        };
        for (String f : fontes) {
            try {
                faseLexica(f);
                faseSintatica(f);
                faseSemantica(f);
                System.out.println("  OK        : " + f);
            } catch (ErroCompilacao e) {
                System.out.println("  " + e.diagnostico() + "  <- " + f);
            }
        }

        System.out.println();

        // -----------------------------------------------------------------
        // [7] Recuperacao: reportar varios erros de uma vez.
        // -----------------------------------------------------------------
        System.out.println("[7] Recuperacao de erros (varios diagnosticos):");
        String ruim = "int x = \"oi\" $ y;";
        for (String d : compilar(ruim)) {
            System.out.println("  " + d);
        }

        System.out.println();

        // -----------------------------------------------------------------
        // [8] Erro de compilacao x erro de execucao.
        // -----------------------------------------------------------------
        System.out.println("[8] Compilacao x execucao:");
        System.out.println("  'int x = 1/0;' COMPILA, mas falha na EXECUCAO:");
        try {
            int x = 1 / 0; // erro de execucao (RuntimeException)
            System.out.println("  " + x);
        } catch (ArithmeticException e) {
            System.out.println("  RuntimeException em execucao -> " + e.getMessage());
        }
        System.out.println("  Ja 'int x = ;' nem compila (erro de SINTAXE).");

        System.out.println("\n==================================================");
        System.out.println(" FIM DO EXEMPLO 16");
        System.out.println("==================================================");
    }
}
