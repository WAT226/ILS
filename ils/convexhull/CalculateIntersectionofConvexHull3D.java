/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ils.convexhull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.math3.geometry.euclidean.threed.Line;
import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 *
 * @author Wataru-T
 */
public class CalculateIntersectionofConvexHull3D {
    //ConvexHull3Dの積集合を作るクラス
    public static final double TOLERANCE = 0.000001;
    public static final double CHECKED = -100.0;

    ConvexHull3D intersection = new ConvexHull3D();
    //String filename = "/Users/Wataru-T/Desktop/Java_writedata/Test_01intersection.ply";    
    //Vector3D colorlist;
    
    //public CalculateIntersectionofConvexHull3D(String file,Vector3D color){
        //filename = file;
        //colorlist = color;
    //}
    
    public CalculateIntersectionofConvexHull3D(){

    }    
    //public CalculateIntersectionofConvexHull3D(){
    //
    //}
    
    public ConvexHull3D Intersectioning(ConvexHull3D p,ConvexHull3D q){
        //2つの凸包の共通部分（積集合）を作る
        List<Vector3D> points = new ArrayList<>();
        
        //List<Vector3D> isp = new ArrayList<>();
        List<Vector3D> isp_a;
        List<Vector3D> isp_b;
        
        isp_a = IntersectionofEdgeandPlane(p,q);
        
        for(int i=0;i<isp_a.size();i++){
            /*
            if(isp_a.get(i).getX() == -0.0){
                double x = isp_a.get(i).getX();
                x = x + 1.0;
                x = x - 1.0;
                isp_a.set(i, new Vector3D(x,isp_a.get(i).getY(),isp_a.get(i).getZ()));
            }
            
            if(isp_a.get(i).getY() == -0.0){
                double y = isp_a.get(i).getY();
                y = y + 1.0;
                y = y - 1.0;
                isp_a.set(i, new Vector3D(isp_a.get(i).getX(),y,isp_a.get(i).getZ()));                
            }
            
            if(isp_a.get(i).getZ() == -0.0){
                double z = isp_a.get(i).getZ();
                z = z + 1.0;
                z = z - 1.0;
                isp_a.set(i, new Vector3D(isp_a.get(i).getX(),isp_a.get(i).getY(),z));                
            }
            */
            //if(!(isp.contains(isp_a.get(i)))){
            //    isp.add(isp_a.get(i));
            //}
            if(!(points.contains(isp_a.get(i)))){
                points.add(isp_a.get(i));
            }       
            
        }
        //System.out.println("isp_a.("+isp_a.size()+"):"+isp_a);        
        //System.out.println("points.size("+points.size() + "):"+points);
                
        isp_b = IntersectionofEdgeandPlane(q,p); //点がかぶらないようにする（かぶると辺の製作でzero normエラーが発生する）
        for(int i=0;i<isp_b.size();i++){
            /*
            if(isp_b.get(i).getX() == -0.0){
                double x = isp_b.get(i).getX();
                x = x + 1.0;
                x = x - 1.0;
                isp_b.set(i, new Vector3D(x,isp_b.get(i).getY(),isp_b.get(i).getZ()));
            }
            
            if(isp_b.get(i).getY() == -0.0){
                double y = isp_b.get(i).getY();
                y = y + 1.0;
                y = y - 1.0;
                isp_b.set(i, new Vector3D(isp_b.get(i).getX(),y,isp_b.get(i).getZ()));                
            }
            
            if(isp_b.get(i).getZ() == -0.0){
                double z = isp_b.get(i).getZ();
                z = z + 1.0;
                z = z - 1.0;
                isp_b.set(i, new Vector3D(isp_b.get(i).getX(),isp_b.get(i).getY(),z));                
            }            
            */
            //if(!(isp.contains(isp_b.get(i)))){
            //    isp.add(isp_b.get(i));                
            //}
            if(!(points.contains(isp_b.get(i)))){
                points.add(isp_b.get(i));
            }              
        }        
        //System.out.println("isp_b.("+isp_b.size()+"):"+isp_b);            
        //System.out.println("points.size("+points.size() + "):"+points);
        
        //isp.addAll(isp_a);
        //isp.addAll(isp_b);
        
        //List<Vector3D> inp = new ArrayList<>();
        List<Vector3D> inp_a;
        List<Vector3D> inp_b;
        
        inp_a = PointsinOtherConv(p,q);
        for(int i=0;i<inp_a.size();i++){        //点がかぶらないようにする（かぶると辺の製作でzero normエラーが発生する）
            /*
            if(inp_a.get(i).getX() == -0.0){
                double x = inp_a.get(i).getX();
                x = x + 1.0;
                x = x - 1.0;
                inp_a.set(i, new Vector3D(x,inp_a.get(i).getY(),inp_a.get(i).getZ()));
            }
            
            if(inp_a.get(i).getY() == -0.0){
                double y = inp_a.get(i).getY();
                y = y + 1.0;
                y = y - 1.0;
                inp_a.set(i, new Vector3D(inp_a.get(i).getX(),y,inp_a.get(i).getZ()));                
            }
            
            if(inp_a.get(i).getZ() == -0.0){
                double z = inp_a.get(i).getZ();
                z = z + 1.0;
                z = z - 1.0;
                inp_a.set(i, new Vector3D(inp_a.get(i).getX(),inp_a.get(i).getY(),z));                
            }            
            */
            //if(!(inp.contains(inp_a.get(i)))){
            //    inp.add(inp_a.get(i));                
            //}
            if(!(points.contains(inp_a.get(i)))){
                points.add(inp_a.get(i));
            }              
        } 
        //System.out.println("inp_a.("+inp_a.size()+"):"+inp_a);       
        //System.out.println("points.size("+points.size() + "):"+points);
                
        inp_b = PointsinOtherConv(q,p);
        for(int i=0;i<inp_b.size();i++){        //点がかぶらないようにする（かぶると辺の製作でzero normエラーが発生する）
            /*
            if(inp_b.get(i).getX() == -0.0){
                double x = inp_b.get(i).getX();
                x = x + 1.0;
                x = x - 1.0;
                inp_b.set(i, new Vector3D(x,inp_b.get(i).getY(),inp_b.get(i).getZ()));
            }
            
            if(inp_b.get(i).getY() == -0.0){
                double y = inp_b.get(i).getY();
                y = y + 1.0;
                y = y - 1.0;
                inp_b.set(i, new Vector3D(inp_b.get(i).getX(),y,inp_b.get(i).getZ()));                
            }
            
            if(inp_b.get(i).getZ() == -0.0){
                double z = inp_b.get(i).getZ();
                z = z + 1.0;
                z = z - 1.0;
                inp_b.set(i, new Vector3D(inp_b.get(i).getX(),inp_b.get(i).getY(),z));                
            }                  
            */
            //if(!(inp.contains(inp_b.get(i)))){
            //    inp.add(inp_b.get(i));
            //}
            if(!(points.contains(inp_b.get(i)))){
                points.add(inp_b.get(i));
            }              
        }           
        //System.out.println("inp_b.("+inp_b.size()+"):"+inp_b);        
        //System.out.println("points.size("+points.size() + "):"+points);
        
        //inp.addAll(inp_a);
        //inp.addAll(inp_b);
        
        //points.addAll(isp);
        
        //for(int i=0;i<inp.size();i++){        //点がかぶらないようにする（かぶると辺の製作でzero normエラーが発生する）
        //    if(!(points.contains(inp.get(i)))){
        //        points.add(inp.get(i));
        //    }
        //}         
        //points.addAll(inp);
        
        //ConvexHull3D p_and_q = MakeIntersection(p,q,points);
        /*
        System.out.println("intersectionpoints:"+points+points.size());
        
        
        for(int i=0 ; i<points.size()- 1 ; i++ ) {
            for(int j=points.size() - 1;j>i;j--){
                if (Math.abs(points.get(i).getX() - points.get(j).getX()) < TOLERANCE &&
                    Math.abs(points.get(i).getY() - points.get(j).getY()) < TOLERANCE &&
                    Math.abs(points.get(i).getZ() - points.get(j).getZ()) < TOLERANCE) {
                    points.remove(j);
                }
            }
        }
        
        Set<Vector3D> set = new HashSet<>(points);
        List<Vector3D> revisedpoints = new ArrayList<>(set);        
        System.out.println("intersectionrepoints:"+revisedpoints+revisedpoints.size());        
        */
        ConvexHull3D p_and_q = new ConvexHull3D();
        CalculateConvexhull3D calpq = new CalculateConvexhull3D(points);
        calpq.MakeConvexHull3D(p_and_q);
        
        //if(p_and_q.c.size() > 0){
        //    calpq.MakeplyFile(filename, new Vector3D(255,255,255));
        //}
        
        /*
        String plyfilename = "/Users/Wataru-T/Desktop/Java_writedata/Test_01intersection.ply";        
        List<Integer> colorlist = Arrays.asList(0,255,255);   //点のカラー成分、左から赤、緑、青    
        Visible3DbyMakingply visualv3 = new Visible3DbyMakingply(filename,p_and_q.c,p_and_q.facet_v,colorlist);       
        visualv3.WriteplyFile();  
        */
        
        //System.out.print("ConvexHull Completed. (v,f,e):"+p_and_q.c.size()+","+p_and_q.facet.size()+","+p_and_q.edge.size()+"\t" );
        //System.out.println("Intersection ConvexHull Completed.");
        //System.out.println(" Result of vertex_num():"+p_and_q.c.size());
        //System.out.println(" Result of facet.size():"+p_and_q.facet.size());
        ///System.out.println("p_and_q.facet_v.size():"+pq.facet_v.size());
        //System.out.println(" Result of edge.size() :"+p_and_q.edge.size());         
               
        return p_and_q;
               
    }      
    //
    //pの全ての辺においてqの面に交差する点
    //qの全ての辺においてpの面に交差する点
    //次にqの内部にあるpの点、pの内部にあるqの点
    //これらの点が、p,qの共通部分の多面体の頂点となる。。はず
    //続いてpのそれぞれのファセットがqの内部に入るかを調べる
    //
            
