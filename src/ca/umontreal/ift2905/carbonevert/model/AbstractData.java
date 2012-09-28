package ca.umontreal.ift2905.carbonevert.model;

import com.j256.ormlite.field.DatabaseField;

public abstract class AbstractData {
	
	@DatabaseField(generatedId = true)
	private int id;

	public int getId() {
		return id;
	}

	public static String toCamelCase(String s) {
		s = s.trim();
	    String[] parts = s.split(" ");
	    StringBuilder camelCaseString = new StringBuilder(s.length());

	    for (String part : parts) {
	    	if (camelCaseString.length() > 0) {
			    camelCaseString.append(" ");
	    	}
		    camelCaseString.append(part.substring(0, 1).toUpperCase());
		    camelCaseString.append(part.substring(1).toLowerCase());
	    }
	    return camelCaseString.toString();
	}

	@Override
	public abstract String toString();
}
