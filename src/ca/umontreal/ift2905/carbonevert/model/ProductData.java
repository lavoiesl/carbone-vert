package ca.umontreal.ift2905.carbonevert.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "products")
public class ProductData extends AbstractData {
	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField
	private String name;

	@ForeignCollectionField(eager = false, foreignFieldName = "product")
	private ForeignCollection<ProductUnitData> units;

	@DatabaseField(foreign = true, canBeNull = true)
	private CategoryData category = null;

	public int getId() {
		return id;
	}

	public ForeignCollection<ProductUnitData> getUnits() {
		return units;
	}
	
	public CategoryData getCategory() {
		return category;
	}

	public void setCategory(CategoryData category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return getName();
	}
}
