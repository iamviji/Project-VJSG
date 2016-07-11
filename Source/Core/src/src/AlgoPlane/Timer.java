/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoPlane;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import Common.DataBase;
/**
 *
 * @author vikumar
 */
public class Timer {
    private static Logger logger = DataBase.logger;
    public int timeoutValue;
    public int lock;
    private ITimeOutEventListener eventListener;
    public String name;
    public Timer (ITimeOutEventListener eventListener, String name)
    {
        this.eventListener = eventListener;
        this.name = name;
        this.timeoutValue = 0;
        this.lock = 0;
    }
    public void start (int timeoutValue)
    {
        logger.info ("Starting timer:"+this.name);
        this.timeoutValue = timeoutValue / DataBase.TIMER_RESOLUTION; 
        Iterator <Timer> it = TimerManager.timerList.iterator();
        while (it.hasNext())
        {
            Timer timer = it.next();
            if (timer == this)
            {
                return;
            }
        }
        TimerManager.timerList.add(this);
    }
    public void stop ()
    {
        logger.info ("Stoping timer:"+this.name);
        this.timeoutValue = 0;
        if (TimerManager.lock == 0)
        {
            logger.info ("Removing timer:"+this.name);
            TimerManager.timerList.remove(this);            
        }
    }
    public void giveTick ()
    {
        if (this.timeoutValue > 0)
        {
            --this.timeoutValue;
            if (this.timeoutValue == 0)
            {
                this.eventListener.handleTimeoutEvent(this);                
            }
        }
    }
}
