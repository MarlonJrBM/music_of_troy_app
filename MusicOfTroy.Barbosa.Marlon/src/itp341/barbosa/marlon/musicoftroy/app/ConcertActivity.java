package itp341.barbosa.marlon.musicoftroy.app;

import itp341.barbosa.marlon.musicoftroy.db.DBConnector;
import itp341.barbosa.marlon.musicoftroy.db.DBConnector.TABLE_ARTISTS;
import itp341.barbosa.marlon.musicoftroy.db.DBConnector.TABLE_CONCERTS;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;

public class ConcertActivity extends ListActivity {
	
	private static final String TAG = ListActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_concert);
		Intent i = getIntent();
		long artist_id = i.getLongExtra(ChooseArtistActivity.EXTRA_ARTIST_ID, 0);
		if (artist_id==0) {
			loadAllConcerts();	
		} else {
			loadConcertsByArtistId(artist_id);
		}
		
		
	}
	
	private void loadConcertsByArtistId(long artist_id) {
		// TODO Auto-generated method stub
		Log.d(TAG, "loadConcertByArtistId()");
		new DBSelectConcertTask(this).execute(artist_id);
	}

	private void loadAllConcerts() {
		Log.d(TAG, "loadAllConcerts()");
		new DBSelectAllTask(this).execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.concert, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_artists) {
			startActivity(new Intent(getApplicationContext(), ChooseArtistActivity.class));
			return true;
		} else if (id == R.id.action_all_concerts) {
			startActivity(new Intent(getApplicationContext(), ConcertActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	private class DBSelectConcertTask extends AsyncTask<Long, Void, Cursor> {
		
		private Context context;
		private DBConnector dbConnector = new DBConnector(getApplicationContext());
		
		public DBSelectConcertTask(Context activityContext) {
			this.context = activityContext;
		}

		@Override
		protected Cursor doInBackground(Long... arg0) {
			// TODO Auto-generated method stub
			
			return dbConnector.selectConcertByArtistId(arg0[0]);
			
		}
		
		protected void onPostExecute(Cursor result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			String[] from = new String[] {TABLE_ARTISTS.KEY_NAME, TABLE_CONCERTS.KEY_CONCERT_DATE, TABLE_CONCERTS.KEY_LOCATION, TABLE_CONCERTS.KEY_TIME};
			int[] to = new int[] {R.id.textConcertArtistName, R.id.textConcertDate, R.id.textWhere, R.id.textConcertTime};
			
			
			//must use "this" here or the UI style / theme is wrong (white text)
			SimpleCursorAdapter adapter = new SimpleCursorAdapter(
					this.context,		
					R.layout.concertrowlayout, 
					result, 
					from, 
					to, 
					0);						
			
			setListAdapter(adapter);
			dbConnector.close();
			
		}
		
		
		
	}
	
	private class DBSelectAllTask extends AsyncTask<Void, Void, Cursor> {
		private Context context;
		private DBConnector dbConnector = new DBConnector(getApplicationContext());
		
		public DBSelectAllTask(Context activityContext) {
			this.context = activityContext;
//			dbConnector.populateSample();
		}
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		
		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Cursor doInBackground(Void... arg0) {
			// TODO Auto-generated method stub

			return dbConnector.selectAllConcerts();
		}
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Cursor result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			String[] from = new String[] {TABLE_ARTISTS.KEY_NAME, TABLE_CONCERTS.KEY_CONCERT_DATE, TABLE_CONCERTS.KEY_LOCATION, TABLE_CONCERTS.KEY_TIME};
			int[] to = new int[] {R.id.textConcertArtistName, R.id.textConcertDate, R.id.textWhere, R.id.textConcertTime};
			
			
			//must use "this" here or the UI style / theme is wrong (white text)
			SimpleCursorAdapter adapter = new SimpleCursorAdapter(
					this.context,		
					R.layout.concertrowlayout, 
					result, 
					from, 
					to, 
					0);						
			
			setListAdapter(adapter);
			dbConnector.close();
			
		}
		
		
	}
}
