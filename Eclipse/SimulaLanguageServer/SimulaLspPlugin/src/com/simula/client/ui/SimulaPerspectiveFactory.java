package com.simula.client.ui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class SimulaPerspectiveFactory implements IPerspectiveFactory {
	
	public SimulaPerspectiveFactory() {
		IO.println("NEW SimulaPerspectiveFactory");
	}

	@Override
	public void createInitialLayout(IPageLayout layout) {
		try {
			String editorArea = layout.getEditorArea();
			layout.addView(IPageLayout.ID_PROJECT_EXPLORER, IPageLayout.LEFT, 0.25f, editorArea);
//		    layout.addView(IPageLayout.ID_OUTLINE, IPageLayout.RIGHT, 0.70f, editorArea);
			layout.addView(IPageLayout.ID_PROBLEM_VIEW, IPageLayout.BOTTOM, 0.75f, editorArea);

			String path = getPathToRezisedIcon("icons/simula.png", 320, 180);
			if(path != null) layout.setEditorOnboardingImageUri(path);
			layout.setEditorOnboardingText(				
					"\nThis is a Simula System created by the Open Source Project 'Portable Simula Revisited'.\n"
					+ "The project was initiated as a response to the lecture held by James Gosling at the 50th\n"
					+ "anniversary of Simula at Ifi, University of Oslo (UiO) on 27th September, 2017.\n\n"

//					+ "This Simula System is written in pure Java and compiles directly to executable .jar\n"
//					+ "files using the new Java Classfile API.\n\n"

					+ "Simula source files should, by convention, end with .sim\n\n"

					+ "\t\t• Drop files here to open them\n"
					+ "\t\t• Menu: File -> Open File - to open files\n"
					+ "\t\t• Menu: File -> New - to create a new file\n"
					+ "\t\t• Press Ctrl+N to create a new file\n"
					+ "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String getPathToRezisedIcon(String path, int newWidth, int newHight) throws IOException {
		// 1. Get the bundle containing the resource
		Bundle bundle = FrameworkUtil.getBundle(this.getClass());

		// 2. Locate the file within your plugin (e.g., inside an "icons" or "images" directory)
		java.net.URL url = FileLocator.find(bundle, new Path(path), null);

		ImageData[] originalData = null;
		if (url != null) {
			// 3. Open an InputStream and load it via ImageLoader
			try (InputStream stream = url.openStream()) {
				ImageLoader loader = new ImageLoader();
				originalData = loader.load(stream); // Returns an array of ImageData
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}

		// 1. Last opprinnelig bilde
		ImageLoader loader = new ImageLoader();

		// 2. Definer ny størrelse og skaler ImageData
		ImageData scaledData = originalData[0].scaledTo(newWidth, newHight);

		// 3. Lagre til en midlertidig fil
		loader.data = new ImageData[] { scaledData };
		File tempFile = File.createTempFile("onboarding_scaled", ".png");
		loader.save(tempFile.getAbsolutePath(), SWT.IMAGE_PNG);

		// 4. Send den nye URI-en til layouten
		return tempFile.toURI().toString();
	}
}
