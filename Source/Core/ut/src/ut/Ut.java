/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ut;
import Common.DataBase;
import java.io.*;
import java.util.logging.*;
import java.lang.*;
import java.util.concurrent.TimeUnit;
import AlgoPlane.*;
/**
 *
 * @author vikumar
 */
public class Ut {
 private static Logger logger = DataBase.logger;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException 
    {
          logger.setUseParentHandlers(false);
        Handler[] handlers = logger.getHandlers();
        for(Handler handler : handlers)
        {
            logger.removeHandler(handler);
            System.out.println ("removing handler");
        }
        // file handler
        FileHandler fh = new FileHandler("AutoCarTestLog.txt");
        //fh.setFormatter(new MS2Formatter());
         fh.setFormatter(new Formatter() 
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

        // console handler
        ConsoleHandler ch = new ConsoleHandler();
        //ch.setFormatter(new MS2Formatter());
        //logger.addHandler(ch);

        logger.setLevel(Level.FINER);
        
        /* Timer Test */
        
        /*
        TimerTester tester = new TimerTester ();
        Timer t1 = new Timer (tester);
        Timer t2 = new Timer (tester);
        t1.start (2000);
        t2.start (1000);
        TimerManager.giveTick();
        TimerManager.giveTick();
        TimerManager.giveTick();
        TimerManager.giveTick();
        t1.start (2000);
        TimerManager.giveTick();
        TimerManager.giveTick();
      */
  
        FileInputStream in = null;
        AutoCarTestAlgoPlane algoPlane = new AutoCarTestAlgoPlane ();
        
        try 
        {
            String filename = args [0];
            in = new FileInputStream (filename);
            BufferedReader br = new BufferedReader (new InputStreamReader (in));
            String line;
            while ((line = br.readLine ())!= null)
            {
                logger.info ("Reading Line : " + line);
                String str [] = line.split (":");
                if (str[0].equals("TT"))
                {
                    TimerManager.giveTick();
                } else if (str[0].equals("DI"))
                {
                    algoPlane.handleSensorMsg(line);
                }
                
                try 
                {
                    Thread.sleep(10);
                    //TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e)
                {
                     e.printStackTrace();
                }
                
            }
        } 
        finally 
        {
            
        }
    }
}
