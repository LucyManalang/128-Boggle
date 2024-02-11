package comp128.boggle;

import java.util.Map;
import java.util.HashSet;

/**
 * DictionaryTree implimentation mostly taken from PrefixTree in HW3
 * Did not write any new tests, as all methods in this class were tested and confirmed to be working in HW3
 */
public class DictionaryTree {
    private TreeNode root; 
    private TreeNode currentNode;
    private Map<Character, TreeNode> children;

    public DictionaryTree(HashSet<String> wordSet){
        root = new TreeNode();
        
        for (String string : wordSet) {
            add(string);
        }
    }


    /**
     * Adds the word to the tree where each letter in sequence is added as a node
     * If the word is already in the tree, then this has no effect.
     * @param word
     */
    public void add(String word){
        Character[] charArray = toCharArray(word);
        TreeNode tempNode;

        if (contains(word)) {
            return;
        }
        currentNode = root;
        children = currentNode.children;
        for (Character character : charArray) {
            if (treeIterationHelper(character)) {
                tempNode = new TreeNode();
                currentNode.children.put(character, tempNode);
                currentNode = tempNode;
                children = currentNode.children;
                currentNode.letter = character;
            } 
        }
        currentNode.isWord = true;
    }


    /**
     * Checks whether the word has been added to the tree
     * @param word
     * @return true if contained in the tree.
     */
    public boolean contains(String word){
        Character[] charArray = toCharArray(word);
        currentNode = root;
        children = currentNode.children;

        for (Character character : charArray) {
            if (treeIterationHelper(character)) {
                return false;
            }
        }
        return currentNode.isWord;
    }


    /**
     * Helps iterate through the path of a tree, called once for each node.
     * @param character 
     * @return boolean that displays lack of existence of characterin currentNode.children. 
     *         returns true if character is not found
     */
    public boolean treeIterationHelper(Character character) {
        if (children.get(character) != null) {
            currentNode = children.get(character);
            children = currentNode.children;
            return false;
        }
        return true;
    }


    /**
     * Returns inputted string as array of Characters
     * @param word
     * @return array of Characters
     */
    public Character[] toCharArray(String word) {
        Character[] charArray = new Character[word.length()];

        for (int i = 0; i < charArray.length; i++) {
            charArray[i] = word.charAt(i);
        }
        return charArray;
    }


    /**
     * @return TreeNode currentNode
     */
    public TreeNode getCurrentNode() {
        return currentNode;
    }


    public TreeNode getRoot() {
        return root;
    }
}