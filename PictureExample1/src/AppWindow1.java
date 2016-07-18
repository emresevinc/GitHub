import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
//import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.wb.swt.SWTResourceManager;
import javafx.embed.swing.*;

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

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(1246, 780);
		shell.setText("SWT Application");
		shell.setLayout(null);
		
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
			}
		});
		btnNewButton.setBounds(20, 596, 90, 30);
		btnNewButton.setText("New Button");
		
		Label label = new Label(shell, SWT.NONE);
		label.setBounds(621, 169, 250, 300);
		BufferedImage image = null;
		BufferedImage logo = null;
		try {
		   image = ImageIO.read(new File("C:/test/18500xIG0.jpg"));
		   logo = ImageIO.read(new File("C:/test/devoicon.png"));
		   scaleToImage(image, 250, 300, "C:/test/18500xIG0.png");
		   scaleToImage(logo, 70, 70, "C:/test/devoicon1.png");
//		   BufferedImage outputImage = new BufferedImage(250,
//	                300, image.getType());
//		   Graphics2D g2d = outputImage.createGraphics();
//	        g2d.drawImage(image, 0, 0, 250, 300, null);
//	        g2d.dispose();
//	        ImageIO.write(outputImage, "png", new File("C:/test/18500xIG0.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Image imgx = new Image(display,"C:/test/18500xIG0.png");
		label.setImage(imgx);
		
	    Image img = new Image(display,"C:/test/devoicon1.png");//ImageIO.read(getClass().getResourceAsStream("/devoicon.png"));
//	    Image img2 = new Image(display,"C:/test/devoicon.png");
		
		shell.addPaintListener(new PaintListener() {
			
			@Override
			public void paintControl(PaintEvent event) {
				GC gc = event.gc;
				gc.drawImage(img, 600, 282);
//				gc.drawImage(img2, 120, 120);
				
			}
		});

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
}