    private ConvexHull3D MakeIntersection(ConvexHull3D p,ConvexHull3D q,List<Vector3D> points){
        
        ConvexHull3D pq = new ConvexHull3D();
        List<Plane> pq_facet = new ArrayList<>();               //ファセット、頂点のcのインデックス3つを要素と持つ
        List<List<Integer>> pq_facet_v = new ArrayList<List<Integer>>();     //ファセットがcのどの頂点を持っているか、cの頂点のインデックスを格納して管理        
        
        pq.c = points;
        
        if(pq.c.size() >= 4){ //点が4個以上でないとファセットはできない。。＝共通部分もできない
            for(int i=0;i<p.facet.size();i++){
                List<Integer> pq_point_ind = new ArrayList<>();
                for(int j=0;j<points.size();j++){
                    if(p.facet.get(i).contains(points.get(j))){
                        pq_point_ind.add(j);    //pのi番目のファセットが持つpqの点のインデックスのリストになる
                    }
                }            
            
                if(pq_point_ind.size() > 0){    //pのi番目のファセットがpqの点を持ってた場合、そのファセットはpqのファセットになる
                    pq_facet.add(p.facet.get(i));
                    pq_facet_v.add(pq_point_ind);
                }
            }
        
            for(int i=0;i<q.facet.size();i++){
                List<Integer> pq_point_ind = new ArrayList<>();
                for(int j=0;j<points.size();j++){
                    if(q.facet.get(i).contains(points.get(j))){
                        pq_point_ind.add(j);
                    }
                }            
            
                if(pq_point_ind.size() > 0){
                    pq_facet.add(q.facet.get(i));
                    pq_facet_v.add(pq_point_ind);
                }
            }        
        
            pq.facet = pq_facet;
            pq.facet_v = pq_facet_v;
        
            //Facet_V_OrderAdjust_and_MakeEdge(pq);
            Facet_V_OrderAdjust(pq);
        
            //System.out.println("pq.c.size():"+pq.c.size());
            //System.out.println("pq.c:"+pq.c);

            MakeEdge(pq);
        }
        
        return pq;
    }
    
