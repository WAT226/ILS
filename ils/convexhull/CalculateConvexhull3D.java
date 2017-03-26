/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ils.convexhull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.geometry.euclidean.threed.Line;
import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import static org.apache.commons.math3.geometry.euclidean.threed.Vector3D.angle;

/**
 *
 * @author Wataru-T
 */
public class CalculateConvexhull3D {

    int POINT_NUM;  //点の個数
    int POINT_IND; //点のとる範囲
    public static final double TOLERANCE = 0.000001;
    
    File file;
    //PrintWriter pw;

    //list of random/unprocessed yet points
    List<Vector3D> v3;                                      //ランダムに生成した点へのベクトル
    List<Vector3D> allpoints;           //↑と同じ、v3は要素が削除されていくので、v3のすべての要素を取っておくためのリスト
    //points belonging on the Convex Hull
    List<List<Boolean>> g;                                  //衝突グラフ、どの面がどの点から見えるかを示す
    List<List<Integer>> Fconflict;                          //各点Pt(ただしt>r)に対して、ptから見えるCH(Pr)のファセットの集合
    
    List<List<Integer>> facet_allv;                 //    

    public CalculateConvexhull3D(){
        //サブクラス用のためのコンストラクタ
    }

    CalculateConvexhull3D(int pointnum,int pointrange) { //点の個数と範囲与えたらその範囲内にランダムに点を生成してくれるコンストラクタ
        POINT_NUM = pointnum;
        POINT_IND = pointrange;
        v3 = new ArrayList<>();
        v3.addAll(pointGenerate());                     //点へのベクトル。ランダムに生成
        g = new ArrayList<List<Boolean>>();        //衝突グラフ
        Fconflict = new ArrayList<List<Integer>>();
        allpoints = new ArrayList<>();
        allpoints.addAll(v3);
    }
    
    CalculateConvexhull3D(List<Vector3D> points){   //点が既に決まっているときはこっちのコンストラクタ

        v3 = new ArrayList<>();
        v3.addAll(AdjustingMinusZero(points));      
        g = new ArrayList<List<Boolean>>();        //衝突グラフ
        Fconflict = new ArrayList<List<Integer>>(); 
        allpoints = new ArrayList<>();
        allpoints.addAll(v3);
        POINT_NUM = v3.size();        
    }
        
    private List<Vector3D> pointGenerate() {
        //点をランダムにPOINT_NUM個生成する
        List<Vector3D> points = new ArrayList<>();
        
        for (int i = 0; i < POINT_NUM; i++) {

            //x,yをランダムに作成
            Random rnd = new Random();
            double x = rnd.nextInt(POINT_IND);
            double y = rnd.nextInt(POINT_IND);
            double z = rnd.nextInt(POINT_IND);

            if (0 <= x && x < POINT_IND
                    && 0 <= y && y < POINT_IND) {
                Vector3D v = new Vector3D(x, y, z);
                points.add(v);
            } else {
                System.out.println("Illegal x y,(0 ~ "+ POINT_IND+ ")");
            }
        }
        
        return points;
    }
    
    private List<Vector3D> AdjustingMinusZero(List<Vector3D> v){
        //vの中の被っている要素をなくす
        
        //System.out.println("points:"+v+v.size());   
        System.out.print("points.size():"+v.size());   
        List<Vector3D> adjusted_v = new ArrayList<>();
        for(int h=0;h<v.size();h++){
            //要素に-0.0を持つものがある場合がある。この-0.0を0.0に正す動作を行う
            Vector3D vh = v.get(h);
            
            double vh_x = vh.getX();            
            if(vh_x == -0.0 || vh_x == 0.0){
                vh_x = Math.abs(vh_x);
            }
            
            double vh_y = vh.getY();            
            if(vh_y == -0.0 || vh_y == 0.0){
                vh_y = Math.abs(vh_y);
            }

            double vh_z = vh.getZ();            
            if(vh_z == -0.0 || vh_z == 0.0){
                vh_z = Math.abs(vh_z);
            }
            
            Vector3D vh_adjusted = new Vector3D(vh_x,vh_y,vh_z);
            
            if(!adjusted_v.contains(vh_adjusted)){
                adjusted_v.add(vh_adjusted);
            }
        }       
        
        for(int i=0 ; i<adjusted_v.size()- 1 ; i++ ) {
            for(int j=adjusted_v.size() - 1;j>i;j--){
                if (Math.abs(adjusted_v.get(i).getX() - adjusted_v.get(j).getX()) < TOLERANCE &&
                    Math.abs(adjusted_v.get(i).getY() - adjusted_v.get(j).getY()) < TOLERANCE &&
                    Math.abs(adjusted_v.get(i).getZ() - adjusted_v.get(j).getZ()) < TOLERANCE) {
                    adjusted_v.remove(j);
                }
            }
        }         
        //System.out.println("points:"+adjusted_v+adjusted_v.size());   
        System.out.println(" -> "+adjusted_v.size());
        return adjusted_v;        
    }

