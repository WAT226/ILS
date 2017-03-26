/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ils.system;

/**
 *
 * @author Wataru-T
 */
public class Time {
    //データを取った時刻を示す
    
    int hh;
    int mm;
    int ss;
    int ms;
    
    Time(){
        
    }
    
    Time(int hour,int minute,int second,int millsecond){
        hh = hour;
        mm = minute;
        ss = second;
        ms = millsecond;
    }
    
}
