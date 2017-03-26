/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ils.convexhull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 *
 * @author Wataru-T
 */
public class CalculateUnionConvexHull3D {
    //ConvexHull3Dの和集合を作るクラス
    public static final double TOLERANCE = 0.000001;
    
    List<ConvexHull3D> ch3ds = new ArrayList<>();
    
    String filename = "/Users/Wataru-T/Desktop/Java_writedata/Test_01union.ply";    
    
    public CalculateUnionConvexHull3D(){

    }
    
    
    public ConvexHull3D Unioning(ConvexHull3D p, ConvexHull3D q){
        
        List<Vector3D> points = new ArrayList<>();
        
        points.addAll(p.c);
        
        for(int i=0;i<q.c.size();i++){  //点がかぶらないようにする（かぶると辺の製作でzero normエラーが発生する）
            if(!(points.contains(q.c.get(i)))){
                points.add(q.c.get(i));
            }
        }        
        //points.addAll(q.c);
        
        CalculateConvexhull3D cal_union = new CalculateConvexhull3D(points);        
        ConvexHull3D pq_union = new ConvexHull3D();        
        cal_union.MakeConvexHull3D(pq_union);
        
        //if(pq_union.c.size() > 0){
        //    cal_union.MakeplyFile(filename, new Vector3D(255,255,0));
        //}
        
        //String plyfilename = "/Users/Wataru-T/Desktop/Java_writedata/Test_01union.ply";       
        //List<Integer> colorlist = Arrays.asList(0,128,128);   //点のカラー成分、左から赤、緑、青    
        //Visible3DbyMakingply visualv3 = new Visible3DbyMakingply(filename,pq_union.c,pq_union.facet_v,colorlist);       
        //visualv3.WriteplyFile();    
        
        System.out.println("Union ConvexHull Completed.");
        System.out.println(" Result of vertex_num():"+pq_union.c.size());
        System.out.println(" Result of facet.size():"+pq_union.facet.size());
        //System.out.println("p_and_q.facet_v.size():"+pq.facet_v.size());
        System.out.println(" Result of edge.size() :"+pq_union.edge.size());        
        
        
        return pq_union;
    }
    
    public void MakeplyFile(String plyfilename,ConvexHull3D c,Vector3D colorlist){
        
        System.out.println(plyfilename + " will be made.");
        Visible3DbyMakingply visualv3 = new Visible3DbyMakingply(plyfilename,c,colorlist);
        
        visualv3.WriteplyFile();
        
    } 
    
}
