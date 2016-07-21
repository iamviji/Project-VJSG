/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoPlane;

import AlgoPlane.EventObjectTouched;
import AlgoPlane.EventObjectEntered;
import AlgoPlane.EventObjectMoved;
import AlgoPlane.EventObjectExited;
import java.util.logging.Logger;
import java.lang.Math.*;

import Common.DataBase;
/**
 *
 * @author vikumar
 */
public class ProximitySensorMsgListener extends SensorMsgListener {
    private static Logger logger = DataBase.logger;
    enum State 
    {
        STATE_IDLE,
        STATE_SENSING,
        STATE_TOUCHED,
    };


    State state;
    int prevDistance;
    int curDistance;
    public ProximitySensorMsgListener (IEventListener eventListener, String name)
    {
        super (eventListener, name);
        this.reset();
    }
    protected void reset ()
    {
            super.reInit();
            this.moveToIdleState ();
    }
    public int getCurDistance ()
    {
        return this.curDistance;
    }
    public void handleSensorMsg (String msg)
    {
        logger.info ("Entry->"+msg);
        String str [] = msg.split (":");
        this.curDistance = Integer.parseInt (str [2]);
        logger.info ("update cur distance="+this.curDistance);
        if (state.equals(State.STATE_IDLE))
        {
            logger.info ("Current State is IDLE");
            if (curDistance < DataBase.PP_PROXIMITY_SENSE_THRESHOLD && curDistance >= DataBase.PP_PROXIMITY_TOUCH_THRESHOLD)
            {
                moveToSensingState ();
                logger.info ("Generating EventObjectEntered");
                this.eventListener.handleEvent(this, new EventObjectEntered ());
            } else if (curDistance < DataBase.PP_PROXIMITY_TOUCH_THRESHOLD)
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
            if (curDistance >= DataBase.PP_PROXIMITY_SENSE_THRESHOLD)
            {
                moveToIdleState ();
                logger.info ("Generating EventObjectExited");
                this.eventListener.handleEvent(this, new EventObjectExited ());
            } else if (curDistance < DataBase.PP_PROXIMITY_TOUCH_THRESHOLD) 
            {
                moveToTouchedState ();
                // Send EventObjectTouched
                logger.info ("Generating EventObjectTouched");
                this.eventListener.handleEvent(this, new EventObjectTouched ());
            } else
            {
                int diff = this.prevDistance - this.curDistance;
                diff = Math.abs (diff);
                if (diff >= DataBase.PP_PROXIMITY_MOVE_THRESHOLD)
                {
                    this.prevDistance = this.curDistance;
                    logger.info ("Generating EventObjectMoved");
                    this.eventListener.handleEvent(this, new EventObjectMoved ());
                } else
                {
                    logger.info ("No Movement is observed. Threshold="+DataBase.PP_PROXIMITY_MOVE_THRESHOLD + " prevDistance="+this.prevDistance+" curDistance="+this.curDistance);
                }
            }
        } else if (state.equals(State.STATE_TOUCHED))
        {
            logger.info ("Current State is TOUCHED");
            if (curDistance >= DataBase.PP_PROXIMITY_SENSE_THRESHOLD)
            {
                logger.info ("State is changed to IDLE");
                moveToIdleState ();
                this.eventListener.handleEvent(this, new EventObjectExited ());
            } else if (curDistance >= DataBase.PP_PROXIMITY_TOUCH_THRESHOLD)
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
