# Checkpoint 4 — Bug Hunt StreamFIAP

## Identificação

**Grupo:** Grupo 7

| Integrante | RM      | Turma  |
|------------|---------|--------|
| Miguel     | 563491  | 2CCPG  |
| Joao Vitor | 566541  | 2CCPG  |
| Henry      | 565309  | 2CCPG  |
| Samuel     | 564435  | 2CCPG  |

| Campo | |
|---|---|
| **Total de bugs corrigidos** | 4 / 12 |
| **Total de ajustes de Clean Code** | 1 / 6 |

---

## Parte 1 — Bugs encontrados

> Uma linha por bug, na ordem em que você os encontrou. Use a numeração dos seus
> commits (`fix: bug01 ...`). Preencha TODAS as colunas — metade da nota está aqui.

| # | Sintoma observado (o que fiz/vi) | Causa raiz (arquivo e linha aproximada) | Correção aplicada | Conceito da disciplina |
|---|---|---|---|---|
| bug01 | Documentario e Serie sendo alugados por R$ 9,90 em vez do valor correto (Grátis e R$4,90/temp) | Classes Documentario.java e Serie.java não sobrescreviam o método de cálculo de preço da classe mãe | Implementei o @Override do método calcularPrecoAluguel() nas duas classes, retornando 0.0 para Documentário e 4.90 * numeroTemporadas para Série. | Herança, Polimorfismo e Sobrescrita de métodos |
| bug02 | O preço promocional do Filme ficava mais caro que o normal | Filme.java (método aplicarPromocao): o cálculo multiplicava por 1.2 em vez de subtrair o desconto | Alterei o multiplicador de 1.2 para 0.8 | Operadores aritméticos |
| bug03 | Ao cadastrar uma Série, os dados base (título, categoria, etc.) não eram salvos | Serie.java (Construtor): Os parâmetros eram recebidos, mas ignorados e não eram repassados à classe mãe | Adicionei a chamada super() no construtor repassando os atributos para a superclasse Conteudo | Construtores, Herança e uso do super() |
| bug04 | Usuário não salva no banco (erro de ID nulo) | Usuario.java: Faltava a anotação para gerar o ID automaticamente | Adicionado @GeneratedValue(strategy = GenerationType.IDENTITY) no atributo id | JPA e Mapeamento de Entidades |
| bug05 | | | | |
| bug06 | | | | |
| bug07 | | | | |
| bug08 | | | | |
| bug09 | | | | |
| bug10 | | | | |
| bug11 | | | | |
| bug12 | | | | |

## Parte 2 — Ajustes de Clean Code

| # | Onde estava | Qual princípio/boas práticas era violado | O que eu mudei |
|---|---|---|---|
| clean01 | Classe Conteudo (método calcularPrecoAluguel()) | Violação de Abstração e Polimorfismo | Transformei o método calcularPrecoAluguel() em abstract e removi seu corpo |
| clean02 | | | |
| clean03 | | | |
| clean04 | | | |
| clean05 | | | |
| clean06 | | | |

---

## Parte 3 — Perguntas de reflexão

> Responda com suas palavras, 5 a 10 linhas cada, **usando o código real do projeto
> como exemplo**. Respostas genéricas de tutorial não pontuam.

### 1. Injeção de dependência (Aula 13)
Os controllers recebem os repositories via `@Autowired` (ex.: `ConteudoController`
usa `ConteudoRepository`). Explique por que o Spring precisa gerenciar esses objetos
em vez de criarmos com `new ConteudoRepository()`. O que exatamente o Spring faz ao
injetar um bean, e por que isso não funcionaria com um `new` comum?

### 2. JDBC vs Spring Data JPA (Aulas 12 e 13)
Na Aula 12 escrevemos um `ProdutoDAO` na mão com `Connection`, `PreparedStatement` e
`ResultSet`. Aqui o `ConteudoRepository` tem 2 linhas e faz CRUD completo. Compare as
duas abordagens: o que o Spring Data JPA automatiza, o que o JDBC/DAO ainda resolve
melhor, e como o `findByCategoria` consegue funcionar sem implementação.

### 3. Exceções checked vs unchecked (Aula 11)
A `ClassificacaoIndicativaException` estourava como um erro genérico do servidor,
sem mensagem útil para o cliente. Explique a diferença entre `extends Exception` e
`extends RuntimeException` no contexto desse bug, e como você fez a mensagem da
regra (classificação indicativa) chegar de forma clara ao cliente da API.

### 4. Sobrescrita vs sobrecarga (Aula 7)
Um dos bugs compilava sem nenhum erro: o método da `Serie` parecia sobrescrever
`calcularPrecoAluguel`, mas na verdade sobrecarregava. Explique a diferença entre
override e overload nesse caso e por que a anotação `@Override` teria impedido o bug.

### 5. Onde blindar o objeto? (Aulas 3, 4 e 13)
Vimos bugs de dados inválidos aceitos (duração negativa, créditos negativos, campos
nulos). Em quais lugares (construtor, setter, método do model) cada tipo de validação
deve ficar? Justifique usando os bugs que você encontrou e explique por que validar só
em um lugar não foi suficiente.

### 6. Abstração e interface (Aulas 8 e 9)
`Conteudo` é abstrata e `Promocionavel` é uma interface. Explique a diferença de
propósito entre as duas nesse projeto e o que mudaria no código se o Documentário
passasse a ter promoções — quais classes/linhas seriam tocadas e quais ficariam
intactas? O que isso diz sobre o design do sistema?

---

## Parte 4 — Espaço livre (opcional)

Alguma dificuldade, dúvida ou comentário sobre o checkpoint?

```

```
