/**
 * This interface is used to register callback methods in the PCCom class.
 * A function needs to implement this interface to be able to register methods in PCCom
 *
 * @author Gregor
 */

package de.rwthaachen.nxtpraktikum.gruppe2_2017.comm;

/**
 * @author Gregor & Justus
 * 
 * This class inherites type definitions 
 */

import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author DiddiZ
 */
public interface CommandHandler
{
	void handle(DataInputStream is) throws IOException;
}
