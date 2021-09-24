#!/bin/bash

echo "this is a +-*/ calculation"
read -p "please input a=" a
read -p "please input b=" b

echo "$a + $b=" $((a + b))
echo "$a - $b=" $((a - b))
echo "$a * $b=" $((a * b))
echo "$a / $b=" $((a / b))