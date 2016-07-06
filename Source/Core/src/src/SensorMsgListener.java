/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AutoCarTestCore;
/**
 *
 * @author vikumar
 */
public abstract class SensorMsgListener implements ISensorMsgListener, IEventDispatcher {
 
 //protected int timerTick;
 //protected int internalTimerTick;
 //protected int sensorMsgFreq;
 protected IEventListener eventListener;

 public SensorMsgListener (IEventListener eventListener)
 {
     //this.timerTick = 0;
     //this.internalTimerTick = 0;
     this.eventListener = eventListener;
     
 }
 /*
 protected void restartTimer (int timeout)
 {
     this.timerTick = timeout / AutoCarTestAlgoPlane.TIMER_RESOLUTION;
     this.internalTimerTick = timeout / sensorMsgFreq;
 }
 protected void stopTimer ()
 {
        this.timerTick = 0;
        this.internalTimerTick = 0;
 }
 abstract  void handleTimeOut ();
 public void hadleTimerTick ()
 {
     if (this.timerTick > 0)
     {
        this.timerTick--;
        if (this.timerTick == 0)
        {
            this.internalTimerTick = 0;
            handleTimeOut ();
        }
     }
 }
 protected void executeInternalTimer ()
 {
    this.internalTimerTick--;
    if (this.internalTimerTick == 0)
    {
        this.timerTick = 0;
        handleTimeOut ();
    }
 }
 */
}
