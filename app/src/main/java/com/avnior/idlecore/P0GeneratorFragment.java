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
import android.widget.TextView;

import java.util.ArrayList;

public class P0GeneratorFragment extends Fragment {
	
	int                id               = -1;
	ArrayList<Integer> pointsPerSecond  = new ArrayList<>();
	final ArrayList<Integer> basePPS;
	final ArrayList<Integer> basePrice;
	ArrayList<Integer> count            = new ArrayList<>();
	
	TextView genLabel;
	TextView countLabel;
	TextView genPerSecLabel;
	TextView buy1PriceLabel;
	TextView buy10PriceLabel;
	TextView buy100PriceLabel;
	TextView buyMaxPriceLabel;
	View view;
	
	
	public                          P0GeneratorFragment(    ) {
		// Required empty public constructor
		this.basePrice = new ArrayList<>();
		this.basePPS = new ArrayList<>();
	}
	public P0GeneratorFragment(int id, ArrayList<Integer> pointsPerSecond, ArrayList<Integer> basePrice, ArrayList<Integer> count) {
		this.id = id;
		this.pointsPerSecond.add(0);
		this.basePPS = pointsPerSecond;
		this.basePrice = basePrice;
		this.count = count;
	}
	@Override
	public      void                onCreate(               Bundle savedInstanceState) {
		Log.d("devNotes_functions_start", "p0GeneratorFragment: onCreate()");
		super.onCreate(savedInstanceState);
	}
	@Override
	public      View                onCreateView(           LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d("devNotes_temp", "p0GeneratorFragment: onCreateView(): id: " + this.id);
		Log.d("devNotes_functions_start", "p0GeneratorFragment: onCreateView(): START");
		
		view = inflater.inflate(R.layout.fragment_p0_generator, container, false);
		genLabel            = view.findViewById(R.id.generatorLabel);
		countLabel          = view.findViewById(R.id.ownLabel);
		genPerSecLabel      = view.findViewById(R.id.genPerSecLabel);
		buy1PriceLabel      = view.findViewById(R.id.buy_1_price);
		buy10PriceLabel     = view.findViewById(R.id.buy_10_price);
		buy100PriceLabel    = view.findViewById(R.id.buy_100_price);
		buyMaxPriceLabel    = view.findViewById(R.id.buy_max_price);
		
		Log.d("devNotes_functions", "p0GneratorFragment: onCreateView(): label.setText()");
		genLabel        .setText((String)("Generator " + (this.id+1)));
		this.updateLabels();
		
		Log.d("devNotes_functions", "p0GeneratorFragment: onCreateView(): .setOnClickListener()");
		view.findViewById(R.id.buy_1).setOnClickListener(v -> ((MainActivity)requireActivity()).p0View.buyGenerator(getID(), 1));
		view.findViewById(R.id.buy_10).setOnClickListener(v -> ((MainActivity)requireActivity()).p0View.buyGenerator(getID(), 10));
		view.findViewById(R.id.buy_100).setOnClickListener(v -> ((MainActivity)requireActivity()).p0View.buyGenerator(getID(), 100));
		view.findViewById(R.id.buy_max).setOnClickListener(v -> ((MainActivity)requireActivity()).p0View.buyGenerator(getID(), -1));
		
		Log.d("devNotes_functions", "p0GeneratorFragment: onCreateView(): END");
		return view;
	}
	
