#Gateway Dockerfile
# Etapa de construcción
FROM gradle:8.5-jdk21 AS build
WORKDIR /app

# Copiar todo el proyecto y compilar
COPY . .
RUN gradle build --no-daemon

# Etapa de ejecución
FROM eclipse-temurin:21-jre
WORKDIR /app

# Variables de entorno para Spring Boot
ENV SPRING_PROFILES_ACTIVE=docker

# Copiar el JAR desde la etapa de construcción
COPY --from=build /app/build/libs/*.jar app.jar

# Copiar archivos de configuración
COPY --from=build /app/src/main/resources/bootstrap.yml /app/
COPY --from=build /app/src/main/resources/application.yml /app/
COPY --from=build /app/src/main/resources/application-docker.yml /app/

# Puerto expuesto (8080 para Gateway)
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
