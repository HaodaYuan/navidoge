package tju.noil.navidoge.database;

import tju.noil.navidoge.control.structure.Pos;

/**
 * Created by noil on 2016/7/19.
 * sType
 *      1:WiFi Station
 *      2:....
 */

public class Station {
    public String sAddress;
    public int sType;
    public int aId;
    public int asId;
    public Pos sRelativeCoordinate;
    public double RSSI;
    public Station(){
        this.sAddress="00:00:00:00:00:00";
        this.sType=0;
        this.aId=0;
        this.asId=0;
        this.sRelativeCoordinate=new Pos();
    }
    public Station(String sAddress,int sType,Pos sRelativeCoordinate){
        this.sAddress=sAddress;
        this.sType=sType;
        this.sRelativeCoordinate=sRelativeCoordinate;
    }
}
