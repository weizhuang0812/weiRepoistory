package com.foriseland.fif.telnet;

/**
 * <p>ClassName: ThreadRun</p>
 * <p>Description: </p>
 * <p>Company: 梦想工厂--基础架构部/p>
 *
 * @author weizhuang
 * @date 2018-07-16
 */
public class ThreadRun extends Thread {

    private Thread t;


    public void run() {
        new TelnetServer().run();
    }

    public void start () {
        if (t == null) {
            t = new Thread (this);
            t.start ();
        }
    }
}
