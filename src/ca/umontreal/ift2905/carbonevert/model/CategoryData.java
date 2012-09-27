package ca.umontreal.ift2905.carbonevert.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "categories")
public class CategoryData extends AbstractData {
	@DatabaseField(id = true)
	private int id;

	@DatabaseField
	private String name;

	@ForeignCollectionField(eager = false, foreignFieldName = "category")
	private ForeignCollection<ProductData> products;

	@Override
	public String toString() {
		return name;
	}	
	
	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public ForeignCollection<ProductData> getProducts() {
		return products;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setProducts(ForeignCollection<ProductData> products) {
		this.products = products;
	}
}