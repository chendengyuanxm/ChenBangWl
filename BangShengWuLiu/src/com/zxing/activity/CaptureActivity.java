package com.zxing.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.devin.bangsheng.R;
import com.devin.bangsheng.adapter.ScanListAdapter;
import com.devin.bangsheng.ui.BaseActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.zxing.camera.CameraManager;
import com.zxing.decoding.CaptureActivityHandler;
import com.zxing.decoding.InactivityTimer;
import com.zxing.view.ViewfinderView;

public class CaptureActivity extends BaseActivity implements Callback {

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	private boolean openLight;
	private boolean batchScan;	//批量扫描
	private ListView lv;
	private ScanListAdapter adapter;
	private ArrayList<String> orders = new ArrayList<String>();
	private Button batchSubmitBtn;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera);
		initTopbar("扫描");
		//ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		final Button batchScanBtn = (Button) findViewById(R.id.btn_batch_scan);
		batchScanBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				v.setClickable(false);
				batchScan = true;
				orders.clear();
				
				int marginTop = CameraManager.get().getFramingRect().bottom;
				int marginLeft = CameraManager.get().getFramingRect().left;
				LayoutParams lp = (LayoutParams) lv.getLayoutParams();
				lp.topMargin = marginTop + 30;
				lp.leftMargin = marginLeft;
				lp.rightMargin = marginLeft;
				if(batchScan) {
					Drawable drawable = getResources().getDrawable(R.drawable.img_scan_pressed);
					batchScanBtn.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
				}else {
					Drawable drawable = getResources().getDrawable(R.drawable.img_scan);
					batchScanBtn.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
				}
			}
		});
		final Button openLightBtn = (Button) findViewById(R.id.btn_open_light);
		openLightBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!openLight) {
					CameraManager.get().openLight();
					openLight = true;
					openLightBtn.setText("关闭闪光灯");
					Drawable drawable = getResources().getDrawable(R.drawable.img_flash_pressed);
					openLightBtn.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
				}else {
					CameraManager.get().closeLight();
					openLight = false;
					openLightBtn.setText("打开闪光灯");
					Drawable drawable = getResources().getDrawable(R.drawable.img_flash);
					openLightBtn.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
				}
			}
		});
		batchSubmitBtn = (Button) findViewById(R.id.btn_submit);
		batchSubmitBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent resultIntent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable("orders", orders);
				resultIntent.putExtras(bundle);
				setResult(RESULT_OK, resultIntent);
				finish();
			}
		});
		lv = (ListView) findViewById(R.id.lv);
		adapter = new ScanListAdapter(this, orders);
		lv.setAdapter(adapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}
	
	/**
	 * Handler scan result
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		String resultString = result.getText();
		//FIXME
		if (resultString.equals("")) {
//			Toast.makeText(CaptureActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
		}else {
//			Toast.makeText(CaptureActivity.this, "Scan success!", Toast.LENGTH_SHORT).show();
			if(!batchScan) {
				Intent resultIntent = new Intent();
				Bundle bundle = new Bundle();
//				bundle.putString("result", resultString);
				orders.add(resultString);
				bundle.putSerializable("orders", orders);
				resultIntent.putExtras(bundle);
				this.setResult(RESULT_OK, resultIntent);
				CaptureActivity.this.finish();
			}else {
				if(!orders.contains(resultString)) {
					orders.add(resultString);
					adapter.notifyDataSetChanged();
					lv.setSelection(orders.size()-1);
				}
				if(orders.size() >= 1) batchSubmitBtn.setVisibility(View.VISIBLE);
				else batchSubmitBtn.setVisibility(View.GONE);
				handler.sendEmptyMessageDelayed(R.id.restart_preview, 1000);
			}
		}
	}
	
	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	@Override
	public void onResponse(Message msg) {
		
	}
}