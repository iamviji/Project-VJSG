/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Common;

import java.util.logging.Logger;

/**
 *
 * @author vikumar
 */
public class DataBase {

  public static int INTR_DETECTION_SIDE_THRESHOLD =200;     /* in cm*/
  public static int INTR_DETECTION_GATE_THRESHOLD =100;     /* in cm*/

  public static int PP_PROXIMITY_TOUCH_THRESHOLD = 5;       /*in cm*/
  public static int PP_PROXIMITY_SENSE_THRESHOLD = 300;
  public static int PP_PROXIMITY_MOVE_THRESHOLD = 5; 
  public static int PP_PROXIMITY_SPURIOUS_MOVE_THRESHOLD = 20;  
  public static int PP_PARK_AREA_LENGTH = 700;  /* in cm */
  public static int PP_PARKING_ENTRY_TIMEOUT = 10;  
  public static int PP_NO_OF_PARKING_ENTRY_WARNING = 3;
  public static int PP_PARKING_TIMEOUT = 10;
  public static int PP_NO_OF_PARKING_WARNING = 3;
  public static int PP_PARKING_IDLE_TIMEOUT = 10;
  public static int PP_PARKING_EXIT_TIMEOUT = 10;
  public static int PP_NO_OF_PARKING_EXIT_WARNING = 3;
  public static int PP_VERIFY_ANGLE = 50;
  public static int PP_VEHICLE_MIN_LENGTH=300;
    
  public static int TIMER_RESOLUTION = 1;   /*timer tick interval */
  
  
  public static Logger logger = Logger.getLogger ("AutoCarTestLogger");
  

  public DataBase (String xmlFilePath)
  {
      //TODO : Read XML File and Populate initial  Values

  }
  public void ConfigureBasedOnVehicleType ()
  {
  }
}
