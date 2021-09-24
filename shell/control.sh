#!/bin/bash

# 流程控制
var1=$1
var2=$2

# if
if [ ${var1} = ${var2} ]; then
  echo ${var1} = ${var2}
fi

# if else
if [ ${var1} = ${var2} ]; then
    echo ${var1} = ${var2}
else
    echo ${var1} != ${var2}
fi

# if else if
if [ ${var1} = ${var2} ]; then
    echo ${var1} = ${var2}
elif [ ${var1} != ${var2} ]; then
    echo false
fi

# for
n=10
for (( i = 0; i < n; i++ )); do
    echo ${i}
done

for i in {1..5} ; do
    echo ${i}
done

# while
echo "input exit out"
read input
index=0
while [ ${input} != "exit" ]; do
    echo index = ${index}
    read input
    if [ ${index} -gt 5 ]; then
        break
    fi
    index=`expr ${index} + 1`
done

# case case
echo "case"
case ${input} in
  1) echo "1";;
  2) echo "2";;
  3) echo "3";;
  *) echo "other";;
esac