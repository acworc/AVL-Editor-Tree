package editortrees;

import java.util.ArrayList;

// A node in a height-balanced binary tree with rank.
// Except for the NULL_NODE (if you choose to use one), one node cannot
// belong to two different trees.

public class Node extends EditTree {

	// The fields would normally be private, but for the purposes of this class,
	// we want to be able to test the results of the algorithms in addition to
	// the
	// "publicly visible" effects

	char element;
	Node left, right; // subtrees
	int rank; // inorder position of this node within its own subtree.
	Code balance;
	


	enum Code {
		SAME, LEFT, RIGHT;
		// Used in the displayer and debug string
		public String toString() {
			switch (this) {
			case LEFT:
				return "/";
			case SAME:
				return "=";
			case RIGHT:
				return "\\";
			default:
				throw new IllegalStateException();
			}
		}
	}

	public Node(char ch) {
		this.element = ch;
		this.right = EditTree.NULL_NODE;
		this.left = EditTree.NULL_NODE;
		this.rank = 0;
		this.balance = Code.SAME;
	
	}

	public Node() {
		this.right = EditTree.NULL_NODE;
		this.left = EditTree.NULL_NODE;
		this.rank = 0;
		this.balance = Code.SAME;
	
	}
	
	public Node(char ch, Node left, Node right, int rank, Code balance) {
		this.element = ch;
		this.right = right;
		this.left = left;
		this.rank = rank;
		this.balance = balance;
	
	}
	
	public Node treeBuilder(String s){
		//System.out.println("this: "+this.element);
		if(s.length() == 0){
			return NULL_NODE;
		}
		char element = s.charAt(s.length()/2);
		String leftSubString = s.substring(0, s.length()/2);
		String rightSubString = s.substring((s.length()/2) +1);
		if(leftSubString.length() > rightSubString.length()){
			this.balance = Code.LEFT;
		}
		else{this.balance = Code.SAME;
		
		}
		
		this.element = element;
		this.rank = leftSubString.length();
		this.left = new Node();
		this.right = new Node();
		this.left = this.left.treeBuilder(leftSubString);
		this.right = this.right.treeBuilder(rightSubString);
		return this;
	}

	// Node parent; // You may want this field.
	// Feel free to add other fields that you find useful

	// You will probably want to add several other methods

	// For the following methods, you should fill in the details so that they
	// work correctly

	public Wrapper insert(char ch, int pos, DataContainer data) {
		Wrapper tempNode = new Wrapper(false, this);
		if (this == NULL_NODE) {
			
			throw new IndexOutOfBoundsException();
		}
		// if inserting on the left side
		if (pos <= this.rank) 
		{
			this.rank++;
			if (this.left == NULL_NODE) {
				this.left = new Node(ch);
				return handleLeftBalance(data);
			}
			tempNode = this.left.insert(ch, pos, data);
			this.left = tempNode.node;
			
			if (tempNode.needUpdate) {
				return handleLeftBalance(data);
			}
			
		} 
		else if (pos > this.rank) {
			int nextPos = pos - (this.rank +1);
			if(nextPos == 0 && this.right == NULL_NODE){
				this.right  = new Node(ch);
				return handleRightBalance(data);
			}
			
				tempNode = this.right.insert(ch, nextPos, data);
				this.right = tempNode.node;
				
				if(tempNode.needUpdate) {
					return this.handleRightBalance(data);
				}
				
			
			}
		return new Wrapper(false, this);
	}
		
	
	
	public Node copy(){
		if (this == NULL_NODE) {
			return NULL_NODE;
		}
		return new Node(this.element, this.left.copy(), this.right.copy(), this.rank, this.balance);
	}
	
	public Wrapper rightPaste(Node q, int smaller, int target, DataContainer data){
		
		//System.out.println("rightPaste");
		int tmp = 0; 
				if(this.balance == Code.LEFT){
					tmp =  -2; 
				}
				else{
					tmp =-1;
				}
//		int tmp=0;
//		switch(this.balance){
//			
//			case SAME:
//				tmp = -1;
//			case RIGHT:
//				tmp = -1;
//			case LEFT:
//				tmp = -2;
//		}
		
		if(target + tmp < 0){
			System.out.println("size: " + this.size());
			
			q.left = this;
			if(this.height() > q.right.height()){
				q.balance = Code.LEFT;
			}
			else{
				q.balance = Code.SAME;
			}
			q.rank = this.size();
			return new Wrapper(true, q);
		
		}
		Wrapper temp = this.right.rightPaste(q,smaller, target + tmp, data);
		this.right = temp.node;
		if(temp.needUpdate){
			return this.handleRightBalance(data);
		}
		return new Wrapper(false, this);
	}
	
