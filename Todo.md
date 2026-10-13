# Checklist de Análise da Linguagem Java

## 1. Tipos de Dados
- [x] **Tipos primitivos** (byte, short, int, long, float, double, char, boolean)
  - [x] Tamanho em bits/bytes de cada tipo
  - [x] Representação (ponto fixo vs. ponto flutuante)
  - [x] Valores padrão
- [x] **Tipos compostos/referência** (classes, interfaces, arrays, enums, records)
  - [x] Formato de implementação (heap, vetores estáticos vs. dinâmicos)
  - [x] Tipos permitidos para índices de arrays
- [x] **Tipos básicos padrão da linguagem** (String, wrappers, Object, etc.)
- [x] **Conversões** (implícitas, explícitas/casting, boxing/unboxing)

## 2. Ponteiros e Referências
- [x] Java usa referências (não ponteiros explícitos)
- [x] Gerenciamento de memória (Garbage Collector)
- [x] Sem aritmética de ponteiros
- [x] Referências nulas (null)

## 3. Palavras-Chave e Reservadas
- [x] Listar palavras-chave (abstract, final, static, synchronized, volatile, transient, etc.)
- [x] Palavras reservadas mas não usadas (goto, const)
- [x] Literais especiais (true, false, null)

## 4. Variáveis
- [x] **Nomes**: case sensitive, caracteres permitidos (letras, dígitos, `_`, `$`), convenções
- [x] **Vinculação**: estática (tipagem forte e estática)
- [x] **Tempo de vida**: local (stack), instância/objeto (heap), estática (class load)
- [x] **Escopo**: bloco, método, classe, pacote

## 5. Expressões
- [x] **Associatividade** (esquerda/direita)
- [x] **Precedência** de operadores
- [x] **Sobrecarga de operadores** (Java não suporta sobrecarga de operadores definida pelo usuário, exceto `+` para String)

## 6. Estruturas de Controle e Desvios de Fluxo
- [x] `break`, `continue`, `return`
- [x] `break`/`continue` rotulados
- [x] `yield` (switch expressions)

## 7. Comandos Condicionais
- [x] `if` / `else if` / `else`
- [x] `switch` (tradicional e expressão)
- [x] Operador ternário
- [x] Pattern matching (instanceof, switch)

## 8. Comandos de Repetição
- [x] `for` tradicional
- [x] `for-each` (enhanced for)
- [x] `while`
- [x] `do-while`

## 9. Subprogramas
- [x] **Declaração e chamada** de métodos
- [x] **Modificadores** (public, private, protected, static, final, abstract)
- [x] **Passagem de parâmetros**: apenas por valor (objetos passam referência por valor)
- [x] **Sobrecarga** e **sobrescrita** de métodos
- [x] **Co-rotinas**: não suportadas nativamente (alternativas com threads/virtual threads)
- [x] **Funções lambda** (a partir do Java 8)
- [x] **Programação genérica** (Generics)
- [x] **Métodos delegados** (via composição, não nativo)

## 10. Concorrência
- [ ] **Threads** (Thread, Runnable)
- [ ] **Executors / Thread Pools**
- [ ] **Sincronização**: `synchronized`, `Lock`, `Semaphore`, `Atomic*`
- [ ] **CompletableFuture** (programação assíncrona)
- [ ] **Virtual Threads** (Java 21+)
- [ ] **Coleções concorrentes** (ConcurrentHashMap, etc.)
- [ ] **Memória compartilhada** e `volatile`

## 11. Tratamento de Exceções
- [ ] **Checked exceptions** (verificadas em compilação)
- [ ] **Unchecked exceptions** (RuntimeException e Error)
- [ ] **Erros tratáveis vs. intratáveis** (Error vs. Exception)
- [ ] **Criação de exceções personalizadas**
- [ ] **Blocos** `try`, `catch`, `finally`
- [ ] **try-with-resources**
- [ ] **Delegação** (`throws`)
- [ ] **Multi-catch**

## 12. Tratamento de Eventos
- [ ] **Modelo de eventos** (Listener/Observer)
- [ ] **Eventos de GUI** (AWT/Swing/JavaFX)
- [ ] **Interfaces funcionais** para callbacks
- [ ] **Eventos em programação reativa** (Flow API, Reactive Streams)

## 13. Paradigmas e Aspectos Adicionais
- [ ] **Paradigma orientado a objetos** (classes, herança, polimorfismo, encapsulamento)
- [ ] **Paradigma funcional** (lambdas, Streams, Optional)
- [ ] **Metaprogramação** (Reflection, Annotations, APT)
- [ ] **Programação paralela** (Fork/Join, Parallel Streams)
- [ ] **Programação distribuída** (RMI, sockets, bibliotecas externas)
- [ ] **Modularização** (JPMS - Java Platform Module System)
