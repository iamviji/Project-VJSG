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
