package dsClasses;

public class MyMaxHeap {

    private HeapNode root;
    private int size;

    public MyMaxHeap() {
        this.root = null;
        this.size = 0;
    }

    public int size() {
        return size;
    }

    public void insert(UserSimilarity data) {
        HeapNode newNode = new HeapNode(data);

        if (root == null) {
            root = newNode;
            size++;
            return;
        }

        size++; 

        HeapNode parent = getParentOfNodeAtPosition(size);
        newNode.parent = parent;

        if (parent.left == null) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        heapifyUp(newNode);
    }

    public UserSimilarity extractMax() {
        if (root == null) {
            return null;
        }

        UserSimilarity maxData = root.data;

        if (size == 1) {
            root = null;
            size--;
            return maxData;
        }

        HeapNode lastNode = getNodeAtPosition(size);

        root.data = lastNode.data;

        HeapNode parent = lastNode.parent;
        if (parent.left == lastNode) {
            parent.left = null;
        } else {
            parent.right = null;
        }

        size--;

        heapifyDown(root);

        return maxData;
    }

   

    private void heapifyUp(HeapNode node) {
        while (node.parent != null && node.data.similarity > node.parent.data.similarity) {
            UserSimilarity temp = node.data;
            node.data = node.parent.data;
            node.parent.data = temp;

            node = node.parent;
        }
    }

    private void heapifyDown(HeapNode node) {
        while (node != null) {
            HeapNode largest = node;

            if (node.left != null && node.left.data.similarity > largest.data.similarity) {
                largest = node.left;
            }
            if (node.right != null && node.right.data.similarity > largest.data.similarity) {
                largest = node.right;
            }

            if (largest != node) {
                UserSimilarity temp = node.data;
                node.data = largest.data;
                largest.data = temp;

                node = largest;
            } else {
                break; 
            }
        }
    }

    private HeapNode getNodeAtPosition(int pos) {
        if (pos <= 0 || root == null) {
            return null;
        }

        String binaryPath = Integer.toBinaryString(pos);
        
        HeapNode current = root;

        for (int i = 1; i < binaryPath.length(); i++) {
            if (current == null) {
                break;
            }
            
            char direction = binaryPath.charAt(i);
            
            if (direction == '0') {
                current = current.left;  
            } else {
                current = current.right; 
            }
        }
        
        return current;
    }

    private HeapNode getParentOfNodeAtPosition(int pos) {
        int mask = Integer.highestOneBit(pos) >> 1;
        HeapNode current = root;

        while (mask > 1 && current != null) {
            if ((pos & mask) == 0) {
                current = current.left;
            } else {
                current = current.right;
            }
            mask >>= 1;
        }
        return current;
    }
}