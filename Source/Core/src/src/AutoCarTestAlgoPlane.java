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
    public static int PROXIMITY_THRESHOLD = 200;
    public static int INTR_DETECTION_SIDE_THRESHOLD =3000; 
    public static int INTR_DETECTION_ENTRY_THRESHOLD =1500;
    private static Logger logger = Logger.getLogger ("AutoCarTestLogger");

    ParallelParkingEventListener ppEventListener = new ParallelParkingEventListener ();
    IntrDetectionSensorMsgListener ppEntrySensor;
    IntrDetectionSensorMsgListener ppStopSensor = new IntrDetectionSensorMsgListener  (ppEventListener);
    ProximitySensorMsgListener ppRearLeftSensor = new ProximitySensorMsgListener (ppEventListener, PROXIMITY_THRESHOLD);;
    ProximitySensorMsgListener ppRearRightSensor = new ProximitySensorMsgListener (ppEventListener, PROXIMITY_THRESHOLD);;
    ProximitySensorMsgListener ppFrontLeftSensor = new ProximitySensorMsgListener (ppEventListener, PROXIMITY_THRESHOLD);;
    ProximitySensorMsgListener ppFrontRightSensor = new ProximitySensorMsgListener (ppEventListener, PROXIMITY_THRESHOLD);
    IntrDetectionSensorMsgListener ppSideSensor = new IntrDetectionSensorMsgListener  (ppEventListener);
    

       
    ParallelParkingTestStateChangeListener ppTestStChngListener;
    ISensorMsgDispatcher sensorMsgDispatcher;
    public AutoCarTestAlgoPlane ()
    {

        ppEventListener.setAllSensor 
                (
                 ppStopSensor, ppRearLeftSensor, ppRearRightSensor, 
                 ppFrontLeftSensor, ppFrontRightSensor, ppSideSensor                        
                );
     
        sensorMsgDispatcher = new SensorMsgDispatcher ();
        sensorMsgDispatcher.registerListener ("P.GST", ppStopSensor);
        sensorMsgDispatcher.registerListener ("P.FR", ppFrontRightSensor);
        sensorMsgDispatcher.registerListener ("P.FL", ppFrontLeftSensor);
        sensorMsgDispatcher.registerListener ("P.RR", ppRearRightSensor);
        sensorMsgDispatcher.registerListener ("P.RL", ppRearLeftSensor);
        sensorMsgDispatcher.registerListener ("P.SD", ppSideSensor);
        
        ppSideSensor.detectionThreshold = INTR_DETECTION_SIDE_THRESHOLD;
        ppStopSensor.detectionThreshold = INTR_DETECTION_ENTRY_THRESHOLD;
        
    }
    public void handleSensorMsg (String msg)
    {
        logger.info (msg);
        msg = msg.replace ("\n","");
        sensorMsgDispatcher.handleMsg (msg);
    }
    public void reset (){
    }
    
    public void registerParallelParkingStateChangeListener (ParallelParkingTestStateChangeListener ppTestStChngListener)
    {
        this.ppTestStChngListener = ppTestStChngListener;
    }
}
