package ca.umontreal.ift2905.carbonevert;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ca.umontreal.ift2905.carbonevert.model.AbstractData;

public class EntityArrayAdapter<T extends AbstractData> extends ArrayAdapter<T> {
	private final List<T> list;
	private final Activity context;

	public EntityArrayAdapter(final Activity context,
			final List<T> list) {
		super(context, R.layout.list_layout, list);
		this.context = context;
		this.list = list;
	}

	class ViewHolder {
		protected TextView text;
		protected T obj;
	}

	@Override
	public void add(final T obj) {
		super.add(obj);
		list.add(obj);
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(final int position, View convertView,
			final ViewGroup parent) {
		final View row = convertView;
		ViewHolder holder = null;
		final T obj = list.get(position);

		if (row == null) {
			final LayoutInflater inflator = context.getLayoutInflater();
			convertView = inflator.inflate(R.layout.list_layout, parent, false);
			holder = new ViewHolder();
			holder.text = (TextView) convertView
					.findViewById(R.id.list_layout_view);
			holder.obj = obj;
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}

		holder.text.setText(obj.toString());
		return convertView;
	}
}