    public boolean HasInterSection(ConvexHull3D p,ConvexHull3D q){
        //２つのConvexHull3D pとqに共通部分があるかを判定
        List<Vector3D> points = new ArrayList<>();
        
        //List<Vector3D> isp = new ArrayList<>();
        List<Vector3D> isp_a;
        List<Vector3D> isp_b;
        
        isp_a = IntersectionofEdgeandPlane(p,q);
        for(int i=0;i<isp_a.size();i++){
            if(isp_a.get(i).getX() == -0.0){
                double x = isp_a.get(i).getX();
                x = x + 1.0;
                x = x - 1.0;
                isp_a.set(i, new Vector3D(x,isp_a.get(i).getY(),isp_a.get(i).getZ()));
            }
            
            if(isp_a.get(i).getY() == -0.0){
                double y = isp_a.get(i).getY();
                y = y + 1.0;
                y = y - 1.0;
                isp_a.set(i, new Vector3D(isp_a.get(i).getX(),y,isp_a.get(i).getZ()));                
            }
            
            if(isp_a.get(i).getZ() == -0.0){
                double z = isp_a.get(i).getZ();
                z = z + 1.0;
                z = z - 1.0;
                isp_a.set(i, new Vector3D(isp_a.get(i).getX(),isp_a.get(i).getY(),z));                
            }

            if(!(points.contains(isp_a.get(i)))){
                points.add(isp_a.get(i));
            }                      
        }
                
        isp_b = IntersectionofEdgeandPlane(q,p); //点がかぶらないようにする（かぶると辺の製作でzero normエラーが発生する）
        for(int i=0;i<isp_b.size();i++){
            if(isp_b.get(i).getX() == -0.0){
                double x = isp_b.get(i).getX();
                x = x + 1.0;
                x = x - 1.0;
                isp_b.set(i, new Vector3D(x,isp_b.get(i).getY(),isp_b.get(i).getZ()));
            }
            
            if(isp_b.get(i).getY() == -0.0){
                double y = isp_b.get(i).getY();
                y = y + 1.0;
                y = y - 1.0;
                isp_b.set(i, new Vector3D(isp_b.get(i).getX(),y,isp_b.get(i).getZ()));                
            }
            
            if(isp_b.get(i).getZ() == -0.0){
                double z = isp_b.get(i).getZ();
                z = z + 1.0;
                z = z - 1.0;
                isp_b.set(i, new Vector3D(isp_b.get(i).getX(),isp_b.get(i).getY(),z));                
            }            

            if(!(points.contains(isp_b.get(i)))){
                points.add(isp_b.get(i));
            }              
        }     
        if(points.size() > 0){
            return true;
        }else{
            return false;
        }
    }
    
