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
public  abstract class ParallelParkingTestStateChangeListener  {
   private static Logger logger = DataBase.logger;
   public void handleEventAtStopSensor ()
   {
       logger.info ("Default Event Handler");
   }
}
