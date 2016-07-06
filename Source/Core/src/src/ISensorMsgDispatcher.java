/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AutoCarTestCore;
/**
 *
 * @author vikumar
 */
public interface ISensorMsgDispatcher {
    public void registerListener(String sensorId, ISensorMsgListener eventListener);
    public void handleMsg (String msg);   
}
