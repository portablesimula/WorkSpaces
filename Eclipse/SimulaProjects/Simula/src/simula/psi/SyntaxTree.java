package simula.psi;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import simula.compiler.syntaxClass.SyntaxClass;
import simula.compiler.utilities.Global;
import simula.compiler.utilities.KeyWord;
import simula.token.Identifier;

public class SyntaxTree {
	SyntaxClass rootClass;
	
	public SyntaxTree(SyntaxClass rootClass) {
		this.rootClass = rootClass;
	}

	public void popUp() {
		JTree tree = this.getJTree();
//		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Program Syntax Tree");
//			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        try { frame.setIconImage(Global.favicon.getImage()); } 
	        catch (Exception e) {}// Util.IERR("Impossible",e); }

			JScrollPane scrollPane = new JScrollPane(tree);
			frame.add(scrollPane, BorderLayout.CENTER);

			frame.setSize(1000, 600);
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
//		});
	}

	public JTree getJTree() {
		IO.println("SyntaxTree.getJTree: " + this);
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        DefaultTreeModel model = new DefaultTreeModel(root);
        JTree tree = new JTree(model);
        
//		JTree tree = new JTree(rootNode);
//		tree.setRootVisible(false);      // Hides the root node
		tree.setShowsRootHandles(true);   // Shows expansion icons for the new "top" level

        
//        tree.addMouseListener(new MouseAdapter() {
//            public void mousePressed(MouseEvent e) {
//        		IO.println("PsiTree'mousePressed: " + e);
//                int selRow = tree.getRowForLocation(e.getX(), e.getY());
//                TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
//                if(selRow != -1) {
//                    if(e.getClickCount() == 1) {
//                        gotSingleClick(selRow, selPath);
//                    }
//                    else if(e.getClickCount() == 2) {
//                        gotDoubleClick(selRow, selPath);
//                    }
//                }
//            }});
        
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) model.getRoot();
        
//        for(PsiElement elt:this.getChildren()) {
//        	addNodes(1, tree, model, parent, elt);
//        }
        rootClass.addSyntaxNodes(tree, model, parent);
	    
        return tree;
	}


    public static void addKeyWordIdentNode(JTree tree, DefaultTreeModel model, DefaultMutableTreeNode parent, String keyWord) {
    	
//    	String html = "<html><b><u><p style=\"color: red;\">" + KeyWord.edit(keyWord).toLowerCase() + "</p></u></b></html>";
//    	String html = "<html><b><u><p style=\"color: rgb(255, 0, 0);\">" + KeyWord.edit(keyWord).toLowerCase() + "</p></u></b></html>";
    	String html = "<html><b><u><p style=\"color: rgb(153,0,51);\">" + keyWord.toLowerCase() + "</p></u></b></html>";
//    	String redText = "<p style=\"color: red;\">Dette er rød tekst.</p>";

//    	String html = "<html><b><u>" + KeyWord.edit(keyWord).toLowerCase() + "</u></b></html>";
//        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(KeyWord.edit(keyWord));
    	
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(html);
        model.insertNodeInto(newNode, parent, parent.getChildCount());
    }

    public static void addKeyWordNode(JTree tree, DefaultTreeModel model, DefaultMutableTreeNode parent, int keyWord) {
    	
//    	String html = "<html><b><u><p style=\"color: red;\">" + KeyWord.edit(keyWord).toLowerCase() + "</p></u></b></html>";
//    	String html = "<html><b><u><p style=\"color: rgb(255, 0, 0);\">" + KeyWord.edit(keyWord).toLowerCase() + "</p></u></b></html>";
    	String html = "<html><b><u><p style=\"color: rgb(153,0,51);\">" + KeyWord.edit(keyWord).toLowerCase() + "</p></u></b></html>";
//    	String redText = "<p style=\"color: red;\">Dette er rød tekst.</p>";

//    	String html = "<html><b><u>" + KeyWord.edit(keyWord).toLowerCase() + "</u></b></html>";
//        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(KeyWord.edit(keyWord));
    	
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(html);
        model.insertNodeInto(newNode, parent, parent.getChildCount());
    }

    public static void addIdentifier(JTree tree, DefaultTreeModel model, DefaultMutableTreeNode parent, String identifier) {
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(identifier);
        model.insertNodeInto(newNode, parent, parent.getChildCount());
    }

}
