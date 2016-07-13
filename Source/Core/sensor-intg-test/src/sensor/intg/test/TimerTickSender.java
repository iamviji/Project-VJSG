/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sensor.intg.test;
import java.io.*;
import java.net.*;
import java.util.*;
import AutoCarTestCoreMain.*;
/**
 *
 * @author vikumar
 */
public class TimerTickSender extends TimerTask{
  IAutoCarTestCoreService service;
  public  TimerTickSender (IAutoCarTestCoreService service)
  {
      this.service = service;
  }
  public void run ()
  {
      service.giveTimerTick(); 
  }
}
