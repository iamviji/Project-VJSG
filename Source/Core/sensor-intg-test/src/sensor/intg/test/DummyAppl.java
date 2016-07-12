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
public class DummyAppl extends ParallelParkingTestStateChangeListener {
  public void handleEventAtStopSensor ()
   {
       System.out.println ("Apl:handleEventAtStopSensor");
   }  
}
