FROM amazoncorretto:17-alpine-jdk

EXPOSE 8080

WORKDIR /app

RUN adduser -D -h /app appuser && chown -R appuser /app

COPY ["build/libs/*[^plain].jar", "app.jar"]

USER appuser

ENV SPRING_PROFILES_ACTIVE=prd

CMD ["java", "-jar", "app.jar"]
