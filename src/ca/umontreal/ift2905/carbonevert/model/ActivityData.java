package ca.umontreal.ift2905.carbonevert.model;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "activities")
public class ActivityData {
	
	@DatabaseField(id = true, generatedId = true)
	private int id;
	
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date date;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getId() {
		return id;
	}
}
