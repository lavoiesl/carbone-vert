package ca.umontreal.ift2905.carbonevert.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "units")
public class UnitData extends AbstractData {
	@DatabaseField(unique = true)
	private String code;

	@DatabaseField
	private String name;

	@ForeignCollectionField(eager = false, foreignFieldName = "unit")
	ForeignCollection<ProductUnitData> products;

	public ForeignCollection<ProductUnitData> getProducts() {
		return products;
	}

	public String getCode() {
		return code;
	}

	public void setCode(final String code) {
		this.code = code;
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
