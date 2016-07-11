/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AutoCarTestCoreMain;

/**
 *
 * @author vikumar
 */
public interface IAutoCarTestCoreService {
    public void startTest (int vehicleLenght, int vehicleWidth);
    public void stopTest ();
    public void reset ();
    public void registerServiceCallBack (IAutoCarTestServiceCallBack callBack);
    public void run ();
    public void handleMsg (String str);
    public void giveTimerTick ();
    public void configure (String xmlPath);
}
