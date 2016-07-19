import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

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

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(1354, 1012);
		shell.setText("SWT Application");
		shell.setLayout(null);

		label = new Label(shell, SWT.NONE);
		label.setBounds(621, 169, 250, 300);

		String sourcePathImg = "C:/test/18500xIG0.jpg";
		String sourcePathLogo = "C:/test/devoicon.png";
		String destPathImg = "C:/test/18500xIG0.png";
		String destPathLogo = "C:/test/devoicon1.png";
		String createPath = "C:/test/18500xIG0xx.png";

		
		drawIt(sourcePathImg, sourcePathLogo, destPathImg, destPathLogo, createPath, 250, 300, 70, 70,oldLogoCoordinates[0],oldLogoCoordinates[1]);
		
		dressToImg(createPath);
		
		
		
		ExpandBar expandBar = new ExpandBar(shell, SWT.V_SCROLL);
		expandBar.setBounds(133, 492, 1072, 428);
		Composite composite = new Composite(expandBar, SWT.NONE);
		composite.setLayout(new GridLayout(5, false));
		
		ExpandItem xpndtmNewExpanditem = new ExpandItem(expandBar, SWT.NONE);
		xpndtmNewExpanditem.setExpanded(true);
		xpndtmNewExpanditem.setText("New ExpandItem");
		
		Image imgx1 = new Image(display,"C:/test/18500xIG0xx1.png");
		Image imgx2 = new Image(display,"C:/test/18500xIG0xx2.png");
		Image imgx3 = new Image(display,"C:/test/18500xIG0xx3.png");
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setImage(imgx1);
		new Label(composite, SWT.NONE);
		
		
		Label lblNewLabel_1 = new Label(composite, SWT.NONE);
		lblNewLabel_1.setImage(imgx2);
		new Label(composite, SWT.NONE);
		
		Label lblNewLabel_2 = new Label(composite, SWT.NONE);
		lblNewLabel_2.setImage(imgx3);
		
		xpndtmNewExpanditem.setControl(composite);
		
		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.setText("New Button");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		xpndtmNewExpanditem.setHeight(350);
		
		Button btnSola = new Button(shell, SWT.NONE);
		btnSola.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				reDraw("C:/test/18500xIG0.png", "C:/test/devoicon1.png", "C:/test/18500xIG0xx.png", oldLogoCoordinates[0]-=3, oldLogoCoordinates[1]);
			}
		});
		btnSola.setBounds(439, 247, 90, 30);
		btnSola.setText("Sola");
		
		Button btnSaga = new Button(shell, SWT.NONE);
		btnSaga.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				reDraw("C:/test/18500xIG0.png", "C:/test/devoicon1.png", "C:/test/18500xIG0xx.png", oldLogoCoordinates[0]+=3, oldLogoCoordinates[1]);
			}
		});
		btnSaga.setBounds(439, 291, 90, 30);
		btnSaga.setText("Saga");
		
		Button btnAsagi = new Button(shell, SWT.NONE);
		btnAsagi.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				reDraw("C:/test/18500xIG0.png", "C:/test/devoicon1.png", "C:/test/18500xIG0xx.png", oldLogoCoordinates[0], oldLogoCoordinates[1]+=3);
			}
		});
		btnAsagi.setBounds(439, 342, 90, 30);
		btnAsagi.setText("Asagi");
		
		Button btnYukari = new Button(shell, SWT.NONE);
		btnYukari.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				reDraw("C:/test/18500xIG0.png", "C:/test/devoicon1.png", "C:/test/18500xIG0xx.png", oldLogoCoordinates[0], oldLogoCoordinates[1]-=3);
			}
		});
		btnYukari.setBounds(439, 398, 90, 30);
		btnYukari.setText("Yukari");
		

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
			   
			   dressToImg(createPath);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	}
	
	public void reDraw(String imgPath,String logoPath,String createPath,int X,int Y){
		BufferedImage image = null;
		BufferedImage logo = null;
		try {
		   image = ImageIO.read(new File(imgPath));
		   logo = ImageIO.read(new File(logoPath));


		   Graphics2D imageGraphic = (Graphics2D) image.getGraphics();
		   imageGraphic.drawImage(logo, X, Y, null);

			   
			ImageIO.write(image, "png", new File(createPath));
			imageGraphic.dispose();
			   
			dressToImg(createPath);
			} 
		catch (IOException e1) {
				e1.printStackTrace();
		}
	}
	
	public void dressToImg(String createPath){
		Image imgx = new Image(display,createPath);
		label.setImage(imgx);
	}
}
