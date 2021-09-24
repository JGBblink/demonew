#!/bin/bash

echo "shell demo"

# 定义局部变量
_var1="JGB"
_var2="var2"
readonly _var2  # readonly标注的对象不允许重新赋值

# 使用局部变量
echo "var1 = ${_var1} var2 = ${_var2}"

# 重新赋值变量
_var1="var1"
echo ${_var1}
# _var2="var22" 报错：./base.sh: line 16: _var2: readonly variable
echo ${_var2}

# 字符串
echo "字符串"
echo 'test single str ${_var1}'   # 单引号里的任何字符都会原样输出，单引号字符串中的变量是无效的
echo "test double str ${_var1} \"${_var2}\""  # 双引号里可以使用变量，也可使用转义符号

# 字符串 - 拼接
echo "字符串 - 拼接"
_var3='a''b''c''d''e'
echo ${_var3}   # 输出abcde
echo ${#_var3}  # 获取字符串长度（#变量）
echo ${_var3:2:2} # 字符串截取（开始下标:截取长度）,下标从0开始

# 字符串运算符
echo "字符串运算符"
# = != -z -n $
if [ "abc" = "zbb" ]; then
    echo "abc" = "zbb"
fi

if [ "abc" != "zbb" ]; then
    echo "abc" != "zbb"
fi

if [ -z "" ]; then
    echo "''字符串长度为0"
fi

if [ -n "aa" ]; then
    echo "'aa'长度不为0"
fi

if [ $"xx" ]; then
  echo "xx" "长度不为空"
fi

# 数组 数组名=(值1 值2 ... 值n)
echo "数组 数组名=(值1 值2 ... 值n)"
_arr1=(0 1 2 3 4 'a' 'b')
_arr1[7]='c'
_arr1[10]='d'
echo ${_arr1[0]}  # 根据下标获取元素
echo ${_arr1[@]}  # 使用'@'获取所有元素
echo ${_arr1[*]}  # 使用'*'获取所有元素
echo ${#_arr1[@]}    # 获取数组长度,和获取字符串长度类似

# 接收标准输入参数
read name
echo ${name}