    private void Facet_V_OrderAdjust(ConvexHull3D pq){
        for(int i=0;i<pq.facet_v.size();i++){
            //pq.facet_v(pqの面が持つ点のインデックス)を、点が隣り合うように正しい順番にならべかえるメソッド
            
            if(pq.facet_v.get(i).size() > 3){   //ファセットが三角形ならならべかえる必要はない
                List<Integer> ordered_facetv = new ArrayList<>();
                double x=0,y=0,z=0; 
                List<Vector3D> point_of_pq_facetv_i = new ArrayList<>();
                for(int j=0;j<pq.facet_v.get(i).size();j++){
                    point_of_pq_facetv_i.add(pq.c.get(pq.facet_v.get(i).get(j)));
                    x += point_of_pq_facetv_i.get(j).getX();
                    y += point_of_pq_facetv_i.get(j).getY();
                    z += point_of_pq_facetv_i.get(j).getZ();                    
                }
                Vector3D barycenter_of_pq_facetv_i = new Vector3D(x/(double)pq.facet_v.get(i).size(),
                                                                    y/(double)pq.facet_v.get(i).size(),
                                                                    z/(double)pq.facet_v.get(i).size());
                //System.out.println("重心はこの平面上にある？:"+pq.facet.get(i).contains(barycenter_of_pq_facetv_i));
                
                //重心から、point_of_pq_facetv_iの最初の点へのベクトル。以後はこれを基準に各点の位置関係を調べていく
                Vector3D criteria_v = new Vector3D(point_of_pq_facetv_i.get(0).getX() - barycenter_of_pq_facetv_i.getX(),
                                                    point_of_pq_facetv_i.get(0).getY() - barycenter_of_pq_facetv_i.getY(),
                                                    point_of_pq_facetv_i.get(0).getZ() - barycenter_of_pq_facetv_i.getZ());
                
                List<Double> criteria_and_angle = new ArrayList<>();    //基準となるベウトルと、重心から各点への各ベクトルとの角度を入れるリスト
                List<Integer> criteria_and_side = new ArrayList<>();     //各点が基準となるベクトルを挟んで左右どちらかにあるか。右なら１、左ならー１,または左なら１、右ならー１
                Vector3D crossproduct_of_j_is_1 = new Vector3D(0,0,0);
                
                //
                
                for(int j=0;j<point_of_pq_facetv_i.size();j++){
                    Vector3D barycenter_to_eachpoints = new Vector3D(point_of_pq_facetv_i.get(j).getX() - barycenter_of_pq_facetv_i.getX(),
                                              point_of_pq_facetv_i.get(j).getY() - barycenter_of_pq_facetv_i.getY(),
                                              point_of_pq_facetv_i.get(j).getZ() - barycenter_of_pq_facetv_i.getZ());
                    double angle = criteria_v.dotProduct(barycenter_to_eachpoints) / ((criteria_v.getNorm())*(barycenter_to_eachpoints.getNorm()));
                    //System.out.println("j:"+j+" cos:"+angle+" point:"+point_of_pq_facetv_i.get(j));
                    criteria_and_angle.add(angle);
                    
                    Vector3D cp = criteria_v.crossProduct(barycenter_to_eachpoints);
                    //System.out.println("crossproduct:"+cp);
                    if(j==0){
                        criteria_and_side.add(0);
                    }else if(j==1 || (crossproduct_of_j_is_1.getNorm() == 0)){
                        crossproduct_of_j_is_1 = cp;
                        if(crossproduct_of_j_is_1.getNorm() != 0){
                            criteria_and_side.add(1);
                        }else{
                            criteria_and_side.add(0);
                        }
                    }else{
                        if(crossproduct_of_j_is_1.dotProduct(cp) > 0){
                            criteria_and_side.add(1);
                        }else if(crossproduct_of_j_is_1.dotProduct(cp) < 0){
                            criteria_and_side.add(-1);
                        }else{
                            criteria_and_side.add(0);
                            System.out.println("Caution!(Facet_V_OrderAdjust)");
                        }
                    }
                }
                //System.out.println("criteria_and_side:"+criteria_and_side);
                             
                //
                
                
                ordered_facetv.add(pq.facet_v.get(i).get(0));

                List<Double> cos = new ArrayList<>();
                for(int j=0;j<point_of_pq_facetv_i.size();j++){
                    if(criteria_and_side.get(j) > 0){
                        cos.add(criteria_and_angle.get(j));
                    }else{
                        cos.add(CHECKED);
                    }
                }
                //System.out.println("cos"+cos); 
                
                int cautionflag = 0;
                while(true){
                    int nextpointind_to_clockwise = MaxIndexOfCosList(cos);
                    //System.out.print("MaxIndexofcos:"+nextpointind_to_clockwise);
                    
                    if(nextpointind_to_clockwise != 0){
                        ordered_facetv.add(pq.facet_v.get(i).get(nextpointind_to_clockwise));
                        cos.set(nextpointind_to_clockwise, CHECKED);
                        //System.out.println("cos:"+cos);
                        cautionflag++;
                        //System.out.print("a");
                    }else{
                        //System.out.println("");
                        break;
                    }
                    
                    if(cautionflag > 500){
                        System.out.println("Caution! 無限ループ発生の可能性");
                        break;
                    }
                }
                
                for(int j=0;j<point_of_pq_facetv_i.size();j++){
                    if(criteria_and_angle.get(j) <= -1.0){
                        System.out.println("角度180度の点あり。(Facet_V_OrderAdjust)");
                        ordered_facetv.add(pq.facet_v.get(i).get(j));
                    }
                    criteria_and_angle.set(j, -1*(criteria_and_angle.get(j)));
                    criteria_and_side.set(j, -1*(criteria_and_side.get(j)));
                }
                
                cos = new ArrayList<>();
                for(int j=0;j<point_of_pq_facetv_i.size();j++){
                    if(criteria_and_side.get(j) > 0){
                        cos.add(criteria_and_angle.get(j));
                    }else{
                        cos.add(CHECKED);
                    }
                }
                //System.out.println("cos"+cos); 
                
                cautionflag = 0;
                while(true){
                    int nextpointind_to_clockwise = MaxIndexOfCosList(cos);
                    //System.out.print("MaxIndexofcos:"+nextpointind_to_clockwise);
                    
                    if(nextpointind_to_clockwise != 0){
                        ordered_facetv.add(pq.facet_v.get(i).get(nextpointind_to_clockwise));
                        cos.set(nextpointind_to_clockwise, CHECKED);
                        //System.out.println("cos:"+cos);
                        cautionflag++;
                        //System.out.print("a");
                    }else{
                        //System.out.println("");
                        break;
                    }
                    
                    if(cautionflag > 300){
                        System.out.println("Caution! 無限ループ発生の可能性");
                        break;
                    }
                }    
                              
                
                //System.out.println("pq.facet_v["+i+"] :"+pq.facet_v.get(i));
                //System.out.println("ordered_facetv:"+ordered_facetv);
                //System.out.println("");
                pq.facet_v.set(i, ordered_facetv);             
            }
        }    
    }
      
