package com.simula.client;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class SimulaPluginActivator extends AbstractUIPlugin {

	 // Plug-in ID definert i MANIFEST.MF
    public static final String PLUGIN_ID = "com.simula.lsp.client";

    private static SimulaPluginActivator plugin;

    public SimulaPluginActivator() {
        // Standard konstruktør kreves av OSGi-rammeverket
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    public static SimulaPluginActivator getDefault() {
        return plugin;
    }
}
