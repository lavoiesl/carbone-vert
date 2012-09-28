package ca.umontreal.ift2905.carbonevert.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "categories")
public class CategoryData extends AbstractData {
	@DatabaseField(unique = true)
	private String name;

	@ForeignCollectionField(eager = false, foreignFieldName = "category")
	private ForeignCollection<ProductData> products;

	@Override
	public String toString() {
		return name;
	}

	public ForeignCollection<ProductData> getProducts() {
		return products;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = toCamelCase(name);
	}

	public void setProducts(final ForeignCollection<ProductData> products) {
		this.products = products;
	}
}
