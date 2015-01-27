package bpr10.git.adobeandroiddeveloperchallenge;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SpinerApapter extends ArrayAdapter<String> {
	Activity mActivity;

	public SpinerApapter(Activity activity, int txtViewResourceId,
			String[] categories) {
		super(activity, txtViewResourceId, categories);
		this.mActivity = activity;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = mActivity.getLayoutInflater();
			convertView = inflater.inflate(R.layout.dropdown_item, parent,
					false);
		}
		convertView.setBackgroundColor(convertView.getResources().getColor(
				android.R.color.white));
		TextView subSpinner = (TextView) convertView
				.findViewById(R.id.category_value);
		subSpinner.setTextColor(convertView.getResources().getColor(R.color.spinner_text_color));
		subSpinner.setText(AppController.CATEGORIES[position]);
		ImageView dopDownicon = (ImageView) convertView
				.findViewById(R.id.imageView1);
		dopDownicon.setVisibility(View.GONE);
		return convertView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = mActivity.getLayoutInflater();
			convertView = inflater.inflate(R.layout.dropdown_item, parent,
					false);
		}
		TextView subSpinner = (TextView) convertView
				.findViewById(R.id.category_value);

		if (position != 0) {
			subSpinner.setText("Sorted by "
					+ AppController.CATEGORIES[position]);
		} else {
			subSpinner.setText(R.string.default_spinner_value);
		}
		return convertView;

	}
}
