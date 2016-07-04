/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autocartestcoretest;
import AutoCarTestCore.*;
import java.io.*;
import java.util.logging.*;
/**
 *
 * @author vikumar
 */
public class AutoCarTestCoreTest {
    private static Logger logger = Logger.getLogger ("AutoCarTestLogger");

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException 
    {
        Handler[] handlers = logger.getHandlers();
        for(Handler handler : handlers)
        {
        if(handler.getClass() == ConsoleHandler.class || handler.getClass() == FileHandler.class)
            logger.removeHandler(handler);
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
        logger.addHandler(ch);

        logger.setLevel(Level.FINER);
        
        SampleClass s = new SampleClass ();
        s.print ();
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
                algoPlane.handleSensorMsg(line);
            }
        } 
        finally 
        {
            
        }
    }
    
}