	private     int                 getID(                  ) {
		Log.d("devNotes_functions_start", "p0GeneratorFragment: getID()");
		return this.id;
	}
	public      ArrayList<Integer>  getPrice(               int count, int... tempCount) {
		Log.d("devNotes_functions_start", "p0GeneratorFragment: getPrice(): START");
		ArrayList<Integer> output      = new ArrayList<Integer>();
		double             priceScalar = MainActivity.priceScalar;
		
		MainActivity.arrayList_add(output, this.basePrice);
		
		
		
		if(count == -1) {
			int i = 0;
			if(MainActivity.arrayList_compare_greaterOrEqual(P0Fragment.p0_points, this.getPrice(1))) {
				while (MainActivity.arrayList_compare_greaterOrEqual(
						P0Fragment.p0_points,
						MainActivity.arrayList_add(output, this.getPrice(1, i)))) {
					MainActivity.arrayList_add(output, this.getPrice(1, i));
					i++;
				}
				return MainActivity.standardize(output);
			} else return this.getPrice(1);
		} else {
			
			for (int i = 0; i < count; i++) {
				Log.d("devNotes_values", "p0GeneratorFragment: getPrice(): i: " + i);
				
				if(tempCount.length == 0) {
					MainActivity.arrayList_add(output, (int)(priceScalar * Math.pow(
						MainActivity.arrayList_getInt(this.count) + i, 2)));
				} else {
					MainActivity.arrayList_add(output, (int)(priceScalar * Math.pow(
						MainActivity.arrayList_getInt(this.count) + i + tempCount[0], 2)));
				}
				Log.d("devNotes_values", "p0GeneratorFragment: getPrice(): output: " + output.toString());
			}
			return MainActivity.standardize(output);
		}
	}
	public      ArrayList<Integer>  generatePoints(         int timeInMilliseconds) {
		Log.d("devNotes_functions_start", "p0Generator("+this.id+"): generatePoints(): START");
		// Log.d("devNotes_temp", "p0GeneratorFragment: generatePoints()");
		Log.d("devNotes_values", "p0GeneratorFragment: generatePoints(): this.id: " + this.id);
		ArrayList<Integer> output = new ArrayList<>();
		double percentOfSecond = (double)timeInMilliseconds / 1000;
		
		Log.d("devNotes_values", "p0GeneratorFragment: generatePoints(): output: " + output.toString());
		Log.d("devNotes_values", "p0GeneratorFragment: generatePoints(): percentOfSecond: " + percentOfSecond);
		
		for(int i=0; i<this.pointsPerSecond.size(); i++) {
			Log.d("devNotes_values", "p0GeneratorFragment: generatePoints(): i: " + i);
			output.add((int)(this.pointsPerSecond.get(i)*percentOfSecond));
		}
		
		MainActivity.standardize(output);
		
		ArrayList<Integer> one = new ArrayList<>();
		one.add(1);
		
		if(MainActivity.arrayList_compare_greaterOrEqual(this.count, one)) {
			ArrayList<Integer> output2 = new ArrayList<>();
			for(int i=0; i<MainActivity.arrayList_getInt(this.count); i++) {
				MainActivity.arrayList_add(output2,output);
			}
			Log.d("devNotes_functions", "p0GeneratorFragment: generatePoints(): END");
			return output2;
		} else {
			one.set(0, 0);
			Log.d("devNotes_functions", "p0GeneratorFragment: generatePoints(): END");
			return one;
		}
	}
	
	public void updatePPS(int count) {
		
		for(int i=0; i<count; i++) {
			MainActivity.arrayList_add(this.pointsPerSecond, this.basePPS);
		}
		this.updateLabels();
	}
	public void updatePPS() {
		this.pointsPerSecond = new ArrayList<>();
		this.pointsPerSecond.add(0);
		
		for(int i=0; i<MainActivity.arrayList_getInt(this.count); i++) {
			MainActivity.arrayList_add(this.pointsPerSecond, this.basePPS);
		}
		// this.updateLabels();
	}
	
	public void updateLabels() {
		Log.d("devNotes_functions_start", "p0Generator: updateLabels(): START ("+this.id+")");
		countLabel.setText((String)("Count " + MainActivity.formatNumber(this.count)));
		Log.d("devNotes_functions_mid", "p0Generator: updateLabels(): MID");
		
		genPerSecLabel.setText((String)(MainActivity.formatNumber(this.pointsPerSecond) + "⓪/s"));
	}
	public void updateLabelsPrices() {
		Log.d("devNotes_functions_start", "p0Generator: updateLabelsPrices(): START ("+this.id+")");
		String temp = "⓪"+MainActivity.formatNumber(this.getPrice(1));
		Log.d("devNotes_values","temp: "+temp);
		buy1PriceLabel.setText(temp);
		buy10PriceLabel.setText((String)("⓪"+MainActivity.formatNumber(this.getPrice(10))));
		buy100PriceLabel.setText((String)("⓪"+MainActivity.formatNumber(this.getPrice(100))));
		buyMaxPriceLabel.setText((String)("⓪"+MainActivity.formatNumber(this.getPrice(-1))));
		Log.d("devNotes_functions_end", "p0Generator: updateLabelsPrices(): END");

	}
}