/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AutoCarTestCoreMain;

import java.io.IOException;

/**
 *
 * @author vikumar
 */
public interface IAutoCarTestCoreService {
    public void startTest (int vehicleLength, int vehicleWidth);
    public void stopTest () throws IOException;
    public void reset ();
    public void registerServiceCallBack (IAutoCarTestServiceCallBack callBack);
    public void run () throws IOException;
    public void handleMsg (String str);
    public void giveTimerTick ();
    public void configure (String xmlPath);
}
