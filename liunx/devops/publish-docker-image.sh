#!/usr/bin/env bash

####参数：
# 1、容器名称，如 rplus-service-community
# 2、build号，如 build_v20
# 3、环境标识，如 "-e spring.profiles.active=local"
# 4、端口，如 20090:20090
# 5、是否给镜像打上release标签true、false（可选）
# 6、镜像的地址，如：registry-vpc.cn-qingdao.aliyuncs.com/postop/，如果不传则默认该值
# 最终形式如下：
# ./publish-docker-image.sh rplus-service-community build_v2 "-e spring.profiles.active=dev" 20090:20090 true registry地址

echo '================开始远程启动================'
echo "======= 传入参数：$*"
echo "======= 第1个参数，容器名称：$1"
echo "======= 第2个参数，容器版本：$2"
echo "======= 第3个参数，环境变量：$3"
echo "======= 第4个参数，端口映射：$4"
echo "======= 第5个参数，release标签：$5"
echo "======= 第6个参数，镜像地址：$6"

# Registry地址
# 公网地址:registry.cn-qingdao.aliyuncs.com/postop/
# 经典网络:registry-internal.cn-qingdao.aliyuncs.com/postop/
# 专有网络

registry_vpc_address=registry-vpc.cn-qingdao.aliyuncs.com/postop/
registry_address=$6

if [[ ${registry_address} ]]; then
    echo "======= 使用传入的镜像地址： [$registry_address]"
else
    registry_address=${registry_vpc_address};
    echo "======= 默认的镜像地址： [$registry_address]"
fi

new_original_image=${registry_address}$1:$2
new_image_name=$1:$2
new_container_name=$1.$2
whether_release_tag=$5
tag_for_release_image=${registry_address}$1:'release_'`date '+%Y%m%d'`


# 旧容器ID
old_container_id=$(docker ps -a | grep $1|awk '{print $1}')
# 旧镜像名和tag
old_image_name=$(docker ps -a | grep $1|awk '{print $2}')

echo "======= 变量值：
        old_container_id=$old_container_id, old_image_name=$old_image_name,
        new_original_image=$new_original_image, new_image_name=$new_image_name,
        new_container_name=$new_container_name"

# 校验必要参数
if [[ -z $1 ]] || [[ -z $2 ]] || [[ -z $4 ]]; then
    echo "======= 请传入必要的参数：第一个参数为镜像名称，第二个参数为镜像Tag，第四个参数为容器的端口映射"
    exit
fi

docker login --username=docker@postop --password=followup2019 registry-vpc.cn-qingdao.aliyuncs.com

#### 1.停止原有的容器
echo "======= Begin stop container: [$old_container_id]"
if [[ ${old_container_id} ]]; then
    echo "======= Stop $old_container_id"
    docker stop ${old_container_id}
fi

#### 2.删除上一个容器和镜像
echo "======= Begin remove old container: name=$old_image_name, id=$old_container_id"
if [[ ${old_container_id} ]]; then
    echo "======= Remove container: [$old_container_id]"
    docker rm -v ${old_container_id}
fi

### 删除所有${old_image_name}镜像
echo "======= Begin remove all old image: name=$old_image_name"
if [[ ${old_image_name} ]]; then
    echo "======= Remove image: [$old_image_name]"
    docker rmi --force `docker images | grep $1 | awk '{print $3}'`
    # 删除单个容器 docker rmi ${old_image_name}
fi

#### 3.拉取镜像
echo "======= Begin pull new docker image."
echo "======= Pull new docker image: [$new_original_image]"
docker pull ${new_original_image}
echo "======= Tag docker image from [$new_original_image] to [$new_image_name]"
docker tag ${new_original_image} ${new_image_name}

#### 4.启动新的容器
echo "======= Start container: [$new_container_name]"
docker run -d -p $4 $3 --name=${new_container_name} ${new_image_name}
echo "======= Container [$new_container_name] Successful startup"

#### 5.正式环境发布后给镜像打上Release标签（可选）
if [[ "true" == "$whether_release_tag" ]]; then
    echo "======= Begin tag and push release images [$tag_for_release_image]"
    echo "======= Put release tag for image: [$new_original_image]"
    docker tag ${new_original_image} ${tag_for_release_image}
    echo "======= Begin new original docker image: [$new_original_image]"
    docker push ${tag_for_release_image}
    echo "======= Remove release tag image: [$tag_for_release_image]"
    docker rmi ${tag_for_release_image}
fi

# 删除多余镜像
if [[ ${new_original_image} ]]; then
    echo "======= Remove new original docker image: [$new_original_image]"
    docker rmi ${new_original_image}
fi

echo "Finished!"
echo '================结束远程启动================'
exit