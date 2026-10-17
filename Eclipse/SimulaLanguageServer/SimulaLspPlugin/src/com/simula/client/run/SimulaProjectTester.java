package com.simula.client.run;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;

public class SimulaProjectTester extends PropertyTester {
	
	private static final String PROPERTY_NAME = "isSimulaProject";
	
	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if (property.equals(PROPERTY_NAME)) {
			IResource resource = toResource(receiver);
			if (resource == null) {
				return false;
			}
			return isSimulaProject(resource.getProject());
		}
		return false;
	}

	private IResource toResource(Object obj) {
		if (obj instanceof IResource res) {
			return res;
		} else if (obj instanceof IAdaptable adp) {
			return adp.getAdapter(IResource.class);
		} else {
			return null;
		}
	}

	public static boolean isSimulaProject(IProject project) {
		if (project == null || ! project.isAccessible()) {
			return false;
		}
		try {
			for (IResource projItem : project.members()) {
				IO.println("SimulaProjectTester.isSimulaProject: DETTE MÅ TESTES: " + projItem.getName());
//				if (projItem.getName().matches("^.*\\.csproj$")) { //$NON-NLS-1$
				if (projItem.getName().matches("^.*\\.simulaproj$")) { //$NON-NLS-1$
					return true;
				}
			}
		} catch (CoreException e) {}
		return false;
	}
}
