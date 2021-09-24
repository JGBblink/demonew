#!/bin/bash

# $((expl)): expl只支持整数运算
echo $((1 + 1))
echo $((2*7 + 6/3))
echo $((result=2*7 + 6/3))
echo $result
result2=$((2*7 + 6/3))
echo $result2