/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sensor.intg.test;
import AlgoPlane.*;
/**
 *
 * @author vikumar
 */
public class DummyAppl implements IParallelParkingTestStateChangeListener, ISensorStateChangeListener {
    public void handleEventStateChange (ParallelParkingState curState, ParallelParkingState prevState)
    {
        System.out.println ("Apl:handleStateChange curState="+curState + " prevState="+prevState);
    }  
    public void handleWarning (ParallelParkingState state, ParallelParkingWarning warn)
    {
        System.out.println ("Apl:handleWarning state="+state+" warn="+warn);
    }
        public void handlePassInd()
    {
        System.out.println ("Apl:handlePassInd");
    }
    public void handleFailInd(ParallelParkingState state, FailReason reason)
    {
        System.out.println ("Apl:handleFailInd state="+state+" reason="+reason);
    }
    public void handleSensorStateChangeInd (String str, boolean isActive)
    {
        System.out.println ("Apl:handleSensorStateChangeInd sensor="+str+" isActive="+isActive);
    }
}
