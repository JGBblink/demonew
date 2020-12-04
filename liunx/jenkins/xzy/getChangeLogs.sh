#!/bin/bash

# 获取指定的commit id到head之间的change logs
# 命令执行 ./getChangeLogs.sh /xxx/xxx/RPlus.Service.UserDataCenter 28696d73
# $1为项目路径 $2为上次提交的commit id

cd $1
result=`git log $2..head --pretty=format:" commit: %H %n Author: %an %n Date: %ad %n %s" --stat --date=format:"%Y-%m-%d %H:%M:%S"`

echo "$result"