    private int MaxIndexOfCosList(List<Double> l){
        //もっといい方法がある気がする。。
        int ind=0;
        double value = l.get(0);
        
        for(int i=1;i<l.size();i++){
            if(value < l.get(i)){                
                value = l.get(i);
                ind = i;
            }
        }      
        return ind;
    }
    
    private void MakeEdge(ConvexHull3D pq){
        List<Line> pq_edge = new ArrayList<>();
        List<List<Integer>> pq_edge_v = new ArrayList<List<Integer>>();
               
        for(int i=0;i<pq.facet_v.size();i++){
            //System.out.println("pq.facet_v["+i+"]:"+pq.facet_v.get(i));
            List<List<Integer>> edges = new ArrayList<List<Integer>>();             
            for(int j=0;j<pq.facet_v.get(i).size();j++){                 
                for(int k=j+1;k<pq.facet_v.get(i).size();k++){
                    List<Integer> v_ind = new ArrayList<>();
                    v_ind.add(pq.facet_v.get(i).get(j));
                    v_ind.add(pq.facet_v.get(i).get(k));
                    
                    Collections.sort(v_ind);
                    
                    for(int l=0;l<pq.facet_v.size();l++){                        
                        if((l != i) &&
                           pq.facet_v.get(l).contains(v_ind.get(0)) &&
                           pq.facet_v.get(l).contains(v_ind.get(1)) ){  
                            
                            if(pq.c.get(v_ind.get(0)).distance(pq.c.get(v_ind.get(1))) > 0.0){
                            
                                edges.add(v_ind);     
                            
                                if(!(pq_edge_v.contains(v_ind))){
                                    pq_edge_v.add(v_ind);
                                    Line e = new Line(pq.c.get(v_ind.get(0)),pq.c.get(v_ind.get(1)),TOLERANCE);
                                    pq_edge.add(e);
                                }
                            
                                break;
                            }
                        }
                    }
                }    
            }
            //System.out.println("edges:"+edges);
        }
        
        pq.edge = pq_edge;
        pq.edge_v = pq_edge_v;
        //System.out.println("pq.edge.size():"+pq.edge.size()+" pq.edge_v.size():"+pq.edge_v.size());        
    }
    
