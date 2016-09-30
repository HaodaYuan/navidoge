package tju.noil.navidoge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import tju.noil.navidoge.control.Controller;

public class MainActivity extends AppCompatActivity {
    private Controller controller;
    private Button button;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controller=new Controller(this);
        initialize();
        controller.test();
    }
    private void initialize(){
        setTextView(R.id.textView,0);
        setTextView(R.id.textView2, 1);
        setTextView(R.id.textView3,2);
        setTextView(R.id.textView4, 3);
        controller.initialize();
        button=(Button)findViewById(R.id.button);
        button2=(Button)findViewById(R.id.button2);
        button3=(Button)findViewById(R.id.button3);
        button4=(Button)findViewById(R.id.button4);
        button5=(Button)findViewById(R.id.button5);
        button6=(Button)findViewById(R.id.button6);
        ButtonListener buttonListener=new ButtonListener();
        button.setOnClickListener(buttonListener);
        button2.setOnClickListener(buttonListener);
        button3.setOnClickListener(buttonListener);
        button4.setOnClickListener(buttonListener);
        button5.setOnClickListener(buttonListener);
        button6.setOnClickListener(buttonListener);
    }
    public void setTextView(int id,int no){
        TextView textView;
        textView= (TextView)findViewById(id);
        controller.setTextView(textView,no);
    }
    class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            controller.OnClick(v);
        }
    }
}
