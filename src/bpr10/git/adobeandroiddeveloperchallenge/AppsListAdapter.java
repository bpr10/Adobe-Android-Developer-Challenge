package bpr10.git.adobeandroiddeveloperchallenge;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AppsListAdapter extends BaseAdapter {
	private Cursor cursor;
	private Context context;
	private String optionalColumn;

	public AppsListAdapter(Context context, Cursor cursor, String optionalColumn) {
		this.context = context;
		this.cursor = cursor;
		this.optionalColumn = optionalColumn;
	}

	private class ViewHolder {
		ImageView logo;
		TextView appName, appText ,appDesc;

		public ViewHolder(View v) {
			appName = (TextView) v.findViewById(R.id.app_name_label);
			appText = (TextView) v.findViewById(R.id.app_rest_details_label);
			logo = (ImageView) v.findViewById(R.id.listitem_app_logo);
			appDesc = (TextView)v.findViewById(R.id.app_desc_label);
		}
	}

	@Override
	public int getCount() {
		return cursor.getCount() ;
	}

	@Override
	public Object getItem(int position) {
		return cursor.moveToPosition(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.list_item, parent, false);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		try {

			cursor.moveToPosition(position);
			Picasso.with(context)
					.load(cursor.getString(cursor
							.getColumnIndex(DbHelper.IMAGEURL)))
					.into(viewHolder.logo);
			viewHolder.appName.setText(cursor.getString(cursor
					.getColumnIndex(DbHelper.APP_NAME)));
			if (optionalColumn != null) {

				viewHolder.appText.setText(cursor.getString(cursor
						.getColumnIndex(optionalColumn)));

			} else {
				viewHolder.appText.setText(cursor.getString(cursor
						.getColumnIndex(DbHelper.TYPE)));
			}
			if(cursor.getString(cursor
					.getColumnIndex(DbHelper.DESCRIPTION)).length()>60){
				viewHolder.appDesc.setText(cursor.getString(cursor
						.getColumnIndex(DbHelper.DESCRIPTION)).substring(0, 57)+"...");
			}else{
				viewHolder.appDesc.setText(cursor.getString(cursor
						.getColumnIndex(DbHelper.DESCRIPTION)));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return convertView;
	}

	public Cursor getCursor() {
		return cursor;
	}
}