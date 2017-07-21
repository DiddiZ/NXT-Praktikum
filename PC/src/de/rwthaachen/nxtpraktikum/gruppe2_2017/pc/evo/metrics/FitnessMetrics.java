package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo.metrics;

import de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo.Measurements;

public enum FitnessMetrics implements FitnessMetric {
	LINEAR {
		@Override
		public double getFitness(Measurements measurements) {
			if (measurements.hasFallen())
				return measurements.time;
			return 100 - measurements.averageDistanceDifference - measurements.averageHeadingDifference;
		}
	},
	
	LINEAR2 {
		@Override
		public double getFitness(Measurements measurements) {
			if (measurements.hasFallen())
				return measurements.time;
			return 25 + 75 / (measurements.averageDistanceDifference + measurements.averageHeadingDifference);
		}
	}
	
}
