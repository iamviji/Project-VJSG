/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AutoCarTestCore;

import java.util.logging.Logger;

/**
 *
 * @author vikumar
 */
public class IntrDetectionSensorMsgListener extends SensorMsgListener {
    private static Logger logger = Logger.getLogger ("AutoCarTestLogger");
    enum State 
    {
        STATE_IDLE,
        STATE_SENSED,
    };
    static final int MAX_DISTANCE = 500;
    public int detectionThreshold = 500;
    State state;
    public IntrDetectionSensorMsgListener (IEventListener eventListener)
    {
        super (eventListener);
        moveToIdleState ();
    }
    public int getCurDistance ()
    {
        return -1;
    }
    public void handleMsg (String msg)
    {
        String str [] = msg.split (":");
                    
        int curDistance = Integer.parseInt (str [2]);
        
        if (state.equals (State.STATE_IDLE))
        {
            logger.info ("Curent state is IDLE");
            if (curDistance < detectionThreshold)
            {
                moveToSensedState (); 
                this.eventListener.handleEvent(this, new EventObjectIn ());
            } else
            {
                logger.info ("No Change in state");
            }
        } else if (state.equals(State.STATE_SENSED))
        {
            logger.info ("Curent state is SENSED");
            if (curDistance >= detectionThreshold)
            {
                moveToIdleState (); 
                this.eventListener.handleEvent(this, new EventObjectOut ());
            } else
            {
                  logger.info ("No Change in state");
            }
        }
    }
    
    void handleTimeOut ()
    {
    }
    
    private void moveToIdleState ()
    {
        logger.info ("State is changed to IDLE");
        this.state = State.STATE_IDLE;
    }
    private void moveToSensedState ()
    {
       logger.info ("State is changed to SENSED"); 
       this.state = State.STATE_SENSED;
    }
}
