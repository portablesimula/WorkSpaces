package simula.psi.viewer.intellij;

//
//Source code recreated from a .class file by IntelliJ IDEA
//(powered by Fernflower decompiler)
//

//package com.intellij.ui.treeStructure;
//
//import com.intellij.ide.ActivityTracker;
//import com.intellij.ide.IdeBundle;
//import com.intellij.ide.dnd.SmoothAutoScroller;
//import com.intellij.ide.ui.UISettings;
//import com.intellij.ide.ui.UISettingsListener;
//import com.intellij.ide.util.treeView.CachedTreePresentation;
//import com.intellij.ide.util.treeView.CachedTreePresentationSupport;
//import com.intellij.ide.util.treeView.NodeRenderer;
//import com.intellij.ide.util.treeView.PresentableNodeDescriptor;
//import com.intellij.openapi.application.ApplicationManager;
//import com.intellij.openapi.client.ClientSystemInfo;
//import com.intellij.openapi.diagnostic.Logger;
//import com.intellij.openapi.options.advanced.AdvancedSettings;
//import com.intellij.openapi.ui.GraphicsConfig;
//import com.intellij.openapi.ui.Queryable;
//import com.intellij.openapi.util.Comparing;
//import com.intellij.openapi.util.Condition;
//import com.intellij.openapi.util.Conditions;
//import com.intellij.openapi.util.Disposer;
//import com.intellij.openapi.util.Key;
//import com.intellij.openapi.util.registry.Registry;
//import com.intellij.ui.ClientProperty;
//import com.intellij.ui.ComponentUtil;
//import com.intellij.ui.ComponentWithExpandableItems;
//import com.intellij.ui.ComponentWithFileColors;
//import com.intellij.ui.ExpandableItemsHandler;
//import com.intellij.ui.ExpandableItemsHandlerFactory;
//import com.intellij.ui.LoadingNode;
//import com.intellij.ui.SmartExpander;
//import com.intellij.ui.paint.RectanglePainter2D;
//import com.intellij.ui.speedSearch.SpeedSearchSupply;
//import com.intellij.ui.tree.TreePathBackgroundSupplier;
//import com.intellij.util.ArrayUtil;
//import com.intellij.util.LazyInitializer;
//import com.intellij.util.ThreeState;
//import com.intellij.util.messages.MessageBusConnection;
//import com.intellij.util.ui.AsyncProcessIcon;
//import com.intellij.util.ui.ComponentWithEmptyText;
//import com.intellij.util.ui.JBSwingUtilities;
//import com.intellij.util.ui.JBUI;
//import com.intellij.util.ui.MacUIUtil;
//import com.intellij.util.ui.StatusText;
//import com.intellij.util.ui.TimerUtil;
//import com.intellij.util.ui.UIUtil;
//import com.intellij.util.ui.tree.TreeUtil;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.im.InputMethodRequests;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.TransferHandler;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.plaf.TreeUI;
import javax.swing.text.Position;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//import org.jetbrains.annotations.ApiStatus.Internal;

public class Tree extends JTree implements ComponentWithEmptyText, ComponentWithExpandableItems<Integer>, Queryable, ComponentWithFileColors, TreePathBackgroundSupplier, CachedTreePresentationSupport {
 @Internal
 public static final Key<Boolean> AUTO_SELECT_ON_MOUSE_PRESSED = Key.create("allows to select a node automatically on right click");
 @Internal
 public static final Key<Boolean> AUTO_SCROLL_FROM_SOURCE_BLOCKED = Key.create("auto scroll from source temporarily blocked");
 private static final @NotNull Logger LOG = Logger.getInstance(Tree.class);
 private final StatusText myEmptyText;
 private final ExpandableItemsHandler<Integer> myExpandableItemsHandler;
 private AsyncProcessIcon myBusyIcon;
 private boolean myBusy;
 private Rectangle myLastVisibleRec;
 private Dimension myHoldSize;
 private int myAdditionalRowsCount;
 private final MySelectionModel mySelectionModel;
 private ThreeState myHorizontalAutoScrolling;
 private TreePath rollOverPath;
 private final Timer autoScrollUnblockTimer;
 private final @NotNull ExpandImpl expandImpl;
 private final @NotNull AtomicInteger suspendedExpandAccessibilityAnnouncements;
 private final @NotNull AtomicInteger bulkOperationsInProgress;
 private final @NotNull AtomicBoolean applyingViewModelChanges;
 private transient boolean settingUI;
 private transient TreeExpansionListener uiTreeExpansionListener;
 private final @NotNull AtomicInteger processingDoubleClick;
 private final MyUISettingsListener myUISettingsListener;
 private boolean initialized;

 @Internal
 public static boolean isBulkExpandCollapseSupported() {
     return true;
 }

 @Internal
 public static boolean isExpandWithSingleClickSettingEnabled() {
     return Registry.is("ide.tree.show.expand.with.single.click.setting", true);
 }

 private static boolean isCollapseRecursively() {
     return ApplicationManager.getApplication() != null ? AdvancedSettings.getBoolean("ide.tree.collapse.recursively") : true;
 }

 public Tree() {
     this((TreeNode)(new DefaultMutableTreeNode()));
 }

 public Tree(TreeNode root) {
     this((TreeModel)(new DefaultTreeModel(root, false)));
 }

 public Tree(TreeModel treemodel) {
     super(treemodel);
     this.myAdditionalRowsCount = -1;
     this.mySelectionModel = new MySelectionModel();
     this.myHorizontalAutoScrolling = ThreeState.UNSURE;
     this.autoScrollUnblockTimer = TimerUtil.createNamedTimer("TreeAutoscrollUnblock", 500, (e) -> this.unblockAutoScrollFromSource());
     this.suspendedExpandAccessibilityAnnouncements = new AtomicInteger();
     this.bulkOperationsInProgress = new AtomicInteger();
     this.applyingViewModelChanges = new AtomicBoolean();
     this.processingDoubleClick = new AtomicInteger();
     this.myUISettingsListener = new MyUISettingsListener();
     this.initialized = false;
     SmartExpander.setRecursiveCollapseEnabled(isCollapseRecursively());
     this.expandImpl = new ExpandImpl();
     this.myEmptyText = new StatusText(this) {
         protected boolean isStatusVisible() {
             return Tree.this.isEmptyTextVisible();
         }
     };
     this.myExpandableItemsHandler = ExpandableItemsHandlerFactory.install(this);
     this.initialized = true;
     if (UIUtil.isUnderWin10LookAndFeel()) {
         this.addMouseMotionListener(new MouseMotionAdapter() {
             public void mouseMoved(MouseEvent e) {
                 Point p = e.getPoint();
                 TreePath newPath = Tree.this.getPathForLocation(p.x, p.y);
                 if (newPath != null && !newPath.equals(Tree.this.rollOverPath)) {
                     TreeCellRenderer renderer = Tree.this.getCellRenderer();
                     Object var6 = newPath.getLastPathComponent();
                     if (var6 instanceof TreeNode) {
                         TreeNode node = (TreeNode)var6;
                         JComponent c = (JComponent)renderer.getTreeCellRendererComponent(Tree.this, node, Tree.this.isPathSelected(newPath), Tree.this.isExpanded(newPath), Tree.this.getModel().isLeaf(node), Tree.this.getRowForPath(newPath), Tree.this.hasFocus());
                         c.putClientProperty("JCheckBox.rollOver.rectangle", c instanceof JCheckBox ? Tree.this.getPathBounds(newPath) : node);
                         Tree.this.rollOverPath = newPath;
                         UIUtil.repaintViewport(Tree.this);
                     }
                 }

             }
         });
     }

     this.addMouseListener(new MyMouseListener());
     this.addFocusListener(new MyFocusListener());
     this.setCellRenderer(new NodeRenderer());
     this.setSelectionModel(this.mySelectionModel);
     this.setOpaque(false);
     this.putClientProperty(UIUtil.NOT_IN_HIERARCHY_COMPONENTS, this.myEmptyText.getWrappedFragmentsIterable());
 }

 public void setUI(TreeUI ui) {
     if (this.ui != ui) {
         this.settingUI = true;
         this.uiTreeExpansionListener = null;

         try {
             super.setUI(ui);
         } finally {
             this.settingUI = false;
         }
     }

 }

 public void setToggleClickCount(int clickCount) {
     super.setToggleClickCount(clickCount);
     this.myUISettingsListener.setToggleClickCountCalled();
 }

 public void addTreeExpansionListener(TreeExpansionListener listener) {
     if (this.settingUI) {
         this.uiTreeExpansionListener = listener;
     }

     super.addTreeExpansionListener(listener);
 }

