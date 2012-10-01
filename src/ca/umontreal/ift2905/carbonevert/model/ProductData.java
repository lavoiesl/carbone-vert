package ca.umontreal.ift2905.carbonevert.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "products")
public class ProductData extends AbstractData {
	@DatabaseField
	private String name;

	@DatabaseField
	private boolean favorite = false;

	public boolean isFavorite() {
		return favorite;
	}

	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}

	@ForeignCollectionField(eager = true, foreignFieldName = "product")
	private ForeignCollection<ProductUnitData> units;

	@DatabaseField(foreign = true, canBeNull = true, foreignAutoRefresh = true)
	private CategoryData category = null;

	public ForeignCollection<ProductUnitData> getUnits() {
		return units;
	}

	public void addUnit(final UnitData unit, final double carbonRatio) {
		ProductUnitData productUnitData = getUnit(unit);
		if (productUnitData == null) {
			productUnitData = new ProductUnitData();
			productUnitData.setUnit(unit);
			productUnitData.setProduct(this);
			productUnitData.setCarbonRatio(carbonRatio);
			units.add(productUnitData);
		}
		productUnitData.setCarbonRatio(carbonRatio);
	}

	public ProductUnitData getUnit(final UnitData unit) {
		for (final ProductUnitData productUnitData : units) {
			if (productUnitData.getUnit().equals(unit)) {
				return productUnitData;
			}
		}
		return null;
	}

	public void removeUnit(final UnitData unit) {
		final ProductUnitData productUnitData = getUnit(unit);
		if (productUnitData != null) {
			units.remove(productUnitData);
		}
	}

	public CategoryData getCategory() {
		return category;
	}

	public void setCategory(final CategoryData category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = toCamelCase(name);
	}

	@Override
	public String toString() {
		return "" + getName();
	}
}
