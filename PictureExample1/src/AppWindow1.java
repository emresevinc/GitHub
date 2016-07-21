import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;

import bean.CoreTemplate;
import bean.Model;
import bean.Product;

public class AppWindow1 {

	protected Shell shell;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			AppWindow1 window = new AppWindow1();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	Display display = null;

	/**
	 * Open the window.
	 */
	public void open() {
		display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	
	static int[] oldLogoCoordinates = {90,120}; 
	
	Label label = null;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text text;
	private static String logoPath = "";
	Label logoLabel = null;
	private static int selectedTemplate = 1;
	Button rdBtnSablon1 = null;
	Button rdBtnSablon2 = null;
	Button rdBtnSablon3 = null;
	private static final String logoAppPath = "C:/LogoApp/Models";
	private static final String scaledLogoPath = "C:\\LogoApp\\ScaledLogos";
	private static final String scaledAndProgressedPath = "C:\\LogoApp\\ScaledAndProgressed"; // Bu path iþlenmiþ ürünleri scale edilmiþ hallerinin bulunduðu path'tir. Ürünler iþlendikten sonra ekranda scale halini buradan alýyoruz
	private static final String nonProgressedScaledProductPath = "C:\\LogoApp\\ScaledImages"; // Bu path ekran ilk açýldýðýnda default olan ürünlerin scale hallerinin tutulduðu path'i gösterir. Ekran ilk açýldýðýnda bu bos urunler gozlemlenir
	private List<Model> modelList = null;
	private CoreTemplate coreTemplate = null;
	private Text textX;
	private Text textY;
	

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setMinimumSize(new Point(1920, 1024));
		shell.setSize(1354, 1012);
		shell.setText("SWT Application");
		shell.setLayout(null);

		
		//drawIt(sourcePathImg, sourcePathLogo, destPathImg, destPathLogo, createPath, 250, 300, 70, 70,oldLogoCoordinates[0],oldLogoCoordinates[1]);
        
		

		ExpandBar expandBar = new ExpandBar(shell, SWT.NONE);
		expandBar.setBounds(10, 252, 1850, 680);
		
		//ScrolledComposite composite = new ScrolledComposite(expandBar, SWT.H_SCROLL | SWT.V_SCROLL);
		
		Composite composite = new Composite(expandBar, SWT.V_SCROLL);
		GridLayout gridLayout = new GridLayout(7, false);
		gridLayout.horizontalSpacing = 20;
		composite.setLayout(gridLayout);
		
		
		ExpandItem xpndtmNewExpanditem = new ExpandItem(expandBar, SWT.V_SCROLL);
		xpndtmNewExpanditem.setExpanded(true);
		xpndtmNewExpanditem.setText("Gildan 500");
		
		
		
		// Bu kod bloðu model ürün hiyerarþisini saðlýyor ve model ürün bilgisini elde ediyor
		modelList = new ArrayList<Model>();// Orjinal model bilgilerini alýyor
		coreTemplate = new CoreTemplate(composite, SWT.NONE);
		File folder = new File(logoAppPath);
		File[] listOfFiles = folder.listFiles();
		Model tempModel = null;
	    for (int i = 0; i < listOfFiles.length; i++) {
	    	tempModel = new Model();
	    	if (listOfFiles[i].isDirectory()) {
	    		String modelFullPath = logoAppPath +"/"+listOfFiles[i].getName();
	    		String modelName = listOfFiles[i].getName();
	    		tempModel.setModelFullPath(modelFullPath);
	    		tempModel.setModelName(modelName);
	    		File folderIn = new File(modelFullPath);
	    		File[] listOfFiles2 = folderIn.listFiles();
	    		List<Product> productList = new ArrayList<Product>();
	    		Product tempProduct = null;
	    		for (int j = 0; j < listOfFiles2.length; j++) {
					tempProduct = new Product();
					tempProduct.setModelName(modelName);
					tempProduct.setProductFullPath(listOfFiles2[j].getPath());
					tempProduct.setProductName(listOfFiles2[j].getName());
					productList.add(tempProduct);
				}
	    		tempModel.setProductList(productList);
	    		modelList.add(tempModel);
	    	}
	    }
	    ////////////////////////////////////////////
	    
	    // Not : Ürünlerin iþlenmemiþ hallerinin ölçeklenmiþ halini program açýlýrken scale edip bir yere kaydederek guc harcamak yerine
	    // C:/LogoApp/ScaledImages altýna direk küçük hale scale edilmis halini koymak is yukunu hafifletir.
	    

	    showAllProducts(nonProgressedScaledProductPath);

		xpndtmNewExpanditem.setControl(composite);
		xpndtmNewExpanditem.setHeight(700);
		
		Button btnBirLogoSeiniz = formToolkit.createButton(shell, "Bir logo se\u00E7iniz", SWT.NONE);
		btnBirLogoSeiniz.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				chooseLogo();
			}
		});
		btnBirLogoSeiniz.setImage(SWTResourceManager.getImage("C:\\LogoApp\\ButtonIcons\\fileChooser.png"));
		btnBirLogoSeiniz.setBounds(27, 32, 151, 30);
		
		text = formToolkit.createText(shell, "New Text", SWT.NONE);
		text.setText("");
		text.setBounds(27, 90, 257, 20);
		
		logoLabel = formToolkit.createLabel(shell, "", SWT.NONE);
		
		//Coordinater
		coreTemplate.getRegion3().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				textX.setText(String.valueOf(e.x));
				textY.setText(String.valueOf(e.y));
			}
		});
		//
		logoLabel.setBounds(705, 46, 115, 115);
		
		Label labelTemplate1 = new Label(shell, SWT.NONE);
		labelTemplate1.setImage(SWTResourceManager.getImage("C:\\LogoApp\\Templates\\Template1.jpg"));
		labelTemplate1.setBounds(1008, 36, 160, 185);
		formToolkit.adapt(labelTemplate1, true, true);
		
		Label labelTemplate2 = new Label(shell, SWT.NONE);
		labelTemplate2.setImage(SWTResourceManager.getImage("C:\\LogoApp\\Templates\\Template2.jpg"));
		labelTemplate2.setBounds(1188, 36, 160, 185);
		formToolkit.adapt(labelTemplate2, true, true);
		
		Label labelTemplate3 = new Label(shell, SWT.NONE);
		labelTemplate3.setImage(SWTResourceManager.getImage("C:\\LogoApp\\Templates\\Template3.jpg"));
		labelTemplate3.setBounds(1367, 36, 160, 185);
		formToolkit.adapt(labelTemplate3, true, true);
		
		
		
		// radio button operations		
		rdBtnSablon1 = new Button(shell, SWT.RADIO);
		rdBtnSablon1.setBounds(1020, 10, 111, 20);
		formToolkit.adapt(rdBtnSablon1, true, true);
		rdBtnSablon1.setText("Sablon 1");
		
		rdBtnSablon1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedTemplate = 1;
				manageRadioButtons();
			}
		});
		
		rdBtnSablon2 = new Button(shell, SWT.RADIO);
		rdBtnSablon2.setBounds(1205, 10, 111, 20);
		formToolkit.adapt(rdBtnSablon2, true, true);
		rdBtnSablon2.setText("Sablon 2");
		
		rdBtnSablon2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedTemplate = 2;
				manageRadioButtons();
			}
		});
		
		rdBtnSablon3 = new Button(shell, SWT.RADIO);
		rdBtnSablon3.setBounds(1382, 10, 111, 20);
		formToolkit.adapt(rdBtnSablon3, true, true);
		rdBtnSablon3.setText("Sablon 3");
		
		textX = new Text(shell, SWT.BORDER);
		textX.setBounds(415, 10, 78, 26);
		formToolkit.adapt(textX, true, true);
		
		textY = new Text(shell, SWT.BORDER);
		textY.setBounds(523, 10, 78, 26);
		formToolkit.adapt(textY, true, true);
		
		Button btnApplyLogo = new Button(shell, SWT.NONE);
		btnApplyLogo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applyToLogo();
			}
		});
		btnApplyLogo.setBounds(1672, 122, 115, 30);
		formToolkit.adapt(btnApplyLogo, true, true);
		btnApplyLogo.setText("Logoyu Uygula");
		
		
		rdBtnSablon3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedTemplate = 3;
				manageRadioButtons();
			}
		});
		//radio button operations
		

	}
	
	public Image scaleToImage(BufferedImage img,int widthSc,int heightSc,String outPath){
		BufferedImage outputImage = new BufferedImage(widthSc,heightSc, img.getType());
		Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(img, 0, 0, widthSc, heightSc, null);
        g2d.dispose();
        try {
			ImageIO.write(outputImage, "png", new File(outPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
        Image imgx = new Image(display,outPath);
        return imgx;
	}
	
	
	
	public void drawIt(String sourcePathImg,String sourcePathLogo,String destPathImg,String destPathLogo,String createPath,int widthScaleImg,int heigthScaleImg,int widthScaleLogo,int heigthScaleLogo,int logoCoordinatX,int logoCoordinatY,boolean withoutScale){
		BufferedImage image = null;
		BufferedImage logo = null;
		try {
			   image = ImageIO.read(new File(sourcePathImg));
			   logo = ImageIO.read(new File(sourcePathLogo));
			   if(!withoutScale){// ölçeklenecekse
				   	scaleToImage(image, widthScaleImg, heigthScaleImg, destPathImg); // Ölçekleme yapýlýyor
				   	
			   }
			   scaleToImage(logo, widthScaleLogo, heigthScaleLogo, destPathLogo);
			   //image = ImageIO.read(new File(destPathImg)); // Ölçeklenmiþ kaynak dosyalara eriþiliyor.
			   logo = ImageIO.read(new File(destPathLogo));

			   Graphics2D imageGraphic = (Graphics2D) image.getGraphics();//createGraphics();
			   imageGraphic.drawImage(logo, logoCoordinatX,logoCoordinatY, null);//(image, 260, 50, 400, 70, null);
			   
			   ImageIO.write(image, "png", new File(createPath));
			   imageGraphic.dispose();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	}
	
	public void dressToImg(String createPath,Label label){
		Image imgx = new Image(display,createPath);
		logoLabel.setImage(imgx);
	}
	
	public void chooseLogo(){
		FileDialog fd = new FileDialog(shell, SWT.OPEN);
		fd.setText("Logo seçiniz");
		fd.setFilterPath("C:/");
        String[] filterExt = { "*.png", "*.jpg" };
        fd.setFilterExtensions(filterExt);
        logoPath = fd.open();
        text.setText(logoPath);
        processLogo();
	}
	
	public void processLogo(){
		BufferedImage choosenLogo = null;
		String createPath = "C:\\LogoApp\\SmallLogos\\"+getFileName(logoPath)+"-sm.png";
		try {
			choosenLogo = ImageIO.read(new File(logoPath));
			scaleToImage(choosenLogo, 115, 115, createPath); //C:/LogoApp/SmallLogos altýna ilgili logoyu sonunda -sm olacak þekilde ölçeklendirilmiþ halde kaydediyor
			dressToImg(createPath, logoLabel); // Ekranda küçük logo önizlemesi için kullanýlýyor. 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String getFileName(String filePath){
		File file = new File(filePath);
		String fileName = file.getName().split("\\.")[0];
		return fileName;
	}
	
	private void manageRadioButtons() {
		switch (selectedTemplate) {
		case 1:
			rdBtnSablon1.setSelection(true);
			rdBtnSablon2.setSelection(false);
			rdBtnSablon3.setSelection(false);
			break;
		case 2:
			rdBtnSablon1.setSelection(false);
			rdBtnSablon2.setSelection(true);
			rdBtnSablon3.setSelection(false);
			break;
		case 3:
			rdBtnSablon1.setSelection(false);
			rdBtnSablon2.setSelection(false);
			rdBtnSablon3.setSelection(true);
			break;
		default:
			break;
		}
	}
	
	private void applyToLogo(){
		int height=0, width = 0,coordinatX=0,coordinatY=0;
		switch (selectedTemplate) {
		case 1:
			height = 500;
			width = 600;
			coordinatX = 660;
			coordinatY = 600;
			break;
		case 2:
			height = 150;
			width = 70;
			coordinatX = 88;
			coordinatY = 93;
			break;
		case 3:
			height = 50;
			width = 60;
			coordinatX = 95;
			coordinatY = 132;
			break;
		default:
			break;
		}
		DirectoryDialog dlg = new DirectoryDialog(shell);
		dlg.setFilterPath("C:/");
		dlg.setText("SWT's DirectoryDialog");
		dlg.setMessage("Select a directory");
		String createPath = dlg.open();
		Model tempModel = null;
		for (int i = 0; i < modelList.size(); i++) {
			tempModel = modelList.get(i);
			List<Product> productList = tempModel.getProductList();
			Product tempProduct = null;
			for(int j = 0; j < productList.size();j++){
				tempProduct = productList.get(j);
				scaleLogoForOriginalProduct(tempProduct.getProductFullPath(), logoPath, getFileName(logoPath), 5); 
				
				drawww(tempProduct.getProductFullPath(), scaledLogoPath+"\\"+getFileName(logoPath)+".png", createPath, coordinatX, coordinatY);
				
				//drawIt(tempProduct.getProductFullPath(), "", "", logoPath, createPath+"\\"+tempProduct.getProductName(), 0, 0, width, height, coordinatX, coordinatY, true);
			}
		}
		showAllProducts(createPath);
	}
	
	// Logoyu ürünü içeren resim ile istenilen oranda küçültür
	// Yalnýzca logo üzerinde iþlem yapmak yeterli olacaktýr
	// Ekrana basýlan ölçeklenmiþ logolu ürünlerin buradan oluþturulacak logonun ürünün üzerine draw edilmiþ halinin ölçeklendirilmiþ hali olduðu bilinmelidir
	// Ölceklenmis logo C:\LogoApp\ScaledLogos altýna sonunda -scaled.png/jpg olacak þekilde kaydedilecektir
	private void scaleLogoForOriginalProduct(String productOriginalPath, String logoOriginalPath,String logoNameForPath,int scaleMeasure){ 
		
		BufferedImage productOriginal = null;
		BufferedImage logoOriginal = null;
		//düzenlenecek
		try
		{
			productOriginal = ImageIO.read(new File(productOriginalPath));
			logoOriginal = ImageIO.read(new File(logoOriginalPath));
			int productOriginalWidth = productOriginal.getWidth();
			int productOriginalHeight = productOriginal.getHeight();
	//		int logoOriginalWidth = logoOriginal.getWidth();
	//		int logoOriginalHeight = logoOriginal.getHeight();
			
			int createdLogoWidth = productOriginalWidth / scaleMeasure;
			int createdLogoHeight = productOriginalHeight / scaleMeasure;
			
			BufferedImage resizedLogo = new BufferedImage(createdLogoWidth, createdLogoHeight, logoOriginal.getType());
			Graphics2D graphic = resizedLogo.createGraphics();
			graphic.drawImage(logoOriginal, 0, 0, createdLogoWidth, createdLogoHeight, null);
			graphic.dispose();
		
		
			ImageIO.write(resizedLogo, "png", new File(scaledLogoPath+"\\"+logoNameForPath+".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void drawww(String productPath,String logoPath,String createPath,int coordinatX,int coordinatY){
		BufferedImage productBuff = null;
		BufferedImage logoBuff = null;
		
		try {
			productBuff = ImageIO.read(new File(productPath));
			logoBuff = ImageIO.read(new File(logoPath));
			
			Graphics2D graphicProduct = (Graphics2D) productBuff.getGraphics();
			graphicProduct.drawImage(logoBuff, coordinatX, coordinatY, null);
			graphicProduct.dispose();
			
			ImageIO.write(productBuff, "png", new File(createPath+"\\"+getFileName(productPath)+".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void showAllProducts(String parentPath){
		BufferedImage originalImage = null;
		for(int x = 0; x < modelList.get(0).getProductList().size(); x++){
	    	String productName = modelList.get(0).getProductList().get(x).getProductName();
	    	String productNameStr = productName.substring(0, productName.length()-4)+".png";
	    	try {
				originalImage = ImageIO.read(new File(parentPath+"\\"+productNameStr));
				coreTemplate.getAllRegionList().get(x).setImage(scaleToImage(originalImage, 250, 300, scaledAndProgressedPath+"\\"+productNameStr));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	    	
	    }
	}
	
}
