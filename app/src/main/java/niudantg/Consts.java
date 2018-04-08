package niudantg;

import java.util.ArrayList;

import ktx.pojo.domain.Customer;
import ktx.pojo.domain.CustomerType;
import ktx.pojo.domain.ExtensionStaff;
import ktx.pojo.domain.RegionInfo;
import ktx.pojo.domain.Version;

public final class Consts {
	private Consts() {
	}

	// 在七牛绑定的对应bucket的域名. 默认是bucket.qiniudn.com
	public static String domain = "7ktsa3.com1.z0.glb.clouddn.com";
	//
	public static String api_key = "aa914648d183d06c";
	public static String secret_key = "ad54d59f42bd896068bb5c2db05bad79";

	public static String BasePath = "";
	public static final String BasePath1 = "/yyctg";
	public static final String BasePath2 = "/yyctg/img";

	// 记录验证时间
	public static int yzm_time = 60;
	// 记录验证码退出时间
	public static long yzm_currenttime = 0;
	//
	public static ExtensionStaff user = new ExtensionStaff();
	//
	public static Version version = new Version();
	//
	public static String DeviceID = "";

	//
	public static RegionInfo Provinceinfo = new RegionInfo();
	//
	public static RegionInfo Cityinfo = new RegionInfo();
	//
	public static RegionInfo Regioninfo = new RegionInfo();
	//
	public static boolean region_tf = false;
	//
	public static boolean tf_login = false;

	//
	public static Customer customer = new Customer();
	//
	public static boolean addcar_tf = false;
	//
	public static ArrayList<CustomerType> customertypelist = new ArrayList<CustomerType>();
	//
	public static int CustomerTypeId = -1;
	//
	public static boolean ctype_tf = false;
	//
	public static boolean updateprice_tf = false;
	//
	public static boolean newef_tf = false;

}
