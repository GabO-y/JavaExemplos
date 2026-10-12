/**
 * Exemplo 04 - Variaveis
 *
 * Cobre (Todo.md #4):
 * - [x] Nomes: case sensitive, caracteres permitidos (letras, digitos, _, $), convencoes
 * - [x] Vinculacao: estatica (tipagem forte e estatica)
 * - [x] Tempo de vida: local (stack), instancia/objeto (heap), estatica (class load)
 * - [x] Escopo: bloco, metodo, classe, pacote
 *
 * Exemplo aplicado: sistema de cafeteria que explora nomes, tipos, tempo de
 * vida e escopo de variaveis.
 */

// Classe de apoio para demonstrar variaveis de instancia e estaticas.
class Balcao {
    // Variaveis de INSTANCIA: vivem no heap enquanto o objeto existir.
    String nomeBalcao;
    int pedidosDoDia;

    // Variavel ESTATICA: uma unica copia por classe, criada no class load.
    static int totalPedidosGlobais = 0;

    // Variavel estatica final: constante (convencao: MAIUSCULAS com _).
    static final String LOJA = "Cafe do Campus";

    Balcao(String nomeBalcao) {
        this.nomeBalcao = nomeBalcao;
    }

    void registrarPedido() {
        pedidosDoDia++;
        totalPedidosGlobais++;
    }
}

public class Exemplo04_Variaveis {

    public static void main(String[] args) {
        System.out.println("=== NOMES: CASE SENSITIVE E CARACTERES PERMITIDOS ===");

        // Java e case sensitive: nome, Nome e NOME sao variaveis diferentes.
        int pedido = 1;
        int Pedido = 2;
        int PEDIDO = 3;
        System.out.println("pedido=" + pedido + " Pedido=" + Pedido + " PEDIDO=" + PEDIDO);

        // Caracteres permitidos: letras, digitos, underscore (_) e cifrao ($).
        int _pedido_1 = 10;        // underscore: permitido (cuidado com convencoes)
        int $preco$ = 20;          // cifrao: permitido (usado por gerados de codigo)
        int caféValido = 30;       // caracteres Unicode/acentuados tambem valem
        int precoPorLitro = 4;     // convencao: camelCase para nomes normais
        System.out.println("_pedido_1=" + _pedido_1 + " $preco$=" + $preco$ + " caféValido=" + caféValido + " precoPorLitro=" + precoPorLitro);

        // Regra importante: nao pode comecar com digito.
        // int 2pedidos;            // <- ERRO DE COMPILACAO
        // Nomes sao case sensitive, logo "cafe" != "Cafe".

        System.out.println();
        System.out.println("=== VINCULACAO ESTATICA (TIPAGEM FORTE E ESTATICA) ===");

        // O tipo de cada variavel e definido na declaracao e NAO muda.
        int inteiro = 42;
        System.out.println("inteiro = " + inteiro + " (tipo: int)");

        // Violar o tipo antes da compilacao e erro: cada linha abaixo nao compila.
        // int erroTexto = "texto";      // <- ERRO: String nao cabe em int
        // String erroNumero = 42;       // <- ERRO: int nao cabe em String
        // double x = true;              // <- ERRO: boolean nao vira double

        // Mas conversoes EXPLICITAS (casting) sao permitidas.
        double precoEmReais = 5.9;
        int precoEmCentavosInt = (int) (precoEmReais * 100); // truncamento
        System.out.println("(int)(5.9 * 100) = " + precoEmCentavosInt);

        // 'var' nao fura a tipagem: o tipo e inferido de uma vez.
        var nomeBebida = "capuccino";     // inferido como String
        System.out.println("var nomeBebida = " + nomeBebida + " (inferido: String)");
        // nomeBebida = 42;               // <- ERRO: String nao vira int

        System.out.println();
        System.out.println("=== TEMPO DE VIDA: LOCAL, INSTANCIA E ESTATICA ===");

        Balcao balcao1 = new Balcao("Balcao principal");
        Balcao balcao2 = new Balcao("Balcao drive-thru");

        balcao1.registrarPedido();
        balcao1.registrarPedido();
        balcao2.registrarPedido();

        System.out.println("balcao1.pedidosDoDia = " + balcao1.pedidosDoDia);
        System.out.println("balcao2.pedidosDoDia = " + balcao2.pedidosDoDia);
        System.out.println("totalPedidosGlobais (estatica) = " + Balcao.totalPedidosGlobais);
        System.out.println("constante LOJA = " + Balcao.LOJA);

        {
            // Bloco interno: variaveis locais vivem so dentro do bloco.
            int dentroDoBloco = 7;
            System.out.println("localidades: " + dentroDoBloco + " existe dentro do bloco");
        }
        // System.out.println(dentroDoBloco); // <- ERRO: fora do escopo do bloco

        int vidaUtilSomada = 0;
        for (int i = 0; i < 3; i++) {      // 'i' e local ao for
            vidaUtilSomada += i;
        }
        // System.out.println(i);           // <- ERRO: 'i' nao existe fora do for
        System.out.println("vidaUtilSomada (local) = " + vidaUtilSomada);

        System.out.println();
        System.out.println("=== ESCOPO: BLOCO, METODO, CLASSE E PACOTE ===");

        // Escopos observaveis por visibilidade dos modificadores:
        // - public: qualquer classe do projeto/pacote
        // - protected: subclasses e mesmo pacote
        // - (sem modificador): mesmo pacote (package-private)
        // - private: somente a propria classe
        System.out.println("Balcao.LOJA (public static final) acessivel de qualquer lugar: " + Balcao.LOJA);
        System.out.println("Balcao.totalPedidosGlobais (package-private static) acessivel do mesmo pacote");

        // Escopo de METODO: parametros e locais nao sao vistos fora.
        int resultado = dobrar(21);        // parametro 'valor' existe apenas dentro de dobrar()
        System.out.println("dobrar(21) = " + resultado);

        // Escopo de BLOCO: blocos irmaos podem reutilizar o mesmo nome.
        int nota = 1;
        {
            int notaLocal = 2;             // escopo restrito ao bloco
            System.out.println("notaLocal dentro do bloco = " + notaLocal);
        }
        {
            int notaLocal = 3;             // mesmo nome, outro bloco irmão: ok
            System.out.println("notaLocal no 2º bloco = " + notaLocal);
        }
        // Re-declarar dentro de bloco ANINHADO (shadowing) e erro de compilacao.
        // {
        //     int nota = 9;               // <- ERRO: nome 'nota' ja existe no metodo
        // }
        System.out.println("nota fora = " + nota);

        // Escopo de PACOTE: classes do mesmo arquivo/pacote se enxergam.
        // Balcao (classe do mesmo pacote default) foi usado neste main sem import.
        System.out.println("Classe 'Balcao' do mesmo pacote default acessivel sem import.");
    }

    // Metodo de apio: 'valor' tem escopo de metodo (parametro + corpo).
    static int dobrar(int valor) {
        int dobro = valor * 2;             // local de metodo
        return dobro;
    }
}