    private List<Vector3D> RandomTetrahedral(ConvexHull3D ch3d) {

        /**
         * random tetrahedral points
         */
        List<Vector3D> rt_points;               //四面体をなす点（四面体の頂点）へのベクトル（４点のみ、最初しか使わない）
        rt_points = new ArrayList<>();                     //凸包をなす点へのベクトル（最初の四面体にしか使わない）

        Vector3D r13, r23;
        Random rnd = new Random();
        int r;

        for (int i = 0; i < 2; i++) {
            try{
                r = rnd.nextInt(POINT_NUM - i);
                rt_points.add(v3.get(r));     //p1,p2
                v3.remove(r);
            }catch(IllegalArgumentException e){
                System.out.println("RandomTetrahedral POINT_NUM:"+POINT_NUM);
                Logger.getLogger(CalculateConvexhull3D.class.getName()).log(Level.SEVERE, null, e);                  
            }
        }
         
        while (true) {
            r = rnd.nextInt(POINT_NUM - 2);
            Vector3D r3 = v3.get(r);

            r13 = rt_points.get(0).subtract(r3);
            r23 = rt_points.get(1).subtract(r3);
            if (angle(r13, r23) != 0) {//点p3が点p1,p2を通る直線上に無いならば
                rt_points.add(r3);         //p3を加える
                v3.remove(r);
                break;
            }
            //pw.println("RandomTetrahedral:r3 miss!");
        }

        while (true) {
            r = rnd.nextInt(POINT_NUM - 3);
            Vector3D r4 = v3.get(r);

            //Base of tetrahedron from r1, r2, r3
            try{
                Plane baseOfTH = new Plane(rt_points.get(0), rt_points.get(1), rt_points.get(2), TOLERANCE);

                //check to see if r4 is coplanar with base
                if (!baseOfTH.contains(r4)) {
                    //if not at the point 
                    rt_points.add(r4);     //p4を加える
                    v3.remove(r);
                    break;
                }
                //pw.println("RandomTetrahedral:r4 miss!");
                //pw.println("v3:"+v3);
            }catch(MathArithmeticException m){
                System.out.println("RandomTetrahedral rt0:"+rt_points.get(0)+" rt1:"+rt_points.get(1)+" rt2:"+rt_points.get(2));
                System.out.println("allpoints:"+allpoints);
                Logger.getLogger(CalculateConvexhull3D.class.getName()).log(Level.SEVERE, null, m);                
            }            
        }

        for (int i = 0; i < 4; i++) {
            for (int j = i + 1; j < 4; j++) {
                for (int k = j + 1; k < 4; k++) {
                    //pw.println(i+","+j+","+k);

                    Plane f = new Plane(rt_points.get(i), rt_points.get(j), rt_points.get(k), TOLERANCE);
                    ch3d.facet.add(f);//生成した四面体における4つのファセット（面）は
                    //どの頂点(cのインデックス)によって囲まれているかを示す
                    List<Integer> fv = new ArrayList<Integer>();

                    fv.add(i);
                    fv.add(j);
                    fv.add(k);
                    ch3d.facet_v.add(fv);   //そのファセットが持つcの頂点（cの頂点のインデックスで管理）                                            
                }
            }
        }
        return rt_points;
    }

    /**
     * Compute the lists of conflicts among facets and points. Uses the
     * barycenter as a point of reference.
     */
    private void updateConflicts(ConvexHull3D ch3d) {

        Vector3D barycenter;
        List<Boolean> pointvisibility;
        List<Integer> conflictingpoints;

        //衝突グラフ初期化
        g.clear();
        Fconflict.clear();

        //Generate the barycenter by averaging all the points
        barycenter = this.computeBarycenter(ch3d);
        //pw.println("barycenter:"+barycenter);

        for (int i = 0; i < v3.size(); i++) {       //四面体のすべての面において、点(v3)から見えるか見えないかを調べる
            pointvisibility = new ArrayList<>();        //具体的には、四面体の面(facet)において、面を挟んで点(v3)が重心と同じ側にあるか無いかで判別
            conflictingpoints = new ArrayList<>();

            //pw.print("(v3[" +i+"]:"  + v3.get(i).getX() + "," +v3.get(i).getY() + "," + v3.get(i).getZ()+")" );
            //pw.print("("+i+"):");


            for (int j = 0; j < ch3d.facet.size(); j++) {

                if (isPlaneVisible(ch3d.facet.get(j), v3.get(i), barycenter)) {
                    pointvisibility.add(true);          //面を挟んで点が重心と違う側にあれば,その面は点から見えるのでtrue
                    conflictingpoints.add(j);             //面(facet(j))が点v3(i)から見えるということを示す
                    //pw.print(" ◎");
                    //pw.print("("+j+"),");
                } else {
                    pointvisibility.add(false);         //違うならfalse
                    //pw.print(" ×,");
                }
            }
            g.add(pointvisibility);                     //すべての面、点においてやり、２次元配列gで管理する
            Fconflict.add(conflictingpoints);
            //pw.println("");
        }

    }
    
