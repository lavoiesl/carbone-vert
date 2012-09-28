package ca.umontreal.ift2905.carbonevert.model;

import com.j256.ormlite.field.DatabaseField;

public abstract class AbstractData {
	
	@DatabaseField(generatedId = true)
	private int id;

	public int getId() {
		return id;
	}

	public static String toCamelCase(String s){
	   String[] parts = s.split(" ");
	   String camelCaseString = "";
	   for (String part : parts){
	      camelCaseString = camelCaseString + toProperCase(part);
	   }
	   return camelCaseString;
	}

	private static String toProperCase(String s) {
	    return s.substring(0, 1).toUpperCase() +
	               s.substring(1).toLowerCase();
	}

	@Override
	public abstract String toString();
}
