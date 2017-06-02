/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

/**
 *
 * @author JD
 */
public class Coche{
    
    Ruta objetivo;
    Arma misil;
    Geometry geomBox;
    Spatial coche;    
    Vector3f posIniC = new Vector3f(-47, -4f, 8.4f);
    float velocidad=5f;    
    RigidBodyControl cocheFisico;        
    
    //coche normal
    public Coche(String name){        
        Box box=new Box(0.6f,0.6f,1f);
        geomBox=new Geometry(name, box);
        cocheFisico=new RigidBodyControl(1f);
    }
    
    public Coche(Spatial c,String name){
        coche=c;
        coche.center();
        coche.setName(name);
        cocheFisico= new RigidBodyControl(1f);                        
        
    }
    
    //coche con ruta    
    
    public Coche(String name,Ruta obj){
        objetivo=obj;
        Box box=new Box(0.6f,0.6f,1f);
        geomBox=new Geometry(name, box);
        cocheFisico=new RigidBodyControl(1f);
    }
    
    public Coche(Spatial c,String name,Ruta obj){
        objetivo=obj;
        coche=c;               
        coche.setName(name);             
        cocheFisico= new RigidBodyControl(1f);                
    }
        
    public void aplicarFisica(){        
        cocheFisico.setFriction(0.95f);
        
    }
         

    public void avanzar(){
        //geomBox.lookAt(objetivo.objetivoGeom.getLocalTranslation(), Vector3f.UNIT_Y);
        //cocheFisico.setPhysicsRotation(geomBox.getLocalRotation());
        coche.lookAt(objetivo.objetivoGeom.getLocalTranslation(), Vector3f.UNIT_Y);
        cocheFisico.setPhysicsRotation(coche.getLocalRotation());
        Vector3f dirFrente= cocheFisico.getPhysicsRotation().getRotationColumn(2);
        cocheFisico.setLinearVelocity(new Vector3f(-velocidad*dirFrente.normalize().x,-velocidad*dirFrente.normalize().y, -velocidad*dirFrente.normalize().z));                                        
                    
    }
            
    public Ray rayoFrente(){        
        Vector3f direccion=cocheFisico.getPhysicsRotation().getRotationColumn(2);
        Ray rayo= new Ray (new Vector3f (cocheFisico.getPhysicsLocation().x, cocheFisico.getPhysicsLocation().y-0.5f, cocheFisico.getPhysicsLocation().z),direccion);            
        return rayo;
    }

    

    
    
}

