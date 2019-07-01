package editortrees;

import java.util.ArrayList;

import editortrees.Node.Code;
import editortrees.Node.Wrapper;

// A height-balanced binary tree with rank that could be the basis for a text editor.

public class EditTree {

	private Node root;
	final static Node NULL_NODE = new Node();
	public int numRotes;
	public int pos;
	public DataContainer data = new DataContainer();
	public int size =0;

	/**
	 * MILESTONE 1 Construct an empty tree
	 */
	public EditTree() {
		this.pos = 0;
		this.data.numRot =0;
		this.root = NULL_NODE;
	
	}

	/**
	 * MILESTONE 1 Construct a single-node tree whose element is ch
	 * 
	 * @param ch
	 */
	public EditTree(char ch) {
		this.pos = 0;
		this.root = new Node(ch);
		this.root.left = NULL_NODE;
		this.root.right = NULL_NODE;
		this.data.numRot =0;
	
	} //comment

	/**
	 * MILESTONE 2 Make this tree be a copy of e, with all new nodes, but the
	 * same shape and contents.
	 * 
	 * @param e
	 */
	public EditTree(EditTree e) {
		this.pos = 0;
		this.root= e.root.copy();
	}

	/**
	 * MILESTONE 3 Create an EditTree whose toString is s. This can be done in
	 * O(N) time, where N is the length of the tree (repeatedly calling insert()
	 * would be O(N log N), so you need to find a more efficient way to do this.
	 * 
	 * @param s
	 */
	public EditTree(String s) {
		this.pos = 0;
		this.root = new Node();
		this.root = this.root.treeBuilder(s);
	}
	

	/**
	 * MILESTONE 1 returns the total number of rotations done in this tree since
	 * it was created. A double rotation counts as two.
	 *
	 * @return number of rotations since tree was created.
	 */
	public int totalRotationCount() {
		//System.out.println(this.data.numRot);
		return this.data.numRot; // replace by a real calculation.
	}

	public ArrayList<Character> toArrayList() {
		ArrayList<Character> list = new ArrayList<Character>();
		this.root.ArrayListBuilder(list);
		//System.out.println("The list returned by ALBuilder: "+list);
		return list;
	}

	/**
	 * MILESTONE 1 return the string produced by an inorder traversal of this
	 * tree
	 */
	@Override
	public String toString() {

		ArrayList<Character> temp = this.toArrayList();
		String toReturn = "";
		for(int i = 0; i <temp.size(); i++){
			Character a = temp.get(i);
			String toAdd = a.toString();
			toReturn = toReturn+toAdd;
		}
//		return realReStr;
		return toReturn;
//		return returnedString.substring(1, returnedString.length() - 1);

	}

	/**
	 * MILESTONE 1 This one asks for more info from each node. You can write it
	 * like the arraylist-based toString() method from the BST assignment.
	 * However, the output isn't just the elements, but the elements, ranks, and
	 * balance codes. Former CSSE230 students recommended that this method,
	 * while making it harder to pass tests initially, saves them time later
	 * since it catches weird errors that occur when you don't update ranks and
	 * balance codes correctly. For the tree with node b and children a and c,
	 * it should return the string: [b1=, a0=, c0=] There are many more examples
	 * in the unit tests.
	 * 
	 * @return The string of elements, ranks, and balance codes, given in a
	 *         pre-order traversal of the tree.
	 */
	
	public ArrayList<String> toDebugArrayList(Node start) {
		ArrayList<String> list = new ArrayList<String>();
		this.root.debugArrayListBuilder(start, list);
		return list;
	}
	
	public String toDebugString() {
		/**
		 * We need to create a recurive arraylist builder method in the node 
		 * class which adds:
		 * 
		 * nodes element
		 * rank
		 * balance code
		 * 
		 * to an array list which is a parameter and passed in from this class. 
		 * 
		 */
		//System.out.println(this.root.toDebugString());
		if(this.root == NULL_NODE){
			return "[]";
		}
		return this.root.toDebugArrayList(this.root).toString();
	}

	/**
	 * MILESTONE 1
	 * 
	 * @param ch
	 *            character to add to the end of this tree.
	 */

    /**
     * 
     * This is a wrapper class that holds the number of rotations.
     *
     * @author kuhnja.
     *         Created Apr 20, 2017.
     */
	public class DataContainer {
		public int numRot = 0;
		public boolean continueRot;
		public boolean heightChange = false;
		public DataContainer() {
		}
	}

