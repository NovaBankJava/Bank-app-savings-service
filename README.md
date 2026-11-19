# NovaBank – Serviço de Cofrinhos

Microsserviço responsável pelos **cofrinhos** (savings) da aplicação NovaBank: o usuário pode criar um ou mais cofrinhos com um nome e uma meta de valor, consultá-los, editá-los e excluí-los.

Desenvolvido com **Java 21** e **Spring Boot**, seguindo a **Arquitetura Hexagonal (Ports & Adapters)**.

---

## Funcionalidades

| Requisito | Descrição | Status |
| --- | --- | --- |
| RF01 | Criar múltiplos cofrinhos por usuário | ✅ |
| RF02 | Armazenar nome (campo `description`), meta de valor e data de criação | ✅ |
| RF03 | Editar nome e meta após a criação | ✅ |
| RF04 | Excluir um cofrinho – bloqueado enquanto o saldo for maior que zero (retorna 422) | ✅ |

### Regras de negócio

- Todo cofrinho é criado com **saldo = 0**.
- `userId` e `description` são obrigatórios; `targetAmount` deve ser **maior que zero**.
- Um cofrinho **com saldo maior que zero não pode ser excluído** (retorna `422`).
- As regras ficam no modelo de domínio (`Savings`), então valem independentemente da porta de entrada usada.

---

## Tecnologias

- Java 21
- Spring Boot (Web MVC, Data JPA)
- MapStruct (mapeamento domínio ↔ persistência) + Lombok
- H2 (banco em memória)
- JUnit 5 + Mockito
- Maven

---

## Arquitetura

O projeto segue a **Arquitetura Hexagonal**: o núcleo de negócio (`savings`) não depende de frameworks nem de banco de dados. Todas as dependências apontam para dentro.

```
org.example.bankappsavingsservice
├── savings/                         # Núcleo de negócio (sem Spring/JPA)
│   ├── domain/
│   │   ├── model/Savings            # Entidade com as regras de negócio
│   │   ├── exception/               # SavingsNotFound, SavingsWithBalance
│   │   └── ports/
│   │       ├── in/                  # Casos de uso oferecidos (Create, Get, Update, Delete)
│   │       └── out/                 # O que o núcleo precisa (SavingsRepositoryPort)
│   └── application/usecases/
│       └── SavingsService           # Implementa os casos de uso
│
└── infra/                           # Mundo externo (HTTP, banco de dados)
    ├── adapter/in/                  # Controller REST, DTOs, tratamento global de erros
    ├── adapter/out/                 # Adapter do repositório + mapper MapStruct
    └── repository/                  # Repositório e entidade do Spring Data JPA
```

**Fluxo de uma requisição (criação):**
`SavingsController` → `CreateSavingsUseCase` → `SavingsService` → `Savings.create()` → `SavingsRepositoryPort` → `SavingsAdapter` → `SavingsOutMapper` → `SavingsRepository`

**Por que portas?** O serviço depende de interfaces, não do JPA. Isso mantém o domínio limpo, permite trocar a camada de persistência e torna o serviço testável com mocks.

---

## API

Caminho base: `/savings`

| Método | Endpoint | Descrição | Sucesso |
| --- | --- | --- | --- |
| `POST` | `/savings` | Cria um cofrinho | `201 Created` |
| `GET` | `/savings/user/{userId}` | Lista os cofrinhos de um usuário | `200 OK` |
| `PUT` | `/savings/{savingsId}` | Atualiza nome e meta | `200 OK` |
| `DELETE` | `/savings/{savingsId}` | Exclui um cofrinho (somente com saldo zero) | `204 No Content` |

### Criar um cofrinho

**Requisição**
```json
POST /savings
{
  "userId": "user-123",
  "description": "Viagem",
  "targetAmount": 200
}
```

**Resposta – 201 Created**
```json
{
  "id": "d1263ccb-4b67-44de-98e1-f6cd179c3e9b",
  "userId": "user-123",
  "description": "Viagem",
  "targetAmount": 200,
  "balance": 0,
  "createdAt": "2026-11-17T21:22:13.4410798"
}
```

### Atualizar um cofrinho

```json
PUT /savings/{savingsId}
{
  "description": "Viagem para o Japão",
  "targetAmount": 5000
}
```

### Tratamento de erros

Os erros são tratados de forma global (`GlobalExceptionHandler`) e seguem um formato padrão:

```json
{
  "status": 422,
  "message": "Savings with balance cannot be deleted: d1263ccb-...",
  "timestamp": "2026-11-17T21:30:00"
}
```

| Status | Quando |
| --- | --- |
| `400 Bad Request` | Dados inválidos (descrição vazia, meta ≤ 0, userId vazio) |
| `404 Not Found` | Cofrinho não encontrado |
| `422 Unprocessable Entity` | Regra de negócio violada (excluir cofrinho com saldo) |

---

## Como executar

**Pré-requisito:** Java 21

```bash
./mvnw spring-boot:run        # Windows: mvnw.cmd spring-boot:run
```

A API sobe em `http://localhost:8080`. Por padrão, usa um **banco H2 em memória**, então os dados são perdidos quando a aplicação para.

> Se o mapper do MapStruct for gerado vazio (por exemplo, após compilar só pela IDE), rode `./mvnw clean compile` para gerá-lo novamente com o Lombok processado primeiro.

---

## Testes

```bash
./mvnw test
```

Os testes unitários do `SavingsService` usam **JUnit 5 + Mockito**, com a porta do repositório mockada. Cenários de exclusão cobertos:

- cofrinho não existe → `SavingsNotFoundException`
- cofrinho com saldo → `SavingsWithBalanceException`, nada é excluído
- cofrinho sem saldo → é excluído

---

## Próximos passos

- Operações de depósito e resgate (`deposit()` / `withdraw()` no domínio)
- Remover os setters públicos do modelo de domínio para reforçar o encapsulamento
- Validação das requisições com Bean Validation (`@Valid`)
- Testes de integração cobrindo o mapeamento de persistência
- Perfil para PostgreSQL e configuração com Docker

---

## Autor

**Rafael Kimura** – [GitHub](https://github.com/KimuraRafael) · [LinkedIn](https://www.linkedin.com/in/rafael-kimura-12608b218)
