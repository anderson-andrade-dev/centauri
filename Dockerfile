FROM ubuntu:latest

# Instalação do Java
RUN apt-get update && apt-get install -y openjdk-21-jdk

# Instalação do MySQL
RUN apt-get update && apt-get install -y mysql-server

# Script de espera para o MySQL
COPY wait-for-mysql.sh /wait-for-mysql.sh
RUN chmod +x /wait-for-mysql.sh

# Configuração do MySQL
RUN service mysql start && \
    mysql -e "CREATE DATABASE centauri;" && \
    mysql -e "CREATE USER 'user'@'localhost' IDENTIFIED BY 'A19a17g27n06k18!!';" && \
    mysql -e "GRANT ALL PRIVILEGES ON centauri.* TO 'user'@'localhost';" && \
    mysql -e "FLUSH PRIVILEGES;"

# Definição das variáveis de ambiente
ENV MYSQL_DATABASE=centauri
ENV MYSQL_USER=user
ENV MYSQL_PASSWORD=A19a17g27n06k18!!
ENV MYSQL_ROOT_PASSWORD=root

# Copia o arquivo JAR para o contêiner
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Exponha as portas 8080 e 3306
EXPOSE 8080 3306

# Inicia o MySQL e a aplicação Java
CMD service mysql start && /wait-for-mysql.sh && java -jar /app.jar

LABEL authors="Anderson Andrade Dev"