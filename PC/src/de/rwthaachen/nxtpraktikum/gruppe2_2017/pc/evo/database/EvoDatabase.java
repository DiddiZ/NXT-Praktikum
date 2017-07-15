package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo.database;

import java.io.IOException;
import java.util.List;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo.Measurements;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo.PIDWeights;

/**
 * Common interface to store and search evo data.
 *
 * @author Robin
 */
public interface EvoDatabase
{
	public void addData(PIDWeights pidValues, Measurements measurements) throws IOException;

	public List<Measurements> getMeasurements(PIDWeights lowerBound, PIDWeights upperBound) throws IOException;
}
