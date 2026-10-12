# Compiladores 2026.2 — Exemplos de Java

Repositório da disciplina **Compiladores** (semestre 2026.2) para a atividade de
"exemplos na linguagem de origem".

## Sobre o projeto

Projeto de um transpilador cuja **linguagem de origem é Java**. Este repositório
disponibiliza os códigos-fonte de exemplo que documentam as características da
linguagem, organizados por tópico.

## Equipe

- Davi Gledson
- João Heitor
- Bruno Eduardo
- Gabriel Oliveira

## Como compilar e executar

**Requisito:** JDK 21 (compilador `javac`). Verifique a instalação com
`java -version` e `javac -version`.

### Compilar todos os exemplos

```bash
javac -d bin exemplos-origem/*.java
```

Os arquivos `*.class` gerados ficam na pasta `bin/`.

### Executar um exemplo

```bash
java -cp bin NomeDoExemplo
```

Exemplo:

```bash
java -cp bin Exemplo01_TiposDeDados
```

Cada exemplo imprime os resultados dos tópicos com `System.out.println`.

### Organização

- `exemplos-origem/` — códigos-fonte de exemplo (um arquivo por tópico);
- `bin/` — classes compiladas.
