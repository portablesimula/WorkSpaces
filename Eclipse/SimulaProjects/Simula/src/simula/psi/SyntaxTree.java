package simula.psi;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import simula.compiler.syntaxClass.SyntaxClass;
import simula.compiler.utilities.Global;

public class SyntaxTree {
	SyntaxClass syntaxClass;
	
	public SyntaxTree(SyntaxClass syntaxClass) {
		this.syntaxClass = syntaxClass;
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
        syntaxClass.addSyntaxNodes(tree, model, parent);
	    
        return tree;
	}

}