	public void add(char ch) {
		// Notes:
		// 1. Please document chunks of code as you go. Why are you doing what
		// you are doing? Comments written after the code is finalized tend to
		// be useless, since they just say WHAT the code does, line by line,
		// rather than WHY the code was written like that. Six months from now,
		// it's the reasoning behind doing what you did that will be valuable to
		// you!
		// 2. Unit tests are cumulative, and many things are based on add(), so
		// make sure that you get this one correct.
//		if (this.root == NULL_NODE) {
//			this.data.continueRot = true;
//			this.root = this.root.insert(ch, this.pos, this.data);
//			pos++;
//		} else {
//			this.data.continueRot = true;
//			this.root.insert(ch, this.pos, this.data);
//			pos++;
//		}
		this.data.continueRot = true;
		//System.out.println("add char: "+ch+" pos: "+pos);
		this.data.heightChange = false;
		if(this.root == NULL_NODE) {
			this.root = new Node(ch);
		}
		else {
			//System.out.println("Position passed in: "+this.pos);
			this.root = this.root.insert(ch, this.pos, data).node;
		}
		pos++;
		size++;
		this.data.continueRot = true;
	}


	/**
	 * 
	 * This method preforms a single left rotation and returns the node
	 * of the new root. 
	 *
	 * @param parent
	 * @param data
	 * @return node of the root after rotaion (subtree)
	 */
	public Node singleLeftRotation(Node parent, DataContainer data){
		//System.out.println("single left");
		data.numRot++;
		Node child = parent.right;
		Node orphan = child.left;
		child.left = parent;
		parent.right = orphan;
		child.rank += parent.rank + 1;
		parent.balance = Code.SAME;
		child.balance = Code.SAME;
		data.continueRot = false;
		return child;
	}

	/**
	 * 
	 * This method preforms a single right rotation and returns the node
	 * of the new root. 
	 *
	 * @param parent
	 * @param data
	 * @return node of the root after rotaion (subtree)
	 */
	public Node singleRightRotation(Node parent, DataContainer data) {
//		System.out.println("SR: "+data.numRot);
//		System.out.println("newRoot: "+newRoot.element);
//		System.out.println("newRootRight: "+newRoot.right.element);
		//System.out.println("single right");
		data.numRot++;
		//System.out.println("Rotated: "+this.data.numRot);
		Node child = parent.left;
		Node orphan = child.right;
		child.right = parent;
		parent.left = orphan;
		parent.rank -= child.rank + 1;
		parent.balance = Code.SAME;
		child.balance = Code.SAME;
		data.continueRot = false;
		return child;
	}

	/**
	 * 
	 * This method preforms a double left rotation and returns the node
	 * of the new root. 
	 *
	 * @param parent
	 * @param data
	 * @return node of the root after rotaion (subtree)
	 */
	public Node doubleLeftRotation(Node parent,  DataContainer data) {
//		
		data.numRot++;
		data.numRot++;
		Node child = parent.right;
		Node grandChild = child.left;
		parent.right = grandChild.left;
		grandChild.left = parent;
		child.left = grandChild.right;
		grandChild.right = child;	
		if(grandChild.balance == Code.LEFT){
			parent.balance = Code.SAME;
			child.balance = Code.RIGHT;
		}
		else if(grandChild.balance == Code.RIGHT){
			child.balance = Code.SAME;
			parent.balance = Code.LEFT;
		}
		else {
			parent.balance = Code.SAME;
			child.balance = Code.SAME;
		}
		grandChild.balance = Code.SAME;
		child.rank -= 1+ grandChild.rank;
		grandChild.rank += parent.rank + 1;
		
		data.continueRot = false;
		return grandChild;
	}
	/**
	 * 
	 * This method preforms a double right rotation and returns the node
	 * of the new root. 
	 *
	 * @param parent
	 * @param data
	 * @return node of the root after rotaion (subtree)
	 */
	public Node doubleRightRotation(Node parent, DataContainer data) {
//		Node temp = parent.singleLeftRotation(parent.left, data);
//		Node child = temp.singleRightRotation(temp, data);
		data.numRot++;
		data.numRot++;
		Node child = parent.left;
		Node grandChild = child.right;
		parent.left = grandChild.right;
		grandChild.right = parent;
		child.right = grandChild.left;
		grandChild.left = child;
		if(grandChild.balance == Code.RIGHT){
			parent.balance = Code.SAME;
			child.balance = Code.LEFT;
		}
		else if(grandChild.balance == Code.LEFT){
			child.balance = Code.SAME;
			parent.balance = Code.RIGHT;
		}
		else {
			parent.balance = Code.SAME;
			child.balance = Code.SAME;
		}
		grandChild.balance = Code.SAME;
		parent.rank -= child.rank + grandChild.rank + 2;
		grandChild.rank += child.rank + 1;
		data.continueRot = false;
		return grandChild;
	}

