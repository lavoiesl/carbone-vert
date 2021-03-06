package ca.umontreal.ift2905.carbonevert.model;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "activities")
public class ActivityData extends AbstractData {
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date date;

	@DatabaseField(foreign = true, canBeNull = true, foreignAutoRefresh = true)
	private ProductUnitData unit;

	@DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
	private ProductData product;

	@DatabaseField
	private int quantity;

	@DatabaseField
	private float carbon;

	@DatabaseField
	private String notes;

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public float getCarbon() {
		return carbon;
	}

	public void setCarbon(final float carbon) {
		this.carbon = carbon;
	}

	public ProductUnitData getUnit() {
		return unit;
	}

	public void setUnit(final ProductUnitData unit) {
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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(final int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return product.toString();
	}
}
