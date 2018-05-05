import java.util.LinkedList;

/*
 * Perform all updates besides flowValues, which is in aug
 */
public class update {


	private int f_one;
	private int f_two;
	private int e_one;
	private int e_two;
	private Arc enteringArc;
	private Arc leavingArc;

	update(Arc enteringArc, Arc leavingArc) {

		f_one = 0;
		f_two = 0;
		if (nsimplex.tree[leavingArc.getTailNode()].getDepth() == nsimplex.tree[leavingArc.getHeadNode()].getDepth() + 1) {
			f_one = leavingArc.getHeadNode();
			f_two = leavingArc.getTailNode();
		} else {
			f_one = leavingArc.getTailNode();
			f_two = leavingArc.getHeadNode();
		}
		// find orientation of enteringArc with regard to components T1, T2
		boolean noCoincidence = true;
		if (f_one == enteringArc.getHeadNode()) {
			e_one = enteringArc.getHeadNode();
			e_two = enteringArc.getTailNode();
			noCoincidence = false;
		}
		if (f_two == enteringArc.getHeadNode()) {
			e_one = enteringArc.getTailNode();
			e_two = enteringArc.getHeadNode();
			noCoincidence = false;
		}
		if (f_one == enteringArc.getTailNode()) {
			e_one = enteringArc.getTailNode();
			e_one = enteringArc.getHeadNode();
			noCoincidence = false;
		}
		if (f_two == enteringArc.getTailNode()) {
			e_one = enteringArc.getHeadNode();
			e_two = enteringArc.getTailNode();
			noCoincidence = false;
		}
		if (noCoincidence) {
			int i = nsimplex.tree[f_two].getS();
			while (nsimplex.tree[i].getDepth() > nsimplex.tree[f_two].getDepth()) {
				if (enteringArc.getHeadNode() == i) {
					e_one = enteringArc.getTailNode();
					e_two = enteringArc.getHeadNode();
					break;
				}
				if (enteringArc.getTailNode() == i) {
					e_one = enteringArc.getHeadNode();
					e_two = enteringArc.getTailNode();
					break;
				}
				i = nsimplex.tree[i].getS();
			}
		}

		this.enteringArc = enteringArc;
		this.leavingArc = leavingArc;

		// System.out.println("Eund f" + e_one + " " + e_two + " " + f_one + "  " + f_two);

	}

	/**
	 * also updates reducedCost of enteringArc
	 */
	void updateNodePrices() {

		int u = 0;
		long updateValue;
		if (enteringArc.getHeadNode() == e_two) {
			updateValue = enteringArc.getReducedCost();
		} else {
			updateValue = -enteringArc.getReducedCost();
		}

		nsimplex.tree[f_two].setNodePrice(nsimplex.tree[f_two].getNodePrice() + updateValue);

		u = nsimplex.tree[f_two].getS();
		while (nsimplex.tree[u].getDepth() > nsimplex.tree[f_two].getDepth()) {
			nsimplex.tree[u].setNodePrice(nsimplex.tree[u].getNodePrice() + updateValue);
			u = nsimplex.tree[u].getS();
			if (u >= nsimplex.tree.length) {
				break;
			}
		}

		this.enteringArc.setReducedCost(0L);

	}

	int[] savePreorder() {
		int[] preorder = new int[nsimplex.tree.length];
		for (int i = 0; i < nsimplex.tree.length; i++) {
			preorder[i] = nsimplex.tree[i].getS();
		}
		return preorder;
	}

	void updatePreorder() {

		int a, b, i, j, k, r = 0;
		a = f_one;
		while (nsimplex.tree[a].getS() != f_two) {
			a = nsimplex.tree[a].getS();
		}
		b = nsimplex.tree[e_one].getS();
		i = e_two;
		k = i;
		while (nsimplex.tree[nsimplex.tree[k].getS()].getDepth() > nsimplex.tree[i].getDepth()) {
			k = nsimplex.tree[k].getS();
			// if (k >= nsimplex.tree.length) {
			// break;
			// }
		}
		r = nsimplex.tree[k].getS();

		while (i != f_two) { // step 3
			// step 4
			j = i;
			i = nsimplex.tree[i].getPredNode(i);
			nsimplex.tree[k].setS(i);
			// step 5
			k = i;
			while (nsimplex.tree[k].getS() != j) {
				k = nsimplex.tree[k].getS();
			}
			// step 6
			if (nsimplex.tree[r].getDepth() > nsimplex.tree[i].getDepth()) {
				nsimplex.tree[k].setS(r);
				while (nsimplex.tree[nsimplex.tree[k].getS()].getDepth() > nsimplex.tree[i].getDepth()) {
					k = nsimplex.tree[k].getS();
				}
				r = nsimplex.tree[k].getS();
			}

		} // end while

		nsimplex.tree[e_one].setS(e_two);
		if (e_one == a) {
			nsimplex.tree[k].setS(r);
		} else {
			nsimplex.tree[k].setS(b);
			nsimplex.tree[a].setS(r);
		}
	}

