#Eureka Dockerfile
# Etapa de construcción
FROM gradle:8.5-jdk21 AS build
WORKDIR /app

# Copiar todo el proyecto
COPY . .

# Compilar usando Gradle directamente (no el wrapper)
RUN gradle build --no-daemon

# Etapa de ejecución (igual que antes)
FROM eclipse-temurin:21-jre
WORKDIR /app
ENV SPRING_PROFILES_ACTIVE=docker
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]
