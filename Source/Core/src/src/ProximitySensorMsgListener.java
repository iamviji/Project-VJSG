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
    //static final int NO_MOVEMENT_TIMEOUT_VALUE = 200;
    // static final int SENSOR_MSG_FREQ = 50;
    State state;
    int prevDistance;
    int curDistance;
    public ProximitySensorMsgListener (IEventListener eventListener, int threshold)
    {
        super (eventListener);
        this.threshold = threshold;
        //this.sensorMsgFreq = SENSOR_MSG_FREQ;
        moveToIdleState ();
        this.prevDistance = MAX_DISTANCE;
    }
    public int getCurDistance ()
    {
        return this.curDistance;
    }
    public void handleMsg (String msg)
    {
        logger.info ("Entry");
        String str [] = msg.split (":");
        this.curDistance = Integer.parseInt (str [2]);
        if (state.equals(State.STATE_IDLE))
        {
            logger.info ("Current State is IDLE");
            if (curDistance < MAX_DISTANCE && curDistance >= MIN_DISTANCE)
            {
                moveToSensingState ();
                logger.info ("Generating EventObjectEntered");
                this.eventListener.handleEvent(this, new EventObjectEntered ());
            } else if (curDistance < MIN_DISTANCE)
            {
                moveToTouchedState ();
                logger.info ("Generating EventObjectTouched");
                this.eventListener.handleEvent(this, new EventObjectTouched ());
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
                this.eventListener.handleEvent(this, new EventObjectExited ());
                // Send EventObjectExited                
            } else if (curDistance < MIN_DISTANCE) 
            {
                moveToTouchedState ();
                // Send EventObjectTouched
                logger.info ("Generating EventObjectTouched");
                this.eventListener.handleEvent(this, new EventObjectTouched ());
            } else
            {
                int diff = this.prevDistance - this.curDistance;
                diff = Math.abs (diff);
                if (diff >= this.threshold)
                {
                    this.prevDistance = this.curDistance;
                    logger.info ("Generating EventObjectMoved");
                    this.eventListener.handleEvent(this, new EventObjectMoved ());
                } else
                {
                    logger.info ("No Movement is observed. Threshold="+this.threshold + " prevDistance="+this.prevDistance+" curDistance="+this.curDistance);
                }
            }
        } else if (state.equals(State.STATE_TOUCHED))
        {
            logger.info ("Current State is TOUCHED");
            if (curDistance >= MAX_DISTANCE)
            {
                logger.info ("State is changed to IDLE");
                moveToIdleState ();
                this.eventListener.handleEvent(this, new EventObjectExited ());
            } else if (curDistance >= MIN_DISTANCE)
            {
                logger.info ("Movement is observed");
                logger.info ("Generating EventObjectMoved");
                moveToSensingState ();
                this.prevDistance = curDistance;
                this.eventListener.handleEvent(this, new EventObjectMoved ());
            }     
        }
    }
    
    private void moveToIdleState ()
    {
        logger.info ("State is changed to IDLE");
        this.state = State.STATE_IDLE;
    }
    private void moveToSensingState ()
    {
        this.prevDistance = this.curDistance;  
        logger.info ("State is changed to SENSING");
        this.state = State.STATE_SENSING;
    }
    private void moveToTouchedState ()
    {
        logger.info ("State is changed to TOUCHED");
        this.state = State.STATE_TOUCHED;
    }
    
    
}
