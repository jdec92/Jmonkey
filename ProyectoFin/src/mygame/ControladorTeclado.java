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
    Coche crash;
    Coche mario;    
    
    public ControladorTeclado(Coche c,Coche m){
        crash=c;
        mario=m;
    }
    
    AnalogListener analogListener = new AnalogListener(){
        @Override
        public void onAnalog(String name, float value, float tpf) {
            
            if (name.equals("CamM")) {
               mario.cam=true;
               crash.cam=false;
            }
            if (name.equals("CamC")){
               mario.cam=false;
               crash.cam=true;
            }
            if (name.equals("MuteOFF")){
               
            }
            if (name.equals("MuteON")){
               
            }   
        }
    };   
}
