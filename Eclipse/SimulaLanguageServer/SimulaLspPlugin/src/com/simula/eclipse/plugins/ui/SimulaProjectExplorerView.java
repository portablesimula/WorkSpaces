package com.simula.eclipse.plugins.ui;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.part.ViewPart;

import com.simula.client.ui.dnd.SimulaProjectDropListener;

public class SimulaProjectExplorerView extends ViewPart {
    
    private TreeViewer viewer;

    @Override
    public void createPartControl(org.eclipse.swt.widgets.Composite parent) {
        viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        
        // Sett opp innholds- og labelleverandører for Simula-prosjektet ditt
        // viewer.setContentProvider(new SimulaContentProvider());
        // viewer.setLabelProvider(new SimulaLabelProvider());

        // Definer gyldige operasjoner og overføringstyper (FileTransfer for eksterne filer)
        int operations = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_DEFAULT;
        Transfer[] transferTypes = new Transfer[] { FileTransfer.getInstance() };
        
        // Koble drop-lytteren til vieweren
        viewer.addDropSupport(operations, transferTypes, new SimulaProjectDropListener(viewer));
    }

    @Override
    public void setFocus() {
        viewer.getControl().setFocus();
    }
}
