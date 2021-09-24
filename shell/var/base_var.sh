#!/bin/bash

#
var_1=$1
var_2=var2
var_3="var 3"
var_4='var 4'
var_count=$#
var_all1=$*
var_all2=$@

echo "----base var test----"
echo "base var:" $var_1 $var_2
echo "base var 3:" ${var_3}
echo 'var: $var xx test'
echo "\"\" var : $var_1 test"
echo "var count: $var_count"
echo "all var: $var_all1"
echo "all var: $var_all2"

echo "foreach all var:"
for i in $var_all1 ; do
    echo $i
done

echo "foreach all var str for*:"
for i in "$*" ; do
    echo $i
done

echo "foreach all var for@:"
for i in "$@" ; do
    echo $i
done

echo "----Read Test----"
read -p "please input name:" read_1
echo $read_1

