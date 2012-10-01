package ca.umontreal.ift2905.carbonevert.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "products_units")
public class ProductUnitData extends AbstractData {
	@DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
	private ProductData product;

	@DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
	private UnitData unit;

	@DatabaseField
	double carbonRatio;

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

	public double getCarbonRatio() {
		return carbonRatio;
	}

	public void setCarbonRatio(final double carbonRatio) {
		this.carbonRatio = carbonRatio;
	}
	
	public String toString() {
		return unit.toString();
	}
}
