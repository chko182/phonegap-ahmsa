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

import android.content.ContentResolver; //afaria
import android.content.pm.ApplicationInfo; //afaria
import android.content.pm.ProviderInfo; //afaria
import android.database.Cursor; //afaria
import android.os.Bundle; //afaria
import android.os.NetworkOnMainThreadException; //afaria
import android.os.StrictMode; //afaria
import java.io.BufferedReader; //afaria
import java.io.BufferedWriter; //afaria
import java.io.DataOutputStream; //afaria
import java.io.FileReader; //afaria
import java.io.IOException; //afaria
import java.io.InputStreamReader; //afaria
import java.io.OutputStreamWriter; //afaria
import java.io.File; //afaria
import java.io.FileOutputStream; //afaria

import org.apache.http.auth.NTCredentials; //afaria
import org.apache.http.HttpResponse; //afaria
import org.apache.http.client.ClientProtocolException; //afaria
import org.apache.http.client.CredentialsProvider; //afaria
import org.apache.http.client.methods.HttpGet; //afaria
import org.apache.http.impl.client.BasicCredentialsProvider; //afaria
import org.apache.http.impl.client.DefaultHttpClient; //afaria
import org.apache.http.params.HttpConnectionParams; //afaria
import org.apache.http.auth.AuthScope; //afaria

public class Hello extends CordovaPlugin {
	public static final String TAG = "Device";

    public static String platform;                            // Device OS
    public static String uuid;                                // Device UUID

    private static final String ANDROID_PLATFORM = "Android";
    private static final String AMAZON_PLATFORM = "amazon-fireos";
    private static final String AMAZON_DEVICE = "Amazon";
	
	private static Context _context = null; //Afaria
    private static NTCredentials Credentials = null; //Afaria
	
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
					message = "s115302";
					callbackContext.success(message);
					
