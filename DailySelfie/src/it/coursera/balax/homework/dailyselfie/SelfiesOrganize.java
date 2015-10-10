package it.coursera.balax.homework.dailyselfie;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SelfiesOrganize extends CursorAdapter {	
	
	private Context mContext;
	
	private String bitmapStoragePath;

	private static LayoutInflater mLayoutInflater = null;
	
	private ArrayList<SelfieRecord> mSelfieRecords = new ArrayList<SelfieRecord>();
	
	private SelfiesDBOperation selfiesDatabase;

	public SelfiesOrganize(Context context, Cursor cursor, int flags) {
		super(context, cursor, flags);

		mContext = context;
		mLayoutInflater = LayoutInflater.from(mContext);
		
		selfiesDatabase = new SelfiesDBOperation(mContext);
		selfiesDatabase.open();

		bitmapStoragePath = SelfiesImageHelper.getBitmapStoragePath(mContext);
		
		reloadData();
	}
	
	public void addSelfie(SelfieRecord newSelfie) {

		String filePath = bitmapStoragePath + "/" + newSelfie.getName();

		if(SelfiesImageHelper.storeBitmapToFile(newSelfie.getImageFile(), filePath)) {
			newSelfie.setImagePath(filePath);
        }
		
		selfiesDatabase.insertSelfie(newSelfie.getName(), newSelfie.getImagePath());
		
		reloadData();
	}
	
	public void deleteSelfie(SelfieRecord selfie) {
		selfiesDatabase.deleteSelfie(selfie.getId());
		
		reloadData();
	}
	
	public void deleteAllSelfies() {
		File bitmapStorageDir = new File(bitmapStoragePath);
		deleteAllFilesRecursive(bitmapStorageDir);
		
		selfiesDatabase.deleteAllSelfies();
		
		reloadData();
	}

	public void freeResources() {
		selfiesDatabase.close();
	}
	
	public void reloadData() {
		this.swapCursor(selfiesDatabase.getAllSelfies());
	}
	
	private SelfieRecord getSelfieRecordFromCursor(Cursor cursor) {

		int id = cursor.getInt(cursor.getColumnIndex(SelfiesDBOperation.KEY_ROWID));
		String name = cursor.getString(cursor.getColumnIndex(SelfiesDBOperation.KEY_NAME));
		String picturePath = cursor.getString(cursor.getColumnIndex(SelfiesDBOperation.KEY_PICTURE_PATH));

		SelfieRecord selfie = new SelfieRecord(name, picturePath);
		selfie.setId(id);

		return selfie;
	}
	
	
	private void deleteAllFilesRecursive(File fileOrDirectory) {
		if(fileOrDirectory.isDirectory()) {
			for(File child : fileOrDirectory.listFiles()) {
				deleteAllFilesRecursive(child);
			}
		}
		else {
			fileOrDirectory.delete();
		}
	}
	
	static class ViewHolder {
		TextView name;
		ImageView picture;
	}
	
	@Override
	public Cursor swapCursor(Cursor newCursor) {

		mSelfieRecords.clear();
		
		if(newCursor != null && newCursor.moveToFirst()) {
			do {
				mSelfieRecords.add(getSelfieRecordFromCursor(newCursor)); 	
			} while (newCursor.moveToNext());
		}
    
        return super.swapCursor(newCursor);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		ViewHolder holder = (ViewHolder) view.getTag();
		holder.name.setText(cursor.getString(cursor.getColumnIndex(SelfiesDBOperation.KEY_NAME)));
		
		int dimenPix = (int)mContext.getResources().getDimension(R.dimen.selfie_row_picture_width_height);
		
		holder.picture.setImageBitmap(SelfiesImageHelper.getScaledBitmap(cursor.getString(cursor.getColumnIndex(SelfiesDBOperation.KEY_PICTURE_PATH)), dimenPix, dimenPix));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		ViewHolder holder = new ViewHolder();

		View newView = mLayoutInflater.inflate(R.layout.selfie_row, parent, false);
		holder.name = (TextView)newView.findViewById(R.id.selfie_name_text_view);
		holder.picture = (ImageView)newView.findViewById(R.id.selfie_picture_image_view);

		newView.setTag(holder);

		return newView;
	}
	
	@Override
	public int getCount() {
		return mSelfieRecords.size();
	}

	@Override
	public Object getItem(int position) {
		return mSelfieRecords.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}