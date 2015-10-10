package it.coursera.balax.homework.dailyselfie;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class SelfieFragment extends Fragment {
	
	//the selfie object associated with this detail fragment 
	private SelfieRecord selfie;
	
	public SelfieFragment(SelfieRecord selfie) {
		this.selfie = selfie;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {	
		setHasOptionsMenu(true);
		
		//generation of view
		View view = inflater.inflate(R.layout.selfie_details_fragment, container, false);
		
		//add the image view and load the bitmap using the image path
		ImageView imageView = (ImageView)view.findViewById(R.id.selfie_details_picture_image_view);
		imageView.setImageBitmap(SelfiesImageHelper.getScaledBitmap(selfie.getImagePath(), 0, 0));
		
	    getActivity().getActionBar().setTitle(selfie.getName());
		
        return view;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		//generate the menu of the fragment		
		menu.clear();
		inflater.inflate(R.menu.selfie_details_menu, menu);
	}
}