package bean;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.imageio.ImageIO;

public class ProcessImage implements Runnable{

	
	private String productPath;
	private String logoPath;
	private String createPath;
	private Integer coordinatX;
	private Integer coordinatY;
	private String txtLogoName;
	private String fileName;
	BufferedImage logoBuff;
	private boolean isParent;
	


	public ProcessImage(String productPath,String logoPath,String createPath,
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
	
	
	@Override
	public void run() {
		BufferedImage productBuff = null;
		File f = null;
		try {
			productBuff = ImageIO.read(f = new File(productPath));

			Graphics2D graphicProduct = (Graphics2D) productBuff.getGraphics();
			graphicProduct.drawImage(logoBuff, coordinatX, coordinatY, null);
			graphicProduct.dispose();			
			
			File preparedImageFile = new File(createPath+"\\"+txtLogoName+fileName+".jpg");
			
			ImageIO.write(productBuff, "jpg", preparedImageFile);
			
			if(isParent){
				File dest =  new File(createPath+"\\Parents\\"+txtLogoName+fileName+".jpg");
				Files.copy(preparedImageFile.toPath(), dest.toPath(),StandardCopyOption.REPLACE_EXISTING);
				
				
			}
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

}