 public void removeTreeExpansionListener(TreeExpansionListener listener) {
     super.removeTreeExpansionListener(listener);
     if (this.uiTreeExpansionListener == listener) {
         this.uiTreeExpansionListener = null;
     }

 }

 protected Graphics getComponentGraphics(Graphics graphics) {
     return JBSwingUtilities.runGlobalCGTransform(this, super.getComponentGraphics(graphics));
 }

 @Internal
 public void startMeasuringExpandDuration(@NotNull TreePath path) {
 }

 public boolean isEmpty() {
     return 0 >= this.getRowCount();
 }

 protected boolean isWideSelection() {
     return true;
 }

 protected @NotNull Condition<Integer> getWideSelectionBackgroundCondition() {
     return Conditions.alwaysTrue();
 }

 public boolean isFileColorsEnabled() {
     return false;
 }

 protected boolean isEmptyTextVisible() {
     return this.isEmpty();
 }

 public @NotNull StatusText getEmptyText() {
     return this.myEmptyText;
 }

 public @NotNull ExpandableItemsHandler<Integer> getExpandableItemsHandler() {
     return this.myExpandableItemsHandler;
 }

 public void setExpandableItemsEnabled(boolean enabled) {
     this.myExpandableItemsHandler.setEnabled(enabled);
 }

 public Color getBackground() {
     return this.isBackgroundSet() ? super.getBackground() : UIUtil.getTreeBackground();
 }

 public Color getForeground() {
     return this.isForegroundSet() ? super.getForeground() : UIUtil.getTreeForeground();
 }

 public void addNotify() {
     super.addNotify();
     this.firePropertyChange("font", (Object)null, (Object)null);
     this.updateBusy();
     this.myUISettingsListener.connect();
 }

 public void removeNotify() {
     super.removeNotify();
     if (this.myBusyIcon != null) {
         this.remove(this.myBusyIcon);
         this.myBusyIcon.dispose();
         this.myBusyIcon = null;
     }

     this.myUISettingsListener.disconnect();
 }

 public void doLayout() {
     super.doLayout();
     this.updateBusyIconLocation();
 }

 private void updateBusyIconLocation() {
     if (this.myBusyIcon != null) {
         this.myBusyIcon.updateLocation(this);
     }

 }

 public void paint(Graphics g) {
     Rectangle visible = this.getVisibleRect();

     try {
         super.paint(g);
         if (!visible.equals(this.myLastVisibleRec)) {
             this.updateBusyIconLocation();
         }

         this.myLastVisibleRec = visible;
     } finally {
         this.mySelectionModel.unholdSelection();
     }

 }

 public void setPaintBusy(boolean paintBusy) {
     if (this.myBusy != paintBusy) {
         this.myBusy = paintBusy;
         this.updateBusy();
     }
 }

 private void updateBusy() {
     boolean shouldPaintBusyIcon = this.myBusy && this.shouldShowBusyIconIfNeeded();
     if (shouldPaintBusyIcon) {
         if (this.myBusyIcon == null) {
             this.myBusyIcon = new AsyncProcessIcon(this.toString());
             this.myBusyIcon.setOpaque(false);
             this.myBusyIcon.setPaintPassiveIcon(false);
             this.myBusyIcon.setToolTipText(IdeBundle.message("tooltip.text.update.is.in.progress.click.to.cancel", new Object[0]));
             this.add(this.myBusyIcon);
         }

         this.myBusyIcon.resume();
         this.myBusyIcon.setVisible(true);
         this.updateBusyIconLocation();
     }

     if (!shouldPaintBusyIcon && this.myBusyIcon != null) {
         this.myBusyIcon.suspend();
         this.myBusyIcon.setVisible(false);
         SwingUtilities.invokeLater(() -> {
             if (this.myBusyIcon != null) {
                 this.repaint();
             }

         });
     }

 }

 protected boolean shouldShowBusyIconIfNeeded() {
     return this.hasFocus();
 }

 protected boolean paintNodes() {
     return false;
 }

 protected void paintComponent(Graphics g) {
     if (this.paintNodes()) {
         g.setColor(this.getBackground());
         g.fillRect(0, 0, this.getWidth(), this.getHeight());
     }

     if (this.isFileColorsEnabled()) {
         g.setColor(this.getBackground());
         g.fillRect(0, 0, this.getWidth(), this.getHeight());
         this.paintFileColorGutter(g);
     }

     super.paintComponent(g);
     this.myEmptyText.paint(this, g);
 }

 protected void paintFileColorGutter(Graphics g) {
     GraphicsConfig config = new GraphicsConfig(g);
     config.setupAAPainting();
     Rectangle rect = this.getVisibleRect();
     int firstVisibleRow = this.getClosestRowForLocation(rect.x, rect.y);
     int lastVisibleRow = this.getClosestRowForLocation(rect.x, rect.y + rect.height);
     Color prevColor = firstVisibleRow == 0 ? null : this.getFileColorForRow(firstVisibleRow - 1);
     Color curColor = this.getFileColorForRow(firstVisibleRow);

     for(int row = firstVisibleRow; row <= lastVisibleRow; ++row) {
         Color nextColor = row + 1 < this.getRowCount() ? this.getFileColorForRow(row + 1) : null;
         if (curColor != null) {
             Rectangle bounds = this.getRowBounds(row);
             double x = (double)JBUI.scale(4);
             double y = (double)bounds.y;
             double w = (double)JBUI.scale(4);
             double h = (double)bounds.height;
             if (Registry.is("ide.file.colors.at.left")) {
                 g.setColor(curColor);
                 if (curColor.equals(prevColor) && curColor.equals(nextColor)) {
                     RectanglePainter2D.FILL.paint((Graphics2D)g, x, y, w, h);
                 } else if (!curColor.equals(prevColor) && !curColor.equals(nextColor)) {
                     RectanglePainter2D.FILL.paint((Graphics2D)g, x, y + (double)2.0F, w, h - (double)4.0F, w);
                 } else if (curColor.equals(prevColor)) {
                     RectanglePainter2D.FILL.paint((Graphics2D)g, x, y - w, w, h + w - (double)2.0F, w);
                 } else {
                     RectanglePainter2D.FILL.paint((Graphics2D)g, x, y + (double)2.0F, w, h + w, w);
                 }
             } else {
                 g.setColor(curColor);
                 g.fillRect(0, bounds.y, this.getWidth(), bounds.height);
             }
         }

         prevColor = curColor;
         curColor = nextColor;
     }

     config.restore();
 }

 public @Nullable Color getPathBackground(@NotNull TreePath path, int row) {
     return this.isFileColorsEnabled() && !Registry.is("ide.file.colors.at.left") ? this.getFileColorForPath(path) : null;
 }

 public @Nullable Color getFileColorForRow(int row) {
     TreePath path = this.getPathForRow(row);
     return path != null ? this.getFileColorForPath(path) : null;
 }

 public @Nullable Color getFileColorForPath(@NotNull TreePath path) {
     Object component = path.getLastPathComponent();
     if (component instanceof LoadingNode) {
         Object[] pathObjects = path.getPath();
         if (pathObjects.length > 1) {
             component = pathObjects[pathObjects.length - 2];
         }
     }

     return this.getFileColorFor(TreeUtil.getUserObject(component));
 }

 public @Nullable Color getFileColorFor(Object object) {
     return null;
 }

 protected void processKeyEvent(KeyEvent e) {
     super.processKeyEvent(e);
 }

 public boolean getDragEnabled() {
     return super.getDragEnabled() && this.processingDoubleClick.get() == 0;
 }

 protected void processMouseEvent(MouseEvent e) {
     MouseEvent e2 = e;
     if (ClientSystemInfo.isMac()) {
         e2 = MacUIUtil.fixMacContextMenuIssue(e);
     }

     boolean isDoubleClick = e.getClickCount() >= 2;
     if (isDoubleClick) {
         this.processingDoubleClick.incrementAndGet();
     }

     try {
         super.processMouseEvent(e2);
     } finally {
         if (isDoubleClick) {
             this.processingDoubleClick.decrementAndGet();
         }

     }

     if (e != e2 && e2.isConsumed()) {
         e.consume();
     }

 }

 public TreePath getNextMatch(String prefix, int startingRow, Position.Bias bias) {
     return null;
 }

 public TreePath getPath(@NotNull PresentableNodeDescriptor node) {
     return null;
 }

 @Nullable CachingTreePath getPath(@Nullable TreeModelEvent event) {
     if (event == null) {
         return null;
     } else {
         TreePath path = event.getTreePath();
         TreeModel model = this.getModel();
         if (path == null && model != null) {
             Object root = model.getRoot();
             if (root != null) {
                 return new CachingTreePath(root);
             }
         }

         return CachingTreePath.ensureCaching(path);
     }
 }

