# API Financeira Inteligente com Spring Boot e Spring AI

Projeto desenvolvido como atividade prática do bootcamp DIO/Itaú, utilizando o módulo Spring AI do repositório didático de Thiago Poiani.

## Objetivo

Explorar a integração entre uma API financeira, persistência de dados e recursos de inteligência artificial, incluindo reconhecimento de fala, Tool Calling e síntese de voz.

## Tecnologias

- Java 25
- Spring Boot 4.0.5
- Spring AI 2.0.0-M4
- Gradle 9.4.1
- Spring Data JPA
- MySQL 9.6
- Docker Compose
- JUnit 5

## Funcionalidades do projeto-base

- Cadastro de transações financeiras.
- Consulta de transações por categoria.
- Persistência no banco de dados MySQL.
- Transcrição de áudio com OpenAI.
- Interpretação de comandos por IA com Tool Calling.
- Geração de respostas em áudio.

## Endpoints

| Método | Endpoint | Descrição |
| --- | --- | --- |
| POST | /transactions | Cadastra uma transação |
| GET | /transactions/{category} | Consulta transações por categoria |
| POST | /transactions/ai | Processa áudio com recursos de IA |

Categorias disponíveis: GROCERIES, PHARMA e AUTO.

## Como executar

Pré-requisitos: Java 25, Docker e Docker Compose.

O projeto inclui Gradle Wrapper, dispensando uma instalação global do Gradle.

Inicie o MySQL:

    docker compose up -d database

Configure as credenciais do banco conforme o arquivo compose.yml.

No Windows PowerShell, utilizando a configuração local do laboratório:

    $env:JAVA_HOME = "$HOME\JDKs\jdk-25.0.5.1+2"
    $env:SPRING_DATASOURCE_URL = "jdbc:mysql://localhost:3307/transaction"
    $env:SPRING_DATASOURCE_USERNAME = "app"
    $env:SPRING_DATASOURCE_PASSWORD = "app"

Para executar a aplicação, é necessário configurar OPENAI_API_KEY no ambiente.

    .\gradlew.bat bootRun

A API REST utiliza a porta 8080.

As credenciais do compose.yml são exclusivamente para desenvolvimento local.

## Validações realizadas

Durante a atividade foram realizadas:

- Compilação do código principal com sucesso.
- Compilação do código de testes com sucesso.
- Inicialização do Spring Boot com MySQL no Docker.
- Cadastro de transação via POST /transactions.
- Consulta da transação gravada via GET /transactions/GROCERIES.
- Validação dos filtros PHARMA e AUTO.
- Execução bem-sucedida do teste automatizado BudgetingApplicationTests.contextLoads().

## Limitações da validação

A integração real com OpenAI não foi executada por exigir credenciais de API e poder gerar custos.

As funcionalidades de transcrição, Tool Calling e geração de áudio estão presentes no projeto-base, mas não foram validadas de ponta a ponta nesta execução.

O campo amount foi testado com um valor numérico, sem validar uma regra específica de conversão monetária.

## Origem e créditos

Código-base: Thiago Poiani, DIO Spring Boot Learning Track.

https://github.com/thpoiani/dio-spring-boot-learning-track/tree/main/05-spring-ai

Esta entrega apresenta a reprodução e a validação local do módulo didático, com documentação das verificações realizadas e das limitações encontradas.
## Validacao complementar de reconhecimento de fala

Em 21/11/2026, foi realizado um teste real de transcricao com a API Groq, utilizando o modelo whisper-large-v3-turbo e o idioma portugues.

- Arquivo: recording-1.m4a
- Resultado: "Passei na farmácia rapidinho e deixei 80 reais em 3 itens."
- Status: transcricao concluida com sucesso.
- Custo: realizado utilizando o plano gratuito da Groq.

Este teste foi realizado diretamente na API Groq, sem integracao com o endpoint Spring Boot.

Permanecem sem validacao de ponta a ponta o Tool Calling e a geracao de audio pelo endpoint /transactions/ai.