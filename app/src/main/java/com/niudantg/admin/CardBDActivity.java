package com.niudantg.admin;

import java.io.IOException;
import java.util.Map;
import java.util.Vector;
import ktx.pojo.domain.EquipmentInfo;
import niudantg.BaseActivity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
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
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.mining.app.zxing.camera.CameraManager;
import com.mining.app.zxing.decoding.CaptureActivityHandler;
import com.mining.app.zxing.decoding.InactivityTimer;
import com.mining.app.zxing.view.ViewfinderView;
import com.niudantg.http.CFHttpClient;
import com.niudantg.http.CFHttpMsg;
import com.niudantg.util.ToastUtil;

/**
 * Initial the camera
 * 
 * @author Ryan.Tang
 */
public class CardBDActivity extends BaseActivity implements Callback, CFHttpMsg {

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
	//
	private Intent resultIntent;
	//
	private EquipmentInfo einfo = new EquipmentInfo();
	//
	private Intent intent;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture);
		// ViewUtil.addTopView(getApplicationContext(), this,
		// R.string.scan_card);
		resultIntent = new Intent();
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);

		ImageView mButtonBack = (ImageView) findViewById(R.id.button_back);
		mButtonBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CardBDActivity.this.finish();

			}
		});
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		intent = new Intent();
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
	 * ����ɨ����
	 * 
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		// playBeepSoundAndVibrate();
		String resultString = result.getText();
		// System.out.println("---resultString =" + resultString);
		if (resultString.equals("")) {
			ToastUtil.showMessages(CardBDActivity.this, "扫描失败，请重新扫描~");
			finish();
		} else {
			String a = resultString.substring(0, 27);
			if (a.equals("http://nd.kuailetongyao.com")) {
				String[] Codes = resultString.split("/download/");
				UpDeviceIDId(Codes[1]);
			} else {
				ToastUtil.showMessages(CardBDActivity.this, "请扫描设备二维码~");
				finish();
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

	// 上传设备编码
	private void UpDeviceIDId(String Code) {
		mProgressDialog.setTitle("加载中");
		mProgressDialog.show();
		Message m = new Message();
		m.what = 9010;
		CFHttpClient.s().get(
				"?MsgType=9010&mobileType=android&DeviceID=" + Code, this, m,
				true);

	}

	public void httpMsg(Message m) {
		mProgressDialog.cancel();
		if (m.arg1 == 1 || m.arg1 == -3) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) m.obj;
			einfo = (EquipmentInfo) map.get("eqinfo");
			intent.setClass(CardBDActivity.this,
					UpdateCardBDStatusActivity.class);
			intent.putExtra("ID", einfo.id);
			intent.putExtra("DeviceID", einfo.DeviceID);
			intent.putExtra("MachineID", einfo.MachineID);
			intent.putExtra("Code", einfo.Code);
			startActivity(intent);
			finish();

		} else if (m.arg1 == -2) {
			mProgressDialog.cancel();
			Toast.makeText(CardBDActivity.this, "未检索到该设备~", Toast.LENGTH_SHORT)
					.show();
		} else {
			mProgressDialog.cancel();
			Toast.makeText(CardBDActivity.this, "检索失败，请重试~", Toast.LENGTH_SHORT)
					.show();
		}
	}
}