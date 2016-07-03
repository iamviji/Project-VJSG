/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autocartestcoretest;
import AutoCarTestCore.*;
import java.io.*;
/**
 *
 * @author vikumar
 */
public class AutoCarTestCoreTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException 
    {
        // TODO code application logic here
        SampleClass s = new SampleClass ();
        s.print ();
        FileInputStream in = null;
        
        try 
        {
            String filename = args [0];
            in = new FileInputStream (filename);
        } 
        finally 
        {
            
        }
    }
    
}
