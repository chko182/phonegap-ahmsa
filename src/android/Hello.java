package com.example.plugin;

import java.util.TimeZone; //device
import org.apache.cordova.CordovaWebView; //device

import org.apache.cordova.CallbackContext; //startapp
import org.apache.cordova.CordovaPlugin; //startapp
import org.apache.cordova.CordovaInterface; //device

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONObject;	//startapp
import org.json.JSONException;

import java.net.InetAddress;   
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.content.Context; //startapp
import android.content.Intent;  //startapp
import android.content.ComponentName; //startapp
import android.content.pm.PackageManager; //startapp
import android.content.pm.PackageInfo; //startapp
import java.util.Iterator; //startapp
import android.net.Uri; //startapp

import android.provider.Settings; //device

public class Hello extends CordovaPlugin {
	public static final String TAG = "Device";

    public static String platform;                            // Device OS
    public static String uuid;                                // Device UUID

    private static final String ANDROID_PLATFORM = "Android";
    private static final String AMAZON_PLATFORM = "amazon-fireos";
    private static final String AMAZON_DEVICE = "Amazon";
	
    //@Override
	
	/**
     * Constructor.
     */
	public Hello() { }
	
	//DEVICE
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        Hello.uuid = getUuid();
    }
	
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {

        if (action.equals("greet")) {

            String ip = "";
			String ipvpn = "";
			String nom_conexion = "Falso";
			//String nom_conexion = "";
			String ide_conexion = "";
			Integer indiceenc = 0;
			
			try {
				Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface.getNetworkInterfaces();
				while(enumNetworkInterfaces.hasMoreElements()){
					NetworkInterface networkInterface =
							enumNetworkInterfaces.nextElement();
					Enumeration<InetAddress> enumInetAddress =
							networkInterface.getInetAddresses();
					
					//Identificamos los nombres de la interface de red.
					//nom_conexion += "Red: " + networkInterface.getDisplayName();
					ide_conexion = "(" + networkInterface.getDisplayName() + ")";
					
					//Revisa si se esta ejecutando el controlador TUN (VPN)
					indiceenc = ide_conexion.indexOf("tun0");
					if (indiceenc == -1) {
						indiceenc = ide_conexion.indexOf("tun1");
					} else {
						nom_conexion = "Verdadero";
						break;
					}
					if (indiceenc == -1) {
						indiceenc = ide_conexion.indexOf("tun2");
					} else {
						nom_conexion = "Verdadero";
						break;
					}		
					if (indiceenc == -1) {
						
					} else {
						nom_conexion = "Verdadero";
						break;
					}
										
					while(enumInetAddress.hasMoreElements()){
						InetAddress inetAddress = enumInetAddress.nextElement();

						String ipAddress = "";
						if(inetAddress.isLoopbackAddress()){
							ipAddress = "LoopbackAddress: ";
						}else if(inetAddress.isSiteLocalAddress()){
							ipAddress = "SiteLocalAddress: ";
						}else if(inetAddress.isLinkLocalAddress()){
							ipAddress = "LinkLocalAddress: ";
						}else if(inetAddress.isMulticastAddress()){
							ipAddress = "MulticastAddress: ";
						}
						ip += ipAddress + inetAddress.getHostAddress() + "\n";
						
						if(ipAddress == "SiteLocalAddress: ") {
							ipvpn += ipAddress + inetAddress.getHostAddress() + "\n"; 
							//nom_conexion += " = " + inetAddress.getHostAddress();
						} 

					}
					
					//nom_conexion += "\n";
				}

			} catch (SocketException e) {
				ip = "Something Wrong! ";
			}
					
            String name = data.getString(0);
            String message = nom_conexion; //ipvpn;
            callbackContext.success(message);

            return true;

        } else {
            
            if (action.equals("start")) {
				this.start(data, callbackContext);
				return true;
			} else {
				
				if (action.equals("getDeviceInfo")) {
										
					String message = "Nombre: " + this.getProductName() + "\n";
					message += "UUID: " + this.getUuid() + "\n";
					/*message += "|" + this.getModel();
					message += "|" + this.getManufacturer();
					message += "|" + this.getSerialNumber();
					message += "|" + this.getOSVersion();*/
					callbackContext.success(message);
					
					return true;
					
				} else {
					
					if (action.equals("getPlataforma")) {
						
						String message = "Android";
						callbackContext.success(message);
						
						return true;
						
					} else {
						
						return false;
						
					}
				}
			}		
        }
    }
	
	//--------------------------------------------------------------------------
    // LOCAL METHODS
    //--------------------------------------------------------------------------
    /**
     * startApp
     */
    public void start(JSONArray data, CallbackContext callbackContext) {
		
		Intent LaunchIntent;
		
		String com_name = null;
		
		String activity = null;
		String spackage = null;
		String intetype = null;
		String intenuri = null;
		
		try {
			if (data.get(0) instanceof JSONArray) {
				com_name = data.getJSONArray(0).getString(0);
				activity = data.getJSONArray(0).getString(1);
				
				if(data.getJSONArray(0).length() > 2) {
					spackage = data.getJSONArray(0).getString(2);
				}
				
				if(data.getJSONArray(0).length() > 3) {
					intetype = data.getJSONArray(0).getString(3);
				}
				
				if(data.getJSONArray(0).length() > 4) {
					intenuri = data.getJSONArray(0).getString(4);
				}
			}
			else {
				com_name = data.getString(0);
			}
		
			/**
			 * call activity
			 */
			if(activity != null) {
				if(com_name.equals("action")) {
					/**
					 * . < 0: VIEW
					 * . >= 0: android.intent.action.VIEW
					 */
					if(activity.indexOf(".") < 0) {
						activity = "android.intent.action." + activity;
					}
					
					// if uri exists
					if(intenuri != null) {
						LaunchIntent = new Intent(activity, Uri.parse(intenuri));
					}
					else {
						LaunchIntent = new Intent(activity);
					}
				}
				else {
					LaunchIntent = new Intent();
					LaunchIntent.setComponent(new ComponentName(com_name, activity));
				}
			}
			else {
				LaunchIntent = this.cordova.getActivity().getPackageManager().getLaunchIntentForPackage(com_name);
			}
			
			/**
			 * setPackage, http://developer.android.com/intl/ru/reference/android/content/Intent.html#setPackage(java.lang.String)
			 */
			if(spackage != null) {
				LaunchIntent.setPackage(spackage);
			}
			
			/**
			 * setType, http://developer.android.com/intl/ru/reference/android/content/Intent.html#setType(java.lang.String)
			 */
			if(intetype != null) {
				LaunchIntent.setType(intetype);
			}
			
			/**
			 * put arguments
			 */
			if(data.length() > 1) {
				JSONArray params = data.getJSONArray(1);
				JSONObject key_value;
				String key;
				String value;

				for(int i = 0; i < params.length(); i++) {
					if (params.get(i) instanceof JSONObject) {
						Iterator<String> iter = params.getJSONObject(i).keys();
						
						 while (iter.hasNext()) {
							key = iter.next();
							try {
								value = params.getJSONObject(i).getString(key);
								
								LaunchIntent.putExtra(key, value);
							} catch (JSONException e) {
								callbackContext.error("json params: " + e.toString());
							}
						}
					}
					else {
						LaunchIntent.setData(Uri.parse(params.getString(i)));
					}
				}
			}
			
			/**
			 * start activity
			 */
			this.cordova.getActivity().startActivity(LaunchIntent);
			callbackContext.success();
			
		} catch (JSONException e) {
			callbackContext.error("json: " + e.toString());
		} catch (Exception e) {
			callbackContext.error("intent: " + e.toString());
        }
    }

    /**
     * checkApp
     */	 
	public void check(String component, CallbackContext callbackContext) {
		PackageManager pm = this.cordova.getActivity().getApplicationContext().getPackageManager();
		try {
			/**
			 * get package info
			 */
			PackageInfo PackInfo = pm.getPackageInfo(component, PackageManager.GET_ACTIVITIES);
			
			/**
			 * create json object
			 */
			JSONObject info = new JSONObject();
			
			info.put("versionName", PackInfo.versionName);
			info.put("packageName", PackInfo.packageName);
			info.put("versionCode", PackInfo.versionCode);
			info.put("applicationInfo", PackInfo.applicationInfo);
			
			callbackContext.success(info);
		} catch (Exception e) {
			callbackContext.error(e.toString());
		}
	}
	
	/**
     * Get the OS name.
     *
     * @return
     */
    public String getPlatform() {
        String platform;
        if (isAmazonDevice()) {
            platform = AMAZON_PLATFORM;
        } else {
            platform = ANDROID_PLATFORM;
        }
        return platform;
    }
	
	/**
     * Get the device's Universally Unique Identifier (UUID).
     *
     * @return
     */
	public String getUuid() {
        String uuid = Settings.Secure.getString(this.cordova.getActivity().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        return uuid;
    }
	
	public String getModel() {
        String model = android.os.Build.MODEL;
        return model;
    }

    public String getProductName() {
        String productname = android.os.Build.PRODUCT;
        return productname;
    }

    public String getManufacturer() {
        String manufacturer = android.os.Build.MANUFACTURER;
        return manufacturer;
    }

    public String getSerialNumber() {
        String serial = android.os.Build.SERIAL;
        return serial;
    }

    /**
     * Get the OS version.
     *
     * @return
     */
    public String getOSVersion() {
        String osversion = android.os.Build.VERSION.RELEASE;
        return osversion;
    }

    public String getSDKVersion() {
        @SuppressWarnings("deprecation")
        String sdkversion = android.os.Build.VERSION.SDK;
        return sdkversion;
    }

    public String getTimeZoneID() {
        TimeZone tz = TimeZone.getDefault();
        return (tz.getID());
    }

    /**
     * Function to check if the device is manufactured by Amazon
     *
     * @return
     */
    public boolean isAmazonDevice() {
        if (android.os.Build.MANUFACTURER.equals(AMAZON_DEVICE)) {
            return true;
        }
        return false;
    }

    public boolean isVirtual() {
	return android.os.Build.FINGERPRINT.contains("generic") ||
	    android.os.Build.PRODUCT.contains("sdk");
    }
}

