# Usando uma imagem leve com JDK 17
FROM amazoncorretto:17-alpine-jdk

# Porta exposta pela aplicação
EXPOSE 8080

# Diretório de trabalho para o contêiner
WORKDIR /app

# Criação de um usuário não root para maior segurança
RUN adduser -D -h /app appuser && chown -R appuser /app

# Copiando o JAR gerado pela build
COPY ["build/libs/*[^plain].jar", "app.jar"]

# Definindo o usuário não root
USER appuser

# Comando de inicialização da aplicação
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
