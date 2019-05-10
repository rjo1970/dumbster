#!groovy

stage('Build') {
    node {
        checkout scm
        sh './gradlew clean assemble'
        stash includes: 'build/**/*', name: 'build'
    }
}
stage('Tests') {
    node {
        checkout scm
        unstash 'build'
        sh './gradlew test jacocoTestReport'
        junit 'build/test-results/test/*.xml'
        stash includes: 'build/reports/jacoco/test/jacocoTestReport.xml', name: 'jacocoTest'
        stash includes: 'build/test-results/test/*.xml', name: 'junitTest'
    }
}
if (scm.branches[0].name == 'master') {
    stage('SonarQube Analysis') {
        node {
            checkout scm
            withSonarQubeEnv('Sonar') {
                unstash 'build'
                unstash 'jacocoTest'
                unstash 'junitTest'
                sh './gradlew --info sonarqube'
            }
        }
    }
    stage('SonarQube Quality Gate'){
        timeout(time: 1, unit: 'HOURS') {
            node {
                def qg = waitForQualityGate()
                if (qg.status != 'OK') {
                  error "Pipeline aborted due to quality gate failure: ${qg.status}"
                }
            }
        }
    }
}
stage('Archive') {
    node {
        checkout scm
        unstash 'build'
        sh './gradlew uploadArchives'
    }
}
stage('Build for production') {
    milestone 10
    timeout(time:7, unit:'DAYS') {
        input message:'Make a production build (version change)?'
    }
    milestone 11
    node {
        checkout scm
        unstash 'build'
        sh './gradlew release'
    }
}
