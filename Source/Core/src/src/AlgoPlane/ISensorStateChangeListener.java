/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoPlane;

/**
 *
 * @author vikumar
 */
public interface ISensorStateChangeListener {
    public void handleSensorStateChangeInd (String sensorId, boolean isActive);
}
