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
    private final SensorManager sensorManager;
    private final Sensor accelerateSensor;
    private final Sensor magneticFieldSensor;
    private final Sensor orientationSensor;
    private final Sensor gyroscopeSensor;
    private static final float NS2S = 1.0f / 1000000000.0f;
    private float timestamp;
    private TextView textView[];
    private static int index=0;
    private float value[][] = new float[10][3];
    private float thetaT;
    private float thetaT_1;
    private float thetaCompass;
    private float thetaGyro;
    private static float ROTATION_C=0.9f;
    private final float[] deltaRotationVector = new float[4];
    private float[] deltaRotationMatrix = new float[9];
    private float[] rotationCurrent = new float[9];
    private StringBuilder outputString;
    public float startTimestamp;
    public boolean record;
    public int recordNo;
    public Sensors(Context context,TextView[] textView) {
        this.textView=textView;
        recordNo=1;
        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        accelerateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magneticFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sensorManager.registerListener(this, accelerateSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, magneticFieldSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, orientationSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public void startRecord(){
        startTimestamp=timestamp;
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
        switch (event.sensor.getType())
        {
            case Sensor.TYPE_ACCELEROMETER:
                value[1][0] = event.values[0];
                value[1][1] = event.values[1];
                value[1][2] = event.values[2];
                value[3]=calculateOrientation();
                updateCurrent(event.sensor.getType());
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                value[2][0] = event.values[0];
                value[2][1] = event.values[1];
                value[2][2] = event.values[2];
                value[3]=calculateOrientation();
                updateCurrent(event.sensor.getType());
                break;
            case Sensor.TYPE_ORIENTATION:
                updateCurrent(event.sensor.getType());
                break;
            case Sensor.TYPE_GYROSCOPE:
                // This timestep's delta rotation to be multiplied by the
                // current rotation
                // after computing it from the gyro sample data.
                if (timestamp!=0) {
                    final float dT = (event.timestamp - timestamp) * NS2S;
                    // Axis of the rotation sample, not normalized yet.
                    value[4][0] = event.values[0];
                    value[4][1] = event.values[1];
                    value[4][2] = event.values[2];
                    // Calculate the angular speed of the sample  
                    float omegaMagnitude = (float) Math.sqrt(value[4][0] * value[4][0]
                            + value[4][1] * value[4][1] + value[4][2] * value[4][2]);
                    // Integrate around this axis with the angular speed by the  
                    // timestep  
                    // in order to get a delta rotation from this sample over  
                    // the timestep  
                    // We will convert this axis-angle representation of the  
                    // delta rotation  
                    // into a quaternion before turning it into the rotation  
                    // matrix.  
                    thetaGyro = omegaMagnitude * dT / 2.0f;
//                    float sinThetaOverTwo = (float) Math.sin(thetaOverTwo);
//                    float cosThetaOverTwo = (float) Math.cos(thetaOverTwo);
//                    deltaRotationVector[0] = sinThetaOverTwo * value[4][0];
//                    deltaRotationVector[1] = sinThetaOverTwo * value[4][1];
//                    deltaRotationVector[2] = sinThetaOverTwo * value[4][2];
//                    deltaRotationVector[3] = cosThetaOverTwo;
                }
                else{
                    thetaT_1=(float) Math.toDegrees(value[3][0]);
                }
                timestamp=event.timestamp;

//                SensorManager.getRotationMatrixFromVector(deltaRotationMatrix,
//                        deltaRotationVector);
                updateCurrent(event.sensor.getType());
                break;
        }
    }
    public float[] calculateOrientation(){
        float[] values = new float[3];
        float[] R =  new float[9];
        SensorManager.getRotationMatrix(R, null, value[1],
                value[2]);
        //SensorManager.getRotationMatrixFromVector();
        SensorManager.getOrientation(R, values);
        return values;
    }
    public void updateCurrent(int type){
        textView[2].setText(getCurrent(type));
    }
    public void updateRecond(int type){
        if (record){
            if (type==1){
                outputString.append(recordNo+" "+(timestamp-startTimestamp)*NS2S+" ");
                outputString.append(thetaT+" "+thetaT_1+"\n");
            }
            else{
                outputString=new StringBuilder();
                outputString.append(recordNo+" "+(timestamp-startTimestamp)*NS2S+" ");
                outputString.append(thetaT+" "+thetaT_1+"\n");
            }
        }
    }
    public String getCurrent(int i){
        index=i;
        StringBuilder stringBuilder = new StringBuilder();
        switch (i) {
            case Sensor.TYPE_ACCELEROMETER:
                stringBuilder.append(" 加速度传感器");
                break;
            case Sensor.TYPE_GYROSCOPE:
                updateOrientation();
                stringBuilder.append(" 陀螺仪传感器");
                break;
            case Sensor.TYPE_LIGHT:
                stringBuilder.append(" 环境光线传感器");
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                stringBuilder.append(" 电磁场传感器");
                break;
            case Sensor.TYPE_ORIENTATION:
                stringBuilder.append(" 加速度+电磁");
                break;
            case Sensor.TYPE_PROXIMITY:
                stringBuilder.append(" 距离传感器");
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
                stringBuilder.append(" 翻转传感器");
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                stringBuilder.append(" 线性加速度");
                break;
            case Sensor.TYPE_GRAVITY:
                stringBuilder.append(" 重力感应器传感器");
                break;
            default:
                stringBuilder.append(" 未知传感器" );
                break;
        }
        stringBuilder.append("\nx:" + value[i][0] + "\n" + "y:" + value[i][1] + "\n" + "z:" + value[i][2] + "\n");
        return stringBuilder.toString();
    }
    public void updateOrientation(){
        textView[1].setText(getOrientation());
    }
    public String getOrientation(){
        String direction=new String();
        thetaCompass=(float) Math.toDegrees(value[3][0]);
        thetaT=ROTATION_C*thetaCompass+(1-ROTATION_C)*(thetaT_1+thetaGyro);
        thetaT_1=thetaT;
        float angle=thetaT;
        if (angle>=-5&&angle<=5){
            direction="正北";
        }
        else if (angle>=5&&angle<85){
            direction="东北";
        }
        else if (angle>=85&&angle<=95){
            direction="正东";
        }
        else if (angle>95&&angle<175){
            direction="东南";
        }
        else if(angle>=175&&angle<=180
                ||angle>=-180&&angle<=-175){
            direction="正南";
        }
        else if(angle>-175&&angle<-95){
            direction="西南";
        }
        else if(angle>=-95&&angle<=-85){
            direction="正西";
        }
        else if(angle>-85&&angle<-5){
            direction="西北";
        }
        direction=direction+thetaT;
        return direction;
    }
    public String getOutputString(){
        return outputString.toString();
    }
}

