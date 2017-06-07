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
    Audio audio;
    
    public ControladorTeclado(Coche c,Coche m,Audio a){
        crash=c;
        mario=m;
        audio=a;
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
                    audio.audio_base.setVolume(3);
                    audio.audio_seta.setVolume(2);
                    audio.audio_fin.setVolume(2);               
            }
            if (name.equals("MuteON")){
                    audio.audio_base.setVolume(0);
                    audio.audio_seta.setVolume(0);
                    audio.audio_fin.setVolume(0);               
            }   
        }
    };   
}
