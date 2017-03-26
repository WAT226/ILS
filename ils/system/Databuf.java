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
public class Databuf{

    
    int tagid = -1;         //ユーザID
    double distance = 0.0;  //ユーザとの推定距離
    boolean detect = false; //ユーザ検知の有無(Speedwayで検知した時もtrueにさせる)
    
    int ind;                //↑のどれを使うかを表す、0~7の2進数3桁で、左桁からtagid,distance,detect
    
    int convexhulltype = -1;//どのタイプのconvexhullで表すか。現在は0だとSphere,1だとCorn.(初期値は-1)
    
    public void SetData(int index,int id,double dist,boolean det,int ch_type){
        ind = index;

        if((ind / 4) % 2 == 1){
            //tagid,ind=4,5,6,7
            //000 001 010 011 100 101 110 111
            //000 000 000 000 001 001 001 001
            tagid = id;
        }
        
        if((ind / 2) % 2 == 1){
           //distance,ind=2,3,6,7
           //000 001 010 011 100 101 110 111
           //000 000 001 001 010 010 011 011
           distance = dist;
        }
        
        if(ind % 2 == 1){
           //detect,ind=1,3,5,7
           //000 001 010 011 100 101 110 111 
           detect = det;
        }                
        
        convexhulltype = ch_type;//0だとSphere,1だとCorn
        //System.out.println("DataBuf.SetData,index:"+ind+" ,tagid:"+tagid+" distance:"+distance+" detect:"+detect);
    }
    
    public int GetInd(){
        int index = ind;
        ind = 0;
        return index;
    }
    
    public int GetConvexhullType(){
        int ch_type = convexhulltype;
        convexhulltype = -1;
        return ch_type;
    }
    
    public List<Object> GetData(int index){
        List<Object> o = new ArrayList<>();
        
        if((index / 4) % 2 == 1){
            //tagid,ind=4,5,6,7
            //000 001 010 011 100 101 110 111
            //000 000 000 000 001 001 001 001
            o.add(tagid);
        }else{
            o.add(-1);
        }
        tagid = -1;
        
        if((index / 2) % 2 == 1){
           //distance,ind=2,3,6,7
           //000 001 010 011 100 101 110 111
           //000 000 001 001 010 010 011 011
           o.add(distance);
        }else{
           o.add(0.0);
        }
        distance = 0.0;
        
        if(index % 2 == 1){
           //detect,ind=1,3,5,7
           //000 001 010 011 100 101 110 111 
           o.add(detect);
        }else{
           o.add(false);
        }
        detect = false;
        
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