    /**
     * Computes the convex hull for a set of given random points
     *
     * @return the convex hull, as a list of points
     */
    public void MakeConvexHull3D(ConvexHull3D ch3d) {

        int i;
        //list of indexes of removed facets
        //TODO this may be better as a set
        List<Integer> removed_facet_v;
        List<Integer> visiblezone_border_pointind;
        // list of the horizon points (their indexes into c)
        List<Integer> L;
        
        //デバッグ用ログ書き込みファイル
        /*
        Calendar time = Calendar.getInstance();
        file = new File("/Users/Wataru-T/Desktop/Java_writedata/CalculateConvexhull3D_log"+
            time.YEAR+time.MONTH+time.DATE+time.HOUR+time.MINUTE+time.SECOND+".txt");        
        try{
            if (!checkBeforeWritefile(file)){
                file.createNewFile();
                System.out.println(file +"を作りました");
            }               
            pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
        }catch(IOException e){
            System.out.println(e);
        }             
        */
        
        //System.out.println("v3.size():"+v3.size());
        
        if(v3.size() >= 4){
        
            //1.４面体を構成するv3の4点p1,p2,p3,p4を求める
            List<Vector3D> rt_points = RandomTetrahedral(ch3d);

            //pw.println("四面体の頂点:"+rt_points);
        
            //2.c <- ch({p1,p2,p3,p4})
            ch3d.c.addAll(rt_points);

            //3.残りの点のランダムな順列p5,..,pnを求める、これはpointGenerateでランダムに作成してるからOK
            //4.全ての可視点対(pt,f)を求めて衝突グラフgを初期化する。
            //ただしfはcのファセットであり、t>4である。
            updateConflicts(ch3d);

            //System.out.println("c:"+c);
            //System.out.println("v3.size():"+v3.size());
            double percentage_counter = 0.1;
            //System.out.print("v3: (size):"+v3.size());
            //5. for r <- 5 to n                            
            for (i = 0; i < POINT_NUM - 4; i++) {

                if((double)i/(double)(POINT_NUM-4) > percentage_counter){
                    //pw.println("v3の" + (i+5) + "個目の点.");
                    //pw.print((int)(100 * (double)i/(double)(POINT_NUM-4))+"%, ");
                    //pw.println("i:"+i+","+(double)i/(double)(POINT_NUM-4)+"  ,  "+percentage_counter);
                    percentage_counter = (double)((int)(percentage_counter * 10)) * 0.1 + 0.1;
                }
            
                //6.do(*prをcに挿入する*)
                //this v3.get(0) is removed later on. Always getting the first
                //vector of v3
                //System.out.println("ch3d.c.size:"+ch3d.c.size() +" ,v3.size:"+v3.size());
                ch3d.c.add(v3.get(0));

                //7.if(Fconflict(pr)は空でない)
                //  (*すなわち、prはcの外にある*))
                if (!(Fconflict.get(0).isEmpty())) {

                    //削除されるファセットはcのどの頂点(のインデックス)を持っているか                                
                    removed_facet_v = new ArrayList<Integer>();
                    //prから見える領域の境界をなすcの頂点(のインデックス)
                    visiblezone_border_pointind = new ArrayList<Integer>();
                    //↑を頂点が実際に隣り合うように並べかえたもの

                    //8.then Fconflict(pr)のファセット(点prから見えるファセット)をすべてCから削除する
                    //その前に9.の下準備、削除されるファセットが接するcの頂点(のインデックス)がどれであるかを求める(removed_facet_vに格納される)
                    //pw.println("まだ削除前のfacet_v:"+ch3d.facet_v);
                    for (int j = 0; j < Fconflict.get(0).size(); j++) {

                        int facet_ind = Fconflict.get(0).get(j);    //削除される予定のファセットのインデックス
                        //pw.print("v3["+i+"]から見えるファセット["+facet_ind+"]は削除される。これについて見る。 ");
                        for (int k = 0; k < ch3d.facet_v.get(facet_ind).size(); k++) {
                            if (!(removed_facet_v.contains(ch3d.facet_v.get(facet_ind).get(k)))) {
                                removed_facet_v.add(ch3d.facet_v.get(facet_ind).get(k));
                                //pw.print(ch3d.facet_v.get(facet_ind).get(k));
                            }
                        }
                        //pw.println("");                    
                    }

                    //removed_facet_vに正しく格納されているかの確認
                    //pw.println("削除される予定のfacet_v(removed_facet_v):"+removed_facet_v);
                    //16.で使用するので削除されるファセットとそれが接する頂点のインデックスを取っておく
                    List<Plane> removed_facets = new ArrayList<>();
                    List<List<Integer>> removed_fv = new ArrayList<>();

                    //ファセット削除
                    //pw.println("削除前のfacet_v:"+ch3d.facet_v);
                    //pw.println("Fconflict.get(v3の" +(i+5)+ "個目の点).size():" + Fconflict.get(0).size());
                    for (int j = 0; j < Fconflict.get(0).size(); j++) {
                        int removed_facet_index = Fconflict.get(0).get(j) - j;
                        //pw.println("removed_facet_index:"+ removed_facet_index + " Fconflict.get(0).get("+j+"):"+Fconflict.get(0).get(j));
                        removed_facets.add(ch3d.facet.get(removed_facet_index));
                        ch3d.facet.remove(removed_facet_index); //ArrayListは要素を削除すると自動的にインデックスがずれるので-jで調整   
                        removed_fv.add(ch3d.facet_v.get(removed_facet_index));
                        ch3d.facet_v.remove(removed_facet_index);
                    }
                    //pw.println("removed_fv:" + removed_fv);
                    //pw.println("削除後のfacet_v:"+ch3d.facet_v);

                    //9.prから見える領域の境界を順にたどり(その領域はFconflict(pr)のファセットから構成されている)地平面の辺のリストLを順に作る
                    //ここでは頂点のリストを作る。地平面をなす頂点は、先ほど削除したファセットのいずれかと、削除されずに残っているファセットのいずれかの両方に接している。
                    //そのため、地平面をなす頂点は、先ほど削除したファセットが接する頂点のリストremoved_facet_vと、
                    //削除されずに残っているファセットが接する頂点のリストfacet_vの両方にあるといえる。
                    //そのため、地平面をなす頂点のリストvisiblezone_border_pointindを作成するには、
                    //removed_facet_vとfacet_vの共通項のみを取り出す必要がある。
                    for (int j = 0; j < ch3d.facet_v.size(); j++) {
                        for (int k = 0; k < ch3d.facet_v.get(j).size(); k++) {
                            if ((removed_facet_v.contains(ch3d.facet_v.get(j).get(k))
                                    && !(visiblezone_border_pointind.contains(ch3d.facet_v.get(j).get(k))))) {
                                visiblezone_border_pointind.add(ch3d.facet_v.get(j).get(k));
                            }
                        }
                    }

                    L = computeHorizon(ch3d,removed_fv, visiblezone_border_pointind);

                    //10. for すべての e∈L
                    for (int j = 0; j < L.size(); j++) {

                        //ファセット更新
                        int a = L.get(j);
                        int b = L.get((j + 1) % L.size());
                        //pw.println("v3["+i+"]"+","+j+"(a,b):"+a+","+b);   
                        //pw.println("現在のfacet_v:"+ch3d.facet_v);
                        Plane f = addNewFacet(ch3d,a, b);
                        //pw.println("(新しいファセットを加えた後の)facet_v:"+ch3d.facet_v);                    
                        int nearest_facet_ind = getNearestFaceIndex(ch3d,a, b);

                        //12. if fがeに沿った近傍のファセットと同一平面にある 
                        if(ch3d.facet.get(nearest_facet_ind).isSimilarTo(f) &&
                                (nearest_facet_ind != ch3d.facet_v.size()-1)){
                            //pw.println("同一平面！,nearest_facet_ind:"+nearest_facet_ind+", c,size:"+ch3d.c.size());         
                            //pw.println("f :"+ch3d.facet_v.get(ch3d.facet_v.size()-1));
                            //pw.println("f':"+ch3d.facet_v.get(nearest_facet_ind));
                            //pw.println("L.size():"+L.size()+" L:"+L);
                            //13.then fとf'を1つのファセットに統合する。ただし、その衝突リストはf'のものと同じである
                            //→つまり四角い（もしくはそれより上の）ファセットができる？
                        
                            //対応するfacet_vに頂点のインデックス追加→そこだけ点のリストが3つより多くなる
                            //ch3d.facet_v.get(nearest_facet_ind).add(ch3d.c.size()-1); ・・・↓のfacet_v_order_adjustで行わせる
                        
                            //ここで変更されたch3d.facet_v.get(nearest_facet_ind)は隣り合う辺が正しい順番で並んでるとは限らないので並べ替える必要がある
                            //(たまにだが、f'にすでにch3d.c.size()-1がある時がある。この時はこの操作はしない)←これはJUnitテストで判明
                            if(! (ch3d.facet_v.get(nearest_facet_ind).contains(ch3d.c.size()-1))){
                                facet_v_order_adjust(ch3d,nearest_facet_ind,a,b);
                            }
                        
                            //ファセット統合に伴い、11.で追加したfacet,facet_vは消しておく
                            int facet_size = ch3d.facet.size()-1;
                            int facetv_size = ch3d.facet_v.size()-1;
                            ch3d.facet.remove(facet_size);
                            ch3d.facet_v.remove(facetv_size);
                        }else{
                            //14.else (*fの衝突リストを定める*)
                            //pw.println("updateconflict.v3[0]はまだ削除してないから、初めのv3[0]は必ず全部×になる？");
                            updateConflicts(ch3d);       //使えるかな？？解らないけど。。。

                            //15.gにfに対する節点を作る     ↑のupdateConflicts()でやっている？？
                            //16.ここでf1とf2を、元の凸包でeに接していたファセットとする
                            Plane f1 = ch3d.facet.get(nearest_facet_ind);
                            int f2_ind = removed_fv.size() + 1; //これも引っかかるはず、ひっかからなかったらセグフォ
                            for (int k = 0; k < removed_fv.size(); k++) {
                                //pw.print("removed_fv["+k+"]:"+removed_fv.get(k));                        
                                if (removed_fv.get(k).contains(a)
                                        && removed_fv.get(k).contains(b)) {
                                    f2_ind = k;
                                    //pw.println("f2 Cleared! ");
                                    break;
                                }
                                //pw.println("");
                            }
                            Plane f2 = removed_facets.get(f2_ind);
                        
                            //17.P(e) <- Pconflict(f1) ∪ Pconflict(f2)
                            //this is copy paste from updateConflicts()
                            List<Vector3D> Pe = new ArrayList<>();

                            Vector3D barycenter = computeBarycenter(ch3d);

                            for (int k = 0; k < v3.size(); k++) {
                                if (isPlaneVisible(f1, v3.get(k), barycenter) || isPlaneVisible(f2, v3.get(k), barycenter)) {
                                    Pe.add(v3.get(k));
                                }
                            }

                            //pw.println("g(tate):"+g.size()+" g(yoko):"+g.get(0).size()+" facet.size():"+ch3d.facet.size());
                            //18. for すべての点p∈P(e)    
                            for (int k = 0; k < Pe.size(); k++) {

                                //19.do fがpから見えるなら、(p,f)をgに加える
                                double bf = f.getOffset(barycenter);
                                double pkf = f.getOffset(Pe.get(k));
                            
                                if (isPlaneVisible(f, Pe.get(k), barycenter)) {
                                    int pkind = v3.indexOf(Pe.get(k));
                                    int find = ch3d.facet.indexOf(f);
                                    g.get(pkind).add(find, true);
                                    //System.out.println("18.added!");
                                }

                            }
                        }
                        //pw.println("削除・調整後のfacet_v"+ch3d.facet_v);
                    }

                    //20.prに対応する節点とFconflict(pr)のファセットに対応する節点をそれらに接続する枝とともにgから削除する
                    //つまりprをv3,g,Fconflictとかから削除する(g,Fconflictはv3から削除後CGInitで再構成)

                    v3.remove(0);
                    updateConflicts(ch3d);
                                            
                }else{
                    //点(v3)から見えるファセット（面）がない → その点はConvexHullの中にあるということになるので凸包をなす点のリストcから削除する
                    ch3d.c.remove(ch3d.c.size()-1);
                    v3.remove(0);//そういえばやってなかったような。。いるかな？
                    //System.out.println("v3("+i+") removed.");
                    updateConflicts(ch3d);
                }
                //System.out.println("c:"+c);
                RemovePointOnEdge(ch3d);            
            }
        
            removeInnerPoints(ch3d);
            
            System.out.println("");
        

            MakeEdge(ch3d);
        
            //System.out.print("ConvexHull Completed. (v,f,e):"+ch3d.c.size()+","+ch3d.facet.size()+","+ch3d.edge.size()+"\t" );
            
            //System.out.print(" v:"+ch3d.c.size());
            //System.out.print(" f:"+ch3d.facet.size());
            //System.out.println(" Result of facet_v:"+ch3d.facet_v);        
            //System.out.print(" e:"+ch3d.edge.size());
  
            //pw.println("c         :"+ch3d.c);
            //pw.println("allpoint  :"+allpoints);
            //pw.println("facet_v   :"+ch3d.facet_v);
            facet_allv = facet_v_to_allv(allpoints,ch3d);
            //pw.println("facet_allv:"+facet_allv);        
        
            //21. return c
            //return c;
        
            //pw.close();
        
        }else{
            System.out.println("point.size():"+v3.size()+",so cannot create ConvexHull3D.");
            ch3d.c = null;
        }
    }
    
