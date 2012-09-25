package ca.umontreal.ift2905.carbonevert.model;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "activities")
public class ActivityData {

	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date date;

	@DatabaseField
	private String name;

	public Date getDate() {
		return date;
	}

	public void setDate(final Date date) {
		this.date = date;
	}

	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static ActivityData testData(String name) {
		ActivityData obj = new ActivityData();
		obj.date = new Date();
		obj.name = name;
		
		return obj;
	}
}
