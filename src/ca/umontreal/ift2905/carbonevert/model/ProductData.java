package ca.umontreal.ift2905.carbonevert.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

public class ProductData extends AbstractData {
	@DatabaseField
	private String name;

	@ForeignCollectionField(eager = false)
	ForeignCollection<UnitData> units;

	@DatabaseField(foreign = true, canBeNull = true)
	ProductData parent = null;

	@ForeignCollectionField(eager = false, foreignFieldName = "parent")
	ForeignCollection<ProductData> children;

	public ForeignCollection<UnitData> getUnits() {
		return units;
	}

	public void setUnits(final ForeignCollection<UnitData> units) {
		this.units = units;
	}

	public ProductData getParent() {
		return parent;
	}

	public void setParent(final ProductData parent) {
		if (getParent() != null) {
			getParent().children.remove(this);
		}
		if (parent != null) {
			parent.children.add(this);
		}
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
