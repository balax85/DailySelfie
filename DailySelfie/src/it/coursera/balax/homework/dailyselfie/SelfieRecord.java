package it.coursera.balax.homework.dailyselfie;

import android.graphics.Bitmap;

public class SelfieRecord {	
	
	//the id of the selfie
	private int id;
		
	//the name of the selfie
	private String name;
	
	//the path where the image is stored
	private String imagePath;
	
	//the bitmap of the image
	private Bitmap imageFile;
	
	public SelfieRecord(String name, String imagePath) {
		super();		
		this.name = name;
		this.imagePath = imagePath;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getImagePath() {
		return imagePath;
	}
	
	public void setImagePath(String picturePath) {
		this.imagePath = picturePath;
	}
	
	public Bitmap getImageFile() {
		return imageFile;
	}
	
	public void setImageFile(Bitmap imageFile) {
		this.imageFile = imageFile;
	}
}