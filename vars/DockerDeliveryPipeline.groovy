def call(body) {
def config = [:]
body.resolveStrategy = Closure.DELEGATE_FIRST
body.delegate = config
body()
        pipeline {
        agent any
    
    environment {
        DOCKER_HUB_CREDENTIALS = credentials('rajanikantlugade999_docker_hub') // Replace with your credentials ID
        DOCKER_HUB_REPO = 'rajanikantlugade999/cloudethix-nginix-rajani'       // Replace with your Docker Hub repository
        IMAGE_TAG = 'latest'                                              // Replace with your desired image tag
        //platform = 'getPlatformName'
    }
   /*   stages {
        stage('Get Platform Name') {
            steps {
                script {
                    // Ensure getPlatformName is called as a function
                    def platform = org.example.utilities.PlatformUtil.getPlatformName()
                    echo "Platform: ${platform}"
                }
            }
        }
    }
*/ }
    stages {
        stage('Build Docker Image from shared libraary') {
            steps {
                script {
                        // sh 'echo "${env.paltform}"'
                    // Define the Docker image name and tag
                    def dockerImage = "${env.DOCKER_HUB_REPO}:${env.IMAGE_TAG}"
                    
                    // Build the Docker image
                    sh "docker build -t ${dockerImage} ."
                }
            }
        }
    }
        stage('Login to Docker Hub from shared libraary') {
            steps {
                script {
                    // Log in to Docker Hub
                    sh "echo ${DOCKER_HUB_CREDENTIALS_PSW} | docker login -u ${DOCKER_HUB_CREDENTIALS_USR} --password-stdin"
                }
            }
        }
        
        stage('Push Docker Image from shared libraary') {
            steps {
                script {
                    // Define the Docker image name and tag
                    def dockerImage = "${env.DOCKER_HUB_REPO}:${env.IMAGE_TAG}"
                    
                    // Push the Docker image to Docker Hub
                    sh "docker push ${dockerImage}"
                }
            }
        }
    }
    
    post {
        always {
            // Clean up the Docker environment from shared libraary
            sh 'docker logout'
        }
    }
}
def getPlatformName() {
    return config.platform
}

