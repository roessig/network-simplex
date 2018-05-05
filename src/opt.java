public class opt {

	/**
	 * tests if optimums is already reached and returns entering arc otherwise
	 * 
	 * @return enteringArc or null, if optimum is reached
	 */
	static Arc findEnteringArc() {

		long maxReducedCost = 0L;
		long minReducedCost = 0L;
		Arc enteringArcLow = null;
		Arc enteringArcUpp = null;
		int lowArcIndex = 0;
		int uppArcIndex = 0;
		for (int i = 0; i < nsimplex.lower.size(); i++) {
			Arc low = nsimplex.lower.get(i);
			if (low.getReducedCost() < minReducedCost) {
				enteringArcLow = low;
				minReducedCost = low.getReducedCost();
				lowArcIndex = i;
			}
		}
		for (int i = 0; i < nsimplex.upper.size(); i++) {
			Arc upp = nsimplex.upper.get(i);
			if (upp.getReducedCost() > maxReducedCost) {
				enteringArcUpp = upp;
				maxReducedCost = upp.getReducedCost();
				uppArcIndex = i;
			}
		}


		if (maxReducedCost == 0L && minReducedCost == 0L) {
			return null;
		}


		// System.out.println("__________");
		// System.out.println(enteringArcLow);
		// System.out.println(enteringArcUpp);
		Arc enteringArc;
		if (-minReducedCost > maxReducedCost) {
			enteringArc = enteringArcLow;
			nsimplex.lower.remove(lowArcIndex);
			System.out.println("entering from lower");
		} else {
			enteringArc = enteringArcUpp;
			nsimplex.upper.remove(uppArcIndex);
			System.out.println("Entering from uppper");
		}

		return enteringArc;
	}


	/**
	 * check whether there is flow on any artificial arc of the form (0,v) or (v,0)
	 * 
	 * @return true if a feasible solution exists
	 */
	static boolean feasibleSolution() {
		for (Arc arc : nsimplex.lower) {
			if (arc.getHeadNode() == 0 || arc.getTailNode() == 0) {
				if (arc.getFlowValue() > 0L) {
					return false;
				}
			}
		}
		// artificial arcs can't be in upper since upper capacity is infinte

		for (int i = 1; i < nsimplex.tree.length; i++) { // predArc of Node 0 doesn't actually exist
			if (nsimplex.tree[i].getPredArc().getTailNode() == 0 || nsimplex.tree[i].getPredArc().getHeadNode() == 0) {
				if (nsimplex.tree[i].getPredArc().getFlowValue() > 0L) {
					return false;
				}
			}
		}

		return true;
	}

}
