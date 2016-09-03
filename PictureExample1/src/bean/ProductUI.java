package bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	private List<String> parentModels = new ArrayList<String>(
		    Arrays.asList("18000xDHG","18500xIB0","185C00xNS","5700xHGB0","2000TxR00","5000BxHP0","5100PxSG0","18000BxAP",
		    			  "18500BxC0","5000xIB00","2200xOR00","18600xR00","2700xSG00","982xHB000","64400xC00","5000LxNB0",
		    			  "64200LxAP","8880xCP00","5V00LxHP0","S190TCxHC","18600FLxR","6680xDH00","6012xNB00","8816xKLL0",
		    			  "8850xMAR0","8881xTRB0"));
	
	public ProductUI(String productName,String modelName,Composite composite) {
		this.productName = productName;
		this.modelName = modelName;
		this.lblProductImg = new Label(composite, SWT.NONE);
		this.radioParent = new Button(composite, SWT.RADIO);
		this.checkIsApply = new Button(composite, SWT.CHECK);
		this.checkIsApply.setSelection(true);
		
		if(parentModels.contains(productName.substring(0,productName.indexOf('.')))){
			this.radioParent.setSelection(true);
		}
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
