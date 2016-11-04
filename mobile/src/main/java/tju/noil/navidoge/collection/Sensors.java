package tju.noil.navidoge.collection;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import java.io.OutputStream;

/**
 * Created by noil on 2016/7/22.
 */
public class Sensors extends Activity implements SensorEventListener {
    public int[] Sensors={0,Sensor.TYPE_ACCELEROMETER,Sensor.TYPE_MAGNETIC_FIELD,Sensor.TYPE_GYROSCOPE,Sensor.TYPE_GRAVITY};
    private final SensorManager sensorManager;
    private final Sensor accelerateSensor;
    private final Sensor magneticFieldSensor;
    private final Sensor gyroscopeSensor;
    private final Sensor gravitySensor;
    public int sensorNumber=4;
    private int windowSize=20;
    private static final float NS2S = 1.0f / 1000000000.0f;
    private TextView textView[];
    private static int index=0;
    private float value[][] = new float[10][6];
    private float records[][]=new float[4][windowSize];
    public boolean record;
    public int recordNo;
    private StringBuilder outputString;
    public boolean first;
    public Sensors(Context context,TextView[] textView) {
        this.textView=textView;
        recordNo=0;
        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        accelerateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magneticFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sensorManager.registerListener(this, accelerateSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, magneticFieldSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_GAME);
        first=true;
    }

    public void startRecord(){
        record=true;
        outputString=new StringBuilder();
    }

    public void stopRecord(){
        recordNo++;
        record=false;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        if (first){
            for (int i=0;i<10;i++) {
                value[i][3]=event.timestamp;
                value[i][4]=0;
            }
            first=false;
        }
        switch (event.sensor.getType())
        {
            case Sensor.TYPE_ACCELEROMETER:
                value[1][0] = event.values[0];
                value[1][1] = event.values[1];
                value[1][2] = event.values[2];
                value[1][3] = event.timestamp;
                if (index==1){setString(getCurrentData(),textView[2]);}
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                value[2][0] = event.values[0];
                value[2][1] = event.values[1];
                value[2][2] = event.values[2];
                value[2][3] = event.timestamp;
                float [] values = new float[3];
                float [] R= new float[9];
                float [] acc=new float[3];
                acc[0]=value[1][0];
                acc[1]=value[1][1];
                acc[2]=value[1][2];
                SensorManager.getRotationMatrix(R,null,acc,event.values);
                SensorManager.getOrientation(R,values);
                value[2][4] =  values[0];
                if (index==2){setString(getCurrentData(),textView[2]);}
                break;
            case Sensor.TYPE_GYROSCOPE:
                final float dT = (event.timestamp - value[3][3]) * NS2S;
                value[3][0] = event.values[0];
                value[3][1] = event.values[1];
                value[3][2] = event.values[2];
                value[3][3] = event.timestamp;
                float v = (float)Math.sqrt(value[3][1]*value[3][1]+value[3][2]*value[3][2]);
                if (value[3][1]*value[3][2]<0){
                    if (value[3][1]+value[3][2]<0)
                        v=-v;
                }
                else{
                    if (value[3][1]<0)
                        v=-v;
                }
                value[3][4]=value[3][4]-v*dT;
                value[3][5]=value[3][5]-value[3][0]*dT;
                if (index==3){setString(getCurrentData(),textView[2]);}
                break;
            case Sensor.TYPE_GRAVITY:
                value[4][0] = event.values[0];
                value[4][1] = event.values[1];
                value[4][2] = event.values[2];
                value[4][3] = event.timestamp;
                float g=(float)Math.sqrt(value[4][0]*value[4][0]+value[4][1]*value[4][1]+value[4][2]*value[4][2]);
                value[4][4]=(float)Math.asin(value[4][2]/g);
                if (index==4){setString(getCurrentData(),textView[2]);}
                break;
        }
    }

    public void updateRecord(boolean All){

        if(All){
            outputString.append(getCurrentOutput(1));
            outputString.append(getCurrentOutput(2));
            outputString.append(getCurrentOutput(3));
            outputString.append(getCurrentOutput(4));
        }
        else{
            records[0][recordNo%windowSize]=value[2][4];//C
            records[1][recordNo%windowSize]=value[3][4];//S
            records[2][recordNo%windowSize]=value[3][5];//S2
            records[3][recordNo%windowSize]=value[4][4];//G
            recordNo++;

            if (recordNo%windowSize==0){
                for (int i=0;i<windowSize;i++){
                    for (int j=0;j<4;j++){
                        outputString.append(records[j][i]+" ");
                    }
                    outputString.append("\n");
                }
                records=new float[4][windowSize];
            }
        }
    }
    public String getCurrentData(){
        return new String("x:" + value[index][0] + "\n" + "y:" + value[index][1] + "\n" + "z:" + value[index][2] + "\n"+ "o:" + value[index][4] + "\n");
    }
    public String getCurrentOutput(int i){
        index=i;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(i+ " "+value[i][0] + " " + value[i][1] + " "  + value[i][2]+ " "  + value[i][3]+ " "  + value[i][4]+ " "  + value[i][5]+"\n");
        return stringBuilder.toString();
    }
    public String getCurrentType(int i){
        index=i;
        StringBuilder stringBuilder = new StringBuilder();
        switch (Sensors[i]) {
            case Sensor.TYPE_ACCELEROMETER:
                stringBuilder.append(" 加速度传感器");
                break;
            case Sensor.TYPE_GYROSCOPE:
                stringBuilder.append(" 陀螺仪传感器");
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                stringBuilder.append(" 电磁场传感器");
                break;
            case Sensor.TYPE_GRAVITY:
                stringBuilder.append(" 重力传感器");
        }
        return stringBuilder.toString();
    }

    public String getOutputString(){
        return outputString.toString();
    }

    public void setString(String string,TextView v){
        v.setText(string);
    }

//    public void updateOrientation(){
//        textView[1].setText(getOrientation());
//    }
//    public float[] calculateOrientation(){
//        float[] values = new float[3];
//        float[] R =  new float[9];
//        SensorManager.getRotationMatrix(R, null, value[1],
//                value[2]);
//        //SensorManager.getRotationMatrixFromVector();
//        SensorManager.getOrientation(R, values);
//        return values;
//    }
//    public void updateCurrent(String s){
//        textView[2].setText(s);
//    }
//
//    public String getOrientation(){
//        String direction=new String();
//        thetaCompass=(float) Math.toDegrees(value[3][0]);
//        thetaT=ROTATION_C*thetaCompass+(1-ROTATION_C)*(thetaT_1+thetaGyro);
//        thetaT_1=thetaT;
//        float angle=thetaT;
//        if (angle>=-5&&angle<=5){
//            direction="正北";
//        }
//        else if (angle>=5&&angle<85){
//            direction="东北";
//        }
//        else if (angle>=85&&angle<=95){
//            direction="正东";
//        }
//        else if (angle>95&&angle<175){
//            direction="东南";
//        }
//        else if(angle>=175&&angle<=180
//                ||angle>=-180&&angle<=-175){
//            direction="正南";
//        }
//        else if(angle>-175&&angle<-95){
//            direction="西南";
//        }
//        else if(angle>=-95&&angle<=-85){
//            direction="正西";
//        }
//        else if(angle>-85&&angle<-5){
//            direction="西北";
//        }
//        direction=direction+thetaT;
//        return direction;
//    }
}

