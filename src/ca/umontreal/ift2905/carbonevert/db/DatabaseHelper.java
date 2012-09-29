package ca.umontreal.ift2905.carbonevert.db;

import java.io.IOException;
import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import ca.umontreal.ift2905.carbonevert.R;
import ca.umontreal.ift2905.carbonevert.model.ActivityData;
import ca.umontreal.ift2905.carbonevert.model.CategoryData;
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
	// appropriate for your application
	private static final String DATABASE_NAME = "carbone_vert.db";
	// any time you make changes to your database objects, you may have to
	// increase the database version
	private static final int DATABASE_VERSION = 25;
	private final Context context;
	@SuppressWarnings("rawtypes")
	private final Class[] classes = new Class[] { CategoryData.class,
			UnitData.class, ProductData.class, ProductUnitData.class,
			ActivityData.class };

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
	@Override
	public void onCreate(final SQLiteDatabase db,
			final ConnectionSource connectionSource) {
		createAllTables(db, connectionSource);
		loadFixtures();
	}

	private void loadFixtures() {
		try {
			importCategories();
			importUnits();
			importProducts();
		} catch (final Exception e) {
			Log.e(DatabaseHelper.class.getName(), "Can't load fixtures", e);
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
				} catch (final SQLException e) {
					throw new RuntimeException(e);
				}
			}
		};
		importer.importResource(R.raw.units);
		Log.i(DatabaseHelper.class.getName(), dao.queryForAll().size()
				+ " units");
	}

	private void importCategories() throws IOException, SQLException {
		final Dao<CategoryData, Integer> dao = getDao(CategoryData.class);
		final CsvImporter importer = new CsvImporter(context) {

			@Override
			public void onRow(final String[] header, final String[] row) {
				final CategoryData category = new CategoryData();
				try {
					category.setName(row[0]);
					dao.create(category);
				} catch (final SQLException e) {
					throw new RuntimeException(e);
				}
			}
		};
		importer.importResource(R.raw.categories);
		Log.i(DatabaseHelper.class.getName(), dao.queryForAll().size()
				+ " categories");
	}

	private void importProducts() throws IOException, SQLException {
		final Dao<ProductData, Integer> products = getDao(ProductData.class);
		final Dao<CategoryData, Integer> categories = getDao(CategoryData.class);
		final CsvImporter importer = new CsvImporter(context) {

			@Override
			public void onRow(final String[] header, final String[] row) {
				final ProductData product = new ProductData();
				try {
					product.setName(row[1]);
					if (!row[1].isEmpty()) {
						String categoryName = row[0].trim();
						CategoryData category = categories.queryForEq("name", categoryName).get(0);
						product.setCategory(category);
					}
					products.create(product);
				} catch (final SQLException e) {
					throw new RuntimeException(e);
				}
			}
		};
		importer.importResource(R.raw.products);
		Log.i(DatabaseHelper.class.getName(), products.queryForAll().size()
				+ " products");
	}

	/**
	 * This is called when your application is upgraded and it has a higher
	 * version number. This allows you to adjust the various data to match the
	 * new version number.
	 */
	@Override
	public void onUpgrade(final SQLiteDatabase db,
			final ConnectionSource connectionSource, final int oldVersion,
			final int newVersion) {
		Log.i(DatabaseHelper.class.getName(), "onUpgrade");
		dropAllTables(db, connectionSource);

		// after we drop the old databases, we create the new ones
		onCreate(db, connectionSource);
	}

	@SuppressWarnings("unchecked")
	private void createAllTables(final SQLiteDatabase db,
			final ConnectionSource connectionSource) {
		try {
			for (@SuppressWarnings("rawtypes")
			final Class c : classes) {
				TableUtils.createTable(connectionSource, c);
			}
		} catch (final SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	private void dropAllTables(final SQLiteDatabase db,
			final ConnectionSource connectionSource) {
		try {
			for (@SuppressWarnings("rawtypes")
			final Class c : classes) {
				TableUtils.dropTable(connectionSource, c, true);
			}
			// after we drop the old databases, we create the new ones
		} catch (final SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}

}