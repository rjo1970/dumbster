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
        sh './gradlew test'
        junit 'build/test-results/test/*.xml'
        stash includes: 'build/jacoco/test.exec', name: 'jacocoTest'
    }
}
stage('Checks') {
    parallel(warnings: {
        node {
            checkout scm
            unstash 'build'
            step([$class: 'WarningsPublisher', canRunOnFailed: true, consoleParsers: [[parserName: 'Java Compiler (javac)']]])
        }
    }, pmd: {
        node {
            checkout scm
            unstash 'build'
            sh './gradlew pmdMain pmdTest'
            step([$class: 'PmdPublisher', canRunOnFailed: true, pattern: 'build/reports/pmd/*.xml'])
        }
    }, findbugs: {
        node {
            checkout scm
            unstash 'build'
            sh './gradlew findbugsMain findbugsTest'
            step([$class: 'FindBugsPublisher', canRunOnFailed: true, pattern: 'build/reports/findbugs/*.xml'])
        }
    }, jacoco: {
        node {
            checkout scm
            unstash 'build'
            unstash 'jacocoTest'
            step([$class: 'JacocoPublisher', execPattern: 'build/jacoco/*.exec', classPattern: 'build/classes/java/main'])
        }
    })
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
