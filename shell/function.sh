#!/bin/bash

function max() {
    expr 1 + $1 &>/dev/null
    if [ $? -eq 2 ]; then exit 2
    fi

    if [ $1 -gt $2 ];
      then return $1
      else return $2
    fi
}

max
echo $?
