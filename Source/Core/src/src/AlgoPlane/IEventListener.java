/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoPlane;

import AlgoPlane.Event;

/**
 *
 * @author vikumar
 */
public interface IEventListener {
    public void handleEvent (IEventDispatcher src, Event event);
}
