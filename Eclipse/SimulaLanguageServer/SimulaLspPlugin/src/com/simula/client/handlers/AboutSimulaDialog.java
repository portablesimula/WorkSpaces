package com.simula.client.handlers;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;

public class AboutSimulaDialog extends Dialog {

    protected AboutSimulaDialog(Shell parentShell) {
        super(parentShell);
        IO.println("NEW AboutDialog: " + parentShell);
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("About Simula");
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        container.setLayout(new GridLayout(1, false));

        // Use the SWT Link component to display text and native-style hyperlinks
        Link link = new Link(container, SWT.NONE);
//        String href = "<a href=\\\"https://example.com\\\">https://portablesimula.github.io/github.io/</a>";
//        String href = "<a href="https://portablesimula.github.io/github.io/">https://portablesimula.github.io/github.io/</a>";
        String href = "<a href=\"https://portablesimula.github.io/github.io/\">Portable Simula on GitHub</a>";

        link.setText(
        		"My Custom Eclipse Plugin\n"
        +"Version 1.0.0\n\n"
        				+"Visit our website at: " + href);
        link.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        // Handle hyperlink clicks using the Eclipse workbench browser subsystem
        link.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                try {
                    IWebBrowser browser = PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser();
                    browser.openURL(new java.net.URL(e.text));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        return container;
    }
}
