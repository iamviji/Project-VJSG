/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoPlane;
/**
 *
 * @author vikumar
 */
import Common.DataBase;
import java.util.*;
import java.util.logging.Logger;
public class SensorMsgDispatcher implements ISensorMsgDispatcher {
    private static Logger logger = DataBase.logger;
    HashMap<String, ISensorMsgListener> mapOfMsgListener;
    public SensorMsgDispatcher ()
    {
        mapOfMsgListener = new HashMap<String, ISensorMsgListener> ();
    }
    public void registerListener(String sensorId, ISensorMsgListener msgListener)
    {
        logger.info("Registering "+ sensorId);
        mapOfMsgListener.put (sensorId, msgListener);
              
    }
    public void handleMsg (String msg)
    {
        String str [] = msg.split (":");
        ISensorMsgListener msgListener = null;
        
        logger.info ("Sensor ID : "+ str [1]);
        if ((msgListener = mapOfMsgListener.get(str[1])) != null)
        {
            logger.info ("Dispatcher able to find Listener. ID : " + str[1]);
            msgListener.handleMsg(msg);
        } else
        {
           logger.warning("Dispatcher not able to find Listener. ID : " + str[1]);
        }
        
    }
    
}
