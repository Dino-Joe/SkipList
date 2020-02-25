# SkipList
Generic SkipList implementation in Java. Feel free to use and abuse this code however you'd like.

Below is a brief rundown of this class and its helper class.

## SkipList Constructors
- **SkipList<>()** ---- This constructor creates a new SkipList. The height set by default to 1.

- **SkipList<>(int height)** ---- This constructor creates a new skip list and initializes the head node to have the height specified by the height parameter. If the height is less than the default height, it is set to the default instead.

## SkipList Public Methods

- **int size()** ---- Returns the number of nodes in the SkipList (excluding the head, since it does not
contain a value).

- **int height()** ---- Returns the current height of the SkipList.

- **Node\<T\> head()** ---- Returns the head of the SkipList.

- **void insert(T data)** ---- Inserts *data* into the SkipList with a random height. If there are duplicates, it inserts to the left of these.

- **void insert(T data, int height)** ---- Inserts data into the SkipList with the specified height. If there are duplicates, it inserts to the left of these.

- **void delete(T data)** ---- Deletes a single occurrence of *data* from the SkipList (if it is present). If there are duplicates, it deletes the leftmost node that contains *data*. If this method call results in the deletion of a node, it will trim the SkipList to the minimum permissible height (i.e., ceil(log(n)/log(2))).

- **boolean contains(T data)** ---- Returns true if the SkipList contains *data*. Otherwise, returns false.

- **Node\<T\> get(T data)** ---- Returns a reference to a node in the SkipList that contains *data*. If no such node exists, returns null. If multiple such nodes exist, returns the leftmost one.
