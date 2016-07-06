/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AutoCarTestCore;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
/**
 *
 * @author vikumar
 */
public class Timer {
    private static Logger logger = Logger.getLogger ("AutoCarTestLogger");
    public int timeoutValue;
    public int lock;
    private ITimeOutEventListener eventListener;
    public Timer (ITimeOutEventListener eventListener)
    {
        this.eventListener = eventListener;
        this.timeoutValue = 0;
        this.lock = 0;
    }
    public void start (int timeoutValue)
    {
        logger.info ("Starting timer"+this);
        this.timeoutValue = timeoutValue / TimerManager.resolution; 
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
        logger.info ("Stoping timer"+this);
        this.timeoutValue = 0;
        if (TimerManager.lock == 0)
        {
            logger.info ("Removing timer"+this);
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
