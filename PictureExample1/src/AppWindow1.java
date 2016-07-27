import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import bean.CoreTemplate;
import bean.Model;
import bean.Product;
import bean.ProductUI;

public class AppWindow1 {

	protected Shell shlLogoapp;
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
		shlLogoapp.open();
		shlLogoapp.layout();
		while (!shlLogoapp.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	
	
	Label label = null;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text text;
	private static String logoPath = "";
	Label logoLabel = null;
	private static int selectedTemplate = 1;
	Button rdBtnSablon1 = null;
	Button rdBtnSablon2 = null;
	Button rdBtnSablon3 = null;
	private static final String logoAppModelsPath = "C:\\LogoApp\\Models";
	private static final String scaledLogoPath = "C:\\LogoApp\\ScaledLogos";
	private static final String scaledAndProgressedPath = "C:\\LogoApp\\ScaledAndProgressed"; // Bu path iþlenmiþ ürünleri scale edilmiþ hallerinin bulunduðu path'tir. Ürünler iþlendikten sonra ekranda scale halini buradan alýyoruz
	private static final String nonProgressedScaledProductPath = "C:\\LogoApp\\ScaledImages"; // Bu path ekran ilk açýldýðýnda default olan ürünlerin scale hallerinin tutulduðu path'i gösterir. Ekran ilk açýldýðýnda bu bos urunler gozlemlenir
	private static final String changedTemplatesPath ="C:\\LogoApp\\ChangedTemplates";
	private List<Model> modelList = null;
	private static List<Composite> compositeList = new ArrayList<Composite>();
	private static List<CoreTemplate> coreTemplateList = new ArrayList<CoreTemplate>();
	private static List<ExpandItem> expandItemList = new ArrayList<ExpandItem>();
	private Text txtLogoName;
	Label labelTemplate1 = null;
	Label labelTemplate2 = null;
	Label labelTemplate3 = null;

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlLogoapp = new Shell();
		shlLogoapp.setMinimumSize(new Point(1024, 500));
		shlLogoapp.setSize(1354, 1012);
		shlLogoapp.setText("LogoApp.1.0");
		shlLogoapp.setLayout(null);
		
		// ilk Açýlýþta Ekranýn ortalanmasý için Hazýrlandý.
		Monitor primary = display.getPrimaryMonitor();
	    Rectangle bounds = primary.getBounds();
	    Rectangle rect = shlLogoapp.getBounds();	    
	    int x = bounds.x + (bounds.width - rect.width) / 2;
	    int y = bounds.y + (bounds.height - rect.height) / 2;	    
	    shlLogoapp.setLocation(x, y);
		
		// Bu kod bloðu model ürün hiyerarþisini saðlýyor ve model ürün bilgisini elde ediyor
		modelList = new ArrayList<Model>();// Orjinal model bilgilerini alýyor
		
		File folder = new File(logoAppModelsPath);
		File[] listOfFiles = folder.listFiles();
		Model tempModel = null;
	    for (int i = 0; i < listOfFiles.length; i++) {
	    	tempModel = new Model();
	    	if (listOfFiles[i].isDirectory()) {
	    		String modelFullPath = logoAppModelsPath +"\\"+listOfFiles[i].getName();
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
		
	 // Expandbar ve ExpandItem oluþturan kod bloðu
	 	ExpandBar expandBar = new ExpandBar(shlLogoapp, SWT.V_SCROLL);
	 	expandBar.setBounds(10, 249, 980, 357);		

	 	//ExpandItem oluþturma iþlemi
	    Composite composite = null;
		GridLayout gridLayout = null;
		CoreTemplate coreTemplate = null;
		ExpandItem xpndtmNewExpanditem = null; 
		Model currentModel = null;
		for (int i = 0; i < modelList.size(); i++) {
			currentModel = modelList.get(i);
			
			composite = new Composite(expandBar, SWT.NONE);
			gridLayout = new GridLayout(9, false); //3
			gridLayout.horizontalSpacing = 20;
			composite.setLayout(gridLayout);
			
			
			
			xpndtmNewExpanditem = new ExpandItem(expandBar, SWT.NONE,i);
			xpndtmNewExpanditem.setExpanded(false);
			xpndtmNewExpanditem.setText(modelList.get(i).getModelName());
			
			
			coreTemplate = new CoreTemplate(composite,currentModel); // label'larý gride teker teker uygun sýrada dolduracaktýr
			
			compositeList.add(composite);
			coreTemplateList.add(coreTemplate);
			expandItemList.add(xpndtmNewExpanditem);
			
			showProducts(coreTemplate); // Siradaki model altýndaki urunlerin ekrana basilmasi icin tetikleniyor
			
			xpndtmNewExpanditem.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);//(coreTemplate.getAllRegionList().size() / 3) + 1) * 300);
			xpndtmNewExpanditem.setControl(composite);
			
			
			
		}
		

	    
	    // Not : Ürünlerin iþlenmemiþ hallerinin ölçeklenmiþ halini program açýlýrken scale edip bir yere kaydederek guc harcamak yerine
	    // C:/LogoApp/ScaledImages altýna direk küçük hale scale edilmis halini koymak is yukunu hafifletir.
	    

	    

		
		
		Button btnBirLogoSeiniz = formToolkit.createButton(shlLogoapp, "Bir logo seciniz", SWT.NONE);
		btnBirLogoSeiniz.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				chooseLogo();
			}
		});
		btnBirLogoSeiniz.setImage(SWTResourceManager.getImage("C:\\LogoApp\\ButtonIcons\\fileChooser.png"));
		btnBirLogoSeiniz.setBounds(10, 33, 155, 35);
		
		text = formToolkit.createText(shlLogoapp, "New Text", SWT.BORDER | SWT.WRAP | SWT.MULTI);
		text.setText("");
		text.setBounds(90, 91, 145, 55);
		
		logoLabel = formToolkit.createLabel(shlLogoapp, "", SWT.NONE);
		logoLabel.setAlignment(SWT.CENTER);
		logoLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		
		
		logoLabel.setBounds(254, 33, 200, 200);
		
		labelTemplate1 = new Label(shlLogoapp, SWT.NONE);
		labelTemplate1.setImage(SWTResourceManager.getImage("C:\\LogoApp\\Templates\\Template1.jpg"));
		labelTemplate1.setBounds(477, 33, 200, 200);
		formToolkit.adapt(labelTemplate1, true, true);
		
		labelTemplate2 = new Label(shlLogoapp, SWT.NONE);
		labelTemplate2.setImage(SWTResourceManager.getImage("C:\\LogoApp\\Templates\\Template2.jpg"));
		labelTemplate2.setBounds(696, 33, 200, 200);
		formToolkit.adapt(labelTemplate2, true, true);
		
		labelTemplate3 = new Label(shlLogoapp, SWT.NONE);
		labelTemplate3.setImage(SWTResourceManager.getImage("C:\\LogoApp\\Templates\\Template3.jpg"));
		labelTemplate3.setBounds(914, 33, 200, 200);
		formToolkit.adapt(labelTemplate3, true, true);
		
		
		
		// radio button operations		
		rdBtnSablon1 = new Button(shlLogoapp, SWT.RADIO);
		rdBtnSablon1.setBounds(495, 9, 111, 20);
		formToolkit.adapt(rdBtnSablon1, true, true);
		rdBtnSablon1.setText("Sablon 1");
		
		rdBtnSablon1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedTemplate = 1;
				manageRadioButtons();
			}
		});
		
		rdBtnSablon2 = new Button(shlLogoapp, SWT.RADIO);
		rdBtnSablon2.setBounds(715, 9, 111, 20);
		formToolkit.adapt(rdBtnSablon2, true, true);
		rdBtnSablon2.setText("Sablon 2");
		
		rdBtnSablon2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedTemplate = 2;
				manageRadioButtons();
			}
		});
		
		rdBtnSablon3 = new Button(shlLogoapp, SWT.RADIO);
		rdBtnSablon3.setBounds(950, 9, 111, 20);
		formToolkit.adapt(rdBtnSablon3, true, true);
		rdBtnSablon3.setText("Sablon 3");
		
		
		Button btnApplyLogo = new Button(shlLogoapp, SWT.NONE);
		btnApplyLogo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applyToLogo();
			}
		});
		btnApplyLogo.setBounds(875, 629, 115, 39);
		formToolkit.adapt(btnApplyLogo, true, true);
		btnApplyLogo.setText("Logoyu Uygula");
		
		txtLogoName = new Text(shlLogoapp, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		txtLogoName.setBounds(90, 162, 145, 55);
		formToolkit.adapt(txtLogoName, true, true);
		
		Label lblLogoDizini = new Label(shlLogoapp, SWT.NONE);
		lblLogoDizini.setBounds(10, 91, 75, 20);
		formToolkit.adapt(lblLogoDizini, true, true);
		lblLogoDizini.setText("Logo Dizini:");
		
		Label lblLogoAd = new Label(shlLogoapp, SWT.NONE);
		lblLogoAd.setBounds(10, 165, 75, 20);
		formToolkit.adapt(lblLogoAd, true, true);
		lblLogoAd.setText("Logo Ad\u0131:");
		
		Button btnTemizle = new Button(shlLogoapp, SWT.NONE);
		btnTemizle.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlLogoapp.setVisible(false);
				AppWindow1 app = new AppWindow1();
				app.open();
				shlLogoapp.dispose();
			}
		});
		btnTemizle.setBounds(728, 629, 111, 39);
		formToolkit.adapt(btnTemizle, true, true);
		btnTemizle.setText("Temizle");
		
		
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
        //Image imgx = SWTResourceManager.getImage(outPath).getImageData();
        //Image imgx = new Image(display, SWTResourceManager.getImage(outPath).getImageData());
        Image imgx = new Image(display, outPath);
        return imgx;
	}
	
	
	public void dressToImg(String createPath,Label label){
		Image imgx = new Image(display,createPath);
		logoLabel.setImage(imgx);
	}
	
	public void chooseLogo(){
		FileDialog fd = new FileDialog(shlLogoapp, SWT.OPEN);
		fd.setText("Logo seçiniz");
		fd.setFilterPath("C:/");
        String[] filterExt = { "*.png", "*.jpg" };
        fd.setFilterExtensions(filterExt);
        String fdStr =  fd.open();
        String LogoName [];
        String LogoNameStr="";
        if(fdStr != null){
        	logoPath = fdStr;
        }else{
        	logoPath = "";
        }
       
        if(!logoPath.equals("")){
            text.setText(logoPath);
            LogoName = logoPath.split("\\\\");
            LogoNameStr = LogoName[LogoName.length-1];
            LogoNameStr = LogoNameStr.substring(0, LogoNameStr.indexOf('.'));
            txtLogoName.setText(LogoNameStr);
        	processLogo();
        	processTemplates();
        }
	}
	
	private void processTemplates() {// Ek gelistirme ile istedikleri urunun sablon olmasý istenirse bu metoda 'modelName' ve 'productName' parametre olarak gelmesi yeterli olur
		String modelName = "Gildan 5000 Mens Tee"; //"Gildan 18500 Unisex Hoodie";
		String modelNameStr = modelName.replace(" ", "_");
		String productName = "5000xC000"; //"18500xB00";
		List<Integer> templateCoordinates = null;
		List<Integer> calculatedPosition = new ArrayList<Integer>();
		BufferedImage logoOriginal = null;
		BufferedImage tempBufferedImage = null;
		String productPath = logoAppModelsPath+"\\"+modelName+"\\"+productName+".jpg";
		try{
			logoOriginal = ImageIO.read(new File(logoPath));
			
			for(int i = 1; i < 4; i++){
				calculatedPosition.clear();
				templateCoordinates = getProductTemplateCoordinates(i, templateCoordinates, modelNameStr, productName);
				int templateHeight = templateCoordinates.get(1) - templateCoordinates.get(4); // Bottom_LeftY - TopLeftY
				int templateWidth = templateCoordinates.get(2) - templateCoordinates.get(0);
				
				Dimension logoSize = new Dimension(logoOriginal.getWidth(), logoOriginal.getHeight());
				Dimension templateSize = new Dimension(templateWidth, templateHeight);			
				Dimension resizedLogoDimension = getScaledDimension(logoSize,templateSize); // Logo botunu template ve orjinal logo boyutuna göre belirler.
				
				calculatedPosition = calculateLogoStartingPosition(calculatedPosition,resizedLogoDimension,templateCoordinates.get(5),templateCoordinates.get(4),templateCoordinates.get(6)); // Logo her zaman þablonun üst sýnýrýna dayalý olacak (Özcan Bey Öyle olmasýný istiyor.) 
				
				BufferedImage resizedLogo = new BufferedImage(resizedLogoDimension.width, resizedLogoDimension.height, logoOriginal.getType());
				Graphics2D graphic = resizedLogo.createGraphics();
				graphic.drawImage(logoOriginal, 0, 0, resizedLogoDimension.width, resizedLogoDimension.height, null);
				graphic.dispose();
			
				ImageIO.write(resizedLogo, "png", new File(scaledLogoPath+"\\LogoForTemplate"+i+".png"));
				drawww(productPath, scaledLogoPath+"\\LogoForTemplate"+i+".png", changedTemplatesPath+"\\Template"+i, calculatedPosition.get(0), calculatedPosition.get(1));
				if(i == 1){
					tempBufferedImage = ImageIO.read(new File(changedTemplatesPath+"\\Template"+i+"\\"+txtLogoName.getText()+getFileName(productPath)+".png"));
					labelTemplate1.setImage(scaleToImage(tempBufferedImage, 200, 200, changedTemplatesPath+"\\Template"+i+"\\"+txtLogoName.getText()+getFileName(productPath)+"__"+i+".png"));
				}else if(i == 2){
					tempBufferedImage = ImageIO.read(new File(changedTemplatesPath+"\\Template"+i+"\\"+txtLogoName.getText()+getFileName(productPath)+".png"));
					labelTemplate2.setImage(scaleToImage(tempBufferedImage, 200, 200, changedTemplatesPath+"\\Template"+i+"\\"+txtLogoName.getText()+getFileName(productPath)+"__"+i+".png"));
				}else if(i == 3){
					tempBufferedImage = ImageIO.read(new File(changedTemplatesPath+"\\Template"+i+"\\"+txtLogoName.getText()+getFileName(productPath)+".png"));
					labelTemplate3.setImage(scaleToImage(tempBufferedImage, 200, 200, changedTemplatesPath+"\\Template"+i+"\\"+txtLogoName.getText()+getFileName(productPath)+"__"+i+".png"));
				}
			}
				
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void processLogo(){
		BufferedImage choosenLogo = null;
		String createPath = "C:\\LogoApp\\SmallLogos\\"+getFileName(logoPath)+"-sm.png";
		List<Integer> calculatedPosition = new ArrayList();
		try {
			choosenLogo = ImageIO.read(new File(logoPath));
			
			Dimension logoSize = new Dimension(choosenLogo.getWidth(), choosenLogo.getHeight());
			Dimension templateSize = new Dimension(logoLabel.getBounds().width, logoLabel.getBounds().height);			
			Dimension resizedLogoDimension = getScaledDimension(logoSize,templateSize);
			
			calculatedPosition = calculateLogoStartingPosition(calculatedPosition,resizedLogoDimension,
					logoLabel.getBounds().y,logoLabel.getBounds().x,logoLabel.getBounds().x+logoLabel.getBounds().width);
			
			
			scaleToImage(choosenLogo, (int)resizedLogoDimension.getWidth(), (int)resizedLogoDimension.getHeight(), createPath); 
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
		if(!logoPath.equals("")){
			List<Integer> calculatedPosition = new ArrayList();
			DirectoryDialog dlg = new DirectoryDialog(shlLogoapp);
			dlg.setFilterPath("C:/");
			dlg.setText("SWT's DirectoryDialog");
			dlg.setMessage("Select a directory");
			String tmpCreate = dlg.open();
			String createPath = "";
			if(tmpCreate != null){
				createPath = tmpCreate;
			}
			if(!createPath.equals("")){
				File parentDir = new File(createPath+"\\Parents");//Parent'lari kaydetmek icin
				if(!parentDir.exists())
					parentDir.mkdir();
				Model tempModel = null;
				List<Product> productList = null;
				CoreTemplate coreTemplate = null;
				List<ProductUI> productUIList = null;
				int coreTemplateSize = coreTemplateList.size();
				for (int i = 0; i < coreTemplateSize; i++) {
					coreTemplate = coreTemplateList.get(i);
					tempModel = coreTemplate.getModel();
					productList = tempModel.getProductList();
					productUIList = coreTemplate.getProductUIList();
					ProductUI tempProductUI = null;
					int productUIListSize = productUIList.size();
					
					//scaleLogoForOriginalProductForTemplate: measure parametresi yerine selectedTemplate 
					//kullanýlarak template'e göre modelTemplateCoordinate sabit sýnýfýndan ilgili ürün için sabitler okunarak
					// logonun doðru orantýlý olarak küçültülmesi saðlanacak.
					
					for(int j = 0; j < productUIListSize;j++){
						tempProductUI = productUIList.get(j);
						if(tempProductUI.getCheckIsApply().getSelection()){
							String productFullPath = logoAppModelsPath +"\\"+tempProductUI.getModelName()+"\\"+tempProductUI.getProductName();
							calculatedPosition = scaleLogoForOriginalProductForTemplate(productFullPath, logoPath, getFileName(logoPath), selectedTemplate);  
							drawww(productFullPath, scaledLogoPath+"\\"+getFileName(logoPath)+".png", createPath, calculatedPosition.get(0), calculatedPosition.get(1));
							if(tempProductUI.getRadioParent().getSelection()){ 
								drawww(productFullPath, scaledLogoPath+"\\"+getFileName(logoPath)+".png", createPath, calculatedPosition.get(0), calculatedPosition.get(1));
								File source = new File(createPath+"\\"+txtLogoName.getText()+getFileName(productFullPath)+".png");
								File dest =  new File(createPath+"\\Parents\\"+txtLogoName.getText()+getFileName(productFullPath)+".png");
								try {
									Files.copy(source.toPath(), dest.toPath());
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}else{
							tempProductUI.getCheckIsApply().setVisible(false);
							tempProductUI.getRadioParent().setVisible(false);
							tempProductUI.getLblProductImg().setVisible(false);
						}
						//drawIt(tempProduct.getProductFullPath(), "", "", logoPath, createPath+"\\"+tempProduct.getProductName(), 0, 0, width, height, coordinatX, coordinatY, true);
					}
				}
				showAllProducts(createPath);
			}
		}else{
			 MessageBox messageBox = new MessageBox(shlLogoapp, SWT.ERROR);
			 messageBox.setText("HATA");
			 messageBox.setMessage("Logo seçmelisiniz!");
			 messageBox.open();
		}
	}
	
	
	
	private List<Integer> scaleLogoForOriginalProductForTemplate(String productOriginalPath, String logoOriginalPath,String logoNameForPath,int selectedTemplate){ 
		
		BufferedImage productOriginal = null;
		BufferedImage logoOriginal = null;
		List<Integer> productTemplateCoodinates = new ArrayList(); 
		List<Integer> calculatedPosition = new ArrayList();
		//düzenlenecek
		try
		{
			//productOriginalPath: C:\LogoApp\Models\Gildan 18500 Unisex Hoodie\18500xB00.jpg
			//String replacedProductPath = productOriginalPath.replace('\\', "\\");
			String[] paths = productOriginalPath.split("\\\\");
			String modelName = paths[paths.length-2].replace(" ", "_");
			String productName = paths[paths.length-1];
			productName = productName.substring(0, productName.indexOf('.'));
			
			logoOriginal = ImageIO.read(new File(logoOriginalPath));		
			
			//productTemplateCoodinates Seçilen þablona göre ilgili ürünün o þablona ait koordinatlarýný dönecek
			// productTemplateCoodinates [Bottom_LeftX , Bottom_LeftY , Bottom_RightX , Bottom_RightY , TopLeftX , TopLeftY , TopRightX , TopRightY]
			productTemplateCoodinates = getProductTemplateCoordinates(selectedTemplate,productTemplateCoodinates,modelName,productName);
			
			int templateHeight = productTemplateCoodinates.get(1) - productTemplateCoodinates.get(5); // Bottom_LeftY - TopLeftY
			int templateWidth = productTemplateCoodinates.get(2) - productTemplateCoodinates.get(0); //Bottom_rightX - BottomWidthX
			
			Dimension logoSize = new Dimension(logoOriginal.getWidth(), logoOriginal.getHeight());
			Dimension templateSize = new Dimension(templateWidth, templateHeight);			
			Dimension resizedLogoDimension = getScaledDimension(logoSize,templateSize); // Logo botunu template ve orjinal logo boyutuna göre belirler.
			
			calculatedPosition = calculateLogoStartingPosition(calculatedPosition,resizedLogoDimension,productTemplateCoodinates.get(5),productTemplateCoodinates.get(4),productTemplateCoodinates.get(6)); // Logo her zaman þablonun üst sýnýrýna dayalý olacak (Özcan Bey Öyle olmasýný istiyor.) 
			
			BufferedImage resizedLogo = new BufferedImage(resizedLogoDimension.width, resizedLogoDimension.height, logoOriginal.getType());
			Graphics2D graphic = resizedLogo.createGraphics();
			graphic.drawImage(logoOriginal, 0, 0, resizedLogoDimension.width, resizedLogoDimension.height, null);
			graphic.dispose();
		
		
			ImageIO.write(resizedLogo, "png", new File(scaledLogoPath+"\\"+logoNameForPath+".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return calculatedPosition;
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

	public static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {

	    int original_width = imgSize.width;
	    int original_height = imgSize.height;
	    int bound_width = boundary.width;
	    int bound_height = boundary.height;
	    int new_width = original_width;
	    int new_height = original_height;

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

	    return new Dimension(new_width, new_height);
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
		default:
			break;
		}
		
		JSONParser parser = new JSONParser();
		
		try {
			Object obj = parser.parse(new FileReader(new File("res\\ProductInfo.json")));//getClass().getClassLoader().getResource("ProductInfo.json").getFile())));//getClass().getClassLoader().getResource("").getFile()));
			
			JSONObject job = (JSONObject) obj;
			
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

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return productTemplateCoodinates;
	}
	
	

	public void drawww(String productPath,String logoPath,String createPath,int coordinatX,int coordinatY){
		BufferedImage productBuff = null;
		BufferedImage logoBuff = null;
		File f = null;
		try {
			productBuff = ImageIO.read(f = new File(productPath));
			logoBuff = ImageIO.read(new File(logoPath));
			
			Graphics2D graphicProduct = (Graphics2D) productBuff.getGraphics();
			graphicProduct.drawImage(logoBuff, coordinatX, coordinatY, null);
			graphicProduct.dispose();
			
//			File modelDirectory = new File(createPath+"\\"+f.getParentFile().getName());
//			if(!modelDirectory.exists())
//				modelDirectory.mkdir();
//			ImageIO.write(productBuff, "png", new File(createPath+"\\"+f.getParentFile().getName()+"\\"+txtLogoName.getText()+getFileName(productPath)+".png"));
			ImageIO.write(productBuff, "png", new File(createPath+"\\"+txtLogoName.getText()+getFileName(productPath)+".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void showAllProducts(String parentPath){
		BufferedImage originalImage = null;
		Model model = null;
		CoreTemplate coreTemplate = null;
		List<ProductUI> tempProductUIList = null;
		int coreTemplateSize = coreTemplateList.size();
		for(int i = 0; i<coreTemplateSize; i++){
			model = coreTemplateList.get(i).getModel();
			coreTemplate = coreTemplateList.get(i);
			tempProductUIList = coreTemplate.getProductUIList();
			int productSize = tempProductUIList.size();
			ProductUI productUI = null;
			for(int x = 0; x < productSize; x++){
		    	productUI = tempProductUIList.get(x);
		    	if(productUI.getCheckIsApply().getSelection()){
					String productName = productUI.getProductName();
			    	String productNameStr = productName.substring(0, productName.length()-4)+".png";
			    	
			    	try {
						originalImage = ImageIO.read(new File(parentPath+"\\"+txtLogoName.getText()+productNameStr));
						tempProductUIList.get(x).getLblProductImg().setImage(scaleToImage(originalImage, 163, 195, scaledAndProgressedPath+"\\"+productNameStr));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
		    	}
		    }
		}
	}
	
	private void showProducts(CoreTemplate coreTemplate){ 
		BufferedImage originalImg = null;
		Model model = coreTemplate.getModel();
		List<Product> productList = model.getProductList();
		List<ProductUI> regionList = coreTemplate.getProductUIList();
		ProductUI tempProductUI = null;
		Product tempProduct = null;
		File folder = new File(nonProgressedScaledProductPath+"\\"+model.getModelName());
		if(!folder.exists())// olceklenmis fakat islenmemis modelin urunlerinin saklanabilmesi icin ilgili klasor yoksa olusturuluyor
			folder.mkdir();
		File[] listOfFiles = folder.listFiles();
		int scaledImgcount = listOfFiles.length;
		String productName = "";
		for (int i = 0; i < productList.size(); i++) {
			tempProduct = productList.get(i);
			tempProductUI = regionList.get(i);
			productName = tempProduct.getProductName().split("\\.")[0];
			try {
				if(scaledImgcount == 0){ // Program ilk calistirildiginda scale edilip logo islenmemis urunlerin ilgili klasor altýnda olmamasina karsin burada o urunleri olusturuyor. Bunlar program acildiginda ekrana basilan ilk gorsellerdir
					originalImg = ImageIO.read(new File(model.getModelFullPath()+"\\"+tempProduct.getProductName()));
					tempProductUI.getLblProductImg().setImage(scaleToImage(originalImg, 200, 200, nonProgressedScaledProductPath+"\\"+model.getModelName()+"\\"+productName+".png"));
				}else{
					tempProductUI.getLblProductImg().setImage(new Image(display, nonProgressedScaledProductPath+"\\"+model.getModelName()+"\\"+productName+".png"));					
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
}
