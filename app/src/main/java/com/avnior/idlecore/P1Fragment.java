/*
* This code is designed, developed, and maintained by Kevin Holt.
* You may not copy, distribute, or reuse any part of this project.
*/
package com.avnior.idlecore;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avnior.idlecore.databinding.FragmentP1Binding;


public class P1Fragment extends Fragment {
	
	FragmentP1Binding binding;
	
	public P1Fragment() {
		// Required empty public constructor
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d("devNotes_functions", "p1Fragment: onCreate(): START");
		super.onCreate(savedInstanceState);
		binding = FragmentP1Binding.inflate(getLayoutInflater());
		Log.d("devNotes_functions", "p1Fragment: onCreate(): END");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d("devNotes_functions", "p1Fragment: onCreateView(): START");
		
		
		
		Log.d("devNotes_functions", "p1Fragment: onCreateView(): END");
		return inflater.inflate(R.layout.fragment_p1, container, false);
	}
}