/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.input.controls.AnalogListener;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;

/**
 *
 * @author JD
 */
public class ControladorTeclado {    
    Coche jug;
    Coche enem;    
    
    public ControladorTeclado(Coche j,Coche e){
        jug=j;
        enem=e;
    }
    
    AnalogListener analogListener = new AnalogListener(){
        @Override
        public void onAnalog(String name, float value, float tpf) {
            
            if (name.equals("CamE")) {
               enem.cam=true;
               jug.cam=false;
            }
            if (name.equals("CamJ")){
                enem.cam=false;
               jug.cam=true;
            }
            if (name.equals("MuteOFF")){
                enem.cam=false;
               jug.cam=true;
            }
            if (name.equals("MuteON")){
                enem.cam=false;
               jug.cam=true;
            }   
        }
    };   
}
