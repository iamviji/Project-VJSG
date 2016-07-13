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
public interface IAutoCarTestServiceCallBack {
    public void testStartStatusIndication (int result);
    public void testWarningIndication (int warningId);
    public void testFinishIndication (int result, String sensorDataFilePath, String logFilePath);
    public void testStopedIndication ();
}