    private List<Vector3D> PointsinOtherConv(ConvexHull3D p,ConvexHull3D q){
        //pの頂点でqの内部にあるものを列挙
        List<Vector3D> inpoints = new ArrayList<>();
        Vector3D q_barycenter;
        double x=0,y=0,z=0;
        
        for(int i=0;i<q.c.size();i++){
            x += q.c.get(i).getX();
            y += q.c.get(i).getY();
            z += q.c.get(i).getZ();
        }
        x /= q.c.size();
        y /= q.c.size();
        z /= q.c.size();
        q_barycenter = new Vector3D(x,y,z);
                
        for(int i=0;i<p.c.size();i++){
            boolean p_point_in_q = true;
            for(int j=0;j<q.facet.size();j++){
                if(isPlaneVisible(q.facet.get(j),p.c.get(i),q_barycenter)){
                    p_point_in_q = false;
                    break;
                }
            }            
            
            if(p_point_in_q){
                inpoints.add(p.c.get(i));
            }
        }
        
        //System.out.println("inpoints.size():"+inpoints.size());
        //System.out.println("inpoints: "+inpoints);
        return inpoints;
    }    
    
    private boolean EdgeinOtherConv(Vector3D startpoint,Vector3D endpoint,ConvexHull3D q){
        //ある線分(startpointからendpointまで)はqの内部にあるか(または貫くか)、もしくはqの外部にあるか判定
        List<Vector3D> inpoints = new ArrayList<>();
        Vector3D q_barycenter;
        double x=0,y=0,z=0;
        
        for(int i=0;i<q.c.size();i++){
            x += q.c.get(i).getX();
            y += q.c.get(i).getY();
            z += q.c.get(i).getZ();
        }
        x /= q.c.size();
        y /= q.c.size();
        z /= q.c.size();
        q_barycenter = new Vector3D(x,y,z);
                        
        boolean startpoint_in_q = true;
        boolean endpoint_in_q = true;
        for(int i = 0;i < q.facet.size();i++){
            if(isPlaneVisible(q.facet.get(i),startpoint,q_barycenter)){
                startpoint_in_q = false;
            }
            
            if(isPlaneVisible(q.facet.get(i),endpoint,q_barycenter)){
                endpoint_in_q = false;
            }            
            
            if(!(startpoint_in_q || endpoint_in_q)){
                break;
            }
        }
        
        //System.out.println("inpoints.size():"+inpoints.size());
        //System.out.println("inpoints: "+inpoints);
        return (startpoint_in_q || endpoint_in_q);
    }        
    
