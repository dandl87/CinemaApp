# Scegli l'immagine di base Java 21
FROM openjdk:21

# Imposta la directory di lavoro all'interno del container
WORKDIR /app

# creo una variabile con l'applicativo inpacchettato come jar presente nella cartella target
ARG JAR_FILE=target/CinemaApp-0.0.1-SNAPSHOT.jar

# Copia l'applicazione Java nel container
COPY ${JAR_FILE} app.jar

# Imposta il punto di ingresso per l'applicazione
ENTRYPOINT ["java", "-jar", "app.jar"]