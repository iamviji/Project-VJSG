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
public class ParallelParkingEventListener implements IEventListener {
    IEventDispatcher stopSensor;
    IEventDispatcher rearLeftSensor;
    IEventDispatcher rearRightSnesor;
    IEventDispatcher frontLeftSensor;
    IEventDispatcher frontRightSensor;
    IEventDispatcher sideSensor;
    
    enum State
    {
        STATE_IDLE,
        STATE_AT_STOP_GATE,
        STATE_PAKRING,
        STATE_PARKED,
        STATE_PARKED_ENOUGH,
        STATE_MOVING_OUT,
        STATE_MOVED_OUT,
        STATE_FINISHED,
        STEATE_FAILED,
    };
    public ParallelParkingEventListener (IEventDispatcher stopSensor,
            IEventDispatcher rearLeftSensor,
            IEventDispatcher rearRightSensor,
            IEventDispatcher frontLeftSensor,
            IEventDispatcher frontRightSensor,
            IEventDispatcher sideSensor)
    {
        this.stopSensor         = stopSensor;
        this.rearLeftSensor     = rearLeftSensor;
        this.rearRightSnesor    = rearRightSensor;
        this.frontLeftSensor    = frontLeftSensor;
        this.frontRightSensor   = frontRightSensor;
        this.sideSensor         = sideSensor;              
                
    }
    public void handleEvent (IEventDispatcher src, Event event)
    {
    
    }
}
