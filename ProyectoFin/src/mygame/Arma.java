/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

/**
 *
 * @author JD
 */
public class Arma {

    Coche obj;
    Geometry balaG;
    Spatial bala;
    Vector3f posIniC;    
    RigidBodyControl balaFisica;
    boolean usar = false;   
    boolean lanzar=false;
    

    public Arma(String name) {                
        Box bal=new Box(0.7f,0.7f,0.7f);
        balaG = new Geometry(name, bal);
        balaFisica = new RigidBodyControl(1000f);
        posIniC=new Vector3f(100f, 100f, 0);
    }

    public Arma(Spatial s, String name, Coche c) {
        obj = c;
        bala = s;
        bala.center();
        bala.setName(name);
        balaFisica = new RigidBodyControl(1f);
        posIniC=new Vector3f(0f, 100f, 0);
    }

    public void aplicarFisica() {
        balaFisica.setGravity(Vector3f.ZERO);
        balaFisica.setLinearDamping(0.5f);
    }

    public void dectector(CollisionResults r, Vector3f pos) {        
        if (r.size() > 0 && usar) {            
            usar=false;            
            Vector3f posi = new Vector3f(pos.x, pos.y + 2, pos.z);
            balaFisica.setPhysicsLocation(posi);                        
            lanzar=true;
        }
        avanzarMD();                
    }

    public void defensa(float s1,float s2,float dm, Vector3f posCM,Vector3f posCS) {        
        if (dm<5f && usar) {                        
            balaFisica.setGravity(Vector3f.UNIT_Y);
            balaFisica.setPhysicsLocation(posCM);
            usar=false;
        }else if(s1<8f && usar || s2<8f && usar){
            //System.out.println("Aqui entra"+s1+" usar:"+usar);
            balaFisica.setGravity(Vector3f.UNIT_Y);
            balaFisica.setPhysicsLocation(posCS);
            usar=false;
        }                       
    }
    
    public void avanzarMD() {
        float velocidad = -10f;
        if (lanzar) {                        
            bala.lookAt(obj.coche.getLocalTranslation(), Vector3f.UNIT_Y);
            balaFisica.setPhysicsRotation(bala.getLocalRotation());
            Vector3f dirFrente2 = balaFisica.getPhysicsRotation().getRotationColumn(2);
            balaFisica.setLinearVelocity(new Vector3f(velocidad * dirFrente2.normalize().x, velocidad * dirFrente2.normalize().y, velocidad * dirFrente2.normalize().z));
        }

    }
    
    public void posOrigen() {
        lanzar = false;        
        balaFisica.setLinearVelocity(Vector3f.ZERO);
        balaFisica.setGravity(Vector3f.ZERO);
        balaFisica.setPhysicsLocation(posIniC);        
    }
    
    public Vector3f orientarCaja(Vector3f posCoche,int id){
        Vector3f v;
        float dist=2.5f;
        switch(id){
            case 0:
                v=new Vector3f(posCoche.x,posCoche.y,posCoche.z-dist);
                break;
            case 1:
                v=new Vector3f(posCoche.x-dist,posCoche.y,posCoche.z);
                break;
            case 2:
                v=new Vector3f(posCoche.x,posCoche.y,posCoche.z+dist);
                break;            
            case 4:
                v=new Vector3f(posCoche.x,posCoche.y,posCoche.z+dist);
                break;
            default:
                v=new Vector3f(posCoche.x+dist,posCoche.y,posCoche.z);
                break;                
        }
        return v;
    }
}
