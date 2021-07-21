package com.example.demo_test.base.design.命令模式;

/**
 * @author JinGuiBo
 * @date 6/25/21 11:10 AM
 **/
public class Command2 extends Command{
    public Command2(Receiver receiver) {
        super(receiver);
    }

    @Override
    void excute() {
        this.receiver.execute2();
    }
}
