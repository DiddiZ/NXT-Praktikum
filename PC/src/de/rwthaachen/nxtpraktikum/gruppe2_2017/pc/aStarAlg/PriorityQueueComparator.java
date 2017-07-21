package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.aStarAlg;

import java.util.Comparator;

/**
 * This class is the comparartor used for the priorityQueue in the a-star Algorithm
 *
 * @author Fabian
 */
public class PriorityQueueComparator implements Comparator<QueueElement>
{
	@Override
	public int compare(QueueElement o1, QueueElement o2) {
		int res;
		final double comp = o1.getPriority() - o2.getPriority();
		if (comp <= 0) {
			if (comp == 0) {
				res = 0;
			} else {
				res = -1;
			}
		} else {
			res = 1;
		}
		return res;
	}
}
