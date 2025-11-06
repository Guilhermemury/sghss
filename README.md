# SGHSS - Sistema de Gestão Hospitalar e de Serviços de Saúde

## Sobre o Projeto

O SGHSS é um sistema de back-end desenvolvido em Java com Spring Boot para gerenciar hospitais, clínicas, laboratórios e equipes de home care da instituição VidaPlus. O sistema centraliza o gerenciamento de pacientes, profissionais de saúde, consultas e prontuários eletrônicos.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **Spring Security**
- **JWT (JSON Web Token)**
- **PostgreSQL**
- **H2 Database** (para testes)
- **SpringDoc OpenAPI** (Swagger)
- **Maven**
- **Lombok**
- **JUnit 5**
- **Mockito**

## Funcionalidades Principais

### Autenticação e Segurança
- Autenticação baseada em JWT
- Controle de acesso por perfis (RBAC): PACIENTE, PROFISSIONAL, ADMIN
- Criptografia de senhas com BCrypt
- Conformidade com LGPD

### Gestão de Pacientes
- Cadastro de pacientes
- Listagem de pacientes
- Busca de pacientes por ID ou CPF
- Prontuário eletrônico

### Gestão de Consultas
- Agendamento de consultas
- Validação de disponibilidade de horários
- Cancelamento de consultas
- Listagem de consultas por paciente ou profissional

### Gestão de Profissionais
- Cadastro de profissionais de saúde
- Especialidades médicas
- Gerenciamento de agendas

## Pré-requisitos

- Java 17 ou superior
- Maven 3.8 ou superior
- PostgreSQL 14 ou superior

## Configuração do Banco de Dados

1. Crie um banco de dados PostgreSQL:

```sql
CREATE DATABASE sghss_db;
```

2. Configure as credenciais no arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/sghss_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

## Como Executar

1. Clone o repositório:

```bash
git clone https://github.com/guilhermemury/sghss
cd sghss-backend
```

2. Compile o projeto:

```bash
mvn clean install
```

3. Execute a aplicação:

```bash
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

## Documentação da API

A documentação interativa da API está disponível através do Swagger UI:

```
http://localhost:8080/swagger-ui.html
```

## Endpoints Principais

### Autenticação

- `POST /api/v1/auth/login` - Autenticação de usuário

### Pacientes

- `POST /api/v1/pacientes` - Cadastrar paciente (ADMIN)
- `GET /api/v1/pacientes` - Listar pacientes (ADMIN, PROFISSIONAL)
- `GET /api/v1/pacientes/{id}` - Buscar paciente por ID

### Consultas

- `POST /api/v1/consultas` - Agendar consulta (PACIENTE, ADMIN)
- `GET /api/v1/consultas/paciente/{id}` - Listar consultas do paciente
- `GET /api/v1/consultas/profissional/{id}` - Listar consultas do profissional
- `PATCH /api/v1/consultas/{id}/cancelar` - Cancelar consulta

## Testes

Execute os testes com:

```bash
mvn test
```

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/vidaplus/sghss/
│   │   ├── config/          # Configurações (Security, OpenAPI)
│   │   ├── controller/      # Controllers REST
│   │   ├── dto/             # Data Transfer Objects
│   │   ├── entity/          # Entidades JPA
│   │   ├── enums/           # Enumerações
│   │   ├── exception/       # Exceções customizadas
│   │   ├── repository/      # Repositórios JPA
│   │   ├── security/        # Segurança e JWT
│   │   └── service/         # Serviços de negócio
│   └── resources/
│       ├── application.properties
│       └── logback.xml
└── test/
    └── java/com/vidaplus/sghss/
        └── service/         # Testes unitários
```

## Segurança

- Todas as senhas são criptografadas usando BCrypt
- Autenticação stateless com JWT
- Controle de acesso baseado em perfis
- Logs de auditoria para operações críticas
- Proteção contra vulnerabilidades OWASP

## Logs

Os logs são armazenados em:
- `logs/sghss.log` - Logs da aplicação
- `logs/audit.log` - Logs de auditoria

## Contribuindo

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

## Licença

Este projeto está sob a licença Apache 2.0.

## Autor

**Guilherme Mury**  
RU: 4551362

Desenvolvido como parte da disciplina de Projetos Multidisciplinares - 2025/A1

## Contato

Para dúvidas ou sugestões, entre em contato através do canal de tutoria da disciplina.
