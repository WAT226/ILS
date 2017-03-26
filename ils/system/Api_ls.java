/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ils.system;

import ils.convexhull.CalculateCorn;
import ils.convexhull.CalculateRectangular;
import ils.convexhull.CalculateSphere3D;
import ils.convexhull.ConvexHull3D;
import ils.system.humansensor.HumanSensorConfig;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 *
 * @author Wataru-T
 */
public class Api_ls {

    Room room;
    LSData lsd;
    //List<LocationData> lcd =new ArrayList<>();  //出力するLocationDataはリスト形式で何個も並べたほうが良い？
    //LocationData lcd_single;                    //出力するLocationDataは一つにまとめたほうが良い？
    UserColorList ucl;
    Properties properties;
    
    //Api_ls(String name,int id,int timeacc,int spaceacc,int acc,List<Vector3D> ls){
    //    lsd = new LSData(name,id,timeacc,spaceacc,acc,ls);
    //}
    
    Api_ls(Room r,LSData lsdata){
        room = r;
        lsd = lsdata;
        ucl = new UserColorList();
        properties = new Properties();
    }
 
    //LocationDataにConvexHull3Dを入れる用    
    public LocationData makeLocationData(ConvexHull3D con3d,int userid,Time time,boolean subtractflag){

        LocationData ld = new LocationData();  
      
        ld.room_id = room.roomNo; //部屋ID
        //ld.coordinate = calculationDistance(distance,lsd.ls_id);
        ld.area = con3d;
        ld.user_id = userid; //Speedway(RFID)だとuseridは０以上、センサ系LSだとuseridは-1(不明)
        ld.cur = time;
        ld.ls = lsd;

        ld.subtract_flag = subtractflag;
        return ld;
    }    
    /*
    public List<LocationData> ReceiveDataofSpeedway(List<DataBufferofSpeedway> db,Time t){   
        //一つのspeedwayにつながっている全てのアンテナについてを見る
        List<LocationData> lcd = new ArrayList<>();
        //System.out.println("Api_ls.ReceiveDataofSpeedway, db.size:"+db.size());
        for(int i=0;i<db.size();i++){//アンテナごとに見る
            //System.out.println("db("+i+").eachdistance.size:"+db.get(i).eachdistance.size()+"("+db.get(i).eachdistance +")");
            ConvexHull3D speedway;
            //double distance = (double)db.get(i).TakeData();
            List<Double> distance = db.get(i).TakeEachData();            
            for(int j=0;j<distance.size();j++){//ユーザごとに見る
                //if(distance > 0.0){
                if(distance.get(j) > 0.0){                    
                    speedway = new ConvexHull3D();
                    CalculateSphere3D makesphere3D = new CalculateSphere3D(lsd.coordinate,distance.get(j),30);//db.get(i).distance,30); //一応３０度        
                    makesphere3D.MakeConvexHull3D(speedway);
                    //makesphere3D.MakeplyFile(properties.convexfilepath+"Speedway_antenna"+ i +"_user"+ j +".ply", ucl.Getcolor(j));
                }else{
                    speedway = null;
                }
                LocationData lcd_single = makeLocationData(speedway,j,t,false); //アンテナiが読み取ったユーザjの位置情報、ConvexHull3D型変数のspeedwayがnullなら読み取られなかったということになる
                lcd.add(lcd_single);
            }            
        }
        
        return lcd;            
    }
    
    //public LocationData ReceiveDataofHumanSensor(DataBufferofSensor db,Time t){
    public LocationData ReceiveDataofHumanSensor(DataBufferofSensor db,Time t,int i,HumanSensorConfig hsc){        
        ConvexHull3D humansensor;
        LocationData lcd_single;          
        if((boolean)db.TakeData()){//db.detect == true){
            //System.out.println("true");
            humansensor = new ConvexHull3D();
            CalculateCorn makecorn3D = new CalculateCorn(lsd.coordinate,hsc);//コーンの向きは後で調整            
            makecorn3D.MakeConvexHull3D(humansensor);
            ///makecorn3D.MakeplyFile(properties.convexfilepath+"HumanSensor_"+i+".ply", new Vector3D(0,0,0));
            lcd_single = makeLocationData(humansensor,-1,t,false);
        }else{
            //System.out.println("false");
            //humansensor = null;
            humansensor = new ConvexHull3D();
            CalculateCorn makecorn3D = new CalculateCorn(lsd.coordinate,hsc);//コーンの向きは後で調整            
            makecorn3D.MakeConvexHull3D(humansensor);
            lcd_single = makeLocationData(humansensor,-1,t,true);            
        }
        
        //LocationData lcd_single = makeLocationData(humansensor,-1,t);        
        return lcd_single;
    } 
    
    public LocationData ReceiveDataofCameraSensor(DataBufferofSensor cs,Time t,int i,HumanSensorConfig hsc){
        
        ConvexHull3D camerasensor;
        LocationData lcd_single;        
        if((boolean)cs.TakeData()){//cs.detect == true){
            camerasensor = new ConvexHull3D();
            //CalculateCorn makeCorn3D = new CalculateCorn(lsd.coordinate.get(0),room.width,60,10,0,90);//コーンの向きは後で調整
            CalculateCorn makeCorn3D = new CalculateCorn(lsd.coordinate,hsc);//コーンの向きは後で調整            
            makeCorn3D.MakeConvexHull3D(camerasensor);
            ///makeCorn3D.MakeplyFile(properties.convexfilepath + "CameraSensor_"+i+".ply", new Vector3D(0,0,0)); 
            lcd_single = makeLocationData(camerasensor,-1,t,false);            
        }else{
            //camerasensor = null;
            camerasensor = new ConvexHull3D();
            CalculateCorn makeCorn3D = new CalculateCorn(lsd.coordinate,hsc);//コーンの向きは後で調整            
            makeCorn3D.MakeConvexHull3D(camerasensor);
            lcd_single = makeLocationData(camerasensor,-1,t,true);             
        }
        
        //LocationData lcd_single = makeLocationData(camerasensor,-1,t);        
        return lcd_single;
    }     
    */
    public LocationData ReceiveData(Databuf db,Time t,HumanSensorConfig hsc){
        //
        ConvexHull3D output_ls;
        LocationData lcd = new LocationData();
        int databufind = db.GetInd();
        List<Object> o = new ArrayList<>();
        o = db.GetData(databufind);
        
        int tagid = (int)o.get(0);
        double distance = (double)o.get(1);
        boolean detect = (boolean)o.get(2);
        
        int convexhulltype = db.GetConvexhullType();
             
        //System.out.println("Api_ls.ReceiveData,tagid:"+tagid+", distance:"+distance+", detect:"+detect+", convextype:"+convexhulltype+" ,lsd.coordinate:"+lsd.coordinate);
        
        switch(convexhulltype){
            case 0://0ならSphere
                if(distance > 0.0){
                    System.out.println("Api_ls.receivedata:(sphere_Convexhull)distance:"+distance);
                    output_ls = new ConvexHull3D();
                    CalculateSphere3D makeSphere3D = new CalculateSphere3D(lsd.coordinate,distance,30);
                    makeSphere3D.MakeConvexHull3D(output_ls);
                    lcd = makeLocationData(output_ls,tagid,t,!detect);
                    //subtract_flagがtrueならConvexhullを引くので検知(detect==true)したらsubtract_flagはfalse
                }else{
                    lcd = makeLocationData(null,tagid,t,!detect);
                }
                break;
            case 1://1ならCorn
                output_ls = new ConvexHull3D();
                CalculateCorn makeCorn3D = new CalculateCorn(lsd.coordinate,hsc);
                makeCorn3D.MakeConvexHull3D(output_ls);
                lcd = makeLocationData(output_ls,tagid,t,!detect); 
                break;
            default:
                //System.out.println("ERROR!:convexhulltype:"+convexhulltype);
                lcd = makeLocationData(null,-1,t,false);                
                break;
        }
        
        /*
        switch(databufind){
            case 1://detectのみ
                makeCorn3D = new CalculateCorn(lsd.coordinate,hsc);
                makeCorn3D.MakeConvexHull3D(output_ls);
                lcd = makeLocationData(output_ls,-1,t,detect);
            case 2://distanceのみ
                makeSphere3D = new CalculateSphere3D(lsd.coordinate,distance,30);
                makeSphere3D.MakeConvexHull3D(output_ls);
                lcd = makeLocationData(output_ls,-1,t,detect);
            case 3://distance,detect
                makeSphere3D = new CalculateSphere3D(lsd.coordinate,distance,30);
                makeSphere3D.MakeConvexHull3D(output_ls);
                lcd = makeLocationData(output_ls,-1,t,detect);
            case 4://tagidのみ,,存在するか？
                makeCorn3D = new CalculateCorn(lsd.coordinate,hsc);
                makeCorn3D.MakeConvexHull3D(output_ls);
                lcd = makeLocationData(output_ls,tagid,t,detect);
            case 5://tagid,detect
                makeCorn3D = new CalculateCorn(lsd.coordinate,hsc);
                makeCorn3D.MakeConvexHull3D(output_ls);
                lcd = makeLocationData(output_ls,tagid,t,detect);
            case 6://tagid,distance
                makeSphere3D = new CalculateSphere3D(lsd.coordinate,distance,30);
                makeSphere3D.MakeConvexHull3D(output_ls);
                lcd = makeLocationData(output_ls,tagid,t,detect);
            case 7://tagid,distance,detect
                makeCorn3D = new CalculateCorn(lsd.coordinate,hsc);
                makeCorn3D.MakeConvexHull3D(output_ls);
                lcd = makeLocationData(output_ls,tagid,t,detect);
            default:
                
        }
        */
        return lcd;
    }
}
