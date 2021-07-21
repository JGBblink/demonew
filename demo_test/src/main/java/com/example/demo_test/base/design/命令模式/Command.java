package com.example.demo_test.base.design.命令模式;

/**
 * 命令对象,提供给命令的抽象
 * @author JinGuiBo
 * @date 6/25/21 11:05 AM
 **/
public abstract class Command {

    protected Receiver receiver;

    public Command(Receiver receiver) {
        this.receiver = receiver;
    }

    abstract void excute();
}
