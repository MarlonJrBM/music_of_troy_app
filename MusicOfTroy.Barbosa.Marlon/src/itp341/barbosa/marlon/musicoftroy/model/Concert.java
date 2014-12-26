package itp341.barbosa.marlon.musicoftroy.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class Concert {
	
	private static final String JSON_CONCERT_ID = "_id";
	private static final String JSON_ARTIST_ID = "artist_id";
	private static final String JSON_CONCERT_NAME = "concert_name";
	private static final String JSON_CONCERT_DATE = "concert_date";
	private static final String JSON_PLACE = "location";
	private static final String JSON_TIME = "time";
	
	private long concert_id;
	private long artist_id;
	private String artistName;
	private String concertName;
	private Date concertDate;
	private String place;
	private String time;
	
	
	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}


	/**
	 * @param time the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}


	/**
	 * @return the concert_id
	 */
	public long getConcert_id() {
		return concert_id;
	}

	
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(JSON_CONCERT_ID, this.concert_id);
		json.put(JSON_ARTIST_ID, this.artist_id);
		json.put(JSON_CONCERT_NAME, this.concertName);
		json.put(JSON_CONCERT_DATE, this.getconcertDate());
		json.put(JSON_PLACE, this.place);
		json.put(JSON_TIME, this.time);
		
		return json;
	}

	
	public Concert(JSONObject json) throws ParseException, JSONException {
		this.concert_id = json.getLong(JSON_CONCERT_ID);
		this.artist_id = json.getLong(JSON_ARTIST_ID);
		this.concertName = json.getString(JSON_CONCERT_NAME);
		this.setConcertDate(json.getString(JSON_CONCERT_DATE));
		this.place = json.getString(JSON_PLACE);
		this.time = json.getString(JSON_TIME);
		
	}

	/**
	 * @param concert_id the concert_id to set
	 */
	public void setConcert_id(long concert_id) {
		this.concert_id = concert_id;
	}



	/**
	 * @return the artistName
	 */
	public String getArtistName() {
		return artistName;
	}



	/**
	 * @param artistName the artistName to set
	 */
	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}



	/**
	 * @return the concertName
	 */
	public String getConcertName() {
		return concertName;
	}



	/**
	 * @param concertName the concertName to set
	 */
	public void setConcertName(String concertName) {
		this.concertName = concertName;
	}



	/**
	 * @return the place
	 */
	public String getPlace() {
		return place;
	}



	/**
	 * @param place the place to set
	 */
	public void setPlace(String place) {
		this.place = place;
	}



	public String getconcertDate() {
		return new SimpleDateFormat("MM/dd/yyyy").format(this.concertDate);
	}

	
	
	public void setConcertDate(Date dateCreated) {
		this.concertDate = dateCreated;
	}
	
	public void setConcertDate(String dateCreated) throws ParseException {
		this.concertDate = new SimpleDateFormat("MM/dd/yyyy").parse(dateCreated);
	}


	/**
	 * @return the artist_id
	 */
	public long getArtist_id() {
		return artist_id;
	}


	/**
	 * @param artist_id the artist_id to set
	 */
	public void setArtist_id(long artist_id) {
		this.artist_id = artist_id;
	}

}
