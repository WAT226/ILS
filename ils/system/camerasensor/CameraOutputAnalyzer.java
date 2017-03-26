/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ils.system.camerasensor;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Wataru-T
 */
public class CameraOutputAnalyzer {
    
    public static final int DETECTION_VALUE = 9;//検知した速度がこれより大きいなら動体検知しているとみなす
    
    public List<Integer> outputanalyze(String output){
        //a b c: d e f:g h i:
        //左上 中上 右上: 左中 中中 右中: 左下 中下 右下 という感じ？
        
        //とりあえずここでは９boxのどれかが動いてれば動いているという感じにする
        //細かい変更は後で適宜調整して
        
        List<Boolean> ninebox = new ArrayList<>();
        List<Integer> speedinninebox = new ArrayList<>();
        String[] threelinespeed = output.split(" :");
        List<String> eachspeed = new ArrayList<>();
        
        for(int i=0;i<threelinespeed.length;i++){
            String[] speed = threelinespeed[i].split(" ");
            for(int j=0;j<speed.length;j++){
                eachspeed.add(speed[j]);
            }
        }
                 
        for(int i=0;i<eachspeed.size()-1;i++){
            //System.out.println(eachspeed.get(i) + "←");
            speedinninebox.add(StringtoInt(eachspeed.get(i)));
            //System.out.println(speedinninebox.get(i) + "←");
        }
        
        //System.out.println(speedinninebox);
        return speedinninebox;
    }
    
    public boolean recognizer(List<Integer> speed){
        //９個の要素を持つリスト（９ボックスのスピードが記されている）を引数
        
        //今回は９ボックスのうち一つでも動き検知していればOKとする
        //それぞれにおいて検知した・してないを判別したいならその都度変更して
        boolean detect = false;
        
        for(int i=0;i<speed.size();i++){
            if(speed.get(i) > DETECTION_VALUE){//0){
                detect = true;
                break;
            }
        }
        
        
        return detect;
    }
    
    public int StringtoInt(String str){
        
        int length = str.length();
        double num=0;
        for(int i=0;i<length;i++){
            double digit = Math.pow(10.0, (double)(length-i-1));
            //System.out.print(digit+","+str.charAt(i)+","+num+"  ");
            switch(str.charAt(i)){
                case '1':
                    num += (1 * digit);
                    break;
                case '2':    
                    num += (2 * digit);
                    break;  
                case '3':
                    num += (3 * digit);
                    break;
                case '4':    
                    num += (4 * digit);
                    break; 
                case '5':
                    num += (5 * digit);
                    break;
                case '6':    
                    num += (6 * digit);
                    break;  
                case '7':
                    num += (7 * digit);
                    break;
                case '8':    
                    num += (8 * digit);
                    break;   
                case '9':
                    num += (9 * digit);
                    break;
                default:    
                    num += (0 * digit);
                    break;                     
                    
            }
        }
        //System.out.println(num);
        return (int)num;
    }
        
}
