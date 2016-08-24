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
	


	public ProcessImagePreview(String productPath,String logoPath,String createPath,
			Integer coordinatX,Integer coordinatY,String txtLogoName,String fileName,BufferedImage logoBuff,boolean isParent){
	    this.productPath = productPath;
	    this.logoPath = logoPath;
	    this.createPath = createPath;
	    this.coordinatX = coordinatX;
	    this.coordinatY = coordinatY;
	    this.txtLogoName = txtLogoName;
	    this.fileName = fileName;
	    this.logoBuff = logoBuff;
	    this.isParent = isParent;
	    
	}
	
	
	public HashMap<String, BufferedImage> call() {
		BufferedImage productBuff = null;
		HashMap<String, BufferedImage> imageMap = new HashMap<String, BufferedImage>(1);
		try {
			productBuff = ImageIO.read(new File(productPath));

			Graphics2D graphicProduct = (Graphics2D) productBuff.getGraphics();
			graphicProduct.drawImage(logoBuff, coordinatX, coordinatY, null);
			graphicProduct.dispose();			
			
			imageMap.put(fileName, productBuff);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imageMap;
		
		
	}

}
