package simula.psi;

import simula.compiler.syntaxClass.SyntaxClass;
import simula.compiler.utilities.Global;
import simula.compiler.utilities.Util;

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

//Base class for composite nodes (branching nodes)
public class PsiTree extends PsiElement {
	private int LEVEL;
//	public Class<?> clazz; // Expected syntax Class
	public SyntaxClass syntaxClass;
	protected final List<PsiElement> children = new ArrayList<>();
//	private String error;
	
	public PsiTree(Class<?> clazz, String debugName, PsiTree parent) {
		super(debugName);
//		this.clazz = clazz;
		this.parent = parent;
		this.LEVEL = (parent == null)? 1 : parent.LEVEL + 1;
		checkLEVELS(this.LEVEL);
	}
	
	public int level() {
//		if(parent == null) return 1;
//		return(parent.level() +1);
		return LEVEL;
	}
	
	public void checkLEVELS() {
		checkLEVELS(this.LEVEL);
	}
	
	public void checkLEVELS(int expected) {
		if(this.level() != expected) {
			Util.IERR("PsiTree.checkLEVELS: FAILED !!!");
			Util.STOP();
		}
//		else IO.println("PsiTree.checkLEVELS: Level " + LEVEL + ": " + this.debugName + " OK");
		
		if(parent != null) {
			parent.checkLEVELS(level() - 1);
		}
	}
	
//	public boolean isDeclarationTree() {
//		return clazz == Declaration.class;
//	}
//	
//	public boolean isExpressionTree() {
//		return clazz == Expression.class;
//	}
//	
//	public boolean isStatementTree() {
//		return clazz == Statement.class;
//	}
//	
//	public boolean is(Class<?> clazz) {
//		return this.clazz == clazz;
//	}
//	
//	public boolean in(Class<?> clazz) {
//		return this.clazz.isAssignableFrom(clazz);
////		return clazz.isAssignableFrom(this.clazz);
//	}
//	
//	public static void checkLegalClass(PsiTree psiTree, Class<?> clazz) {
//		try {
//			if(psiTree.clazz != clazz) {
////				if(this.clazz.isAssignableFrom(clazz))
//				if(clazz.isAssignableFrom(psiTree.clazz))
//					Util.IERR("The class " + clazz.getSimpleName() + " is not an instance of " + psiTree.clazz.getSimpleName());
//			}
//		} catch (Exception e) {
//			Util.IERR("The 'checkLegalClass' FAILED: psiTree=" + psiTree
//					+ "\n" + " ".repeat(56)  + "clazz=" + clazz, e);						
//		}
//	}

	public void addChild(PsiElement child) {
//		if (child instanceof BasePsiElement) {
//			((BasePsiElement) child).setParent(this); 
//		}
		if(child == null) {
//			Util.IERR("addChild NULL !!");
			return;
		}
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
		if(children.getLast() instanceof LexToken token)
			return token;
		return null;
	}
	
	public LexToken getLastParserChild() {
		int n = children.size();
//		IO.println("PsiTree.getLastParserChild: "+n);
//		this.printPsiTree("PsiTree.getLastParserChild: ");
		for(int i=n-1;n>=0;i--) {
			PsiElement elt = children.get(i);
//			IO.println("PsiTree.getLastParserChild: CHECK elt "+i+": "+elt);
			if(elt instanceof LexToken token && token.isParserToken()) {
				return token;
			}
		}
		return null;
	}

	public List<PsiElement> getChildren() { return children; }
	
	@Override public int getLineNumber() {
		try {
			PsiElement firstChild = children.getFirst();
			if(firstChild != null) return firstChild.getLineNumber();
		} catch(Exception e) { }
		return -1;
	}
	
	@Override public int lastLineNumber() {
		IO.println("PsiTree.lastLineNumber: ");
//		Util.STOP();
		try {
//			PsiElement lastChild = getLastParserChild();
			PsiElement lastChild = children.getLast();
			if(lastChild != null) return lastChild.getLineNumber();
		} catch(Exception e) { }
		return getLineNumber();
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
//        		IO.println("PsiTree'mousePressed: " + e);
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
        
	    tree.repaint();
	    
        return tree;
	}
	
