---

# 🚀 Centauri - Plataforma de Publicação de Artigos e Mensagens com IA  

![Imgur](https://i.imgur.com/C1g4eJY.jpg)

---

## 🛠️ Tecnologias Utilizadas  

- **Backend**: [Spring Boot 3.4.0](https://spring.io/projects/spring-boot)  
- **Linguagem**: [Java 23](https://www.oracle.com/java/technologies/javase-downloads.html)  
- **Banco de Dados**: [MySQL](https://www.mysql.com/)  
- **Segurança com Spring Security**: Gerenciamento de autenticação e autorização seguro.  
- **Envio de E-mails com JavaMail**: Facilita o envio de notificações por e-mail no sistema.  

---

## 📖 O Que é o Centauri?  

O **Centauri** é uma aplicação desenvolvida para permitir a **publicação de artigos de tecnologia utilizando IA**, possibilitar **trocas de mensagens entre usuários**, e realizar **envio de notificações por e-mail**.  

A plataforma oferece uma maneira rápida de publicar conteúdos relevantes, interagir com outros entusiastas de tecnologia e manter os usuários informados através de notificações enviadas por e-mail.

---

## ⚙️ Como a Aplicação Funciona  

O **Centauri** é composto por recursos principais:  

1. **Publicação de Artigos com IA**:  
   - Os usuários podem criar artigos sobre temas tecnológicos com sugestões e melhorias geradas por IA.  

2. **Mensagens Instantâneas**:  
   - Um espaço dedicado para interações entre usuários, permitindo trocas de ideias, discussões e colaboração em tempo real.  

3. **Envio de Notificações por E-mail**:  
   - A aplicação utiliza **JavaMail** para enviar e-mails automáticos, como notificações de cadastro, alertas ou atualizações importantes.  

4. **Segurança com Spring Security**:  
   - Gerenciamento de autenticação seguro com Spring Security para proteger dados e recursos importantes do sistema.

---

## 🏁 Pré-requisitos  

Antes de iniciar, você precisará:  

1. **Java 23+ instalado no sistema**  
   - [Java - Download](https://www.oracle.com/java/technologies/javase-downloads.html)  
2. **Banco de dados MySQL configurado e em execução**  
   - Caso não tenha, instale através do [MySQL Docs](https://dev.mysql.com/doc/)  
3. Configurar a conexão com o MySQL no **application.properties**  
4. Configurar as credenciais para envio de e-mails no **application.properties**  

---

## 📂 Configurando o Ambiente  

### 1. Clone o Repositório  
```bash
git clone https://github.com/seu-usuario/centauri.git
cd centauri
```

---

### 2. Configure o Banco de Dados  
1. Crie um banco de dados no MySQL:  

```sql
CREATE DATABASE centauri_db;
```

2. Atualize o `application.properties` com as credenciais:  

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/centauri_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
```

---

### 3. Configure o Envio de E-mails com JavaMail  
1. Configure no `application.properties`:  

```properties
spring.mail.host=smtp.seuprovedor.com
spring.mail.port=587
spring.mail.username=seu_email@provedor.com
spring.mail.password=sua_senha
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

2. Certifique-se de que as credenciais correspondem ao seu servidor SMTP.

---

### 4. Execute o Projeto  

```bash
./mvnw spring-boot:run
```

---

### 5. Acesse a Aplicação  

Após iniciar, você pode acessar a aplicação através do navegador:  
[http://localhost:8080](http://localhost:8080)  

---

## 💬 Como Utilizar  

1. **Cadastre-se e faça login no sistema** para criar seu perfil.  
2. Publique seus próprios artigos de tecnologia utilizando sugestões geradas pela IA.  
3. Participe das conversas com outros usuários através do painel de mensagens.  
4. Receba notificações importantes por e-mail relacionadas ao seu perfil, como atualizações ou alertas.  

---

## 🧰 Estrutura do Projeto  

Aqui está uma visão geral da estrutura do projeto:  

```plaintext
src/main/java/com/seu/centauri
├── controller/         # Lógica para manipulação das requisições HTTP
├── service/            # Camada de lógica de negócios
│   ├── email/          # Lógica para envio de e-mails com JavaMail
│   └── security/       # Lógica de autenticação com Spring Security
├── repository/         # Interfaces para acesso ao banco de dados
├── model/              # Estruturas de dados e entidades
└── exception/          # Tratamento de exceções globais
```

---

## 🔧 Contribuindo  

Se deseja contribuir com o Centauri, sinta-se à vontade para abrir **issues** ou criar pull requests! Toda contribuição é bem-vinda.

---

## 📜 Licença  

Este projeto está licenciado sob a licença [MIT](https://opensource.org/licenses/MIT).  
```

---
