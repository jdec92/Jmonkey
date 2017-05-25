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
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;

/**
 *
 * @author JD
 */
public class Arma{
    
    Geometry balaG;
    Spatial bala;
    Vector3f posIniC = new Vector3f(-10, 4f, 7.75f);
    //Vector3f posIniC = new Vector3f(-47, -3.5f, 8.4f);
    RigidBodyControl balaFisico;
    
    
    public Arma(String name){
        Cylinder bal=new Cylinder(32,32,0.1f,0.2f,1f,true,false);
        balaG=new Geometry(name, bal);
        balaFisico=new RigidBodyControl(1f);
    }
    
    public Arma(Spatial c,String name){
        bala=c;
        bala.center();
        bala.setName(name);
        balaFisico= new RigidBodyControl(1f);                
    }
        
    
    public void aplicarFisica(){        
        balaFisico.setGravity(Vector3f.ZERO);
        balaFisico.setLinearDamping(0.5f);
    }
                
        
}