	public Wrapper leftPaste(Node q,int smallSize, int target, DataContainer data){
		int tmp = 0; 
		if(this.balance == Code.RIGHT){
			tmp =  -2; 
		}
		else{
			tmp =-1;
		}
		if(target + tmp < 0){
			//handle attachment
			q.right = this;
			if(this.height() > q.left.height()){
				q.balance = Code.RIGHT;
			}
			else{
				q.balance = Code.SAME;
			}

			q.rank = smallSize;
			return new Wrapper(true, q);
		}
	Wrapper temp = this.left.leftPaste(q, smallSize, target+tmp, data);
	this.left = temp.node;
	this.rank += smallSize+ 1;
	if(temp.needUpdate){
		return this.handleLeftBalance(data);
	}
	return new Wrapper(false, this);
	}
	
	/**
	 * 
	 * @param pos
	 *		position of character to delete from tree
	 *
	 *	Goes through tree and deletes character
	 *	while updating balance codes and performing
	 *	rotations as necessary
	 *
	 * @return character that's deleted
	 */
	public Wrapper remove(int posToDelete, DataContainer data)
	{
		if (this == NULL_NODE){
			throw new IndexOutOfBoundsException();
		}
		//recurse left check
		if(posToDelete < this.rank){
			Wrapper left = this.left.remove(posToDelete, data);
			this.rank -= 1;
			this.left = left.node;
			char oldElement = left.element;
			if(left.needUpdate){
				left  = this.removeUpdateRight(data);
				left.element = oldElement;
				return left;
			}
			return new Wrapper(false, this, left.element);
		}
		//recurse right check
		else if(posToDelete > this.rank){
			posToDelete = posToDelete - (this.rank +1);
			Wrapper right = this.right.remove(posToDelete, data);	
			//System.out.println("This right wrapper: "+right.element);
			this.right = right.node;
			char oldElement = right.element;
			//System.out.println("the right element: "+right.element);
			if(right.needUpdate){
				right  = this.removeUpdateLeft(data);
				right.element = oldElement;
				return right;
			}
			return new Wrapper(false, this, right.element);
		}
			//at the target pos
			return this.removeHandleChildren(posToDelete+1, data);
	}
		
	
	public Wrapper removeHandleChildren( int currPos, DataContainer data){
		
		if(this.right == NULL_NODE && this.left != NULL_NODE){
			return new Wrapper(true, this.left, this.element);
		}
		if(this.left == NULL_NODE && this.right != NULL_NODE){
			return new Wrapper(true, this.right, this.element);
		}
		
		if(this.right == NULL_NODE && this.left == NULL_NODE){
			//System.out.println("this element: "+this.element);
			return new Wrapper(true, NULL_NODE, this.element);
		}
		// handles if node had both children
		Wrapper temp = this.remove(currPos, data);
		char oldElement = this.element;
		this.element = temp.element;
		return new Wrapper(temp.needUpdate, temp.node, oldElement);
		
	}
	
	public Wrapper removeUpdateRight(DataContainer data){
		switch(this.balance) {
		case LEFT: 
			this.balance = Code.SAME;
			return new Wrapper(true, this);
		case SAME:
			this.balance = Code.RIGHT;
			return new Wrapper(false, this);
		case RIGHT:
			boolean ambiguous = this.right.balance != Code.SAME;
			return new Wrapper(ambiguous,this.handleRotate(this.right, data));
		}
		return new Wrapper(false, this);
	}
	public Wrapper removeUpdateLeft(DataContainer data){
		switch(this.balance) {
		case LEFT:
			boolean ambiguous = this.left.balance != Code.SAME;
			return new Wrapper(ambiguous,this.handleRotate(this.left, data));
		case SAME:
			this.balance = Code.LEFT;
			return new Wrapper(false, this);
		case RIGHT:
			this.balance = Code.SAME;
			return new Wrapper(true, this);
		}
		return new Wrapper(false, this);
	}

