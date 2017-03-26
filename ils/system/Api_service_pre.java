/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ils.system;

import ils.convexhull.CalculateConvexhull3D;
import ils.convexhull.ConvexHull3D;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 *
 * @author Wataru-T
 */
public class Api_service_pre {
    
    int acc_level;
    
    Vector3D coordinate_inroom;     //部屋の中での具体的な座標                  (レベル1)
    ConvexHull3D area_inroom;       //部屋の中での大まかなエリア(ConvexHull3D)  (レベル2)
    int room_id;                    //部屋ID                                (レベル1,2,3(,4))
    List<Integer> room_ids = new ArrayList<>();         //部屋IDのリスト                         (レベル4)
    boolean user_inhouse;           //ユーザは家庭内にいるか？                  (レベル5)
    
    Api_service_pre(int accl){
        acc_level = accl;
        room_ids = new ArrayList<>();
    }
    
    public void Returning(int acc_level){
        switch(acc_level){
            case 1: //ユーザがどの部屋で、
                    //具体的に部屋内のどこにいるかの座標を返す(x,y,z,room)
                System.out.println(this.coordinate_inroom);
                break;
            case 2: //ユーザがどの部屋で、
                    //大まかにどのあたりにいるかを返す(area,room)
                CalculateConvexhull3D ch3d = new CalculateConvexhull3D();
                System.out.println(this.area_inroom);
                break;
            case 3: //ユーザがどの部屋にいるかを返す(room)
                    //たとえばroom_id、部屋名で返すとか？
                System.out.println(this.room_id);              
                break;
            case 4: //(possibly multiple rooms)
                    //ユーザがいる可能性のある部屋IDのリストを列挙
                System.out.println(this.room_ids);              
                break;
            case 5: //(Is the user in the house?)
                    //booleanで返す？
                System.out.println(this.user_inhouse);              
                break;
            case 6: //ユーザが識別、検知できないとき、不明ユーザとして扱う。
                    //no information available
                System.out.println("unknown");               
                break;
            default:
                System.out.println("error!"); 
                //エラー出力（とりあえずこの形）
                break;
        }        
    }
    
    public void DataTransforming(LocationData l,int acc_level){
        //Appに応じ、適した精度レベルで部屋・ユーザのデータを返す。
        //acc_levelは1~6
        
        LocationData output_l;
        //output_l = l;//とりあえず、、具体的な処理追加したらこれはなしで
        
        //どのようなデータ構造にして返すかも一度考える必要あり
        //今はRoomクラスを返してアプリケーションに送るということにしている
        switch(acc_level){
            case 1: //ユーザがどの部屋で、
                    //具体的に部屋内のどこにいるかの座標を返す(x,y,z,room)
                //output_l = DatatoLevel1(l);
                DatatoLevel1(l);                
                break;
            case 2: //ユーザがどの部屋で、
                    //大まかにどのあたりにいるかを返す(area,room)
                //output_l = DatatoLevel2(l);
                DatatoLevel2(l);                
                break;
            case 3: //ユーザがどの部屋にいるかを返す(room)
                    //たとえばroom_id、部屋名で返すとか？
                //output_l = DatatoLevel3(l);  
                DatatoLevel3(l);
                break;
            case 4: //(possibly multiple rooms)
                    //ユーザがいる可能性のある部屋IDのリストを列挙
                //output_l = DatatoLevel4(l);  
                DatatoLevel4(l);
                break;
            case 5: //(Is the user in the house?)
                    //booleanで返す？
                //output_l = DatatoLevel5(l);   
                DatatoLevel5(l);
                break;
            case 6: //ユーザが識別、検知できないとき、不明ユーザとして扱う。
                    //no information available
                //output_l = DatatoLevel6(l);
                DatatoLevel6(l);
                break;
            default:
                System.out.println("error!"); 
                output_l = l;
                //エラー出力（とりあえずこの形）
                break;
        }
        
        //return output_l;       
    }    
    
    public void DatatoLevel1(LocationData inputld){    
        CalculateConvexhull3D calcon3D = new CalculateConvexhull3D();
        
        //入力した位置情報のレベルが１より低かったら、、何もし無いでそのまま出す？？
        
        //入力した位置情報のレベルが１より低い、つまり座標がない場合
        //→Convexhull3Dはある、その重心座標を出させてみる？
        this.coordinate_inroom = calcon3D.computeBarycenter(inputld.area); // とりあえず、、今はこんな形、具体的な実装は後でしろ
        
        //return ld;
    }
    
    public void DatatoLevel2(LocationData inputld){
        
        
        this.area_inroom = inputld.area; // とりあえず、、今はこんな形、具体的な実装は後でしろ
        //入力した位置情報のレベルが2より低かったら、、何もし無いでそのまま出す？？
        
        //return ld;
    }

    public void DatatoLevel3(LocationData inputld){
        
        this.room_id = inputld.room_id; // とりあえず、、今はこんな形、具体的な実装は後でしろ
        //入力した位置情報のレベルが3より低かったら、、何もし無いでそのまま出す？？
        
        //return ld;
    }

    public void DatatoLevel4(LocationData inputld){
        
        this.room_ids.add(inputld.room_id); // とりあえず、、今はこんな形、具体的な実装は後でしろ
        //入力した位置情報のレベルが4より低かったら、、何もし無いでそのまま出す？？
        
        //return ld;
    }

    public void DatatoLevel5(LocationData inputld){
        
        if(inputld != null){
            this.user_inhouse = true; // とりあえず、、今はこんな形、具体的な実装は後でしろ
            //入力した位置情報のレベルが5より低かったら、、何もし無いでそのまま出す？？
        }else{
            this.user_inhouse = false;
        }
        
        //return ld;
    }
     
    public void DatatoLevel6(LocationData inputld){
        //LocationData ld;
        
        //ld = inputld; // とりあえず、、今はこんな形、具体的な実装は後でしろ
        //入力した位置情報のレベルが6より低かったら、、何もし無いでそのまま出す？？
        
        //return ld;
    }     

    
    public void DoAll(LocationData l,int acc_level){
        room_ids = new ArrayList<>();
        DataTransforming(l,acc_level);
        Returning(acc_level);
    }
}