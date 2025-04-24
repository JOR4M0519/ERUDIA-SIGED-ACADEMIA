#Academy Dockerfile
# Etapa de construcción
FROM gradle:8.5-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle build -x test -x compileTestJava

# Etapa de ejecución
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

# Copiar archivos de configuración
COPY --from=build /app/src/main/resources/bootstrap.yml /app/
COPY --from=build /app/src/main/resources/application.yml /app/
COPY --from=build /app/src/main/resources/application-docker.yml /app/

# Variables de entorno para la base de datos en la nube (fallback)
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://34.72.214.106:5432/academy
ENV SPRING_DATASOURCE_USERNAME=apiclient-erudia
ENV SPRING_DATASOURCE_PASSWORD=postgres
ENV SPRING_JPA_HIBERNATE_DDL_AUTO=update
ENV SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT=org.hibernate.dialect.PostgreSQLDialect
ENV SPRING_PROFILES_ACTIVE=docker


EXPOSE 0
ENTRYPOINT ["java", "-jar", "app.jar"]
