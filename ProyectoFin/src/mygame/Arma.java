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
public class Arma {

    Coche obj;
    Geometry balaG;
    Spatial bala;
    Vector3f posIniC = new Vector3f(0, 100f, 0);
    Vector3f posIniC2 = new Vector3f(0, -100f, 0f);
    RigidBodyControl balaFisica;
    boolean enmov = false;
    boolean usar = false;    
    

    public Arma(String name, Coche c) {
        
        obj = c;
        Box bal=new Box(0.5f,0.5f,0.5f);
        balaG = new Geometry(name, bal);
        balaFisica = new RigidBodyControl(1000f);
    }

    public Arma(Spatial s, String name, Coche c) {
        obj = c;
        bala = s;
        bala.center();
        bala.setName(name);
        balaFisica = new RigidBodyControl(1f);
    }

    public void aplicarFisica() {
        balaFisica.setGravity(Vector3f.ZERO);
        balaFisica.setLinearDamping(0.5f);
    }

    public void dectector(CollisionResults r, Vector3f pos) {
        
        if (r.size() > 0 && usar) {
            enmov = true;
            Vector3f posi = new Vector3f(pos.x, pos.y + 2, pos.z);
            balaFisica.setPhysicsLocation(posi);
            
        }
        avanzarMD();        
        
        
    }

    public void defensa(float d, Vector3f pos) {
        
        if (d<2f && usar) {            
            Vector3f posi = new Vector3f(pos.x, pos.y, pos.z);
            balaFisica.setGravity(Vector3f.UNIT_Y);
            balaFisica.setPhysicsLocation(posi);
            usar=false;
        }        
        
        
    }
    
    public void avanzarMD() {
        float velocidad = -10f;
        if (enmov) {
            usar=false;
            //balaG.lookAt(obj.coche.getLocalTranslation(), Vector3f.UNIT_Y);
            //balaFisica.setPhysicsRotation(balaG.getLocalRotation());        
            bala.lookAt(obj.coche.getLocalTranslation(), Vector3f.UNIT_Y);
            balaFisica.setPhysicsRotation(bala.getLocalRotation());
            Vector3f dirFrente2 = balaFisica.getPhysicsRotation().getRotationColumn(2);
            balaFisica.setLinearVelocity(new Vector3f(velocidad * dirFrente2.normalize().x, velocidad * dirFrente2.normalize().y, velocidad * dirFrente2.normalize().z));
        }

    }
    
    public void posOrigen() {
        enmov = false;        
        balaFisica.setLinearVelocity(Vector3f.ZERO);
        balaFisica.setGravity(Vector3f.ZERO);
        balaFisica.setPhysicsLocation(posIniC2);
    }
}
