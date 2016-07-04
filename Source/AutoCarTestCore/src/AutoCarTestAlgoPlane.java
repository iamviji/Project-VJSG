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
    IntrDetectionSensorMsgListener ppEntry;
    ProximitySensorMsgListener ppStop;
    ProximitySensorMsgListener ppRearLeft;
    ProximitySensorMsgListener ppRearRight;
    ProximitySensorMsgListener ppFrontLeft;
    ProximitySensorMsgListener ppFrontRight;
    ProximitySensorMsgListener ppSide;
       
    ParallelParkingEventListener ppEventListener;
    
    ParallelParkingTestStateChangeListener ppTestStChngListener;
    ISensorMsgDispatcher sensorMsgDispatcher;
    public AutoCarTestAlgoPlane ()
    {
        ppEntry = new IntrDetectionSensorMsgListener  (ppEventListener);
        ppFrontRight = new ProximitySensorMsgListener (ppEventListener, 100);
        
        sensorMsgDispatcher = new SensorMsgDispatcher ();
        sensorMsgDispatcher.registerListener ("P.En", ppEntry);
        sensorMsgDispatcher.registerListener ("P.FR", ppFrontRight);
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
