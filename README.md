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
| **Total de bugs corrigidos** | 12 / 12 |
| **Total de ajustes de Clean Code** | 6 / 6 |

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
| bug05 | Ao cadastrar o usuário, o nome ficava null no banco | Usuario.java: No construtor, estava nome = nome; omitindo o this | Alterado para this.nome = nome; | Construtores e uso do this |
| bug06 | O sistema permitia alugar conteúdos mesmo sem saldo, deixando créditos negativos | Usuario.java: A validação estava preco >= this.creditos (lógica invertida) | Corrigido para this.creditos >= preco; | Operadores relacionais e lógica booleana |
| bug07 | O sistema efetivava aluguel de conteúdos com disponivel = false | Usuario.java (método alugar): Não havia validação de disponibilidade | Adicionado um if(!c.isDisponivel()) estourando a exceção devida | Regras de negócio e lançamento de Exceções |
| bug08 | A API aceitava cadastrar conteúdo com duração menor ou igual a zero | Conteudo.java: Falta de validação no construtor e no setter | Adicionado if (duracaoMinutos <= 0) lançando exceção | Blindagem de objetos / Validação de estado |
| bug09 | Erro de idade retornava status 500 (Internal Server Error) genérico | ClassificacaoIndicativaException.java: Estava herdando de Exception (Checked), bloqueando o Handler do Spring | Alterado para estender RuntimeException | Exceções |
| bug10 | Buscar ID inexistente (ex: 999) devolvia status 200 vazio em vez de erro | ConteudoController.java: Havia um bloco try/catch vazio que engolia a exceção | Removido o try/catch para que a exceção suba ao GlobalExceptionHandler | Tratamento de Exceções em Controllers |
| bug11 | Busca por categoria não filtrava corretamente e trazia todos os resultados | ConteudoController.java: O método listarPorCategoria usava .findAll() e comparava Strings incorretamente com == | Alterado para usar conteudoRepository.findByCategoria(categoria), delegando o filtro para o banco | Spring Data JPA |
| bug12 | O aluguel dava sucesso na resposta, mas não atualizava os créditos no banco | AluguelController.java: A alteração era feita na memória, mas não chamava o save() | Adicionados usuarioRepository.save(u) e conteudoRepository.save(c) | Persistência e ciclo de vida de Entidades JPA |

## Parte 2 — Ajustes de Clean Code

| # | Onde estava | Qual princípio/boas práticas era violado | O que eu mudei |
|---|---|---|---|
| clean01 | Classe Conteudo (método calcularPrecoAluguel()) | Violação de Abstração e Polimorfismo | Transformei o método calcularPrecoAluguel() em abstract e removi seu corpo |
| clean02 | Conteudo.java (public int duracaoMinutos) | Encapsulamento. Atributos não devem ficar expostos | Alterado o modificador de acesso de public para private |
| clean03 | Usuario.java (método alugar) | Nomes sem significado (Clean Names). Variáveis p e c não explicavam o contexto | Renomeei c para conteudo e p para preco |
| clean04 | Usuario.java (método alugar) | Responsabilidade Única (SRP). Classes de Model não devem fazer I/O (imprimir no console) | Apaguei todo o bloco de System.out.println |
| clean05 | AluguelController.java (método alugar) | Uso inseguro de classe Optional. O uso direto de .get() pode causar um NoSuchElementException e quebrar a aplicação caso o registro não exista no banco | Substituí o .get() por .orElseThrow(), garantindo que uma exceção controlada seja lançada |
| clean06 | Conteudo.java (método calcularPrecoPromocional) | Princípio Aberto/Fechado (OCP - SOLID). A superclasse usava instanceof para descobrir o comportamento das subclasses, criando alto acoplamento | Removi o instanceof da classe mãe, deixando-a retornar apenas o preço padrão. Usei polimorfismo, adicionando @Override nas classes filhas (Filme e Serie) para que elas mesmas apliquem o método aplicarPromocao() |

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

## Respostas das Perguntas:
### Pergunta 1:
O Spring usa o @Autowired para criar e gerenciar os objetos pra gente. Se a gente usasse new ConteudoRepository(), ia dar erro na hora, porque o repositório é uma interface, e o Java não deixa dar new em interfaces. Ao injetar o bean, o Spring cria a classe concreta por trás dos panos, conecta com o banco de dados e já entrega o objeto pronto pro Controller usar, sem a gente ter que configurar essa conexão na mão toda vez

### Pergunta 2:
O Spring Data JPA automatiza o código grande e repetitivo, como abrir conexão e escrever comandos SQL na mão. O JDBC dá mais trabalho por ser manual, mas ele ainda é melhor quando precisamos fazer consultas no banco que são muito complexas e precisam de performance específica. Já o método findByCategoria funciona sozinho no JPA por causa das "Derived Queries": o Spring lê o nome do método e, por ele seguir um padrão, já monta um SELECT filtrando pela coluna categoria automaticamente

### Pergunta 3:
A classe Exception é "checked", o que obriga o código a ter um try/catch. Isso atrapalhava o Spring, fazendo ele travar e devolver um erro 500 genérico. Quando mudamos para RuntimeException (unchecked), o código não exige mais o try/catch. Assim, a exceção fica livre para subir direto para o arquivo GlobalExceptionHandler. É lá que esse handler pega a mensagem da regra de classificação indicativa e devolve a resposta formatada corretamente pro usuário da API

### Pergunta 4:
A sobrescrita mantém os mesmos parâmetros do método da classe mãe para apenas mudar o que ele faz. A sobrecarga muda os parâmetros, criando um método diferente na classe. O bug na Serie aconteceu porque os parâmetros estavam diferentes, gerando uma sobrecarga sem querer, com isso, o Java rodou o cálculo da classe mãe em vez do da filha. Se tivessem usado a anotação @Override, o código nem ia compilar, pois o Java ia avisar na hora que os parâmetros não batiam

### Pergunta 5:
Nós blindamos no construtor para impedir que o objeto seja criado com dados inválidos. Blindamos no setter para garantir que ninguém coloque um valor errado no atributo depois que o objeto já foi instanciado. E as validações de regras de negócio, como ver se o saldo é negativo, ficam dentro dos métodos específicos, como o alugar(). Fazer isso só no construtor não resolve, porque o setter ficaria aberto para receber dados inválidos logo em seguida

### Pergunta 6:
A classe abstrata Conteudo serve para agrupar atributos em comum (como título e duração) para as classes filhas herdarem e não repetirem código. A interface Promocionavel serve para definir um contrato obrigatório de comportamento, ou seja, quem assina tem que implementar o método do desconto. Se o Documentario ganhasse promoção, bastaria colocar um implements Promocionavel nele e escrever a regra do desconto. O resto do código (mãe e as outras filhas) ficaria totalmente intacto, mostrando que o código é bem estruturado

---
