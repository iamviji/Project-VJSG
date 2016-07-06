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
public class TimerManager {
    private static Logger logger = Logger.getLogger ("AutoCarTestLogger");
    public static List<Timer> timerList = new ArrayList<Timer> ();
    public static int lock = 0;
    public static int resolution;
    protected TimerManager ()
    {
      // nothing 
    }
    
    public static void giveTick ()
    {
        lock = 1;
        Iterator<Timer> it = timerList.iterator ();
        while (it.hasNext ())
        {
            
            Timer timer = it.next (); 
            logger.info ("While Tick handling"+timer);
            timer.giveTick(); 
            if (timer.timeoutValue == 0)
            {
                logger.info ("removing timer"+timer);
                it.remove ();
            }
        }
        lock = 0;
    }
}
