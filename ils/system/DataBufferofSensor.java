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
public class DataBufferofSensor extends DataBuffer{
    
    boolean detect;
    
    @Override
    public void SetData(boolean d){
        this.detect = d;
    }
    
    @Override
    public boolean TakeSensorData(){
        
        boolean data = this.detect;
        this.detect = false;
        
        return data;
    }
    
    @Override
    public Object TakeData(){
        boolean data = this.detect;
        this.detect = false;
        
        return data;
    }
}
