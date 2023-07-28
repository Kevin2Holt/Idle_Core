/*
* This code is designed, developed, and maintained by Kevin Holt.
* You may not copy, distribute, or reuse any part of this project.
*/
package com.avnior.idlecore;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import androidx.appcompat.app.AppCompatActivity;

import com.avnior.idlecore.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
	
// Class Variables
	static final    String                      endings                     = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	static final    double                      priceScalar                 = 0.5;
	static          int                         updateTimeInMilliseconds    = 1000;
	static          boolean                     isStartup                   = true;
	static          boolean[]                   unlockedLayers              = {true,   // p0
												                               false,  // p1
												                               false,  // p2
												                               false,  // p3
												                               false,  // p4
												                               };
	
// Reference Variables
					SharedPreferences          localData;
					SharedPreferences.Editor   localDataEditor;
	
					ActivityMainBinding         binding;
					P0Fragment                  p0View;
					P1Fragment                  p1View;
					SettingsFragment            settingsView;
					HelpFragment                helpView;
	
					ScheduledExecutorService    executorService             = Executors.newScheduledThreadPool(1);
					ScheduledFuture             updateInterval;
	
					
					
// Functions
	@Override
	protected       void                        onCreate(                           Bundle savedInstanceState) {
		Log.d("devNotes_functions_start","MainActivity: onCreate(): START");
		super.onCreate(savedInstanceState);
		binding = ActivityMainBinding.inflate(getLayoutInflater());
		supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(binding.getRoot());
		
		try {
			localData       = getSharedPreferences("Idlecore_data", MODE_PRIVATE);
			localDataEditor = localData.edit();
		} catch(Exception e) {
			Log.w("devNotes_data", "Couldn't find file IdleCore_data", e);
			localDataEditor = getSharedPreferences("Idlecore_data", MODE_PRIVATE).edit();
			localDataEditor.apply();
			localData = getSharedPreferences("Idlecore_data", MODE_PRIVATE);
		}
		
		testUnlockedLayers();
		
		p0View          = new P0Fragment();
		p1View          = new P1Fragment();
		settingsView    = new SettingsFragment();
		helpView        = new HelpFragment();
		
		getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.mainContainer, p0View)
				.commit();
		
		
		// pX navigation
		binding.navigationP0        .setOnClickListener(v -> getSupportFragmentManager()
																.beginTransaction()
																.replace(R.id.mainContainer, p0View)
																.commit());
		binding.navigationP1        .setOnClickListener(v -> getSupportFragmentManager()
																.beginTransaction()
																.replace(R.id.mainContainer, p1View)
																.commit());
		// Other navigation
		binding.settingsButton      .setOnClickListener(v -> getSupportFragmentManager()
																.beginTransaction()
																.replace(R.id.mainContainer, settingsView)
																.commit());
		binding.disclaimerCloseBtn  .setOnClickListener(v -> binding.disclaimerOuterBox.setVisibility(View.GONE));
		
		
		
		updateInterval = executorService.scheduleAtFixedRate(gameLoop, 5000, updateTimeInMilliseconds, TimeUnit.MILLISECONDS);
		Log.d("devNotes_functions", "MainActivity: onCreate(): END");
	}
	
	
