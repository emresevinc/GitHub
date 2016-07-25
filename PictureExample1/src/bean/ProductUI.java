package bean;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class ProductUI {
	
	private String productName;
	private String modelName;
	private Button radioParent; // secili olan parent'tir
	private Button checkIsApply; // secili ise logo uygulanacak degil ise uygulanmayacak
	private Label lblProductImg;
	
	public ProductUI(String productName,String modelName,Composite composite) {
		this.productName = productName;
		this.modelName = modelName;
		this.lblProductImg = new Label(composite, SWT.NONE);
		this.radioParent = new Button(composite, SWT.RADIO);
		this.checkIsApply = new Button(composite, SWT.CHECK);
		this.checkIsApply.setSelection(true);
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public Button getRadioParent() {
		return radioParent;
	}

	public void setRadioParent(Button radioParent) {
		this.radioParent = radioParent;
	}

	public Button getCheckIsApply() {
		return checkIsApply;
	}

	public void setCheckIsApply(Button checkIsApply) {
		this.checkIsApply = checkIsApply;
	}

	public Label getLblProductImg() {
		return lblProductImg;
	}

	public void setLblProductImg(Label lblProductImg) {
		this.lblProductImg = lblProductImg;
	}
	
	
	

}
