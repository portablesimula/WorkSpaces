package com.simula.run;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;

public class EXAMPLE_SimulaRunTabGroup extends AbstractLaunchConfigurationTabGroup {

	@Override
	public void createTabs(ILaunchConfigurationDialog arg0, String arg1) {
		setTabs(new EXAMPLE_SimulaRunTab(), new CommonTab());
	}

}
