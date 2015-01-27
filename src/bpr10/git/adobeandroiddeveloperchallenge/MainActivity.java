package bpr10.git.adobeandroiddeveloperchallenge;

import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class MainActivity extends ActionBarActivity {

	private ListView mlistView;
	private Spinner appsFilter;
	private String[] args = { DbHelper.C_ID, DbHelper.APP_NAME, DbHelper.TYPE,
			DbHelper.DESCRIPTION, DbHelper.IMAGEURL, DbHelper.IN_APP_PURCHASE,
			DbHelper.LAST_UPDATED, DbHelper.PLAYSTORE_URL, DbHelper.RATING };
	SQLiteDatabase dbWrite;
	AppsListAdapter mAdapter;
	protected String tag = getClass().getSimpleName();
	private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mlistView = (ListView) findViewById(R.id.app_listview);
		appsFilter = (Spinner) findViewById(R.id.apps_filter);
		dbWrite = DbHelper.getInstance(getApplicationContext())
				.getWritableDatabase();
		getAppsList();
		SpinerApapter spinerAdapter = new SpinerApapter(this,

		R.layout.dropdown_item, AppController.CATEGORIES);
		appsFilter.setAdapter(spinerAdapter);
		appsFilter.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Cursor cursor;
				position = appsFilter.getSelectedItemPosition();
				switch (position) {

				case 0:
					cursor = dbWrite.query(DbHelper.APP_DETAILS_TABLE, args,
							null, null, null, null, DbHelper.RATING + " DESC");
					updateUI(cursor, DbHelper.RATING);
					break;
				case 1:
					cursor = dbWrite.query(DbHelper.APP_DETAILS_TABLE, args,
							null, null, null, null, DbHelper.IN_APP_PURCHASE
									+ " DESC");
					updateUI(cursor, DbHelper.IN_APP_PURCHASE);
					break;
				default:
					break;
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				Cursor cursor = dbWrite.query(DbHelper.APP_DETAILS_TABLE, args,
						null, null, null, null, DbHelper.RATING + " DESC");
				updateUI(cursor, DbHelper.RATING);
			}
		});
		mlistView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mAdapter.getCursor().moveToPosition(position);
					Intent i = new Intent(MainActivity.this, AppDetails.class);
				i.putExtra(
						DbHelper.C_ID,
						mAdapter.getCursor().getString(
								mAdapter.getCursor().getColumnIndex(
										DbHelper.C_ID)));
				startActivity(i);
			}
		});
	}

	private void getAppsList() {
		pDialog = new ProgressDialog(MainActivity.this);
		pDialog.setMessage("Please wait");
		pDialog.setCancelable(false);
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.show();
		StringRequest appsrequest = new StringRequest(
				"http://adobe.0x10.info/api/products?type=json",
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						try {
							JSONArray appsArray = new JSONArray(response);
							dbWrite.delete(DbHelper.APP_DETAILS_TABLE, null,
									null);
							for (int i = 0; i < appsArray.length(); i++) {
								if (appsArray.getJSONObject(i)
										.getString("name").length() != 0) {
									ContentValues cv = new ContentValues();
									cv.put(DbHelper.APP_NAME, appsArray
											.getJSONObject(i).getString("name"));
									cv.put(DbHelper.TYPE, appsArray
											.getJSONObject(i).getString("type"));
									cv.put(DbHelper.PLAYSTORE_URL, appsArray
											.getJSONObject(i).getString("url"));
									cv.put(DbHelper.IMAGEURL, appsArray
											.getJSONObject(i)
											.getString("image"));
									cv.put(DbHelper.RATING, (appsArray
											.getJSONObject(i)
											.getString("rating")));
									cv.put(DbHelper.LAST_UPDATED,
											appsArray.getJSONObject(i)
													.getString("last updated"));
									cv.put(DbHelper.IN_APP_PURCHASE,
											appsArray
													.getJSONObject(i)
													.getString("inapp-purchase"));
									cv.put(DbHelper.DESCRIPTION,
											appsArray.getJSONObject(i)
													.getString("description"));
									dbWrite.insert(DbHelper.APP_DETAILS_TABLE,
											null, cv);
									Log.d(tag, "Inserting data "
											+ appsArray.getJSONObject(i)
													.getString("name"));
								}

							}
							Cursor cursor = dbWrite
									.query(DbHelper.APP_DETAILS_TABLE, args,
											null, null, null, null,
											DbHelper.RATING + " DESC");
							updateUI(cursor, DbHelper.RATING);
						} catch (JSONException e) {
							e.printStackTrace();

							if (pDialog.isShowing()) {
								pDialog.hide();
							}
							Toast.makeText(getApplicationContext(),
									getString(R.string.internal_error),
									Toast.LENGTH_SHORT).show();
						}
						if (pDialog.isShowing()) {
							pDialog.hide();
						}
					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						arg0.printStackTrace();

						if (pDialog.isShowing()) {
							pDialog.hide();
						}
						if(arg0 instanceof NoConnectionError) {
							Toast.makeText(getApplicationContext(),
									getString(R.string.internet_error),
									Toast.LENGTH_SHORT).show();
							  } else{
								  Toast.makeText(getApplicationContext(),
											getString(R.string.internal_error),
											Toast.LENGTH_SHORT).show();
							    }
						

					}
				});
		AppController.getInstance().addToRequestQueue(appsrequest);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void updateUI(Cursor cursor, String optionalColumn) {
		mAdapter = new AppsListAdapter(this, cursor, optionalColumn);
		mlistView.setAdapter(mAdapter);
	}
}
