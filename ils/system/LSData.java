/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ils.system;

import ils.system.humansensor.HumanSensorConfig;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 *
 * @author Wataru-T
 */
public class LSData {
    //LSのデータ
    
    String ls_name;
    
    int ls_id;
    int ls_time_acc;
    int ls_space_acc;
    int ls_acc;

    //List<Vector3D> coordinate = new ArrayList<>();
    //List<HumanSensorConfig> sensorconfig = new ArrayList<>(); 
    
    Vector3D coordinate;
    HumanSensorConfig sensorconfig;     
    
    LSData(){
        
    }
    
    LSData(String name,int id,int timeacc,int spaceacc,int acc,Vector3D c){//List<Vector3D> los){
        ls_name = name;
        
        ls_id = id;
        ls_time_acc = timeacc;
        ls_space_acc = spaceacc;
        ls_acc = acc;
        //coordinate.addAll(los);
        coordinate = c;
    }
    
    LSData(String name,int id,int timeacc,int spaceacc,int acc,Vector3D c,HumanSensorConfig hsc){//List<Vector3D> los,List<HumanSensorConfig> hsc){
        ls_name = name;
        
        ls_id = id;
        ls_time_acc = timeacc;
        ls_space_acc = spaceacc;
        ls_acc = acc;
        //coordinate.addAll(los);
        //sensorconfig.addAll(hsc);
        
        coordinate = c;
        sensorconfig = hsc;
    }    
}
