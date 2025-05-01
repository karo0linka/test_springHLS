pipeline {
    agent any

    environment {
        AWS_REGION = 'us-east-1'
        ECR_REPOSITORY = 'springboot-app'
        ECS_CLUSTER = 'springboot-cluster'
        ECS_SERVICE = 'springboot-ecs-alb-ECSService-RtmMX5V66GL6'
        TASK_FAMILY = 'springboot-ecs-alb-TaskDefinition-ONVUIWKtWlSK'
        CONTAINER_NAME = 'springboot-container'
        SONARQUBE_URL = 'https://sonarqube.hlsgroup.com.mx'
    }

    tools {
        jdk 'Temurin-21'
        maven 'maven3'
    }

    stages {
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonarqube-hls') {
                    withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                        sh '''
                            chmod +x ./mvnw
                            DOCKER_HOST=unix:///var/run/docker.sock \
                            TESTCONTAINERS_HOST_OVERRIDE=localhost \
                            TESTCONTAINERS_RYUK_DISABLED=true \
                            ./mvnw clean verify -Dtest=!PostgresContainerTest -Dsonar.login=$SONAR_TOKEN
                        '''
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    docker.build("${ECR_REPOSITORY}:latest")
                }
            }
        }

        stage('Login to AWS ECR') {
            steps {
                withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-hls']]) {
                    sh '''
                        aws ecr get-login-password --region $AWS_REGION | \
                        docker login --username AWS --password-stdin \
                        $(aws sts get-caller-identity --query "Account" --output text).dkr.ecr.$AWS_REGION.amazonaws.com
                    '''
                }
            }
        }

        stage('Tag and Push Docker Image') {
            steps {
                script {
                    def accountId = sh(script: "aws sts get-caller-identity --query 'Account' --output text", returnStdout: true).trim()
                    def imageTag = "${accountId}.dkr.ecr.${AWS_REGION}.amazonaws.com/${ECR_REPOSITORY}:latest"
                    sh """
                        docker tag ${ECR_REPOSITORY}:latest $imageTag
                        docker push $imageTag
                    """
                }
            }
        }

        stage('Deploy to ECS') {
            steps {
                withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-hls']]) {
                    sh '''
                        aws ecs update-service \
                          --cluster $ECS_CLUSTER \
                          --service $ECS_SERVICE \
                          --force-new-deployment \
                          --region $AWS_REGION
                    '''
                }
            }
        }
    }
}
