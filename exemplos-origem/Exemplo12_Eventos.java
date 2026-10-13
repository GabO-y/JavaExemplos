/**
 * Exemplo 12 - Tratamento de Eventos
 *
 * Exemplo aplicado: uma casa inteligente que reage a eventos (campainha,
 * botoes de GUI, sensores) usando os modelos de eventos do Java.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;
import javax.swing.JButton;

// Listener: interface padrao do modelo Observer/LISTENER.
interface AlarmeListener {
    void alarmeDisparado(String origem);
}

// Subject (Observable): mantem os listeners e notifica todos.
class SistemaDeAlarme {
    private final List<AlarmeListener> ouvintes = new ArrayList<>();

    void registrar(AlarmeListener ouvinte) {
        ouvintes.add(ouvinte);
    }

    void disparar(String origem) {
        System.out.println("evento disparado a partir de: " + origem);
        for (AlarmeListener ouvinte : ouvintes) {   // notifica todos
            ouvinte.alarmeDisparado(origem);
        }
    }
}

// Interface FUNCIONAL usada como callback (tem um unico metodo abstrato).
@FunctionalInterface
interface CallbackSensor {
    void onLeitura(double valor);
}

// Classe que usa callbacks para cada leitura do sensor.
class SensorTemperatura {
    void ler(CallbackSensor callback) {       // recebe o "codigo a executar" de fora
        callback.onLeitura(22.5);             // invoca o callback
    }
}

public class Exemplo12_Eventos {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== [1] MODELO DE EVENTOS: LISTENER/OBSERVER ===");

        SistemaDeAlarme alarme = new SistemaDeAlarme();

        // Cada listener reage do seu jeito (lambda -> listener e interface funcional).
        alarme.registrar(origem -> System.out.println("  central: registrando " + origem));
        alarme.registrar(origem -> System.out.println("  app: notificacao enviada sobre " + origem));

        alarme.disparar("movimento na sala");   // ambos os ouvintes sao chamados

        // Tambem funciona com classe/instancia anonima classica.
        alarme.registrar(new AlarmeListener() {
            @Override
            public void alarmeDisparado(String origem) {
                System.out.println("  sirene: tocando por causa de " + origem);
            }
        });
        alarme.disparar("porta aberta");

        System.out.println();
        System.out.println("=== [2] EVENTOS DE GUI (AWT/Swing) ===");

        // Botao Swing: o ActionListener e uma interface funcional (callback).
        JButton botaoLuz = new JButton("Ligar luz");
        botaoLuz.addActionListener(e -> System.out.println("  clique -> toggling light, action=" + e.getActionCommand()));

        // Sem precisar de janela visivel, o doClick() dispara o ActionEvent,
        // e o listener registrado executa.
        botaoLuz.doClick();
        botaoLuz.doClick();

        // Mostrando que o listener e notificado com o evento (ActionEvent).
        JButton botaoCamera = new JButton("Camera");
        botaoCamera.addActionListener(e -> System.out.println("  evento " + e.getClass()
                + " na origem " + e.getSource().getClass().getSimpleName()));
        botaoCamera.doClick();

        System.out.println();
        System.out.println("=== [3] INTERFACES FUNCIONAIS PARA CALLBACKS ===");

        SensorTemperatura sensor = new SensorTemperatura();

        // Callback via lambda: decidimos o que acontecer quando houver leitura.
        sensor.ler(v -> System.out.printf("  temperatura lida: %.1f C%n", v));

        // O mesmo callback pode acumular valores em vez de imprimir.
        List<Double> historico = new ArrayList<>();
        sensor.ler(historico::add);                     // method reference como callback
        sensor.ler(v -> historico.add(v));
        System.out.println("  historico acumulado via callback: " + historico);

        // Callbacks tambem sao usados em APIs padrao (Consumer/Runnable).
        Runnable aoIniciar = () -> System.out.println("  callback Runnable: sistema inicializando...");
        aoIniciar.run();

        System.out.println();
        System.out.println("=== [4] EVENTOS REATIVOS: FLOW API (Reactive Streams) ===");

        // SubmissionPublisher emite itens; Subscriber reage a cada onNext.
        SubmissionPublisher<String> publicador = new SubmissionPublisher<>();
        CountDownLatch latch = new CountDownLatch(4);   // 3 itens + 1 onComplete

        Flow.Subscriber<String> assinante = new Flow.Subscriber<>() {
            private Flow.Subscription assinatura;

            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                this.assinatura = subscription;
                assinatura.request(Long.MAX_VALUE);     // pede todos os proximos itens
            }

            @Override
            public void onNext(String item) {
                System.out.println("  onNext recebido: " + item);
                latch.countDown();
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("  onError: " + throwable.getMessage());
                latch.countDown();
            }

            @Override
            public void onComplete() {
                System.out.println("  (fluxo concluido - onComplete)");
                latch.countDown();
            }
        };

        publicador.subscribe(assinante);

        // Eventos emitidos de forma assincrona pelo publicador.
        publicador.submit("sensor temperatura 22.5C");
        publicador.submit("sensor luminosidade 415 lux");
        publicador.submit("alerta: movimento no corredor");
        publicador.close();                              // sinaliza onComplete

        latch.await(3, TimeUnit.SECONDS);                // aguarda os eventos fluirem
    }
}
