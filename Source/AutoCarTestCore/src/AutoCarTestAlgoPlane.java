/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AutoCarTestCore;
import java.util.logging.*;
/**
 *
 * @author vikumar
 */
public class AutoCarTestAlgoPlane {
    public static int TIMER_RESOLUTION = 100;
    private static Logger logger = Logger.getLogger ("AutoCarTestLogger");
    IntrDetectionSensorMsgListener ppEntrySensor;
    ProximitySensorMsgListener ppStopSensor;
    ProximitySensorMsgListener ppRearLeftSensor;
    ProximitySensorMsgListener ppRearRightSensor;
    ProximitySensorMsgListener ppFrontLeftSensor;
    ProximitySensorMsgListener ppFrontRightSensor;
    ProximitySensorMsgListener ppSideSensor;
       
    ParallelParkingEventListener ppEventListener;
    
    ParallelParkingTestStateChangeListener ppTestStChngListener;
    ISensorMsgDispatcher sensorMsgDispatcher;
    public AutoCarTestAlgoPlane ()
    {
        ppEventListener  = new ParallelParkingEventListener 
                (
                 ppEntrySensor, ppRearLeftSensor, ppRearRightSensor, 
                 ppFrontLeftSensor, ppFrontRightSensor, ppSideSensor                        
                );
                
        ppEntrySensor = new IntrDetectionSensorMsgListener  (ppEventListener);
        ppFrontRightSensor = new ProximitySensorMsgListener (ppEventListener, 100);
        
        sensorMsgDispatcher = new SensorMsgDispatcher ();
        sensorMsgDispatcher.registerListener ("P.En", ppEntrySensor);
        sensorMsgDispatcher.registerListener ("P.FR", ppFrontRightSensor);
        
        
    }
    public void handleSensorMsg (String msg)
    {
        logger.info (msg);
        sensorMsgDispatcher.handleMsg (msg);
    }
    public void reset (){
    }
    
    public void registerParallelParkingStateChangeListener (ParallelParkingTestStateChangeListener ppTestStChngListener)
    {
        this.ppTestStChngListener = ppTestStChngListener;
    }
}
