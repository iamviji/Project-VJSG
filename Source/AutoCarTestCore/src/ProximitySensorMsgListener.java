/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AutoCarTestCore;

import java.util.logging.Logger;
import java.lang.Math.*;
/**
 *
 * @author vikumar
 */
public class ProximitySensorMsgListener extends SensorMsgListener {
    private static Logger logger = Logger.getLogger ("AutoCarTestLogger");
    int threshold;
    enum State 
    {
        STATE_IDLE,
        STATE_SENSING,
        STATE_TOUCHED,
    };
    static final int MAX_DISTANCE = 500;
    static final int MIN_DISTANCE = 10;
    static final int NO_MOVEMENT_TIMEOUT_VALUE = 200;
    static final int SENSOR_MSG_FREQ = 50;
    State state;
    int prevDistance;
    public ProximitySensorMsgListener (IEventListener eventListener, int threshold)
    {
        super (eventListener);
        this.threshold = threshold;
        this.sensorMsgFreq = SENSOR_MSG_FREQ;
        moveToIdleState ();
        this.prevDistance = MAX_DISTANCE;
      }
    
    public void handleMsg (String msg)
    {
        logger.info ("Entry. GapCounter " + this.internalTimerTick + " timer count " + this.timerTick);
        String str [] = msg.split (":");
        int curDistance = Integer.parseInt (str [2]);
        if (state.equals(State.STATE_IDLE))
        {
            logger.info ("Current State is IDLE");
            if (curDistance < MAX_DISTANCE && curDistance >= MIN_DISTANCE)
            {
                moveToSensingState ();
                logger.info ("Generating EventObjectEntered");
                // Send EventObjectEntered
                prevDistance = curDistance;    
            } else if (curDistance < MIN_DISTANCE)
            {
                moveToTouchedState ();
                logger.info ("Generating EventObjectTouched");
            } else
            {
                // Ignore
            }
        } else if (state.equals(State.STATE_SENSING))
        {
            logger.info ("Current State is SENSING");
            if (curDistance >= MAX_DISTANCE)
            {
                moveToIdleState ();
                logger.info ("Generating EventObjectExited");
                // Send EventObjectExited                
            } else if (curDistance < MIN_DISTANCE) 
            {
                moveToTouchedState ();
                // Send EventObjectTouched
                logger.info ("Generating EventObjectTouched");
            } else
            {
                int diff = prevDistance - curDistance;
                diff = Math.abs (diff);
                if (diff >= this.threshold)
                {
                    restartTimer (NO_MOVEMENT_TIMEOUT_VALUE);
                    this.prevDistance = curDistance;
                    logger.info ("Movement is observed, Timer is restarted");
                } else
                {
                    logger.info ("No Movement is observed");
                    executeInternalTimer ();
                }
            }
        } else if (state.equals(State.STATE_TOUCHED))
        {
            logger.info ("Current State is TOUCHED");
            if (curDistance >= MAX_DISTANCE)
            {
                logger.info ("State is changed to IDLE");
                logger.info ("Generating EventObjectExited");
            } else if (curDistance >= MIN_DISTANCE)
            {
                logger.info ("Movement is observed");
                logger.info ("Generating EventObjectMoved");
                moveToSensingState ();
                this.prevDistance = curDistance;
            }     
        }
    }
    void handleTimeOut ()
    {
        logger.info ("Generating EventObjectNoMovement");
        restartTimer (NO_MOVEMENT_TIMEOUT_VALUE);      
    }
    
    private void moveToIdleState ()
    {
        logger.info ("State is changed to IDLE");
        this.state = State.STATE_IDLE;
        stopTimer ();
    }
    private void moveToSensingState ()
    {
        logger.info ("State is changed to SENSING");
        this.state = State.STATE_SENSING;
        restartTimer (NO_MOVEMENT_TIMEOUT_VALUE);
    }
    private void moveToTouchedState ()
    {
        logger.info ("State is changed to TOUCHED");
        this.state = State.STATE_TOUCHED;
        restartTimer (NO_MOVEMENT_TIMEOUT_VALUE);   
    }
    
    
}
