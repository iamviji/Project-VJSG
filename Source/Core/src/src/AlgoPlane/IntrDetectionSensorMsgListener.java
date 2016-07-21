/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoPlane;

import AlgoPlane.EventObjectIn;
import AlgoPlane.EventObjectOut;
import Common.DataBase;
import java.util.logging.Logger;

/**
 *
 * @author vikumar
 */
public class IntrDetectionSensorMsgListener extends SensorMsgListener {
    private static Logger logger = DataBase.logger;
    enum State 
    {
        STATE_IDLE,
        STATE_SENSED,
    };

    State state;
    int threshold;
    public IntrDetectionSensorMsgListener (IEventListener eventListener, String name, int threshold)
    {
        super (eventListener, name);
        this.reset ();
        this.threshold = threshold;
    }
    protected void reset ()
    {
            super.reInit();
            this.moveToIdleState ();
    }
    public int getCurDistance ()
    {
        return -1;
    }
    public void handleSensorMsg (String msg)
    {
        String str [] = msg.split (":");
                    
        int curDistance = Integer.parseInt (str [2]);
        
        if (state.equals (State.STATE_IDLE))
        {
            logger.info ("Curent state is IDLE");
            if (curDistance < threshold)
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
            if (curDistance >= threshold)
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
