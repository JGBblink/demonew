//该脚本为通过邮件发送通知的demo

pipeline {
    agent any
    environment {
       BUILD_USER_ID = ""
       BUILD_USER = ""
       BUILD_USER_EMAIL = ""
    }
    stages {
        stage('build') {
            steps {
                echo 'build code'
            }
            
            post {
                success {
                    //安装build user vars plugin插件后，可以通过这种方式获得构建者的信息
                    wrap([$class: 'BuildUser'])
                    {
                        script {
                           BUILD_USER_ID = "${env.BUILD_USER_ID}"
                           BUILD_USER = "${env.BUILD_USER}"
                           BUILD_USER_EMAIL = "${env.BUILD_USER_EMAIL}"
                       }
                    }
                    
                    mail to: "liufeng@postop.cn",
                         subject: "${env.JOB_NAME} 构建失败",
                         body: "---\n- 任     务：${env.BUILD_URL}\n- 状     态：${currentBuild.result}\n- 持续时间：${currentBuild.durationString}\n- 执 行 人：${BUILD_USER}"
                }
            }
        }
    }
}