# =========================
# STAGE 1 - BUILD
# =========================
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copiamo solo i file necessari per scaricare le dipendenze
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Cache delle dipendenze
RUN ./mvnw dependency:go-offline

# Copiamo il codice sorgente
COPY src src

# Build del jar (per ora skip test)
RUN ./mvnw clean package -DskipTests


# =========================
# STAGE 2 - RUNTIME
# =========================
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copiamo il jar costruito nello stage precedente
COPY --from=build /app/target/*.jar app.jar

# Railway usa una porta dinamica
ENV PORT=8080
EXPOSE 8080

# Avvio applicazione
ENTRYPOINT ["java", "-jar", "app.jar"]
