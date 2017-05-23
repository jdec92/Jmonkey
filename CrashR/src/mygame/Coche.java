/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 *
 * @author JD
 */
public class Coche{
    
    Spatial coche;
    Vector3f posIniC = new Vector3f(-47, -3.5f, 8.4f);
    RigidBodyControl cocheFisico;
    
    public Coche(Spatial c,String name){
        coche=c;
        coche.setName(name);
        cocheFisico= new RigidBodyControl(196f);                
    }

    public void propiedades(float rebote,float friccion){
        cocheFisico.setRestitution(rebote);
        cocheFisico.setFriction(friccion);
    }
            
    
}