// Get Data
	private static  char[]                      getNumLabel(                        int size) {
		Log.d("devNotes_functions_start", "getNumLabel()");
		Log.d("devNotes_values_getNumLabel", "getNumLabel(): size: "+ size);
		if(size == 1) {
			Log.d("devNotes_values_getNumLabel", "getNumLabel(): numLabels: " + String.valueOf(new char[] {'\0', endings.charAt(0)}));
			Log.d("devNotes_functions", "getNumLabel(): END");
			return new char[] {'\0', endings.charAt(0)};
		}
		Log.d("devNotes_values_getNumLabel", "getNumLabel(): numLabels: " + String.valueOf(new char[] {endings.charAt(((size-1)*2)-1), endings.charAt((size-1)*2)}));
		return new char[] {endings.charAt(((size-1)*2)-1), endings.charAt((size-1)*2)};
	}
	public static   String                      formatNumber(                       ArrayList<Integer> input) {
		Log.d("devNotes_functions_start", "formatNumber(): START");
		String output = "";
		int val = input.get(input.size()-1);
		Log.d("devNotes_values_formatNumber","input: " + input.toString());
		
		if(input.size() == 1) {
			Log.d("values_formatNumber","input.size() == 1");
			if(input.get(0) == 0) {
				output = "0";
				Log.d("values_formatNumber", "input.get(0) == 0");
			} else {
				switch ((int)Math.log10(val) + 1) {
					case (1):
					case (2):
					case (3):
						output = String.valueOf(val + getNumLabel(input.size())[0]);
						Log.d("devNotes_values_formatNumber", "case(1,2,3)");
						break;
					case (4):
						output = (val / 1000) + "." + ((val % 1000) / 10) + getNumLabel(input.size())[1];
						Log.d("devNotes_values_formatNumber", "case(4)");
						break;
					case (5):
						output = (val / 1000) + "." + ((val % 1000) / 100) + getNumLabel(input.size())[1];
						Log.d("devNotes_values_formatNumber", "case(5)");
						break;
					case (6):
						output = String.valueOf(val / 1000) + getNumLabel(input.size())[1];
						Log.d("devNotes_values_formatNumber", "case(6)");
						break;
					default:
						output = "ERROR.formatNumber()";
						Log.d("devNotes_values_formatNumber", "case(default)");
				}
			}
		} else {
			switch ((int)Math.log10(val) + 1) {
				case (1):
					output = val + "." + (input.get(input.size() - 2) / 10000) + getNumLabel(input.size())[0];
					Log.d("values_formatNumber", "case(1)");
					break;
				case (2):
					output = val + "." + (input.get(input.size() - 2) / 100000) + getNumLabel(input.size())[0];
					Log.d("values_formatNumber", "case(2)");
					break;
				case (3):
					output = String.valueOf(val) + getNumLabel(input.size())[0];
					Log.d("values_formatNumber", "case(3)");
					Log.d("values_formatNumber", "case(3): getNumLabel(): " + String.valueOf(getNumLabel(input.size())));
					break;
				case (4):
					output = (val / 1000) + "." + ((val % 1000) / 10) + getNumLabel(input.size())[1];
					Log.d("values_formatNumber", "case(4)");
					break;
				case (5):
					output = (val / 1000) + "." + ((val % 1000) / 100) + getNumLabel(input.size())[1];
					Log.d("values_formatNumber", "case(5)");
					break;
				case (6):
					output = String.valueOf(val / 1000) + getNumLabel(input.size())[1];
					Log.d("values_formatNumber", "case(6)");
					break;
				default:
					output = "ERROR.formatNumber()";
					Log.d("values_formatNumber", "case(default)");
			}
		}
		Log.d("devNotes_values","output: " + output);
		Log.d("devNotes_functions", "formatNumber(): END");
		return output;
	}
	public          int                         getUpdateTimeInMilliseconds(        ) {return updateTimeInMilliseconds;}
	public          void                        testUnlockedLayers(                 ) {
		Log.d("devNotes_functions_start", "testUnlockedLayers()");
		if(unlockedLayers[1]) binding.navigationP1.setVisibility(View.VISIBLE);
			else binding.navigationP1.setVisibility(View.GONE);
		if(unlockedLayers[2]) binding.navigationP2.setVisibility(View.VISIBLE);
			else binding.navigationP2.setVisibility(View.GONE);
		if(unlockedLayers[3]) binding.navigationP3.setVisibility(View.VISIBLE);
			else binding.navigationP3.setVisibility(View.GONE);
		if(unlockedLayers[4]) binding.navigationP4.setVisibility(View.VISIBLE);
			else binding.navigationP4.setVisibility(View.GONE);
	}

// Update  Data
	public          void                        updateCount(                        ) {
		Log.d("devNotes_functions_start", "updateCount(): START");
		binding.p0Count.setText((String)formatNumber(P0Fragment.p0_points));
		Log.d("devNotes_functions", "updateCount(): END");
	}
	public          void                        changeUpdateInterval(               long time) {
		Log.d("devNotes_functions", "changeUpdateInterval(): START");
	    if(time > 0) {
	        if (updateInterval != null) {
	            updateInterval.cancel(true);
	        }
	        updateInterval = executorService.scheduleAtFixedRate(gameLoop, 0, time, TimeUnit.MILLISECONDS);
	    }
		Log.d("devNotes_functions", "changeUpdateInterval(): END");

	}
	