    private void RemovePointOnEdge(ConvexHull3D ch3d){
        //計算結果のConvexHull3Dには辺の上にある点がある場合がある。その点を削除、調整する
        
        List<Integer> remove_v_index = new ArrayList<>();
        //pw.println("RemovePointOnEdge");
        //pw.println("削除、調整する前のc      :"+ch3d.c);
        //pw.println("削除・調整する前のfacet_v:"+ch3d.facet_v);
        
        for(int i=0;i<ch3d.c.size();i++){
            int onplanes_count = 0;//その点がいくつの平面上にあるか
            for(int j=0;j<ch3d.facet.size();j++){
                if(ch3d.facet.get(j).contains(ch3d.c.get(i))){
                    onplanes_count++;
                }
            }
            
            //if(onplanes_count == 1){
            //    //この場合はあるかわからないけど。、、
            //    pw.println(ch3d.c.get(i)+"is on 1 plane.");                   
            //}else 
            if(onplanes_count <= 2){
                //pw.println(ch3d.c.get(i)+"is on "+ onplanes_count +" planes.");
                remove_v_index.add(i);
            }           
        }
        
        for(int i=0;i<remove_v_index.size();i++){
            int removevind = remove_v_index.get(i) - i;
            
            //該当する頂点の削除
            ch3d.c.remove(removevind);
            
            //削除に伴い、facet_vの該当する点も削除し、インデックスもずらす
            for(int j=0;j<ch3d.facet_v.size();j++){
                ch3d.facet_v.get(j).remove((Integer)removevind);
                for(int k=0;k<ch3d.facet_v.get(j).size();k++){
                    if(ch3d.facet_v.get(j).get(k) > removevind){
                        ch3d.facet_v.get(j).set(k, ch3d.facet_v.get(j).get(k)-1);
                    }
                }
            }
        }
        
        //pw.println("削除、調整後のc      :"+ch3d.c);
        //pw.println("削除・調整後のfacet_v:"+ch3d.facet_v);        
        
    }
    
