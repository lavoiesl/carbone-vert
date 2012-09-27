package ca.umontreal.ift2905.carbonevert.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "products_units")
public class ProductUnitData {
	@DatabaseField(foreign = true)
	ProductData product = null;

	@DatabaseField(foreign = true)
	UnitData unit = null;

	@DatabaseField
	float carbonRatio;

	public ProductData getProduct() {
		return product;
	}

	public void setProduct(ProductData product) {
		this.product = product;
	}

	public UnitData getUnit() {
		return unit;
	}

	public void setUnit(UnitData unit) {
		this.unit = unit;
	}

	public float getCarbonRatio() {
		return carbonRatio;
	}

	public void setCarbonRatio(float carbonRatio) {
		this.carbonRatio = carbonRatio;
	}
}
