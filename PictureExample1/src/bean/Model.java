package bean;

import java.util.ArrayList;
import java.util.List;

public class Model {

	private String modelName;
	private String modelFullPath;
	private List<Product> productList;
	private CoreTemplate coreTemplate = null;
	
	public Model() {
		this.modelName = "";
		this.modelFullPath = "";
		this.productList = new ArrayList<Product>();
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

	public CoreTemplate getCoreTemplate() {
		if (coreTemplate == null) 
			return null;
		return coreTemplate;
	}

	public void setCoreTemplate(CoreTemplate coreTemplate) {
		this.coreTemplate = coreTemplate;
	}
	
	
}
