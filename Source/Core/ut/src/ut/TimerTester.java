/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autocartestcoretest;

import AlgoPlane.*;

/**
 *
 * @author vikumar
 */
public class TimerTester implements ITimeOutEventListener {
     public void handleTimeoutEvent (Timer timer)
     {
         System.out.println ("Timeout Event " + timer);
     }
}
