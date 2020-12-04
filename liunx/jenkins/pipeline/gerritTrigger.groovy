//思路：使用Gerrit Trigger插件，当开发人员提交补丁到gerrit上时，触发流水线，而后进行后续的步骤；当构建成功后，自动submit代码。

//样例：
//1、示例job：http://jenkins2.suifangyun.com/job/test.pipeline.zengling/
//2、补丁日志：https://gerrit.suifangyun.com/#/c/project-for-testing-jenkins-gerrit-trigger/+/19344/
//3、手动触发：http://jenkins2.suifangyun.com/gerrit_manual_trigger/

//其他说明：代码中的jenkins:Unol0EIxHzjaj27KAQ9e2bvRs6zaN8LZB/ecDO7/FQ   是帐号密码，
// （1）需要研究是否可以直接从jenkins的配置里面去获取，这样更好；或者 用传入环境变量的方式；
// （2）在jenkins log中是明文输出，后续看一下如何避免泄漏。

pipeline {
    agent any
    stages {
        stage('代码检出') {
            //steps stage中具体要做的事情
            steps {
                //从svn地址检出代码
                echo '收到Gerrit Trigger事件:'
                echo "${GERRIT_BRANCH}"
                echo "${GERRIT_CHANGE_NUMBER}"
                echo "${GERRIT_CHANGE_ID}"
                echo "${GERRIT_PATCHSET_NUMBER}"
                echo "${GERRIT_PATCHSET_REVISION}"
                echo "${GERRIT_CHANGE_SUBJECT}"
                echo "${GERRIT_CHANGE_OWNER_NAME}"
                dir("${GERRIT_PROJECT}"){
                    echo '检出代码'
                    checkout([$class: 'GitSCM', branches: [[name: "$GERRIT_REFSPEC"]],
                              doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [],
                              userRemoteConfigs: [
                                      [credentialsId: 'gerrit_user_jenkins',
                                       refspec: '+refs/heads/*:refs/remotes/origin/* +refs/changes/*:refs/changes/*',
                                       url: "https://git.suifangyun.com/${GERRIT_PROJECT}"
                                      ]
                              ]
                    ])
                }
                echo '代码检出：done'
            }
        }
        stage('静态扫描') {
            steps {
                echo '开始代码静态扫描'
                //
                echo '阿里代码公约扫描'
                //
                echo '自定义代码规范扫描'
                //
                echo 'SonarCube扫描'
                //
                echo '代码静态扫描：done'
                //完成代码静态扫描
                sh label: '在Gerrit中添加注释', script: 'curl -X POST --basic -u jenkins:Unol0EIxHzjaj27KAQ9e2bvRs6zaN8LZB/ecDO7/FQ' +
                        ' -d \'{"message":"完成代码静态扫描"}\' ' +
                        ' --header "Content-Type: application/json; charset=UTF-8"' +
                        ' https://git.suifangyun.com/a/changes/${GERRIT_PROJECT}~${GERRIT_CHANGE_NUMBER}/revisions/${GERRIT_PATCHSET_REVISION}/review'
            }
        }
        stage('单元测试') {
            //单元测试通过说明编译成功。
            steps {
                echo '开始单元测试'
                //
                echo '单元测试:done'
                //完成单元测试
                sh label: '在Gerrit中添加注释', script: 'curl -X POST --basic -u jenkins:Unol0EIxHzjaj27KAQ9e2bvRs6zaN8LZB/ecDO7/FQ' +
                        ' -d \'{"message":"完成单元测试"}\' ' +
                        ' --header "Content-Type: application/json; charset=UTF-8"' +
                        ' https://git.suifangyun.com/a/changes/${GERRIT_PROJECT}~${GERRIT_CHANGE_NUMBER}/revisions/${GERRIT_PATCHSET_REVISION}/review'
            }
        }
        stage('镜像打包') {
            //单元测试通过说明编译成功。
            steps {
                echo '开始镜像打包'
                //
                echo '镜像打包:done'
                sh label: '在Gerrit中添加注释', script: 'curl -X POST --basic -u jenkins:Unol0EIxHzjaj27KAQ9e2bvRs6zaN8LZB/ecDO7/FQ' +
                        ' -d \'{"message":"完成镜像打包"}\' ' +
                        ' --header "Content-Type: application/json; charset=UTF-8"' +
                        ' https://git.suifangyun.com/a/changes/${GERRIT_PROJECT}~${GERRIT_CHANGE_NUMBER}/revisions/${GERRIT_PATCHSET_REVISION}/review'
            }
        }
        stage('Gerrit Code Review') {
            //如果以上成功，则自动review +2
            steps {
                //成功则+2
                sh label: '在Gerrit中添加注释', script: 'curl -X POST --basic -u jenkins:Unol0EIxHzjaj27KAQ9e2bvRs6zaN8LZB/ecDO7/FQ' +
                        ' -d \'{"labels":{"Code-Review":2},"drafts":"PUBLISH_ALL_REVISIONS","message":"自动Review代码"}\' ' +
                        ' --header "Content-Type: application/json; charset=UTF-8"' +
                        ' https://git.suifangyun.com/a/changes/${GERRIT_PROJECT}~${GERRIT_CHANGE_NUMBER}/revisions/${GERRIT_PATCHSET_REVISION}/review'
                //submit代码
                sh label: '在Gerrit中添加注释', script: 'curl -X POST --basic -u jenkins:Unol0EIxHzjaj27KAQ9e2bvRs6zaN8LZB/ecDO7/FQ' +
                        ' --header "Accept: application/json"' +
                        ' https://git.suifangyun.com/a/changes/${GERRIT_PROJECT}~${GERRIT_CHANGE_NUMBER}/revisions/${GERRIT_PATCHSET_REVISION}/submit'
                echo ' Code Review:done'
            }
        }
        stage('XXXX') {
            steps {


                echo '执行完成'

            }
        }
    }

    post {
        changed {
            echo 'pipeline post changed'
        }
        always {
            echo 'pipeline post always'
        }
        success {
            echo 'pipeline post success'
        }
    }
}