    private void MakeEdge(ConvexHull3D ch3d){
        //ConvexHull3Dの辺のリストを作る
        List<List<Integer>> edge_by_index_of_vertex = new ArrayList<List<Integer>>();
       
        for(int i=0;i<ch3d.facet_v.size();i++){
            for(int j=0;j<ch3d.facet_v.get(i).size();j++){
                List<Integer> oneedge_by_index_of_vertex = new ArrayList<>();                
                oneedge_by_index_of_vertex.add(ch3d.facet_v.get(i).get(j));
                oneedge_by_index_of_vertex.add(ch3d.facet_v.get(i).get((j+1)%ch3d.facet_v.get(i).size()));   
                
                Collections.sort(oneedge_by_index_of_vertex);  
                if (!(edge_by_index_of_vertex.contains(oneedge_by_index_of_vertex))) {
                    edge_by_index_of_vertex.add(oneedge_by_index_of_vertex);
                }                
            }
        }
        //pw.println("edge_by_index_of_vertex:"+edge_by_index_of_vertex);
        
        for(int i=0;i<edge_by_index_of_vertex.size();i++){
            Vector3D a = ch3d.c.get(edge_by_index_of_vertex.get(i).get(0));
            Vector3D b = ch3d.c.get(edge_by_index_of_vertex.get(i).get(1));
            Line oneedge = new Line(a,b,TOLERANCE);
            ch3d.edge.add(oneedge);
            ch3d.edge_v.add(edge_by_index_of_vertex.get(i));
        }
    }
    
