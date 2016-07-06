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
public class ParallelParkingEventListener implements IEventListener, ITimeOutEventListener {
    private static Logger logger = Logger.getLogger ("AutoCarTestLogger");
    Timer timer;
    Timer idleTimer;
    IEventDispatcher stopSensor;
    IEventDispatcher rearLeftSensor;
    IEventDispatcher rearRightSnesor;
    IEventDispatcher frontLeftSensor;
    IEventDispatcher frontRightSensor;
    IEventDispatcher sideSensor;
    
    enum State
    {
        STATE_IDLE,
        STATE_AT_GATE,
        STATE_PARKING,
        STATE_PARKED,
        STATE_PASSED,
        STATE_FAILED,
    };
    
    State state;
    static final int PARK_AREA_LENGTH = 7000;
    static final int PARKING_ENTRY_TIMEOUT = 10000;
    static final int NO_OF_PARKING_ENTRY_WARNING = 3;
    static final int PARKING_TIMEOUT = 10000;
    static final int NO_OF_PARKING_WARNING = 5;
    static final int PARKING_IDLE_TIMEOUT = 10000;
    static final int PARKING_EXIT_TIMEOUT = 10000;
    static final int NO_OF_PARKING_EXIT_WARNING = 3;
    static final int VERIFY_ANGLE = 500;
    static final int VEHICLE_MIN_LENGTH=3000;
    int timerCount;
    public void setAllSensor (IEventDispatcher stopSensor,
            IEventDispatcher rearLeftSensor,
            IEventDispatcher rearRightSensor,
            IEventDispatcher frontLeftSensor,
            IEventDispatcher frontRightSensor,
            IEventDispatcher sideSensor)
    {
        this.timer = new Timer (this);
        this.idleTimer = new Timer (this);
        this.stopSensor         = stopSensor;
        this.rearLeftSensor     = rearLeftSensor;
        this.rearRightSnesor    = rearRightSensor;
        this.frontLeftSensor    = frontLeftSensor;
        this.frontRightSensor   = frontRightSensor;
        this.sideSensor         = sideSensor;              
        this.state = State.STATE_IDLE;     
        this.timerCount = 0;
    }
    public void handleEvent (IEventDispatcher src, Event event)
    {
        logger.info ("Entry, Src = " + src + " Event = "+event);
        if (state.equals (State.STATE_IDLE))
        {
            logger.info ("Current State is IDLE");
            if (event instanceof EventObjectIn && src == this.stopSensor)
            {
                print ("Vehicle arrived at Parallel Parking Stop Gate");
                moveToStateAtGate (); 
            } else
            {
                logger.warning ("Event is not handled");
            }
        } else if (this.state.equals(State.STATE_AT_GATE))
        {
            logger.info ("Current State is STATE_AT_GATE");
            if (event instanceof EventObjectEntered)
            {
                print ("Vehicle arrived inside parking area");
                moveToStateParking ();
            } else if ( (src == this.sideSensor && event instanceof EventObjectIn)
                    ||
                    (event instanceof EventObjectTouched)
                    )
            {
                moveToStateFailed ();
                print ("Object Touched so failed");
            } else
            {
                logger.warning ("Event is not handled");
            }
        } else if (this.state.equals(State.STATE_PARKING))
        {
            logger.info ("Current State is STATE_PARKING");
            if ((src == this.frontRightSensor || src == this.frontLeftSensor || src == this.rearLeftSensor ||
                    src == this.rearRightSnesor) &&
                    (event instanceof EventObjectMoved || event instanceof EventObjectEntered ))
            {
                print ("Movement inside Parking Area"); 
                this.idleTimer.start (PARKING_IDLE_TIMEOUT);
            } else if ((src == this.sideSensor && event instanceof EventObjectIn)
                    ||
                    (event instanceof EventObjectTouched)
                    )
            {
                    moveToStateFailed ();
                    print ("Object Touched so failed");
            } else
            {
                logger.warning ("Event is not handled");
            }
        } else if (this.state.equals(State.STATE_PARKED))
        {
            if ( (src == this.sideSensor && event instanceof EventObjectIn)
                    ||
                    (event instanceof EventObjectTouched)
                    )
            {
                    moveToStateFailed ();
                    print ("Object Touched so failed");
            } else if (src == this.stopSensor && event instanceof EventObjectIn)
            {
            }
        } else
        {
            logger.warning ("Unhandled event");
    
        }
    }
    public void handleTimeoutEvent (Timer timer)
    {
        logger.info ("Currest state =" + this.state.name());
        logger.info ("TimetOut Event");
        if (this.state.equals (State.STATE_AT_GATE))
        {
            if (timer == this.timer)
            {
                if (this.timerCount < NO_OF_PARKING_ENTRY_WARNING)
                {
                    print ("Warning : Park as early as possible");
                    this.timer.start (PARKING_ENTRY_TIMEOUT);
                    this.timerCount++;
                } else
                {
                    moveToStateFailed ();
                    System.out.println ("Testing Failed due to Parking entry timeout\n");
                }
            } else
            {
                logger.warning ("Unhandled");
            }
        } else if (this.state.equals (State.STATE_PARKING))
        {
            if (timer == this.idleTimer)
            {
                print ("Parked successfully. Verifying wait");
                logger.info ("Gap FR:" + this.frontRightSensor.getCurDistance()+" FL:"+this.frontLeftSensor.getCurDistance()
                +" RR:"+this.rearRightSnesor.getCurDistance()+" RL:"+this.rearLeftSensor.getCurDistance()
                );
                
                int frGap = this.frontRightSensor.getCurDistance();
                int flGap = this.frontRightSensor.getCurDistance();
                int rrGap = this.rearRightSnesor.getCurDistance();
                int rlGap = this.rearLeftSensor.getCurDistance();
                
                int frAngle = Math.abs(frGap-flGap);
                int rrAngle = Math.abs(rrGap-rlGap);
                int vehicleLength = PARK_AREA_LENGTH - frGap - rrGap; 
                
                if (rrAngle < VERIFY_ANGLE && vehicleLength > VEHICLE_MIN_LENGTH)
                {
                    moveToStateParked ();
                    print ("Parking Complete. Move to next test angle="+rrAngle+" vehicle length="+vehicleLength);
                } else
                {
                    moveToStateFailed ();
                    print ("Parking is not accurate failed");
                }
            } else if (timer == this.timer)
            {
                logger.info ("parking timer");
                if (this.timerCount < NO_OF_PARKING_WARNING)
                {
                    print ("Warning : Park as early as possible");
                    this.timer.start (PARKING_TIMEOUT);
                    this.timerCount++;
                } else
                {
                    moveToStateFailed ();
                    print ("Testing Failed due to parking timeout\n");
                }
            }
        } else if (this.state.equals (State.STATE_PARKED))
        {
            if (this.timerCount < NO_OF_PARKING_EXIT_WARNING)
            {
                print ("Warning : Exit as early as possible");
                this.timer.start (PARKING_EXIT_TIMEOUT);
                this.timerCount++;
            } else
            {
                moveToStateFailed ();
                System.out.println ("Testing Failed due to Parking exit timeout\n");
            }  
        } else
        {
            logger.warning ("Unhandled event");
        }
    }
    
    private void  moveToStateAtGate ()
    {
        this.state = State.STATE_AT_GATE;
        logger.info ("State is changed to AT GATE");
        this.timer.start(PARKING_ENTRY_TIMEOUT);
        this.timerCount = 0;
    }
    private void  moveToStateParking ()
    {
        this.state = State.STATE_PARKING;
        logger.info ("State is changed to PARKING");
        this.timerCount = 0;
        this.timer.start (PARKING_TIMEOUT);
        this.idleTimer.start(PARKING_IDLE_TIMEOUT);
    }
    private void moveToStateParked ()
    {
        this.state = State.STATE_PARKED;
        logger.info ("State is changed to PARKED");
        this.timer.start(PARKING_EXIT_TIMEOUT);
        this.timerCount = 0;
    }
    private void moveToStateFailed ()
    {
        this.state = State.STATE_FAILED;
        logger.info ("State is changed to FAILED");
        this.timer.stop();
        this.idleTimer.stop ();
    }
   private void moveToStatePassed ()
    {
        this.state = State.STATE_PASSED;
        logger.info ("State is changed to PASSED");
        this.timer.stop();
        this.idleTimer.stop ();
    } 
    private void print (String str)
    {
        System.out.println (str);
        logger.info (str);
    }
    
}
