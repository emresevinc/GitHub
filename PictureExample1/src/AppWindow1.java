import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import bean.CoreTemplate;
import bean.Model;
import bean.ProcessImage;
import bean.ProcessImagePreview;
import bean.Product;
import bean.ProductUI;
import bean.WriteImagePreview;

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
	
	
	
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text txtLogo;
	private static String logoPath = "";
	Label logoLabel = null;
	private static int selectedTemplate = 1;
	Button rdBtnSablon1 = null;
	Button rdBtnSablon2 = null;
	Button rdBtnSablon3 = null;
	Button rdBtnSablon4 = null;
	Button btnIsApplyPreviewChk = null;
	private static final String logoAppModelsPath = "C:\\LogoApp\\Models";
	private static final String scaledLogoPath = "C:\\LogoApp\\ScaledLogos";
	private static final String scaledAndProgressedPath = "C:\\LogoApp\\ScaledAndProgressed"; // Bu path iþlenmiþ ürünleri scale edilmiþ hallerinin bulunduðu path'tir. Ürünler iþlendikten sonra ekranda scale halini buradan alýyoruz
	private static final String nonProgressedScaledProductPath = "C:\\LogoApp\\ScaledImages"; // Bu path ekran ilk açýldýðýnda default olan ürünlerin scale hallerinin tutulduðu path'i gösterir. Ekran ilk açýldýðýnda bu bos urunler gozlemlenir
	private static final String changedTemplatesPath ="C:\\LogoApp\\ChangedTemplates";
	private List<Model> modelList = null;
	private static List<CoreTemplate> coreTemplateList = new ArrayList<CoreTemplate>();
	private Text txtLogoName;
	Label labelTemplate1 = null;
	Label labelTemplate2 = null;
	Label labelTemplate3 = null;
	Label labelTemplate4 = null;
	BufferedImage logoOriginal = null;
	BufferedImage resizedLogo = null;
	BufferedImage logoOriginalForTemplate = null;
	private static final String previewPath ="C:\\LogoApp\\Preview";
	private HashMap<String, BufferedImage> previewBuffer;
	private HashMap<String, Image> labelImage;
	private Text txtSaveDirectory;
	private HashMap<TableItem, CoreTemplate> mapControlModel = null;
	private static final String baseTemplate = "C:\\LogoApp\\Templates\\SizeChart.png";
	private ExpandBar expandBar = null;
	private static File logoAppFolder = null;
	private static File[] listOfFiles = null;
	
	//Program ilk çalistiginda bu static blok ilk json dosyasý sadece bir kereye mahsus read edilir
	static Object jsonMainobj = null;
	private Table tblModels = null;
	
	static
	{
	JSONParser parser = new JSONParser();
		try {
			logoAppFolder =  new File(logoAppModelsPath);
			listOfFiles = logoAppFolder.listFiles();
			InputStream is = AppWindow1.class.getClassLoader().getResourceAsStream("ProductInfo.json");
			String json = null;
			if(is != null)
				json = IOUtils.toString(is);
			jsonMainobj = parser.parse(json);//new FileReader(new File("res\\ProductInfo.json")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlLogoapp = new Shell();
		shlLogoapp.setImage(SWTResourceManager.getImage("C:\\LogoApp\\ButtonIcons\\Logo.png"));
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
		mapControlModel = new HashMap<TableItem,CoreTemplate>();
		
	    
	    tblModels = new Table(shlLogoapp, SWT.BORDER | SWT.CHECK);
	    tblModels.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		tblModels.setBounds(10, 280, 252, 356);
		formToolkit.adapt(tblModels);
		formToolkit.paintBordersFor(tblModels);
		tblModels.setHeaderVisible(false);
		
		
	 // Expandbar ve ExpandItem oluþturan kod bloðu
	    expandBar = new ExpandBar(shlLogoapp, SWT.V_SCROLL);
	 	expandBar.setSpacing(6);
	 	expandBar.setBounds(269, 280, 1036, 1500);		
		expandBar.getVerticalBar().setIncrement(16);
		expandBar.setBounds(269, 280, 1036, 356);

		
	    
	    // Not : Ürünlerin iþlenmemiþ hallerinin ölçeklenmiþ halini program açýlýrken scale edip bir yere kaydederek guc harcamak yerine
	    // C:/LogoApp/ScaledImages altýna direk küçük hale scale edilmis halini koymak is yukunu hafifletir.
	    

	    tblModels.addListener(SWT.Selection, new Listener(){
	    	public void handleEvent(Event event){
		    	boolean control = false;
		    	TableItem tableItem = (TableItem) event.item;
		    	
		    	//if(event.detail == SWT.CHECK)
		    	if(tableItem.getChecked())	
		    		control = true;
		    	
		    	
		    	CoreTemplate currentCoreTemplate = mapControlModel.get(event.item);
		    	int checkSize = currentCoreTemplate.getProductUIList().size();
		    	ProductUI tempProductUI = null;
		    	if(control){
		    		for (int i = 0; i < checkSize; i++) {
		    			tempProductUI = currentCoreTemplate.getProductUIList().get(i);
		    			tempProductUI.getCheckIsApply().setSelection(true);
		    			tempProductUI.getCheckIsApply().setEnabled(true);
		    		}
		    	}else{
		    		for (int i = 0; i < checkSize; i++) {
		    			tempProductUI = currentCoreTemplate.getProductUIList().get(i);
		    			tempProductUI.getCheckIsApply().setSelection(false);
		    			tempProductUI.getCheckIsApply().setEnabled(false);
		    		}
		    	}
	    	}
	    });

		
		
		Button btnBirLogoSeiniz = formToolkit.createButton(shlLogoapp, "Bir Logo Se\u00E7iniz", SWT.NONE);
		btnBirLogoSeiniz.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					chooseLogo();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnBirLogoSeiniz.setImage(SWTResourceManager.getImage("C:\\LogoApp\\ButtonIcons\\fileChooser.png"));
		btnBirLogoSeiniz.setBounds(10, 36, 162, 35);
		
		txtLogo = formToolkit.createText(shlLogoapp, "New Text", SWT.BORDER | SWT.WRAP | SWT.MULTI);
		txtLogo.setText("");
		txtLogo.setBounds(211, 28, 145, 55);
		txtLogo.setVisible(false);
		
		logoLabel = formToolkit.createLabel(shlLogoapp, "", SWT.NONE);
		logoLabel.setAlignment(SWT.CENTER);
		logoLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		
		
		logoLabel.setBounds(269, 43, 200, 200);
		
		labelTemplate1 = new Label(shlLogoapp, SWT.BORDER);
		labelTemplate1.setBounds(487, 43, 200, 200);
		formToolkit.adapt(labelTemplate1, true, true);
		
		labelTemplate2 = new Label(shlLogoapp, SWT.BORDER);
		labelTemplate2.setBounds(693, 43, 200, 200);
		formToolkit.adapt(labelTemplate2, true, true);
		
		
		labelTemplate3 = new Label(shlLogoapp, SWT.BORDER);
		labelTemplate3.setBounds(899, 43, 200, 200);
		formToolkit.adapt(labelTemplate3, true, true);
		
		labelTemplate4 = new Label(shlLogoapp, SWT.BORDER);
		labelTemplate4.setBounds(1105, 43, 200, 200);
		formToolkit.adapt(labelTemplate4, true, true);
		
		
		Button btnApplyLogo = new Button(shlLogoapp, SWT.NONE);
		btnApplyLogo.setImage(SWTResourceManager.getImage("C:\\LogoApp\\ButtonIcons\\Apply.png"));
		btnApplyLogo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					applyToLogo();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnApplyLogo.setBounds(1143, 650, 162, 39);
		formToolkit.adapt(btnApplyLogo, true, true);
		btnApplyLogo.setText("Logoyu Uygula");
		
		txtLogoName = new Text(shlLogoapp, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		txtLogoName.setBounds(10, 122, 200, 27);
		formToolkit.adapt(txtLogoName, true, true);
		
		Label lblLogoDizini = new Label(shlLogoapp, SWT.NONE);
		lblLogoDizini.setBounds(9, 129, 75, 20);
		formToolkit.adapt(lblLogoDizini, true, true);
		lblLogoDizini.setText("Logo Dizini:");
		lblLogoDizini.setVisible(false);
		
		Label lblLogoAd = new Label(shlLogoapp, SWT.NONE);
		lblLogoAd.setBounds(10, 96, 75, 20);
		formToolkit.adapt(lblLogoAd, true, true);
		lblLogoAd.setText("Logo Ad\u0131:");
		
		Button btnTemizle = new Button(shlLogoapp, SWT.NONE);
		btnTemizle.setImage(SWTResourceManager.getImage("C:\\LogoApp\\ButtonIcons\\Clean.png"));
		btnTemizle.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				prepareFileHierarchyForScreen();
			
			}
		});
		btnTemizle.setBounds(957, 650, 132, 39);
		formToolkit.adapt(btnTemizle, true, true);
		btnTemizle.setText("Temizle");
		
		
		
		// radio button operations	
		rdBtnSablon1 = new Button(shlLogoapp, SWT.RADIO);
		rdBtnSablon1.setBounds(547, 10, 82, 20);
		formToolkit.adapt(rdBtnSablon1, true, true);
		rdBtnSablon1.setText("\u015Eablon 1");
		
		rdBtnSablon2 = new Button(shlLogoapp, SWT.RADIO);
		rdBtnSablon2.setBounds(754, 10, 82, 20);
		formToolkit.adapt(rdBtnSablon2, true, true);
		rdBtnSablon2.setText("\u015Eablon 2");
		
		rdBtnSablon3 = new Button(shlLogoapp, SWT.RADIO);
		rdBtnSablon3.setBounds(963, 10, 82, 20);
		formToolkit.adapt(rdBtnSablon3, true, true);
		rdBtnSablon3.setText("\u015Eablon 3");
		
		rdBtnSablon4 = new Button(shlLogoapp, SWT.RADIO);
		rdBtnSablon4.setBounds(1166, 10, 82, 20);
		formToolkit.adapt(rdBtnSablon4, true, true);
		rdBtnSablon4.setText("\u015Eablon 4");
		
		
		txtSaveDirectory = new Text(shlLogoapp, SWT.BORDER);
		txtSaveDirectory.setBounds(10, 216, 200, 27);
		formToolkit.adapt(txtSaveDirectory, true, true);
		
		Button btnKaydedilecekYer = new Button(shlLogoapp, SWT.NONE);
		btnKaydedilecekYer.setImage(SWTResourceManager.getImage("C:\\LogoApp\\ButtonIcons\\save.jpg"));
		btnKaydedilecekYer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dlg = new DirectoryDialog(shlLogoapp);
				dlg.setFilterPath("C:/");
				dlg.setText("SWT's DirectoryDialog");
				dlg.setMessage("Select a directory");
				String tmpCreate = dlg.open();
				if(tmpCreate != null){
					txtSaveDirectory.setText(tmpCreate);
				}
			}
		});
		btnKaydedilecekYer.setBounds(10, 175, 162, 35);
		formToolkit.adapt(btnKaydedilecekYer, true, true);
		btnKaydedilecekYer.setText("Kaydedilecek Yer");
		
		btnIsApplyPreviewChk = new Button(shlLogoapp, SWT.CHECK);
		btnIsApplyPreviewChk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(btnIsApplyPreviewChk.getSelection())
					unselectTemplateOptions();
			}
		});
		btnIsApplyPreviewChk.setBounds(284, 10, 169, 20);
		formToolkit.adapt(btnIsApplyPreviewChk, true, true);
		btnIsApplyPreviewChk.setText("\u00D6nizleme yap\u0131ls\u0131n m\u0131?");
		
		
		rdBtnSablon4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				try {
					controlLogoIsEmpty();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				
				boolean isSelected = ((Button)e.getSource()).getSelection();
				
				if(isSelected){
				selectedTemplate = 4;
				try {
					try {
						manageRadioButtons();
						if(btnIsApplyPreviewChk.getSelection())
							preview();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ExecutionException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			}
		});
		
		rdBtnSablon3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				try {
					controlLogoIsEmpty();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				
				boolean isSelected = ((Button)e.getSource()).getSelection();
				
				if(isSelected){
				selectedTemplate = 3;
				try {
					try {
						manageRadioButtons();
						if(btnIsApplyPreviewChk.getSelection())
							preview();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ExecutionException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			}
		});
		
		rdBtnSablon2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				try {
					controlLogoIsEmpty();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				
				boolean isSelected = ((Button)e.getSource()).getSelection();
				
				if(isSelected){
				selectedTemplate = 2;
				try {
					try {
						manageRadioButtons();
						if(btnIsApplyPreviewChk.getSelection())
							preview();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ExecutionException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			}
		});
		
		rdBtnSablon1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				try {
					controlLogoIsEmpty();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				
				boolean isSelected = ((Button)e.getSource()).getSelection();
				
				if(isSelected){
					
				selectedTemplate = 1;
				try {
					try {
						manageRadioButtons();
						if(btnIsApplyPreviewChk.getSelection())
							preview();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ExecutionException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			}
		});
		//radio button operations
		
		prepareFileHierarchyForScreen();
	}

	
	private void prepareFileHierarchyForScreen(){
		modelList.clear();
		mapControlModel.clear();
		coreTemplateList.clear();
		unselectTemplateOptions();
		setDefaultImagesToTemplates();
		btnIsApplyPreviewChk.setSelection(false);
		txtLogoName.setText("");
		txtSaveDirectory.setText("");
		
		for(TableItem item: tblModels.getItems()){
			item.dispose();
		}
		for(ExpandItem item: expandBar.getItems()){
			item.setExpanded(false);
			item.dispose();
		}
		expandBar.setBounds(269, 280, 1036, 1500);		
		
		
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

	 	//ExpandItem oluþturma iþlemi
	    Composite composite = null;
		GridLayout gridLayout = null;
		CoreTemplate coreTemplate = null;
		ExpandItem xpndtmNewExpanditem = null; 
		Model currentModel = null;
		TableItem tableItem = null;
		for (int i = 0; i < modelList.size(); i++) {
			currentModel = modelList.get(i);
			
			composite = new Composite(expandBar, SWT.NONE);
			gridLayout = new GridLayout(9, false); //3
			gridLayout.horizontalSpacing = 20;
			composite.setLayout(gridLayout);
			
			//model bazlý logo isleme checkbox kontrolu icin duzenleme
			tableItem = new TableItem(tblModels, SWT.NONE);
			tableItem.setText(currentModel.getModelName());
			tableItem.setChecked(true);
			
			//ExpandBar'a ExpandItem eklenmesi
			xpndtmNewExpanditem = new ExpandItem(expandBar, SWT.NONE,i);
			xpndtmNewExpanditem.setExpanded(false);
			xpndtmNewExpanditem.setText(modelList.get(i).getModelName());
			
			//ExpandItem icin componentleri iceren CoreTemplate nesnesinin olusturulmasi
			coreTemplate = new CoreTemplate(composite,currentModel); // label'larý gride teker teker uygun sýrada dolduracaktýr
			
			mapControlModel.put(tableItem, coreTemplate);
			
			coreTemplateList.add(coreTemplate);
			
			showProducts(coreTemplate); // Siradaki model altýndaki urunlerin ekrana basilmasi icin tetikleniyor
			
			xpndtmNewExpanditem.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);//(coreTemplate.getAllRegionList().size() / 3) + 1) * 300);
			xpndtmNewExpanditem.setControl(composite);
		}
		expandBar.setBounds(269, 280, 1036, 356);
	}
	
	
	private void setDefaultImagesToTemplates(){
		logoLabel.setImage(null);
		labelTemplate1.setImage(SWTResourceManager.getImage("C:\\LogoApp\\Templates\\Template1.png"));
		labelTemplate2.setImage(SWTResourceManager.getImage("C:\\LogoApp\\Templates\\Template2.png"));
		labelTemplate3.setImage(SWTResourceManager.getImage("C:\\LogoApp\\Templates\\Template3.png"));
		labelTemplate4.setImage(SWTResourceManager.getImage("C:\\LogoApp\\Templates\\Template4.png"));
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
	
	public void chooseLogo() throws IOException{
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
        	unselectTemplateOptions();
			//clearAllTemplates();
        	txtLogo.setText(logoPath);
            LogoName = logoPath.split("\\\\");
            LogoNameStr = LogoName[LogoName.length-1];
            LogoNameStr = LogoNameStr.substring(0, LogoNameStr.indexOf('.'));
            txtLogoName.setText(LogoNameStr);
        	processLogo();
        	processTemplates();
        }
	}
	
	private void unselectTemplateOptions(){
		rdBtnSablon1.setSelection(false);
		rdBtnSablon2.setSelection(false);
		rdBtnSablon3.setSelection(false);
		rdBtnSablon4.setSelection(false);
	}
	
	private void processTemplates() throws IOException {// Ek gelistirme ile istedikleri urunun sablon olmasý istenirse bu metoda 'modelName' ve 'productName' parametre olarak gelmesi yeterli olur
		String modelName = "BaseTemplate"; 
		String modelNameStr = modelName.replace(" ", "_");
		String productName = "BaseTemplate"; 
		List<Integer> templateCoordinates = null;
		List<Integer> calculatedPosition = new ArrayList<Integer>();

		File f = null;
		BufferedImage productBuffer = ImageIO.read(f = new File(baseTemplate));
		try{

			for(int i = 1; i < 5; i++){
				calculatedPosition.clear();

				BufferedImage originalProductBuffer = copyImage(productBuffer);
				templateCoordinates = getProductTemplateCoordinates(i, templateCoordinates, modelNameStr, productName);
				int templateHeight = templateCoordinates.get(1) - templateCoordinates.get(4); // Bottom_LeftY - TopLeftY
				int templateWidth = templateCoordinates.get(2) - templateCoordinates.get(0);

				Dimension logoSize = new Dimension(logoOriginalForTemplate.getWidth(), logoOriginalForTemplate.getHeight());
				Dimension templateSize = new Dimension(templateWidth, templateHeight);			
				Dimension resizedLogoDimension = getScaledDimension(logoSize,templateSize); // Logo botunu template ve orjinal logo boyutuna göre belirler.
				
				calculatedPosition = calculateLogoStartingPosition(calculatedPosition,resizedLogoDimension,templateCoordinates.get(5),templateCoordinates.get(4),templateCoordinates.get(6)); // Logo her zaman þablonun üst sýnýrýna dayalý olacak (Özcan Bey Öyle olmasýný istiyor.) 
				
				BufferedImage resizedLogo = new BufferedImage(resizedLogoDimension.width, resizedLogoDimension.height, logoOriginalForTemplate.getType());
				Graphics2D graphic = resizedLogo.createGraphics();
				graphic.drawImage(logoOriginalForTemplate, 0, 0, resizedLogoDimension.width, resizedLogoDimension.height, null);
				graphic.dispose();
			
				BufferedImage productWithTemplate = drawForTemplate(originalProductBuffer, resizedLogo, changedTemplatesPath+"\\Template"+i, calculatedPosition.get(0), calculatedPosition.get(1));
				
				if(i == 1){
					//tempBufferedImage = ImageIO.read(new File(changedTemplatesPath+"\\Template"+i+"\\"+txtLogoName.getText()+getFileName(productPath)+".png"));
					labelTemplate1.setImage(scaleToImage(productWithTemplate, 200, 200, changedTemplatesPath+"\\Template"+i+"\\"+txtLogoName.getText()+getFileName(baseTemplate)+"__"+i+".png"));
				}else if(i == 2){
					//tempBufferedImage = ImageIO.read(new File(changedTemplatesPath+"\\Template"+i+"\\"+txtLogoName.getText()+getFileName(productPath)+".png"));
					labelTemplate2.setImage(scaleToImage(productWithTemplate, 200, 200, changedTemplatesPath+"\\Template"+i+"\\"+txtLogoName.getText()+getFileName(baseTemplate)+"__"+i+".png"));
				}else if(i == 3){
					//tempBufferedImage = ImageIO.read(new File(changedTemplatesPath+"\\Template"+i+"\\"+txtLogoName.getText()+getFileName(productPath)+".png"));
					labelTemplate3.setImage(scaleToImage(productWithTemplate, 200, 200, changedTemplatesPath+"\\Template"+i+"\\"+txtLogoName.getText()+getFileName(baseTemplate)+"__"+i+".png"));
				}else if(i == 4){
					//tempBufferedImage = ImageIO.read(new File(changedTemplatesPath+"\\Template"+i+"\\"+txtLogoName.getText()+getFileName(productPath)+".png"));
					labelTemplate4.setImage(scaleToImage(productWithTemplate, 200, 200, changedTemplatesPath+"\\Template"+i+"\\"+txtLogoName.getText()+getFileName(baseTemplate)+"__"+i+".png"));
				}
			}
				
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static BufferedImage copyImage(BufferedImage source){
	    BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
	    Graphics g = b.getGraphics();
	    g.drawImage(source, 0, 0, null);
	    g.dispose();
	    return b;
	}

	public void processLogo(){
		//BufferedImage choosenLogo = null;
		String createPath = "C:\\LogoApp\\SmallLogos\\"+getFileName(logoPath)+"-sm.png";
		List<Integer> calculatedPosition = new ArrayList();
		try {
			//choosenLogo = ImageIO.read(new File(logoPath));
			logoOriginalForTemplate = ImageIO.read(new File(logoPath));
			Dimension logoSize = new Dimension(logoOriginalForTemplate.getWidth(), logoOriginalForTemplate.getHeight());
			Dimension templateSize = new Dimension(logoLabel.getBounds().width, logoLabel.getBounds().height);			
			Dimension resizedLogoDimension = getScaledDimension(logoSize,templateSize);
			
			calculatedPosition = calculateLogoStartingPosition(calculatedPosition,resizedLogoDimension,
					logoLabel.getBounds().y,logoLabel.getBounds().x,logoLabel.getBounds().x+logoLabel.getBounds().width);
			
			
			scaleToImage(logoOriginalForTemplate, (int)resizedLogoDimension.getWidth(), (int)resizedLogoDimension.getHeight(), createPath); 
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
	
	// RadioButtonlar Group içerisine alýndý bu methoda gerek kalmadý.
	private void manageRadioButtons() throws IOException, InterruptedException, ExecutionException {
		switch (selectedTemplate) {
		case 1:
			rdBtnSablon1.setSelection(true);
			rdBtnSablon2.setSelection(false);
			rdBtnSablon3.setSelection(false);
			rdBtnSablon4.setSelection(false);
			break;
		case 2:
			rdBtnSablon1.setSelection(false);
			rdBtnSablon2.setSelection(true);
			rdBtnSablon3.setSelection(false);
			rdBtnSablon4.setSelection(false);
			break;
		case 3:
			rdBtnSablon1.setSelection(false);
			rdBtnSablon2.setSelection(false);
			rdBtnSablon3.setSelection(true);
			rdBtnSablon4.setSelection(false);
			break;
		case 4:
			rdBtnSablon1.setSelection(false);
			rdBtnSablon2.setSelection(false);
			rdBtnSablon3.setSelection(false);
			rdBtnSablon4.setSelection(true);
			break;
		default:
			break;
		}
	}
	private void applyToLogo() throws Exception{
		MessageBox messageBox = null;
		if(!logoPath.equals("") && !(rdBtnSablon1.getSelection() == false && rdBtnSablon2.getSelection() == false && rdBtnSablon3.getSelection() == false && rdBtnSablon4.getSelection() == false)){
			
			String tmpCreate = txtSaveDirectory.getText();
			String createPath = "";
			if(tmpCreate != null && !tmpCreate.equals("")){
				createPath = tmpCreate;
			}else{
			    messageBox = new MessageBox(shlLogoapp, SWT.ERROR);
				messageBox.setText("HATA");
				messageBox.setMessage("Kaydedilecek dizini belirlemelisiniz!");
				messageBox.open();
				throw new Exception("Kaydedilecek yer secilirken hata!!!");
			}
			System.gc();
			// her ürün için ayný logoyu tekrar tekrar okumamak için for un dýþýna alýndý.
			logoOriginal = ImageIO.read(new File(logoPath)); 
			if(!createPath.equals("")){
				File parentDir = new File(createPath+"\\Parents");//Parent'lari kaydetmek icin
				if(!parentDir.exists())
					parentDir.mkdir();
				Model tempModel = null;
				List<Product> productList = null;
				CoreTemplate coreTemplate = null;
				List<ProductUI> productUIList = null;
				int coreTemplateSize = coreTemplateList.size();
//				ExecutorService executor = Executors.newCachedThreadPool();
				ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
				List<Integer> calculatedPosition = new ArrayList();
				List<Integer> productTemplateCoodinates = new ArrayList();
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
							
							calculatedPosition.clear();
							productTemplateCoodinates.clear();
							String productFullPath = logoAppModelsPath +"\\"+tempProductUI.getModelName()+"\\"+tempProductUI.getProductName();
							calculatedPosition = scaleLogoForOriginalProductForTemplate(productFullPath, getFileName(logoPath), selectedTemplate,calculatedPosition,productTemplateCoodinates);  
							
							
							executor.execute(new ProcessImage(productFullPath, scaledLogoPath+"\\"+getFileName(logoPath)+".png", createPath,
									calculatedPosition.get(0), calculatedPosition.get(1),txtLogoName.getText(),getFileName(productFullPath),resizedLogo,tempProductUI.getRadioParent().getSelection()));
							
							
						}
						
						if(j % 3 == 0){
					    	  System.out.println("Run GC:");
					    	  System.gc();
					      }

					}
				}
				
				executor.shutdown();
				
				try {
					executor.awaitTermination(120, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					
				}
				System.out.println("Run GC:");
				System.gc();
				messageBox = new MessageBox(shlLogoapp, SWT.ICON_WORKING);
				messageBox.setText("BASARILI ISLEM");
				messageBox.setMessage("ISLEM BASARILI, DIZINI KONTROL EDINIZ!");
				messageBox.open();
				//showAllProducts(createPath); preview'da kullanýlacak.
			}
		}else{
			 messageBox = new MessageBox(shlLogoapp, SWT.ERROR);
			 messageBox.setText("HATA");
			 messageBox.setMessage("Logo seçmelisiniz ve bir sablon belirlemelisiniz!");
			 messageBox.open();
		}
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
	
//	public static BufferedImage rotate(BufferedImage image, double angle) {
//	   
//		AffineTransform at = new AffineTransform();
//
//        at.translate(image.getWidth() / 2, image.getHeight() / 2);
//        at.rotate(angle);
//
//        // 1. translate the object so that you rotate it around the 
//        //    center (easier :))
////        at.translate(-image.getWidth()/2, -image.getHeight()/2);
//
//        // draw the image
//        Graphics2D g2d = (Graphics2D) image.getGraphics();
//        g2d.drawImage(image, at, null);
//        return image;
//        
//	}


	private static GraphicsConfiguration getDefaultConfiguration() {
	    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    GraphicsDevice gd = ge.getDefaultScreenDevice();
	    return gd.getDefaultConfiguration();
	}
	
	private void controlLogoIsEmpty() throws Exception{
		if(txtLogoName.getText().equals("") || logoPath == null){
			 MessageBox messageBox = new MessageBox(shlLogoapp, SWT.ERROR);
			 messageBox.setText("HATA");
			 messageBox.setMessage("Logo seçmelisiniz!");
			 messageBox.open();
			 rdBtnSablon1.setSelection(false);
			 rdBtnSablon2.setSelection(false);
			 rdBtnSablon3.setSelection(false);
			 rdBtnSablon4.setSelection(false);
			 
			 throw new Exception("Logo secmelisiniz");
		}
		
	}
	
	private void preview() throws Exception{
		
		logoOriginal = ImageIO.read(new File(logoPath)); 
		
		Model tempModel = null;
		List<Product> productList = null;
		CoreTemplate coreTemplate = null;
		List<ProductUI> productUIList = null;
		int coreTemplateSize = coreTemplateList.size();
		previewBuffer = new HashMap<String, BufferedImage>();
		ThreadPoolExecutor call = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
		Set<Future<HashMap<String, BufferedImage>>> set = new HashSet<Future<HashMap<String, BufferedImage>>>();
		HashMap<String, BufferedImage> imageMap = new HashMap<String, BufferedImage>();
		List<Integer> calculatedPosition = new ArrayList();
		List<Integer> productTemplateCoodinates = new ArrayList();
		for (int i = 0; i < coreTemplateSize; i++) {
			coreTemplate = coreTemplateList.get(i);
			tempModel = coreTemplate.getModel();
			productList = tempModel.getProductList();
			productUIList = coreTemplate.getProductUIList();
			ProductUI tempProductUI = null;
			int productUIListSize = productUIList.size();

			
			for(int j = 0; j < productUIListSize;j++){
				tempProductUI = productUIList.get(j);
				calculatedPosition.clear();
				productTemplateCoodinates.clear();
				if(tempProductUI.getCheckIsApply().getSelection()){
					String productFullPath = logoAppModelsPath +"\\"+tempProductUI.getModelName()+"\\"+tempProductUI.getProductName();
					calculatedPosition = scaleLogoForOriginalProductForTemplate(productFullPath, getFileName(logoPath), selectedTemplate,calculatedPosition,productTemplateCoodinates);  
					
					Callable<HashMap<String, BufferedImage>> callable = new ProcessImagePreview(productFullPath, scaledLogoPath+"\\"+getFileName(logoPath)+".png", previewPath+"\\"+tempProductUI.getModelName(),
							calculatedPosition.get(0), calculatedPosition.get(1),txtLogoName.getText(),getFileName(productFullPath),resizedLogo,tempProductUI.getRadioParent().getSelection(),imageMap);
					
					Future<HashMap<String, BufferedImage>> future = call.submit(callable);
				    set.add(future);
				}
			}
			System.gc();
			
		}
		call.shutdown();
		
		try {
			call.awaitTermination(60, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			System.out.println("preview awaitTermination"+e.getMessage());
		}
		for (Future<HashMap<String, BufferedImage>> future : set) {
			try {
//					if(future.get() == null)
//						future.wait();
				previewBuffer.putAll(future.get());					
			} catch (Exception e) {
				System.out.println("preview future.get error"+e.getMessage());
				System.out.println(future+" futureeee");
				System.out.println(future.get()+" future getttttt");
				e.printStackTrace();
			}
		}
		

		showAllProducts(previewPath,previewBuffer);
		
		System.gc();
	}
	
	
	private List<Integer> scaleLogoForOriginalProductForTemplate(String productOriginalPath, String logoNameForPath,int selectedTemplate, List<Integer> calculatedPosition, List<Integer> productTemplateCoodinates){ 
		
		 
		try {			
		BufferedImage productOriginal = null;		
		//productOriginalPath: C:\LogoApp\Models\Gildan 18500 Unisex Hoodie\18500xB00.jpg
		//String replacedProductPath = productOriginalPath.replace('\\', "\\");
		String[] paths = productOriginalPath.split("\\\\");
		String modelName = paths[paths.length-2].replace(" ", "_");
		String productName = paths[paths.length-1];
		productName = productName.substring(0, productName.indexOf('.'));
		
		//logoOriginal = ImageIO.read(new File(logoOriginalPath));// Her seferinde Ayný Logoyu okuyoruz her seferinde 1saniyeye yakýn ayný logo olduðu için gerek yok. 
		
		//productTemplateCoodinates Seçilen þablona göre ilgili ürünün o þablona ait koordinatlarýný dönecek
		// productTemplateCoodinates [Bottom_LeftX , Bottom_LeftY , Bottom_RightX , Bottom_RightY , TopLeftX , TopLeftY , TopRightX , TopRightY]
		System.out.println("productName:"+productName+" modelName:"+modelName + " selectedTemplate:"+selectedTemplate);
		productTemplateCoodinates = getProductTemplateCoordinates(selectedTemplate,productTemplateCoodinates,modelName,productName);
		
		int templateHeight = productTemplateCoodinates.get(1) - productTemplateCoodinates.get(5); // Bottom_LeftY - TopLeftY
		int templateWidth = productTemplateCoodinates.get(2) - productTemplateCoodinates.get(0); //Bottom_rightX - BottomWidthX
		
		Dimension logoSize = new Dimension(logoOriginal.getWidth(), logoOriginal.getHeight());
		Dimension templateSize = new Dimension(templateWidth, templateHeight);			
		Dimension resizedLogoDimension = getScaledDimension(logoSize,templateSize); // Logo botunu template ve orjinal logo boyutuna göre belirler.
		
		calculatedPosition = calculateLogoStartingPosition(calculatedPosition,resizedLogoDimension,productTemplateCoodinates.get(5),productTemplateCoodinates.get(4),productTemplateCoodinates.get(6)); // Logo her zaman þablonun üst sýnýrýna dayalý olacak (Özcan Bey Öyle olmasýný istiyor.) 
		
		resizedLogo = new BufferedImage(resizedLogoDimension.width, resizedLogoDimension.height, logoOriginal.getType());
		
		if(!productTemplateCoodinates.get(5).equals(productTemplateCoodinates.get(7))){
			BufferedImage tempLogoOriginal = null ;
			tempLogoOriginal = logoOriginal;
			double angle = 0 ;
			
				
				
				if(productName.equals("8880xNP00")){
					angle= -0.17;
					
					if(selectedTemplate==4){
						calculatedPosition.set(1, calculatedPosition.get(1)-62);
					}else
				    calculatedPosition.set(1, calculatedPosition.get(1)-72);
				}else if(productName.equals("8880xNY00")){
				     angle= +0.11;     
				     calculatedPosition.set(0, calculatedPosition.get(0)-51);

				}else if((productName.equals("S190TCxHF") 
				      || productName.equals("S190TCxHR")||productName.equals("S190TCxHU"))){
				     angle= +0.03;
				}
				
				Graphics2D graphic = resizedLogo.createGraphics();
				graphic.drawImage(tempLogoOriginal, 0, 0, resizedLogoDimension.width, resizedLogoDimension.height, null);
				graphic.dispose();
				
				resizedLogo = rotate(resizedLogo,angle);

			
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
	
	public double getAngle(Point p1,Point p2) {
		double xDiff = p2.x - p1.x;
        double yDiff = p2.y - p1.y;
        return Math.toDegrees(Math.atan2(yDiff, xDiff));
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
	
	
	
	public BufferedImage drawForTemplate(BufferedImage productBuff,BufferedImage logoBuff,String createPath,int coordinatX,int coordinatY){

		Graphics2D graphicProduct = (Graphics2D) productBuff.getGraphics();
		graphicProduct.drawImage(logoBuff, coordinatX, coordinatY, null);
		graphicProduct.dispose();
		
		//ImageIO.write(productBuff, "png", new File(createPath+"\\"+txtLogoName.getText()+getFileName(productPath)+".png"));
		return productBuff;
		
	}
	
	private void showAllProducts(String parentPath, HashMap<String, BufferedImage> previewBuffer) throws InterruptedException, ExecutionException{
		Model model = null;
		CoreTemplate coreTemplate = null;
		List<ProductUI> tempProductUIList = null;
		int coreTemplateSize = coreTemplateList.size();
		labelImage = new HashMap<String, Image>();
//		ExecutorService call = Executors.newFixedThreadPool(100);
//		ExecutorService call = Executors.newCachedThreadPool();
		ThreadPoolExecutor call = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
		Set<Future<HashMap<String, Image>>> imageSet = new HashSet<Future<HashMap<String, Image>>>();
		HashMap<String, Image> imageMap = new HashMap<String, Image>();
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
			    
			    	Callable<HashMap<String, Image>> callable = new WriteImagePreview(previewBuffer.get(productName.substring(0, productName.indexOf("."))), 180, 220, scaledAndProgressedPath+"\\"+productNameStr,productName.substring(0, productName.indexOf(".")),display,imageMap);					
					Future<HashMap<String, Image>> future = call.submit(callable);
					imageSet.add(future);
		    	}
		    }
			
		}
		call.shutdown();

		try {
			call.awaitTermination(60, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			
		}
		for (Future<HashMap<String, Image>> future : imageSet) {
			try {
//				if(future == null)
//					System.out.println("null future showAllProducts"+future);
//				if(future.get() == null)
//					System.out.println("null future.get() showAllProducts"+future);
				labelImage.putAll(future.get());				
			} catch (Exception e) {
				System.out.println("showAllProducts future.get"+e.getMessage());
			}
		}

		setImagesToLabel();
	}
	
	private void setImagesToLabel() {
		
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
			    	try {
			    		String productName = productUI.getProductName();
			    		
			    		tempProductUIList.get(x).getLblProductImg().setImage(labelImage.get(productName.substring(0, productName.indexOf("."))));	    	
						
					} catch (Exception e) {
						e.printStackTrace();
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
	
	
	private void clearAllTemplates(){
		
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
				productUI.getCheckIsApply().setSelection(true);
				productUI.getRadioParent().setSelection(false);
			}
		}
		
	}
}
