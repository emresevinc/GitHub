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

public class ProcessImagePreview implements Callable{

	
	private String productPath;
	private String logoPath;
	private String createPath;
	private Integer coordinatX;
	private Integer coordinatY;
	private String txtLogoName;
	private String fileName;
	BufferedImage logoBuff;
	private boolean isParent;
	private HashMap<String, BufferedImage> imageMap;
	


	public ProcessImagePreview(String productPath,String logoPath,String createPath,
			Integer coordinatX,Integer coordinatY,String txtLogoName,String fileName,BufferedImage logoBuff,boolean isParent,HashMap imageMap){
	    this.productPath = productPath;
	    this.logoPath = logoPath;
	    this.createPath = createPath;
	    this.coordinatX = coordinatX;
	    this.coordinatY = coordinatY;
	    this.txtLogoName = txtLogoName;
	    this.fileName = fileName;
	    this.logoBuff = logoBuff;
	    this.isParent = isParent;
	    this.imageMap = imageMap;
	    
	}
	
	
	public HashMap<String, BufferedImage> call() {
		BufferedImage productBuff = null;
		
		try {
			System.out.println("ProcessImagePreview productPath:"+productPath);
			
			try {
				productBuff = ImageIO.read(new File(productPath));				
			} catch (IOException e) {
				System.out.println("ProcessImage Error Read ProductPath:");
				
				productBuff = ImageIO.read(new File(productPath));
				
			}

			Graphics2D graphicProduct = (Graphics2D) productBuff.getGraphics();
			graphicProduct.drawImage(logoBuff, coordinatX, coordinatY, null);
			graphicProduct.dispose();			

			imageMap.put(fileName, productBuff);
			
		} catch (IOException e) {
			System.out.println("ProcessImage Error :");
			e.printStackTrace();
		}
		return imageMap;
		
		
	}

}