	/**
	 * MILESTONE 1
	 * 
	 * @param ch
	 *            character to add
	 * @param pos
	 *            character added in this inorder position
	 * @throws IndexOutOfBoundsException
	 *             id pos is negative or too large for this tree
	 */
	public void add(char ch, int pos) throws IndexOutOfBoundsException {

		if(pos < 0){
			throw new IndexOutOfBoundsException();
		}
		this.data.heightChange = false;
		if(this.root ==NULL_NODE && pos == 0) {
			this.root = new Node(ch);
			this.pos++;
			return;
		}
		this.root = this.root.insert(ch, pos, this.data).node;
		this.pos++;
		size++;
	}

	/**
	 * MILESTONE 1
	 * 
	 * @param pos
	 *            position in the tree
	 * @return the character at that position
	 * @throws IndexOutOfBoundsException
	 */
	public char get(int pos) throws IndexOutOfBoundsException {
		/**+
		 * Should initially check for an index out of bounds exception by 
		 * seeing if the position is greater than zero but also less than
		 * the max position.
		 * 
		 * Then the method needs to call a recusive helper wich uses the 
		 * rank and pos ition passed in to locate a node and return it. 
		 * this method should then return its element. 
		 */
		if(pos<0 || pos > this.pos -1){
			throw new IndexOutOfBoundsException();
		}
		if(pos == 0 && this.pos == 0){
			throw new IndexOutOfBoundsException();
		}
		return this.root.search(pos).element;
	}

	/**
	 * MILESTONE 1
	 * 
	 * @return the height of this tree
	 */
	public int height() {
		if(this.root == this.NULL_NODE){
			return -1;
		}
		return this.root.height() -1 ;
	}
		
	/**
	 * MILESTONE 2
	 * 
	 * @return the number of nodes in this tree
	 */
	public int size() {
		return this.root.size();
	}

	/**
	 * MILESTONE 2
	 * 
	 * @param pos
	 *            position of character to delete from this tree
	 * @return the character that is deleted
	 * @throws IndexOutOfBoundsException
	 */
	public char delete(int pos) throws IndexOutOfBoundsException {
		// Implementation requirement:
		// When deleting a node with two children, you normally replace the
		// node to be deleted with either its in-order successor or predecessor.
		// The tests assume assume that you will replace it with the
		// *successor*.

		if(pos <0 || this.root == NULL_NODE) {
			throw new IndexOutOfBoundsException();
		}
		Wrapper toReturn = this.root.remove(pos, this.data);
		this.pos--;
		this.root = toReturn.node;// replace by a real calculation.
		
		return toReturn.element;
	}

	/**
	 * MILESTONE 3, EASY This method operates in O(length*log N), where N is the
	 * size of this tree.
	 * 
	 * @param pos
	 *            location of the beginning of the string to retrieve
	 * @param length
	 *            length of the string to retrieve
	 * @return string of length that starts in position pos
	 * @throws IndexOutOfBoundsException
	 *             unless both pos and pos+length-1 are legitimate indexes
	 *             within this tree.
	 */
	public String get(int pos, int length) throws IndexOutOfBoundsException {

		String toReturn = "";
		int j = pos;
		for(int i = 0; i < length; i ++){
			toReturn = toReturn + this.root.search(j).element;
			j++;
		}
		
		return toReturn;
		
	}