	void updatePredecessors(int[] preorderOld) {


		LinkedList<Integer> preds = new LinkedList<Integer>(); // list for pivot trunk
		preds.add(e_two);
		int x = e_two;
		// System.out.println(e_two + " " + f_two);
		while (x != f_two) {
			preds.add(nsimplex.tree[x].getPredNode(x));
			x = preds.getLast();
		}
		// f2 != e1/e2
		// nsimplex.tree[f_two].setPredArc(nsimplex.tree[preds.getLast()].getPredArc());
		for (int i = preds.size() - 1; i > 0; i--) {
			nsimplex.tree[preds.get(i)].setPredArc(nsimplex.tree[preds.get(i - 1)].getPredArc());
		}
		nsimplex.tree[e_two].setPredArc(enteringArc);


		int change = nsimplex.tree[e_one].getDepth() - nsimplex.tree[e_two].getDepth() + 1;
		// System.out.println(preds);
		LinkedList<LinkedList<Integer>> component = new LinkedList<LinkedList<Integer>>(); // save all nodes
																							// from component T2
		//System.out.println(preds + " " + "Eund f" + e_one + " " + e_two + " " + f_one + "  " + f_two);

		// find components of the tree, i.e., T2
		for (int i = 0; i < preds.size(); i++) {
			x = preds.get(i);
			LinkedList<Integer> currentList = new LinkedList<Integer>();
			currentList.add(x);
			// System.out.println("added " + x + " as pivot ");
			if (preorderOld[x] >= nsimplex.tree.length) {
				// nsimplex.tree[x].setDepth(nsimplex.tree[x].getDepth() + change);
				component.add(currentList);
				continue;
			}
			int y = preorderOld[x];

			// find nodes that belong to the set of the current pivot element
			while (nsimplex.tree[y].getDepth() > nsimplex.tree[x].getDepth()) {

				if (i > 0 && y == preds.get(i - 1)) {
					int j = i - 1;
					while (y == preds.get(j)) {
						y = preorderOld[component.get(j).getLast()];
						j--;
						if (j < 0) {
							break;
						}
					}

					continue;
				}
				/*
				 * // case only e2 is already taken if ( i == 1 && y == preds.getFirst()) { y =
				 * preorderOld[y]; continue; } // case possibly bigger part of the tree is alrea if (i > 1 &&
				 * y == preds.get(i - 1)) { System.out.println("pivot element " + y); int z = y; //
				 * System.out.println("ifcase"); while (nsimplex.tree[z].getDepth() > nsimplex.tree[preds.get(i -
				 * 1)].getDepth()) { z = preorderOld[z]; } System.out.println("last z: " + z); if (y != z) { y
				 * = z; } else { y = preorderOld[y]; }
				 * 
				 * continue;
				 * 
				 * } }
				 */
				// System.out.println("set depth to " + nsimplex.tree[y].getDepth() + change + " for " + y);
				currentList.add(y);
				//System.out.println("added " + y + " to pivot " + x);
				// nsimplex.tree[y].setDepth(nsimplex.tree[y].getDepth() + change);
				y = preorderOld[y];
			}
			// System.out.println("depht" + nsimplex.tree[x].getDepth() + "  " + x);
			// nsimplex.tree[x].setDepth(nsimplex.tree[x].getDepth() + change);
			// System.out.println("depht" + nsimplex.tree[x].getDepth() + "  " + x);
			change += 2;
			component.add(currentList);

		}
		change = nsimplex.tree[e_one].getDepth() - nsimplex.tree[e_two].getDepth() + 1;
		// System.out.println("apply changes" + component.size());
		for (LinkedList<Integer> set : component) {
			//System.out.println(change + " " + set);
			for (int node : set) {

				nsimplex.tree[node].setDepth(nsimplex.tree[node].getDepth() + change);
			}
			change += 2;
		}

		// depth update
		// preds doesn't contain f_two
		/*
		 * int change = nsimplex.tree[e_one].getDepth() - nsimplex.tree[e_two].getDepth() + 1 + 2 * (preds.size() -
		 * 1); x = f_two; int y = nsimplex.tree[f_two].getS(); System.out.println(x + " " + y); for (int node :
		 * preds) { System.out.print(preds + "-- "); } System.out.println(); while (!preds.isEmpty()) {
		 * 
		 * LinkedList<Integer> adjacent = new LinkedList<Integer>();
		 * 
		 * try { System.out.println(y); System.out.println(nsimplex.tree[y].getDepth());
		 * System.out.println(nsimplex.tree[x].getDepth()); while (nsimplex.tree[y].getDepth() >
		 * nsimplex.tree[x].getDepth()) { y = nsimplex.tree[y].getS(); System.out.println(nsimplex.tree.length); if (y >=
		 * nsimplex.tree.length) { System.out.println("BAEREWR"); break; }
		 * System.out.println(nsimplex.tree[y].getPredNode(y)); if (nsimplex.tree[y].getPredNode(y) == x && y !=
		 * preds.getLast()) { adjacent.add(y); } } } catch (ArrayIndexOutOfBoundsException e) {
		 * 
		 * }
		 * 
		 * nsimplex.tree[x].setDepth(nsimplex.tree[x].getDepth() + change); // change depth for pivot trunk node for
		 * (int node : adjacent) { nsimplex.tree[node].setDepth(nsimplex.tree[node].getS() + change); int curr =
		 * nsimplex.tree[node].getS(); while (nsimplex.tree[curr].getDepth() > nsimplex.tree[node].getDepth()) {
		 * nsimplex.tree[curr].setDepth(nsimplex.tree[curr].getDepth() + change); curr = nsimplex.tree[node].getS(); if
		 * (curr >= nsimplex.tree.length) { break; } } }
		 * 
		 * change -= 2; x = preds.pollLast();
		 */


	}

	/**
	 * updates reducedCost for Arcs that aren't in tree, those have reducedCost 0, enteringArc is updatet in
	 * aug
	 */
	void updateReducedCost() {
		for (Arc arc : nsimplex.lower) {
			arc.setReducedCost(arc.getCost() + nsimplex.tree[arc.getTailNode()].getNodePrice()
					- nsimplex.tree[arc.getHeadNode()].getNodePrice());
		}
		for (Arc arc : nsimplex.upper) {
			arc.setReducedCost(arc.getCost() + nsimplex.tree[arc.getTailNode()].getNodePrice()
					- nsimplex.tree[arc.getHeadNode()].getNodePrice());
		}
	}

}
