package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo;

import java.io.IOException;

/**
 * Common interface to store and search evo data.
 *
 * @author Robin
 */
public interface EvoDatabase
{
	public void addData(PIDWeights pidValues, Measurements measurements) throws IOException;
}