	/**
	 * MILESTONE 3, MEDIUM - SEE PAPER REFERENCED IN SPEC FOR ALGORITHM! Append
	 * (in time proportional to the log of the size of the larger tree) the
	 * contents of the other tree to this one. Other should be made empty after
	 * this operation.
	 * 
	 * @param other
	 * @throws IllegalArgumentException
	 *             if this == other
	 */
	public void concatenate(EditTree other) throws IllegalArgumentException {
		if(this == other){
			throw new IllegalArgumentException();
		}
		
		if(this.root == NULL_NODE){
			this.root = other.getRoot();
			other.root = NULL_NODE;
			this.pos = this.size();
			return;
		}
		
		if(other.getRoot() == NULL_NODE){
			return;
		}
		
		int heightThis = this.height();
		int heightOther = other.height();
		int tallest;
		EditTree smaller;
		EditTree bigger;
		if(heightThis <= heightOther){
			smaller = this;
			bigger = other;
			tallest = heightOther;
			
		}
		else{
			smaller = other;
			bigger = this;
			tallest = heightThis;

		}
		if(this == bigger){
			Node q = new Node(smaller.delete(0));
			int tH = tallest-smaller.height()-1;
			q.right = smaller.getRoot();
			this.root = bigger.root.rightPaste(q, smaller.size(),tH, this.data).node;
		}
		else{
			int smallerSize = smaller.size();
			Node q = new Node(smaller.delete(smallerSize-1));
			int tH = tallest-smaller.height()-1;
			q.left = smaller.getRoot();
			this.root = bigger.root.leftPaste(q, smallerSize, tH,this.data).node;
			
			
		}
		this.pos = this.size();
		other.root = NULL_NODE;
	
	}

	/**
	 * MILESTONE 3: DIFFICULT This operation must be done in time proportional
	 * to the height of this tree.
	 * 
	 * @param pos
	 *            where to split this tree
	 * @return a new tree containing all of the elements of this tree whose
	 *         positions are >= position. Their nodes are removed from this
	 *         tree.
	 * @throws IndexOutOfBoundsException
	 */
	public EditTree split(int pos) throws IndexOutOfBoundsException {
		return this;
	}

	/**
	 * MILESTONE 3: JUST READ IT FOR USE OF SPLIT/CONCATENATE This method is
	 * provided for you, and should not need to be changed. If split() and
	 * concatenate() are O(log N) operations as required, delete should also be
	 * O(log N)
	 * 
	 * @param start
	 *            position of beginning of string to delete
	 * 
	 * @param length
	 *            length of string to delete
	 * @return an EditTree containing the deleted string
	 * @throws IndexOutOfBoundsException
	 *             unless both start and start+length-1 are in range for this
	 *             tree.
	 */
	public EditTree delete(int start, int length) throws IndexOutOfBoundsException {
		if (start < 0 || start + length >= this.size())
			throw new IndexOutOfBoundsException(
					(start < 0) ? "negative first argument to delete" : "delete range extends past end of string");
		EditTree t2 = this.split(start);
		EditTree t3 = t2.split(length);
		this.concatenate(t3);
		return t2;
	}

	/**
	 * MILESTONE 3 Don't worry if you can't do this one efficiently.
	 * 
	 * @param s
	 *            the string to look for
	 * @return the position in this tree of the first occurrence of s; -1 if s
	 *         does not occur
	 */
	public int find(String s) {
		if(s.equals("")){
			return 0;
		}
		return this.find(s,0);
	}

	/**
	 * MILESTONE 3
	 * 
	 * @param s
	 *            the string to search for
	 * @param pos
	 *            the position in the tree to begin the search
	 * @return the position in this tree of the first occurrence of s that does
	 *         not occur before position pos; -1 if s does not occur
	 */
	public int find(String s, int pos) {
		
		String tree = this.toString();
		String reset = s;
		int toReturn = -1;
		System.out.println("tree: " + tree);
		
		for (int i = pos; i < tree.length(); i++)
		{
			if (s.length() == 1 && s.charAt(0) == tree.charAt(i) && reset.length() == 1){
				System.out.println("HERE");
				s = reset;
				return i;
			}
			if (s.length() == 1 && s.charAt(0) == tree.charAt(i)){
					s = reset;
					return i - reset.length()+1;
				}
			if (s.charAt(0) != tree.charAt(i)){
				System.out.println("s: "+s.charAt(0)+" t: "+tree.charAt(i));
				System.out.println("reset");
				s = reset;
			}
			
			if (s.charAt(0) == tree.charAt(i))
			{
				System.out.println("Cut");
				s = s.substring(1);
				System.out.println(s);
			}
			
		}
		
		return toReturn;
	}

	/**
	 * @return The root of this tree.
	 */
	public Node getRoot() {
		//System.out.println("This is get root: "+this.root);
		return this.root;
	}
}
