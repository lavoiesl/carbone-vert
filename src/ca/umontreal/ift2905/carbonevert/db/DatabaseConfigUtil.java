package ca.umontreal.ift2905.carbonevert.db;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

public class DatabaseConfigUtil extends OrmLiteConfigUtil {
	public static void main(final String[] args) throws Exception {
		writeConfigFile("ormlite_config.txt");
	}
}