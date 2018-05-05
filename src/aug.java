import java.util.HashMap;
import java.util.LinkedList;

public class aug {

	private static HashMap<Integer, Boolean> arcOrientation;


	/**
	 * finds cycle, determines leavingArc, updates flowValues
	 * 
	 * @param enteringArc
	 * @return
	 */
	static Arc[] findCycle(Arc enteringArc) {

		// cycle orientation
		boolean sameDirection = false;
		if (enteringArc.getFlowValue() == enteringArc.getLowerBound()) {
			sameDirection = true;
		}
		int u = 0;
		int v = 0;
		LinkedList<Integer> u_list = new LinkedList<Integer>();
		LinkedList<Integer> v_list = new LinkedList<Integer>();
		arcOrientation = new HashMap<Integer, Boolean>();


		boolean u_to_v; // orientation of the cycle
		if (nsimplex.tree[enteringArc.getTailNode()].getDepth() >= nsimplex.tree[enteringArc.getHeadNode()]
				.getDepth()) {
			u_to_v = sameDirection;
			u = enteringArc.getTailNode();
			v = enteringArc.getHeadNode();
		} else {
			u_to_v = !sameDirection;
			u = enteringArc.getHeadNode();
			v = enteringArc.getTailNode();
		}


		u_list.add(u);
		v_list.add(v);
		Arc last_v = null;
		Arc first_v = null;
		Arc last_u = null;
		Arc first_u = null;
		long min_u = -1;
		long min_v = -1;
		long min_u_first = -1;

		// walk along u until same heigt is reached

		while (nsimplex.tree[u_list.getLast()].getDepth() != nsimplex.tree[v].getDepth()) {
			System.out.println(nsimplex.tree[u_list.getLast()].getDepth() + " " + nsimplex.tree[v].getDepth());
			int predNode = 0;
			long value = 0L;
			if (nsimplex.tree[u_list.getLast()].getPredArc().getTailNode() == u_list.getLast()) {
				// case pred = head
				predNode = nsimplex.tree[u_list.getLast()].getPredArc().getHeadNode();
				value = remainingSpace(!u_to_v, nsimplex.tree[u_list.getLast()].getPredArc());
				arcOrientation.put(u_list.getLast(), !u_to_v);

			} else {
				// case pred = tail
				predNode = nsimplex.tree[u_list.getLast()].getPredArc().getTailNode();
				value = remainingSpace(u_to_v, nsimplex.tree[u_list.getLast()].getPredArc());
				arcOrientation.put(u_list.getLast(), u_to_v);

			}
			if (min_u < 0L) {
				min_u = value;
				first_u = nsimplex.tree[u_list.getLast()].getPredArc();
				last_u = first_u;
			} else {
				if (value >= 0) {
					if (min_u > value) {
						min_u = value;
						first_u = nsimplex.tree[u_list.getLast()].getPredArc();
						last_u = first_u;
					}
					if (min_u == value) {
						last_u = nsimplex.tree[u_list.getLast()].getPredArc();
					}
				}

			}
			u_list.add(predNode);
			min_u_first = min_u;
		}
		System.out.println("MIn first" + min_u_first + "first-u " + first_u + " last u" + last_u);
		System.out.println(u_list + " " + v_list);

		// walk until cycle is closed
		while (u_list.getLast() != v_list.getLast()) {
			// u part (left side)
			System.out.println("U liste" + u_list + v_list);
			int predNode = 0;
			long value = 0L;

			System.out.println(nsimplex.tree[u_list.getLast()].getPredArc());
			if (nsimplex.tree[u_list.getLast()].getPredArc().getTailNode() == u_list.getLast()) {
				// case pred = head
				predNode = nsimplex.tree[u_list.getLast()].getPredArc().getHeadNode();
				value = remainingSpace(!u_to_v, nsimplex.tree[u_list.getLast()].getPredArc());
				arcOrientation.put(u_list.getLast(), !u_to_v);
			} else {
				// case pred = tail
				predNode = nsimplex.tree[u_list.getLast()].getPredArc().getTailNode();
				value = remainingSpace(u_to_v, nsimplex.tree[u_list.getLast()].getPredArc());
				arcOrientation.put(u_list.getLast(), u_to_v);
			}

			if (min_u < 0L) {
				min_u = value;
				first_u = nsimplex.tree[u_list.getLast()].getPredArc();
				last_u = first_u;
			} else {
				if (value >= 0) {
					if (min_u > value) {
						min_u = value;
						first_u = nsimplex.tree[u_list.getLast()].getPredArc();
						last_u = first_u;
					}
					if (min_u == value) {
						last_u = nsimplex.tree[u_list.getLast()].getPredArc();
					}
				}

			}
			u_list.add(predNode);


			// v part (right side)
			predNode = 0;
			value = 0L;
			if (nsimplex.tree[v_list.getLast()].getPredArc().getTailNode() == v_list.getLast()) {
				// case pred = head
				predNode = nsimplex.tree[v_list.getLast()].getPredArc().getHeadNode();
				value = remainingSpace(u_to_v, nsimplex.tree[v_list.getLast()].getPredArc());
				arcOrientation.put(v_list.getLast(), u_to_v);
			} else {
				// case pred = tail
				predNode = nsimplex.tree[v_list.getLast()].getPredArc().getTailNode();
				value = remainingSpace(!u_to_v, nsimplex.tree[v_list.getLast()].getPredArc());
				arcOrientation.put(v_list.getLast(), !u_to_v);
			}

			if (min_v < 0L) {
				min_v = value;
				first_v = nsimplex.tree[v_list.getLast()].getPredArc();
				last_v = first_v;
			} else {
				if (value >= 0) {
					if (min_v > value) {
						min_v = value;
						first_v = nsimplex.tree[v_list.getLast()].getPredArc();
						last_v = first_v;
					}
					if (min_v == value) {
						last_v = nsimplex.tree[v_list.getLast()].getPredArc();
					}
				}

			}
			v_list.add(predNode);


		}


		System.out.println("After MIn first" + min_u_first + "first-u " + first_u + " last u" + last_u);
		System.out.println("After MIn first-v " + first_v + " last v" + last_v);
		System.out.println(u_to_v);


		System.out.println(u_list);
		System.out.println(v_list);
		if ((u_list.contains(43) || u_list.contains(15)) || (v_list.contains(43) || v_list.contains(15))) {
			System.out.println("hier");
		}

		long updateValue = 0L;
		Arc returnArc = null;


		if (min_v != min_u) {
			if (min_v < min_u) {
				updateValue = min_v;
				if (u_to_v) {
					returnArc = last_v;
				} else {
					returnArc = first_v;
				}
			} else {
				updateValue = min_u;
				if (u_to_v) {
					returnArc = first_u;
				} else {
					returnArc = last_u;
				}
			}
		} else {
			updateValue = min_u;
			if (u_to_v) {
				returnArc = last_v;
			} else {
				returnArc = last_u;
			}
		}
		if (min_v < 0L) {
			updateValue = min_u;
			if (u_to_v) {
				returnArc = first_u;
			} else {
				returnArc = last_u;
			}
		}
		if (min_u < 0L) {
			updateValue = min_v;
			if (u_to_v) {
				returnArc = last_v;
			} else {
				returnArc = first_v;
			}
		}

		if (updateValue > (enteringArc.getUpperBound() - enteringArc.getFlowValue()) && sameDirection) {
			updateValue = enteringArc.getUpperBound() - enteringArc.getFlowValue();
			returnArc = enteringArc;
		}
		if (updateValue > (enteringArc.getFlowValue() - enteringArc.getLowerBound()) && !sameDirection) {
			updateValue = enteringArc.getFlowValue() - enteringArc.getLowerBound();
			returnArc = enteringArc;
		}
		System.out.println("update value " + updateValue);
		System.out.println("Ende");
		// update of flow values
		setFlowValues(u_list, updateValue, returnArc);
		setFlowValues(v_list, updateValue, returnArc);
		if (sameDirection) {
			enteringArc.setFlowValue(enteringArc.getFlowValue() + updateValue);
		} else {
			enteringArc.setFlowValue(enteringArc.getFlowValue() - updateValue);
		}
		// --> flowValue of leavingArc is already adjusted


		// add leavingArc to upper /lower
		if (returnArc.getFlowValue() == returnArc.getLowerBound()) {
			nsimplex.lower.add(returnArc);
			System.out.println(returnArc + " added to lower");
		} else {
			nsimplex.upper.add(returnArc);
			System.out.println(returnArc + " added to upper");
		}

		Arc[] returnArray = new Arc[2];
		returnArray[0] = enteringArc; // returns enteringArc with updated flowValue
		returnArray[1] = returnArc;
		return returnArray;

	}

	private static void setFlowValues(LinkedList<Integer> nodes, long updateValue, Arc leavingArc) {
		// last arc in each list is not in cycle anymore
		nodes.pollLast();
		for (int node : nodes) {
			Arc arc = nsimplex.tree[node].getPredArc();

			if (arcOrientation.get(node)) { // forward Arcs
				arc.setFlowValue(arc.getFlowValue() + updateValue);
			} else { // backward Arcs
				arc.setFlowValue(arc.getFlowValue() - updateValue);
			}

			nsimplex.tree[node].setPredArc(arc);
		}
	}

	// cycle found

	/**
	 * returns -1 if uncapacitated
	 * 
	 * @param plus
	 * @param arc
	 * @return
	 */
	private static long remainingSpace(boolean plus, Arc arc) {
		if (plus) {
			if (arc.getUpperBound() < 0L) {
				return -1;
			} else {
				return arc.getUpperBound() - arc.getFlowValue();
			}

		} else {
			return arc.getFlowValue() - arc.getLowerBound();
		}
	}

}
