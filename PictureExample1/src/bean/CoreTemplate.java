package bean;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class CoreTemplate {

//	private Label region1,region2,region3,region4,region5,region6,region7,region8,
//		region9,region10,region11,region12,region13,region14;
	
	private List<ProductUI> productUIList = null;
	private Model model = null;
	
	public CoreTemplate(Composite composite,Model model) {
		this.productUIList = new ArrayList<ProductUI>();
		this.model = model;
		
		List<Product> productList = model.getProductList();
		int productSize = productList.size();
		Product product = null;
		for (int i = 0; i < productSize; i++) {
			product = productList.get(i);
			productUIList.add(new ProductUI(product.getProductName(), model.getModelName(), composite));
		}
//		region1 = new Label(composite, style);
//		region1.setText("     1");
//		region2 = new Label(composite, style);
//		region3 = new Label(composite, style);
//		region4 = new Label(composite, style);
//		region5 = new Label(composite, style);
//		region6 = new Label(composite, style);
//		region7 = new Label(composite, style);
//		region8 = new Label(composite, style);
//		region8.setText("    8");
//		region9 = new Label(composite, style);
//		region10 = new Label(composite, style);
//		region11 = new Label(composite, style);
//		region12 = new Label(composite, style);
//		region12.setText("    12");
//		region13 = new Label(composite, style);
//		region14 = new Label(composite, style);
	}


//	public Label getRegion1() {
//		return region1;
//	}
//
//	public void setRegion1(Label region1) {
//		this.region1 = region1;
//	}
//
//	public Label getRegion2() {
//		return region2;
//	}
//
//	public void setRegion2(Label region2) {
//		this.region2 = region2;
//	}
//
//	public Label getRegion3() {
//		return region3;
//	}
//
//	public void setRegion3(Label region3) {
//		this.region3 = region3;
//	}
//
//	public Label getRegion4() {
//		return region4;
//	}
//
//	public void setRegion4(Label region4) {
//		this.region4 = region4;
//	}
//
//	public Label getRegion5() {
//		return region5;
//	}
//
//	public void setRegion5(Label region5) {
//		this.region5 = region5;
//	}
//
//	public Label getRegion6() {
//		return region6;
//	}
//
//	public void setRegion6(Label region6) {
//		this.region6 = region6;
//	}
//
//	public Label getRegion7() {
//		return region7;
//	}
//
//	public void setRegion7(Label region7) {
//		this.region7 = region7;
//	}
//
//	public Label getRegion8() {
//		return region8;
//	}
//
//	public void setRegion8(Label region8) {
//		this.region8 = region8;
//	}
//
//	public Label getRegion9() {
//		return region9;
//	}
//
//	public void setRegion9(Label region9) {
//		this.region9 = region9;
//	}
//
//	public Label getRegion10() {
//		return region10;
//	}
//
//	public void setRegion10(Label region10) {
//		this.region10 = region10;
//	}
//
//	public Label getRegion11() {
//		return region11;
//	}
//
//	public void setRegion11(Label region11) {
//		this.region11 = region11;
//	}
//
//	public Label getRegion12() {
//		return region12;
//	}
//
//	public void setRegion12(Label region12) {
//		this.region12 = region12;
//	}
//
//	public Label getRegion13() {
//		return region13;
//	}
//
//	public void setRegion13(Label region13) {
//		this.region13 = region13;
//	}
//
//	public Label getRegion14() {
//		return region14;
//	}
//
//	public void setRegion14(Label region14) {
//		this.region14 = region14;
//	}
	
	public Model getModel() {
		return model;
	}


	public void setModel(Model model) {
		this.model = model;
	}


	public List<ProductUI> getProductUIList(){
//		List<Label> regionList = new ArrayList<Label>();
//		if(region1 != null &&region2 != null &&region3 != null &&region4 != null &&region5 != null &&region6 != null &&region7 != null &&region8 != null &&
//				region9 != null &&region10 != null &&region11 != null &&region12 != null &&region13 != null &&region14 != null ){
//			regionList.add(region1);
//			regionList.add(region2);
//			regionList.add(region3);
//			regionList.add(region4);
//			regionList.add(region5);
//			regionList.add(region6);
//			regionList.add(region7);
//			regionList.add(region8);
//			regionList.add(region9);
//			regionList.add(region10);
//			regionList.add(region11);
//			regionList.add(region12);
//			regionList.add(region13);
//			regionList.add(region14);
//			return regionList;
//		}
		if(productUIList != null)
			return this.productUIList;
		return null;
	}
	
}
