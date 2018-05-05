public class Node {

	private Arc predArc;
	private int depth;
	private int s;
	private long nodePrice;

	/**
	 * new Node is initialized with predNode = 0
	 * 
	 * @param predArc
	 * @param depth
	 * @param s
	 * @param nodePrice
	 */
	public Node(Arc predArc, int depth, int s, long nodePrice) {
		this.predArc = predArc;
		this.depth = depth;
		this.s = s;
		this.nodePrice = nodePrice;
	}

	public Arc getPredArc() {
		return predArc;
	}

	public void setPredArc(Arc predArc) {
		this.predArc = predArc;
	}

	/**
	 * @returns predNode to given currentNode
	 */
	public int getPredNode(int node) {
		if (this.predArc.getHeadNode() == node) {
			return this.predArc.getTailNode();
		} else {
			return this.predArc.getHeadNode();
		}
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public int getS() {
		return s;
	}

	public void setS(int s) {
		this.s = s;
	}

	public long getNodePrice() {
		return nodePrice;
	}

	public void setNodePrice(long nodePrice) {
		this.nodePrice = nodePrice;
	}

	public String toString() {
		return predArc.toString() + "; d:" + String.valueOf(depth) + ", s: " + String.valueOf(s)
				+ ", price: " + String.valueOf(nodePrice);
	}


}
