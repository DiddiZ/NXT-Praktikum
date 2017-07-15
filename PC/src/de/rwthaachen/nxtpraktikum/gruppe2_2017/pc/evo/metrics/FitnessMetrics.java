package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo.metrics;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo.Measurements;

public enum FitnessMetrics implements FitnessMetric {
	LINEAR {
		@Override
		public double getFitness(Measurements measurements) {
			return measurements.averageDistanceDifference + measurements.averageHeadingDifference;
		}
	};
}
