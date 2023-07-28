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
import android.widget.Button;

import com.avnior.idlecore.databinding.FragmentP0Binding;

import java.util.ArrayList;


public class P0Fragment extends Fragment {

// Class Variables
	static final int numGenerators = 8;
	static boolean isStartup = true;
	static ArrayList<Integer> p0_points      = new ArrayList<>();
	static ArrayList<Integer> p0_value_click = new ArrayList<>();
	static ArrayList<ArrayList<Integer>>   p0Gen_basePrice    = new ArrayList<>();
	FragmentP0Binding binding;
	
	
	
	ArrayList<P0GeneratorFragment>  p0Generators       = new ArrayList<>();
	ArrayList<ArrayList<Integer>>   p0Gen_pointsPerSec = new ArrayList<>();
	int[] p0Gen_views = {R.id.p0Gen0,
	                     R.id.p0Gen1,
	                     R.id.p0Gen2,
	                     R.id.p0Gen3,
	                     R.id.p0Gen4,
	                     R.id.p0Gen5,
	                     R.id.p0Gen6,
	                     R.id.p0Gen7};
	
// **** This is used for startup and data sync purposes                                    ****
// **** In all other cases, use the generator's internal count (P0GeneratorFragment.count) ****
	ArrayList<ArrayList<Integer>>   p0Gen_count        = new ArrayList<>();
	
// Values
	
	
	public                          P0Fragment(         ) {
		// Required empty public constructor
	}
	@Override
	public          void                onCreate(           Bundle savedInstanceState) {
		Log.d("devNotes_functions_start", "p0Fragment: onCreate(): START");
		super.onCreate(savedInstanceState);
		binding = FragmentP0Binding.inflate(getLayoutInflater());
		
		// Initial Values
		if(isStartup) {
			p0_value_click.add(1);
			p0_points.add(0);
			Log.d("devNotes_values_p0", "p0_value_click: " + p0_value_click);
			Log.d("devNotes_values_p0", "p0: " + p0_points.toString());
		}
		
		Log.d("devNotes_functions", "p0Fragment: onCreate(): Initializing Generators");
		for(int i=0; i<numGenerators; i++) {
			Log.d("devNotes_values","i: " + i);
			if(isStartup) {
				p0Gen_pointsPerSec.add(new ArrayList<>());
				p0Gen_basePrice.add(new ArrayList<>());
				p0Gen_count.add(new ArrayList<>());
				
				Log.d("devNotes_values", "p0Gen_pointsPerSec[i]: " + p0Gen_pointsPerSec.get(i).toString());
				p0Gen_pointsPerSec.get(i).add((int)Math.pow(10, i));
				p0Gen_basePrice.get(i).add((int)Math.pow(10, i + 1));
				p0Gen_count.get(i).add(0);
				
				MainActivity.standardize(p0Gen_basePrice.get(i));
			}
			
			MainActivity.standardize(p0Gen_pointsPerSec.get(i));
			
			MainActivity.standardize(p0Gen_count.get(i));
			
			if(isStartup) {
				p0Generators.add(new P0GeneratorFragment(i, p0Gen_pointsPerSec.get(i), p0Gen_basePrice.get(i), p0Gen_count.get(i)));
			} else {
				p0Generators.set(i, new P0GeneratorFragment(i, p0Gen_pointsPerSec.get(i), p0Gen_basePrice.get(i), p0Gen_count.get(i)));
			}
			
			((MainActivity)requireActivity()).getSupportFragmentManager().beginTransaction().replace(p0Gen_views[i],p0Generators.get(i)).commit();
		}
		
		Log.d("devNotes_functions", "Exit For Loop");

		MainActivity.standardize(p0_value_click);
		MainActivity.standardize(p0_points);
		((MainActivity)requireActivity()).updateCount();
		
		isStartup = false;
		
		Log.d("devNotes_values_p0", "p0_value_click: "  + p0_value_click.toString());
		Log.d("devNotes_values_p0", "p0: "              + p0_points.toString());
		Log.d("devNotes_functions", "p0Fragment: "      + "onCreate(): END");
	}
	@Override
	public          View                onCreateView(       LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d("devNotes_functions_start", "p0Fragment: onCreateView(): START");
		
		View   view = inflater.inflate(R.layout.fragment_p0, container, false);
		Button button = (Button) view.findViewById(R.id.p0_click_button);
		updateClickText(button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("devNotes_functions", "p0Fragment: p0ClickButton: onClick(): START");
				MainActivity.arrayList_add(p0_points, p0_value_click);
				Log.d("devNotes_values_p0", "p0_click_button: onClick(): p0: " + p0_points.toString());
				Log.d("devNotes_functions", "p0Fragment: p0ClickButton: onClick: END");
			}
		});
		Log.d("devNotes_functions", "p0Fragment: onCreateView(): END");
		return view;
	}
	
	
	public static   ArrayList<Integer>  getP0_points(       ) {
		Log.d("devNotes_functions_start", "getP0()");
		return new ArrayList<Integer>(p0_points);
	}
	private         void                updateClickText(    Button btn) {
		Log.d("devNotes_functions_start", "p0Fragment: updateClickText(): START");
		
		Log.d("values_click","updateClickText(): p0_value_click: " + p0_value_click.toString());
		Log.d("values_click", "updateClickText(): formatNumber(p0_value_click): " + MainActivity.formatNumber(p0_value_click));
		String buttonText = "+"+MainActivity.formatNumber(p0_value_click)+"⓪";
		btn.setText(buttonText);
		
		Log.d("devNotes_functions", "p0Fragment: updateClickText(): END");
	}
	public          void                buyGenerator(       int id, int count) {
		Log.d("devNotes_functions_start", "p0Fragment: buyGenerator(): START");
		if(count == -1) {
			while(MainActivity.buy(p0_points, p0Generators.get(id).getPrice(1))) {
				MainActivity.arrayList_add(p0Generators.get(id).count, 1);
			}
		} else if(MainActivity.buy(p0_points, p0Generators.get(id).getPrice(count))) {
			MainActivity.arrayList_add(p0Generators.get(id).count, count);
		}
		p0Generators.get(id).updateLabels();
		p0Generators.get(id).updateLabelsPrices();
		p0Generators.get(id).updatePPS(count);
		Log.d("devNotes_functions", "p0Fragment: buyGenerator(): END");
	}
	public          void                updateGenerators(   int timeInMilliseconds) {
		Log.d("devNotes_functions_start", "p0Fragment: updateGenerators(): START");
		// Log.d("devNotes_temp", "p0Fragment: updateGenerators(): p0Generators.length: " + p0Generators.size());
		for (int i=0; i<p0Generators.size(); i++) {
			Log.d("devNotes_temp", "p0Fragment: updateGenerators(): i: " + i);
			MainActivity.arrayList_add(p0_points, p0Generators.get(i).generatePoints(timeInMilliseconds));
			Log.d("devNotes_temp", "p0Fragment: updateGenerators(): Spot 2");
		}
		Log.d("devNotes_functions", "p0Fragment: updateGenerators(): END");
	}
}