// Change Data
	public static   ArrayList<Integer>          standardize(                        ArrayList<Integer> input) {
		Log.d("devNotes_functions_start", "standardize()");
		Log.d("devNotes_values_standardize","standardize(): input1: " + input.toString());
		int toNext = 0;
		int value = 0;
		
		for(int i=0; i<input.size(); i++) {
			Log.d("devNotes_values_standardize","standardize(): i: " + i);
			Log.d("devNotes_values_standardize", "standardize(): toNext1: " + toNext);
			
			value = input.get(i);
			Log.d("devNotes_values_standardize", "standardize(): value1: " + value);
			value += toNext;
			Log.d("devNotes_values_standardize", "standardize(): value2: " + value);
			
			if(value > 999_999) {
				toNext = value / 1_000_000;
				value -= toNext * 1_000_000;
			} else if(value < 0 ) {
				toNext = value;
				value = 0;
				input.set(i-1,toNext);
			} else {
				toNext = 0;
			}
			
			Log.d("devNotes_values_standardize", "standardize(): toNext2: " + toNext);
			
			if(toNext != 0) {
				input.set(i, value);
			}
			
			if(i==input.size()-1 && toNext > 0) {
				Log.d("devNotes_values_standardize","standardize(): input2: " + input.toString());
				input.add(toNext);
				Log.d("values_standardize","standardize(): input3: " + input.toString());
				Log.d("devNotes_functions", "standardize(): END");
				return input;
			}
			
			if(toNext < 0) {
				Log.d("devNotes_functions", "standardize(): RESTART");
				i = -1;
			}
		}
		return input;
	}
	public static   ArrayList<Integer>          arrayList_add(                      ArrayList<Integer> input, ArrayList<Integer> toAdd) {
		Integer oldValue = 0;
		for(int i=0; i<toAdd.size(); i++) {
			Log.d("values_points", "arrayList_add(): for: i: " + i);

			try {
				oldValue = input.get(i);
			}
			catch(Exception e) {
				oldValue = 0;
			}
			Log.d("values_points", "arrayList_add(): oldValue: " + oldValue);
			
			try {
				input.set(i, oldValue + toAdd.get(i));
				Log.d("devNotes_functions", "arrayList_add(): input.set()");
			} catch(Exception e) {
				Log.d("devNotes_functions", "arrayList_add(): input.add()");

				input.add(oldValue + toAdd.get(i));
			}
		}
		standardize(input);
		return input;
	}
	public static   void                        arrayList_subtract(                 ArrayList<Integer> input, ArrayList<Integer> toSubtract) {
		Integer oldValue = 0;
		for (int i = 0; i < toSubtract.size(); i++) {
			Log.d("values_points", "arrayList_subtract(): for: i: " + i);
			
			try {oldValue = input.get(i);}
			catch (Exception e) {oldValue = 0;}
			Log.d("values_points", "arrayList_subtract(): oldValue: " + oldValue);
			
			try {
				input.set(i, oldValue - toSubtract.get(i));
				Log.d("devNotes", "arrayList_subtract(): input.set()");
			} catch (Exception e) {
				input.add(oldValue - toSubtract.get(i));
				Log.d("devNotes", "arrayList_subtract(): input.add()");
			}
		}
		standardize(input);
	}
	public static   void                        arrayList_add(                      ArrayList<Integer> input, int toAdd) {
		try {
			input.set(0, input.get(0) + toAdd);
		}
		catch(Exception e) {
			input.add(toAdd);
		}
		standardize(input);
		
		Log.d("devNotes_functions", "arrayList_add(): END");
	}
	public static   int                         arrayList_getInt(                   ArrayList<Integer> input) {
		int output = 0;
		for(int i=0; i<input.size(); i++) {
			output += input.get(i) * (int)Math.pow(1_000_000,i);
		}
		return output;
	}
	public static   boolean                     arrayList_compare_greaterOrEqual(   ArrayList<Integer> input, ArrayList<Integer> toCompare) {
		
		if      (input.size() >  toCompare.size()) return true;
		else if (input.size() == toCompare.size()) return input.get(input.size()-1) >= toCompare.get(toCompare.size()-1);
		else                                       return false;
	}
	public static   ArrayList<Integer>          getIntegerArray(                    List<String> stringArray) {
        ArrayList<Integer> result = new ArrayList<>();
        for(String stringValue : stringArray) {
            try {
                //Convert String to Integer, and store it into integer array list.
                result.add(Integer.parseInt(stringValue.trim()));
            } catch(NumberFormatException nfe) {
               //System.out.println("Could not parse " + nfe);
                Log.w("NumberFormat", "Parsing failed! " + stringValue + " can not be an integer");
            }
        }
        return result;
    }
	public static   boolean                     buy(                                ArrayList<Integer> points, ArrayList<Integer> price) {
		Log.d("devNotes_functions_start", "buy(): START");
		Log.d("values_points",      "buy(): price: "  + price.toString());
		Log.d("values_points",      "buy(): points: " + points.toString());
		
		if(arrayList_compare_greaterOrEqual(points, price)) {
			arrayList_subtract(points, price);
			Log.d("devNotes_functions", "buy(): END");
			return true;
		} else {
			Log.d("devNotes_functions", "buy(): END");
			return false;
		}
	}
	
	
	
