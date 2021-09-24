#!/bin/bash

# 获取执行参数 $[n],$[0]为执行文件名
echo $0
echo $1

# $
echo $#   # 传递参数个数
echo $*   # 以字符串显示所有传递的参数 以"$1" "$2" … "$n" 的形式输出所有参数
echo $@   # 现实所有传递参数，与$*不同，该参数是可分割的
echo $?   # 显示最后命令的退出状态。0表示没有错误，其他任何值表明有错误。
echo $$   # 脚本运行的当前进程ID号

# 获取所有传递参数,并单独显示
for i in $@ ; do
    echo $i
done

# 运算符
var1=10
var2=5

# 加法 `expr var1 + var2 + ...` 表达式和运算符之间要有空格，例如 2+2 是不对的，必须写成 2 + 2
# + - * / % 基本相同，表达式规则为 `expr var1 运算符 var2` 乘法需要进行转义
echo `expr ${var1} + ${var2} + 4`
echo `expr ${var1} \* ${var2} + 4`    # 乘法

# 赋值
var3=${var1}
echo ${var3}

# 比较运算符 == !=
if [ ${var1} == ${var2} ]
then
  echo true
fi

if [ 1 = 1 ]
then
  echo true
fi

if [ 1 -eq 1 ]
then
  echo true
fi

if [ ${var1} != ${var2} ]
then
  echo false
fi

# 关系运算符 -eq -ne -gt -lt -ge -le
# -o(或运算) -a(与运算)


