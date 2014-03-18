package flash.light.pro;

import java.io.IOException;
import java.util.Random;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.actionbarsherlock.app.SherlockActivity;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class FlashlightActivity extends SherlockActivity implements Callback {
	private static final String TAG = FlashlightActivity.class.getSimpleName();

	Camera mCamera;
	ImageButton btnSwitch;
	private ToggleButton mLightSwitch;
	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	String toggler;
	String on = "ON";
	String off = "OFF";
	String SWITCH;
	MediaPlayer mp;
	private AdView mAdView;
	TextView txttitle;
	TextView txtversion;
	RelativeLayout layout;
	Parameters p;
	boolean swap;
	SeekBar seekbar;
	Integer modeint;
	Integer modeprogress;
	Handler mHandler;
	Runnable runner;
	Integer milliseconds;
	Integer solid;
	String ON;
	Vibrator v;
	TextView txtmode;
	int min = 1;
	int max = 10;
	int colorscheme;
	SlidingMenu menu;
	RadioGroup radioGroup;
	BootstrapButton setbtn;
	String scheme;
	View topleft;
	View bottomleft;
	View right;
	String random = "Random";
	String blue = "Blue";
	String green = "Green";
	String orange = "Orange";
	String red = "Red";
	String turquoise = "Turquoise";
	String midnight = "Midnight";
	String rasta = "Rasta";
	String beach = "Beach";
	String camo = "Camo";
	String carbon = "Carbon";
	TextView title;
	Typeface monbold;
	Typeface monreg;
	RadioButton radRandom;
	RadioButton radBlue;
	RadioButton radGreen;
	RadioButton radOrange;
	RadioButton radRed;
	RadioButton radTurq;
	RadioButton radMidnight;
	RadioButton radRasta;
	RadioButton radBeach;
	RadioButton radCamo;
	RadioButton radCarbon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//getSupportActionBar().hide();

		setContentView(R.layout.main);

		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		// menu.setShadowWidthRes(R.dimen.shadow_width);
		// menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.8f);
		menu.setBehindWidthRes(R.dimen.menu_width);
		menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		menu.setMenu(R.layout.menu);

		topleft = findViewById(R.id.topleft);
		bottomleft = findViewById(R.id.bottomleft);
		right = findViewById(R.id.right);

		radioGroup = (RadioGroup) findViewById(R.id.radioColor);
		setbtn = (BootstrapButton) findViewById(R.id.setbtn);

		setbtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (v == setbtn) {

					// Get Selected Radio Button and display output
					RadioButton selectRadio = (RadioButton) findViewById(radioGroup
							.getCheckedRadioButtonId());
					scheme = selectRadio.getText().toString();
					checkColourScheme();
					storeScheme();
					menu.showContent();
				}
			}
		});

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		mCamera = Camera.open();
		initCamera();

		// TYPEFACES
		String bold = "fonts/Montserrat-Bold.ttf";
		String regular = "fonts/Montserrat-Regular.ttf";

		monbold = Typeface.createFromAsset(getAssets(), bold);
		monreg = Typeface.createFromAsset(getAssets(), regular);

		//setbtn.setTypeface(monbold);
		radRandom = (RadioButton) findViewById(R.id.random);
		radRandom.setTypeface(monreg);
		radBlue = (RadioButton) findViewById(R.id.blue);
		radBlue.setTypeface(monreg);
		radGreen = (RadioButton) findViewById(R.id.green);
		radGreen.setTypeface(monreg);
		radOrange = (RadioButton) findViewById(R.id.orange);
		radOrange.setTypeface(monreg);
		radRed = (RadioButton) findViewById(R.id.red);
		radRed.setTypeface(monreg);
		radTurq = (RadioButton) findViewById(R.id.turquoise);
		radTurq.setTypeface(monreg);
		radMidnight = (RadioButton) findViewById(R.id.midnight);
		radMidnight.setTypeface(monreg);
		radRasta = (RadioButton) findViewById(R.id.rasta);
		radRasta.setTypeface(monreg);
		radBeach = (RadioButton) findViewById(R.id.beach);
		radBeach.setTypeface(monreg);
		radCamo = (RadioButton) findViewById(R.id.camo);
		radCamo.setTypeface(monreg);
		radCarbon = (RadioButton) findViewById(R.id.carbon);
		radCarbon.setTypeface(monreg);
		// TYPFACES

		getStoredScheme(); // shared prefs
		checkColourScheme();

		// MODE FUNCTION
		seekbar = (SeekBar) findViewById(R.id.seekBar);
		modeint = 0;
		milliseconds = 100;
		solid = 1;
		ON = "on";
		txtmode = (TextView) findViewById(R.id.txtmode);
		txtmode.setTypeface(monbold);
		v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			int stepSize = 1;

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				progress = ((int) Math.round(progress / stepSize)) * stepSize;
				seekBar.setProgress(progress);
				modeint = progress;
				modeprogress = progress;
				txtmode.setText(progress + "");

				if (modeint == 0) {
					solid = 1;
					milliseconds = 500;
					//v.vibrate(15);
					if (mLightSwitch.isChecked()) {
						check();
					}
				}
				if (modeint == 1) {
					milliseconds = 1000;
					//v.vibrate(15);
					if (mLightSwitch.isChecked()) {
						solid = 0;
						check();
					}
				}
				if (modeint == 2) {
					milliseconds = 800;
					solid = 0;
					//v.vibrate(15);
					if (mLightSwitch.isChecked()) {
						check();
					}
				}
				if (modeint == 3) {
					milliseconds = 600;
					solid = 0;
					//v.vibrate(15);
					if (mLightSwitch.isChecked()) {
						check();
					}
				}
				if (modeint == 4) {
					milliseconds = 420;
					solid = 0;
					//v.vibrate(15);
					if (mLightSwitch.isChecked()) {
						check();
					}
				}
				if (modeint == 5) {
					milliseconds = 330;
					solid = 0;
					//v.vibrate(15);
					if (mLightSwitch.isChecked()) {
						check();
					}
				}
				if (modeint == 6) {
					milliseconds = 280;
					solid = 0;
					//v.vibrate(15);
					if (mLightSwitch.isChecked()) {
						check();
					}
				}
				if (modeint == 7) {
					milliseconds = 200;
					solid = 0;
					//v.vibrate(15);
					if (mLightSwitch.isChecked()) {
						check();
					}
				}
				if (modeint == 8) {
					milliseconds = 150;
					solid = 0;
					//v.vibrate(15);
					if (mLightSwitch.isChecked()) {
						check();
					}
				}
				if (modeint == 9) {
					milliseconds = 100;
					//v.vibrate(15);
					if (mLightSwitch.isChecked()) {
						solid = 0;
						check();
					}
				}
				if (modeint == 10) {
					milliseconds = 50;
					solid = 0;
					//v.vibrate(15);
					if (mLightSwitch.isChecked()) {
						check();
					}
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});
		// MODE FUNCTION

		// relative layout
		//layout = (RelativeLayout) findViewById(R.id.mainLayout);

		p = mCamera.getParameters();
		p.setFlashMode(Parameters.FLASH_MODE_OFF);
		mCamera.setParameters(p);

		if (mCamera == null) {
			Log.d(TAG, "mCamera is null.");
			AlertDialog.Builder builder = new AlertDialog.Builder(
					FlashlightActivity.this);
			builder.setMessage(R.string.not_supported);
			builder.setPositiveButton(R.string.exit,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					});
		}

		mLightSwitch = (ToggleButton) findViewById(R.id.light_switch);
		mLightSwitch
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							mCamera.startPreview();
							p = mCamera.getParameters();
							check();
							v.vibrate(15);
							// playSound();
							// layout.setBackgroundResource(R.drawable.radial_background);
							toggler = "ON";
						} else {
							mCamera.stopPreview();
							ON = "no";
							p = mCamera.getParameters();
							p.setFlashMode(Parameters.FLASH_MODE_OFF);
							mCamera.setParameters(p);
							v.vibrate(15);
							// playSound();
							// layout.setBackgroundResource(R.drawable.bg);
							toggler = "OFF";
						}
					}
				});

		mLightSwitch.setChecked(false);

		mAdView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();

		// Load the adView with the ad request.
		mAdView.loadAd(adRequest);
	}

	private void checkColourScheme() {

		if (scheme.equals(random)) {

			Random r = new Random();
			int i1 = r.nextInt(max - min + 1) + min;
			colorscheme = i1;
			radRandom.setChecked(true);
			radBlue.setChecked(false);
			radGreen.setChecked(false);
			radOrange.setChecked(false);
			radRed.setChecked(false);
			radTurq.setChecked(false);
			radMidnight.setChecked(false);
			radRasta.setChecked(false);
			radBeach.setChecked(false);
			radCamo.setChecked(false);
			radCarbon.setChecked(false);
			if (colorscheme == 1) {
				topleft.setBackgroundResource(R.color.light_blue);
				bottomleft.setBackgroundResource(R.color.medium_blue);
				right.setBackgroundResource(R.color.dark_blue);
			}
			if (colorscheme == 2) {
				topleft.setBackgroundResource(R.color.light_green);
				bottomleft.setBackgroundResource(R.color.medium_green);
				right.setBackgroundResource(R.color.dark_green);
			}
			if (colorscheme == 3) {
				topleft.setBackgroundResource(R.color.light_orange);
				bottomleft.setBackgroundResource(R.color.medium_orange);
				right.setBackgroundResource(R.color.dark_orange);
			}
			if (colorscheme == 4) {
				topleft.setBackgroundResource(R.color.light_red);
				bottomleft.setBackgroundResource(R.color.medium_red);
				right.setBackgroundResource(R.color.dark_red);
			}
			if (colorscheme == 5) {
				topleft.setBackgroundResource(R.color.light_turq);
				bottomleft.setBackgroundResource(R.color.medium_turq);
				right.setBackgroundResource(R.color.dark_turq);
			}
			if (colorscheme == 6) {
				topleft.setBackgroundResource(R.color.light_midnight);
				bottomleft.setBackgroundResource(R.color.medium_midnight);
				right.setBackgroundResource(R.color.dark_midnight);
			}
			if (colorscheme == 7) {
				topleft.setBackgroundResource(R.color.rasta_red);
				bottomleft.setBackgroundResource(R.color.rasta_yellow);
				right.setBackgroundResource(R.color.rasta_green);
			}
			if (colorscheme == 8) {
				topleft.setBackgroundResource(R.color.beach_darkblue);
				bottomleft.setBackgroundResource(R.color.beach_lightblue);
				right.setBackgroundResource(R.color.beach_sand);
			}
			if (colorscheme == 9) {
				topleft.setBackgroundResource(R.color.camo_green);
				bottomleft.setBackgroundResource(R.color.camo_dark);
				right.setBackgroundResource(R.color.camo_brown);
			}
			if (colorscheme == 10) {
				topleft.setBackgroundResource(R.color.carbon_medium);
				bottomleft.setBackgroundResource(R.color.carbon_light);
				right.setBackgroundResource(R.color.carbon_dark);
			}
		}

		// Space

		if (scheme.equals(blue)) {
			radRandom.setChecked(false);
			radBlue.setChecked(true);
			radGreen.setChecked(false);
			radOrange.setChecked(false);
			radRed.setChecked(false);
			radTurq.setChecked(false);
			radMidnight.setChecked(false);
			radRasta.setChecked(false);
			radBeach.setChecked(false);
			radCamo.setChecked(false);
			radCarbon.setChecked(false);
			topleft.setBackgroundResource(R.color.light_blue);
			bottomleft.setBackgroundResource(R.color.medium_blue);
			right.setBackgroundResource(R.color.dark_blue);
		}

		if (scheme.equals(green)) {
			radRandom.setChecked(false);
			radBlue.setChecked(false);
			radGreen.setChecked(true);
			radOrange.setChecked(false);
			radRed.setChecked(false);
			radTurq.setChecked(false);
			radMidnight.setChecked(false);
			radRasta.setChecked(false);
			radBeach.setChecked(false);
			radCamo.setChecked(false);
			radCarbon.setChecked(false);
			topleft.setBackgroundResource(R.color.light_green);
			bottomleft.setBackgroundResource(R.color.medium_green);
			right.setBackgroundResource(R.color.dark_green);
		}
		if (scheme.equals(orange)) {
			radRandom.setChecked(false);
			radBlue.setChecked(false);
			radGreen.setChecked(false);
			radOrange.setChecked(true);
			radRed.setChecked(false);
			radTurq.setChecked(false);
			radMidnight.setChecked(false);
			radRasta.setChecked(false);
			radBeach.setChecked(false);
			radCamo.setChecked(false);
			radCarbon.setChecked(false);
			topleft.setBackgroundResource(R.color.light_orange);
			bottomleft.setBackgroundResource(R.color.medium_orange);
			right.setBackgroundResource(R.color.dark_orange);
		}

		if (scheme.equals(red)) {
			radRandom.setChecked(false);
			radBlue.setChecked(false);
			radGreen.setChecked(false);
			radOrange.setChecked(false);
			radRed.setChecked(true);
			radTurq.setChecked(false);
			radMidnight.setChecked(false);
			radRasta.setChecked(false);
			radBeach.setChecked(false);
			radCamo.setChecked(false);
			radCarbon.setChecked(false);
			topleft.setBackgroundResource(R.color.light_red);
			bottomleft.setBackgroundResource(R.color.medium_red);
			right.setBackgroundResource(R.color.dark_red);
		}
		if (scheme.equals(turquoise)) {
			radRandom.setChecked(false);
			radBlue.setChecked(false);
			radGreen.setChecked(false);
			radOrange.setChecked(false);
			radRed.setChecked(false);
			radTurq.setChecked(true);
			radMidnight.setChecked(false);
			radRasta.setChecked(false);
			radBeach.setChecked(false);
			radCamo.setChecked(false);
			radCarbon.setChecked(false);
			topleft.setBackgroundResource(R.color.light_turq);
			bottomleft.setBackgroundResource(R.color.medium_turq);
			right.setBackgroundResource(R.color.dark_turq);
		}

		if (scheme.equals(midnight)) {
			radRandom.setChecked(false);
			radBlue.setChecked(false);
			radGreen.setChecked(false);
			radOrange.setChecked(false);
			radRed.setChecked(false);
			radTurq.setChecked(false);
			radMidnight.setChecked(true);
			radRasta.setChecked(false);
			radBeach.setChecked(false);
			radCamo.setChecked(false);
			radCarbon.setChecked(false);
			topleft.setBackgroundResource(R.color.light_midnight);
			bottomleft.setBackgroundResource(R.color.medium_midnight);
			right.setBackgroundResource(R.color.dark_midnight);
		}
		if (scheme.equals(rasta)) {
			radRandom.setChecked(false);
			radBlue.setChecked(false);
			radGreen.setChecked(false);
			radOrange.setChecked(false);
			radRed.setChecked(false);
			radTurq.setChecked(false);
			radMidnight.setChecked(false);
			radRasta.setChecked(true);
			radBeach.setChecked(false);
			radCamo.setChecked(false);
			radCarbon.setChecked(false);
			topleft.setBackgroundResource(R.color.rasta_red);
			bottomleft.setBackgroundResource(R.color.rasta_yellow);
			right.setBackgroundResource(R.color.rasta_green);
		}

		if (scheme.equals(beach)) {
			radRandom.setChecked(false);
			radBlue.setChecked(false);
			radGreen.setChecked(false);
			radOrange.setChecked(false);
			radRed.setChecked(false);
			radTurq.setChecked(false);
			radMidnight.setChecked(false);
			radRasta.setChecked(false);
			radBeach.setChecked(true);
			radCamo.setChecked(false);
			radCarbon.setChecked(false);
			topleft.setBackgroundResource(R.color.beach_darkblue);
			bottomleft.setBackgroundResource(R.color.beach_lightblue);
			right.setBackgroundResource(R.color.beach_sand);
		}
		if (scheme.equals(camo)) {
			radRandom.setChecked(false);
			radBlue.setChecked(false);
			radGreen.setChecked(false);
			radOrange.setChecked(false);
			radRed.setChecked(false);
			radTurq.setChecked(false);
			radMidnight.setChecked(false);
			radRasta.setChecked(false);
			radBeach.setChecked(false);
			radCamo.setChecked(true);
			radCarbon.setChecked(false);
			topleft.setBackgroundResource(R.color.camo_green);
			bottomleft.setBackgroundResource(R.color.camo_dark);
			right.setBackgroundResource(R.color.camo_brown);
		}

		if (scheme.equals(carbon)) {
			radRandom.setChecked(false);
			radBlue.setChecked(false);
			radGreen.setChecked(false);
			radOrange.setChecked(false);
			radRed.setChecked(false);
			radTurq.setChecked(false);
			radMidnight.setChecked(false);
			radRasta.setChecked(false);
			radBeach.setChecked(false);
			radCamo.setChecked(false);
			radCarbon.setChecked(true);
			topleft.setBackgroundResource(R.color.carbon_medium);
			bottomleft.setBackgroundResource(R.color.carbon_light);
			right.setBackgroundResource(R.color.carbon_dark);
		}

		topleft.invalidate();
		bottomleft.invalidate();
		right.invalidate();
	}

	private void playSound() {
		if (toggler == on) {
			mp = MediaPlayer.create(FlashlightActivity.this,
					R.raw.light_switch_off);
		} else {
			mp = MediaPlayer.create(FlashlightActivity.this,
					R.raw.light_switch_on);
		}
		mp.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.release();
			}
		});
		mp.start();
	}

	@SuppressWarnings("deprecation")
	private void initCamera() {
		mSurfaceView = (SurfaceView) findViewById(R.id.surface_view);
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

	}

	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.release(); // release the camera for other applications
			mCamera = null;
		}
	}

	private void light() {
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// Do something after x milliseconds
				if (ON == "on") {
					p.setFlashMode(Parameters.FLASH_MODE_TORCH);
					mCamera.setParameters(p);
					dark();
				}
			}
		}, milliseconds);
	}

	private void dark() {
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// Do something after x milliseconds
				if (ON == "on") {
					p.setFlashMode(Parameters.FLASH_MODE_OFF);
					mCamera.setParameters(p);
					light();
				}
			}
		}, milliseconds);
	}

	private void continuous() {
		p.setFlashMode(Parameters.FLASH_MODE_TORCH);
		mCamera.setParameters(p);
	}

	private void check() {
		if (solid == 0) {
			ON = "on";
			light();
		}
		if (solid == 1) {
			ON = "off";
			continuous();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ON = "off";
		if (mCamera != null) {
			releaseCamera();
			mAdView.destroy();
			mCamera = null;
		}
	}

	@Override
	protected void onPause() {
		mLightSwitch.setChecked(false);
		mAdView.pause();
		ON = "off";
		super.onPause();
		Log.v("OnPause", "True");
		if (mCamera != null) {
			Log.v("OnPause", "Cam isn't null");
			mCamera.stopPreview();
			Log.v("OnPause", "CamPreview stopped");
			releaseCamera();
			Log.v("OnPause", "CamReleased");
			mCamera = null;
			Log.v("OnPause", "Cam is null");
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mAdView.resume();
		Log.v("OnResume", "True");
		if (mCamera != null) {
			Log.v("OnResume", "Camera is in use");
		} else {
			Log.v("OnResume", "Camera isn't in use");
			//layout = (RelativeLayout) findViewById(R.id.mainLayout);
			mCamera = Camera.open();
			Log.v("OnResume", "Camera has been opened");
			mCamera.startPreview();
			Log.v("OnResume", "CamPreview started");
			p = mCamera.getParameters();
			Log.v("OnResume", "Wrote parameters to variable p");
			// p.setFlashMode(Parameters.FLASH_MODE_TORCH);
			// Log.v("OnResume", "Set flashmode OFF");
			// mCamera.setParameters(p);
			// Log.v("OnResume", "set camera parameters");
			Log.d("Function", "surfaceChanged iniciado");
			if (mSurfaceHolder.getSurface() == null) {
				// preview surface does not exist
				return;
			}

			// stop preview before making changes
			try {
				mCamera.stopPreview();
			} catch (Exception e) {
				// ignore: tried to stop a non-existent preview
			}

			// set preview size and make any resize, rotate or
			// reformatting changes here

			// start preview with new settings
			try {
				mCamera.setPreviewDisplay(mSurfaceHolder);
				mCamera.startPreview();

			} catch (Exception e) {
				Log.d(TAG, "Error starting camera preview: " + e.getMessage());
			}
		}

	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// If your preview can change or rotate, take care of those events here.
		// Make sure to stop the preview before resizing or reformatting it.
		Log.d("Function", "surfaceChanged iniciado");
		if (mSurfaceHolder.getSurface() == null) {
			// preview surface does not exist
			return;
		}

		// stop preview before making changes
		try {
			mCamera.stopPreview();
		} catch (Exception e) {
			// ignore: tried to stop a non-existent preview
		}

		// set preview size and make any resize, rotate or
		// reformatting changes here

		// start preview with new settings
		try {
			mCamera.setPreviewDisplay(mSurfaceHolder);
			mCamera.startPreview();

		} catch (Exception e) {
			Log.d(TAG, "Error starting camera preview: " + e.getMessage());
		}
	}

	public void surfaceCreated(SurfaceHolder holder) {
		try {
			mCamera.setPreviewDisplay(holder);
		} catch (IOException e) {
			releaseCamera();
			Log.e(TAG, "Sigh", e);
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		releaseCamera();
	}

	private String getStoredScheme() {
		SharedPreferences prefs = getSharedPreferences("stored scheme",
				MODE_PRIVATE);
		scheme = prefs.getString("colour scheme", "Random");
		return null;
	}

	private void storeScheme() {
		SharedPreferences prefs = getSharedPreferences("stored scheme",
				MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("colour scheme", scheme);
		editor.commit();
	}

}
