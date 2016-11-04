package tju.noil.navidoge.collection;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.wifi.ScanResult;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import tju.noil.navidoge.database.WifiStation;

/**
 * Created by noil on 2016/7/18.
 */
public class DataCollector {
    private WiFi wiFi;
    private SensorManager sensorManager;
    private List<Sensor> allSensors;
    private Sensors sensors;
    private Context context;
    private int index;
    private File file;
    private FileOutputStream fos;
    private WifiStation wifiStation;
    private StringBuilder outputString;
    public DataCollector(Context context,TextView[] textView){
        this.context=context;
        this.wiFi=new WiFi(context);
        this.sensors=new Sensors(context,textView);
        wifiStation=new WifiStation();
        //从系统服务中获得传感器管理器
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        //从传感器管理器中获得全部的传感器列表
        allSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        index=-1;
    }
    public String getAllSensorsSize(){
        return new String("该设备共有" + allSensors.size() + "个传感器");
    }
    public void IndexMove(int i){
        if(index+i>=0&&index+i<=sensors.sensorNumber){
            index=index+i;
        }
    }
    public String getCurrentIndex(){
        return new String("当前传感器: " +index);
    }
    public String getCurrentSensor(){
        if(index==0){
            sensors.getCurrentType(index);
            return "WiFi";
        }
        else
            return sensors.getCurrentType(index);
    }
    public String getCurrentData(){
        if (index>0){
            return sensors.getCurrentData();
        }
        else
            return wiFi.ScanWifi();
    }
    public void updateRecord(){
        if (index==-1){
            sensors.updateRecord(true);
            wiFi.updateRecord();
        }
        else if(index==0){
            wiFi.updateRecord();
        }
        else{
            sensors.updateRecord(false);
        }
    }
    public void startRecord(){
        sensors.startRecord();
    }
    public void stopRecord(){
        sensors.stopRecord();
    }
    public String getCurrentOutput(){
        if (index==0)
            return wiFi.getOutputString();
        else
            return sensors.getOutputString();
    }
    public String getOutputString(int type){
        if (type==1){
            return sensors.getOutputString();
        }
        else if (type==2){
            return wiFi.getOutputString();
        }
        else
            return "";
    }
}
