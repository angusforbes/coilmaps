package coilcircles;

/* CoilTree.java (created on Aug 3, 2008) */

import coilmaps.GeomCoilTreeMap.DirEnum;
import java.util.ArrayList;
import java.util.List;
import utils.Utils;

/**
 * A CoilTree is simply the root CoilNode of a tree of CoilNodes. It contains methods for navigating
 * and parsing the tree of CoilNodes.
 *  
 * @author Angus Forbes
 */
public class CoilTree extends CoilNode
{
	//public static enum DirEnum {HORIZONTAL, VERTICAL};

	List<CoilNode> leafNodes = new ArrayList<CoilNode>();
	List<CoilNode> cutNodes = new ArrayList<CoilNode>();
	
	public CoilTree(float x, float y, float w, float h)
	{
		super(0, null, x, y, w, h);
	}

	public void incrementCount(CoilNode node)
	{
		node.count++;

		CoilNode parentNode = node.parent;
						
		while (parentNode != null)
		{
			parentNode.count++;
			parentNode = parentNode.parent;
		}	
		
		updateCuts(node);
		updatePositions(this);
	}
	
	public void updateCuts(CoilNode node)
	{
		//recalculate percentages...
	
		CoilNode parentNode = node.parent;
		
		if (parentNode != null)
		{
			parentNode.cut = (float) ((float)parentNode.side1.count / (float)(parentNode.side1.count + parentNode.side2.count)); 
		
			updateCuts(parentNode);
		}
	}
	
	public List<CoilNode> getLeafNodes()
	{
		return leafNodes;
	}
	
	
	private void updatePositions(CoilNode parent)
	{
		if (parent.isLeaf == true)
		{
			return;
		}
		switch(parent.dir)
		{
			case HORIZONTAL:
				parent.side1.setPos(parent.x, parent.y, 
								parent.w, parent.h * parent.cut);
				parent.side2.setPos(parent.x, parent.y + (parent.h * parent.cut), 
								parent.w, parent.h - (parent.h * parent.cut));
								
				break;

			case VERTICAL:
				parent.side1.setPos(parent.x, parent.y, 
								parent.w * parent.cut, parent.h);
				parent.side2.setPos(parent.x + (parent.w * parent.cut), parent.y, 
								parent.w - (parent.w * parent.cut), parent.h);
								
				break;
		}
		
		updatePositions(parent.side1);
		updatePositions(parent.side2);
	}
	
	public void initializeUniformNodes(int maxLevel)
	{
		initializeUniformNodes(this, 1, maxLevel);
		
		for (CoilNode leafNode : leafNodes)
		{
			incrementCount(leafNode);
		}
		
		updatePositions(this);
	}
	private void initializeUniformNodes(CoilNode parent, int level, int maxLevel)
	{
		cutNodes.add(parent);
			
		parent.isLeaf = false;
		
		switch(parent.dir)
		{
			case HORIZONTAL:
				//parent.side1 = new CoilNode(level, parent, parent.x, parent.y, parent.w, parent.h * parent.cut);
				parent.side1 = new CoilNode(level, parent, .5f, DirEnum.VERTICAL);
				parent.side2 = new CoilNode(level, parent, .5f, DirEnum.VERTICAL);
				
				//parent.side1.cut = .5f;
				//parent.side1.dir = DirEnum.VERTICAL;
				//parent.side2 = new CoilNode(level, parent, parent.x, parent.y + (parent.h * parent.cut), parent.w, parent.h * parent.cut);
				//parent.side2.cut = .5f;
				//parent.side2.dir = DirEnum.VERTICAL;
				break;
			case VERTICAL:
				parent.side1 = new CoilNode(level, parent, .5f, DirEnum.HORIZONTAL);
				parent.side2 = new CoilNode(level, parent, .5f, DirEnum.HORIZONTAL);
				//parent.side1 = new CoilNode(level, parent, parent.x, parent.y, parent.w * parent.cut, parent.h);
				//parent.side1.cut = .5f;
				//parent.side1.dir = DirEnum.HORIZONTAL;
				//parent.side2 = new CoilNode(level, parent, parent.x + (parent.w * parent.cut), parent.y, parent.w * parent.cut, parent.h);
				//parent.side2.cut = .5f;
				//parent.side2.dir = DirEnum.HORIZONTAL;
				break;
		}

		if (level == maxLevel)
		{
			//calc color...
			parent.side1.isLeaf = true;
			leafNodes.add(parent.side1);
			parent.side2.isLeaf = true;
			leafNodes.add(parent.side2);

			return;
		}
		else
		{ 
			//cutNodes.add(parent.side1);
			initializeUniformNodes(parent.side1, level + 1, maxLevel);
			//cutNodes.add(parent.side2);
			initializeUniformNodes(parent.side2, level + 1, maxLevel);
		
			return;
		}
	}
	
