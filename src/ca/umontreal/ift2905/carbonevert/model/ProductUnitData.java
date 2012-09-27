package ca.umontreal.ift2905.carbonevert.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "products_units")
public class ProductUnitData {
	@DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
	private ProductData product;

	@DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
	private UnitData unit;

	@DatabaseField
	float carbonRatio;

	public ProductData getProduct() {
		return product;
	}

	public void setProduct(final ProductData product) {
		this.product = product;
	}

	public UnitData getUnit() {
		return unit;
	}

	public void setUnit(final UnitData unit) {
		this.unit = unit;
	}

	public float getCarbonRatio() {
		return carbonRatio;
	}

	public void setCarbonRatio(final float carbonRatio) {
		this.carbonRatio = carbonRatio;
	}
}
