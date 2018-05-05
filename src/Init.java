import java.util.Map;

public class Init {

	/**
	 * Creates the primary tree solution. lower and upper are already initialized by Input.
	 */
	static void createStartSolution(Input inp) {

		Map<Integer, Long> nodeList = inp.getSpecialNodes();

		long maxCost = 0L;

		// calculate net demands
		for (Arc arc : nsimplex.lower) {
			long old = 0L;
			if (nodeList.containsKey(arc.getTailNode())) { // if there is a value given consider it otherwise
															// not
				old = nodeList.get(arc.getTailNode());
			}
			nodeList.put(arc.getTailNode(), old - arc.getLowerBound());

			old = 0L;
			if (nodeList.containsKey(arc.getHeadNode())) {
				old = nodeList.get(arc.getHeadNode());
			}
			nodeList.put(arc.getHeadNode(), old + arc.getLowerBound());

			if (Math.abs(arc.getCost()) > maxCost) {
				maxCost = Math.abs(arc.getCost());
			}
		}

		long bigM = 20L + (long) Math.round(0.5 * inp.getNodeNumber() * maxCost);

		// create new node 0
		//nsimplex.tree[0] = new Node(new Arc(-1, 0, 0, 0, 0), 0, 1, 0); // added node, node 1 is
		// set as successor in
		// tree in preorder
		nsimplex.tree[0] = new Node(null, 0, 1, 0);
		// predArc has tailNode -1 which is actually inexistent but never used

		// create new arcs containing node 0
		// arc orientation changed
		for (int i = 1; i <= inp.getNodeNumber(); i++) {
			long netDemand = 0L;
			if (nodeList.containsKey(i)) {
				netDemand = nodeList.get(i);
			}
			int first, last;
			if (netDemand <= 0L) {
				first = 0;
				last = i;
			} else {
				first = i;
				last = 0;
			}
			Arc arc = new Arc(first, last, 0, -1, bigM); // negative upperBound means infinity, reducedCost is
															// set 0
			nsimplex.tree[i] = new Node(arc, 1, i + 1, 0); // depth always 1 due to tree
														// structure, successor is always
			// the following node by index, that means last node gets (inexistent) successor n+1, node price
			// still unknown
		}
		nsimplex.tree[inp.getNodeNumber()].setS(0); // set root as successor of last node


		// calculate feasible flow
		// WARNING: arc orientation different from the one given in script
		long demandValueNewNode = 0L;
		for (int i = 1; i <= inp.getNodeNumber(); i++) {
			Arc newFlowArc = nsimplex.tree[i].getPredArc();
			long newFlowValue = 0L;
			if (nodeList.containsKey(i)) { // check if net demand is != 0
				newFlowValue = nodeList.get(i);
			}
			demandValueNewNode += newFlowValue;

			if (newFlowArc.getHeadNode() == 0) {
				newFlowArc.setFlowValue(newFlowValue);
			} else {
				newFlowArc.setFlowValue(-newFlowValue);
			}

			nsimplex.tree[i].setPredArc(newFlowArc);
		}
		if (demandValueNewNode != 0L) { // first check for feasibility
			System.out.println("No feasible Flow: " + String.valueOf(demandValueNewNode));
		}

		// calculate node prices
		nsimplex.tree[0].setNodePrice(0L);
		for (int i = 1; i <= inp.getNodeNumber(); i++) {
			if (nsimplex.tree[i].getPredArc().getTailNode() == 0) {
				nsimplex.tree[i].setNodePrice(nsimplex.tree[i].getPredArc().getCost());
			} else {
				nsimplex.tree[i].setNodePrice(-nsimplex.tree[i].getPredArc().getCost());
			}
		}

		// calculate reduced costs
		// reducedCost is already set 0 for all arcs in spanning tree
		// all other arcs are in lower
		for (Arc arc : nsimplex.lower) {
			arc.setReducedCost(arc.getCost() + nsimplex.tree[arc.getTailNode()].getNodePrice()
					- nsimplex.tree[arc.getHeadNode()].getNodePrice());
		}

	}


}