// Data Storage
	private         void                        readLocalData(                      ) {
		Log.d("devNotes_functions_start", "readLocalData(): START");
		updateTimeInMilliseconds =              localData.getInt("updateTimeInMilliseconds", 1000);
		
		// p0
		String p0_pointsString =                localData.getString("p0_points","[0]");
		P0Fragment.p0_points =                      getIntegerArray(Arrays.asList((p0_pointsString.substring(1, p0_pointsString.length() - 1).split(", "))));
		for(int i=0; i< P0Fragment.numGenerators; i++) {
			String p0GenString =                localData.getString("p0_generators_" + i + "_count", "[0]");
			p0View.p0Generators.get(i).count =  getIntegerArray(Arrays.asList((p0GenString.substring(1, p0GenString.length() - 1).split(", "))));
		}
	}
	private         void                        writeLocalData(                     ) {
		Log.d("devNotes_functions_start", "writeLocalData(): START");
		localDataEditor.putInt(         "updateTimeInMilliseconds",         updateTimeInMilliseconds);
		
		// p0
		localDataEditor.putString(      "p0_points",                        P0Fragment.p0_points.toString());
		
		for(int i=0; i< P0Fragment.numGenerators; i++) {
			localDataEditor.putString(  "p0_generators_" + i + "_count",    p0View.p0Generators.get(i).count.toString());
		}
		localDataEditor.apply();
	}
	
// Visuals & Updates
	public          void                        openHelpScreen(                     ) {
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.mainContainer, helpView)
				.commit();
	}
					Runnable                    gameLoop = (                        ) -> {
		Log.d("devNotes_functions_start", "----------------------------------------------------------------------------------------------------");
		Log.d("devNotes_functions_start", "gameLoop: run(): START");
		if(isStartup) {
			Log.d("devNotes","gameLoop: run(): Startup");
			readLocalData();
		}
		updateCount();
		testUnlockedLayers();
		if(unlockedLayers[0]) { // p0
			Log.d("devNotes_temp", "22222222222222222222222222222222222222222222222");
			p0View.updateGenerators(updateTimeInMilliseconds);
		}
		for(int i=0; i< P0Fragment.numGenerators; i++) {
			if(!isStartup) {
				// p0View.p0Generators.get(i).updateLabelsPrices();
				// p0View.p0Generators.get(i).updateLabels();
				p0View.p0Generators.get(i).updatePPS();
			}
		}
		writeLocalData();
		if(isStartup) {
			fadeOut();
			isStartup = false;
		}
		Log.d("devNotes_functions_end", "gameLoop: run(): END");
	};
	private         void                        fadeOut(                            ) {
		Animation fade2 = new AlphaAnimation(1.00f, 0.00f);
		
		fade2.setDuration(500);
		fade2.setAnimationListener(new Animation.AnimationListener() {
		
		    public void onAnimationStart(   Animation animation) {
		
		    }
		    public void onAnimationRepeat(  Animation animation) {
		
		    }
		    public void onAnimationEnd(     Animation animation) {
				binding.loadingScreen   .setVisibility(View.GONE);
		    }
		});
		
		
		Animation fade1 = new AlphaAnimation(1.00f, 0.00f);

		fade1.setDuration(500);
		fade1.setAnimationListener(new Animation.AnimationListener() {
		
		    public void onAnimationStart(   Animation animation) {
		
		    }
		    public void onAnimationRepeat(  Animation animation) {
		
		    }
		    public void onAnimationEnd(     Animation animation) {
				binding.idleCoreTitle   .setVisibility(View.GONE);
				binding.avniLogo        .setVisibility(View.GONE);
				binding.avniorLabel     .setVisibility(View.GONE);
				binding.loadingLabel    .setVisibility(View.GONE);
				
				binding.loadingScreen   .startAnimation(fade2);
		    }
		});
		
		
		binding.idleCoreTitle   .startAnimation(fade1);
		binding.avniLogo        .startAnimation(fade1);
		binding.avniorLabel     .startAnimation(fade1);
		binding.loadingLabel    .startAnimation(fade1);
	}
	
	
}