	public Wrapper handleLeftBalance(DataContainer data) {
		switch (this.balance) {
		case LEFT:
			return new Wrapper(false, this.handleRotate(this.left, data));
		case SAME:
			this.balance = Code.LEFT;
			return new Wrapper(true, this);
		case RIGHT:
			this.balance = Code.SAME;
			return new Wrapper(false, this);
		}
		return new Wrapper(false, this);
	}


	public Wrapper handleRightBalance(DataContainer data) {
		//System.out.println("Balance Code: " + this.balance + " hc: " + data.heightChange + "handle right");

		switch (this.balance) {
		case RIGHT:
			//System.out.println("handle right");
		//	System.out.println("is Null -- >"+this.right);
			return new Wrapper(false, this.handleRotate(this.right, data));
		case SAME:
			this.balance = Code.RIGHT;
			return new Wrapper(true, this);
		case LEFT:
			this.balance = Code.SAME;
			return new Wrapper(false, this);
		}
		return new Wrapper(false, this);
	}
	
	private Node handleRotate(Node child, DataContainer data) {
		if(this.balance == Code.RIGHT)
			if(child.balance == Code.RIGHT){
				return singleLeftRotation(this, data);
			} else if( child.balance == Code.LEFT) {
				return doubleLeftRotation(this, data);
			} else {
				Node node = this.singleLeftRotation(this, data);
				this.balance = Code.RIGHT;
				child.balance = Code.LEFT;
				return node;
		}
		
		if(child.balance == Code.RIGHT){
			return doubleRightRotation(this, data);
		}else if (child.balance == Code.LEFT) {
			return this.singleRightRotation(this, data);
		} else {
			Node node = this.singleRightRotation(this, data);
			this.balance = Code.LEFT;
			child.balance = Code.RIGHT;
			return node;
		}
	}

	/**
	 * 
	 * Searches for node at a particular position
	 *
	 * @param pos
	 * @return a node
	 */
	
	public Node search(int pos) {

		if (this.rank == pos) {
			return this;
		}
		if (pos < this.rank) {
			return this.left.search(pos);
		}
		return this.right.search(pos - (this.rank + 1));

	}
	
	/**
	 * Use the balance codes to compute height in 
	 * log(n)
	 */

	public int height() {
		int toReturn = 0;
		if (this == NULL_NODE) {
			return toReturn;
		}
		if(this.balance == Code.RIGHT ){
			System.out.println(this.element);
			System.out.println("The right: "+this.right.element);
			
			//temp = this.right;
			toReturn = toReturn + this.right.height() +1 ;
		}
		else{
			//temp = this.left;
			toReturn = toReturn + this.left.height() +1;
		}
		return toReturn;
	}

	public int size() {
		if (this == NULL_NODE) {
			return 0;
		}
		return 1 + this.rank + this.right.size();
	}
	
	
	public void ArrayListBuilder(ArrayList<Character> list) {
		// System.out.println("test print " + this.element);
		if (this == EditTree.NULL_NODE) {
			return;
		}
		// System.out.println("Tree in builder: "+this.left.element + " "
		// +this.element+" "+this.right.element);
		this.left.ArrayListBuilder(list);

		list.add(this.element);
		// System.out.println("added: "+this.element);
		// System.out.println("this nodes's right: "+this.right.element);
		// System.out.println("this nodes's left: "+this.left.element);

		this.right.ArrayListBuilder(list);
		// System.out.println("Right: "+this.right.element);
	}

	public void debugArrayListBuilder(Node start, ArrayList<String> list) {
		// System.out.println("start in builder: "+start.element);
		if (start == EditTree.NULL_NODE) {
			// System.out.println("NULL NODE");
			return;
		}
		Character a = start.element;
		Integer b = start.rank;
		Code c = start.balance;
		String element = a.toString();
		// System.out.println(element);
		String rank = b.toString();
		String balance = c.toString();
		// System.out.println(element+rank+balance);
		list.add(element + rank + balance);
		debugArrayListBuilder(start.left, list);
		debugArrayListBuilder(start.right, list);
	}

	class Wrapper {
		public boolean needUpdate;
		public Node node;
		public char element;

		public Wrapper(boolean hc, Node n) {
			this.needUpdate = hc;
			this.node = n;

		}
		
		public Wrapper(boolean hc, Node n, char element) {
			this.needUpdate = hc;
			this.node = n;
			this.element = element ;
		}

	}
}