package ca.umontreal.ift2905.carbonevert.db;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.skife.csv.ReaderCallback;
import org.skife.csv.SimpleReader;

import android.content.Context;
import android.util.Log;
import ca.umontreal.ift2905.carbonevert.R;

public abstract class CsvImporter extends SimpleReader {
	private final Context context;

	public CsvImporter(final Context context) {
		this.context = context;
	}

	private class Callback implements ReaderCallback {
		private int i = 0;
		private String[] header;
		final CsvImporter importer;

		public Callback(final CsvImporter importer) {
			this.importer = importer;
		}

		public void onRow(final String[] row) {
			Log.i("CSV", Arrays.toString(row));
			if (i++ == 0) {
				header = row;
			} else if (row.length == header.length) {
				importer.onRow(header, row);
			} else if (Arrays.toString(row).trim().length() > 0) {
				throw new IndexOutOfBoundsException("CSV is malformed at line " + i);
			}
		}
	}

	public abstract void onRow(final String[] header, final String[] row) ;

	public void importResource(final int resource) throws IOException {
		final InputStream inputStream = context.getResources().openRawResource(resource);
		final Callback callback = new Callback(this);
		parse(inputStream, callback);
		inputStream.close();
	}
}
