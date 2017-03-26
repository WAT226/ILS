/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ils.system;

import ils.convexhull.ConvexHull3D;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 *
 * @author Wataru-T
 */
public class LocationData {
    //ユーザの位置情報を示すクラス

    int room_id=-1;        //部屋ID(どの部屋かを示す)
    Vector3D coordinate;    //部屋内での座標            ←どっちか
    ConvexHull3D area;      //部屋内でユーザがいうる区域  ←あればよい  
    int user_id=-1;        //ユーザID(誰であるかを示す)
    Time cur;           //データを取った時刻
    LSData ls;          //どのLSからのデータか
    List<Double> user_inference = new ArrayList<>(); //ユーザの推論値(データ統合時(Integrate.java)で付け加えられる)
    boolean subtract_flag = false; //subtractするフラグ
}
