package itp341.barbosa.marlon.musicoftroy.db;



import itp341.barbosa.marlon.musicoftroy.model.Artist;
import itp341.barbosa.marlon.musicoftroy.model.ArtistLoader;
import itp341.barbosa.marlon.musicoftroy.model.Concert;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class DBConnector {
	private static final String TAG = DBConnector.class.getSimpleName();
	
	private SQLiteDatabase db;
	
	//SQL statement to create tables
			private static final String CREATE_TABLE_ARTISTS = 
					"CREATE TABLE " + TABLE_ARTISTS.NAME + " (" + 
							TABLE_ARTISTS.KEY_ARTIST_ID + " integer primary key autoincrement, " + 
							TABLE_ARTISTS.KEY_NAME + " TEXT, " +
							TABLE_ARTISTS.KEY_BIOGRAPHY + " TEXT, " +
							TABLE_ARTISTS.KEY_IMAGE_DATA + " BLOB " +
							");";
			private static final String CREATE_TABLE_CONCERTS = 
					"CREATE TABLE " + TABLE_CONCERTS.NAME + " (" + 
							TABLE_CONCERTS.KEY_CONCERT_ID + " integer primary key autoincrement, " + 
							TABLE_CONCERTS.KEY_ARTIST_ID + " integer, " + 
							TABLE_CONCERTS.KEY_CONCERT_NAME + " TEXT, " +
							TABLE_CONCERTS.KEY_CONCERT_DATE + " TEXT, " +
							TABLE_CONCERTS.KEY_LOCATION + " TEXT, " +
							TABLE_CONCERTS.KEY_TIME + " TEXT " +
							");";
	
	public DBConnector(Context context) {
		Log.d(TAG, "In DBConnector");
		db = new DBOpenHelper(context).getWritableDatabase();
		
	}
	
	public void close() {
		if (db != null)
			db.close();
	}
	
	
	public long insert(Artist artist) {
		Log.d(TAG, "insert: " + artist.toString());
		
		ContentValues cv = new ContentValues();
		
		cv.put(TABLE_ARTISTS.KEY_ARTIST_ID, artist.getArtist_id());
		cv.put(TABLE_ARTISTS.KEY_NAME, artist.getName());
		cv.put(TABLE_ARTISTS.KEY_BIOGRAPHY, artist.getBiography());
		cv.put(TABLE_ARTISTS.KEY_IMAGE_DATA, artist.getImageData());
		
		return db.insert(TABLE_ARTISTS.NAME, null, cv);
//		return 2;
	}
	
	public long insert(Concert concert) {
		Log.d(TAG, "insert: " + concert.toString());
		ContentValues cv = new ContentValues();
		cv.put(TABLE_CONCERTS.KEY_CONCERT_ID, concert.getConcert_id());
		cv.put(TABLE_CONCERTS.KEY_ARTIST_ID, concert.getArtist_id());
		cv.put(TABLE_CONCERTS.KEY_CONCERT_DATE, concert.getconcertDate());
		cv.put(TABLE_CONCERTS.KEY_CONCERT_NAME, concert.getConcertName());
		cv.put(TABLE_CONCERTS.KEY_LOCATION, concert.getPlace());
		cv.put(TABLE_CONCERTS.KEY_TIME, concert.getTime());
		
		return db.insert(TABLE_CONCERTS.NAME, null, cv);
	}
	
	
		
	public int deleteBySingleId(long id) {
		Log.d(TAG, "deleteSingleId: id = " + id);
		
		String selection = TABLE_ARTISTS.KEY_ARTIST_ID + "= ?";
		String[] selectionArgs = {Long.toString(id)};
		
		return db.delete(TABLE_ARTISTS.NAME, selection, selectionArgs);
		
	}
	
	public Cursor selectSearchArtists(String input) {
		Log.d(TAG, "SearchArtists() + "+  input);
		
		String sql = "SELECT _id, name, biography, imageData FROM artists WHERE name LIKE '%"+ input + "%'";
		
		return db.rawQuery(sql, null);
		
	}
	
	public Cursor selectAllArtists() {
		Log.d(TAG, "selectAllArtists");
		
		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = {
		    TABLE_ARTISTS.KEY_ARTIST_ID,
		    TABLE_ARTISTS.KEY_NAME,
		    TABLE_ARTISTS.KEY_BIOGRAPHY,
		    TABLE_ARTISTS.KEY_IMAGE_DATA
		    };

		
		//NB it is important to include KEY_ID or Creating the SimpleCursorAdapter
		//later will crash because _id not present
		Cursor cursor = db.query(TABLE_ARTISTS.NAME, 
				projection,	null, null, null, null, null);

		return cursor;
	}
	
	public Cursor selectAllConcerts() {
		Log.d(TAG, "selectAllConcerts");
		
		String sql = "SELECT concerts._id, concert_name, concert_date, location, time, name FROM artists,concerts WHERE artists._id=concerts.artist_id ORDER BY concert_date DESC";
		return db.rawQuery(sql, null);

	}
	
	public Cursor selectConcertByArtistId(long id) {
		Log.d(TAG, "selectConcertByArtistId");
		String[] selectionArgs = {Long.toString(id)};
		String sql = "SELECT concerts._id, concert_name, concert_date, location, time, name FROM artists,concerts WHERE artists._id=concerts.artist_id AND concerts.artist_id=? ORDER BY concert_date DESC";
		return db.rawQuery(sql, selectionArgs);
		
	}
	
	public Cursor selectById(long id) {
		Log.d(TAG, "selectById: id = " + id);
		
		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = {
			    TABLE_ARTISTS.KEY_ARTIST_ID,
			    TABLE_ARTISTS.KEY_NAME,
			    TABLE_ARTISTS.KEY_BIOGRAPHY,
			    TABLE_ARTISTS.KEY_IMAGE_DATA
			    };

		String selection = TABLE_ARTISTS.KEY_ARTIST_ID + "= ?";
		String[] selectionArgs = {Long.toString(id)};
		
		//NB it is important to include KEY_ID or Creating the SimpleCursorAdapter
		//later will crash because _id not present
		Cursor cursor = db.query(TABLE_ARTISTS.NAME, 
				projection,	selection, selectionArgs, null, null, null);

		return cursor;
	}

	public int updateBySingleId(long id, Artist artist) {
		Log.d(TAG, "UpdateSingleId: id = " + id);
		Log.d(TAG, "coffeeShop info = " + artist.toString());
		
		ContentValues cv = new ContentValues();
		cv.put(TABLE_ARTISTS.KEY_NAME, artist.getName());
		cv.put(TABLE_ARTISTS.KEY_BIOGRAPHY, artist.getBiography());
		cv.put(TABLE_ARTISTS.KEY_IMAGE_DATA, artist.getImageData());
		
		String selection = TABLE_ARTISTS.KEY_ARTIST_ID + "= ?";
		String[] selectionArgs = {Long.toString(id)};
		
		return db.update(TABLE_ARTISTS.NAME, 
				cv, 
				selection, 
				selectionArgs
				);
		
	}
	
	public void upgradeDB() {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTISTS.NAME);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONCERTS.NAME);
		db.execSQL(CREATE_TABLE_ARTISTS);
		db.execSQL(CREATE_TABLE_CONCERTS);
		
	}
	
	private void populateSample(SQLiteDatabase db) {
		ArrayList<Artist> artists = ArtistLoader.loadArtists();
		
		for (Artist artist: artists) {
			ContentValues cv = new ContentValues();
			cv.put(TABLE_ARTISTS.KEY_NAME, artist.getName());
			cv.put(TABLE_ARTISTS.KEY_BIOGRAPHY, artist.getBiography());
			cv.put(TABLE_ARTISTS.KEY_IMAGE_DATA, artist.getImageData());
			
			db.insert(TABLE_ARTISTS.NAME, null, cv);
		}
	}
	
	
	
	public static final class TABLE_ARTISTS {
		
		public static final String NAME = "artists";
		
		public static final String KEY_ARTIST_ID = "_id";
		public static final String KEY_NAME = "name";
		public static final String KEY_BIOGRAPHY = "biography";
		public static final String KEY_IMAGE_DATA = "imageData";
		
		
		public static final int COLUMN_ARTIST_ID = 0;
		public static final int COLUMN_NAME = 1;
		public static final int COLUMN_BIOGRAPHY = 2;
		public static final int COLUMN_IMAGE_DATA = 3;
	}
	
	public static final class TABLE_CONCERTS {
		public static final String NAME = "concerts";
		
		public static final String KEY_CONCERT_ID = "_id";
		public static final String KEY_ARTIST_ID = "artist_id";
		public static final String KEY_CONCERT_NAME = "concert_name";
		public static final String KEY_CONCERT_DATE = "concert_date";
		public static final String KEY_LOCATION = "location";
		public static final String KEY_TIME = "time";
		
		
		public static final int COLUMN_CONCERT_ID = 0;
		public static final int COLUMN_ARTIST_ID = 1;
		public static final int COLUMN_CONCERT_NAME = 2;
		public static final int COLUMN_CONCERT_DATE = 3;
		public static final int COLUMN_PLACE = 4;
		public static final int COLUMN_TIME = 5;
		
		
	}
	
	
	
	private class DBOpenHelper extends SQLiteOpenHelper {
		private static final String DATABASE_NAME = "music_of_troy.sqlite.db";
		private static final int DATABASE_VERSION = 5;
		
		

		public DBOpenHelper(Context context, int database_version) {
			super(context, DATABASE_NAME, null, database_version);
		}
		
		public DBOpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		

		//Called only first time database is created
		//Create the schema for the new table
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_TABLE_ARTISTS);
			db.execSQL(CREATE_TABLE_CONCERTS);
			populateSample(db);
			
		}
		
		
		//Implement this to address changes to database schema
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			
			Log.w("Example",
					"Upgrading database, this will drop ALL tables and recreate.");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTISTS.NAME);

			
			onCreate(db);
		}
		
	}

}
