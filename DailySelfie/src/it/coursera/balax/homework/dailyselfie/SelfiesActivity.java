package it.coursera.balax.homework.dailyselfie;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;

public class SelfiesActivity extends Activity {
	
	private PendingIntent pendingIntent;
	
	private SelfiesFragment selfiesFragment;
	
	private static final int REQUEST_IMAGE_CAPTURE = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selfies_activity);
		
		selfiesFragment = new SelfiesFragment();
		
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container, selfiesFragment).commit();
		}
		
        setupAlarm();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.selfies_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_new_selfie) {
			//take the action to get a new selfie
			dispatchTakePictureIntent();
			return true;
		}
		else if (id == R.id.action_delete_all_selfies) {
			//take the action to delete all the selfie
			deleteAllSelfies();
			return true;
		}
		else if (id == R.id.action_delete_selfie) {
			//take the action to delete the selected selfie
			deleteSelfie();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
	        //generate a name for the selfie acquired with the camera. I use the datetime to generate a unique name
	        String selfieName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());	        
	        //generate an object with the name, that represent the new selfie
	        SelfieRecord newSelfie = new SelfieRecord(selfieName, null);
	        //add to the selfie object the bitmap of the file
	        newSelfie.setImageFile(SelfiesImageHelper.getTempBitmap(this));
	        
	        //add the selfie to the fragment
	        selfiesFragment.addSelfie(newSelfie);
	    }
	}
	
	private void setupAlarm() {
		//generate the intent to see the alarm in the home view
        Intent alarmIntent = new Intent(this, SelfiesAlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        
		AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 30000;

        //make the alarm repeating
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + interval, interval, pendingIntent);
	}
	
	private void dispatchTakePictureIntent() {
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(SelfiesImageHelper.getTempFile(this))); 
	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
	    }
	}
	
	private void deleteAllSelfies() {
		//request to user if he wnat to remove all seflies whith a dialog box
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getString(R.string.dialog_delete_all_selfies));
		builder.setPositiveButton(getString(R.string.dialog_yes), dialogDeleteAllSelfiesClickListener);
		builder.setNegativeButton(getString(R.string.dialog_no), dialogDeleteAllSelfiesClickListener).show();
	}
	
	private void deleteSelfie() {
		//delete the selfie
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getString(R.string.dialog_delete_selfie));
		builder.setPositiveButton(getString(R.string.dialog_yes), dialogDeleteSelfieClickListener);
		builder.setNegativeButton(getString(R.string.dialog_no), dialogDeleteSelfieClickListener).show();
	}
	
	DialogInterface.OnClickListener dialogDeleteAllSelfiesClickListener = new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
	        switch (which){
	        case DialogInterface.BUTTON_POSITIVE:
	        	selfiesFragment.deleteAllSelfies();
	            break;
	        case DialogInterface.BUTTON_NEGATIVE:
	            break;
	        }
	    }
	};
	
	DialogInterface.OnClickListener dialogDeleteSelfieClickListener = new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
	        switch (which){
	        case DialogInterface.BUTTON_POSITIVE:
	        	selfiesFragment.deleteSelectedSelfie();
	        	getFragmentManager().popBackStack();
	            break;
	        case DialogInterface.BUTTON_NEGATIVE:
	            break;
	        }
	    }
	};
}