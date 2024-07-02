FROM openjdk:21-jdk-slim
ARG JAR_FILE=infrastructure/target/*.jar
# Copie du fichier jar dans le répertoire de travail
COPY ${JAR_FILE} /app/app.jar
# Création d'un répertoire de travail pour l'application
WORKDIR /app
# Définition du point d'entrée pour démarrer l'application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
