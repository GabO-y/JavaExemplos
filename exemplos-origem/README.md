## Requisito

**JDK 21** (compilador `javac`). Confira se já está instalado:

```bash
java -version
javac -version
```

### Instalando o JDK 21

**Linux (Debian/Ubuntu):**

```bash
sudo apt update
sudo apt install openjdk-21-jdk
```

**Linux/macOS (SDKMAN):**

```bash
curl -s "https://get.sdkman.io" | bash
# abra um novo terminal e então:
sdk install java 21.0.8-tem
```

**Windows:** baixe o instalador `.msi` do projeto Temurin (Adoptium):

- https://adoptium.net (versão 21, x64)

## Como compilar

A partir da raiz do repositório, compile todos os exemplos:

```bash
javac -d bin exemplos-origem/*.java
```

Os arquivos `.class` gerados ficam na pasta `bin/`.

## Como executar

```bash
java -cp bin NomeDoExemplo
```

Por exemplo:

```bash
java -cp bin Exemplo01_TiposDeDados
```

Cada exemplo imprime os resultados dos tópicos demonstrados com
`System.out.println`.

## Exemplos disponíveis

| Arquivo | Tópico |
|---|---|
| `Exemplo01_TiposDeDados.java` | Tipos de dados |
| `Exemplo02_PonteirosEReferencias.java` | Ponteiros e referências |
| `Exemplo03_PalavrasChave.java` | Palavras-chave e reservadas |
| `Exemplo04_Variaveis.java` | Variáveis |
| `Exemplo05_Expressoes.java` | Expressões |
| `Exemplo06_ControleDeFluxo.java` | Estruturas de controle e desvios de fluxo |
| `Exemplo07_Condicionais.java` | Comandos condicionais |
| `Exemplo08_Repeticao.java` | Comandos de repetição |
| `Exemplo09_Subprogramas.java` | Subprogramas |
| `Exemplo10_Concorrencia.java` | Concorrência |
| `Exemplo11_Excecoes.java` | Tratamento de exceções |
| `Exemplo12_Eventos.java` | Tratamento de eventos |
| `Exemplo13_Paradigmas.java` | Paradigmas e aspectos adicionais |
| `Exemplo14_AnaliseLexica.java` | Análise léxica |
| `Exemplo15_AnaliseSintatica.java` | Análise sintática |
| `Exemplo16_AnaliseSemantica.java` | Análise semântica |
| `Exemplo17_GeracaoDeCodigo.java` | Geração de código |
| `Exemplo18_MaquinaVirtualEBytecode.java` | Máquina virtual Java e bytecode |
| `Exemplo19_Otimizacao.java` | Otimização de código |
| `Exemplo20_Metaprogramacao.java` | Metaprogramação (Reflection e Annotations) |
