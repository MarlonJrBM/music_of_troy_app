package itp341.barbosa.marlon.musicoftroy.model;

import java.text.ParseException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class ConcertLoader {

	public static ArrayList<Concert> loadConcertsFromJSON(String jsonString) throws JSONException, ParseException {
		ArrayList<Concert> concerts = new ArrayList<Concert>();
		
		JSONArray array;
			array = (JSONArray) new JSONTokener(
					jsonString).nextValue();
			for (int i = 0; i < array.length(); i++) {
				JSONObject json = array.getJSONObject(i);
				concerts.add(new Concert(json));
			}

		
		
		return concerts;
	}
	
}
