package tju.noil.navidoge.control;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import tju.noil.navidoge.R;
import tju.noil.navidoge.collection.DataCollector;

/**
 * Created by noil on 2016/7/19.
 */
public class Controller {
    private DataCollector dataCollector;
    private TextView textView [];
    private File file;
    private FileOutputStream fos;
    private Context context;
    private boolean timerOn;
    private int time_gap=1000;
    Handler handler=new Handler();
    int timeCounter=0;
    //Control 构造函数
    public Controller(Context context){
        this.context=context;
        textView=new TextView[4];
    }
    public void initialize(){
        File appPath = context.getExternalFilesDir(null);
        file = new File(appPath, "data.txt");
        dataCollector =new DataCollector(context,textView);
        setString(dataCollector.getAllSensorsSize(),textView[0]);
        try {
            file.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void setTextView(TextView v,int i){textView[i]=v;}
    public void setString(String string,TextView v){v.setText(string);}
    public void OnClick(View v){
        switch (v.getId())
        {
            case R.id.button:
                //setString(dataCollector.getAllSensorsDetail(),textView[3]);
                dataCollector.startRecord();
                timerOn=true;
                time_gap=50;
                timer();
                break;
            case R.id.button2:;
                timerOn=false;
                saveDate(dataCollector.getOutputString());
                dataCollector.stopRecord();
                break;
            case R.id.button5:
                //setString(dataCollector.getAllSensorsDetail(),textView[3]);
                dataCollector.startRecord();
                timerOn=true;
                time_gap=200;
                timer2();
                break;
            case R.id.button6:
                timerOn=false;
                dataCollector.stopRecord();
                break;
            case R.id.button3:
                setString(dataCollector.getCurrentSensor(-1),textView[1]);
                setString(dataCollector.getCurrentIndex(),textView[0]);
                break;
            case R.id.button4:
                setString(dataCollector.getCurrentSensor(1),textView[1]);
                setString(dataCollector.getCurrentIndex(),textView[0]);
                break;
        }
    }
    public void timer(){
        Runnable runnable=new Runnable(){
            @Override
            public void run() {
                //TODO Auto-generated method stub
                timeCounter++;
                if(timerOn){
                    dataCollector.updateRecond(1);
                    handler.postDelayed(this, time_gap);
                }

            }
        };
        handler.postDelayed(runnable, time_gap);
    }
    public void timer2(){
        Runnable runnable=new Runnable(){
            @Override
            public void run() {
                //TODO Auto-generated method stub
                timeCounter++;
                if(timerOn){
                    dataCollector.updateRecond(2);
                    //setString("Large Test",textView[2]);
                    setString(dataCollector.getCurrentSensor(0),textView[3]);
                    saveData(dataCollector.getCurrentOutput());
                    handler.postDelayed(this, time_gap);
                }

            }
        };
        handler.postDelayed(runnable, time_gap);
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
    public void saveData(String content){
        try {
            fos = new FileOutputStream(file,true);
            byte[] bytes = content.getBytes();
            fos.write(bytes);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void test(){
        //timer();
    }
}
