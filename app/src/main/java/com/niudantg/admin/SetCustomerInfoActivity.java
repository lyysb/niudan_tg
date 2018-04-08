package com.niudantg.admin;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

import niudantg.BaseActivity;
import niudantg.Consts;
import niudantg.service.QiniuResult;
import niudantg.service.QiniuSerVice;
import niudantg.service.QiniuSerVice.QiniuBinder;

import ktx.pojo.domain.Customer;
import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.niudantg.http.CFHttpClient;
import com.niudantg.http.CFHttpMsg;
import com.niudantg.util.MemorySize;
import com.niudantg.util.ToastUtil;
import com.niudantg.util.Utils;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.niudantg.admin.R;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

@ContentView(R.layout.layout_set_customerinfo)
public class SetCustomerInfoActivity extends BaseActivity implements CFHttpMsg,
		TencentLocationListener {
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int PICK_IMAGE_ACTIVITY_REQUEST_CODE = 200;
	private static final String IMAGE_FILE_LOCATION1 = Consts.BasePath
			+ Consts.BasePath2;
	private static final String OutImageFile = Consts.BasePath
			+ Consts.BasePath2 + "/outimage.jpg";

	//
	private QiniuBinder binder;
	//
	private String token;
	// 图片区分
	private int Img_type = 1;
	//
	private String Img_Url1;
	//
	private String Img_Url2;
	//
	@ViewInject(R.id.btn_img1)
	private ImageView btn_img1;
	//
	@ViewInject(R.id.btn_img2)
	private ImageView btn_img2;
	//
	private String Rewark;

	/***********************************************************/
	// 返回按钮
	@ViewInject(R.id.btn_back)
	private ImageView btn_back;

	//
	@ViewInject(R.id.ed_name)
	private EditText ed_name;
	//
	@ViewInject(R.id.ed_people)
	private EditText ed_people;
	//
	@ViewInject(R.id.ed_phone)
	private EditText ed_phone;
	//
	@ViewInject(R.id.btn_region)
	private RelativeLayout btn_region;
	//
	@ViewInject(R.id.t_region)
	private TextView t_region;
	//
	@ViewInject(R.id.ed_address)
	private TextView ed_address;

	//
	@ViewInject(R.id.t_northlatitude)
	private TextView t_northlatitude;
	//
	@ViewInject(R.id.t_eastlongitude)
	private TextView t_eastlongitude;

	//
	@ViewInject(R.id.t_address)
	private TextView t_address;
	//
	@ViewInject(R.id.btn_up)
	private RelativeLayout btn_up;
	//
	@ViewInject(R.id.t_up)
	private TextView t_up;
	//
	@ViewInject(R.id.btn_location)
	private RelativeLayout btn_location;

	//
	@ViewInject(R.id.btn_to_up2)
	private TextView btn_to_up2;

	//
	@ViewInject(R.id.ed_RentCash)
	private EditText ed_RentCash;
	//
	@ViewInject(R.id.t_type1)
	private TextView t_type1;
	//
	@ViewInject(R.id.btn_nei)
	private Button btn_nei;
	//
	@ViewInject(R.id.btn_wai)
	private Button btn_wai;

	//
	@ViewInject(R.id.t_typename)
	private TextView t_typename;
	//
	@ViewInject(R.id.btn_type)
	private RelativeLayout btn_type;
	//
	@ViewInject(R.id.ed_remark)
	private EditText ed_remark;
	//
	@ViewInject(R.id.ed_carinfo)
	private EditText ed_carinfo;

	//
	private Customer customer = new Customer();
	//
	private Intent intent;
	//
	private String Name;
	private String ContactName;
	private String Region;
	private String Address;

	private String Phone;
	//
	private double East_Longitude;
	//
	private double North_atitude;
	// 省
	private String province;
	private String city; // 市
	private String district; // 区
	private String town; // 镇
	private String village;// 村
	private String street;// 街道
	private String streetNo; // 门号

	//
	private int Type1 = -1;
	private String RentCash;
	//
	private String CarInfo;

	//
	private TencentLocationManager mLocationManager;

	protected void onCreate(Bundle savedInstanceState) {
		ViewUtils.inject(this);
		super.onCreate(savedInstanceState);
		getqiniutoken();
		init();
	}

	private void init() {
		intent = new Intent();
		ed_RentCash.setText("0");

		mLocationManager = TencentLocationManager.getInstance(this);
		mLocationManager
				.requestLocationUpdates(
						TencentLocationRequest
								.create()
								.setRequestLevel(
										TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA)
								.setInterval(500).setAllowDirection(true),
						this, getMainLooper());
	}

	// 下方控制栏的点击事件
	@OnClick({ R.id.btn_back, R.id.btn_up, R.id.btn_region, R.id.btn_location,
			R.id.btn_to_up2, R.id.btn_type, R.id.btn_nei, R.id.btn_wai,
			R.id.btn_img1, R.id.btn_img2 })
	public void viewonclick(View v) {
		switch (v.getId()) {
		//
		case R.id.btn_back:
			finish();
			break;
		//
		case R.id.btn_up:
			setData();
			break;
		//
		case R.id.btn_region:
			intent.setClass(SetCustomerInfoActivity.this,
					ProvinceListActivity.class);
			intent.putExtra("Type",1);
			startActivity(intent);
			break;
		//
		case R.id.btn_to_up2:
			intent.setClass(SetCustomerInfoActivity.this,
					SetCustomerInfoActivity2.class);
			startActivity(intent);
			break;
		//
		case R.id.btn_location:
			setLocation();
			break;
		//
		case R.id.btn_type:
			intent.setClass(SetCustomerInfoActivity.this,
					CustomerTypeListActivity.class);
			startActivity(intent);
			break;
		//
		case R.id.btn_nei:
			Type1 = 2;
			t_type1.setText("室内");
			break;
		//
		case R.id.btn_wai:
			Type1 = 1;
			t_type1.setText("室外");
			break;
		//
		case R.id.btn_img1:
			Img_type = 1;
			setphoto();
			break;
		//
		case R.id.btn_img2:
			Img_type = 2;
			setphoto();
			break;

		default:
			break;
		}
	}

	private void setLocation() {
		mProgressDialog.setTitle("定位中...");
		mProgressDialog.show();
		mLocationManager = TencentLocationManager.getInstance(this);
		mLocationManager
				.requestLocationUpdates(
						TencentLocationRequest
								.create()
								.setRequestLevel(
										TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA)
								.setInterval(500).setAllowDirection(true),
						this, getMainLooper());
	}

	private void setData() {
		Name = ed_name.getText().toString();
		ContactName = ed_people.getText().toString();
		Address = ed_address.getText().toString();
		Phone = ed_phone.getText().toString();
		RentCash = ed_RentCash.getText().toString();
		Rewark = ed_remark.getText().toString();

		CarInfo = ed_carinfo.getText().toString();

		Name = Utils.setSrule(Name);
		ContactName = Utils.setSrule(ContactName);
		Address = Utils.setSrule(Address);
		Phone = Utils.setSrule(Phone);
		RentCash = Utils.setSrule(RentCash);

		Rewark = Utils.setSrule(Rewark);
		CarInfo = Utils.setSrule(CarInfo);

		if (Phone == null || Phone.equals("")) {
			ToastUtil.showMessages(SetCustomerInfoActivity.this, "联系电话不能为空~");
			return;
		}
		if (Phone.length() != 11) {
			ToastUtil.showMessages(SetCustomerInfoActivity.this,
					"联系电话必须是11位手机号码~");
			return;
		}
		if (Name == null || Name.equals("")) {
			ToastUtil.showMessages(SetCustomerInfoActivity.this, "店铺名称不能为空~");
			return;
		}
		if (ContactName == null || ContactName.equals("")) {
			ToastUtil.showMessages(SetCustomerInfoActivity.this, "联系人不能为空~");
			return;
		}
		if (Region == null || Region.equals("")) {
			ToastUtil.showMessages(SetCustomerInfoActivity.this, "安装地区不能为空~");
			return;
		}
		if (Address == null || Address.equals("")) {
			ToastUtil.showMessages(SetCustomerInfoActivity.this, "详细地址不能为空~");
			return;
		}
		if (North_atitude == 0) {
			ToastUtil.showMessages(SetCustomerInfoActivity.this, "定位失败请重新定位~");
			return;
		}
		if (Type1 == -1) {
			ToastUtil.showMessages(SetCustomerInfoActivity.this, "还未选择场地类型~");
			return;
		}
		if (Consts.CustomerTypeId == -1) {
			ToastUtil.showMessages(SetCustomerInfoActivity.this, "还未选择店铺类型~");
			return;
		}
		if (CarInfo == null || CarInfo.equals("")) {
			ToastUtil
					.showMessages(SetCustomerInfoActivity.this, "设备台数及类型不能为空~");
			return;
		}

		if (RentCash == null || RentCash.equals("")) {
			RentCash = "0";
		}

		if (customer == null) {
			customer = new Customer();
		}
		customer.Phone = Phone;
		customer.North_atitude = North_atitude;
		customer.East_Longitude = East_Longitude;
		customer.Name = Name;
		customer.ContactName = ContactName;
		customer.ProvinceId = Consts.Provinceinfo.id;
		customer.ProvinceName = Consts.Provinceinfo.Name;
		customer.CityId = Consts.Cityinfo.id;
		customer.CityName = Consts.Cityinfo.Name;
		customer.RegionId = Consts.Regioninfo.RegionId;
		customer.RegionName = Consts.Regioninfo.Name;
		customer.Address = String.format("%s%s%s%s %s", town, village, street,
				streetNo, Name);
		customer.Address = customer.Address.replaceAll("Unknown", "");
		customer.OpenBankName = Name;
		customer.BankCode = "未设置";
		customer.BankHolder = "未设置";

		int rentcash = Integer.valueOf(RentCash);
		if (rentcash == 0) {
			customer.RentCash = 0;
			customer.RentType = 0;
		} else {
			customer.RentCash = rentcash;
			customer.RentType = 1;
		}
		customer.Type = Consts.customertypelist.get(Consts.CustomerTypeId).id;
		customer.Type1 = Type1;
		customer.Remark = Rewark;
		customer.Image01 = Img_Url1;
		customer.Image02 = Img_Url2;
		customer.CarInfo = CarInfo;

		UpdateCustomerInfo();
	}

	//
	private void UpdateCustomerInfo() {
		mProgressDialog.show();
		mProgressDialog.setTitle("提交中...");
		String str = URLEncoder.encode(JSON.toJSONString(customer));
		Message m = new Message();
		m.what = 9009;
		CFHttpClient.s().get(
				"?MsgType=9009&mobileType=android&customer=" + str + "&EId="
						+ Consts.user.id, this, m, true);

	}

	// 获取token
	private void getqiniutoken() {
		Message message = new Message();
		message.what = 8000;
		CFHttpClient.s().get(
				"?MsgType=8000&mobileType=android&DeviceID=123654", this,
				message, false);

	}

	public void httpMsg(Message m) {
		mProgressDialog.cancel();
		switch (m.what) {
		// token
		case 8000:
			if (m.arg1 == 1) {
				Map<String, Object> map = (Map<String, Object>) m.obj;
				token = (String) map.get("uptoken");
			}
			break;
		//
		case 9009:
			if (m.arg1 == 1) {
				ToastUtil.showMessages(SetCustomerInfoActivity.this, "信息上传成功~");
				finish();
			} else if (m.arg1 == -2) {
				ToastUtil.showMessages(SetCustomerInfoActivity.this,
						"手机号码已注册，无法重复注册~");
			} else {
				ToastUtil.showMessages(SetCustomerInfoActivity.this,
						"基本信息设置失败~");
			}
			break;

		default:
			break;
		}

	}

	protected void onResume() {
		super.onResume();
		if (Consts.region_tf) {
			Consts.region_tf = false;
			Region = String.format("%s %s %s", Consts.Provinceinfo.Name,
					Consts.Cityinfo.Name, Consts.Regioninfo.Name);
			if (Region != null && !Region.equals("")) {
				t_region.setText(Region);
			}
		}
		if (Consts.ctype_tf) {
			Consts.ctype_tf = false;
			if (Consts.CustomerTypeId != -1 && Consts.customertypelist != null
					&& Consts.customertypelist.size() != 0) {
				t_typename.setText(Consts.customertypelist
						.get(Consts.CustomerTypeId).TypeName);
			}
		}
	}

	@Override
	public void onLocationChanged(TencentLocation location, int error,
			String arg2) {
		mProgressDialog.cancel();
		if (TencentLocation.ERROR_OK == error) {
			// 定位成功
			North_atitude = location.getLatitude();
			East_Longitude = location.getLongitude();
			province = location.getProvince();
			city = location.getCity();
			district = location.getDistrict();
			town = location.getTown();
			village = location.getVillage();
			street = location.getStreet();
			streetNo = location.getStreetNo();
			String Address1 = String.format("%s%s%s%s%s%s%s",
					location.getProvince(), location.getCity(),
					location.getDistrict(), location.getTown(),
					location.getVillage(), location.getStreet(),
					location.getStreetNo());
			String Address2 = String.format("%s%s%s%s", location.getTown(),
					location.getVillage(), location.getStreet(),
					location.getStreetNo());
			Address1 = Address1.replaceAll("Unknown", "");
			Address2 = Address2.replaceAll("Unknown", "");
			t_address.setText(Address1);
			ed_address.setText(Address2);
			t_northlatitude.setText(String.format("%.5f", North_atitude));
			t_eastlongitude.setText(String.format("%.5f", East_Longitude));
			mLocationManager.removeUpdates(this);
		} else {
			// 定位失败
			ToastUtil.showMessages(SetCustomerInfoActivity.this, "无法获取当前位置");
		}

	}

	@Override
	public void onStatusUpdate(String arg0, int arg1, String arg2) {
		// TODO Auto-generated method stub

	}

	// 拍照
	private void setphoto() {
		Long w_memorysize = MemorySize
				.getAvailablefileMemorySize(Consts.BasePath) / (1024 * 1024);
		if (w_memorysize < 10) {
			ToastUtil.showMessages(SetCustomerInfoActivity.this, "手机存储空间不足~");
			return;
		}
		final AlertDialog dlg = new AlertDialog.Builder(
				SetCustomerInfoActivity.this).create();
		dlg.show();
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dlg.getWindow().getAttributes();
		lp.width = (int) (display.getWidth()); // 设置宽度
		dlg.getWindow().setAttributes(lp);

		Window window = dlg.getWindow();
		window.setGravity(Gravity.BOTTOM);
		window.setContentView(R.layout.dialog_get_img);
		// 为确认按钮添加事件,执行退出应用操作
		LinearLayout btn_photo_Cameras = (LinearLayout) window
				.findViewById(R.id.btn_photo_Cameras);

		btn_photo_Cameras.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				takePicture();
				dlg.cancel();
			}
		});
		LinearLayout btn_photo_album = (LinearLayout) window
				.findViewById(R.id.btn_photo_album);

		btn_photo_album.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				openAlbum();
				dlg.cancel();
			}
		});
		LinearLayout btn_photo_canel = (LinearLayout) window
				.findViewById(R.id.btn_photo_canel);
		btn_photo_canel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dlg.cancel();
			}
		});
	}

	// 打开本地相册
	public void openAlbum() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_PICK);
		this.startActivityForResult(intent, PICK_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	private static String picFileFullName;

	// 拍照
	public void takePicture() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			File outDir = new File(IMAGE_FILE_LOCATION1);
			if (!outDir.exists()) {
				outDir.mkdirs();
			}
			File outFile = new File(outDir, System.currentTimeMillis() + ".jpg");
			picFileFullName = outFile.getAbsolutePath();
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outFile));
			intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
			startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
		} else {
			ToastUtil.showMessages(SetCustomerInfoActivity.this, "请确认已经插入SD卡");
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Utils.compressPicture(picFileFullName, OutImageFile);
				setImageView(OutImageFile);
				mProgressDialog.setTitle("图片上传中");
				mProgressDialog.show();
				Intent startServiceIntent = new Intent(
						SetCustomerInfoActivity.this, QiniuSerVice.class);
				Bundle bundle1 = new Bundle();
				bundle1.putString("img", OutImageFile);
				bundle1.putString("token", token);
				startServiceIntent.putExtras(bundle1);
				this.startService(startServiceIntent);
				bindService(startServiceIntent, conn, this.BIND_AUTO_CREATE);

				//
			} else if (resultCode == RESULT_CANCELED) {
				// 用户取消了图像捕获
			} else {
				ToastUtil.showMessages(SetCustomerInfoActivity.this, "拍照失败");
			}
		} else if (requestCode == PICK_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				if (uri != null) {
					String realPath = getRealPathFromURI(uri);
					Utils.compressPicture(realPath, OutImageFile);
					mProgressDialog.setTitle("图片上传中");
					mProgressDialog.show();
					Intent startServiceIntent = new Intent(
							SetCustomerInfoActivity.this, QiniuSerVice.class);
					Bundle bundle1 = new Bundle();
					bundle1.putString("img", OutImageFile);
					bundle1.putString("token", token);
					startServiceIntent.putExtras(bundle1);
					this.startService(startServiceIntent);
					bindService(startServiceIntent, conn, this.BIND_AUTO_CREATE);
					setImageView(OutImageFile);
				} else {
					ToastUtil.showMessages(SetCustomerInfoActivity.this,
							"从相册获取图片失败");
				}
			}
		}
	}

	//
	private void setImageView(String img_file) {

		if (img_file != null && !img_file.equals("")) {
			Bitmap bmp = BitmapFactory.decodeFile(img_file);
			int degree = readPictureDegree(img_file);
			if (degree <= 0) {
				if (Img_type == 1) {
					btn_img1.setImageBitmap(bmp);
				} else if (Img_type == 2) {
					btn_img2.setImageBitmap(bmp);
				}
			} else {
				// 创建操作图片是用的matrix对象
				Matrix matrix = new Matrix();
				// 旋转图片动作
				matrix.postRotate(degree);
				// 创建新图片
				Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0,
						bmp.getWidth(), bmp.getHeight(), matrix, true);
				if (Img_type == 1) {
					btn_img1.setImageBitmap(resizedBitmap);
				} else if (Img_type == 2) {
					btn_img2.setImageBitmap(resizedBitmap);
				}
			}
		}

	}

	/**
	 * 
	 * 照片路径
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	public String getRealPathFromURI(Uri contentUri) {
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = this.managedQuery(contentUri, proj, null, null,
					null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} catch (Exception e) {
			return contentUri.getPath();
		}
	}

	ServiceConnection conn = new ServiceConnection() {
		public void onServiceDisconnected(ComponentName name) {
		}

		public void onServiceConnected(ComponentName name, IBinder service) {
			binder = (QiniuBinder) service;
			binder.addupdishback(qnback, 1);
		}

	};

	private QiniuResult qnback = new QiniuResult() {
		public void OnQiniuResult(String img, int status) {
			mProgressDialog.cancel();
			if (status == 1) {
				ToastUtil.showMessages(SetCustomerInfoActivity.this, "图片上传成功~");
				if (Img_type == 1) {
					Img_Url1 = img;
				} else if (Img_type == 2) {
					Img_Url2 = img;
				}
			} else {
				ToastUtil.showMessages(SetCustomerInfoActivity.this,
						"图片上传失败请重试~ ");
			}
		}
	};

}
