/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ils.convexhull;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.threed.Line;
import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 *
 * @author Wataru-T
 */
public class ConvexHull3D {
    //ConvexHull(3D)を表すオブジェクトクラス
    
    public List<Vector3D> c;                //凸包をなす点（凸包の頂点）へのベクトル
    public List<Plane> facet;               //ファセット、頂点のcのインデックス3つを要素と持つ
    public List<List<Integer>> facet_v;     //ファセットがcのどの頂点を持っているか、cの頂点のインデックスを格納して管理
    //(↑ファセットをcの頂点３つで管理する方式)
    public List<Line> edge;                 //辺のリスト
    public List<List<Integer>> edge_v;      //辺がどの頂点を持っているか、cの頂点のインデックスを格納して管理
    
    public ConvexHull3D(){
        c = new ArrayList<>();
        facet = new ArrayList<>();
        facet_v = new ArrayList<>();
        edge = new ArrayList<>();
        edge_v = new ArrayList<>();
    }

    
    
}
