/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ils.system;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author Wataru-T
 */
abstract public class DataBuffer{
 
    public void SetData(boolean d){
        
    }
    
    public void SetData(double d){
        
    }
    
    public boolean TakeSensorData(){
        
        return false;
    }
    
    public double TakeSpeedwayData(){
        
        return 0.0;
    }
    
    public Object TakeData(){
        Object o = true;
        
        return o;
    }
}
/*
public class DataBuffer {
    //LSが出したデータを格納しておくためのバッファ
    
    //List<Double> distance = new ArrayList<>();
    //List<Calendar> time = new ArrayList<>();
    //List<DataofSpeedway> dos = new ArrayList<>();
    //List<DataofHumanSensor> dohs = new ArrayList<>();
    DataofSpeedway dos = new DataofSpeedway(0);
    DataofSensor dohs = new DataofSensor(false); //初期値    
    
    public void Storedataofspeedway(double dist){//,Calendar t){
        //バッファに距離・時間を格納しておく
        //distance.add(dist);        
        //time.add(t);
        
        //DataofSpeedway ds = new DataofSpeedway(dist,t);
        //if(dos.size() == 0){
        //    dos.add(ds);
        //}else{
        //    dos.set(0, ds);
        //}
        
        dos = new DataofSpeedway(dist);
    }
    
    public void Storedataofsensor(boolean hd){
        //バッファに格納
        
        //DataofSensor dhs = new DataofSensor(hd);
        //if(dohs.size() == 0){
        //    dohs.add(dhs);
        //}else{
        //    dohs.set(0, dhs);
        //}
        
        dohs = new DataofSensor(hd);
    }
    
    //public void Removedataofspeedway(int ind){
        //バッファから要素を削除
        //distance.remove(ind);
        //time.remove(ind);
        //dos.remove(ind);
    //}
    
    //public boolean Removedataofsensor(int ind){
        //バッファから要素を取り出す
        //dohs.remove(ind);
    //}    
}
*/