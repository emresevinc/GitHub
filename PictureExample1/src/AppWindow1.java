import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DND;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;

import bean.CoreTemplate;
import bean.Model;
import bean.Product;

import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.graphics.Point;

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
	List<Label> modelList = null;
	

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setMinimumSize(new Point(1920, 1024));
		shell.setSize(1354, 1012);
		shell.setText("SWT Application");
		shell.setLayout(null);

		String sourcePathImg = "C:/test/18500xIG0.jpg";
		String sourcePathLogo = "C:/test/devoicon.png";
		String destPathImg = "C:/test/18500xIG0.png";
		String destPathLogo = "C:/test/devoicon1.png";
		String createPath = "C:/test/18500xIG0xx.png";

		
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
		
		Image imgx1 = new Image(display,"C:/test/18500xIG0xx1.png");
		Image imgx2 = new Image(display,"C:/test/18500xIG0xx2.png");
		Image imgx3 = new Image(display,"C:/test/18500xIG0xx3.png");
		
		
		// Bu kod bloðu model ürün hiyerarþisini saðlýyor ve model ürün bilgisini elde ediyor
		List<Model> modelList = new ArrayList<Model>();
		CoreTemplate coreTemplate = new CoreTemplate(composite, SWT.NONE);
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
	    
	    
	    for(int x = 0; x < modelList.get(0).getProductList().size(); x++){
	    	BufferedImage originalImage = null;
	    	String originalImagePath = modelList.get(0).getProductList().get(x).getProductFullPath();
	    	String scaledImageParentPath = "C:\\LogoApp\\ScaledImages";
	    	String scaledImageName = modelList.get(0).getProductList().get(x).getProductName()+"-scaled.png";
	    	try {
				originalImage = ImageIO.read(new File(originalImagePath));
				coreTemplate.getAllRegionList().get(x).setImage(scaleToImage(originalImage, 250, 300, scaledImageParentPath+"\\"+scaledImageName));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	    	
	    }
	    
	    
//		Label lblProduct1 = new Label(composite, SWT.NONE);
//		lblProduct1.setImage(imgx1);
//		
//		
//		Label lblProduct2 = new Label(composite, SWT.NONE);
//		lblProduct2.setImage(imgx2);
//		
//		Label lblProduct3 = new Label(composite, SWT.NONE);
//		lblProduct3.setText("         3");
//		
//		Label lblProduct4 = new Label(composite, SWT.NONE);
//		lblProduct4.setText("        4");
//		
//		
//		Label lblProduct5 = new Label(composite, SWT.NONE);
//		//formToolkit.adapt(lblProduct5, true, true);
//		lblProduct5.setImage(imgx3);
//		
//		Label lblProduct6 = new Label(composite, SWT.NONE);
//		lblProduct6.setText("          6");
//		
//		Label lblProduct7 = new Label(composite, SWT.NONE);
//		lblProduct7.setText("        7");
//		
//		Label lblProduct8 = new Label(composite, SWT.NONE);
//		lblProduct8.setText("        8");
//		
//		Label lblProduct9 = new Label(composite, SWT.NONE);
//		lblProduct9.setText("        9");
//		
//		Label lblProduct10 = new Label(composite, SWT.NONE);
//		lblProduct10.setText("       10");
//		
//		Label lblProduct11 = new Label(composite, SWT.NONE);
//		lblProduct11.setText("       11");
//		
//		Label lblProduct12 = new Label(composite, SWT.NONE);
//		lblProduct12.setText("      12");
//		
//		Label lblProduct13 = new Label(composite, SWT.NONE);
//		lblProduct13.setText("       13");
//		
//		Label lblProduct14 = new Label(composite, SWT.NONE);
//		lblProduct14.setText("      14");
		
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
		logoLabel.setBounds(690, 37, 115, 115);
		
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
	
	public void drawIt(String sourcePathImg,String sourcePathLogo,String destPathImg,String destPathLogo,String createPath,int widthScaleImg,int heigthScaleImg,int widthScaleLogo,int heigthScaleLogo,int logoCoordinatX,int logoCoordinatY){
		BufferedImage image = null;
		BufferedImage logo = null;
		try {
			   image = ImageIO.read(new File(sourcePathImg));
			   logo = ImageIO.read(new File(sourcePathLogo));
			   scaleToImage(image, widthScaleImg, heigthScaleImg, destPathImg); // Ölçekleme yapýlýyor
			   scaleToImage(logo, widthScaleLogo, heigthScaleLogo, destPathLogo);
			   
			   image = ImageIO.read(new File(destPathImg)); // Ölçeklenmiþ kaynak dosyalara eriþiliyor.
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
        showLogo();
	}
	
	public void showLogo(){
		BufferedImage choosenLogo = null;
		String createPath = "C:\\LogoApp\\SmallLogos\\"+getFileName(logoPath)+"-sm.png";
		try {
			choosenLogo = ImageIO.read(new File(logoPath));
			scaleToImage(choosenLogo, 115, 115, createPath);
			dressToImg(createPath, logoLabel);
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
}
