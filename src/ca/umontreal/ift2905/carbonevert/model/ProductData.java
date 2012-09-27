package ca.umontreal.ift2905.carbonevert.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "products")
public class ProductData extends AbstractData {
	@DatabaseField
	private String name;

	@ForeignCollectionField(eager = false, foreignFieldName = "product")
	private ForeignCollection<ProductUnitData> units;

	@DatabaseField(foreign = true, canBeNull = true)
	private ProductData parent = null;

	@ForeignCollectionField(eager = false, foreignFieldName = "parent")
	private ForeignCollection<ProductData> children;

	public ForeignCollection<ProductUnitData> getUnits() {
		return units;
	}

	public ProductData getParent() {
		return parent;
	}

	public void setParent(final ProductData parent) {
//		if (getParent() != null) {
//			getParent().getChildren().remove(this);
//		}
//		if (parent != null) {
//			parent.getChildren().add(this);
//		}
		this.parent = parent;
	}

	public ForeignCollection<ProductData> getChildren() {
		return children;
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
