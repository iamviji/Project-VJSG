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
public  interface  IParallelParkingTestStateChangeListener  {
    
    
    public void handleEventStateChange (ParallelParkingState curState, ParallelParkingState prevState);
    public void handleWarning (ParallelParkingState state, ParallelParkingWarning warn);
    public void handlePassInd();
    public void handleFailInd(ParallelParkingState state, FailReason reason);
    
    /*
            private static Logger logger = DataBase.logger;
    {
    logger.info ("DefaultEventHandler: state="+curState+" "+prevState);
    }
    {
        logger.info ("DefaultEventHandler: state="+state+" warn="+warn);
    }
    {
        logger.info ("DefaultEventHandler: pass");
    }
    {
        logger.info ("DefaultEventHandler: fail state="+state+" reason="+reason);
    }*/
}
