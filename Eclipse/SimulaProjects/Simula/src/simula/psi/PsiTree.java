package simula.psi;

import simula.compiler.syntaxClass.SyntaxClass;
import simula.compiler.utilities.Global;
import simula.compiler.utilities.Html;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

//Base class for composite nodes (branching nodes)
public class PsiTree extends PsiElement {
	public SyntaxClass syntaxClass;
	protected final List<PsiElement> children = new ArrayList<>();
//	private String error;
	
	public PsiTree(String debugName, PsiTree parent) {
		super(debugName);
		this.parent = parent;
	}
	
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
	
	@Override public int firstLineNumber() {
		try {
			PsiElement firstChild = children.getFirst();
			if(firstChild != null) return firstChild.firstLineNumber();
		} catch(Exception e) { }
		return -1;
	}
	
	@Override public int lastLineNumber() {
		try {
//			PsiElement lastChild = getLastParserChild();
			PsiElement lastChild = children.getLast();
//			if(lastChild != null) return lastChild.getLineNumber();
			if(lastChild != null) return lastChild.lastLineNumber();
		} catch(Exception e) { }
		return firstLineNumber();
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
	
	private JTree doRenderPsiTreeAction() {
		IO.println("PsiTree.doRenderPsiTreeAction: " + this);
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        DefaultTreeModel model = new DefaultTreeModel(root);
        JTree tree = new JTree(model);
        tree.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
//        		IO.println("PsiTree'mousePressed: " + e);
                int selRow = tree.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
                if(selRow != -1) {
                    if(e.getClickCount() > 0) gotClick(selRow, selPath);
                }
        }});
        
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) model.getRoot();
        for(PsiElement elt:this.getChildren()) {
        	addNodes(1, tree, model, parent, elt);
        }
        model.reload(root); 
//	    tree.repaint();
        return tree;
	}
	
	private void addNodes(int indent, JTree tree,DefaultTreeModel model, DefaultMutableTreeNode parent, PsiElement elt) {
//		char cc = (char)(0x00B6);
//		char cc = (char)(0x204B);
//		String xxx = "" + cc + elt.getLineNumber() + ": " + elt.debugName + " |" + elt.getText() +"|";
//        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(xxx);
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(elt);
//		int lno = elt.getLineNumber();
//		int lastLine = elt.lastLineNumber();
//        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(Html.edPsi(lno, lastLine, elt.toString()));
        model.insertNodeInto(newNode, parent, parent.getChildCount());
		if(elt instanceof PsiTree psiTree) {
	        for(PsiElement subelt:psiTree.getChildren()) {
	        	addNodes(indent++, tree, model, newNode, subelt);
	        }
		}
	}

	private void gotClick(int selRow, TreePath selPath) {
		DefaultMutableTreeNode last = (DefaultMutableTreeNode) selPath.getLastPathComponent();
		IO.println("PsiTree.doRenderPsiTreeAction: gotSingleClick: userObject=" + last.getUserObject().getClass().getSimpleName());
		PsiElement psiElement = (PsiElement) last.getUserObject();
   		IO.println("PsiTree.doRenderPsiTreeAction: gotSingleClick: GOT PSI ELEMENT:  " + psiElement);
		if(psiElement instanceof PsiTree psiTree) {
			IO.println("PsiTree.doRenderPsiTreeAction: gotSingleClick: GOT SYNTAX CLASS: " + psiTree.syntaxClass.getClass().getSimpleName() + " " + psiTree.syntaxClass);
			if(psiTree.syntaxClass != null) {
//				psiTree.syntaxClass.printTree(1, "GOT SYNTAX CLASS: ");
//				SyntaxTree syntaxTree = new SyntaxTree(psiTree.syntaxClass);
//				syntaxTree.popUp(psiTree.toString());
				popUpPsiPanel(psiElement);//(psiTree.toString());
			}
		}
		
//		Util.IERR("");
	}
	
	
	public void popUpPsiPanel(PsiElement elt) {
//		JTree tree = this.doRenderPsiTreeAction();
		JPanel panel = getPanel(elt);
//		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Program Structure Tree Info");
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        try { frame.setIconImage(Global.favicon.getImage()); } 
	        catch (Exception e) {}// Util.IERR("Impossible",e); }

			JScrollPane scrollPane = new JScrollPane(panel);
			frame.add(scrollPane, BorderLayout.CENTER);

			frame.setSize(1000, 600);
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
//		});
	}
	
	private JPanel getPanel(PsiElement elt) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Vertical stacking
		panel.add(new JLabel("Dette er label 1"));
		panel.add(new JLabel("PsiElement: " + elt.getClass().getSimpleName()));
		panel.add(new JLabel("DebugName: " + elt.debugName));
		panel.add(new JLabel("Parent: " + elt.parent));
		if(elt instanceof PsiTree psiTree) {
			panel.add(new JLabel("SyntaxClass: " + psiTree.syntaxClass));
			if(psiTree.syntaxClass != null) {
				panel.add(new JLabel("SyntaxClass: " + psiTree.syntaxClass.getClass().getSimpleName() + " " + psiTree.syntaxClass));
				panel.add(new JLabel("SyntaxClass.psiTree: " + psiTree.syntaxClass.getPsiTree()));
				JButton button = new JButton("Open Syntax Tree");
		        panel.add(button);
		        
		        button.addActionListener(e -> {
		            System.out.println("Button was clicked!  elt: " + elt + e);
	    			IO.println("PsiTree.doRenderPsiTreeAction: gotSingleClick: GOT SYNTAX CLASS: " + psiTree.syntaxClass.getClass().getSimpleName() + " " + psiTree.syntaxClass);
					psiTree.syntaxClass.printTree(1, "GOT SYNTAX CLASS: ");
	    			SyntaxTree syntaxTree = new SyntaxTree(psiTree.syntaxClass);
	    			syntaxTree.popUp(toString());
	//				popUpPsiPanel();//(psiTree.toString());
		        });
			}
		}
        return panel;
	}

	public void popUp() {
		JTree tree = this.doRenderPsiTreeAction();
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
    	int line = element.firstLineNumber();
    	String text = element.getText().replace("\r", "\\r").replace("\n", "\\n");
    	IO.println("  ".repeat(depth) + "Line " + line + ": " + element.getClass().getSimpleName() + "("+element.debugName+"): [" + text + "]");
        if(element instanceof PsiTree subTree) {
	        for (PsiElement child : subTree.getChildren()) {
	            printPsiTree(child, depth + 1);
	        }
        }
    }

	@Override public String toString() {
//		return "Line-" + this.getLineNumber() + ":PsiTree(" + debugName + ":" + level() + ") Text=\"" + getText().replace("\n", "\\n").replace("\r", "\\r") + '"';
//		return "PsiTree(" + debugName + ":" + level() + ") Text=\"" + getText().replace("\n", "\\n").replace("\r", "\\r") + '"';
		String ID = "PsiTree(" + debugName + ") Text=\"" + getText().replace("\n", "\\n").replace("\r", "\\r") + '"';
		int lno = this.firstLineNumber();
		int lastLine = this.lastLineNumber();
        return Html.edPsi(lno, lastLine, ID);
	}

}