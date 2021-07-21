package com.example.demo_test.base.design.命令模式;

/**
 * @author JinGuiBo
 * @date 6/25/21 11:10 AM
 **/
public class Command3 extends Command{
    public Command3(Receiver receiver) {
        super(receiver);
    }

    @Override
    void excute() {
        this.receiver.execute3();
    }
}
