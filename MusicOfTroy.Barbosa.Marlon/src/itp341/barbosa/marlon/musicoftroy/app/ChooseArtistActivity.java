package itp341.barbosa.marlon.musicoftroy.app;

import itp341.barbosa.marlon.musicoftroy.db.DBConnector;
import itp341.barbosa.marlon.musicoftroy.db.DBConnector.TABLE_ARTISTS;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SimpleCursorAdapter;

public class ChooseArtistActivity extends ListActivity {
	private static final String TAG = ChooseArtistActivity.class
			.getSimpleName();
	public static final String EXTRA_ARTIST_ID = "extra_artist_id";
	SimpleCursorAdapter adapter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.artist, menu);
		
		//Set searchable xml to search bar
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
//		searchView.setSubmitButtonEnabled(true);
		searchView.setOnQueryTextListener(new OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				adapter.getFilter().filter(newText);
				return false;
			}
		});
		return super.onCreateOptionsMenu(menu);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_concerts:
			startActivity(new Intent(getApplicationContext(), ConcertActivity.class));
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_artist);
		loadData();
	}

	private void loadData() {
		Log.d(TAG, "loadData()");
		new DBSelectAllTask(this).execute();
	}

	
	private class DBSearchQuery extends AsyncTask<String, Void, Cursor> {
		
		private Context context;
		private DBConnector dbConnector = new DBConnector(
				getApplicationContext());
		
		public DBSearchQuery(Context activityContext) {
			this.context = activityContext;
		}

		@Override
		protected Cursor doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			Log.d(TAG, "Search Query in the background: " +  arg0[0]);
			
			
			return dbConnector.selectSearchArtists(arg0[0]);
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Cursor result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			
		}
		
		
		
	}
	private class DBSelectAllTask extends AsyncTask<Void, Void, Cursor> {
		private Context context;
		private DBConnector dbConnector = new DBConnector(
				getApplicationContext());

		public DBSelectAllTask(Context activityContext) {
			this.context = activityContext;
			// dbConnector.populateSample();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Cursor doInBackground(Void... arg0) {
			// TODO Auto-generated method stub

			return dbConnector.selectAllArtists();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Cursor result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			String[] from = new String[] { TABLE_ARTISTS.KEY_NAME,
					TABLE_ARTISTS.KEY_BIOGRAPHY, TABLE_ARTISTS.KEY_IMAGE_DATA };
			int[] to = new int[] { R.id.textArtistName, R.id.textBiography,
					R.id.imageArtist };

			// must use "this" here or the UI style / theme is wrong (white
			// text)
			adapter = new SimpleCursorAdapter(this.context,
					R.layout.artistrowlayout, result, from, to, 0);

			
			adapter.setFilterQueryProvider(new FilterQueryProvider() {
				
				@Override
				public Cursor runQuery(CharSequence constraint) {
					// TODO Auto-generated method stub
					Log.d(TAG, "runQuery constraint:"+constraint);
					if (dbConnector==null) {
						dbConnector = new DBConnector(
								getApplicationContext());
					}
					
					return dbConnector.selectSearchArtists(constraint.toString());
				}
			});
			// SUPER UPER HACK
			adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {

				@Override
				public boolean setViewValue(View view, Cursor cursor,
						int columnIndex) {
					if (view.getId() == R.id.imageArtist) {
						Log.d(TAG, "imageArtist - imageView");
						ImageView imageArtist = (ImageView) view;
						byte[] outImage = cursor.getBlob(3);
						if (outImage != null) {
							Bitmap bm = BitmapFactory.decodeByteArray(outImage, 0, outImage.length);
							int width = bm.getWidth();
							int height = bm.getHeight();
							Log.d(TAG, width + " " + height);
							int newWidth = 270;
							float newHeight = (float)height * (float)newWidth / width;
							Log.d(TAG, newWidth + " " + newHeight);
							
							imageArtist.setImageBitmap(Bitmap.createScaledBitmap(bm, newWidth,(int)newHeight,true));
								
							
							return true;
						}

					}
					return false;
				}

			});

			setListAdapter(adapter);
			dbConnector.close();
			dbConnector = null;

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView,
	 * android.view.View, int, long)
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);

		Intent i = new Intent(getApplicationContext(), ConcertActivity.class);
		i.putExtra(EXTRA_ARTIST_ID, id);
		startActivity(i);

	}

}