    private boolean isPlaneVisible(Plane plane, Vector3D point, Vector3D barycenter) {
        double pointoffset = plane.getOffset(point);
        double baryoffset = plane.getOffset(barycenter);
        return ((pointoffset * baryoffset) < 0);   //< or <=...どっちがいいかな
    }    
    
    private List<Vector3D> IntersectionofEdgeandPlane(ConvexHull3D p,ConvexHull3D q){
        //pの辺からqの面へ
        List<Vector3D> intersectionpoint = new ArrayList<>();
        
        //まずは1つのpの辺と1つのqの面との交点を調べる
        for(int i=0;i<p.edge.size();i++){
            for(int j=0;j<q.facet.size();j++){
                Vector3D isp = q.facet.get(j).intersection(p.edge.get(i));
                if(isp != null){
                    if( (PointonLineSegment(p.c.get(p.edge_v.get(i).get(0)),p.c.get(p.edge_v.get(i).get(1)),isp))
                        && (PointonFacet(q,j,isp))
                        && !(intersectionpoint.contains(isp))   
                        && EdgeinOtherConv(p.c.get(p.edge_v.get(i).get(0)),p.c.get(p.edge_v.get(i).get(1)),q)){
                        
                        intersectionpoint.add(isp);
                        //System.out.print("edge:"+p.c.get(p.edge_v.get(i).get(0))+"->"+p.c.get(p.edge_v.get(i).get(1)));
                        //System.out.println("isp:"+isp);
                    }
                }
            }            
        }
        //System.out.println("intersectionpoint.size():"+intersectionpoint.size());
        
        return intersectionpoint;       
    }
    
