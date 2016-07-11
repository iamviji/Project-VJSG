/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AutoCarTestCoreMain;

import ControlPlane.*;
import AlgoPlane.*;
import Common.DataBase;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;



import java.net.*;
/**
 *
 * @author vikumar
 */
public class AutoCarTestCoreMain implements IAutoCarTestCoreService{
    private AutoCarTestCtrlPlane ctrlPlane;
    private AutoCarTestAlgoPlane algoPlane;
 
    private static Logger logger = DataBase.logger;
    private DatagramSocket clientSocket;
    private DatagramPacket sendPacket;
    private int port;
    
    public AutoCarTestCoreMain (int port, ParallelParkingTestStateChangeListener cb)
    {
        this.algoPlane = new AutoCarTestAlgoPlane ();
        this.ctrlPlane = new AutoCarTestCtrlPlane ();
        //this.algoPlane.registerParallelParkingStateChangeListener (this.ctrlPlane); For demo not required
        this.algoPlane.registerParallelParkingStateChangeListener(cb); // for demo let application listen it directly
        this.port = port;
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
        
        try
        {
            clientSocket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName("localhost");
            String sentence = "TT";
            byte[] sendData = sentence.getBytes();
            sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
        }
        catch (IOException e) 
        { 
            System.err.println("Could not open timer udp."); 
            System.exit(1); 
        }
    }
    public void startTest (int vehicleLength, int vehicleWidth)
    {}
    public void stopTest ()
    {}
    public void registerServiceCallBack (IAutoCarTestServiceCallBack callBack)
    {}
    public void run ()
    {
           System.out.println("xxx");
        try 
        {
            DatagramSocket serverSocket = new DatagramSocket(port);
            
            byte[] sendData = new byte[1024];

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
                    //System.out.println("giving tick");
                    TimerManager.giveTick();
                } else if (str[0].equals("DI"))
                {
                    //System.out.println("handling msg");
                    algoPlane.handleSensorMsg(sentence);
                } else
                {
                    System.out.println ("Unhandled"+str[0]);
                }
            }                        
        }
        catch (IOException e) 
        { 
            System.err.println("Could not open udp to receive"); 
            System.exit(1); 
        }
    }
    public void handleMsg (String str)
    {}
    public void giveTimerTick ()
    {
        try
        {
            clientSocket.send(sendPacket);
        }
        catch (IOException e) 
        { 
            System.err.println("Could not open timer udp"); 
            System.exit(1); 
        }
    }
   public void configure (String xmlPath)
   {}
   public void reset ()
   {}

}
