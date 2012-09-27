package ca.umontreal.ift2905.carbonevert.db;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import ca.umontreal.ift2905.carbonevert.R;
import ca.umontreal.ift2905.carbonevert.model.AbstractData;
import ca.umontreal.ift2905.carbonevert.model.ActivityData;
import ca.umontreal.ift2905.carbonevert.model.ProductData;
import ca.umontreal.ift2905.carbonevert.model.ProductUnitData;
import ca.umontreal.ift2905.carbonevert.model.UnitData;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Database helper class used to manage the creation and upgrading of your
 * database. This class also usually provides the DAOs used by the other
 * classes.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	// name of the database file for your application -- change to something
	// appropriate for your app
	private static final String DATABASE_NAME = "carbone_vert.db";
	// any time you make changes to your database objects, you may have to
	// increase the database version
	private static final int DATABASE_VERSION = 13;
	private final Context context;
	@SuppressWarnings("rawtypes")
	private final Class[] classes = new Class[] {
		ProductData.class,
		UnitData.class,
		ProductUnitData.class,
		ActivityData.class
	};

	public DatabaseHelper(final Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION,
				R.raw.ormlite_config);
		this.context = context;
	}

	/**
	 * This is called when the database is first created. Usually you should
	 * call createTable statements here to create the tables that will store
	 * your data.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(final SQLiteDatabase db,
			final ConnectionSource connectionSource) {
		try {
			for (@SuppressWarnings("rawtypes") Class c : classes) {
				TableUtils.createTable(connectionSource, c);
			}
			importUnits();
			importProducts();
		} catch (Exception e) {
			Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
	}

	private void importUnits() throws IOException, SQLException {
		final Dao<UnitData, Integer> dao = getDao(UnitData.class);
		final CsvImporter importer = new CsvImporter(context) {

			@Override
			public void onRow(final String[] header, final String[] row) {
				final UnitData unit = new UnitData();
				try {
					unit.setCode(row[0]);
					unit.setName(row[1]);
					dao.create(unit);
					dao.refresh(unit);
				} catch (Exception e) {
				}
			}
		};
		importer.importResource(R.raw.units);
		Log.i(DatabaseHelper.class.getName(), dao.queryForAll().size() + " units");
	}

	private void importProducts() throws IOException, SQLException {
		final Dao<ProductData, Integer> dao = getDao(ProductData.class);
		final HashMap<String, ProductData> cache = new HashMap<String, ProductData>(); 
		final CsvImporter importer = new CsvImporter(context) {

			@Override
			public void onRow(final String[] header, final String[] row) {
				final ProductData product = new ProductData();
				try {
					product.setName(row[2]);
					if (!row[1].isEmpty()) {
						product.setParent(cache.get(row[1]));
					}
					dao.create(product);
					dao.refresh(product);
					cache.put(row[0], product);
				} catch (SQLException e) {
					Log.i(DatabaseHelper.class.getName(), e.getMessage());
				}
			}
		};
		importer.importResource(R.raw.products);
		Log.i(DatabaseHelper.class.getName(), dao.queryForAll().size() + " products");
	}

	/**
	 * This is called when your application is upgraded and it has a higher
	 * version number. This allows you to adjust the various data to match the
	 * new version number.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onUpgrade(final SQLiteDatabase db,
			final ConnectionSource connectionSource, final int oldVersion,
			final int newVersion) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onUpgrade");
			for (@SuppressWarnings("rawtypes") Class c : classes) {
				TableUtils.dropTable(connectionSource, c, true);
			}
			// after we drop the old databases, we create the new ones
			onCreate(db, connectionSource);
		} catch (final SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}

}