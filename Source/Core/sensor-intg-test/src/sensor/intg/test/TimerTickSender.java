/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sensor.intg.test;
import java.io.*;
import java.net.*;
import java.util.*;
/**
 *
 * @author vikumar
 */
public class TimerTickSender extends TimerTask{
  private DatagramSocket clientSocket;
  private DatagramPacket sendPacket;
  public void init (int port)
  {
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
         System.err.println("Could not open timer udp"); 
         System.exit(1); 
        } 
  }
  public void run ()
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
}