    private void facet_v_order_adjust(ConvexHull3D ch3d,int nearest_facet_ind,int a,int b){
        
        List<Integer> integrated_facetv = ch3d.facet_v.get(nearest_facet_ind);
        int insert_index = -1;  //下の処理で書き換えられるはず。書き換えられなかったらおかしいのでセグフォ起こさせるような初期値にする
        //pw.println("a:"+a+"b:"+b+" integrated_facetv(before):"+integrated_facetv);
        
        for(int i=0;i<integrated_facetv.size();i++){
            if( ((integrated_facetv.get(i) == a) && (integrated_facetv.get((i+1)%integrated_facetv.size()) == b))  ||
                ((integrated_facetv.get(i) == b) && (integrated_facetv.get((i+1)%integrated_facetv.size()) == a))){
                insert_index = (i+1) % integrated_facetv.size();
                break;
            }
        }
        integrated_facetv.add(insert_index, ch3d.c.size()-1);
        //pw.println("insert_index:"+insert_index+" integrated_facetv(after):"+integrated_facetv);
        ch3d.facet_v.set(nearest_facet_ind, integrated_facetv);
    }

    private void removeInnerPoints(ConvexHull3D ch3d) {
        //またファセットの削除、凸包cの拡張に伴い凸包cの内部に入ることになったcの点もここで削除しておく(違う所で削除しておくべきか？)
        List<Integer> pointoffacetv = new ArrayList<>();
        for(int j=0;j<ch3d.facet_v.size();j++){
            for(int k=0;k<ch3d.facet_v.get(j).size();k++){
                int pfv = ch3d.facet_v.get(j).get(k);
                
                if(!(pointoffacetv.contains(pfv))){
                    pointoffacetv.add(pfv);
                }                         
            }
        }            
        //pw.println("pointoffacetv.size:"+pointoffacetv.size());
        //pw.println(pointoffacetv);
        //pw.println("c.size:"+ch3d.c.size());
        if(pointoffacetv.size() < ch3d.c.size()){
            List<Integer> insidepointind = new ArrayList<>();
            for(int k=0;k<ch3d.c.size();k++){
                insidepointind.add(k);
            }
            insidepointind.removeAll(pointoffacetv);
            //pw.println("insidepointind:"+insidepointind);
            for(int k=0;k<insidepointind.size();k++){
                //pw.println("Removed:c"+ch3d.c.get(insidepointind.get(k)-k));
                ch3d.c.remove(insidepointind.get(k)-k);
            }
            //削除したcの点に伴い、facet_vのインデックスもずらしておく
            //pw.println("facet_v(before):"+ch3d.facet_v);
            int facet_v_size = ch3d.facet_v.size();
            for(int k=0;k<insidepointind.size();k++){
                for(int l=0;l<facet_v_size;l++){
                    List<Integer> facet_v_ind_adjuster = ch3d.facet_v.get(l);                    
                    for(int m=0;m<ch3d.facet_v.get(l).size();m++){
                        if(ch3d.facet_v.get(l).get(m) > insidepointind.get(k)-k){
                            facet_v_ind_adjuster.set(m,ch3d.facet_v.get(l).get(m)-1);
                        }
                    }
                    ch3d.facet_v.set(l, facet_v_ind_adjuster);
                }
            //pw.println("facet_v(after ):"+ch3d.facet_v);                
            }

        }
        
        updateConflicts(ch3d);//削除したcの点に伴い
    }