					return true;
					
				} else {
					
					if (action.equals("getPlataforma")) {
						
						String message = "Android";
						callbackContext.success(message);
						
						return true;
						
					} else {
						
						if (action.equals("opensett")) {
							
							String name = data.getString(0);
							
							Context context=this.cordova.getActivity().getApplicationContext();
							Intent dialogIntent = new Intent(name);
							dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							context.startActivity(dialogIntent);
							callbackContext.success();
							
							return true;
							
						} else {
						
							if (action.equals("dataAfaria")) {
								
								String cod_usuario = "";
								
								try {
										cod_usuario = this.get_value_provider();
								} catch (Exception e) {
									cod_usuario = e.toString();
								}								
								
								String message = cod_usuario;
								callbackContext.success(message);
								
								return true;
								
							} else {
							
								return false;
								
							}
						}
						
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
	
	//Obtiene el usuario desde el proveedor de Afaria
	public String get_value_provider() 
		throws Hello.SeedDataAPIException
	{
        String retval = null;

        _context = this.cordova.getActivity().getApplicationContext(); 
        String seedDataURL = getSeedDataURL();

        String sPath = "";
        try {
            sPath = retrieveSeedData();
        } catch (Hello.SeedDataAPIException e) {
            e.printStackTrace();
        }

        StringBuilder text = ReadFile(sPath);
        String[] lines = text.toString().split("\\n");

        for (String s : lines) {
            retval = s;
        }

        return retval;
	}
	
	public static StringBuilder ReadFile(String sPath) {
        File file = new File(sPath);

        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error : " +  e.getMessage());
        }

        return text;
    }
	
	public static String retrieveSeedData()
            throws Hello.SeedDataAPIException {
        String retval = null;

        Credentials = null;

        String seedDataURL = getSeedDataURL();
        DefaultHttpClient httpclient = new DefaultHttpClient();

        int timeoutConnection = 30000;
        HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), timeoutConnection);

        String seedData = "";
        try {
            String fullURL = seedDataURL;
            HttpGet httpget = new HttpGet(fullURL);
            if (Credentials != null) {
                CredentialsProvider credsProvider = new BasicCredentialsProvider();
                credsProvider.setCredentials(AuthScope.ANY, Credentials);
                httpclient.setCredentialsProvider(credsProvider);
            }
            HttpResponse httpResponse = httpclient.execute(httpget);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == 401) {
                throw new SeedDataAPIException("HTTP request requires Authentication", 110);
            }
            if (statusCode != 200) {
                throw new SeedDataAPIException("Package Server could not be reached. Response Code: " + statusCode, 2);
            }
            seedData = getResponseBody(httpResponse);

            File dirpath = new File(_context.getFilesDir(), "seedData");
            dirpath.mkdirs();
            File filepath = new File(dirpath, "SUPOnboardingSeedData.txt");

            FileOutputStream fileOutStream = new FileOutputStream(filepath);
            DataOutputStream out = new DataOutputStream(fileOutStream);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));

            bw.write(seedData);
            bw.flush();

            retval = filepath.getAbsolutePath();

            bw.close();
            out.close();
            fileOutStream.close();

            return retval;
        } catch (ClientProtocolException e) {
            throw new SeedDataAPIException(e.getMessage(), 2);
        } catch (IOException e) {
            throw new SeedDataAPIException(e.getMessage(), 2);
        } catch (NetworkOnMainThreadException e) {
            throw new SeedDataAPIException(e.getMessage(), 2);
        } catch (Exception e) {
            throw new SeedDataAPIException(e.getMessage(), 101);
        }
    }
	
	private static String getSeedDataURL()
    {
        String retval = null;
        String mensajedevuelto = "";
        if (null != _context) {
            ApplicationInfo appInfo = _context.getApplicationInfo();
            if (null != appInfo) {
                String packageName = appInfo.packageName;
                if (null != packageName) {
                    ContentResolver cr = _context.getContentResolver();
                    Uri uri = Uri.withAppendedPath(SharedPrefsContentProvider.CONTENT_URI, packageName);

                    Cursor c = cr.query(uri, null, null, null, null);
                    if (null == c) {
                        mensajedevuelto = "Cursor Nulo";
                    } else {
                        if (!c.moveToFirst()) {
                            if (!c.isClosed()) {
                                c.close();
                            }
                            mensajedevuelto = "Query Returned Not Data";
                        } else {
                            retval = c.getString(0);
                            if (!c.isClosed()) {
                                c.close();
                            }
                            if (retval == null) {
                                mensajedevuelto = "No data for this package: " + packageName;
                            }
                        }
                    }
                }
            }
        } else {
            mensajedevuelto = "Unknow Error";
        }

        if (retval == null) {
            return  mensajedevuelto;
        } else {
            return retval;
        }
    }
	
	private static String getResponseBody(HttpResponse response)
    {
        String responseText = null;
        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String dataString = "";
            String newData = null;
            while ((newData = in.readLine()) != null) {
                dataString = dataString + newData + '\n';
            }
            responseText = dataString;
        }
        catch (Exception e)
        {
            responseText = e.toString();
        }
        return responseText;
    }
	
	public static class SeedDataAPIException
            extends Exception {
        public static final int AFARIA_CLIENT_NOT_INSTALLED = 0;
        public static final int NO_DATA_AVAILABLE = 1;
        public static final int COULD_NOT_CONTACT_SERVER = 2;
        public static final int UNKNOWN = 101;
        public static final int AUTHENTICATION_FAILED = 110;

        public SeedDataAPIException(String sMessage, int iErrorCode) {
            super();
            setErrorCode(iErrorCode);
        }

        public int getErrorCode() {
            return this.m_iErrorCode;
        }

        private void setErrorCode(int iCode) {
            this.m_iErrorCode = iCode;
        }

        private int m_iErrorCode = 101;
        private static final long serialVersionUID = -1261972544210264770L;
    }
}

