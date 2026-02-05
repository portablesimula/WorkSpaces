package simula.psi;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import simula.compiler.syntaxClass.SyntaxClass;
import simula.compiler.utilities.Util;

//Base class for composite nodes (branching nodes)
public class PsiTree extends PsiElement {
	public SyntaxClass syntaxClass;
	protected final List<PsiElement> children = new ArrayList<>();
	private String error;
	
	public PsiTree(String debugName, PsiTree parent) {
		super(debugName);
		this.parent = parent;
	}

	public void addChild(PsiElement child) {
//		if (child instanceof BasePsiElement) {
//			((BasePsiElement) child).setParent(this); 
//		}
		if(child == null) Util.IERR("addChild NULL !!");
		children.add(child);
	}
	
	public void addTree(PsiTree psiTree) {
		
//		for(PsiElement elt:psiTree.children) {
//			children.add(elt);
//		}
		children.addAll(psiTree.children);
	
	}
	
	public boolean isEmpty() {
		return children.isEmpty();
	}
	
//	public LexToken removeLastChild() {
//		return (LexToken) children.removeLast();
	public PsiElement removeLastChild() {
		return children.removeLast();
	}

	public LexToken getLastChild() {
		return (LexToken) children.getLast();
	}

	public List<PsiElement> getChildren() { return children; }
	
	@Override public int getLineNumber() {
		try {
			PsiElement firstChild = children.getFirst();
			if(firstChild != null) return firstChild.getLineNumber();
		} catch(Exception e) { }
		return -1;
	}

	@Override public String getText() {
//		IO.println("PsiTree.getText: " + debugName);
		StringBuilder sb = new StringBuilder();
		for(PsiElement child:children) {
//			IO.println("PsiTree.getText: child: " + child);
			sb.append(child.getText());
		}
//		IO.println(("PsiTree.getText: " + debugName + " ==> \"" + sb + '"').replace("\n", "\\n").replace("\r", "\\r"));
		return sb.toString();
	}
	
	public JTree getJTree() {
		IO.println("PsiTree.getJTree: " + this);
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        DefaultTreeModel model = new DefaultTreeModel(root);
        JTree tree = new JTree(model);
//        MouseListener ml = new MouseAdapter() {
//            public void mousePressed(MouseEvent e) {
//                int selRow = tree.getRowForLocation(e.getX(), e.getY());
//                TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
//                if(selRow != -1) {
//                    if(e.getClickCount() == 1) {
//                        mySingleClick(selRow, selPath);
//                    }
//                    else if(e.getClickCount() == 2) {
//                        myDoubleClick(selRow, selPath);
//                    }
//                }
//            }
//        };
//        tree.addMouseListener(ml);
        tree.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
        		IO.println("PsiTree'mousePressed: " + e);
                int selRow = tree.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
                if(selRow != -1) {
                    if(e.getClickCount() == 1) {
                        gotSingleClick(selRow, selPath);
                    }
                    else if(e.getClickCount() == 2) {
                        gotDoubleClick(selRow, selPath);
                    }
                }
            }});
        
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) model.getRoot();
        
        for(PsiElement elt:this.getChildren()) {
        	addNodes(1, tree, model, parent, elt);
        }
		
//        for (int i = 0; i < tree.getRowCount(); i++) {
//            tree.expandRow(i);
//        }
    	// Initial call
    	expandAllNodes(tree, 0, tree.getRowCount());
        
        model.reload(root); 
        return tree;
	}
	
	private void addNodes(int indent, JTree tree,DefaultTreeModel model, DefaultMutableTreeNode parent, PsiElement elt) {
//		char cc = (char)(0x00B6);
		char cc = (char)(0x204B);
		String xxx = "" + cc + elt.getLineNumber() + ": " + elt.debugName + " |" + elt.getText() +"|";
//        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(xxx);
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(elt);
        model.insertNodeInto(newNode, parent, parent.getChildCount());
		if(elt instanceof PsiTree psiTree) {
	        for(PsiElement subelt:psiTree.getChildren()) {
	        	addNodes(indent++, tree, model, newNode, subelt);
	        }
		}
	}

	private void gotSingleClick(int selRow, TreePath selPath) {
		IO.println("PsiTree.getJTree: gotSingleClick: selRow=" + selRow);
		IO.println("PsiTree.getJTree: gotSingleClick: selPath=" + selPath);
		IO.println("PsiTree.getJTree: gotSingleClick: lastPathComponent=" + selPath.getLastPathComponent().getClass().getSimpleName());
		IO.println("PsiTree.getJTree: gotSingleClick: lastPathComponent=" + selPath.getLastPathComponent());
		DefaultMutableTreeNode last = (DefaultMutableTreeNode) selPath.getLastPathComponent();
		IO.println("PsiTree.getJTree: gotSingleClick: userObject=" + last.getUserObject().getClass().getSimpleName());
		PsiElement psiElement = (PsiElement) last.getUserObject();
		IO.println("PsiTree.getJTree: gotSingleClick: GOT: " + psiElement);
//		Util.IERR("");
	}

	private void gotDoubleClick(int selRow, TreePath selPath) {
		IO.println("PsiTree.getJTree: gotSingleClick: selRow=" + selRow);
//		Util.IERR("");
	}

	
	private void expandAllNodes(JTree tree, int startingIndex, int rowCount) {
	    for (int i = startingIndex; i < rowCount; ++i) {
	        tree.expandRow(i);
	    }

	    if (tree.getRowCount() != rowCount) {
	        // If expanding rows added new visible rows, recurse to expand them too
	        expandAllNodes(tree, rowCount, tree.getRowCount());
	    }
	}




	public void popUp() {
		JTree tree = this.getJTree();
//		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("JTree Scroll Example");
//			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			JScrollPane scrollPane = new JScrollPane(tree);
			frame.add(scrollPane, BorderLayout.CENTER);

			frame.setSize(1000, 600);
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
//		});
	}

    public void printPsiTree(String title) {
    	IO.println("====== PrintTree: " + title + " ======");
    	printPsiTree(this, 1);
    }

    private static void printPsiTree(PsiElement element, int depth) {
    	int line = element.getLineNumber();
    	String text = element.getText().replace("\r", "\\r").replace("\n", "\\n");
    	System.out.println("  ".repeat(depth) + "Line " + line + ": " + element.getClass().getSimpleName() + "("+element.debugName+"): [" + text + "]");
        if(element instanceof PsiTree subTree) {
	        for (PsiElement child : subTree.getChildren()) {
	            printPsiTree(child, depth + 1);
	        }
        }
    }

	@Override public String toString() {
		return "PsiTree(" + debugName + ") Text=\"" + getText().replace("\n", "\\n").replace("\r", "\\r") + '"';
	}

}