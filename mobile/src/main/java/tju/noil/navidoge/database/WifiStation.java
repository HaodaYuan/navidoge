package tju.noil.navidoge.database;

import android.net.wifi.ScanResult;

import java.util.ArrayList;
import java.util.List;

import tju.noil.navidoge.control.structure.Pos;

/**
 * Created by noil on 2016/7/19.
 */
public class WifiStation {
    private List<Station> wifiStationList;
    static final int MAX_STATION=5;
    public WifiStation(){
        wifiStationList=new ArrayList<Station>(MAX_STATION);
        wifiStationList.add(new Station("00:18:4d:9b:15:8a",1,new Pos(5.6,5.5)));
    }
    public void setRSSI(List<ScanResult> wifiList){
        for(ScanResult scanResult:wifiList){
            for(Station station:wifiStationList){
                if(scanResult.BSSID.equals(station.sAddress)){
                    station.RSSI=scanResult.level;
                }
            }
        }
    }
    public String toString(){
        StringBuilder stringBuilder=new StringBuilder();
        for(Station station:wifiStationList){
            stringBuilder.append("BSSID: "+station.sAddress+"\nRSSI: "+station.RSSI+"\n");
        }
        return stringBuilder.toString();
    }
}
