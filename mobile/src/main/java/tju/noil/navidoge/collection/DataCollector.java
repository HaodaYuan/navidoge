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
    public String getCurrentIndex(){
        return new String("当前传感器: " +index);
    }
    public String getCurrentSensor(int i){
        if(index+i>=0&&index+i<=9){
            index=index+i;
            if(index==0)
                return wiFi.ScanWifi();
            else
                return sensors.getCurrent(index);
        }
        return "NO CURRENT DATA";
    }
    public void updateRecond(int type){
        if (index==0)
            return;
        else
            sensors.updateRecond(type);
    }
    public void startRecord(){
        if (index!=0)
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
    public String getOutputString(){
        return sensors.getOutputString();
    }
    public void saveDate(String content){
        try {
            fos = new FileOutputStream(file,true);
            byte [] bytes = content.getBytes();
            fos.write(bytes);
            fos.close();
            Toast.makeText(context, "保存成功", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    public String getAllSensorsDetail(){
//        StringBuilder stringBuilder=new StringBuilder();
//        for (Sensor s : allSensors) {
//            String tempString=s.getType()+"\n" + "  设备名称：" + s.getName() + "\n" + "  设备版本：" + s.getVersion() + "\n" + "  供应商："
//                    + s.getVendor() + "\n";
//            switch (s.getType()) {
//
//                case Sensor.TYPE_ACCELEROMETER:
//                    stringBuilder.append(" 加速度传感器"+ tempString);
//                    break;
//                case Sensor.TYPE_GYROSCOPE:
//                    stringBuilder.append(" 陀螺仪传感器" + tempString);
//                    break;
//                case Sensor.TYPE_LIGHT:
//                    stringBuilder.append(" 环境光线传感器" + tempString);
//                    break;
//                case Sensor.TYPE_MAGNETIC_FIELD:
//                    stringBuilder.append(" 电磁场传感器" + tempString);
//                    break;
//                case Sensor.TYPE_ORIENTATION:
//                    stringBuilder.append(" 方向传感器" + tempString);
//                    break;
//                case Sensor.TYPE_PROXIMITY:
//                    stringBuilder.append(" 距离传感器" + tempString);
//                    break;
//                case Sensor.TYPE_ROTATION_VECTOR:
//                    stringBuilder.append(" 翻转传感器" + tempString);
//                    break;
//                case Sensor.TYPE_LINEAR_ACCELERATION:
//                    stringBuilder.append(" 线性加速度" + tempString);
//                    break;
//                case Sensor.TYPE_GRAVITY:
//                    stringBuilder.append(" 重力感应器传感器" + tempString);
//                    break;
//                default:
//                    stringBuilder.append(" 未知传感器" + tempString);
//                    break;
//            }
//        }
//        return stringBuilder.toString();
//    }
//    public WifiStation getWifiStation(){
//        return wifiStation;
//    }
}
