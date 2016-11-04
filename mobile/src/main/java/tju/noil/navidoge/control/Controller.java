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
    private File file2;
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
        file2 = new File(appPath, "wifi_data.txt");
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
            //开始记录
            case R.id.button:
                //setString(dataCollector.getAllSensorsDetail(),textView[3]);
                dataCollector.startRecord();
                timerOn=true;
                time_gap=50;
                timer();
                break;
            //停止记录
            case R.id.button2:;
                timerOn=false;
                saveData(dataCollector.getOutputString(1), this.file, true);
                //saveData("wifi",this.file2,true);
                dataCollector.stopRecord();
                break;
            //开始持续记录
            case R.id.button5:
                //setString(dataCollector.getAllSensorsDetail(),textView[3]);
                dataCollector.startRecord();
                timerOn=true;
                time_gap=200;
                timer2();
                break;
            //结束持续记录
            case R.id.button6:
                timerOn=false;
                dataCollector.stopRecord();
                break;
            //上一个
            case R.id.button3:
                //更新模式
                dataCollector.IndexMove(-1);
                //更新面板
                setString(dataCollector.getCurrentIndex(), textView[0]);
                setString(dataCollector.getCurrentSensor(),textView[1]);
                setString(dataCollector.getCurrentData(),textView[2]);
                break;
            //下一个
            case R.id.button4:
                //更新模式
                dataCollector.IndexMove(1);
                //更新面板
                setString(dataCollector.getCurrentIndex(), textView[0]);
                setString(dataCollector.getCurrentSensor(),textView[1]);
                setString(dataCollector.getCurrentData(),textView[2]);
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
                    dataCollector.updateRecord();
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
                    dataCollector.updateRecord();
                    //setString("Large Test",textView[2]);
                    setString(dataCollector.getCurrentSensor(),textView[3]);
                    saveData(dataCollector.getCurrentOutput(),file,false);
                    handler.postDelayed(this, time_gap);
                }

            }
        };
        handler.postDelayed(runnable, time_gap);
    }
    public void saveData(String content,File file,boolean toast){
        try {
            fos = new FileOutputStream(file,true);
            byte [] bytes = content.getBytes();
            fos.write(bytes);
            fos.close();
            if (toast)
                Toast.makeText(context, file.getName()+"保存成功", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void test(){
        //timer();
    }
}
