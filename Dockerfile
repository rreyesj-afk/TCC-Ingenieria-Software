# Dockerfile para la aplicación Spring Boot
FROM eclipse-temurin:21-jdk

# Información del mantenedor
LABEL maintainer="Equipo de Desarrollo"
LABEL description="Sistema de Reservas de Restaurante - Spring Boot Application"

# Instalar curl para health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Directorio de trabajo
WORKDIR /app

# Copiar el archivo JAR de la aplicación
# Nota: El JAR se genera con: mvn clean package
COPY target/reservas-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto de la aplicación
EXPOSE 9091

# Variables de entorno para la aplicación
ENV SPRING_PROFILES_ACTIVE=docker
ENV SERVER_PORT=9091

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:9091/api/clientes || exit 1

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]

