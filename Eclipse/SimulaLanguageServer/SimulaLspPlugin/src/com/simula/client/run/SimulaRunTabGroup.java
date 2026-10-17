package com.simula.client.run;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;

public class SimulaRunTabGroup extends AbstractLaunchConfigurationTabGroup {

//    @Override
//    public void createTabs(ILunchConfigurationDialog dialog, String mode) {
//        // Her definerer du rekkefølgen og utvalget av faner
//        ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {
//            new MyMainTab(),     // Din egen tilpassede fane (f.eks. for å velge hovedfil)
//            new CommonTab()      // Standard Eclipse-fane for fellesinnstillinger (lagring, favoritter)
//        };
//        
//        // Setter fanene inn i gruppen
//        setTabs(tabs);
//    }
    
	@Override
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		setTabs(
			new SimulaRunTab(),  // Din egen tilpassede fane (f.eks. for å velge hovedfil)
            new CommonTab()      // Standard Eclipse-fane for fellesinnstillinger (lagring, favoritter)
		);
	}

}
