//该脚本为给代码打tag的demo

pipeline {
    agent any
    stages {
        stage('build') {
            steps {
                echo 'build code'
                git url: 'git@github.com:LincLiu/JenkinsTest.git'
            }
            
            post {
                success {
                    script{
                        sh '''
                        GIT_TAG=$(date "+%Y%m%d%H%M%S")
                        git config user.email "liufeng@postop.cn"
                        git config user.name "liufeng"
                        git tag -a -m "$GIT_TAG" $GIT_TAG
                        git push origin --tags
                        '''
                        }
                    }
            }
        }
    }
}