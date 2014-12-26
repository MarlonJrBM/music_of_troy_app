package itp341.barbosa.marlon.musicoftroy.model;

import java.io.Serializable;
import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;

public class Artist implements Serializable {
	
	
	private static final String JSON_ARTIST_ID = "_id";
	private static final String JSON_NAME = "artist_name";
	private static final String JSON_BIOGRAPHY = "biography";
	private static final String JSON_IMAGE_DATA = "image_data";
	private long _id;
	private String name;
	private String biography;
	private byte[] imageData;
	
	public Artist() {
		this.name = "John Lennon";
		this.biography = "The best.";
	}
	
	public Artist(String name, String biography, byte[] imageData) {
		this.name = name;;
		this.biography = biography;
		this.imageData = imageData;
	}
	
	public Artist(String name, String biography, String imageData) {
		this.name = name;
		this.biography = biography;
		this.imageData = Base64.decode(imageData, Base64.DEFAULT);
	}
	
	
	
	public Artist(String name, String biography) {
		this.name = name;
		this.biography = biography;
	}
	
	/**
	 * @return the artist_id
	 */
	public long getArtist_id() {
		return _id;
	}
	/**
	 * @param artist_id the artist_id to set
	 */
	public void setArtist_id(long artist_id) {
		this._id = artist_id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the biography
	 */
	public String getBiography() {
		return biography;
	}
	/**
	 * @param biography the biography to set
	 */
	public void setBiography(String biography) {
		this.biography = biography;
	}
	/**
	 * @return the imageData
	 */
	public byte[] getImageData() {
		return imageData;
	}
	
	/**
	 * @param imageData the imageData to set
	 */
	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}
	
	public Artist(JSONObject json) throws ParseException, JSONException {
		super();
		this._id = json.getLong(JSON_ARTIST_ID);
		this.name = json.getString(JSON_NAME);
		this.biography = json.getString(JSON_BIOGRAPHY);
		this.imageData = Base64.decode(json.getString(JSON_IMAGE_DATA), Base64.DEFAULT);
		
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(JSON_ARTIST_ID, this._id);
		json.put(JSON_NAME, this.name);
		json.put(JSON_BIOGRAPHY, this.biography);
		json.put(JSON_IMAGE_DATA, Base64.encodeToString(this.imageData, Base64.DEFAULT));
		
		return json;
	}

	
	
	

}
