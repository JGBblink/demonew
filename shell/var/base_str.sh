#!/bin/bash

# str test
echo "----str test----"
str_test="this is test str skr skr!"
echo $str_test
echo "str count = " ${#str_test}
echo "sub str skip len 5 =" ${str_test:5}
echo "sub str 5 to 7 = " ${str_test:5:7}

# regex test
echo "regex test = " ${str_test/skr/SKR}
echo "regex test = " ${str_test//skr/SKR}

# var
echo "this is not exist var = "${not_exist}
echo "this is not exist var = "${not_exist:-xxx}
echo ${not_exist}
echo "this is not exist var = "${not_exist:=xxx}
echo ${not_exist}
echo "this is not exist var = "${not_exist2}
# echo "this is not exist var = "${not_exist2:?this var is not exist please input}
echo "this is exist var = "${exist:+6}
exist=exist
echo "this is exist var = "${exist:+6}
