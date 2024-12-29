pipeline {
    agent any

    environment {
        REGISTRY = 'docker.io'  // Remplacer par le registre utilisé
        DOCKER_IMAGE = 'mon-app'
        DOCKER_CREDENTIALS_ID = 'docker-credentials'  // ID des credentials Docker dans Jenkins
        PROD_COMPOSE_FILE = 'docker-compose.prod.yml'
        DOCKER_TAG = "${env.BUILD_NUMBER}"  // Utiliser le numéro de build comme tag pour chaque version
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/votre-repository.git'  // URL de votre repository Git
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Construire l'image Docker avec un tag unique pour cette version
                    sh "docker build -t ${REGISTRY}/${DOCKER_IMAGE}:${DOCKER_TAG} ."
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    // Se connecter au registre Docker et pousser l'image
                    withDockerRegistry([ credentialsId: "${DOCKER_CREDENTIALS_ID}", url: "" ]) {
                        sh "docker push ${REGISTRY}/${DOCKER_IMAGE}:${DOCKER_TAG}"
                    }
                }
            }
        }

        stage('Deploy to Production') {
            steps {
                script {
                    // Mettre à jour et relancer les services en production avec Docker Compose
                    sh "docker-compose -f ${PROD_COMPOSE_FILE} down"
                    sh "docker-compose -f ${PROD_COMPOSE_FILE} pull"
                    sh "docker-compose -f ${PROD_COMPOSE_FILE} up -d"
                }
            }
        }
    }

    post {
        always {
            // Nettoyage des ressources Docker pour éviter de consommer trop d'espace
            sh "docker system prune -f"
        }

        success {
            echo 'Déploiement en production réussi!'
        }

        failure {
            echo 'Le pipeline a échoué lors du déploiement en production.'
        }
    }
}
