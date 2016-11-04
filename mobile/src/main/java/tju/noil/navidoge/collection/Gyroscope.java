package tju.noil.navidoge.collection;

/**
 * Created by noil on 2016/10/20.
 */
import Jama.*;

public class Gyroscope {
    Matrix q;
    Matrix omega;
    Matrix k[];
    Matrix R;
    public void Cal_R(){
        double[][] R = new double[3][3];
        double q1=this.q.get(0, 0);
        double q2=this.q.get(0, 1);
        double q3=this.q.get(0, 2);
        double q4=this.q.get(0, 3);
        R[0][0] = q1 * q1 - q2 * q2 - q3 * q3 + q4 * q4;
        R[0][1] = 2 * (q1 * q2 - q4 * q3);
        R[0][2] = 2 * (q1 * q3 - q4 * q2);

        R[1][0] = 2 * (q1 * q2 + q4 * q3);
        R[1][1] = - q1 * q1 + q2 * q2 - q3 * q3 + q4 * q4;
        R[1][2] = 2 * (q2 * q3 - q4 * q1);

        R[2][0] = 2 * (q3 * q1 - q4 * q2);
        R[2][1] = 2 * (q3 * q2 + q4 * q1);
        R[2][2] = 2 * q1 * q1 - q2 * q2 + q3 * q3 + q4 * q4;
        this.R=new Matrix(R);
        return;
    }
    public Gyroscope() {
        // TODO Auto-generated constructor stub
        q=new Matrix(new double[]{0,0,0,1},4);
        omega=new Matrix(new double[]{0,0,0},3);
        k=new Matrix[4];
    }
    public Gyroscope(Matrix q,Matrix omega){
        // TODO Auto-generated constructor stub
        this.q=q;
        this.omega=omega;
        k=new Matrix[4];
    }
    public void Runge_Kutta(Matrix omega,double h){
        k[0]=function(this.q,this.omega);
        k[1]=function(this.q.plus(k[0].times(h/2)), this.omega.plus(omega).times(0.5));
        k[2]=function(this.q.plus(k[1].times(h/2)), this.omega.plus(omega).times(0.5));
        k[3]=function(this.q.plus(k[2].times(h)), omega);
        this.q=this.q.plus(k[0].plus(k[1].times(2)).plus(k[2].times(2)).plus(k[3]).times(h/6));
        this.omega=omega;
    }
    public Matrix function(Matrix my,Matrix mft){
        double []dq_dt=new double[4];
        double []y=my.getRowPackedCopy();
        double []ft=mft.getRowPackedCopy();
        dq_dt[0]=( y[3]*ft[0] - y[2]*ft[1] + y[1]*ft[2])/2;
        dq_dt[1]=( y[2]*ft[0] + y[3]*ft[1] - y[0]*ft[2])/2;
        dq_dt[2]=(-y[1]*ft[0] + y[0]*ft[1] + y[3]*ft[2])/2;
        dq_dt[3]=(-y[0]*ft[0] - y[1]*ft[1] - y[2]*ft[2])/2;
        return new Matrix(dq_dt,4);
    }

}

