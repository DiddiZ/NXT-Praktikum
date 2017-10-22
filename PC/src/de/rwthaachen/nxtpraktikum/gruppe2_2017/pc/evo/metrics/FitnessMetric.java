package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo.metrics;

import java.util.List;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo.Measurements;

/**
 * Provides a interface for fotness metrics
 * 
 * @author Robin
 */

public interface FitnessMetric
{
	public double getFitness(Measurements measurements);

	/**
	 * @return average fitness of all measurements.
	 */
	public static double getFitness(List<Measurements> measurements, FitnessMetric metric) {
		double sum = 0;
		for (final Measurements m : measurements) {
			sum += metric.getFitness(m);
		}
		return sum / measurements.size();
	}
}
