package com.example.demo_test.base.design.命令模式;

import java.lang.reflect.Proxy;

/**
 * @author JinGuiBo
 * @date 6/25/21 11:14 AM
 **/
public class CommandMain {
    public static void main(String[] args) {

        Receiver receiver = new Receiver();

        Command1 command1 = new Command1(receiver);
        Command2 command2 = new Command2(receiver);
        Command3 command3 = new Command3(receiver);


        Invoker invoker = new Invoker();

        invoker.invok(command1);
        invoker.invok(command2);
        invoker.invok(command3);
    }
}
