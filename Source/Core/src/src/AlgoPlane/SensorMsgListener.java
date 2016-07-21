/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoPlane;

import Common.DataBase;
import java.util.logging.Logger;

/**
 *
 * @author vikumar
 */
public abstract class SensorMsgListener implements ISensorMsgListener, IEventDispatcher, ITimeOutEventListener {
private static Logger logger = DataBase.logger;
public String name;

private static final int IDLE_TIMER = 10000;
//protected int timerTick;
//protected int internalTimerTick;
//protected int sensorMsgFreq;
protected IEventListener eventListener;
private ISensorStateChangeListener stChgListener;
enum State
{
     SENSOR_STATE_IDLE,
     SENSOR_STATE_ACTIVE;
};
Timer timer;
protected State state;
abstract protected void reset ();
protected void reInit ()
{
    this.state = State.SENSOR_STATE_IDLE;    
}
public SensorMsgListener (IEventListener eventListener, String name)
{
     //this.timerTick = 0;
     //this.internalTimerTick = 0;
     this.eventListener = eventListener;
     this.state = State.SENSOR_STATE_IDLE;
     this.name = name;
     timer = new Timer (this, "Sensor.Health."+name);
}
abstract public void handleSensorMsg (String msgId);

public void handleMsg (String msg)
{
    if (state.equals(State.SENSOR_STATE_IDLE))
    {
        this.timer.start(IDLE_TIMER);
        this.state = State.SENSOR_STATE_ACTIVE;
        
        stChgListener.handleSensorStateChangeInd (this.name, true);
        //eventListener.handleEvent(this, new EventSensorActiveInd ());
        
    } else if (state.equals(State.SENSOR_STATE_ACTIVE))
    {
        this.timer.start(IDLE_TIMER);       
    }
    this.handleSensorMsg (msg);
}
    public void handleTimeoutEvent (Timer timer)
    {
        //this.reset();
        this.state = State.SENSOR_STATE_IDLE;
        stChgListener.handleSensorStateChangeInd (this.name, false);
        //eventListener.handleEvent(this, new EventSensorDeActiveInd ());
    }
    public void registerSensorStateChangeListener (ISensorStateChangeListener stChgListener)
    {
        this.stChgListener = stChgListener;
    }
}

 