    /**
     * tests whether plane is visible from point, using barycenter of the convex
     * hull. If the barycenter and the point lie on different half-spaces of the
     * plane, then this point is external to the plane, thus the plane is
     * visible from point.
     * 
     * @param plane the plane to check against
     * @param point the point from which we view the plane
     * @param barycenter the current barycenter of the convex hull
     * @return Whether plane is visible from point.
     */
    private boolean isPlaneVisible(Plane plane, Vector3D point, Vector3D barycenter) {
        double pointoffset = plane.getOffset(point);
        double baryoffset = plane.getOffset(barycenter);
        
        if(plane.contains(point)){
            return false;
        }
        
        return ((pointoffset * baryoffset) < 0.0);   //< or <=...どっちがいいかな
    }

    public Vector3D computeBarycenter(ConvexHull3D ch3d) {
        //Generate the barycenter by averaging all the points
        Vector3D barycenter = new Vector3D(0, Vector3D.ZERO);
        //System.out.println("barycenter(1):"+barycenter);
        for (Vector3D point : ch3d.c) {
            barycenter = barycenter.add(point);
            //System.out.println("barycenter(2):"+barycenter);
        }
        barycenter = barycenter.scalarMultiply(1.0 / ch3d.c.size());
        
        //System.out.println("barycenter(3):"+barycenter);
        return barycenter;
    }

    private int getNearestFaceIndex(ConvexHull3D ch3d,int a, int b) {
        int nearest_facet_ind = ch3d.facet_v.size() + 1; //辺eをもつ現存のファセットのインデックス。ひっかかるはず。ひっかからなかったら後でセグフォ起こるはず
        for (int k = 0; k < ch3d.facet_v.size(); k++) {
            //System.out.print("facet_v["+k+"]:"+facet_v.get(k));
            if (ch3d.facet_v.get(k).contains(a) && ch3d.facet_v.get(k).contains(b)) {
                
                //if(ch3d.facet_v.get(k).contains(ch3d.c.size()-1)){
                //    System.out.println("引っかかった、a:"+a+" b:"+b+" c.size-1:"+ (ch3d.c.size()-1)+" ch3d.facet_v.get(k)"+ch3d.facet_v.get(k));
                //}else{
                
                nearest_facet_ind = k;
                //System.out.println("getNearestFaceIndex.OK. ");
                break;
                //}
            }
            //System.out.println("");
        }
        if(nearest_facet_ind == -1) System.out.println("getNearestFaceIndex:Error!!!!!");
        //同一平面か判定
        //if(facet.get(nearest_facet_ind).getOffset(c.get(c.size()-1)) == 0){                    
        return nearest_facet_ind;
    }

