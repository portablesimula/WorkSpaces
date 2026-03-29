package simula.psi;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import simula.compiler.syntaxClass.SyntaxClass;
import simula.compiler.utilities.Global;
import simula.compiler.utilities.KeyWord;

public class SyntaxTree {
	SyntaxClass rootClass;
	
	public SyntaxTree(SyntaxClass rootClass) {
		this.rootClass = rootClass;
	}

	public void popUp(String title) {
		JTree tree = this.doRenderSyntaxTreeAction();
//		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Syntax Tree: " + title);
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

	private JTree doRenderSyntaxTreeAction() {
		IO.println("SyntaxTree.doRenderSyntaxTreeAction: " + this);
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        DefaultTreeModel model = new DefaultTreeModel(root);
        JTree tree = new JTree(model);
		tree.setShowsRootHandles(true);   // Shows expansion icons for the new "top" level
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
        rootClass.addSyntaxNodes(tree, model, parent);
        model.reload(root); 
//	    tree.repaint();
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

	private void gotClick(int selRow, TreePath selPath) {
		DefaultMutableTreeNode last = (DefaultMutableTreeNode) selPath.getLastPathComponent();
		IO.println("PsiTree.doRenderPsiTreeAction: gotSingleClick: userObject=" + last.getUserObject().getClass().getSimpleName());
//		TreeNodeIdent treeNodeIdent = (TreeNodeIdent) last.getUserObject();
		if(last.getUserObject() instanceof TreeNodeIdent treeNodeIdent) {
	   		IO.println("PsiTree.doRenderPsiTreeAction: gotSingleClick: GOT TreeNodeIdent:  " + treeNodeIdent);
			if(treeNodeIdent.object instanceof SyntaxClass syntaxClass) {
				IO.println("PsiTree.doRenderPsiTreeAction: gotSingleClick: GOT SYNTAX CLASS: " + syntaxClass.getClass().getSimpleName() + " " + syntaxClass);
				if(syntaxClass != null) {
					popUpSyntaxPanel(syntaxClass);//(psiTree.toString());
				}
			}
		}
	}

	public void popUpSyntaxPanel(SyntaxClass elt) {
//		JTree tree = this.doRenderPsiTreeAction();
		JPanel panel = getPanel(elt);
//		JPanel panel = elt.getPanel();
//		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Syntax Info");
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        try { frame.setIconImage(Global.favicon.getImage()); } 
	        catch (Exception e) {}// Util.IERR("Impossible",e); }

			JScrollPane scrollPane = new JScrollPane(panel);
			frame.add(scrollPane, BorderLayout.CENTER);

//			frame.setSize(500, 600);
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
//		});
	}
	
	private JPanel getPanel(SyntaxClass elt) {
		JPanel panel = new JPanel(new BorderLayout());
//		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Vertical stacking
		panel.add(elt.getPanel(), BorderLayout.CENTER);
		PsiTree psiTree = elt.getPsiTree();
		if(psiTree != null) {
//			panel.add(new JLabel("SyntaxClass: " + psiTree.syntaxClass.getClass().getSimpleName() + " " + psiTree.syntaxClass));
//			panel.add(new JLabel("SyntaxClass.psiTree: " + psiTree.syntaxClass.psiTree));
			JButton button = new JButton("Open Psi Tree");
	        panel.add(button, BorderLayout.SOUTH);
	        
	        button.addActionListener(e -> {
	            System.out.println("Button was clicked!  elt: " + elt + e);
    			IO.println("PsiTree.doRenderPsiTreeAction: gotSingleClick: GOT SYNTAX CLASS: " + psiTree.syntaxClass.getClass().getSimpleName() + " " + psiTree.syntaxClass);
				psiTree.syntaxClass.printTree(1, "GOT SYNTAX CLASS: ");
    			psiTree.popUp();
	        });
		}
        return panel;
	}

}
