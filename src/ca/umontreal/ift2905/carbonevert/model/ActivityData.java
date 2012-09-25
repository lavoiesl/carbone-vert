package ca.umontreal.ift2905.carbonevert.model;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "activities")
public class ActivityData extends AbstractData {

	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date date;

	@DatabaseField(foreign = true)
	private UnitData unit;

	@DatabaseField(foreign = true)
	private ProductData product;

	@DatabaseField
	private float quantity;

	@DatabaseField
	private float carbon;

	public float getCarbon() {
		return carbon;
	}

	public void setCarbon(final float carbon) {
		this.carbon = carbon;
	}

	public UnitData getUnit() {
		return unit;
	}

	public void setUnit(final UnitData unit) {
		this.unit = unit;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(final Date date) {
		this.date = date;
	}

	public ProductData getProduct() {
		return product;
	}

	public void setProduct(final ProductData product) {
		this.product = product;
	}

	public float getQuantity() {
		return quantity;
	}

	public void setQuantity(final float quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return product.toString();
	}
}
