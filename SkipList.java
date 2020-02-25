// Joseph Walsh
// COP 3503, Spring 2020

// I dedicate any and all copyright interest in this software to the
// public domain. I make this dedication for the benefit of the public at
// large and to the detriment of my heirs and successors. I intend this
// dedication to be an overt act of relinquishment in perpetuity of all
// present and future rights to this software under copyright law.

// =========================
// SkipList.java
// =========================

import java.util.*;

class Node<T>
{
	private static final Random r = new Random();
	private T data;
	private ArrayList<Node<T>> next;

	// constructs empty node of specified height
	Node(int height)
	{
		next = new ArrayList<>(height + 1);

		// fills next array so height() works
		for (int i = 0; i < height; i++)
			grow();
	}

	// puts actual data into the node
	Node(T data, int height)
	{
		this(height);
		this.data = data;
	}

	// gets value from node
	public T value()
	{
		return data;
	}

	// gets max height of node
	public int height()
	{
		return next.size();
	}

	// gets next node at the specified level
	public Node<T> next(int level)
	{
		if ((level < 0) || (level > (height() - 1)))
			return null;

		return next.get(level);
	}

	// =========================
	// Utility Methods
	// =========================

	// sets next reference at given level within node
	public void setNext(int level, Node<T> node)
	{
		next.set(level, node);
	}

	// grows node height by exactly one
	public void grow()
	{
		next.add(null);
	}

	// 50% chance of growing node
	// used when resizing skip list
	public void maybeGrow()
	{
		if (r.nextBoolean()) 
			grow();
	}

	// trims height to new specified height
	public void trim(int height)
	{
		if (height >= height()) 
			return;

		next.subList(height, height()).clear();
	}
}

// ===========================================

public class SkipList<T extends Comparable<T>>
{
	private static final int MIN_HEIGHT = 1;
	private static final Random r = new Random();
	private final Node<T> head, searchHistory;
	private int size, searchIndex;

	// constructs a new skip list
	SkipList()
	{
		this(MIN_HEIGHT);
	}

	// constructs skip list of specified max height
	SkipList(int height)
	{
		head = new Node<>((height >= MIN_HEIGHT) ? height : MIN_HEIGHT);
		searchHistory = new Node<>(height() + 1);
		size = 0;
	}

	// gets size of skip list
	public int size()
	{
		return size;
	}

	// gets height of skip list
	public int height()
	{
		return head().height();
	}

	// returns head of skip list
	public Node<T> head()
	{
		return head;
	}

	// inserts data into skip list with random height
	public void insert(T data)
	{
		int height = generateRandomHeight(height());

		insert(data, height);
	}

	// inserts data into skip list, with nonrandom height
	public void insert(T data, int height)
	{
		Node<T> node = new Node<>(data, height);

		browseSkipList(data);

		// splices new node into SkipList
		for (int level = height; --level >= 0;)
		{
			Node<T> parent = searchHistory.next(level);

			if (parent != null)
			{
				Node<T> orphan = parent.next(level);

				// new node adopts orphaned reference
				node.setNext(level, orphan);

				// parent adopts new node
				parent.setNext(level, node);
			}
		}

		size++;

		// grows the list vertically, if necessary
		if (height() < getMaxHeight(size()))
			growSkipList();
	}

	// deletes leftmost instance of data from list
	public void delete(T data)
	{
		Node<T> deadNode = get(data);

		if (deadNode == null) 
			return;

		size--;

		// shrinks list vertically, if necessary
		if (height() > getMaxHeight(size()))
			trimList();

		// ex-parents of dead node adopt its children
		for (int level = deadNode.height(); --level >= 0;)
		{
			Node<T> parent = searchHistory.next(level);

			Node<T> orphan = deadNode.next(level);

			parent.setNext(level, orphan);
		}
	}

	// returns true iff skip list contains data
	public boolean contains(T data)
	{
		browseSkipList(data);

		Node<T> test = searchHistory.next(searchIndex);

		if (test == null)
			return false;

		if (test.value().compareTo(data) != 0)
			return false;
		
		return true;
	}

	// gets left-most node containing requested data
	// returns null if not in skip list
	public Node<T> get(T data)
	{
		if (contains(data)) 
			return searchHistory.next(searchIndex);
		
		return null;
	}

	// =========================
	// Utility Methods
	// =========================

	// Searches for a piece of data, saving history as it does so
	private void browseSkipList(T searchTerm)
	{
		Node<T> temp = head(), peekAhead, alreadyChecked = null;

		// traverses the list
		for (int level = height(); --level >= 0;)
		{
			// traverses a level until it would fall off list or go beyond the searchTerm.
			while(true)
			{
				peekAhead = temp.next(level);

				// null indicates end of level
				if (peekAhead == null)
					break;

				// no point in comparing if node was just visited!
				// limits value comparisons in case they are very expensive
				if (peekAhead == alreadyChecked)
					break;

				// checks if we've gone beyond the search term
				if (peekAhead.value().compareTo(searchTerm) >= 0)
				{
					alreadyChecked = peekAhead;
					break;
				}

				temp = peekAhead;
			}

			// stores the drop-down node for this level of the skip list.
			// This data is used by insert() and delete().
			searchHistory.setNext(level, temp);
		}

			// The highest level stores the first instance of our search term...
			// if it exists. This data is used by get() and contains().
			searchHistory.setNext(height(), temp.next(0));
			searchIndex = searchHistory.height() - 1;
	}

	// calculates max height for skip list of size n
	private static int getMaxHeight(int n)
	{
		if (n <= 0)
			return MIN_HEIGHT;
		if (n == 1)
			return 1;
		
		int max = (int) (Math.ceil(Math.log(n) / Math.log(2.0)));

		return max;
	}

	// gets a random height on [1, maxHeight]
	// heights are geometrically distributed
	private static int generateRandomHeight(int maxHeight)
	{
		int coinFlip = r.nextInt();

		// ensures first set bit location > maxHeight
		coinFlip |= -1 << (maxHeight - 1);

		// location of the last set bit for a random int follows ~ G(0.5).
		// that lets us simulate coin flips in O(1) time.
		int height = Integer.numberOfTrailingZeros(coinFlip) + 1;

		return height;
	}

	// grows skip list by one level
	private void growSkipList()
	{
		int newHeight = height();
		Node<T> temp = head(), peekAhead, lastOnHigh;

		// grow searchHistory node
		searchHistory.grow();
		searchIndex++;

		// grow the head
		temp.grow();

		lastOnHigh = temp;

		// randomly grow the largest nodes
		while ((peekAhead = temp.next(newHeight - 1)) != null)
		{
			peekAhead.maybeGrow();

			// if it grew, connect it into the highest level.
			if (peekAhead.height() == height())
			{
				lastOnHigh.setNext(newHeight, peekAhead);
				lastOnHigh = peekAhead;
			}

			temp = peekAhead;
		}
	}

	// trims skip list if unnecessarily tall
	private void trimList()
	{
		int newHeight = getMaxHeight(size());
		Node<T> temp = head(), peekAhead;

		// trim searchHistory node
		searchHistory.trim(newHeight + 1);
		searchIndex = searchHistory.height() - 1;

		// trim head
		temp.trim(newHeight);

		// trim every node along the new height down to size
		while ((peekAhead = temp.next(newHeight - 1)) != null)
		{
			temp = peekAhead;
			temp.trim(newHeight);
		}
	}
}
