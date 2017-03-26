/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ils.system;

import ils.convexhull.CalculateConvexhull3D;
import ils.convexhull.ConvexHull3D;
import ils.convexhull.Visible3DbyMakingply;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 *
 * @author Wataru-T
 */
public class Api_service {
    //Integrateから受け取ったデータを上に送る
    //受け取ったデータは精度がバラバラなので動的に変えて送り出す
    
    int acc_level;
    
    Vector3D coordinate_inroom;     //部屋の中での具体的な座標                  (レベル1)
    ConvexHull3D area_inroom;       //部屋の中での大まかなエリア(ConvexHull3D)  (レベル2)
    int room_id;                    //部屋ID                                (レベル1,2,3(,4))
    List<Integer> room_ids = new ArrayList<>();         //部屋IDのリスト                         (レベル4)
    boolean user_inhouse;           //ユーザは家庭内にいるか？                  (レベル5)
    
    CalculateConvexhull3D makeply = new CalculateConvexhull3D();  
    Properties properties = new Properties();
    UserColorList ucl = new UserColorList();
    
    Api_service(int accl,List<Integer> roomlist){
        acc_level = accl;
        room_ids = roomlist;
    }

    public void EachUserDataReturning(int userid,List<LocationData> loc,int i){
        
        LocationData l = new LocationData();
        
        
        boolean flag = false;
        for(int j=0;j<loc.size();j++){
            if(loc.get(j).user_id == userid){
                l = loc.get(j);
                flag = true;
                break;
            }
        }
        
        if(!flag){//ユーザIDが-1のLocationDataはユーザ推論リストを見て判断
            for(int j=0;j<loc.size();j++){
                if(loc.get(j).user_inference.get(userid) >= 100){//推論が100%のときはそのエリアはそのユーザである
                    System.out.println("User "+userid+" may be in room "+loc.get(j).room_id + 
                            "(" + loc.get(j).user_inference.get(userid) + "%).");                    
                    l = new LocationData();
                    
                    l.area = loc.get(j).area;
                    l.cur = loc.get(j).cur;
                    l.user_id = userid;
                    l.room_id = loc.get(j).room_id;   
                    
                    //ユーザが断定できない（ユーザ推定）が、一応推定エリアのConvexhullは出す
                    System.out.println("("+ properties.convexfilepath + "Result"+i+"_User"+l.user_id+".ply"
                                + " in Room " + l.room_id + ")");               
                    Visible3DbyMakingply visualv3 = 
                                new Visible3DbyMakingply(properties.convexfilepath + "Result"+i+"_User"+l.user_id+".ply",
                                l.area,ucl.Getcolor(l.user_id));
                    visualv3.WriteplyFile();                    
                }else if(loc.get(j).user_inference.get(userid) > 0){
                    System.out.println("User "+userid+" may be in room "+loc.get(j).room_id + 
                            "(" + loc.get(j).user_inference.get(userid) + "%).");
                    l = new LocationData();
                    l.area = loc.get(j).area;
                    l.cur = loc.get(j).cur;
                    l.user_id = userid;                    
                    l.room_id = loc.get(j).room_id;
                    //l.room_id = -1;
                }
            }
        }
        
        if(l.cur != null){
            System.out.print("Data Time:"+l.cur.hh+":"+l.cur.mm+":"+l.cur.ss+"."+l.cur.ms+", ");
        }        
        
        if(l.coordinate != null){
            System.out.print("OUTPUT_ACCURACY_LEVEL:1. ");
            System.out.println("User " + l.user_id +" 's coordinate:"+l.coordinate + "in Room " + l.room_id);
        }else if(l.area != null){
            System.out.print("OUTPUT_ACCURACY_LEVEL:2. ");            
            System.out.println("User " + l.user_id +" 's area:"+ properties.convexfilepath + "Result"+i+"_User"+l.user_id+".ply"
                                + " in Room " + l.room_id);               
            Visible3DbyMakingply visualv3 = 
                    new Visible3DbyMakingply(properties.convexfilepath + "Result"+i+"_User"+l.user_id+".ply",
                                             l.area,ucl.Getcolor(l.user_id));
            visualv3.WriteplyFile();
            //System.out.println(properties.convexfilepath + "Result"+i+"_User"+l.user_id+".ply made.");  
     
        }else if(l.room_id != -1){
            //System.out.println("Result,infer:"+l.user_inference);
            
            System.out.print("OUTPUT_ACCURACY_LEVEL:3. ");
            System.out.println("User " + l.user_id + " is in Room " + l.room_id);            
        }
        else if(l.room_id == -1){
            System.out.print("OUTPUT_ACCURACY_LEVEL:4. ");
            System.out.println("User " + l.user_id + " is in These Room:" + room_ids);               
        }
        /*else if(){
            System.out.print("OUTPUT_ACCURACY_LEVEL:5. ");
            System.out.println("User " + l.user_id + "is in House?:");               
        }*/
        else{
            System.out.print("OUTPUT_ACCURACY_LEVEL:6. ");
            System.out.println("User " + l.user_id + " is missing ");           
        }

    }    
    
