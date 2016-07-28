/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autodrivetest;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
/**
 *
 * @author SPoojary
 */
public class TestTrack extends javax.swing.JPanel {
    public TrackData trackData;
    public static int blockRatio = 8;
    public TestTrack() 
    {
        super();
        trackData = new TrackData();
    }
    
    @Override
    public void paintComponent(Graphics g){
        System.out.println("paintComponent: Printing track");
        super.paintComponent(g);  

        //g.setColor(Color.BLACK);  
        //g.fillRect(350, 350, dim.height, dim.width);

        for (int hpos = 0; hpos < TrackData.TRACK_HEIGHT; ++hpos) {
            for (int wpos = 0; wpos < TrackData.TRACK_WIDTH; ++wpos) {
                DrawBlock(g, hpos, wpos);
            }
        }     
    }
    
    public void DrawBlock(Graphics g, int hpos, int wpos)
    {
        int data = trackData.getData(hpos, wpos);
        int thpos = hpos * 8;// * blockRatio;
        int twpos = wpos * 8;// * blockRatio;
        Dimension dim = getSize();
        
        switch (data) {
            case TrackData.TRACK:
                g.setColor(Color.LIGHT_GRAY); 
                break;
            case TrackData.ROAD:
                g.setColor(Color.DARK_GRAY); 
                break;
            case TrackData.CAR:
                g.setColor(Color.RED); 
                break;                        
        }
        g.fillRect(twpos, thpos, dim.height, dim.width);        
    }
    
    public void moveCar(Graphics g){
        System.out.println("paintComponent: Printing track");
        super.paintComponent(g);
        int chpos = trackData.getCarCurrentPosH();
        int cwpos = trackData.getCarCurrentPosW();
        if (chpos < 0) chpos = 0;
        if (cwpos < 0) cwpos = 0;
        for (int hpos = chpos - 2; hpos < 6; ++hpos) {
            for (int wpos = cwpos - 2; wpos < 6; ++wpos) {
                DrawBlock(g, hpos, wpos);
            }
        }  
    }
    
    public Dimension getPreferredSize() {
        return new Dimension(blockRatio, blockRatio); // appropriate constants
    }
    
    public void moveCarLeft()
    {
        trackData.moveCarLeft();
    }
    public void moveCarRight()
    {
        trackData.moveCarRight();
    }
        public void moveCarUp()
    {
        trackData.moveCarUp();
    }
    public void moveCarBack()
    {
        trackData.moveCarBack();
    }
}