	private void addNodes(int indent, JTree tree,DefaultTreeModel model, DefaultMutableTreeNode parent, PsiElement elt) {
//		char cc = (char)(0x00B6);
//		char cc = (char)(0x204B);
//		String xxx = "" + cc + elt.getLineNumber() + ": " + elt.debugName + " |" + elt.getText() +"|";
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
//		IO.println("PsiTree.getJTree: gotSingleClick: selRow=" + selRow);
//		IO.println("PsiTree.getJTree: gotSingleClick: selPath=" + selPath);
//		IO.println("PsiTree.getJTree: gotSingleClick: lastPathComponent=" + selPath.getLastPathComponent().getClass().getSimpleName());
//		IO.println("PsiTree.getJTree: gotSingleClick: lastPathComponent=" + selPath.getLastPathComponent());
		DefaultMutableTreeNode last = (DefaultMutableTreeNode) selPath.getLastPathComponent();
//		IO.println("PsiTree.getJTree: gotSingleClick: userObject=" + last.getUserObject().getClass().getSimpleName());
		PsiElement psiElement = (PsiElement) last.getUserObject();
   		IO.println("PsiTree.getJTree: gotSingleClick: GOT PSI ELEMENT:  " + psiElement);
		if(psiElement instanceof PsiTree psiTree) {
			IO.println("PsiTree.getJTree: gotSingleClick: GOT SYNTAX CLASS: " + psiTree.syntaxClass.getClass().getSimpleName() + " " + psiTree.syntaxClass);
			if(psiTree.syntaxClass != null) {
				psiTree.syntaxClass.printTree(1, "GOT SYNTAX CLASS: ");
				SyntaxTree syntaxTree = new SyntaxTree(psiTree.syntaxClass);
				syntaxTree.popUp();
			}
		}
		
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
//	    tree.repaint();
	}
	
//	public static void setTreeExpandedState(JTree tree, boolean expanded) {
//        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getModel().getRoot();
//        setNodeExpandedState(tree, node, expanded);
//    }
//
////    public static void setNodeExpandedState(JTree tree, DefaultMutableTreeNode node, boolean expanded) {
////        ArrayList<DefaultMutableTreeNode> list = Collections.list(node.children());
////        for (DefaultMutableTreeNode treeNode : list) {
//    public static void setNodeExpandedState(JTree tree, TreeNode treeNode2, boolean expanded) {
//        ArrayList<TreeNode> list = (ArrayList<TreeNode>) Collections.list(treeNode2.children());
//        for (TreeNode treeNode : list) {
//            setNodeExpandedState(tree, treeNode, expanded);
//        }
//        if (!expanded && treeNode2.isRoot()) {
//            return;
//        }
//        TreePath path = new TreePath(treeNode2.getPath());
//        if (expanded) {
//            tree.expandPath(path);
//        } else {
//            tree.collapsePath(path);
//        }
//    }





	public void popUp() {
		JTree tree = this.getJTree();
//		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Program Structure Tree");
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
	
	public String edChildren() {
		StringBuilder sb = new StringBuilder();
		String sep ="";
        for (PsiElement child : getChildren()) {
            sb.append(sep).append(child);
            sep = ", ";
        }
		return sb.toString();
	}

    public void printPsiTree(String title) {
    	IO.println("====== BEGIN - PrintTree: " + title + " ======");
    	printPsiTree(this, 1);
    	IO.println("====== ENDOF - PrintTree: " + title + " ======");
    }

    private static void printPsiTree(PsiElement element, int depth) {
    	int line = element.getLineNumber();
    	String level = (element instanceof PsiTree psiTree)? (": Level "+psiTree.level()) : "";
    	String text = element.getText().replace("\r", "\\r").replace("\n", "\\n");
    	IO.println("  ".repeat(depth) + "Line " + line + level + ": " + element.getClass().getSimpleName() + "("+element.debugName+"): [" + text + "]");
        if(element instanceof PsiTree subTree) {
	        for (PsiElement child : subTree.getChildren()) {
	            printPsiTree(child, depth + 1);
	        }
        }
    }

	@Override public String toString() {
		return "Line-" + this.getLineNumber() + ":PsiTree(" + debugName + ":" + level() + ") Text=\"" + getText().replace("\n", "\\n").replace("\r", "\\r") + '"';
	}

}