    private boolean PointonLineSegment(Vector3D a,Vector3D b,Vector3D c){
        //点cが、線分ab上にあるかを判定する
        //参考：ttp://tanehp.ec-net.jp/heppoko-lab/prog/resource/3d_doc/3d_memo_apply.html
        Vector3D atob = new Vector3D(b.getX()-a.getX(),b.getY()-a.getY(),b.getZ()-a.getZ());
        Vector3D atoc = new Vector3D(c.getX()-a.getX(),c.getY()-a.getY(),c.getZ()-a.getZ()); 
        double x = atob.dotProduct(atoc) / atob.dotProduct(atob);
        //System.out.print(x+" ");
        if(0 <= x && x <= 1){
            return true;        //cは線分ab上にある
        }else{
            return false;       //cは線分ab上にない
        }
    }
    
    private boolean PointonFacet(ConvexHull3D q,int q_facet_ind,Vector3D intersectionpoint){
        //点intersectionpointは、ConvexHull3D qのq_facet_ind番目のファセットの上にあるかを判定
        //参考：ttp://www.sousakuba.com/Programming/gs_hittest_point_triangle.html
        
        List<Vector3D> q_edges =new ArrayList<>();
        List<Vector3D> v_to_isps = new ArrayList<>();
        List<Vector3D> crossproducts = new ArrayList<>();
        List<Double> dotproducts = new ArrayList<>();
        double judge=1;
        
        for(int i=0;i<q.facet_v.get(q_facet_ind).size();i++){
            //qの指定したファセットが持つ辺のベクトル
            Vector3D edge = new Vector3D(q.c.get(q.facet_v.get(q_facet_ind).get((i+1)%q.facet_v.get(q_facet_ind).size())).getX() 
                                        - q.c.get(q.facet_v.get(q_facet_ind).get(i)).getX(),
                                        q.c.get(q.facet_v.get(q_facet_ind).get((i+1)%q.facet_v.get(q_facet_ind).size())).getY() 
                                        - q.c.get(q.facet_v.get(q_facet_ind).get(i)).getY(),
                                        q.c.get(q.facet_v.get(q_facet_ind).get((i+1)%q.facet_v.get(q_facet_ind).size())).getZ() 
                                        - q.c.get(q.facet_v.get(q_facet_ind).get(i)).getZ());
            q_edges.add(edge);
            
            //qの指定したファセットが持つ頂点から点intersectionpointへのベクトル
            Vector3D vtoisp = new Vector3D(intersectionpoint.getX() - q.c.get(q.facet_v.get(q_facet_ind).get(i)).getX(),
                                            intersectionpoint.getY() - q.c.get(q.facet_v.get(q_facet_ind).get(i)).getY(),
                                            intersectionpoint.getZ() - q.c.get(q.facet_v.get(q_facet_ind).get(i)).getZ());            
            v_to_isps.add(vtoisp);
        }
        
        for(int i=0;i<q.facet_v.get(q_facet_ind).size();i++){
            //ファセットが持つ頂点を点A,B,..,点intersectionpointを点Pとしたとき、AB×BPとなるように↓のインデックスはずらす
            Vector3D c = q_edges.get(i).crossProduct(v_to_isps.get( (i+1)%q.facet_v.get(q_facet_ind).size()));
            crossproducts.add(c);
        }
        
        for(int i=0;i<crossproducts.size();i++){
            for(int j=i+1;j<crossproducts.size();j++){
                double dot = crossproducts.get(i).dotProduct(crossproducts.get(j));
                dotproducts.add(dot);
            }
        }
        
        for(int i=0;i<dotproducts.size();i++){
            judge *= dotproducts.get(i);
            if(judge < 0){              // <= 0にすべき？？
                return false;
            }
        }
        
        return true;
    }
     
    public void MakeplyFile(String plyfilename,ConvexHull3D c,Vector3D colorlist){
        
        System.out.println(plyfilename + " will be made.");
        Visible3DbyMakingply visualv3 = new Visible3DbyMakingply(plyfilename,c,colorlist);
        
        visualv3.WriteplyFile();
        
    }    
}
