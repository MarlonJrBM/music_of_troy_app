package itp341.barbosa.marlon.musicoftroy.app;

import itp341.barbosa.marlon.musicoftroy.db.DBConnector;
import itp341.barbosa.marlon.musicoftroy.model.Artist;
import itp341.barbosa.marlon.musicoftroy.model.ArtistLoader;
import itp341.barbosa.marlon.musicoftroy.model.Concert;
import itp341.barbosa.marlon.musicoftroy.model.ConcertLoader;
import itp341.barbosa.marlon.musicoftroy.model.JSONParser;

import java.io.IOException;
import java.text.ParseException;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final String TAG = MainActivity.class.getSimpleName();

	public static final String CONCERT_HOST = "http://barbosam.student.uscitp.com/m_of_t/json/concert_mobile_json.php";

	private Button buttonArtists;
	private Button buttonSync;
	private Button buttonConcerts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		buttonArtists = (Button) findViewById(R.id.buttonArtists);
		buttonSync = (Button) findViewById(R.id.buttonSync);
		buttonConcerts = (Button) findViewById(R.id.buttonConcerts);
		

		buttonConcerts.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),
						ConcertActivity.class);
				startActivity(i);
			}
		});

		buttonArtists.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),
						ChooseArtistActivity.class);
				startActivity(i);

			}
		});

		buttonSync.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				new OnlineSyncTask(v.getContext()).execute();

			}
		});

	}

	private class OnlineSyncTask extends AsyncTask<Void, Void, Boolean> {
		private static final String ARTIST_HOST = "http://barbosam.student.uscitp.com/m_of_t/json/artist_mobile_json.php";
		private Context context;
		private DBConnector dbConnector = new DBConnector(
				getApplicationContext());

		public OnlineSyncTask(Context activityContext) {
			this.context = activityContext;
			// dbConnector.populateSample();
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			dbConnector.upgradeDB();
			String jsonArrString;
			try {
				jsonArrString = JSONParser.getJSONStringFromUrl(ARTIST_HOST);
				for (Artist artist : ArtistLoader
						.loadArtistsFromJSON(jsonArrString)) {
					dbConnector.insert(artist);
				}
				jsonArrString = JSONParser.getJSONStringFromUrl(CONCERT_HOST);
				for (Concert concert : ConcertLoader
						.loadConcertsFromJSON(jsonArrString)) {
					dbConnector.insert(concert);
				}
			} catch (JSONException | ParseException e) {
				// TODO Auto-generated catch block
				Log.d(TAG, "Problem with JSON");
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.d(TAG, "Problem with Connection");
				e.printStackTrace();
				return false;
			}

			return true;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result == true) {
				Toast.makeText(context, R.string.sync_success,
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(context, R.string.sync_fail,
						Toast.LENGTH_LONG).show();
			}
			dbConnector.close();
		}
	}
}
