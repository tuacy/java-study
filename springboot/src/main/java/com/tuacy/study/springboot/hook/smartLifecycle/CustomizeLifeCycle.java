package com.tuacy.study.springboot.hook.smartLifecycle;

import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

@Component
public class CustomizeLifeCycle implements SmartLifecycle {

    private boolean isRunning = false;

    /**
     * 根据该方法的返回值决定是否执行start方法。
     * 返回true时start方法会被自动执行，返回false则不会。
     */
    @Override
    public boolean isAutoStartup() {
        System.out.println("start");
        // 默认为false
        return true;
    }

    /**
     * 1. 只有该方法返回false时，start方法才会被执行。<br/>
     * 2. 只有该方法返回true时，stop(Runnable callback)或stop()方法才会被执行。
     */
    @Override
    public boolean isRunning() {
        System.out.println("isRunning");
        // 默认返回false
        return isRunning;
    }

    /**
     * 1. 我们主要在该方法中启动任务或者其他异步服务，比如开启MQ接收消息<br/>
     * 2. 当上下文被刷新（所有对象已被实例化和初始化之后）时，将调用该方法，默认生命周期处理器将检查每个SmartLifecycle对象的isAutoStartup()方法返回的布尔值。
     * 如果为“true”，则该方法会被调用，而不是等待显式调用自己的start()方法。
     */
    @Override
    public void start() {
        System.out.println("start");
        // 执行完其他业务后，可以修改 isRunning = true
        isRunning = true;
    }

    /**
     * 接口Lifecycle的子类的方法，只有非SmartLifecycle的子类才会执行该方法。<br/>
     * 1. 该方法只对直接实现接口Lifecycle的类才起作用，对实现SmartLifecycle接口的类无效。<br/>
     * 2. 方法stop()和方法stop(Runnable callback)的区别只在于，后者是SmartLifecycle子类的专属。
     */
    @Override
    public void stop() {
        System.out.println("stop");
        isRunning = false;
    }

    /**
     * SmartLifecycle子类的才有的方法，当isRunning方法返回true时，该方法才会被调用。
     */
    @Override
    public void stop(Runnable callback) {
        System.out.println("stop(Runnable)");

        // 如果你让isRunning返回true，需要执行stop这个方法，那么就不要忘记调用callback.run()。
        // 否则在你程序退出时，Spring的DefaultLifecycleProcessor会认为你这个TestSmartLifecycle没有stop完成，程序会一直卡着结束不了，等待一定时间（默认超时时间30秒）后才会自动结束。
        callback.run();

        isRunning = false;
    }


    /**
     * 如果工程中有多个实现接口SmartLifecycle的类，则这些类的start的执行顺序按getPhase方法返回值从小到大执行。<br/>
     * 例如：1比2先执行，-1比0先执行。 stop方法的执行顺序则相反，getPhase返回值较大类的stop方法先被调用，小的后被调用。
     */
    @Override
    public int getPhase() {
        return 0;
    }
}
