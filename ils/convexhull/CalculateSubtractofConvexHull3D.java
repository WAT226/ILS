/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ils.convexhull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 *
 * @author Wataru-T
 */
public class CalculateSubtractofConvexHull3D {
    //ConvexHull3Dの差集合を作るクラス
    public static final double TOLERANCE = 0.000001;
    public static final double CHECKED = -100.0;

    ConvexHull3D intersection = new ConvexHull3D();
    String filename = "/Users/Wataru-T/Desktop/Java_writedata/Test_01subtract.ply";
    Vector3D colorlist;
    
    public CalculateSubtractofConvexHull3D(){

    }
    
    public ConvexHull3D Subtracting(ConvexHull3D p,ConvexHull3D q){
        //2つの凸包の差集合を作る(p - q)
        //手順としては
        //1.pとqの交点の座標を調べる
        //2.pの頂点でqの内部にないものを調べる        
        //3. 1.と2.の座標でMakeConvexHullすればよい
        
        List<Vector3D> points = new ArrayList<>();
        
        //List<Vector3D> isp = new ArrayList<>();
        List<Vector3D> isp_a;
        List<Vector3D> isp_b;
        
        //pの辺からqの面への交点
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
            
            //if(!(isp.contains(isp_a.get(i)))){
            //    isp.add(isp_a.get(i));
            //}
            if(!(points.contains(isp_a.get(i)))){
                points.add(isp_a.get(i));
            }                      
        }
        //System.out.println("isp_a.("+isp_a.size()+"):"+isp_a);        
        //System.out.println("isp.size():"+isp.size());
                
        //qの辺からpの面への交点
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
            
            //if(!(isp.contains(isp_b.get(i)))){
            //    isp.add(isp_b.get(i));                
            //}
            if(!(points.contains(isp_b.get(i)))){
                points.add(isp_b.get(i));
            }              
        }        
        //System.out.println("isp_b.("+isp_b.size()+"):"+isp_b);            
        //System.out.println("isp.size():"+isp.size());
        
        //List<Vector3D> inp = new ArrayList<>();
        List<Vector3D> inp_a;
        List<Vector3D> inp_b;
        
        inp_a = PointsNotinOtherConv(p,q);
        for(int i=0;i<inp_a.size();i++){        //点がかぶらないようにする（かぶると辺の製作でzero normエラーが発生する）
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
            
            //if(!(inp.contains(inp_a.get(i)))){
            //    inp.add(inp_a.get(i));                
            //}
            if(!(points.contains(inp_a.get(i)))){
                points.add(inp_a.get(i));
            }              
        } 
        //System.out.println("inp.size():"+inp.size());    
               
        //System.out.println("subtractionpoints:"+points);
        
        /*
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
        //System.out.println("subtractionrepoints:"+revisedpoints);
        */
        
        ConvexHull3D p_minus_q = new ConvexHull3D();
        CalculateConvexhull3D calpq = new CalculateConvexhull3D(points);
        calpq.MakeConvexHull3D(p_minus_q);
        
        //if(p_minus_q.c.size() > 0){
        //    calpq.MakeplyFile(filename,colorlist);
        //}
        
        //System.out.print("ConvexHull Completed. (v,f,e):"+p_minus_q.c.size()+","+p_minus_q.facet.size()+","+p_minus_q.edge.size() +"\t");        
        //System.out.println("Subtraction ConvexHull Completed.");
        //System.out.println(" Result of vertex_num():"+p_minus_q.c.size());
        //System.out.println(" Result of facet.size():"+p_minus_q.facet.size());
        ///System.out.println("p_and_q.facet_v.size():"+pq.facet_v.size());
        //System.out.println(" Result of edge.size() :"+p_minus_q.edge.size());         
               
        return p_minus_q;
               
    }      
    
    private List<Vector3D> PointsNotinOtherConv(ConvexHull3D p,ConvexHull3D q){
        //pの頂点でqの内部にないものを列挙
        List<Vector3D> outpoints = new ArrayList<>();
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
            
            if(!p_point_in_q){
                outpoints.add(p.c.get(i));
            }
        }
        
        //System.out.println("inpoints.size():"+inpoints.size());
        //System.out.println("inpoints: "+inpoints);
        return outpoints;
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
