package ca.umontreal.ift2905.carbonevert.model;

import com.j256.ormlite.field.DatabaseField;

public abstract class AbstractData {

	@DatabaseField(generatedId = true)
	private int id;

	public int getId() {
		return id;
	}

	@Override
	public abstract String toString();
}
