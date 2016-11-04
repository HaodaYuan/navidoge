package tju.noil.navidoge.collection;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import java.util.List;

import tju.noil.navidoge.database.Station;

/**
 * Created by noil on 2016/7/18.
 */
public class WiFi {
    private WifiManager wifiManager;
    private List<ScanResult> wifiList;
    private ScanResult scanResult;
    private int recordNo;
    private StringBuilder outputString;
    private Context context;
    public WiFi(Context context){
        this.context=context;
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        recordNo=0;
        outputString=new StringBuilder();
    }
    //打开WIFI
    public void OpenWifi(){
        if (!wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);
        }
    }
    //关闭WIFI
    public void CloseWifi(){
        if (!wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(false);
        }
    }
    public String ScanWifi(){
        StringBuilder stringBuilder=new StringBuilder();
        wifiManager.startScan();
        wifiList=wifiManager.getScanResults();
        for (ScanResult scanResult:wifiList){
            if (scanResult.level>-80)
                stringBuilder.append(scanResult.toString()+"\n\n");
        }
        return stringBuilder.toString();
    }
    public void updateRecord(){
        StringBuilder stringBuilder=new StringBuilder();
        wifiManager.startScan();
        wifiList=wifiManager.getScanResults();
        for (ScanResult scanResult:wifiList){
            if (scanResult.level>-80)
                stringBuilder.append(recordNo+" "+scanResult.timestamp+" "+scanResult.SSID+" "+scanResult.BSSID+" "+scanResult.level+" "+scanResult.frequency+"\n");
        }
        recordNo++;
        //Toast.makeText(context, "wifi update", Toast.LENGTH_LONG).show();
        outputString.append(stringBuilder.toString());
    }
    public String getOutputString(){
        return outputString.toString();
    }
    public List<ScanResult> getWifiList(){
        return wifiList;
    }
}
