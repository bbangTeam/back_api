pipeline {
    // 스테이지 별로 다른 거
    agent any

    tools {
        maven 'mvn'
    }

    triggers {
        pollSCM('*/3 * * * *')
    }

    stages {
        // 레포지토리를 다운로드 받음
        stage('git pull') {
            agent any

            steps {
                git url: 'https://github.com/bbangTeam/back_api.git',
                    branch: 'dev',
                    credentialsId: 'tokenForJenkins'
            }

            post {
                // If Maven was able to run the tests, even if some of the test
                // failed, record the test results and archive the jar file.
                success {
                    echo 'Successfully Cloned Repository'
                }

                always {
                  echo "i tried..."
                }

                cleanup {
                  echo "after all other post condition"
                }

                failure {
                  error 'maven build fail...'
                }
            }
        }

        stage ('Build Maven') {
          agent any
          steps {
            echo 'Build Maven'
            sh 'mvn clean package'
          }

          post {
            success {
                echo 'mvn clean package success..'
            }
            failure {
              error 'maven build fail...'
            }
          }
        }

        stage('Build Docker') {
          agent any
          steps {
            echo 'Build Dokcer'
            sh '''
            docker tag rlabotjd/mysend:backup-bbang-api
            docker build -t rlabotjd/mysend:latest-bbang-api .
            '''
          }

          post {
            success {
                echo 'bbang-api latest docker build success'
            }
            failure {
              error 'bbang-api latest docker build failed..'
            }
          }

        }

        stage('Push Docker hub') {
            agent any

            steps {
                sh '''
                docker login -u rlabotjd -p 251fcc1f-8ec9-4b0a-b440-c97a95a68e9e
                docker push rlabotjd/mysend:latest-bbang-api
                docker push rlabotjd/mysend:backup-bbang-api
                '''
            }

            post {
                success {
                    echo 'bbang-api backup and latest version push in docker-hub success'
                }
                failure {
                    error 'bbang-api backup and latest version push in docker-hub failed...'
                }
            }
        }

        stage('Connect Home-Server and build') {
          agent any
          steps {
            sshPublisher(
                continueOnError: false, failOnError: true,
                publishers: [
                    sshPublisherDesc(
                        configName: "bbang-api",
                        verbose: true,
                        transfers: [
                            sshTransfer(
                                sourceFiles: "docker-compose.yml",
                            ),
                            sshTransfer(
                                sourceFiles: "docker-compose-dev.yml",
                            ),
                            sshTransfer(execCommand: "docker-compose -f docker-compose.yml -f docker-compose-dev.yml down"),
                            sshTransfer(execCommand: "docker-compose -f docker-compose.yml -f docker-compose-dev.yml up -d"),
                            sshTransfer(execCommand: "docker image prune -f")
                        ]
                    )
                ]
            )
          }

          post {
            success {
                echo 'connect Home Server deploy success..'
            }
            failure {
              error 'connect Home Server deploy fail...'
            }
          }
        }
    }
}
