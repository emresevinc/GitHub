package bean;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.eclipse.swt.graphics.Point;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ProcessModel implements Runnable{

	
	private List<ProductUI> productUI;
	private String createPath;
	private String txtLogoName;
	private int selectedTemplate;
	BufferedImage logoOriginal;
	static Object jsonMainobj;


	private String fileName;	
	BufferedImage resizedLogo = null;
	


	public ProcessModel(List<ProductUI> productUI,String createPath,
			String txtLogoName,int selectedTemplate,
			BufferedImage logoOriginal,Object jsonMainobj){
	    this.productUI = productUI;
	    this.createPath = createPath;
	    this.txtLogoName = txtLogoName;
	    this.selectedTemplate = selectedTemplate;
	    this.logoOriginal = logoOriginal;
	    this.jsonMainobj = jsonMainobj;

	    
	    
	}
	
	
	@Override
	public void run() {
		BufferedImage productBuff = null;
		File f = null;
		ProductUI tempProductUI = null;
		String productFullPath = null;
		String logoAppModelsPath = "C:\\LogoApp\\Models";
		List<Integer> calculatedPosition = new ArrayList();
		List<Integer> productTemplateCoodinates = new ArrayList();
		try {
			
			for(int j = 0; j < productUI.size();j++){
				tempProductUI = productUI.get(j);
//				if(tempProductUI.getCheckIsApply().getSelection()){
					
					calculatedPosition.clear();
					productTemplateCoodinates.clear();
					fileName = getFileName(tempProductUI.getProductName());
					productFullPath = logoAppModelsPath +"\\"+tempProductUI.getModelName()+"\\"+tempProductUI.getProductName();
					calculatedPosition = scaleLogoForOriginalProductForTemplate(productFullPath,selectedTemplate,calculatedPosition,productTemplateCoodinates);
					
					productBuff = ImageIO.read(f = new File(productFullPath));
					
					Graphics2D graphicProduct = (Graphics2D) productBuff.getGraphics();
					graphicProduct.drawImage(resizedLogo, calculatedPosition.get(0), calculatedPosition.get(1), null);
					graphicProduct.dispose();			
					
					File preparedImageFile = new File(createPath+"\\"+txtLogoName+fileName+".jpg");
					
					ImageIO.write(productBuff, "jpg", preparedImageFile);
					
//					if(tempProductUI.getRadioParent().getSelection()){
//						File dest =  new File(createPath+"\\Parents\\"+txtLogoName+fileName+".jpg");
//						Files.copy(preparedImageFile.toPath(), dest.toPath(),StandardCopyOption.REPLACE_EXISTING);
//					}
//				}
					
					if(j % 3 == 0 ){
						System.out.println("System.GC");
						System.gc();
					}
				
			}
			
			
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	private List<Integer> scaleLogoForOriginalProductForTemplate(String productOriginalPath,int selectedTemplate,List<Integer> calculatedPosition, List<Integer> productTemplateCoodinates){ 
		
		try {			
		BufferedImage productOriginal = null;		
		String[] paths = productOriginalPath.split("\\\\");
		String modelName = paths[paths.length-2].replace(" ", "_");
		String productName = paths[paths.length-1];
		productName = productName.substring(0, productName.indexOf('.'));
		
		System.out.println("productName:"+productName+" modelName:"+modelName);
		productTemplateCoodinates = getProductTemplateCoordinates(selectedTemplate,productTemplateCoodinates,modelName,productName);
		
		int templateHeight = productTemplateCoodinates.get(1) - productTemplateCoodinates.get(5); // Bottom_LeftY - TopLeftY
		int templateWidth = productTemplateCoodinates.get(2) - productTemplateCoodinates.get(0); //Bottom_rightX - BottomWidthX
		
		Dimension logoSize = new Dimension(logoOriginal.getWidth(), logoOriginal.getHeight());
		Dimension templateSize = new Dimension(templateWidth, templateHeight);			
		Dimension resizedLogoDimension = getScaledDimension(logoSize,templateSize); // Logo botunu template ve orjinal logo boyutuna göre belirler.
		
		calculatedPosition = calculateLogoStartingPosition(calculatedPosition,resizedLogoDimension,productTemplateCoodinates.get(5),productTemplateCoodinates.get(4),productTemplateCoodinates.get(6)); 
		
		resizedLogo = new BufferedImage(resizedLogoDimension.width, resizedLogoDimension.height, logoOriginal.getType());
		
		if(!productTemplateCoodinates.get(5).equals(productTemplateCoodinates.get(7))){
			BufferedImage tempLogoOriginal = null ;
			tempLogoOriginal = logoOriginal;
			double angle = 0 ;
			
				angle = getAngle(new Point(productTemplateCoodinates.get(4),productTemplateCoodinates.get(5)),
						new Point(productTemplateCoodinates.get(6),productTemplateCoodinates.get(7)));				
				
				if(productName.equals("8880xNP00")){
					angle= -0.15;
					calculatedPosition.set(1, productTemplateCoodinates.get(7));
				}else if(productName.equals("8880xNY00")){
					angle= +0.13;					
				}else if((productName.equals("S190TCxHF") 
						|| productName.equals("S190TCxHR")||productName.equals("S190TCxHU"))){
					angle= +0.05;
				}
				
			tempLogoOriginal = rotate(tempLogoOriginal,angle);
			
			Graphics2D graphic = resizedLogo.createGraphics();
			graphic.drawImage(tempLogoOriginal, 0, 0, resizedLogoDimension.width, resizedLogoDimension.height, null);
			graphic.dispose();
			
		}else{
			
			Graphics2D graphic = resizedLogo.createGraphics();
			graphic.drawImage(logoOriginal, 0, 0, resizedLogoDimension.width, resizedLogoDimension.height, null);
			graphic.dispose();
		}
		
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return calculatedPosition;
	}
	
	private List<Integer> getProductTemplateCoordinates(int selectedTemplate,
			List<Integer> productTemplateCoodinates, String modelName, String productName) {
		
		productTemplateCoodinates = new ArrayList<Integer>();
		String selectedTemplateStr = "";
		
		switch (selectedTemplate) {
		case 1:
			selectedTemplateStr = "Template1";
			break;
		case 2:
			selectedTemplateStr = "Template2";
			break;
		case 3:
			selectedTemplateStr = "Template3";
			break;
		case 4:
			selectedTemplateStr = "Template4";
			break;
		default:
			break;
		}
		
		
			
		JSONObject job = (JSONObject) jsonMainobj;
		
		JSONArray jar = (JSONArray)job.get(selectedTemplateStr);
		int size = jar.size();
		JSONObject jobj = null;
		for (int i = 0; i < size; i++) {
			jobj = (JSONObject)jar.get(i);
			JSONArray jsonArr = (JSONArray) jobj.get(modelName);
			if(jsonArr != null){
				int size2 = jsonArr.size();
				for (int j = 0; j < size2; j++) {
					JSONObject jobjk = (JSONObject) jsonArr.get(j);
					if (jobjk.get("ProductName").equals(productName)) {
						String[] coordinatesTopLeft = jobjk.get("Top_Left").toString().split(",");
						String[] coordinatesBottomLeft = jobjk.get("Bottom_Left").toString().split(",");
						String[] coordinatesTopRight = jobjk.get("Top_Right").toString().split(",");
						String[] coordinatesBottomRight = jobjk.get("Bottom_Right").toString().split(",");
						
						productTemplateCoodinates.add(Integer.parseInt(coordinatesBottomLeft[0]));
						productTemplateCoodinates.add(Integer.parseInt(coordinatesBottomLeft[1]));
						productTemplateCoodinates.add(Integer.parseInt(coordinatesBottomRight[0]));
						productTemplateCoodinates.add(Integer.parseInt(coordinatesBottomRight[1]));
						productTemplateCoodinates.add(Integer.parseInt(coordinatesTopLeft[0]));
						productTemplateCoodinates.add(Integer.parseInt(coordinatesTopLeft[1]));
						productTemplateCoodinates.add(Integer.parseInt(coordinatesTopRight[0]));
						productTemplateCoodinates.add(Integer.parseInt(coordinatesTopRight[1]));
						break;
					}
				}
			}
		}
		
		return productTemplateCoodinates;
	}
	
	private String getFileName(String filePath){
		File file = new File(filePath);
		String fileName = file.getName().split("\\.")[0];
		return fileName;
	}
	
	public static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {

	    int original_width = imgSize.width;
	    int original_height = imgSize.height;
	    int bound_width = boundary.width;
	    int bound_height = boundary.height;
	    int new_width = original_width;
	    int new_height = original_height;
	    
	    if( (original_width < bound_width) && (original_height<bound_height)){

	    	new_width = bound_width;
	    	
	    	new_height = (new_width * original_height) / original_width;
	    	
	    	if(new_height>bound_height){
	    		new_height = bound_height;
	    		new_width = (new_height * original_width) / original_height;
	    	}
	    	
	    }else{
	    
	    // first check if we need to scale width
	    if (original_width > bound_width) {
	        //scale width to fit
	        new_width = bound_width;
	        //scale height to maintain aspect ratio
	        new_height = (new_width * original_height) / original_width;
	    }

	    // then check if we need to scale even with the new height
	    if (new_height > bound_height) {
	        //scale height to fit instead
	        new_height = bound_height;
	        //scale width to maintain aspect ratio
	        new_width = (new_height * original_width) / original_height;
	    }
	    
	    }

	    return new Dimension(new_width, new_height);
	}
	
	private List<Integer> calculateLogoStartingPosition(List<Integer> calculatedPosition,
			Dimension resizedLogoDimension, Integer topLeftY,
			Integer topLeftX, Integer topRightX) {
		
		int StartingX ,StartingY ;
		
		StartingY = topLeftY ;
		
		if( (topRightX-topLeftX)>resizedLogoDimension.getWidth()){
			
			StartingX = (int) ((int) topLeftX + (((topRightX-topLeftX)-resizedLogoDimension.getWidth())/2));
		}else{
			StartingX = topLeftX;
		}
		
		calculatedPosition.add(StartingX);
		calculatedPosition.add(StartingY);
		
		return calculatedPosition;
	}
	
	public double getAngle(Point p1,Point p2) {
		double xDiff = p2.x - p1.x;
        double yDiff = p2.y - p1.y;
        return Math.toDegrees(Math.atan2(yDiff, xDiff));
	}
	public static BufferedImage rotate(BufferedImage image, double angle) {
	    double sin = Math.abs(Math.sin(angle)), cos = Math.abs(Math.cos(angle));
	    int w = image.getWidth(), h = image.getHeight();
	    int neww = (int)Math.floor(w*cos+h*sin), newh = (int) Math.floor(h * cos + w * sin);
	    GraphicsConfiguration gc = getDefaultConfiguration();
	    BufferedImage result = gc.createCompatibleImage(neww, newh, Transparency.TRANSLUCENT);
	    Graphics2D g = result.createGraphics();
	    g.translate((neww - w) / 2, (newh - h) / 2);
	    g.rotate(angle, w / 2, h / 2);
	    g.drawRenderedImage(image, null);
	    g.dispose();
	    return result;
	}
	private static GraphicsConfiguration getDefaultConfiguration() {
	    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    GraphicsDevice gd = ge.getDefaultScreenDevice();
	    return gd.getDefaultConfiguration();
	}
}
