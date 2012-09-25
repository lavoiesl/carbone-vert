package ca.umontreal.ift2905.carbonevert;

import java.util.List;

import ca.umontreal.ift2905.carbonevert.model.ActivityData;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ActivityArrayAdapter extends ArrayAdapter<ActivityData> {
	private final List<ActivityData> list;
	  private final Activity context;

	  public ActivityArrayAdapter(Activity context, List<ActivityData> list) {
	    super(context, R.layout.list_layout);
	    this.context = context;
	    this.list = list;
	  }

	  static class ViewHolder {
	    protected TextView text;
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    View view = null;
	    if (convertView == null) {
	      LayoutInflater inflator = context.getLayoutInflater();
	      view = inflator.inflate(R.layout.list_layout, null);
	      final ViewHolder viewHolder = new ViewHolder();
	      viewHolder.text = (TextView) view.findViewById(R.id.list_layout_view);
	      view.setTag(viewHolder);
	    }
	    ViewHolder holder = (ViewHolder) view.getTag();
	    holder.text.setText(list.get(position).getId());
	    return view;
	  }
}
