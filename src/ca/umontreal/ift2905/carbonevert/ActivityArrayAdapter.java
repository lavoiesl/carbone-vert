package ca.umontreal.ift2905.carbonevert;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ca.umontreal.ift2905.carbonevert.model.ActivityData;

public class ActivityArrayAdapter extends ArrayAdapter<ActivityData> {
	private final List<ActivityData> list;
	private final Activity context;

	public ActivityArrayAdapter(final Activity context,
			final List<ActivityData> list) {
		super(context, R.layout.list_layout, list);
		this.context = context;
		this.list = list;
	}

	static class ViewHolder {
		protected TextView text;
	}

	@Override
	public void add(final ActivityData obj) {
		super.add(obj);
		list.add(obj);
	}

	@Override
	public View getView(final int position, View convertView,
			final ViewGroup parent) {
		final View row = convertView;
		ViewHolder holder = null;

		if (row == null) {
			final LayoutInflater inflator = context.getLayoutInflater();
			convertView = inflator.inflate(R.layout.list_layout, parent, false);
			holder = new ViewHolder();
			holder.text = (TextView) convertView
					.findViewById(R.id.list_layout_view);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}

		final ActivityData obj = list.get(position);
		holder.text.setText(obj.toString());
		return convertView;
	}
}