 public void expandPaths(@NotNull Iterable<@NotNull TreePath> paths) {
     if (this.initialized) {
         this.expandImpl.expandPaths(paths);
     } else {
         for(TreePath path : paths) {
             super.expandPath(path);
         }

     }
 }

 public void collapsePath(TreePath path) {
     int row = isCollapseRecursively() ? this.getRowForPath(path) : -1;
     if (row < 0) {
         super.collapsePath(path);
     } else if (!this.isAlwaysExpanded(path)) {
         ArrayDeque<TreePath> deque = new ArrayDeque();
         deque.addFirst(path);

         while(true) {
             ++row;
             if (row >= this.getRowCount()) {
                 break;
             }

             TreePath next = this.getPathForRow(row);
             if (!path.isDescendant(next)) {
                 break;
             }

             if (this.isExpanded(next)) {
                 deque.addFirst(next);
             }
         }

         this.collapsePaths(deque);
     }

 }

 public void collapsePaths(@NotNull Iterable<@NotNull TreePath> paths) {
     if (this.initialized) {
         this.expandImpl.collapsePaths(paths);
     } else {
         for(TreePath path : paths) {
             super.collapsePath(path);
         }

     }
 }

 private boolean isAlwaysExpanded(TreePath path) {
     return path != null && TreeUtil.getNodeDepth(this, path) <= 0;
 }

 @Internal
 public void suspendExpandCollapseAccessibilityAnnouncements() {
     this.suspendedExpandAccessibilityAnnouncements.incrementAndGet();
 }

 @Internal
 public void resumeExpandCollapseAccessibilityAnnouncements() {
     this.suspendedExpandAccessibilityAnnouncements.decrementAndGet();
 }

 @Internal
 public void fireAccessibleTreeExpanded(@NotNull TreePath path) {
     if (this.accessibleContext != null) {
         ((JTree.AccessibleJTree)this.accessibleContext).treeExpanded(new TreeExpansionEvent(this, path));
     }

 }

 @Internal
 public void fireAccessibleTreeCollapsed(@NotNull TreePath path) {
     if (this.accessibleContext != null) {
         ((JTree.AccessibleJTree)this.accessibleContext).treeCollapsed(new TreeExpansionEvent(this, path));
     }

 }

 protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
     TreeModel model = this.treeModel;
     if ("model".equals(propertyName)) {
         if (oldValue instanceof CachedTreePresentationSupport) {
             CachedTreePresentationSupport cps = (CachedTreePresentationSupport)oldValue;
             cps.setCachedPresentation((CachedTreePresentation)null);
         }

         if (this.initialized && model != null) {
             Object treeRoot = model.getRoot();
             if (treeRoot != null && !model.isLeaf(treeRoot)) {
                 super.clearToggledPaths();
                 this.expandImpl.markPathExpanded(new CachingTreePath(treeRoot));
             }
         }

         if (this.initialized && newValue instanceof CachedTreePresentationSupport) {
             CachedTreePresentationSupport cps = (CachedTreePresentationSupport)newValue;
             cps.setCachedPresentation(this.expandImpl.getCachedPresentation());
         }
     }

