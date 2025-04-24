#Config Dockerfile
# Etapa de construcción
FROM gradle:8.5-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle build -x test

# Etapa de ejecución
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

# Crear directorio para configuración
RUN mkdir -p /configuration

# Copiar archivos de configuración base
COPY src/main/resources/configuration/*.yml /configuration/

# Copiar archivos de configuración específicos para Docker
# Estos sobrescribirán los archivos base con el mismo nombre
COPY src/main/resources/configuration-docker/*.yml /configuration/

# Variables de entorno para el Config Server
ENV SPRING_PROFILES_ACTIVE=docker
ENV SPRING_CLOUD_CONFIG_SERVER_NATIVE_SEARCH_LOCATIONS=file:/configuration

EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]
