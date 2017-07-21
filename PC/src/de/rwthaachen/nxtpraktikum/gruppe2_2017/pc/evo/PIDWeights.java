package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.evo;

/**
 * Stores a set of PID weights.
 *
 * @author Robin
 */
public final class PIDWeights
{
	public double weightGyroSpeed,
			weightGyroIntegral,
			weightMotorDistance,
			weightMotorSpeed;

	public PIDWeights(double weightGyroSpeed, double weightGyroIntegral, double weightMotorDistance, double weightMotorSpeed) {
		this.weightGyroSpeed = weightGyroSpeed;
		this.weightGyroIntegral = weightGyroIntegral;
		this.weightMotorDistance = weightMotorDistance;
		this.weightMotorSpeed = weightMotorSpeed;
	}

	public double get(int idx) {
		switch (idx) {
			case 0:
				return weightGyroSpeed;
			case 1:
				return weightGyroIntegral;
			case 2:
				return weightMotorDistance;
			case 3:
				return weightMotorSpeed;
			default:
				throw new IllegalArgumentException();
		}
	}

	public void set(int idx, double weight) {
		switch (idx) {
			case 0:
				weightGyroSpeed = weight;
				break;
			case 1:
				weightGyroIntegral = weight;
				break;
			case 2:
				weightMotorDistance = weight;
				break;
			case 3:
				weightMotorSpeed = weight;
				break;
			default:
				throw new IllegalArgumentException();
		}
	}

	@Override
	public PIDWeights clone() {
		return new PIDWeights(weightGyroSpeed, weightGyroIntegral, weightMotorDistance, weightMotorSpeed);
	}
}
