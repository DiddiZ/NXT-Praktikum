/**
 * This interface is used to register callback methods in the PCCom class.
 * A function needs to implement this interface to be able to register methods in PCCom
 *
 * @author Gregor
 */

package de.rwthaachen.nxtpraktikum.gruppe2_2017.ntx.comm;

public interface CallbackMethod
{
	void callback(float p_parameter);
}
