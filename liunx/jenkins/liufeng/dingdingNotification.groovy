//该脚本为钉钉发送通知的demo

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
                //git url: 'git@github.com:LincLiu/JenkinsTest.git'
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
                    // echo "${DINGDING_ROBOT_ID}"
                    // echo "env.BUILD_ID---${env.BUILD_ID}"
                    // echo "env.BUILD_NUMBER---${env.BUILD_NUMBER}"
                    // echo "env.BUILD_TAG---${env.BUILD_TAG}"
                    // echo "env.BUILD_URL---${env.BUILD_URL}"
                    // echo "env.EXECUTOR_NUMBER---${env.EXECUTOR_NUMBER}"
                    // echo "env.JENKINS_URL---${env.JENKINS_URL}"
                    // echo "env.JOB_NAME---${env.JOB_NAME}"
                    // echo "env.NODE_NAME---${env.NODE_NAME}"
                    // echo "env.WORKSPACE---${env.WORKSPACE}"
                    // echo "env.JOB_URL---${env.JOB_URL}"
                    // echo "env.CHANGE_AUTHOR---${env.CHANGE_AUTHOR}"
                    // echo "env.BUILD_USER---${BUILD_USER}"
                    // echo "env.BUILD_USER---${currentBuild.durationString}"
                    
                    //插件文档 https://jenkinsci.github.io/dingtalk-plugin/
                    dingtalk (
                    	//这里的机器人ID是系统自动生成的，需要运维人员手动创建DINGDING_ROBOT_ID这个环境变量并设置对应的value
                        robot: "${DINGDING_ROBOT_ID}",
                        type: 'ACTION_CARD',
                        //需要在钉钉群里@的人的手机号
                        at: ['15708456403'],
                        atAll: false,
                        title: "${env.JOB_NAME} 构建失败",
                        text: [
                            "[${env.JOB_NAME}](${env.JOB_URL})",
                            "---",
                            "- 任	 务：[#${env.BUILD_NUMBER}](${env.BUILD_URL})",
                            "- 状	 态：<font color=red>${currentBuild.result}</font>",
                            "- 持续时间：${currentBuild.durationString}",
                            "- 执 行 人：${BUILD_USER}",
                            '---',
                        ],
                        //这里提供一个直接跳转控制台的按钮
                        btns: [
                          [
                              title: '控制台',
                              actionUrl: "${env.BUILD_URL}/console"
                          ]
                        ],
                        btnLayout:"V"
                    )
                }
            }
        }
    }
}