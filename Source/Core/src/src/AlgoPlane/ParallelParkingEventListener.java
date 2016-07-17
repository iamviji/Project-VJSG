/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoPlane;

import AlgoPlane.EventObjectTouched;
import AlgoPlane.EventObjectEntered;
import AlgoPlane.EventObjectIn;
import AlgoPlane.EventObjectMoved;
import Common.DataBase;
import java.util.logging.Logger;

/**
 *
 * @author vikumar
 */
public class ParallelParkingEventListener implements IEventListener, ITimeOutEventListener {
    private static Logger logger = DataBase.logger;
    Timer timer;
    Timer idleTimer;
    IEventDispatcher stopSensor;
    IEventDispatcher rearLeftSensor;
    IEventDispatcher rearRightSnesor;
    IEventDispatcher frontLeftSensor;
    IEventDispatcher frontRightSensor;
    IEventDispatcher sideSensor;
    
        
    ParallelParkingState state;
    IParallelParkingTestStateChangeListener stateChngListener;
    int timerCount;
    boolean activate;
    public void setAllSensor (IEventDispatcher stopSensor,
            IEventDispatcher rearLeftSensor,
            IEventDispatcher rearRightSensor,
            IEventDispatcher frontLeftSensor,
            IEventDispatcher frontRightSensor,
            IEventDispatcher sideSensor)
    {
        this.timer = new Timer (this, "P.StateMachine.Timer");
        this.idleTimer = new Timer (this, "P.StateMachine.IdleTimer");
        this.stopSensor         = stopSensor;
        this.rearLeftSensor     = rearLeftSensor;
        this.rearRightSnesor    = rearRightSensor;
        this.frontLeftSensor    = frontLeftSensor;
        this.frontRightSensor   = frontRightSensor;
        this.sideSensor         = sideSensor;              
        this.state = ParallelParkingState.STATE_IDLE;     
        this.timerCount = 0;
        this.activate = true;
    }
    public void reset ()
    {
        this.timer.stop ();
        this.idleTimer.stop ();
        this.state = ParallelParkingState.STATE_IDLE;     
        this.timerCount = 0;
        this.activate = false;
    }
    public void registerParallelParkingTestStateChangeListener (IParallelParkingTestStateChangeListener listener)
    {
        this.stateChngListener = listener;
    }
    public void handleEvent (IEventDispatcher src, Event event)
    {
        if (this.activate == false)
        {
            logger.warning("Event is ignored since not activated");
            return;
        }
        
        logger.info ("Entry , Src = " + src + " Event = "+event + " State="+this.state);
        if (state.equals (ParallelParkingState.STATE_IDLE))
        {
            logger.info ("Current State is IDLE");
            if (event instanceof EventObjectIn && src == this.stopSensor)
            {
                print ("Vehicle arrived at Parallel Parking Stop Gate");
                moveToStateAtGate ();                 
            } else
            {
                logger.warning ("Event is not handled: PP_WARN_UNHANDLED"+event);
                stateChngListener.handleWarning (ParallelParkingState.STATE_IDLE,ParallelParkingWarning.PP_WARN_UNHANDLED);
            }
        } else if (this.state.equals(ParallelParkingState.STATE_AT_GATE))
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
                stateChngListener.handleFailInd (ParallelParkingState.STATE_AT_GATE, FailReason.FAIL_REASON_TOUCH);
                print ("Object Touched so failed");
            } else
            {
                logger.warning ("Event is not handled PP_WARN_UNHANDLED"+event);
                stateChngListener.handleWarning (ParallelParkingState.STATE_AT_GATE,ParallelParkingWarning.PP_WARN_UNHANDLED);

            }
        } else if (this.state.equals(ParallelParkingState.STATE_PARKING))
        {
            logger.info ("Current State is STATE_PARKING");
            if ((src == this.frontRightSensor || src == this.frontLeftSensor || src == this.rearLeftSensor ||
                    src == this.rearRightSnesor) &&
                    (event instanceof EventObjectMoved || event instanceof EventObjectEntered ))
            {
                print ("PP_WARN_MOVEMENT_INSIDE : Movement inside Parking Area"); 
                this.idleTimer.start (DataBase.PP_PARKING_IDLE_TIMEOUT);
                stateChngListener.handleWarning (ParallelParkingState.STATE_PARKING,ParallelParkingWarning.PP_WARN_MOVEMENT_INSIDE);
            } else if ((src == this.sideSensor && event instanceof EventObjectIn)
                    ||
                    (event instanceof EventObjectTouched)
                    )
            {
                    moveToStateFailed ();
                    print ("Object Touched so failed");
                    stateChngListener.handleFailInd (ParallelParkingState.STATE_PARKING, FailReason.FAIL_REASON_TOUCH);
            } else
            {
                logger.warning ("Event is not handled PP_WARN_UNHANDLED"+event);
                stateChngListener.handleWarning (ParallelParkingState.STATE_PARKING,ParallelParkingWarning.PP_WARN_UNHANDLED);
            }
        } else if (this.state.equals(ParallelParkingState.STATE_PARKED))
        {
            logger.info ("Current State is STATE_PARKED");
            if ( (src == this.sideSensor && event instanceof EventObjectIn)
                    ||
                    (event instanceof EventObjectTouched)
                    )
            {
                    moveToStateFailed ();
                    print ("Object Touched so failed");
                    stateChngListener.handleFailInd (ParallelParkingState.STATE_PARKED, FailReason.FAIL_REASON_TOUCH);
            } else if (src == this.stopSensor && event instanceof EventObjectIn)
            {
                moveToStatePassed ();                
            } else
            {
                logger.warning ("Event is not handled PP_WARN_UNHANDLED"+event);
                stateChngListener.handleWarning (ParallelParkingState.STATE_PARKED,ParallelParkingWarning.PP_WARN_UNHANDLED);
            }
        } else
        {
            logger.warning ("Unhandled event-invalid state"+event);
    
        }
    }
    public void handleTimeoutEvent (Timer timer)
    {
        if (this.activate == false)
        {
            logger.warning("Time Out Event is ignored since not activated");
            return;
        }
        logger.info ("Currest state =" + this.state.name());
        logger.info ("TimetOut Event");
        if (this.state.equals (ParallelParkingState.STATE_AT_GATE))
        {
            if (timer == this.timer)
            {
                if (this.timerCount < DataBase.PP_NO_OF_PARKING_ENTRY_WARNING)
                {
                    print ("Warning : PP_WARN_ENTRY : Park as early as possible");
                    this.timer.start (DataBase.PP_PARKING_ENTRY_TIMEOUT);
                    this.timerCount++;
                    stateChngListener.handleWarning (ParallelParkingState.STATE_AT_GATE,ParallelParkingWarning.PP_WARN_ENTRY);
                } else
                {
                    moveToStateFailed ();
                    System.out.println ("Testing Failed due to Parking entry timeout");
                    stateChngListener.handleFailInd (ParallelParkingState.STATE_AT_GATE, FailReason.FAIL_REASON_TIMEOUT);
                }
            } else
            {
                logger.warning ("Event is not handled PP_WARN_UNHANDLED"+timer);
                stateChngListener.handleWarning (ParallelParkingState.STATE_AT_GATE,ParallelParkingWarning.PP_WARN_UNHANDLED);
            }
        } else if (this.state.equals (ParallelParkingState.STATE_PARKING))
        {
            if (timer == this.idleTimer)
            {
                print ("Parked successfully. Verifying wait");
                logger.info ("Gap FR:" + this.frontRightSensor.getCurDistance()+" FL:"+this.frontLeftSensor.getCurDistance()
                +" RR:"+this.rearRightSnesor.getCurDistance()+" RL:"+this.rearLeftSensor.getCurDistance()
                );
                
                int frGap = 0;//this.frontRightSensor.getCurDistance();
                int flGap = 0;//this.frontRightSensor.getCurDistance();
                int rrGap = this.rearRightSnesor.getCurDistance();
                int rlGap = this.rearLeftSensor.getCurDistance();
                
                int frAngle = 0;// Math.abs(frGap-flGap);
                int rrAngle = Math.abs(rrGap-rlGap);
                int vehicleLength = DataBase.PP_PARK_AREA_LENGTH - frGap - rrGap; 
                
                if (rrAngle < DataBase.PP_VERIFY_ANGLE && vehicleLength > DataBase.PP_VEHICLE_MIN_LENGTH)
                //if (rrGap < INTR_DETECTION_SIDE_THRESHOLD_2 && rlGap < )
                {
                    moveToStateParked ();
                    print ("Parking Complete. Move to next test angle="+rrAngle+" vehicle length="+vehicleLength);
                } else
                {
                    moveToStateFailed ();
                    print ("Parking is not accurate failed");
                    stateChngListener.handleFailInd (ParallelParkingState.STATE_PARKING, FailReason.FAIL_REASON_WRONG_ALIGNEMENT);
                }
            } else if (timer == this.timer)
            {
                logger.info ("parking timer");
                if (this.timerCount < DataBase.PP_NO_OF_PARKING_WARNING)
                {
                    print ("Warning : PP_WARN_PARK: Park as early as possible");
                    this.timer.start (DataBase.PP_PARKING_TIMEOUT);
                    this.timerCount++;
                    stateChngListener.handleWarning (ParallelParkingState.STATE_PARKING,ParallelParkingWarning.PP_WARN_PARK);
                } else
                {
                    moveToStateFailed ();
                    print ("Testing Failed due to parking timeout\n");
                    stateChngListener.handleFailInd (ParallelParkingState.STATE_PARKING, FailReason.FAIL_REASON_TIMEOUT);
                }
            }
        } else if (this.state.equals (ParallelParkingState.STATE_PARKED))
        {
            if (this.timerCount < DataBase.PP_NO_OF_PARKING_EXIT_WARNING)
            {
                print ("Warning: PP_WARN_EXIT : Exit as early as possible");
                this.timer.start (DataBase.PP_PARKING_EXIT_TIMEOUT);
                this.timerCount++;
                stateChngListener.handleWarning (ParallelParkingState.STATE_PARKED,ParallelParkingWarning.PP_WARN_EXIT);
            } else
            {
                moveToStateFailed ();
                System.out.println ("Testing Failed due to Parking exit timeout");
                stateChngListener.handleFailInd (ParallelParkingState.STATE_PARKED, FailReason.FAIL_REASON_TIMEOUT);
            } 
        } else
        {
            logger.warning ("Unhandled event: invalid state"+timer);
        }
    }
    
    private void  moveToStateAtGate ()
    {
        ParallelParkingState prevState = state;
        this.state = ParallelParkingState.STATE_AT_GATE;
        logger.info ("State is changed to AT GATE");
        this.timer.start(DataBase.PP_PARKING_ENTRY_TIMEOUT);
        this.timerCount = 0;
        stateChngListener.handleEventStateChange(this.state, prevState);
    }
    private void  moveToStateParking ()
    {
        ParallelParkingState prevState = state;
        this.state = ParallelParkingState.STATE_PARKING;
        logger.info ("State is changed to PARKING");
        this.timerCount = 0;
        this.timer.start (DataBase.PP_PARKING_TIMEOUT);
        this.idleTimer.start(DataBase.PP_PARKING_IDLE_TIMEOUT);
        stateChngListener.handleEventStateChange(this.state, prevState);
    }
    private void moveToStateParked ()
    {
        ParallelParkingState prevState = state;
        this.state = ParallelParkingState.STATE_PARKED;
        logger.info ("State is changed to PARKED");
        this.timer.start(DataBase.PP_PARKING_EXIT_TIMEOUT);
        this.timerCount = 0;
        stateChngListener.handleEventStateChange(this.state, prevState);
    }
    private void moveToStateFailed ()
    {
        ParallelParkingState prevState = state;
        this.state = ParallelParkingState.STATE_FAILED;
        logger.info ("State is changed to FAILED");
        this.timer.stop();
        this.idleTimer.stop ();
        print ("Parallel Parking is finished : FAIL");
    }
   private void moveToStatePassed ()
    {
        ParallelParkingState prevState = state;
        this.state = ParallelParkingState.STATE_PASSED;
        logger.info ("State is changed to PASSED");
        this.timer.stop();
        this.idleTimer.stop ();
        print ("Parallel Parking is finished : PASS");
        stateChngListener.handlePassInd();
    } 
    private void print (String str)
    {
        //System.out.println (str);
        logger.info (str);
    }
    
}
