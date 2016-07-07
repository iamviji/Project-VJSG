/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sensor.intg.test;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import AutoCarTestCore.*;
import java.io.*;
import java.util.logging.*;
import java.lang.*;
import java.util.concurrent.TimeUnit;
/**
 *
 * @author vikumar
 */
public class SensorIntgTest {
private static Logger logger = Logger.getLogger ("AutoCarTestLogger");
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        
        int port = 6000;
        
        logger.setUseParentHandlers(false);
        Handler[] handlers = logger.getHandlers();
        for(Handler handler : handlers)
        {
            logger.removeHandler(handler);
            System.out.println ("removing handler");
        }
        try
        {
        // file handler
        FileHandler fh = new FileHandler("AutoCarTestLog.txt");
        //fh.setFormatter(new MS2Formatter());
         fh.setFormatter(new java.util.logging.Formatter() 
            {
                public String format(LogRecord record) 
                {
                    return record.getLevel() + "  :  "
                    + record.getSourceClassName() + " -:- "
                    + record.getSourceMethodName() + " -:- "
                    + record.getMessage() + "\n";
                }
            }
                     );
        logger.addHandler(fh);
        }
        catch (IOException e) 
        { 
         System.err.println("Could not do file handler"); 
         System.exit(1); 
        }
        // console handler
        ConsoleHandler ch = new ConsoleHandler();
        //ch.setFormatter(new MS2Formatter());
        //logger.addHandler(ch);

        logger.setLevel(Level.FINER);
        
        /* Timer Test */
        
        TimerManager.resolution = 2000;
        
        
        TimerTickSender ttSender = new TimerTickSender ();
        ttSender.init(port);
        java.util.Timer timer = new java.util.Timer ();
        timer.schedule(ttSender, 1000,1000);
        try
        {
            System.out.println("xxx");
            DatagramSocket serverSocket = new DatagramSocket(port);
            
            byte[] sendData = new byte[1024];
            AutoCarTestAlgoPlane algoPlane = new AutoCarTestAlgoPlane ();
            while(true)
            {
                  byte[] receiveData = new byte[1024]; 
                  //Arrays.fill( receiveData, (byte) 0 );
                  DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                  serverSocket.receive(receivePacket);
                  String sentence = new String(receiveData, 0, receivePacket.getLength());
                  System.out.println("RECEIVED: " + sentence);
                  
                logger.info ("Reading Line : " + sentence);
                String str [] = sentence.split (":");
                if (str[0].equals("TT"))
                {
                    System.out.println("giving tick");
                    TimerManager.giveTick();
                } else if (str[0].equals("DI"))
                {
                    System.out.println("handling msg");
                    algoPlane.handleSensorMsg(sentence);
                } else
                {
                    System.out.println ("unhandled"+str[0]);
                }
                
            }
        }
        catch (IOException e) 
        { 
         System.err.println("Could not listen on port"); 
         System.exit(1); 
        } 
    }
}
