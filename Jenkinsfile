// harbor镜像仓库地址和密码信息
def registry = "harbor.coolcollege.cn"
def registry_se = "5221ab7b-e492-45fa-814a-063bce38c1df"
// gitlab地址和密码信息
def gitlab_url = "http://gitlab.coolcollege.cn/coolcollege/backend/fast-engine-platform.git"
def gitlab_se = "bb763521-0cf5-4099-86e5-130f0269ac9d"
// k8s 凭据信息
def k8s_auth = "4f2a66c9-2446-4bb6-8088-93a8c9bddab5"
// harbor仓库的项目前缀信息
def prefix = "blue-k8s"
// 部署应用的服务名称
def app_name = "fast-engine-platform"
// 部署服务所在的命名空间、副本数、容器暴露的端口、svc的端口
def ns = "cool"
def rc = 1
def cport = 30012
def cluport = 30012
// 构建编译的环境参数
def env = "blue"

node(){
  stage("1.pull git code"){
	checkout([$class: 'GitSCM', branches: [[name: '*/master']], userRemoteConfigs: [[credentialsId: "${gitlab_se}", url: "${gitlab_url}"]]]) 	
		script {
            build_tag = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
            TM = sh(script: 'date +%Y%m%d%H%M')
        }
		image_name_build = "${registry}/${prefix}/${app_name}:${BUILD_NUMBER}"
		image_name_latest = "${registry}/${prefix}/${app_name}:latest"
		
  }

  stage("2.build code"){
	  sh "mvn clean package -P${env} -DskipTests"
  }
  stage("3.build docker and push to harbor") {
        withCredentials([usernamePassword(credentialsId: "${registry_se}", passwordVariable: 'dockerHubPassword', usernameVariable: 'dockerHubUser')]) {
            sh "docker login -u ${dockerHubUser} -p ${dockerHubPassword} ${registry}"
            sh "docker build -t ${image_name_build}  -f Dockerfile .  --no-cache"
            sh "docker tag ${image_name_build} ${image_name_latest}"
            sh "docker push ${image_name_build}"
            sh "docker push ${image_name_latest}"
        }
    }
  stage("4.deploy to k8s") {
        sh "cp templete.yaml ${app_name}-dep.yaml"
		sh "sed -i 's#SVC_NAME#${app_name}#g'  ${app_name}-dep.yaml"
		sh "sed -i 's#NS_NAME#${ns}#g'  ${app_name}-dep.yaml"
		sh "sed -i 's#RC_NUM#${rc}#g'  ${app_name}-dep.yaml"
		sh "sed -i 's#IMAGE_URL#${image_name_latest}#g'  ${app_name}-dep.yaml"
		sh "sed -i 's#CON_PORT#${cport}#g'  ${app_name}-dep.yaml"
		sh "sed -i 's#CLU_PORT#${cluport}#g'  ${app_name}-dep.yaml"
        sh "kubectl apply -f  ${app_name}-dep.yaml"
		echo "deploy success"
    }
}