import java.util.ArrayList;
import java.util.Collections;


public class nsimplex {

	public static ArrayList<Arc> lower = new ArrayList<Arc>();
	public static ArrayList<Arc> upper = new ArrayList<Arc>();
	public static Node[] tree;

	public static void main(String[] args) throws Exception {

		// reading input
		//Input inp = new Input("testset2014/transp9.net"); // file according to file "testset2014/stndrd3.net"
		Input inp = new Input(args[0]);

		// format
		System.out.println(inp.getArcNumber());
		System.out.println(inp.getNodeNumber());

		tree = new Node[inp.getNodeNumber() + 1]; // +1 for extra node which is
													// added during
												// initialization

		Init.createStartSolution(inp);
		//for (Arc arc : lower) {
		//System.out.print(arc.toString() + " || ");
		//}
		System.out.println();

		// test if optimum
		// find augmenting arc = Pricing

		int count = 0;
		while (true) {
			Arc enteringArc = opt.findEnteringArc();


			if (enteringArc == null) {
				if (opt.feasibleSolution()) {
					printSolution();
					System.out.println("Optimal solution found.");

					// calculate objective value
					long obj = 0;
					for (Arc arc : lower) {
						obj += arc.getCost() * arc.getFlowValue();
					}

					for (Arc arc : upper) {
						obj += arc.getCost() * arc.getFlowValue();
					}

					for (int i = 1; i < tree.length; i++) {
						Arc arc = nsimplex.tree[i].getPredArc();
						obj += arc.getCost() * arc.getFlowValue();
						//System.out.println(obj + ", " + arc.getCost() + ", " + arc.getFlowValue());
					}

					System.out.println(obj);
				} else {
					System.out.println("No feasible solution.");
					//System.out.println(obj);
				}
				break;

			} else {
				//System.out.println();
				System.out.println(count + "________________________________________________");
				//System.out.println(obj);
				// augmenting
				// find cycle
				// return leavingArc
				System.out.println("Entering: " + enteringArc);
				Arc[] returnedArcs = aug.findCycle(enteringArc);
				enteringArc = returnedArcs[0];
				Arc leavingArc = returnedArcs[1];

				System.out.println("Leaving: " + leavingArc);

				// update
				// nodePrices
				update up = new update(enteringArc, leavingArc);

				if ((enteringArc.getHeadNode() != leavingArc.getHeadNode())
						|| enteringArc.getTailNode() != leavingArc.getTailNode()) {
					up.updateNodePrices();
					int[] old = up.savePreorder();
					up.updatePreorder();
					up.updatePredecessors(old);
				}

				up.updateReducedCost();


				System.out.println();



				count++;

			}

		}


	}

	private static void printSolution() {
		ArrayList<String> output = new ArrayList<String>();
		int linecount = 0;
		for (Arc arc : lower) {
			//if (arc.getTailNode() != 0 && arc.getHeadNode() != 0 && arc.getFlowValue() != 0) {
			output.add(arc.toString());
			linecount++;
			//}
		}
		for (Arc arc : upper) {
			//if (arc.getTailNode() != 0 && arc.getHeadNode() != 0 && arc.getFlowValue() != 0) {
			output.add(arc.toString());
			linecount++;
			//}
		}
		for (int i = 1; i < tree.length; i++) {
			Arc arc = nsimplex.tree[i].getPredArc();
			//if (arc.getTailNode() != 0 && arc.getHeadNode() != 0 && arc.getFlowValue() != 0) {
			output.add(arc.toString());
			linecount++;
			//}
		}
		Collections.sort(output);
		for (String ent : output) {
			System.out.println(ent);
		}

		System.out.println("number of arcs " + linecount);
	}

}
