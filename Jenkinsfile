pipeline {
  agent none
  options {
    skipDefaultCheckout()
    skipStagesAfterUnstable()
  }
  stages {
    stage('Checkout') {
      agent any
      steps {
        checkout scm
        stash(name: 'sources', includes: '**')
      }
    }

    stage("Prepare container") {
      agent {
        docker {
          image 'openjdk:11.0.5-slim'
          args '-v $HOME/.m2:/root/.m2'
        }
      }
      stages {
        stage('Build') {
          steps {
            unstash 'sources'
            sh './mvnw compile'
            stash(name: 'compiled', includes: '**')
          }
        }
        stage('Test') {
          steps {
            unstash 'compiled'
            sh './mvnw test'
            junit '**/target/surefire-reports/TEST-*.xml'
          }
        }
      }
    }

    stage('Push images') {
      agent any
      when {
        branch 'master'
      }
      steps {
        unstash 'sources'
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
