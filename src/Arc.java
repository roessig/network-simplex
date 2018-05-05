public class Arc {

	private int tailNode;
	private int headNode;
	private int lowerBound;
	private long upperBound;
	private long cost;
	private long reducedCost;
	private long flowValue;

	/*
	 * Constructor for Arc object. Flow Value is set to lowerBound and reducedCost = 0;
	 */
	public Arc(int tailNode, int headNode, int lowerBound, long upperBound, long cost) {

		this.tailNode = tailNode;
		this.headNode = headNode;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		this.cost = cost;
		this.reducedCost = 0L;
		this.flowValue = lowerBound;

	}

	public long getFlowValue() {
		return flowValue;
	}

	public void setFlowValue(long flowValue) {
		this.flowValue = flowValue;
	}

	public int getTailNode() {
		return tailNode;
	}

	public int getHeadNode() {
		return headNode;
	}

	public int getLowerBound() {
		return lowerBound;
	}

	public long getUpperBound() {
		return upperBound;
	}

	public long getCost() {
		return cost;
	}

	public long getReducedCost() {
		return reducedCost;
	}

	public void setReducedCost(long reducedCost) {
		this.reducedCost = reducedCost;
	}

	public String toString() {
		return String.valueOf(tailNode) + "->" + String.valueOf(headNode) + "; " + String.valueOf(lowerBound)
				+ " < " + String.valueOf(flowValue) + " < " + String.valueOf(upperBound) + "; "
				+ String.valueOf(reducedCost) + "/" + String.valueOf(cost);
	}

	public String toShortString() {
		return String.valueOf(lowerBound) + " < " + String.valueOf(flowValue) + " < "
				+ String.valueOf(upperBound) + "; " + String.valueOf(reducedCost) + "/"
				+ String.valueOf(cost);
	}


}
