package ca.umontreal.ift2905.carbonevert.model;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "activities")
public class Activity {
	
	@DatabaseField(id = true)
	private int id;
	
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date date;
	
}
