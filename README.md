# SkipList
Generic skip list implementation in Java. Feel free to use and abuse this code however you'd like.

## SkipList Constructors
- **SkipList\<T\>()** ---- This constructor creates a new SkipList. The height is set by default to 1.

- **SkipList\<T\>(int height)** ---- This constructor creates a new SkipList and initializes the head node to have the height specified by the height parameter. If the height is less than the default height, it is set to the default instead.

## Public SkipList Methods

- **int size()** ---- Returns the number of nodes in the SkipList (excluding the head, since it does not
contain a value).

- **int height()** ---- Returns the current height of the SkipList.

- **Node\<T\> head()** ---- Returns the head of the SkipList.

- **void insert(T data)** ---- Inserts *data* into the SkipList with a random height. If there are duplicates, it inserts to the left of these.

- **void insert(T data, int height)** ---- Inserts data into the SkipList with the specified height. If there are duplicates, it inserts to the left of these.

- **void delete(T data)** ---- Deletes a single occurrence of *data* from the SkipList (if it is present). If there are duplicates, it deletes the leftmost node that contains *data*. If this method call results in the deletion of a node, it will trim the SkipList to the minimum permissible height (i.e., ceil(log(n)/log(2))).

- **boolean contains(T data)** ---- Returns true if the SkipList contains *data*. Otherwise, returns false.

- **Node\<T\> get(T data)** ---- Returns a reference to a node in the SkipList that contains *data*. If no such node exists, returns null. If duplicates exist, returns the first instance encountered in a standard search.

## Node Constructors
- **Node\<T\>(int height)** ---- This constructor creates a new node with the specified height, which must be greater than zero. All of the node’s *next* references are initialized to null. 

- **Node\<T\>(T data, int height)** ---- This constructor creates a new node with the specified height, which must be greater than zero, and initializes the node’s value to data. All of the node’s *next* references are initialized to null.

## Public Node Methods
- **T value()** ---- Returns the value stored at this node.

- **int height()** ---- Returns the height of this node.

- **Node\<T\> next(int level)** ---- Returns a reference to the next node in the SkipList at this particular level. Levels
are numbered 0 through (height – 1), from bottom to top. If level is less than 0 or greater than (height – 1), returns null.

- **void setNext(int level, Node\<T\> node)** ---- Updates the pointer to the next node on this level of the SkipList.

- **void grow()** ---- Grows node height by 1.

- **void maybeGrow()** ---- Has a 50% chance of growing the node height by 1.

- **void trim(int height)** ---- Reduces the height of this node to the specified height.
