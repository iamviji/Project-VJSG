/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sensor.intg.test;
import AutoCarTestCoreMain.TimerTickSender;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;


import Common.DataBase;
import java.io.*;
import java.util.logging.*;
import java.lang.*;
import java.util.concurrent.TimeUnit;
import AutoCarTestCoreMain.*;
/**
 *
 * @author vikumar
 */
public class SensorIntgTest {
private static Logger logger = DataBase.logger;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        int port = 6000;
        DummyAppl apl = new DummyAppl ();
        AutoCarTestCoreMain core = new AutoCarTestCoreMain (port, apl, apl, "AutoCarTestLog.txt", "DataLog.Txt");
        //TimerTickSender ttSender = new TimerTickSender (core);
        
        
        core.run ();
         
    }
}