    public void EachUserDataReturning(List<Integer> useridlist,List<LocationData> loc,int i){
        

        
        for(int j=0;j<useridlist.size();j++){
            LocationData l = new LocationData();            
            boolean flag = false;
            int userid = useridlist.get(j);
            l.user_id = userid;
            for(int k=0;k<loc.size();k++){
                if(loc.get(k).user_id == userid){
                    l = loc.get(k);
                    flag = true;
                    //loc.remove(k);
                    break;
                }
            }
            
            if(!flag){//ユーザIDが-1のLocationDataはユーザ推論リストを見て判断
                for(int k=0;k<loc.size();k++){
                    if(loc.get(k).user_inference.get(userid) >= 100){//推論が100%のときはそのエリアはそのユーザである
                        System.out.println("User "+userid+" is in room "+loc.get(k).room_id + 
                                "(" + loc.get(k).user_inference.get(userid) + "%).");                    
                        l = new LocationData();
                        
                        l.area = loc.get(k).area;
                        l.cur = loc.get(k).cur;
                        l.user_id = userid;
                        l.room_id = loc.get(k).room_id;   
                        //loc.remove(k);
                    }else if(loc.get(k).user_inference.get(userid) > 0){
                        System.out.println("User "+userid+" may be in room "+loc.get(k).room_id + 
                                "(" + loc.get(k).user_inference.get(userid) + "%).");
                        l = new LocationData();
                        l.cur = loc.get(k).cur;
                        l.room_id = loc.get(k).room_id;
                        //l.room_id = -1;
                        //loc.remove(k);
                    }
                }
            }
            
            if(loc.size() == 0){//統合クラスからの出力データがない時
                l = new LocationData();
                        
                l.area = null;
                l.cur = null;
                l.user_id = userid;
                l.room_id = -1;  
            }
            
            if(l.cur != null){
                System.out.print("Data Time:"+l.cur.hh+":"+l.cur.mm+":"+l.cur.ss+"."+l.cur.ms+", ");
            }
            if(l.coordinate != null){
                System.out.print("OUTPUT_ACCURACY_LEVEL:1. ");
                System.out.println("User " + l.user_id +" 's coordinate:"+l.coordinate + "in Room " + l.room_id);
            }else if(l.area != null){
                System.out.print("OUTPUT_ACCURACY_LEVEL:2. ");            
                System.out.println("User " + l.user_id +" 's area:"+ properties.convexfilepath + "Result"+i+"_User"+l.user_id+".ply"
                                    + " in Room " + l.room_id);               
                Visible3DbyMakingply visualv3 = 
                        new Visible3DbyMakingply(properties.convexfilepath + "Result"+i+"_User"+l.user_id+".ply",
                                             l.area,ucl.Getcolor(l.user_id));
                visualv3.WriteplyFile();
                //System.out.println(properties.convexfilepath + "Result"+i+"_User"+l.user_id+".ply made.");  
     
            }else if(l.room_id != -1){
                //System.out.println("Result,infer:"+l.user_inference);
            
                System.out.print("OUTPUT_ACCURACY_LEVEL:3. ");
                System.out.println("User " + l.user_id + " is in Room " + l.room_id);            
            }
            //else if(l.room_id == -1){
            //    System.out.print("OUTPUT_ACCURACY_LEVEL:4. ");
            //    System.out.println("User " + l.user_id + " is in Room " + room_ids);               
            //}
            /*else if(){
                System.out.print("OUTPUT_ACCURACY_LEVEL:5. ");
                System.out.println("User " + l.user_id + "is in House?:");               
            }*/
            else{
                System.out.print("OUTPUT_ACCURACY_LEVEL:6. ");
                System.out.println("User " + l.user_id + " is missing ");           
            }
        }
    }        
    /*
    public void DoAll(LocationData l,int i,int acc_level){

        //room_ids = new ArrayList<>();
        //DataTransforming(l,acc_level);
        //Returning(acc_level);
        
        DataReturning(l,i);
    }
    */
    
    public void RestDataReturning(List<LocationData> l,int i){
        
        for(int j=0;j<l.size();j++){
            if(l.get(j).user_id == -1){
                
                boolean flag = false;
                for(int k=0;k<l.get(j).user_inference.size();k++){
                    if(l.get(j).user_inference.get(k) > 0.0){
                        flag = true;
                        break;
                    }
                }
                
                if(!flag){
                    System.out.print("OUTPUT_ACCURACY_LEVEL:3. ");
                    System.out.println("Other User is in Room " + l.get(j).room_id);                      
                }
                
            }
        }
    }
    
    public void DoAll(List<Integer> useridlist,List<LocationData> l,int i){

        //room_ids = new ArrayList<>();
        //DataTransforming(l,acc_level);
        //Returning(acc_level);
        
        EachUserDataReturning(useridlist,l,i);
        RestDataReturning(l,i);
    } 
    
}