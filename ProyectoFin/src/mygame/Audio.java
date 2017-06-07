/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;

/**
 *
 * @author JD
 */
public class Audio {
    AudioNode audio_seta,audio_fin,audio_base,audio_cturbo,audio_mturbo,audio_boom;
    
    public Audio(AudioNode s,AudioNode f,AudioNode b,AudioNode ct,AudioNode mt,AudioNode bm){
            /* Al chocar con la seta. */               
       audio_seta=s;
       audio_seta.setPositional(false);
       audio_seta.setLooping(false);
       audio_seta.setVolume(2);
       
       audio_cturbo=ct;
       audio_cturbo.setPositional(false);
       audio_cturbo.setLooping(false);
       audio_cturbo.setVolume(2);
       
       audio_mturbo=mt;
       audio_mturbo.setPositional(false);
       audio_mturbo.setLooping(false);
       audio_mturbo.setVolume(2);
       
       audio_boom=bm;
       audio_boom.setPositional(false);
       audio_boom.setLooping(false);
       audio_boom.setVolume(2);

       // Al terminar el juego
       
       audio_fin=f;
       audio_fin.setPositional(false);
       audio_fin.setLooping(true);
       audio_fin.setVolume(2);
       //rootNode.attachChild(audio_fin);

       /* Base del juego. */
       
       audio_base=b;
       audio_base.setPositional(false);
       audio_base.setLooping(true);  // activate continuous playing    
       audio_base.setVolume(3);           
    }
    
    public void inicio(){
        audio_base.play();
    }
    
    public void cogeSeta(){
        audio_seta.playInstance();
    }
    
    public void fin(){
        audio_base.stop();
        audio_fin.play();
    }
    
    public void turbo_crash(){
        audio_cturbo.playInstance();
    }
    
    public void turbo_mario(){
        audio_mturbo.playInstance();
    }
    
    public void explosion(){
        audio_boom.playInstance();
    }
}
