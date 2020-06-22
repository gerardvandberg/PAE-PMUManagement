pipeline {
    agent {
        docker {
            image 'maven:3-alpine'
            args '-v /root/.m2:/root/.m2'
        }
    }
    stages {
      stage('SCM') {
                  steps {
        git 'https://github.com/AntoniCasasM/PAE-PumpManagement.git'
        }
      }
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }
  }
}
