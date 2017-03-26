/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ils.system;

import ils.convexhull.CalculateIntersectionofConvexHull3D;
import ils.convexhull.CalculateRectangular;
import ils.convexhull.CalculateSubtractofConvexHull3D;
import ils.convexhull.CalculateUnionConvexHull3D;
import ils.convexhull.ConvexHull3D;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 *
 * @author Wataru-T
 */
public class Integrate {   
    
    Properties properties = new Properties();
    UserColorList colorlist = new UserColorList();
    List<Integer> userlist = new ArrayList<>(); //識別できたユーザのリスト 
    
    Integrate(List<Integer> useridlist){
        userlist = useridlist;
    }
    
    public List<LocationData> IntegrateAreaEachUser(Room r,List<LocationData> ld,int num,Time now){    
        //ここで入力したList<LocationData>を、一つのLocationDataにひとまとめにして出力
        
        //一つのLocationData?複数ユーザを考慮するとListになる？
        List<LocationData> locationdata_eachuser = new ArrayList<>();
        CalculateIntersectionofConvexHull3D makelocationarea = new CalculateIntersectionofConvexHull3D(); 
        CalculateUnionConvexHull3D unionlocationarea = new CalculateUnionConvexHull3D();
        CalculateSubtractofConvexHull3D subtractlocationarea = new CalculateSubtractofConvexHull3D();
        
        ConvexHull3D roomarea = new ConvexHull3D();
        CalculateRectangular makeroom = new CalculateRectangular(new Vector3D(0,0,0),r.width,r.depth,r.height);
        makeroom.MakeConvexHull3D(roomarea);
        makeroom.MakeplyFile(properties.convexfilepath+"Room_"+ num +".ply", new Vector3D(255,255,255));
        //※注意、↑のファイルパスはラズパイ内のもの
        
       
        //複数ユーザを考慮すると、どうするのがよいか。
        //まずリストldの中でユーザ検知できてるものを探す
        //そしてそれから検知できてるユーザのリストを作る
        //それを元にユーザ一人一人に対してintersectionしていってエリア制作
        //この時、違うユーザ同士のSphere型ConvexhullをIntersectionさせないようにする。
        //Sphereとセンサを組み合わせるようにする
        
        //まずdatum(ld)の中でarea(ConvexHull)がnullになっているものを除外する
        int ldsize = ld.size();
        System.out.print("ld.size():"+ld.size()+"->");
        for(int i=0,count=0;count<ldsize;count++){
            System.out.println(ld.get(i).ls.coordinate + " " + ld.get(i).area + ld.get(i).subtract_flag);
            if(ld.get(i).area == null){
                ld.remove(i);
            }else{
                i++;
            }            
        }    
        System.out.println(ld.size());
        
        boolean isallsubtract = true;
        
        for(int i=0;i<ld.size();i++){
            makelocationarea.MakeplyFile(properties.convexfilepath + "locationdata"+num+"_"+i+".ply", ld.get(i).area, colorlist.Getcolor(ld.get(i).user_id));
            //System.out.println(ld.get(i).room_id + "," + ld.get(i).area + "," + ld.get(i).user_id + "," + ld.get(i).cur);
            if( (ld.get(i).user_id != -1) &&                //データを見てユーザリストを更新する  
                !(userlist.contains(ld.get(i).user_id))){   //すでにユーザリストに入っているユーザは入れないようにする
                userlist.add(ld.get(i).user_id);
            }
            
            if(!ld.get(i).subtract_flag){
                isallsubtract = false;
            }
        }
        System.out.println("Userlist:"+userlist);
       
        if((ld.size() == 0) || isallsubtract){ //出力データなし、またはすべてのLSがユーザ検知してない
            return locationdata_eachuser;//空のまま出力            
        }
        
        if(userlist.size() > 0){//一人でもユーザ識別できた時
            
            //user_idが-1じゃないデータと接してない-1のデータを探す→そのデータは完全な不明ユーザ、推論に使われる。そのデータを探す
            List<Integer> independent_data_ind = SearchIndependentData(ld);   
            
            //推論の必要があるユーザのID
            List<Integer> needtoinferring_user_ind = new ArrayList<>();
                                                          
            for(int h=0;h<userlist.size();h++){
                
                ConvexHull3D locationarea = roomarea;//それぞれのユーザごとにInterSectionを行うのでまず最初にlocationareaを部屋のConvexhullに初期化
                
                //先にユーザ識別情報があるConvexHullからInterSectionを始める
                //ただし、datum(ld)の中には識別したユーザ全てのデータがあるとは限らない
                //その場合は推論機能を用いて行う(フラグを立てる)
                boolean reasoning_flag = true;
                for(int i=0;i<ld.size();i++){
                    if(ld.get(i).user_id == userlist.get(h) && 
                       !ld.get(i).subtract_flag &&
                       makelocationarea.HasInterSection(locationarea, ld.get(i).area)){
                        ConvexHull3D calculatedarea = makelocationarea.Intersectioning(locationarea, ld.get(i).area);
                        if(calculatedarea.c != null){
                            //locationarea = makelocationarea.Intersectioning(locationarea, ld.get(i).area);
                            locationarea = calculatedarea;
                            makelocationarea.MakeplyFile(properties.convexfilepath+"Intersection_"+num+"_"+i+".ply", 
                                locationarea,colorlist.Getcolor(userlist.get(h)));                            //ユーザIDはint型1以上の整数,これでユーザごとに色は固定                        
                            reasoning_flag = false; //このユーザに対してはdatum(ld)の中にデータがあったので推論は不要
                            //System.out.println("Integrate:Intersection("+num+"_"+i+") made.");                        
                        }
                    }
                }
                
                if(reasoning_flag){
                    needtoinferring_user_ind.add(h);
                }
                
                //推論リスト作成
                //まずは初期化、現時点で認識しているユーザの数を要素数とし、初期化として要素は全て０とするリストを制作
                List<Double> userinfer_list = new ArrayList<>();
                for(int i=0;i<userlist.size();i++){
                    userinfer_list.add(0.0);
                }
                
                if(!reasoning_flag){//推論の必要がないユーザ
                    
                    //推論リストの該当する箇所にユーザ推論値100%を代入
                    userinfer_list.set(h, 100.0);
                    
                    //その後不明ユーザとのIntersection                    
                    for(int i=0;i<ld.size();i++){
                        if((ld.get(i).area != null) && 
                           (locationarea.c != null) &&
                           ((ld.get(i).user_id == -1))){// || (ld.get(i).user_id == userlist.get(h)) )){
                            /*
                            ConvexHull3D resultintersectionarea = makelocationarea.Intersectioning(locationarea,ld.get(i).area);
                
                            if(resultintersectionarea.c != null){
                                locationarea = resultintersectionarea;
                            */
                            if(makelocationarea.HasInterSection(locationarea, ld.get(i).area)){
                                System.out.print("No Reasoning: ");
                                if(ld.get(i).subtract_flag){
                                    ConvexHull3D calculatedarea = subtractlocationarea.Subtracting(locationarea,ld.get(i).area);
                                    if(calculatedarea.c != null){
                                        System.out.println("Integrate_reasoning:Subtraction("+num+"_"+h+"_"+i+") will make.");                                     
                                        //locationarea = subtractlocationarea.Subtracting(locationarea,ld.get(i).area);
                                        locationarea = calculatedarea;
                                        subtractlocationarea.MakeplyFile(properties.convexfilepath+"Intersection_"+num+"_"+h+"_"+i+".ply", 
                                            locationarea,colorlist.Getcolor(userlist.get(h)));                                                               
                                    }
                                }else{   
                                    ConvexHull3D calculatedarea = makelocationarea.Intersectioning(locationarea,ld.get(i).area);
                                    if(calculatedarea.c != null){
                                        System.out.println("Integrate_reasoning:Intersection("+num+"_"+h+"_"+i+") will make.");                                    
                                        //locationarea = makelocationarea.Intersectioning(locationarea,ld.get(i).area);
                                        locationarea = calculatedarea;
                                        makelocationarea.MakeplyFile(properties.convexfilepath+"Intersection_"+num+"_"+h+"_"+i+".ply", 
                                            locationarea,colorlist.Getcolor(userlist.get(h)));                            
                                    }
                                }
                            }
                        }
                    }
                
                    LocationData lcd = new LocationData();
                    lcd.room_id = r.roomNo;         //部屋ナンバー
                    //lcd.coordinate                //ユーザの部屋内の座標
                    lcd.area    = locationarea;     //ユーザがいうる部屋内のエリア、ConvexHull3Dで表現
                    lcd.user_id = userlist.get(h);  //ユーザID
                    lcd.cur     = now;              //このデータを取った時間
                    
                    lcd.user_inference = userinfer_list;    //ユーザの推論リスト
                    //lcd.ls                        //LSデータ、統合システム特有のものにする？ → ここ以降は使わなくて良いのでは
                    
                    locationdata_eachuser.add(lcd);
                }
            }
            
            if(independent_data_ind.size() > 0){    //独立しているConvexHullが存在する
                    
               //System.out.println("independent_data_ind:"+independent_data_ind);
               if(needtoinferring_user_ind.size() > 0){  //推論の必要があるユーザが存在する
                
                    //とりあえず。。。ただConvexHullは2つ以上ある場合もある、、そういう風にする処理をどうするかが課題
                    //ユーザ推論リストの作成も必要
                    
                    //推論リスト作成
                    //まずは初期化、現時点で認識しているユーザの数を要素数とし、初期化として要素は全て０とするリストを制作
                    List<Double> userinfer_list = new ArrayList<>();
                    for(int i=0;i<userlist.size();i++){
                        userinfer_list.add(0.0);
                    } 
                    
                    double percentage = 100.0 / (double)needtoinferring_user_ind.size();
                    for(int i=0;i<needtoinferring_user_ind.size();i++){
                        userinfer_list.set(needtoinferring_user_ind.get(i), percentage);
                    }
                    System.out.println("independent_userinferlist:"+userinfer_list);
                    ConvexHull3D locationarea = roomarea;                   
                   
                    for(int i=0;i<independent_data_ind.size();i++){
                        if((ld.get(independent_data_ind.get(i)).area != null) && 
                           (locationarea.c != null) &&
                           ((ld.get(independent_data_ind.get(i)).user_id == -1))){// || (ld.get(i).user_id == userlist.get(h)) )){

                            if(makelocationarea.HasInterSection(locationarea, ld.get(independent_data_ind.get(i)).area)){ 
                                if(ld.get(independent_data_ind.get(i)).subtract_flag){
                                    ConvexHull3D calculatedarea = subtractlocationarea.Subtracting(locationarea,ld.get(independent_data_ind.get(i)).area);
                                    if(calculatedarea.c != null){
                                        System.out.println("Integrate_independent:Subtraction("+num+"_minus_data_"+i+") will make.");                                        
                                        //locationarea = subtractlocationarea.Subtracting(locationarea,ld.get(independent_data_ind.get(i)).area);
                                        locationarea = calculatedarea;
                                        subtractlocationarea.MakeplyFile(properties.convexfilepath+"Intersection_"+num+"_minus_data_"+i+".ply", 
                                            locationarea,colorlist.unknownusercolorlist);                            
                                    }
                                }else{
                                    ConvexHull3D calculatedarea = makelocationarea.Intersectioning(locationarea,ld.get(independent_data_ind.get(i)).area);
                                    if(calculatedarea.c != null){
                                        System.out.println("Integrate_independent:Intersection("+num+"_plus_data_"+i+") will make.");                                                                       
                                        //locationarea = makelocationarea.Intersectioning(locationarea,ld.get(independent_data_ind.get(i)).area);                                                                
                                        locationarea = calculatedarea;
                                        makelocationarea.MakeplyFile(properties.convexfilepath+"Intersection_"+num+"_plus_data_"+i+".ply", 
                                            locationarea,colorlist.unknownusercolorlist);                            
                                    }

                                }
                            }
                        }
                    }                   
                   
                    LocationData lcd = new LocationData();
                    lcd.room_id = r.roomNo;         //部屋ナンバー
                    //lcd.coordinate                //ユーザの部屋内の座標
                    lcd.area    = locationarea;     //ユーザがいうる部屋内のエリア、ConvexHull3Dで表現
                    lcd.user_id = -1;               //ユーザID
                    lcd.cur     = now;              //このデータを取った時間
                    lcd.user_inference = userinfer_list;    //ユーザの推論リスト                    
                    //lcd.ls                        //LSデータ、統合システム特有のものにする？ → ここ以降は使わなくて良いのでは
                    
                    locationdata_eachuser.add(lcd);                   
                   
               }else{   //推論の必要がないユーザがいるなら、その独立したConvexhullは不明ユーザとして扱う.新たに入ってきたユーザかもしれない
                   
                    //推論リスト作成
                    //まずは初期化、現時点で認識しているユーザの数を要素数とし、初期化として要素は全て０とするリストを制作
                    List<Double> userinfer_list = new ArrayList<>();
                    for(int i=0;i<userlist.size();i++){
                        userinfer_list.add(0.0);
                    }                    
                   
                    ConvexHull3D locationarea = roomarea;                   
                   
                    for(int i=0;i<independent_data_ind.size();i++){
                        if((ld.get(independent_data_ind.get(i)).area != null) && 
                           (locationarea.c != null) &&
                           ((ld.get(independent_data_ind.get(i)).user_id == -1))){// || (ld.get(i).user_id == userlist.get(h)) )){                          
                            
                            if(makelocationarea.HasInterSection(locationarea, ld.get(independent_data_ind.get(i)).area)){                                  
                                if(ld.get(independent_data_ind.get(i)).subtract_flag){
                                    ConvexHull3D calculatedarea = subtractlocationarea.Subtracting(locationarea,ld.get(independent_data_ind.get(i)).area);                                    
                                    if(calculatedarea.c != null){
                                        System.out.println("Integrate_unknown:Subtraction("+num+"_"+i+") will make.");                                    
                                        //locationarea = subtractlocationarea.Subtracting(locationarea,ld.get(independent_data_ind.get(i)).area);
                                        locationarea = calculatedarea;
                                        subtractlocationarea.MakeplyFile(properties.convexfilepath+"Intersection_"+num+"_"+i+".ply", 
                                            locationarea,colorlist.unknownusercolorlist);                            
                                    }
                                }else{
                                    ConvexHull3D calculatedarea = makelocationarea.Intersectioning(locationarea,ld.get(independent_data_ind.get(i)).area);  
                                    if(calculatedarea.c != null){
                                        System.out.println("Integrate_unknown:Intersection("+num+"_"+i+") will make.");                                    
                                        //locationarea = makelocationarea.Intersectioning(locationarea,ld.get(independent_data_ind.get(i)).area);                                                                
                                        locationarea = calculatedarea;
                                        makelocationarea.MakeplyFile(properties.convexfilepath+"Intersection_"+num+"_plus_data_"+i+".ply", 
                                            locationarea,colorlist.unknownusercolorlist);                            
                                    }
                                }                                                                
                            }
                        }
                    }                   
                   
                    LocationData lcd = new LocationData();
                    lcd.room_id = r.roomNo;         //部屋ナンバー
                    //lcd.coordinate                //ユーザの部屋内の座標
                    lcd.area    = locationarea;     //ユーザがいうる部屋内のエリア、ConvexHull3Dで表現
                    lcd.user_id = -1;               //ユーザID
                    lcd.cur     = now;              //このデータを取った時間
                    lcd.user_inference = userinfer_list;    //ユーザの推論リスト                    
                    //lcd.ls                        //LSデータ、統合システム特有のものにする？ → ここ以降は使わなくて良いのでは
                    
                    locationdata_eachuser.add(lcd);
               }
            }
                        
            
            
        }
        else{//これまでに識別できたユーザが居ない場合、ユーザ識別できなかった場合は単純にデータをIntersectionさせていく
            
            //推論リスト作成
            //まずは初期化、現時点で認識しているユーザの数を要素数とし、初期化として要素は全て０とするリストを制作
            List<Double> userinfer_list = new ArrayList<>();
            for(int i=0;i<userlist.size();i++){
                userinfer_list.add(0.0);
            }               
            
            ConvexHull3D locationarea = roomarea;
            
            for(int i=0;i<ld.size();i++){
                if((ld.get(i).area != null) && 
                   (locationarea.c != null) && 
                    makelocationarea.HasInterSection(locationarea, ld.get(i).area)){ 
                    
                    if(ld.get(i).subtract_flag){
                        ConvexHull3D calculatedarea = subtractlocationarea.Subtracting(locationarea,ld.get(i).area);
                        if(calculatedarea.c != null){
                            System.out.println("Integrate_humei:Subtraction("+num+"_"+i+") will make.");                         
                            //locationarea = subtractlocationarea.Subtracting(locationarea,ld.get(i).area);
                            locationarea = calculatedarea;
                            subtractlocationarea.MakeplyFile(properties.convexfilepath+"InterSection_"+num+"_"+i+".ply", 
                                locationarea,colorlist.unknownusercolorlist);                            
                        }           
                    }else{
                        ConvexHull3D calculatedarea = makelocationarea.Intersectioning(locationarea,ld.get(i).area);                        
                        if(calculatedarea.c != null){
                            System.out.println("Integrate_humei:Intersection("+num+"_"+i+") will make.");                        
                            //locationarea = makelocationarea.Intersectioning(locationarea,ld.get(i).area);                      
                            locationarea = calculatedarea;
                            makelocationarea.MakeplyFile(properties.convexfilepath+"Intersection_"+num+"_plus_data_"+i+".ply", 
                                locationarea,colorlist.unknownusercolorlist);                            
                        }
                    }                                                                
                                      
                    /*
                    else{           
                        locationarea = unionlocationarea.Unioning(locationarea,ld.get(i).area);
                        System.out.println("Integrate:Union("+num+"_"+i+") made.");   
                        unionlocationarea.MakeplyFile(properties.convexfilepath+"Union_"+num+"_"+i+".ply", locationarea,new Vector3D(255,255,255));
                    }
                    */
                }
            }
            LocationData lcd = new LocationData();
            lcd.room_id = r.roomNo;         //部屋ナンバー
            //lcd.coordinate                //ユーザの部屋内の座標
            lcd.area    = locationarea;     //ユーザがいうる部屋内のエリア、ConvexHull3Dで表現
            lcd.user_id = -1;               //ユーザID,検知できなかったから不明ユーザ
            lcd.cur     = now;              //このデータを取った時間
            
            lcd.user_inference = userinfer_list;    //ユーザの推論リスト             
            //lcd.ls                        //LSデータ、統合システム特有のものにする？ 
            locationdata_eachuser.add(lcd);            
        }
        
        //とりあえず。。具体的な実装はまだ

        
        return locationdata_eachuser;
    }           
    
    private List<Integer> SearchIndependentData(List<LocationData> ld){
        
        List<Integer> independent_data = new ArrayList<>();
        CalculateIntersectionofConvexHull3D checkintersection = new CalculateIntersectionofConvexHull3D();
        
        //ldの中にはnullもあるので注意
        for(int h=0;h<ld.size();h++){
            if(ld.get(h).user_id == -1 && ld.get(h) != null){
                boolean flag = false;
                for(int i=0;i<ld.size();i++){
                    if(ld.get(i) != null){
                        if((h != i)&&
                           (ld.get(i).user_id != -1) &&
                           checkintersection.HasInterSection(ld.get(h).area, ld.get(i).area)){
                            flag = true;    //ユーザIDが-1のデータ(h)がユーザIDが-1でないデータ(i)と接していた時、データ(h)は不明ユーザではない
                        }
                    }
                }
                    
                if(!flag){//ユーザがあるデータ全てに接してない時はそのデータは独立している→完全な不明ユーザ、推論に使われる
                    independent_data.add(h);
                }                    
            }
        }    
        
        return independent_data;
    }
}