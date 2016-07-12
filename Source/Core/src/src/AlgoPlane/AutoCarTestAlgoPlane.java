/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoPlane;
import Common.*;
import java.util.logging.*;
/**
 *
 * @author vikumar
 */
public class AutoCarTestAlgoPlane {
 
    private static Logger logger = DataBase.logger;
    ParallelParkingEventListener ppEventListener = new ParallelParkingEventListener ();
    IntrDetectionSensorMsgListener ppEntrySensor;
    IntrDetectionSensorMsgListener ppStopSensor = new IntrDetectionSensorMsgListener  (ppEventListener, "P.GST");
    ProximitySensorMsgListener ppRearLeftSensor = new ProximitySensorMsgListener (ppEventListener, "P.RL");
    ProximitySensorMsgListener ppRearRightSensor = new ProximitySensorMsgListener (ppEventListener, "P.RR");
    ProximitySensorMsgListener ppFrontLeftSensor = new ProximitySensorMsgListener (ppEventListener, "P.FL");
    ProximitySensorMsgListener ppFrontRightSensor = new ProximitySensorMsgListener (ppEventListener, "P.FR");
    IntrDetectionSensorMsgListener ppSideSensor = new IntrDetectionSensorMsgListener  (ppEventListener, "P.SD");;

       
    IParallelParkingTestStateChangeListener ppTestStChngListener;
    ISensorMsgDispatcher sensorMsgDispatcher;
    public AutoCarTestAlgoPlane ()
    {

        ppEventListener.setAllSensor 
                (
                 ppStopSensor, ppRearLeftSensor, ppRearRightSensor, 
                 ppFrontLeftSensor, ppFrontRightSensor, ppSideSensor                        
                );
     
        sensorMsgDispatcher = new SensorMsgDispatcher ();
        sensorMsgDispatcher.registerListener (ppStopSensor.name, ppStopSensor);
        sensorMsgDispatcher.registerListener (ppFrontRightSensor.name, ppFrontRightSensor);
        sensorMsgDispatcher.registerListener (ppFrontLeftSensor.name, ppFrontLeftSensor);
        sensorMsgDispatcher.registerListener (ppRearRightSensor.name, ppRearRightSensor);
        sensorMsgDispatcher.registerListener (ppRearLeftSensor.name, ppRearLeftSensor);
        sensorMsgDispatcher.registerListener (ppSideSensor.name, ppSideSensor);
        
        
    }
    public void handleSensorMsg (String msg)
    {
        logger.info (msg);
        msg = msg.replace ("\n","");
        sensorMsgDispatcher.handleMsg (msg);
    }
    
    public void reset (){
    }
    
    public void registerParallelParkingStateChangeListener (IParallelParkingTestStateChangeListener ppTestStChngListener)
    {
        this.ppTestStChngListener = ppTestStChngListener;
        this.ppEventListener.registerParallelParkingTestStateChangeListener(ppTestStChngListener);
    }
    public void registerSensorStateChangeListener (ISensorStateChangeListener stChgListener)
    {
        this.ppStopSensor.registerSensorStateChangeListener(stChgListener);
        this.ppFrontLeftSensor.registerSensorStateChangeListener(stChgListener);
        this.ppFrontRightSensor.registerSensorStateChangeListener(stChgListener);
        this.ppRearLeftSensor.registerSensorStateChangeListener(stChgListener);
        this.ppRearRightSensor.registerSensorStateChangeListener(stChgListener);
        this.ppSideSensor.registerSensorStateChangeListener(stChgListener);
    }
    
}
