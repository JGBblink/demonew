#!/bin/bash

num_1=6
num_2=9

echo "single if:"
if [ $num_1 -lt $num_2 ];
  then
    echo "$num_1 < $num_2"
fi

echo "double if:"
if [ $num_1 -gt $num_2 ];
  then
    echo "$num_1 > $num_2"
  else
    echo "$num_1 < $num_2"
fi

echo "multi if:"
if [ $num_1 -eq $num_2 ];
 then echo "="
 elif [ $num_1 -gt $num_2 ];
  then echo ">"
 elif [ $num_1 -lt $num_2 ];
  then echo "<"
fi

echo "test:please input two var for equles:"
read a
read b

expr 1 + $a &>/dev/null
if [ ! $? -eq 0 ];
  then echo "input a \"$a\" not is int"
  exit 2
fi

expr 1 + $b &>/dev/null
if [ ! $? -eq 0 ];
  then echo "input \"$b\" not is int"
  exit 2
fi

if [ $a -eq $b ]; then echo "$a = $b"
  elif [ $a -gt $b ]; then echo "$a > $b"
  elif [ $a -lt $b ]; then echo "$a < $b"
  elif [ $a -ge $b ]; then echo "$a >= $b"
  elif [ $a -le $b ]; then echo "$a <= $b"
fi
