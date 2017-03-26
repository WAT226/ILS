/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ils.system.thread;

import ils.system.LSTurn;

/**
 *
 * @author Wataru-T
 */
public class HumanSensorThread extends Thread{
    
    LSTurn lsturn;
    long ms;
    
    public HumanSensorThread(LSTurn lst,long mills){
        lsturn = lst;
        ms = mills;
    }
    
    @Override
    public void run(){
        System.out.println(" HumanSensor Thread Start.");
        lsturn.readingHumanSensor(ms); 
        System.out.println(" HumanSensor Thread ended.");
    }

}
