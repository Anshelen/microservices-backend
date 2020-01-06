pipeline {
  agent none
  options {
    skipDefaultCheckout()
    skipStagesAfterUnstable()
  }
  stages {
    stage('Build') {
      agent any
      steps {
        checkout scm
        sh './mvnw compile'
        stash(name: 'compiled', includes: '**')
      }
    }

    stage('Test') {
      agent {
        docker {
          image 'openjdk:11.0.5-slim'
          args '-v $HOME/.m2:/root/.m2'
        }
      }
      steps {
        unstash 'compiled'
        sh './mvnw test'
        junit '**/target/surefire-reports/TEST-*.xml'
      }
    }

    stage('Push Docker Images') {
      agent any
      when {
        branch 'master'
      }
      steps {
        unstash 'compiled'
        script {
          def imageName = "anshelen/microservices-backend:v$BUILD_NUMBER"
          def dockerImage = docker.build(imageName)
          docker.withRegistry('', 'dockerhub-creds') {
            dockerImage.push()
            dockerImage.push("latest")
          }
          echo "Pushed Docker Image: $imageName"
        }
      }
    }
  }
}
