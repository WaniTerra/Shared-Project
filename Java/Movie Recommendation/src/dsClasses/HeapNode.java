package dsClasses;

public class HeapNode {
    UserSimilarity data;
    HeapNode left;
    HeapNode right;
    HeapNode parent;

    public HeapNode(UserSimilarity data) {
        this.data = data;
        this.left = null;
        this.right = null;
        this.parent = null;
    }
}