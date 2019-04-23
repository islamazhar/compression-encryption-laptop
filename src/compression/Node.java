package compression;

// Huffman trie node
public class Node implements Comparable<Node> {
    public final char ch;
    public final int freq;
    public  Node left, right;

    Node(char ch, int freq, Node left, Node right) {
        this.ch    = ch;
        this.freq  = freq;
        this.left  = left;
        this.right = right;
    }

    // is the node a leaf node?
    public boolean isLeaf() {
        assert ((left == null) && (right == null)) || ((left != null) && (right != null));
        return (left == null) && (right == null);
    }
    public void  swap() {
        Node t = left;
        left = right;
        right = t;
    }
    // compare, based on frequency
    public int compareTo(Node that) {
        return this.freq - that.freq;
    }
}
