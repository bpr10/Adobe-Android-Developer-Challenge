package bpr10.git.adobeandroiddeveloperchallenge;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.squareup.picasso.Picasso;

public class AppDetails extends ActionBarActivity {

	Cursor appDetailsCursor;
	TextView appNameLabel, appRatingLabel, appType, appDescription,
			innAppPurchase, lastUpdate;
	ImageView shareIcon, playStoreIcon, smsIcon;
	ImageView appLogo;

	@Override
	protected void onStart() {
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setElevation(0);
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(getResources()
						.getColor(R.color.primary_color)));
		super.onStart();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_details);
		String c_id = getIntent().getStringExtra(DbHelper.C_ID);
		appDetailsCursor = fetchAppDetailsFromDb(c_id);
		appNameLabel = (TextView) findViewById(R.id.app_name_label);
		appRatingLabel = (TextView) findViewById(R.id.app_rating_label);
		appType = (TextView) findViewById(R.id.app_type);
		appDescription = (TextView) findViewById(R.id.description);
		innAppPurchase = (TextView) findViewById(R.id.in_app_purchase);
		lastUpdate = (TextView) findViewById(R.id.last_update_date);
		appLogo = (ImageView) findViewById(R.id.app_logo);
		shareIcon = (ImageView) findViewById(R.id.share_icon);
		playStoreIcon = (ImageView) findViewById(R.id.play_store_icon);
		smsIcon = (ImageView) findViewById(R.id.text_icon);
		populateUI(appDetailsCursor);
	}

	private Cursor fetchAppDetailsFromDb(String ColumnId) {

		return DbHelper
				.getInstance(getApplicationContext())
				.getReadableDatabase()
				.query(DbHelper.APP_DETAILS_TABLE, null,
						DbHelper.C_ID + " = ?", new String[] { ColumnId },
						null, null, null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.app_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Toast.makeText(getApplication(), "Back", Toast.LENGTH_LONG).show();
			onBackPressed();
			break;
		case R.id.action_settings:
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void populateUI(Cursor cursor) {
		cursor.moveToFirst();
		appNameLabel.setText(cursor.getString(cursor
				.getColumnIndex(DbHelper.APP_NAME)));
		appRatingLabel.setText(cursor.getString(cursor
				.getColumnIndex(DbHelper.RATING)));
		appDescription.setText(cursor.getString(cursor
				.getColumnIndex(DbHelper.DESCRIPTION)));
		appType.setText(cursor.getString(cursor.getColumnIndex(DbHelper.TYPE)));
		innAppPurchase.setText(cursor.getString(cursor
				.getColumnIndex(DbHelper.IN_APP_PURCHASE)));
		lastUpdate.setText(cursor.getString(cursor
				.getColumnIndex(DbHelper.LAST_UPDATED)));
		Picasso.with(this)
				.load(cursor.getString(cursor.getColumnIndex(DbHelper.IMAGEURL)))
				.into(appLogo);

		setUpImageClickActions(cursor);

	}

	private void setUpImageClickActions(final Cursor cursor) {

		// PlayStore Linking
		playStoreIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d("fd", cursor.getString(cursor
						.getColumnIndex(DbHelper.PLAYSTORE_URL)));
				final String appPackageName = cursor.getString(
						cursor.getColumnIndex(DbHelper.PLAYSTORE_URL)).split(
						"=")[1];
				try {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri
							.parse("market://details?id=" + appPackageName)));
				} catch (android.content.ActivityNotFoundException anfe) {
					// for phone that do not have play store app installed
					startActivity(new Intent(
							Intent.ACTION_VIEW,
							Uri.parse("http://play.google.com/store/apps/details?id="
									+ appPackageName)));
				}
			}
		});

		// Share Action Linking
		shareIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent sharingIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				String shareBody = cursor.getString(cursor
						.getColumnIndex(DbHelper.DESCRIPTION));
				sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
						
						"New App from Adobe "+cursor.getColumnIndex(DbHelper.APP_NAME));
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
						shareBody);
				startActivity(Intent.createChooser(sharingIntent, "Share via"));
			}
		});

		// Sms Linking
		smsIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent smsIntent = new Intent(Intent.ACTION_VIEW);
				smsIntent.setType("vnd.android-dir/mms-sms");
				smsIntent.addCategory(Intent.CATEGORY_DEFAULT);
				smsIntent.putExtra("sms_body",
						cursor.getColumnIndex(DbHelper.APP_NAME)+" \n "+
						cursor.getColumnIndex(DbHelper.DESCRIPTION));
				startActivity(smsIntent);
			}
		});
	}

}