	public void inititalizeRandomNodes(int maxLevel)
	{
		initializeRandomNodes(this, 1, maxLevel);
		leafNodes = getLeafNodes();
		
		for (CoilNode leafNode : leafNodes)
		{
			incrementCount(leafNode);
		}
	}
	
	private void initializeRandomNodes(CoilNode parent, int level, int maxLevel)
	{
		parent.isLeaf = false;
		
		switch(parent.dir)
		{
			case HORIZONTAL:
				parent.side1 = new CoilNode(level, parent, parent.x, parent.y, parent.w, parent.h * parent.cut);
				parent.side1.cut = Utils.randomFloat();
				parent.side1.dir = DirEnum.VERTICAL;
				parent.side2 = new CoilNode(level, parent, parent.x, parent.y + (parent.h * parent.cut), 
								parent.w, parent.h - (parent.h * parent.cut));
				parent.side2.cut = Utils.randomFloat();
				parent.side2.dir = DirEnum.VERTICAL;
				break;
			case VERTICAL:
				//parent.side1 = new CoilNode(level, parent.x, parent.y, parent.w * parent.cut, parent.h);
				parent.side1 = new CoilNode(level, parent, parent.x, parent.y, parent.w * parent.cut, parent.h);
				parent.side1.cut = Utils.randomFloat();
				parent.side1.dir = DirEnum.HORIZONTAL;
				parent.side2 = new CoilNode(level, parent, parent.x + (parent.w * parent.cut), parent.y, 
								parent.w - (parent.w * parent.cut), parent.h);
				parent.side2.cut = Utils.randomFloat();
				parent.side2.dir = DirEnum.HORIZONTAL;
				break;
		}

		if (level == maxLevel)
		{
			//calc color...
			parent.side1.isLeaf = true;
			leafNodes.add(parent.side1);
			parent.side2.isLeaf = true;
			leafNodes.add(parent.side2);
			return;
		}
		
		initializeRandomNodes(parent.side1, level + 1, maxLevel);
		initializeRandomNodes(parent.side2, level + 1, maxLevel);
	}

	/**
	 * Returns a list view of all nodes in the tree.
	 * @return a List of CoilNodes.
	 */
	public List<CoilNode> flatten()
	{
		List<CoilNode> nodes = new ArrayList<CoilNode>();
		flatten(this, nodes);
		return nodes;
	}

	private void flatten(CoilNode node, List<CoilNode> nodes)
	{
		System.out.println("adding... " + node);
		nodes.add(node);

		if (node.isLeaf == true)
		{
			return;
		}
		else
		{
			flatten(node.side1, nodes);
			flatten(node.side2, nodes);
		}
	}
	
	public void printNodes()
	{
		printNodes(this);
	}
	
	public void printNodes(CoilNode node)
	{
		System.out.println(node);
		
		if (node.side1 != null)
		{
		System.out.println("" + Utils.tabs(node.level) + "side 1 :");
		printNodes(node.side1);
		}
		if (node.side2 != null)
		{
		System.out.println("" + Utils.tabs(node.level) + "side 2 :");
		printNodes(node.side2);
		}
	}

	public CoilNode findLeafNode(float x, float y)
	{
		return findLeafNode(this, x, y);
	}
	
	public CoilNode findLeafNode(CoilNode node, float x, float y)
	{
		CoilNode which = null;
		
		switch(node.dir)
		{
				case VERTICAL:
				//System.out.print("is x (" + x + ") < " + node.side2.x + "? ");
				if (x < node.side2.x)
				{
					//System.out.println("...yes, use side1 ");
					which = node.side1;
				}
				else
				{
					//System.out.println("...no, use side2 ");
					which = node.side2;
				}
				break;
				
			case HORIZONTAL:
				//System.out.print("is y (" + y + ") < " + node.side2.y + "? ");
				if (y < node.side2.y)
				{
					//System.out.println("...yes, use side1 ");
					which = node.side1;
				}
				else
				{
					//System.out.println("...no, use side2 ");
					which = node.side2;
				}
				break;
		}
		
		if (which.isLeaf == true)
		{
			return which;
		}
		else
		{
			return findLeafNode(which, x, y);
		}
	}
}
