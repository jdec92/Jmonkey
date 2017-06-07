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
    Ruta ruta;
    Spatial bala;
    Vector3f[] posIniC;    
    RigidBodyControl balaFisica;
    int id;
    boolean rastrea=false;
    boolean usar = false;    
    boolean lanzar=false;
    
//caja TNT
    public Arma(String name,int i) {                
        Box bal=new Box(0.7f,0.7f,0.7f);
        balaG = new Geometry(name, bal);
        balaFisica = new RigidBodyControl(10f);
        id=i;
        posIniC=new Vector3f[]{new Vector3f(100f, -4f, 0),new Vector3f(120,-4,0)};
        
    }

    public Arma(Spatial s, String name,int i, Coche c,Ruta r) {
        ruta=r;
        obj = c;
        bala = s;
        id=i;
        bala.center();
        bala.setName(name);
        balaFisica = new RigidBodyControl(1f);
        posIniC=new Vector3f[]{new Vector3f(-100f, -4f, 0),new Vector3f(-120,-4f,0)};
        
    }

    public void aplicarFisicaM() {        
        bala.setCullHint(Spatial.CullHint.Always);
        balaFisica.setGravity(Vector3f.ZERO);
        balaFisica.setLinearDamping(0.5f);
    }

    public void aplicarFisicaC() {        
        balaG.setCullHint(Spatial.CullHint.Always);        
    }
    
    public void dectector(CollisionResults r, Vector3f pos) {                  
        if (r.size() > 0 && usar) {            
            usar=false;            
            bala.setCullHint(Spatial.CullHint.Inherit);
            Vector3f posi = new Vector3f(pos.x, pos.y + 2, pos.z);
            balaFisica.setLinearVelocity(Vector3f.ZERO);
            balaFisica.setPhysicsLocation(posi);                        
            lanzar=true;
            rastrea=false;
        }
        avanzarMD();                
    }

    public void lanzar(float ds1,float ds2,float dmc,int id,Vector3f pos){        
        if(ds1<5f && usar || ds2<5f && usar){
            usar=false;
            bala.setCullHint(Spatial.CullHint.Inherit);            
            Vector3f posi = new Vector3f(pos.x, pos.y + 2, pos.z);
            balaFisica.setLinearVelocity(Vector3f.ZERO);
            balaFisica.setPhysicsLocation(posi);                        
            ruta.actualizarPos(id);
            lanzar=true;            
            rastrea=true;            
        }else if(dmc<6f && !usar){
            rastrea=false;
        }                
    }
    
    public void defensa(float s1,float s2,float dm, Vector3f posCM,Vector3f posCS) {        
        if (dm<5f && usar) {                        
            balaG.setCullHint(Spatial.CullHint.Inherit);
            balaFisica.setLinearVelocity(Vector3f.ZERO);
            balaFisica.setPhysicsLocation(posCM);
            usar=false;
        }else if(s1<8f && usar || s2<8f && usar){                       
            balaG.setCullHint(Spatial.CullHint.Inherit);
            balaFisica.setLinearVelocity(Vector3f.ZERO);
            balaFisica.setPhysicsLocation(posCS);
            usar=false;
        }                       
    }
    
    public void avanzarMD() {  
        float velocidad=-7f;
        if (lanzar) {  
            if(rastrea){
                velocidad=-15f;
                bala.lookAt(ruta.objetivoGeom.getLocalTranslation(), Vector3f.UNIT_Y);                
            }else{                
                bala.lookAt(obj.coche.getLocalTranslation(), Vector3f.UNIT_Y);
            }            
            balaFisica.setPhysicsRotation(bala.getLocalRotation());
            Vector3f dirFrente2 = balaFisica.getPhysicsRotation().getRotationColumn(2);
            balaFisica.setLinearVelocity(new Vector3f(velocidad * dirFrente2.normalize().x, velocidad * dirFrente2.normalize().y, velocidad * dirFrente2.normalize().z));
        }
    }
    
    public void posOrigen() {
        lanzar = false;     
        if(bala!=null){
            bala.setCullHint(Spatial.CullHint.Always);
        }else{
            balaG.setCullHint(Spatial.CullHint.Always);
        }                        
        balaFisica.setPhysicsLocation(posIniC[id]);        
    }
    
    public Vector3f orientarCaja(Vector3f posCoche,int id){
        Vector3f v;
        float dist=2.5f;
        float altura=0.5f;
        switch(id){
            case 0:
                v=new Vector3f(posCoche.x,posCoche.y+altura,posCoche.z-dist);
                break;
            case 1:
                v=new Vector3f(posCoche.x-dist,posCoche.y+altura,posCoche.z);
                break;
            case 2:
                v=new Vector3f(posCoche.x,posCoche.y+altura,posCoche.z+dist);
                break;            
            case 4:
                v=new Vector3f(posCoche.x,posCoche.y+altura,posCoche.z+dist);
                break;
            default:
                v=new Vector3f(posCoche.x+dist,posCoche.y+altura,posCoche.z);
                break;                
        }
        return v;
    }
}
