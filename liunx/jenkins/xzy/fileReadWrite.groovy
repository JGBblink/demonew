// 首先安装jenkins插件Pipeline Utility Steps， 通过这个插件来读取properties文件

pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'Building..'
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
        stage("writeFile demo") {
            steps {
                script {
                    file_path = "${env.WORKSPACE}/test.properties"
                    //准备测试数据，因为Utility Steps插件没有提供写入properties的方法，所以直接将属性拼接成字符串然后写入文件
                    def writeProps = [
                            name: 'zhangsan',
                            age : '25'
                    ]
                    //拼接属性字符串
                    content = writeProps.collect { entry -> entry.key + "=" + entry.value }.join('\n')
                    //写入文件
                    writeFile file: file_path, text: content, encoding: "UTF-8"

                    //读取properties文件，直接用Utility Steps插件提供的readProperties方法
                    def props = readProperties file: file_path, encoding: "UTF-8"
                    println "name为 :" + props['name']
                    println "age为 :" + props['age']

                    //修改属性并写入properties文件
                    props['name'] = 'lisi'
                    //删除属性
                    props.remove('age')
                    //添加属性，也可以用props.put('gender', 'male')方式
                    props['gender'] = 'male'
                    content = props.collect { entry -> entry.key + "=" + entry.value }.join('\n')
                    writeFile file: file_path, text: content, encoding: "UTF-8"
                }
            }
        }
    }
}