     super.firePropertyChange(propertyName, oldValue, newValue);
 }

 public void fireTreeExpanded(@NotNull TreePath path) {
     Object[] listeners = this.listenerList.getListenerList();
     TreeExpansionEvent e = new TreeBulkExpansionEvent(this, path, this.isBulkOperationInProgress());
     if (this.uiTreeExpansionListener != null) {
         this.uiTreeExpansionListener.treeExpanded(e);
     }

     for(int i = listeners.length - 2; i >= 0; i -= 2) {
         if (listeners[i] == TreeExpansionListener.class && listeners[i + 1] != this.uiTreeExpansionListener && (listeners[i + 1] != this.accessibleContext || this.expandAccessibilityAnnouncementsAllowed())) {
             ((TreeExpansionListener)listeners[i + 1]).treeExpanded(e);
         }
     }

 }

 public void fireTreeCollapsed(@NotNull TreePath path) {
     Object[] listeners = this.listenerList.getListenerList();
     TreeExpansionEvent e = new TreeBulkExpansionEvent(this, path, this.isBulkOperationInProgress());
     if (this.uiTreeExpansionListener != null) {
         this.uiTreeExpansionListener.treeCollapsed(e);
     }

     for(int i = listeners.length - 2; i >= 0; i -= 2) {
         if (listeners[i] == TreeExpansionListener.class && listeners[i + 1] != this.uiTreeExpansionListener && (listeners[i + 1] != this.accessibleContext || this.expandAccessibilityAnnouncementsAllowed())) {
             ((TreeExpansionListener)listeners[i + 1]).treeCollapsed(e);
         }
     }

 }

 private boolean isBulkOperationInProgress() {
     return this.initialized && this.bulkOperationsInProgress.get() > 0;
 }

 private void fireBulkExpandStarted() {
     Object[] listeners = this.listenerList.getListenerList();
     TreeBulkExpansionEvent e = null;

     for(int i = listeners.length - 2; i >= 0; i -= 2) {
         if (listeners[i] == TreeExpansionListener.class) {
             Object var5 = listeners[i + 1];
             if (var5 instanceof TreeBulkExpansionListener) {
                 TreeBulkExpansionListener bulkExpansionListener = (TreeBulkExpansionListener)var5;
                 if (e == null) {
                     e = new TreeBulkExpansionEvent(this, (TreePath)null, false);
                 }

                 bulkExpansionListener.treeBulkExpansionStarted(e);
             }
         }
     }

 }

 private void fireBulkExpandEnded() {
     Object[] listeners = this.listenerList.getListenerList();
     TreeBulkExpansionEvent e = null;

     for(int i = listeners.length - 2; i >= 0; i -= 2) {
         if (listeners[i] == TreeExpansionListener.class) {
             Object var5 = listeners[i + 1];
             if (var5 instanceof TreeBulkExpansionListener) {
                 TreeBulkExpansionListener bulkExpansionListener = (TreeBulkExpansionListener)var5;
                 if (e == null) {
                     e = new TreeBulkExpansionEvent(this, (TreePath)null, false);
                 }

                 bulkExpansionListener.treeBulkExpansionEnded(e);
             }
         }
     }

 }

 private void fireBulkCollapseStarted() {
     Object[] listeners = this.listenerList.getListenerList();
     TreeBulkExpansionEvent e = null;

     for(int i = listeners.length - 2; i >= 0; i -= 2) {
         if (listeners[i] == TreeExpansionListener.class) {
             Object var5 = listeners[i + 1];
             if (var5 instanceof TreeBulkExpansionListener) {
                 TreeBulkExpansionListener bulkExpansionListener = (TreeBulkExpansionListener)var5;
                 if (e == null) {
                     e = new TreeBulkExpansionEvent(this, (TreePath)null, false);
                 }

                 bulkExpansionListener.treeBulkCollapseStarted(e);
             }
         }
     }

 }

 private void fireBulkCollapseEnded() {
     Object[] listeners = this.listenerList.getListenerList();
     TreeBulkExpansionEvent e = null;

     for(int i = listeners.length - 2; i >= 0; i -= 2) {
         if (listeners[i] == TreeExpansionListener.class) {
             Object var5 = listeners[i + 1];
             if (var5 instanceof TreeBulkExpansionListener) {
                 TreeBulkExpansionListener bulkExpansionListener = (TreeBulkExpansionListener)var5;
                 if (e == null) {
                     e = new TreeBulkExpansionEvent(this, (TreePath)null, false);
                 }

                 bulkExpansionListener.treeBulkCollapseEnded(e);
             }
         }
     }

 }

 @Internal
 public void fireTreeStateRestoreStarted() {
     Object[] listeners = this.listenerList.getListenerList();
     TreeExpansionEvent e = null;

     for(int i = listeners.length - 2; i >= 0; i -= 2) {
         if (listeners[i] == TreeExpansionListener.class) {
             Object var5 = listeners[i + 1];
             if (var5 instanceof TreeStateListener) {
                 TreeStateListener stateListener = (TreeStateListener)var5;
                 if (e == null) {
                     e = new TreeExpansionEvent(this, (TreePath)null);
                 }

                 stateListener.treeStateRestoreStarted(e);
             }
         }
     }

 }

 @Internal
 public void fireTreeStateCachedStateRestored() {
     Object[] listeners = this.listenerList.getListenerList();
     TreeExpansionEvent e = null;

     for(int i = listeners.length - 2; i >= 0; i -= 2) {
         if (listeners[i] == TreeExpansionListener.class) {
             Object var5 = listeners[i + 1];
             if (var5 instanceof TreeStateListener) {
                 TreeStateListener stateListener = (TreeStateListener)var5;
                 if (e == null) {
                     e = new TreeExpansionEvent(this, (TreePath)null);
                 }

                 stateListener.treeStateCachedStateRestored(e);
             }
         }
     }

 }

 @Internal
 public void fireTreeStateRestoreFinished() {
     Object[] listeners = this.listenerList.getListenerList();
     TreeExpansionEvent e = null;

     for(int i = listeners.length - 2; i >= 0; i -= 2) {
         if (listeners[i] == TreeExpansionListener.class) {
             Object var5 = listeners[i + 1];
             if (var5 instanceof TreeStateListener) {
                 TreeStateListener stateListener = (TreeStateListener)var5;
                 if (e == null) {
                     e = new TreeExpansionEvent(this, (TreePath)null);
                 }

                 stateListener.treeStateRestoreFinished(e);
             }
         }
     }

 }

 private boolean expandAccessibilityAnnouncementsAllowed() {
     return this.suspendedExpandAccessibilityAnnouncements.get() == 0;
 }

 public @NotNull Set<TreePath> getExpandedPaths() {
     if (this.initialized) {
         return this.expandImpl.getExpandedPaths();
     } else {
         TreePath rootPath = this.getRootPath();
         if (rootPath != null && super.isExpanded(rootPath)) {
             HashSet<TreePath> result = new HashSet();
             result.add(rootPath);
             Enumeration<TreePath> more = super.getExpandedDescendants(rootPath);
             if (more != null) {
                 while(more.hasMoreElements()) {
                     result.add((TreePath)more.nextElement());
                 }
             }

             return result;
         } else {
             return Collections.emptySet();
         }
     }
 }

 private @Nullable TreePath getRootPath() {
     TreeModel model = this.treeModel;
     if (model == null) {
         return null;
     } else {
         Object rootObject = model.getRoot();
         return rootObject == null ? null : new CachingTreePath(rootObject);
     }
 }

 public Enumeration<TreePath> getExpandedDescendants(TreePath parent) {
     return !this.initialized ? super.getExpandedDescendants(parent) : this.expandImpl.getExpandedDescendants(parent);
 }

 public boolean hasBeenExpanded(TreePath path) {
     return !this.initialized ? super.hasBeenExpanded(path) : this.expandImpl.hasBeenExpanded(path);
 }

 public boolean isExpanded(TreePath path) {
     return !this.initialized ? super.isExpanded(path) : this.expandImpl.isExpanded(path);
 }

 public boolean isExpanded(int row) {
     return !this.initialized ? super.isExpanded(row) : this.expandImpl.isExpanded(row);
 }

 protected void setExpandedState(TreePath path, boolean state) {
     if (!this.initialized) {
         super.setExpandedState(path, state);
     } else {
         this.expandImpl.setExpandedState(path, state);
     }
 }

 protected Enumeration<TreePath> getDescendantToggledPaths(TreePath parent) {
     return !this.initialized ? super.getDescendantToggledPaths(parent) : this.expandImpl.getDescendantToggledPaths(parent);
 }

 protected void removeDescendantToggledPaths(Enumeration<TreePath> toRemove) {
     if (!this.initialized) {
         super.removeDescendantToggledPaths(toRemove);
     } else {
         this.expandImpl.removeDescendantToggledPaths(toRemove);
     }
 }

 protected void clearToggledPaths() {
     if (this.initialized) {
         this.expandImpl.clearToggledPaths();
     }

     super.clearToggledPaths();
 }

 protected TreeModelListener createTreeModelListener() {
     return new MyTreeModelListener();
 }

 private void blockAutoScrollFromSource() {
     ClientProperty.put(this, AUTO_SCROLL_FROM_SOURCE_BLOCKED, true);
     this.autoScrollUnblockTimer.restart();
 }

 @Internal
 public void unblockAutoScrollFromSource() {
     ClientProperty.remove(this, AUTO_SCROLL_FROM_SOURCE_BLOCKED);
 }

 /** @deprecated */
 @Deprecated
 public final void setLineStyleAngled() {
 }

 public <T> T @NotNull [] getSelectedNodes(Class<T> nodeType, @Nullable NodeFilter<? super T> filter) {
     TreePath[] paths = this.getSelectionPaths();
     if (paths == null) {
         Object[] var10000 = ArrayUtil.newArray(nodeType, 0);
         if (var10000 == null) {
             $$$reportNull$$$0(16);
         }

         return (T[])var10000;
     } else {
         ArrayList<T> nodes = new ArrayList();

         for(TreePath path : paths) {
             Object last = path.getLastPathComponent();
             if (nodeType.isAssignableFrom(last.getClass()) && (filter == null || filter.accept(last))) {
                 nodes.add(last);
             }
         }

         T[] result = (T[])ArrayUtil.newArray(nodeType, nodes.size());
         nodes.toArray(result);
         if (result == null) {
             $$$reportNull$$$0(17);
         }

         return result;
     }
 }

 public void putInfo(@NotNull Map<? super String, ? super String> info) {
     TreePath[] selection = this.getSelectionPaths();
     if (selection != null) {
         StringBuilder nodesText = new StringBuilder();

         for(TreePath eachPath : selection) {
             Object eachNode = eachPath.getLastPathComponent();
             Component c = this.getCellRenderer().getTreeCellRendererComponent(this, eachNode, false, false, false, this.getRowForPath(eachPath), false);
             if (c != null) {
                 if (!nodesText.isEmpty()) {
                     nodesText.append(";");
                 }

                 nodesText.append(c);
             }
         }

         if (!nodesText.isEmpty()) {
             info.put("selectedNodes", nodesText.toString());
         }

     }
 }

 public void setHoldSize(boolean hold) {
     if (hold && this.myHoldSize == null) {
         this.myHoldSize = this.getPreferredSize();
     } else if (!hold && this.myHoldSize != null) {
         this.myHoldSize = null;
         this.revalidate();
     }

 }

 public void setAdditionalRowsCount(int additionalRowsCount) {
     int oldValue = this.myAdditionalRowsCount;
     this.myAdditionalRowsCount = additionalRowsCount;
     this.firePropertyChange("additionalRowsCount", oldValue, additionalRowsCount);
 }

 public int getAdditionalRowsCount() {
     return this.myAdditionalRowsCount;
 }

 public int getEffectiveAdditionalRowsCount() {
     int result = this.myAdditionalRowsCount;
     if (result == -1) {
         result = Registry.intValue("ide.tree.additional.rows.count", 1, 0, 10);
     }

     return result;
 }

 public Dimension getPreferredSize() {
     Dimension size = super.getPreferredSize();
     size.height += this.getAdditionalRowsHeight();
     if (this.myHoldSize != null) {
         size.width = Math.max(size.width, this.myHoldSize.width);
         size.height = Math.max(size.height, this.myHoldSize.height);
     }

     return size;
 }

 private int getAdditionalRowsHeight() {
     int additionalRowsCount = this.getEffectiveAdditionalRowsCount();
     if (additionalRowsCount == 0) {
         return 0;
     } else {
         int rowHeight = this.getDefaultRowHeight();
         if (rowHeight == 0) {
             return 0;
         } else {
             int extraHeight = rowHeight * additionalRowsCount;
             JViewport viewport = ComponentUtil.getViewport(this);
             int viewportHeight = viewport == null ? 0 : viewport.getHeight();
             int maximumSensibleExtraHeight = viewportHeight - rowHeight;
             if (maximumSensibleExtraHeight < 0) {
                 maximumSensibleExtraHeight = 0;
             }

             return Math.min(extraHeight, maximumSensibleExtraHeight);
         }
     }
 }

 private int getDefaultRowHeight() {
     int result = this.getRowHeight();
     if (result <= 0) {
         result = com.intellij.util.ui.JBUI.CurrentTheme.Tree.rowHeight();
     }

     if (result <= 0) {
         result = 0;
     }

     return result;
 }

 public void scrollPathToVisible(@Nullable TreePath path) {
     if (path != null) {
         this.makeVisible(path);
         TreeUtil.scrollToVisible(this, path, false);
     }
 }

 public boolean isHorizontalAutoScrollingEnabled() {
     return this.myHorizontalAutoScrolling != ThreeState.UNSURE ? this.myHorizontalAutoScrolling == ThreeState.YES : Registry.is("ide.tree.horizontal.default.autoscrolling", false);
 }

 public void setHorizontalAutoScrollingEnabled(boolean enabled) {
     this.myHorizontalAutoScrolling = enabled ? ThreeState.YES : ThreeState.NO;
 }

 public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
     int increment = super.getScrollableUnitIncrement(visibleRect, orientation, direction);
     return increment == 0 && orientation == 1 && direction < 0 ? visibleRect.y : increment;
 }

 public @Nullable Component getDeepestRendererComponentAt(int x, int y) {
     int row = this.getRowForLocation(x, y);
     if (row >= 0) {
         TreeCellRenderer renderer = this.getCellRenderer();
         if (renderer != null) {
             TreePath path = this.getPathForRow(row);
             Object node = path.getLastPathComponent();
             Component component = renderer.getTreeCellRendererComponent(this, node, this.isRowSelected(row), this.isExpanded(row), this.getModel().isLeaf(node), row, true);
             Rectangle bounds = this.getPathBounds(path);
             if (bounds != null) {
                 component.setBounds(bounds);
                 component.doLayout();
                 return SwingUtilities.getDeepestComponentAt(component, x - bounds.x, y - bounds.y);
             }
         }
     }

     return null;
 }

 public void setTransferHandler(TransferHandler handler) {
     SmoothAutoScroller.installDropTargetAsNecessary(this);
     super.setTransferHandler(handler);
 }

 public InputMethodRequests getInputMethodRequests() {
     SpeedSearchSupply supply = SpeedSearchSupply.getSupply(this, true);
     return supply == null ? null : supply.getInputMethodRequests();
 }

 @Internal
 public void setCachedPresentation(@Nullable CachedTreePresentation presentation) {
     this.expandImpl.setCachedPresentation(presentation);
 }

 private class MySelectionModel extends DefaultTreeSelectionModel {
     private TreePath[] myHeldSelection;

     protected void fireValueChanged(TreeSelectionEvent e) {
         if (this.myHeldSelection == null) {
             ActivityTracker.getInstance().inc();
             super.fireValueChanged(e);
             if (!Tree.this.applyingViewModelChanges.get()) {
                 TreeModel var3 = Tree.this.treeModel;
                 if (var3 instanceof TreeSwingModel) {
                     TreeSwingModel swingModel = (TreeSwingModel)var3;
                     ArrayList<TreeNodeViewModel> newSelection = new ArrayList();

                     for(TreePath path : this.getSelectionPaths()) {
                         Object var9 = path.getLastPathComponent();
                         if (var9 instanceof TreeNodeViewModel) {
                             TreeNodeViewModel viewModel = (TreeNodeViewModel)var9;
                             newSelection.add(viewModel);
                         }
                     }

                     swingModel.getViewModel().setSelection(newSelection);
                 }
             }
         }

     }

     public void holdSelection() {
         this.myHeldSelection = this.getSelectionPaths();
     }

     public void unholdSelection() {
         if (this.myHeldSelection != null) {
             this.setSelectionPaths(this.myHeldSelection);
             this.myHeldSelection = null;
         }

     }
 }

 private class MyMouseListener extends MouseAdapter {
     private @Nullable TreePath treePathUnderMouse = null;

     private MyMouseListener() {
         Tree.this.autoScrollUnblockTimer.setRepeats(false);
     }

     public void mousePressed(MouseEvent event) {
         this.treePathUnderMouse = Tree.this.getPathForLocation(event.getX(), event.getY());
         if (!Tree.this.hasFocus()) {
             Tree.this.blockAutoScrollFromSource();
         }

         this.setPressed(event, true);
         if (!Boolean.FALSE.equals(UIUtil.getClientProperty(event.getSource(), Tree.AUTO_SELECT_ON_MOUSE_PRESSED)) || Tree.this.getSelectionModel().getSelectionCount() <= 1) {
             if (!SwingUtilities.isLeftMouseButton(event) && (SwingUtilities.isRightMouseButton(event) || SwingUtilities.isMiddleMouseButton(event))) {
                 TreePath path = Tree.this.getClosestPathForLocation(event.getX(), event.getY());
                 if (path == null) {
                     return;
                 }

                 Rectangle bounds = Tree.this.getPathBounds(path);
                 if (bounds != null && bounds.y + bounds.height < event.getY()) {
                     return;
                 }

                 if (Tree.this.getSelectionModel().getSelectionMode() != 1) {
                     TreePath[] selectionPaths = Tree.this.getSelectionModel().getSelectionPaths();
                     if (selectionPaths != null) {
                         for(TreePath selectionPath : selectionPaths) {
                             if (selectionPath != null && selectionPath.equals(path)) {
                                 return;
                             }
                         }
                     }
                 }

                 Tree.this.getSelectionModel().setSelectionPath(path);
             }

         }
     }

     public void mouseReleased(MouseEvent event) {
         TreePath treePathUnderMouseAfterEvent = Tree.this.getPathForLocation(event.getX(), event.getY());
         if (!Comparing.equal(this.treePathUnderMouse, treePathUnderMouseAfterEvent)) {
             event.consume();
         }

         this.treePathUnderMouse = null;
         this.setPressed(event, false);
         if (event.getButton() == 1 && event.getClickCount() == 2 && TreeUtil.isLocationInExpandControl(Tree.this, event.getX(), event.getY())) {
             event.consume();
         }

     }

     public void mouseExited(MouseEvent e) {
         if (UIUtil.isUnderWin10LookAndFeel() && Tree.this.rollOverPath != null) {
             TreeCellRenderer renderer = Tree.this.getCellRenderer();
             Object var4 = Tree.this.rollOverPath.getLastPathComponent();
             if (var4 instanceof TreeNode) {
                 TreeNode node = (TreeNode)var4;
                 JComponent c = (JComponent)renderer.getTreeCellRendererComponent(Tree.this, node, Tree.this.isPathSelected(Tree.this.rollOverPath), Tree.this.isExpanded(Tree.this.rollOverPath), Tree.this.getModel().isLeaf(node), Tree.this.getRowForPath(Tree.this.rollOverPath), Tree.this.hasFocus());
                 c.putClientProperty("JCheckBox.rollOver.rectangle", (Object)null);
                 Tree.this.rollOverPath = null;
                 UIUtil.repaintViewport(Tree.this);
             }
         }

     }

     private void setPressed(MouseEvent e, boolean pressed) {
         if (UIUtil.isUnderWin10LookAndFeel()) {
             Point p = e.getPoint();
             TreePath path = Tree.this.getPathForLocation(p.x, p.y);
             if (path != null) {
                 Object var6 = path.getLastPathComponent();
                 if (var6 instanceof TreeNode) {
                     TreeNode node = (TreeNode)var6;
                     JComponent c = (JComponent)Tree.this.getCellRenderer().getTreeCellRendererComponent(Tree.this, node, Tree.this.isPathSelected(path), Tree.this.isExpanded(path), Tree.this.getModel().isLeaf(node), Tree.this.getRowForPath(path), Tree.this.hasFocus());
                     if (pressed) {
                         c.putClientProperty("JCheckBox.pressed.rectangle", c instanceof JCheckBox ? Tree.this.getPathBounds(path) : node);
                     } else {
                         c.putClientProperty("JCheckBox.pressed.rectangle", (Object)null);
                     }

                     UIUtil.repaintViewport(Tree.this);
                 }
             }
         }

     }
 }

 private class MyFocusListener extends FocusAdapter {
     private void focusChanges() {
         TreePath[] paths = Tree.this.getSelectionPaths();
         if (paths != null) {
             TreeUI ui = Tree.this.getUI();

             for(int i = paths.length - 1; i >= 0; --i) {
                 Rectangle bounds = ui.getPathBounds(Tree.this, paths[i]);
                 if (bounds != null) {
                     Tree.this.repaint(bounds);
                 }
             }
         }

     }

     public void focusGained(FocusEvent e) {
         this.focusChanges();
     }

     public void focusLost(FocusEvent e) {
         this.focusChanges();
     }
 }

 private class CachedPresentationImpl {
     private final @NotNull CachedTreePresentation cachedTree;

     CachedPresentationImpl(CachedTreePresentation cachedTree) {
         this.cachedTree = cachedTree;
     }

     void setExpanded(@NotNull TreePath path, boolean isExpanded) {
         this.cachedTree.setExpanded(path, isExpanded);
     }

     void updateExpandedNodes(@NotNull TreePath parent) {
         Tree.this.expandPaths(this.collectCachedExpandedPaths(parent));
     }

     private @NotNull Iterable<TreePath> collectCachedExpandedPaths(@NotNull TreePath parent) {
         TreeModel model = Tree.this.getModel();
         return (Iterable<TreePath>)(model == null ? Collections.emptyList() : this.cachedTree.getExpandedDescendants(model, parent));
     }
 }

 private class ExpandImpl implements CachedTreePresentationSupport {
     private final Map<TreePath, Boolean> expandedState = new HashMap();
     private CachedPresentationImpl cachedPresentation;

     private ExpandImpl() {
         TreePath rootPath = Tree.this.getRootPath();
         if (rootPath != null) {
             Enumeration<TreePath> toggled = Tree.super.getDescendantToggledPaths(rootPath);
             if (toggled != null) {
                 while(toggled.hasMoreElements()) {
                     TreePath toggledPath = (TreePath)toggled.nextElement();
                     this.expandedState.put(toggledPath, Tree.super.isExpanded(toggledPath));
                 }
             }
         }

         Tree.super.clearToggledPaths();
     }

     @Nullable CachedTreePresentation getCachedPresentation() {
         return this.cachedPresentation != null ? this.cachedPresentation.cachedTree : null;
     }

     public void setCachedPresentation(@Nullable CachedTreePresentation presentation) {
         if (this.cachedPresentation == null || presentation == null) {
             this.cachedPresentation = presentation == null ? null : Tree.this.new CachedPresentationImpl(presentation);
             if (this.cachedPresentation != null) {
                 TreePath rootPath = Tree.this.getRootPath();
                 if (rootPath != null) {
                     this.cachedPresentation.updateExpandedNodes(rootPath);
                 }
             }

             TreeModel var3 = Tree.this.getModel();
             if (var3 instanceof CachedTreePresentationSupport) {
                 CachedTreePresentationSupport cps = (CachedTreePresentationSupport)var3;
                 cps.setCachedPresentation(presentation);
             }

         }
     }

     void markPathExpanded(@NotNull TreePath path) {
         this.markPathExpandedState(path, true);
     }

     void markPathCollapsed(TreePath path) {
         this.markPathExpandedState(path, false);
     }

     private void markPathExpandedState(@NotNull TreePath path, boolean expanded) {
         if (Tree.LOG.isTraceEnabled()) {
             Tree.LOG.trace(new Throwable((expanded ? "Expanding" : "Collapsing") + " " + String.valueOf(path)));
         } else if (Tree.LOG.isDebugEnabled()) {
             Tree.LOG.debug((expanded ? "Expanding" : "Collapsing") + " " + String.valueOf(path));
         }

         TreeModel model = Tree.this.getModel();
         if (!expanded && model != null && model.isLeaf(path.getLastPathComponent())) {
             this.expandedState.remove(path);
         } else {
             this.expandedState.put(path, expanded);
         }

         if (this.cachedPresentation != null) {
             this.cachedPresentation.setExpanded(path, expanded);
         }

         Object var5 = path.getLastPathComponent();
         if (var5 instanceof TreeNodeViewModel viewModel) {
             if (Tree.this.applyingViewModelChanges.get()) {
                 Tree.LOG.debug("Not forwarding the new state to the view model because it came from the model itself");
             } else {
                 Tree.LOG.debug("Forwarding the new state to the view model");
                 viewModel.setExpanded(expanded);
             }
         }

     }

     @NotNull Set<TreePath> getExpandedPaths() {
         HashSet<TreePath> result = new HashSet();
         TreePath rootPath = Tree.this.getRootPath();
         if (!Tree.this.isRootVisible() || this.isExpanded(rootPath)) {
             result.add(rootPath);
         }

         for(Map.Entry<TreePath, Boolean> e : this.expandedState.entrySet()) {
             if ((Boolean)e.getValue()) {
                 result.add((TreePath)e.getKey());
             }
         }

         return result;
     }

     @Nullable Enumeration<TreePath> getExpandedDescendants(@Nullable TreePath parent) {
         if (parent != null && this.isExpanded(parent)) {
             Set<TreePath> toggledPaths = this.expandedState.keySet();
             List<TreePath> elements = null;

             for(TreePath path : toggledPaths) {
                 Boolean value = (Boolean)this.expandedState.get(path);
                 if (!path.equals(parent) && value != null && value && parent.isDescendant(path) && Tree.this.isVisible(path)) {
                     if (elements == null) {
                         elements = new ArrayList();
                     }

                     elements.add(path);
                 }
             }

             return elements == null ? Collections.emptyEnumeration() : Collections.enumeration(elements);
         } else {
             return null;
         }
     }

     boolean hasBeenExpanded(@Nullable TreePath path) {
         return path != null && this.expandedState.get(path) != null;
     }

     boolean isExpanded(@Nullable TreePath path) {
         if (path == null) {
             return false;
         } else {
             do {
                 Boolean value = (Boolean)this.expandedState.get(path);
                 if (value == null || !value) {
                     return false;
                 }
             } while((path = path.getParentPath()) != null);

             return true;
         }
     }

     boolean isExpanded(int row) {
         TreeUI tree = Tree.this.getUI();
         if (tree != null) {
             TreePath path = tree.getPathForRow(Tree.this, row);
             if (path != null) {
                 Boolean value = (Boolean)this.expandedState.get(path);
                 return value != null && value;
             }
         }

         return false;
     }

     void expandPaths(@NotNull Iterable<@NotNull TreePath> paths) {
         long started = 0L;
         long count = 0L;
         if (Tree.LOG.isDebugEnabled()) {
             started = System.currentTimeMillis();
         }

         ArrayList<TreePath> pathList = toList(paths);
         if (pathList.size() == 1) {
             TreePath path = (TreePath)pathList.get(0);
             if (this.isNotLeaf(path)) {
                 this.setExpandedState(path, true);
             }

         } else {
             pathList.sort(Comparator.comparing(TreePath::getPathCount));
             Set<TreePath> toExpand = new LinkedHashSet();
             Set<TreePath> toNotExpand = new HashSet();

             for(TreePath path : pathList) {
                 ++count;
                 this.shouldAllParentsBeExpanded(path, toExpand, toNotExpand);
             }

             Set<TreePath> expandRoots = new LinkedHashSet();

             try {
                 this.beginBulkOperation();
                 Tree.this.fireBulkExpandStarted();
                 Tree.this.suspendExpandCollapseAccessibilityAnnouncements();

                 for(TreePath path : toExpand) {
                     if (this.isNotLeaf(path)) {
                         this.markPathExpanded(path);
                         Tree.this.fireTreeExpanded(path);
                         TreePath parent = path.getParentPath();
                         if (expandRoots.size() < 5 && (parent == null || !toExpand.contains(parent))) {
                             expandRoots.add(path);
                         }
                     }
                 }
             } finally {
                 Tree.this.resumeExpandCollapseAccessibilityAnnouncements();
                 Tree.this.fireBulkExpandEnded();
                 this.endBulkOperation();
             }

             if (Tree.this.accessibleContext != null) {
                 for(TreePath expandRoot : expandRoots) {
                     Tree.this.fireAccessibleTreeExpanded(expandRoot);
                 }

                 ((JTree.AccessibleJTree)Tree.this.accessibleContext).fireVisibleDataPropertyChange();
             }

             if (Tree.LOG.isDebugEnabled()) {
                 Tree.LOG.debug("Expanded " + count + " paths, time: " + (System.currentTimeMillis() - started) + " ms");
             }

         }
     }

     private boolean isNotLeaf(@NotNull TreePath path) {
         TreeModel model = Tree.this.getModel();
         return model != null && !model.isLeaf(path.getLastPathComponent());
     }

     private void beginBulkOperation() {
         Tree.this.bulkOperationsInProgress.incrementAndGet();
         TreeUI ui = Tree.this.getUI();
         if (ui instanceof TreeUiBulkExpandCollapseSupport bulk) {
             bulk.beginBulkOperation();
         }

     }

     private void endBulkOperation() {
         TreeUI ui = Tree.this.getUI();
         if (ui instanceof TreeUiBulkExpandCollapseSupport bulk) {
             bulk.endBulkOperation();
         }

         Tree.this.bulkOperationsInProgress.decrementAndGet();
     }

     void collapsePaths(@NotNull Iterable<@NotNull TreePath> paths) {
         long started = 0L;
         long count = 0L;
         if (Tree.LOG.isDebugEnabled()) {
             started = System.currentTimeMillis();
         }

         ArrayList<TreePath> pathList = toList(paths);
         if (pathList.size() == 1) {
             TreePath path = (TreePath)pathList.get(0);
             if (this.isNotLeaf(path)) {
                 this.setExpandedState(path, false);
             } else {
                 this.expandedState.remove(path);
             }

         } else {
             pathList.sort(Comparator.comparing(TreePath::getPathCount));
             Set<TreePath> toExpand = new LinkedHashSet();
             Set<TreePath> toNotExpand = new HashSet();
             Set<TreePath> toCollapse = new LinkedHashSet();
             Set<TreePath> collapseRoots = new LinkedHashSet();

             for(TreePath path : pathList) {
                 ++count;
                 TreePath parent = path.getParentPath();
                 boolean parentWillBeCollapsed = toCollapse.contains(parent);
                 boolean pathWillBeCollapsed = false;
                 if (parent != null && !toExpand.contains(parent) && !parentWillBeCollapsed) {
                     if (!toNotExpand.contains(parent) && this.shouldAllParentsBeExpanded(parent, toExpand, toNotExpand)) {
                         toCollapse.add(path);
                         pathWillBeCollapsed = true;
                     }
                 } else {
                     toCollapse.add(path);
                     pathWillBeCollapsed = true;
                 }

                 if (!parentWillBeCollapsed && pathWillBeCollapsed) {
                     collapseRoots.add(path);
                 }
             }

             List<TreePath> toCollapseList = new ArrayList(toCollapse);
             Collections.reverse(toCollapseList);

             try {
                 this.beginBulkOperation();
                 Tree.this.fireBulkCollapseStarted();
                 Tree.this.suspendExpandCollapseAccessibilityAnnouncements();

                 for(TreePath path : toExpand) {
                     this.markPathExpanded(path);
                     Tree.this.fireTreeExpanded(path);
                 }

                 for(TreePath path : toCollapseList) {
                     if (this.isNotLeaf(path)) {
                         this.markPathCollapsed(path);
                         Tree.this.fireTreeCollapsed(path);
                         if (Tree.this.removeDescendantSelectedPaths(path, false) && !Tree.this.isPathSelected(path)) {
                             Tree.this.addSelectionPath(path);
                         }
                     } else {
                         this.expandedState.remove(path);
                     }
                 }
             } finally {
                 Tree.this.resumeExpandCollapseAccessibilityAnnouncements();
                 Tree.this.fireBulkCollapseEnded();
                 this.endBulkOperation();
             }

             if (Tree.this.accessibleContext != null) {
                 for(TreePath collapseRoot : collapseRoots) {
                     Tree.this.fireAccessibleTreeCollapsed(collapseRoot);
                 }

                 ((JTree.AccessibleJTree)Tree.this.accessibleContext).fireVisibleDataPropertyChange();
             }

             if (Tree.LOG.isDebugEnabled()) {
                 Tree.LOG.debug("Collapsed " + count + " paths, time: " + (System.currentTimeMillis() - started) + " ms");
             }

         }
     }

     private static @NotNull ArrayList<TreePath> toList(@NotNull Iterable<@NotNull TreePath> paths) {
         ArrayList<TreePath> pathList;
         if (paths instanceof Collection<TreePath> pathCollection) {
             pathList = new ArrayList(pathCollection);
         } else {
             pathList = new ArrayList();
             Objects.requireNonNull(pathList);
             paths.forEach(pathList::add);
         }

         return pathList;
     }

     private boolean shouldAllParentsBeExpanded(@NotNull TreePath path, @NotNull Set<@NotNull TreePath> toExpand, @NotNull Set<@NotNull TreePath> toNotExpand) {
         if (toNotExpand == null) {
             $$$reportNull$$$0(10);
         }

         Deque<TreePath> stack = null;
         TreePath parentPath = path;
         boolean result = true;

         while(parentPath != null) {
             if (!this.isExpanded(parentPath) && !toExpand.contains(parentPath)) {
                 if (toNotExpand.contains(parentPath)) {
                     parentPath = null;
                     result = false;
                 } else {
                     if (stack == null) {
                         stack = new ArrayDeque();
                     }

                     stack.push(parentPath);
                     parentPath = parentPath.getParentPath();
                 }
             } else {
                 parentPath = null;
             }
         }

         while(stack != null && !stack.isEmpty()) {
             parentPath = (TreePath)stack.pop();
             if (result) {
                 try {
                     Tree.this.fireTreeWillExpand(parentPath);
                 } catch (ExpandVetoException var8) {
                     result = false;
                 }
             }

             if (result) {
                 toExpand.add(parentPath);
             } else {
                 toNotExpand.add(parentPath);
             }
         }

         return result;
     }

     void setExpandedState(@Nullable TreePath path, boolean state) {
         if (path != null) {
             if (this.expandParentPaths(path)) {
                 if (state) {
                     this.expandPath(path);
                 } else {
                     this.collapsePath(path);
                 }

             }
         }
     }

     void setExpandedStateFromViewModel(@NotNull TreePath path, boolean state) {
         if (Tree.LOG.isDebugEnabled()) {
             Tree.LOG.debug("Setting expanded state=" + state + " from the view model " + String.valueOf(path));
         }

         if (Tree.this.isVisible(path)) {
             this.setExpandedState(path, state);
         } else {
             this.markPathExpandedState(path, state);
         }

     }

     private boolean expandParentPaths(@NotNull TreePath path) {
         Deque<TreePath> stack = null;
         TreePath parentPath = path.getParentPath();

         while(parentPath != null) {
             if (this.isExpanded(parentPath)) {
                 parentPath = null;
             } else {
                 if (stack == null) {
                     stack = new ArrayDeque();
                 }

                 stack.push(parentPath);
                 parentPath = parentPath.getParentPath();
             }
         }

         while(stack != null && !stack.isEmpty()) {
             parentPath = (TreePath)stack.pop();
             if (!this.isExpanded(parentPath)) {
                 try {
                     Tree.this.fireTreeWillExpand(parentPath);
                 } catch (ExpandVetoException var5) {
                     return false;
                 }

                 this.markPathExpanded(parentPath);
                 Tree.this.fireTreeExpanded(parentPath);
                 if (Tree.this.accessibleContext != null) {
                     ((JTree.AccessibleJTree)Tree.this.accessibleContext).fireVisibleDataPropertyChange();
                 }
             }
         }

         return true;
     }

     private void expandPath(@NotNull TreePath path) {
         if (!Boolean.TRUE.equals(this.expandedState.get(path))) {
             try {
                 Tree.this.fireTreeWillExpand(path);
             } catch (ExpandVetoException var3) {
                 return;
             }

             this.markPathExpanded(path);
             Tree.this.fireTreeExpanded(path);
             if (Tree.this.accessibleContext != null) {
                 ((JTree.AccessibleJTree)Tree.this.accessibleContext).fireVisibleDataPropertyChange();
             }

         }
     }

     private void collapsePath(@NotNull TreePath path) {
         if (Boolean.TRUE.equals(this.expandedState.get(path))) {
             try {
                 Tree.this.fireTreeWillCollapse(path);
             } catch (ExpandVetoException var3) {
                 return;
             }

             this.markPathCollapsed(path);
             Tree.this.fireTreeCollapsed(path);
             if (Tree.this.removeDescendantSelectedPaths(path, false) && !Tree.this.isPathSelected(path)) {
                 Tree.this.addSelectionPath(path);
             }

             if (Tree.this.accessibleContext != null) {
                 ((JTree.AccessibleJTree)Tree.this.accessibleContext).fireVisibleDataPropertyChange();
             }

         }
     }

     @Nullable Enumeration<TreePath> getDescendantToggledPaths(@Nullable TreePath parent) {
         if (parent == null) {
             return null;
         } else {
             List<TreePath> descendants = new ArrayList();

             for(TreePath path : this.expandedState.keySet()) {
                 if (parent.isDescendant(path)) {
                     descendants.add(path);
                 }
             }

             return Collections.enumeration(descendants);
         }
     }

     void removeDescendantToggledPaths(Enumeration<TreePath> toRemove) {
         if (toRemove != null) {
             while(toRemove.hasMoreElements()) {
                 Enumeration<?> descendants = this.getDescendantToggledPaths((TreePath)toRemove.nextElement());
                 if (descendants != null) {
                     while(descendants.hasMoreElements()) {
                         this.expandedState.remove(descendants.nextElement());
                     }
                 }
             }

         }
     }

     void clearToggledPaths() {
         this.expandedState.clear();
     }

     TreeModelListener createTreeModelListener() {
         return new TreeModelListenerImpl();
     }

     private class TreeModelListenerImpl implements TreeSwingModelListener {
         public void treeNodesChanged(TreeModelEvent e) {
             TreePath path = e.getTreePath();
             Object var4 = path.getLastPathComponent();
             if (var4 instanceof TreeNodeViewModel viewModel) {
                 this.applyViewModelChange(() -> Tree.this.expandImpl.setExpandedStateFromViewModel(path, viewModel.stateSnapshot().isExpanded()));
             }

         }

         private void applyViewModelChange(@NotNull Runnable runnable) {
             if (!Tree.this.applyingViewModelChanges.compareAndSet(false, true)) {
                 throw new IllegalStateException("Already applying a view model change, changes should not be recursive, it's a bug");
             } else {
                 try {
                     runnable.run();
                 } finally {
                     Tree.this.applyingViewModelChanges.set(false);
                 }

             }
         }

         public void treeNodesInserted(TreeModelEvent e) {
             TreeModel model = Tree.this.getModel();
             CachingTreePath path = Tree.this.getPath(e);
             if (model != null && path != null) {
                 Object parent = path.getLastPathComponent();
                 int childCount = model.getChildCount(parent);

                 for(int i : e.getChildIndices()) {
                     if (i >= 0 && i < childCount) {
                         Object newChild = model.getChild(parent, i);
                         if (Tree.LOG.isDebugEnabled()) {
                             Tree.LOG.debug("Inserted child " + i + " " + String.valueOf(newChild) + " of parent " + String.valueOf(parent));
                         }

                         TreePath childPath = path.pathByAddingChild(newChild);
                         if (newChild instanceof TreeNodeViewModel) {
                             this.applyViewModelChange(() -> this.applyNewNodeExpandedState(model, childPath));
                         }

                         if (ExpandImpl.this.cachedPresentation != null) {
                             ExpandImpl.this.cachedPresentation.updateExpandedNodes(childPath);
                         }
                     }
                 }

             }
         }

         private void applyNewNodeExpandedState(@NotNull TreeModel model, @NotNull TreePath path) {
             TreeNodeViewModel node = (TreeNodeViewModel)path.getLastPathComponent();
             boolean isExpanded = node.stateSnapshot().isExpanded();
             Tree.this.expandImpl.setExpandedStateFromViewModel(path, isExpanded);
             if (isExpanded) {
                 int childCount = model.getChildCount(node);

                 for(int i = 0; i < childCount; ++i) {
                     Object child = model.getChild(node, i);
                     if (child instanceof TreeNodeViewModel) {
                         this.applyNewNodeExpandedState(model, path.pathByAddingChild(child));
                     }
                 }
             }

         }

         public void treeStructureChanged(TreeModelEvent e) {
             if (e != null) {
                 TreePath parent = Tree.this.getPath(e);
                 if (parent != null) {
                     if (parent.getPathCount() == 1) {
                         ExpandImpl.this.clearToggledPaths();
                         Object treeRoot = Tree.this.treeModel.getRoot();
                         if (treeRoot != null && !Tree.this.treeModel.isLeaf(treeRoot)) {
                             ExpandImpl.this.markPathExpanded(parent);
                         }
                     } else if (ExpandImpl.this.expandedState.get(parent) != null) {
                         List<TreePath> toRemove = new ArrayList(1);
                         boolean isExpanded = ExpandImpl.this.isExpanded(parent);
                         toRemove.add(parent);
                         ExpandImpl.this.removeDescendantToggledPaths(Collections.enumeration(toRemove));
                         if (isExpanded) {
                             TreeModel model = Tree.this.getModel();
                             if (model != null && !model.isLeaf(parent.getLastPathComponent())) {
                                 ExpandImpl.this.markPathExpanded(parent);
                             } else {
                                 ExpandImpl.this.collapsePath(parent);
                             }
                         }
                     }

                     Tree.this.removeDescendantSelectedPaths(parent, false);
                     if (ExpandImpl.this.cachedPresentation != null) {
                         ExpandImpl.this.cachedPresentation.updateExpandedNodes(parent);
                     }

                 }
             }
         }

         public void treeNodesRemoved(TreeModelEvent e) {
             if (e != null) {
                 TreePath parent = Tree.this.getPath(e);
                 Object[] children = e.getChildren();
                 if (children != null && parent != null) {
                     List<TreePath> toRemove = new ArrayList(Math.max(1, children.length));

                     for(int counter = children.length - 1; counter >= 0; --counter) {
                         TreePath path = parent.pathByAddingChild(children[counter]);
                         if (ExpandImpl.this.expandedState.get(path) != null) {
                             toRemove.add(path);
                         }
                     }

                     if (!toRemove.isEmpty()) {
                         ExpandImpl.this.removeDescendantToggledPaths(Collections.enumeration(toRemove));
                     }

                     TreeModel model = Tree.this.getModel();
                     if (model == null || model.isLeaf(parent.getLastPathComponent())) {
                         ExpandImpl.this.expandedState.remove(parent);
                     }

                     this.removeDescendantSelectedPaths(e);
                 }
             }
         }

         private void removeDescendantSelectedPaths(TreeModelEvent e) {
             TreePath pPath = Tree.this.getPath(e);
             if (pPath != null) {
                 Object[] oldChildren = e.getChildren();
                 TreeSelectionModel sm = Tree.this.getSelectionModel();
                 if (sm != null && oldChildren != null && oldChildren.length > 0) {
                     for(int counter = oldChildren.length - 1; counter >= 0; --counter) {
                         Tree.this.removeDescendantSelectedPaths(pPath.pathByAddingChild(oldChildren[counter]), true);
                     }
                 }

             }
         }

         public void selectionChanged(@NotNull TreeSwingModelSelectionEvent event) {
             this.applyViewModelChange(() -> Tree.this.setSelectionPaths(event.getNewSelection()));
         }

         public void scrollRequested(@NotNull TreeSwingModelScrollEvent event) {
             this.applyViewModelChange(() -> TreeUtil.scrollToVisible(Tree.this, event.getScrollTo(), Registry.is("ide.tree.autoscrollToVCenter", false)));
         }
     }
 }

 private class MyUISettingsListener implements UISettingsListener {
     private boolean applyingUiSettings = false;
     private boolean toggleClickCountOverridden;
     private @Nullable MessageBusConnection connection;

     public void uiSettingsChanged(@NotNull UISettings uiSettings) {
         if (this.applyingUiSettings) {
             Tree.LOG.warn(new Throwable("Reentrant com.intellij.ui.treeStructure.Tree.MyUISettingsListener.uiSettingsChanged call"));
         } else {
             this.applyingUiSettings = true;

             try {
                 if (!this.toggleClickCountOverridden && Tree.isExpandWithSingleClickSettingEnabled()) {
                     Tree.this.setToggleClickCount(uiSettings.getExpandNodesWithSingleClick() ? 1 : 2);
                 }
             } finally {
                 this.applyingUiSettings = false;
             }

         }
     }

     void setToggleClickCountCalled() {
         if (!this.applyingUiSettings) {
             this.toggleClickCountOverridden = true;
         }

     }

     void connect() {
         this.disconnect();
         this.connection = ApplicationManager.getApplication().getMessageBus().connect();
         this.connection.subscribe(TOPIC, this);
         this.uiSettingsChanged(UISettings.getInstance());
     }

     void disconnect() {
         if (this.connection != null) {
             Disposer.dispose(this.connection);
             this.connection = null;
         }

     }
 }

 private class MyTreeModelListener implements TreeSwingModelListener {
     private final LazyInitializer.@NotNull LazyValue<@NotNull TreeModelListener> delegate = LazyInitializer.create(() -> Tree.this.expandImpl.createTreeModelListener());

     private @Nullable TreeModelListener delegate() {
         return Tree.this.initialized ? (TreeModelListener)this.delegate.get() : null;
     }

     public void treeNodesChanged(TreeModelEvent e) {
         TreeModelListener delegate = this.delegate();
         if (delegate != null) {
             delegate.treeNodesChanged(e);
         }

     }

     public void treeNodesInserted(TreeModelEvent e) {
         TreeModelListener delegate = this.delegate();
         if (delegate != null) {
             delegate.treeNodesInserted(e);
         }

     }

     public void treeNodesRemoved(TreeModelEvent e) {
         TreeModelListener delegate = this.delegate();
         if (delegate != null) {
             delegate.treeNodesRemoved(e);
         }

     }

     public void treeStructureChanged(TreeModelEvent e) {
         TreeModelListener delegate = this.delegate();
         if (delegate != null) {
             delegate.treeStructureChanged(e);
         }

     }

     public void selectionChanged(@NotNull TreeSwingModelSelectionEvent event) {
         TreeModelListener var3 = this.delegate();
         if (var3 instanceof TreeSwingModelListener treeSwingModelListener) {
             treeSwingModelListener.selectionChanged(event);
         }

     }

     public void scrollRequested(@NotNull TreeSwingModelScrollEvent event) {
         TreeModelListener var3 = this.delegate();
         if (var3 instanceof TreeSwingModelListener treeSwingModelListener) {
             treeSwingModelListener.scrollRequested(event);
         }

     }
 }

 public interface NodeFilter<T> {
     boolean accept(T var1);
 }
}
