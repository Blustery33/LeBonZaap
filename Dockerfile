# Étape 1 : Utiliser une image Maven pour construire le projet
FROM maven:3.8.4-eclipse-temurin-17 AS build

# Copier les fichiers du projet dans le conteneur
COPY . /app

# Définir le répertoire de travail
WORKDIR /app

# Exécuter Maven pour construire le projet et générer le fichier JAR
CMD ["mvn", "spring-boot:run"]
# Étape 2 : Utiliser l'image JDK légère pour exécuter l'application
FROM eclipse-temurin:21

# Définir un argument pour spécifier le chemin du JAR généré
ARG JAR_FILE=target/*.jar

# Copier le fichier JAR généré depuis l'étape de build
COPY --from=build /app/${JAR_FILE} /app.jar

# Exposer le port 8081 (celui de votre application)
EXPOSE 8081

# Commande d'exécution de l'application Spring Boot
ENTRYPOINT ["java", "-jar", "/app.jar"]
