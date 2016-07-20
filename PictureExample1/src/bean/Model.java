package bean;

import java.util.ArrayList;
import java.util.List;

public class Model {

	private String modelName;
	private String modelFullPath;
	private List<Product> productList;
	
	public Model() {
		modelName = "";
		modelFullPath = "";
		productList = new ArrayList<Product>();
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getModelFullPath() {
		return modelFullPath;
	}

	public void setModelFullPath(String modelFullPath) {
		this.modelFullPath = modelFullPath;
	}

	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}
	
	
}
