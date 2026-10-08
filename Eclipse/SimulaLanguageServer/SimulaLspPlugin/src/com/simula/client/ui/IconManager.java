package com.simula.client.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class IconManager {
    
    public static Image resizeImage(Image originalImage, int targetWidth, int targetHeight) {
        // 1. Create a blank destination image with the desired size
        Image scaledImage = new Image(Display.getDefault(), targetWidth, targetHeight);
        
        // 2. Use a GC to paint the original image onto the destination canvas
        GC gc = new GC(scaledImage);
        
        // 3. Optional: Configure settings for smooth, high-quality results
        gc.setAntialias(SWT.ON);
        gc.setInterpolation(SWT.HIGH);
        
        // 4. Draw the original image resized
        int sourceWidth = originalImage.getBounds().width;
        int sourceHeight = originalImage.getBounds().height;
        
        gc.drawImage(originalImage, 0, 0, sourceWidth, sourceHeight, 0, 0, targetWidth, targetHeight);
        
        // 5. Clean up the GC context to avoid OS resource leaks
        gc.dispose();
        
        return scaledImage;
    }


}
