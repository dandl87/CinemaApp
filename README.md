# Cinema 🎬

Cinema è una esercitazione di progetto in Spring Boot.
Lo scopo è  gestire la programmazione di un cinema multiplex composto da 12 sale: 10 sale normali e 2 sale IMAX.
La piattaforma backend deve rendere possibile l'inserimento di nuovi film tramite estrazione da file excel in formato xlsx e in base a quelli già in proiezione nelle sale sostituirli secondo una logica che premia il valore intrinseco dei film ( definito in modo arbitrario).

## Funzionalità principali

- Programmazione settimanale in base ad un valore intrinseco al film ed al numero di settimane che il film è stato in sala
- Ogni film programmato in una sala rimarrà in quella sala o verrà rimosso ma non cambierà sala dopo una esecuzione dell'algoritmo di scheduling
- Inserimento nuovi film tramite file excel xlsx e form
- ricerca settimana di proezioni
- API REST per interfacciamento con frontend o app mobile
- lista di tutti i film appartenenti al cinema
- lista dei file excel caricati
- Al click del pulsante sunday viene simulato l'inizio di una nuova settimana di programmazione

## Run

mvn clean package -DskipTests

docker-compose up --build

the application listen on port 8081

