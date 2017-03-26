/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ils.system;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Wataru-T
 */
public class DataBufferofSpeedway extends DataBuffer{
    
    double distance = 0.0;
    List<Double> eachdistance = new ArrayList<>(); //各個人の距離、識別した人数に応じ要素数が増える
    
    //@Override
    //public void SetData(double d){
    //    this.distance = d;        
    //}
    
    public void SetEachData(int tagid,double d){
        try{
            eachdistance.set(tagid, d);
        }catch(IndexOutOfBoundsException e){
            while(true){
                eachdistance.add(0.0);
                
                if(eachdistance.size() > tagid){
                    eachdistance.set(tagid, d);
                    break;
                }
            }
        }        
    }

    //@Override
    //public Object TakeData(){
    //    double data = this.distance;
    //    this.distance = 0.0;
    //    
    //    return data;
    //}
    
    public List<Double> TakeEachData(){
        List<Double> eachdata = new ArrayList<>();
        eachdata.addAll(eachdistance);
        
        //データをリセット
        for(int i=0;i<eachdistance.size();i++){
            eachdistance.set(i, 0.0);
        }
        
        return eachdata;
    }
}
