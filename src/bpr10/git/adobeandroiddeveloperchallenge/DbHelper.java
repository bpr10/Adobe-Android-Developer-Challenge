package bpr10.git.adobeandroiddeveloperchallenge;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {

	Context context;
	public static DbHelper sInstance;
	
	// Restaurant table Columns
	public static final String C_ID = "_id";
	public static final String APP_NAME = "appName";
	public static final String TYPE = "type";
	public static final String PLAYSTORE_URL = "PlayStoreUrl";
	public static final String IMAGEURL = "ImageUrl";
	public static final String RATING = "rating";
	public static final String LAST_UPDATED = "lastUpdated";
	public static final String IN_APP_PURCHASE = "inAppPurchase";
	public static final String DESCRIPTION = "description";
		// Database Version
	private static final int DATABASE_VERSION = 3;

	// Database Name
	private static final String DATABASE_NAME = "RestaurantData";

	// TableNames
	public static final String APP_DETAILS_TABLE = "AppDetailsTable";
	
	// DB Create
	private static String createAppDetailsTable = "CREATE TABLE IF NOT EXISTS "
			+ APP_DETAILS_TABLE + " ( " + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + APP_NAME
			+ " TEXT, " + TYPE + " TEXT, " + PLAYSTORE_URL + " TEXT, "
			+ IMAGEURL + " TEXT, " + RATING + " DECIMAL, " + LAST_UPDATED
			+ " TEXT, " + IN_APP_PURCHASE + " TEXT,  " + DESCRIPTION + " TEXT );";

	public static DbHelper getInstance(Context context) {

		if (sInstance == null) {
			sInstance = new DbHelper(context.getApplicationContext());
		}
		return sInstance;
	}

	private DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i("database version", String.valueOf(db.getVersion()));
		db.execSQL(createAppDetailsTable);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.rawQuery("DROP TABLE "+APP_DETAILS_TABLE, null);
		onCreate(db);
	}

	

}