    private List<Integer> computeHorizon(ConvexHull3D ch3d,List<List<Integer>> removed_fv, List<Integer> visiblezone_border_pointind) {
        List<Integer> L = new ArrayList<>();
        //visiblezone_border_pointindに正しく格納されているかの確認
        //System.out.println("visiblezone_border_pointind:"+visiblezone_border_pointind);

        //頂点が順になるようにvisiblezone_border_pointindを並び替えてLに格納
        //(ここではLは辺のリストではなく、頂点のリストとする)
        
        List<List<Integer>> periphery = new ArrayList<List<Integer>>();
        for(int j=0;j<removed_fv.size();j++){
            for(int k=0;k<removed_fv.get(j).size();k++){
                List<Integer> edge_of_removedfv = new ArrayList<>();
                edge_of_removedfv.add(removed_fv.get(j).get(k));
                edge_of_removedfv.add(removed_fv.get(j).get((k+1)%removed_fv.get(j).size()));
                Collections.sort(edge_of_removedfv);
                
                if(periphery.contains(edge_of_removedfv)){
                    //int index = periphery.indexOf(edge_of_removedfv);
                    periphery.remove(edge_of_removedfv);
                }else{
                    periphery.add(edge_of_removedfv);
                }
            }
        }
        //System.out.println("periphery:"+periphery);
        
        int vbp_size = visiblezone_border_pointind.size();
        L.add(periphery.get(0).get(0));
        for (int j = 0; j < vbp_size; j++) {
            //System.out.println("visiblezone_border_pointind:"+visiblezone_border_pointind);                
            //System.out.println("L("+j+"):"+L);
            //System.out.println("periphery("+j+"):"+periphery);
            for (int k = 0; k < periphery.size(); k++) {
                if (periphery.get(k).contains(L.get(j))) {
                    periphery.get(k).removeAll(L);
                    if (periphery.get(k).size() > 0) {
                        L.add(periphery.get(k).get(0));
                        //System.out.println("periphery.get("+k+"):"+periphery.get(k)+" visiblezone_border_pointind:"+visiblezone_border_pointind);
                        //commonedge.remove(k);
                        break;
                    }
                }
            }
        }                        
        //Lが正しく格納されているかの確認
        //System.out.println("L:" + L + "(visiblezone_border_pointind:"+visiblezone_border_pointind +")");
        return L;
    }
    
    private Plane addNewFacet(ConvexHull3D ch3d,int a, int b) {
        //11.do 三角形のファセットfを作ることにより、eをprにつなぐ
        //ここではすべてのe(∈L)をprにつなぐ
        //その結果、ファセット(facet,facet_v)を更新する必要がある

        //System.out.print("a:"+a+" b:"+b+" ");
        if((a == b) || (b == (ch3d.c.size()-1)) || ((ch3d.c.size()-1) == a)){
            System.out.println("WARNING!!!!! a:"+a+" b:"+b+" ch3d.c.size()-1:"+(ch3d.c.size() - 1));
        }
        
        try{
            
        Plane f = new Plane(ch3d.c.get(a), ch3d.c.get(b), ch3d.c.get(ch3d.c.size() - 1), TOLERANCE);//最後のはv3.get(i)

        ch3d.facet.add(f);//生成した四面体における4つのファセット（面）は
        //どの頂点(cのインデックス)によって囲まれているかを示す              
        List<Integer> fv = new ArrayList<Integer>();

        fv.add(a);
        fv.add(b);
        fv.add(ch3d.c.size() - 1);
        ch3d.facet_v.add(fv);   //そのファセットが持つcの頂点（cの頂点のインデックスで管理）                    

        return f;
        
        }catch(MathArithmeticException e){
            System.out.println("WARNING!!!!! a:"+a+",("+ch3d.c.get(a)+") b:"+b+",("+ch3d.c.get(b)+") ch3d.c.size()-1:"+(ch3d.c.size() - 1)+",("+ch3d.c.get(ch3d.c.size()-1));
            //pw.println("WARNING!!!!! a:"+a+",("+ch3d.c.get(a)+") b:"+b+",("+ch3d.c.get(b)+") ch3d.c.size()-1:"+(ch3d.c.size() - 1)+",("+ch3d.c.get(ch3d.c.size()-1));                       
            return new Plane(ch3d.c.get(a), ch3d.c.get(b), ch3d.c.get(ch3d.c.size() - 1), TOLERANCE);
        }          
    }
    
    public void MakeplyFile(String plyfilename,Vector3D colorlist){
        
        System.out.println(plyfilename + " will be made.");
        Visible3DbyMakingply visualv3 = new Visible3DbyMakingply(plyfilename,allpoints,facet_allv,colorlist);
        
        visualv3.WriteplyFile();
        
    }
    
    private List<List<Integer>> facet_v_to_allv(List<Vector3D> allv,ConvexHull3D ch3d){
        //最終的な答えとして出力した凸包の点は、もともとPOINT_NUM個生成した点v3の何番目の点だったか
        //ansの点がv3のどのインデックスだったかを求める(.plyを書くために必要な処理)
        List<Integer> v_map = new ArrayList<>();
        List<List<Integer>> v_allv = new ArrayList<List<Integer>>();
        
        for(int i=0;i<ch3d.c.size();i++){
            v_map.add(allv.indexOf(ch3d.c.get(i)));
        }
        
        //System.out.println("v_map:"+v_map);
        
        for(int i=0;i<ch3d.facet_v.size();i++){
            List<Integer> vtoallv = new ArrayList<>();
            for(int j=0;j<ch3d.facet_v.get(i).size();j++){
                vtoallv.add(v_map.get(ch3d.facet_v.get(i).get(j)));
            }
            v_allv.add(vtoallv);            
        }
        
        return v_allv;
    }
}
