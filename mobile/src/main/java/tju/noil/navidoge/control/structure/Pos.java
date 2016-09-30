package tju.noil.navidoge.control.structure;

import java.text.DecimalFormat;

/**
 * Created by noil on 2016/7/19.
 */
public class Pos {
    public double x;
    public double y;
    public Pos (){
        this.x=0;
        this.y=0;
    }
    public Pos(double x,double y){
        this.x=x;
        this.y=y;
    }
    public String toString(){
        DecimalFormat df = new DecimalFormat("#.00");
        return  "x:"+df.format(x)+" y:"+df.format(y);
    }
    public double getDistance(Pos b){
        double d=Math.sqrt((b.x-this.x)*(b.x-this.x)+(b.y-this.y)*(b.y-this.y));
        return d;
    }
}
