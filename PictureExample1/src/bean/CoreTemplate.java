package bean;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class CoreTemplate {

	
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
	}
	
	public Model getModel() {
		return model;
	}


	public void setModel(Model model) {
		this.model = model;
	}


	public List<ProductUI> getProductUIList(){
		if(productUIList != null)
			return this.productUIList;
		return null;
	}
	
}
