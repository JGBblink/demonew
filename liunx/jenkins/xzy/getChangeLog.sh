#!/bin/bash

# 获取最近一条change log
# 命令执行 ./getChangeLog.sh /xxx/xxx/RPlus.Service.UserDataCenter
# $1为项目路径

cd $1
result=`git log -1 --pretty=format:" commit: %H %n Author: %an %n Date: %ad %n %s" --stat --date=format:"%Y-%m-%d %H:%M:%S"`
echo "$result"
