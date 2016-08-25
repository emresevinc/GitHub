package bean;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class WriteImagePreview implements Callable{

	
	BufferedImage img;
	private int widthSc;
	private int heightSc;
	private String outPath;
	private String productName;
	private Display display;
	


	public WriteImagePreview(BufferedImage img,int widthSc,int heightSc,String outPath,String productName, Display display){
	    this.img = img;
	    this.widthSc = widthSc;
	    this.heightSc = heightSc;
	    this.outPath = outPath;
	    this.productName = productName;
	    this.display = display;
	    
	}
	
	
	public HashMap<String, Image> call() {
		HashMap<String, Image> imageMap = new HashMap<String, Image>();
		BufferedImage outputImage = new BufferedImage(widthSc,heightSc, img.getType());
		Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(img, 0, 0, widthSc, heightSc, null);
        g2d.dispose();
        System.out.println("WriteImagePreview outpath : "+outPath);
        try{
        	
        
        try {
			ImageIO.write(outputImage, "png", new File(outPath));
		} catch (IOException e) {
			System.out.println("WriteImagePreview hata..");
			e.printStackTrace();
			ImageIO.write(outputImage, "png", new File(outPath));
		}

        Image imgx = new Image(display, outPath); 			
		imageMap.put(productName, imgx);

		
        }catch(IOException e) {
        	System.out.println("WriteImagePreview hata2");
        	e.printStackTrace();
		}
        return imageMap;
		
	}

}
