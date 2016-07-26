package bean;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class CoreTemplate {

	
	private List<ProductUI> productUIList = null;
	private Model model = null;
	private Button isApplyAllOfThem = null;
	
	public CoreTemplate(Composite composite,Model model) {
		this.productUIList = new ArrayList<ProductUI>();
		this.model = model;
		
		// ExpandItem icerisinde hic bir urune logo uygulanmasýn butonu
		this.isApplyAllOfThem = new Button(composite, SWT.NONE);
		this.isApplyAllOfThem.setText("Bu Modele Uygulama");
		GridData gridData = new GridData();
		gridData.horizontalSpan = 9;
		this.isApplyAllOfThem.setLayoutData(gridData);
		this.isApplyAllOfThem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				List<ProductUI> productUIList = getProductUIList();
				int size = productUIList.size();
				ProductUI productUI = null;
				for (int i = 0; i < size; i++) {
					productUI = productUIList.get(i);
					productUI.getCheckIsApply().setSelection(false);
					productUI.getRadioParent().setSelection(false);
				}
			}
		});
		
		// ExpandItem'a urunlerin eklenmesi
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
