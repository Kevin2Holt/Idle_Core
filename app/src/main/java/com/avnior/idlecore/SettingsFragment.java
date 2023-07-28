/*
* This code is designed, developed, and maintained by Kevin Holt.
* You may not copy, distribute, or reuse any part of this project.
*/
package com.avnior.idlecore;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsFragment extends Fragment {
	
	SeekBar     updateIntervalInput;
	TextView    updateIntervalValue;
	Button      helpButton;
	
	public SettingsFragment() {
		// Required empty public constructor
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_settings, container, false);
		
		updateIntervalInput = view.findViewById(R.id.updateIntervalInput);
		updateIntervalValue = view.findViewById(R.id.updateIntervalValue);
		helpButton          = view.findViewById(R.id.helpButton);
		
		updateIntervalValue.setText((String)(((MainActivity)requireActivity()).getUpdateTimeInMilliseconds()+""));
		updateIntervalInput.setProgress(((MainActivity)requireActivity()).getUpdateTimeInMilliseconds());
		
		helpButton.setOnClickListener(v -> ((MainActivity)requireActivity()).openHelpScreen());
		
		updateIntervalInput.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				updateIntervalValue.setText((String)(progress + "ms"));
				((MainActivity)requireActivity()).changeUpdateInterval(progress);
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			
			}
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			
			}
		});
		return view;
	}
}