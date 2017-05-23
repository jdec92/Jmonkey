/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 *
 * @author JD
 */
public class Seta {
    Spatial seta;    
    RigidBodyControl setaFisico;    
    
    public Seta(Spatial c,String name){
        seta=c;
        seta.setName(name);
        setaFisico= new RigidBodyControl(9.8f);                
    }
    
    public void propiedades(){
        setaFisico.setGravity(new Vector3f(0,0,0));
    }
}
