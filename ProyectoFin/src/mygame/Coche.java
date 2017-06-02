/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Matrix3f;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.system.Timer;

/**
 *
 * @author JD
 */
public class Coche{
    
    Ruta objetivo;
    RigidBodyControl cocheFisico;      
    Geometry geomBox;
    Spatial coche;    
    Vector3f posIniC = new Vector3f(-47, -4f, 8.4f);
    float velocidad=5f;    
    Timer tiempoParado;
          
    
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
         

    public void avanzar(float ds1,float ds2,Vector3f pos1,Vector3f pos2){        
        if(tiempoParado.getTimeInSeconds()>5){
                        if(ds1<8f){
                coche.lookAt(pos1, Vector3f.UNIT_Y);
            }else if(ds2<8f){
                coche.lookAt(pos2, Vector3f.UNIT_Y);
            }else{
                coche.lookAt(objetivo.objetivoGeom.getLocalTranslation(), Vector3f.UNIT_Y);
            }

            cocheFisico.setPhysicsRotation(coche.getLocalRotation());
            Vector3f dirFrente= cocheFisico.getPhysicsRotation().getRotationColumn(2);
            cocheFisico.setLinearVelocity(new Vector3f(-velocidad*dirFrente.normalize().x,-velocidad*dirFrente.normalize().y, -velocidad*dirFrente.normalize().z));                                        
        }
                   
    }
    
    public void esquivar(CollisionResults detec,float iz,float dr){        
        float fuerza=cocheFisico.getMass()*30*1.5f;
        Matrix3f m = new Matrix3f();
        Vector3f direccion=cocheFisico.getPhysicsRotation().getRotationColumn(2);
        if(detec.size()>0){
            if(iz>dr){
                //fuerza para izquierda                            
                m.fromAngleAxis((float) (Math.PI/2),Vector3f.UNIT_Y);                            
                System.out.println("Girando a la izquierda");
            }else{
                //fuerza para derecha
                m.fromAngleAxis((float) (Math.PI/2),Vector3f.UNIT_Y);            
                System.out.println("Girando a la derecha");
            }
            direccion=m.mult(direccion);
            cocheFisico.setLinearVelocity(direccion.mult(velocidad*30f));
            //cocheFisico.applyImpulse(direccion.mult(fuerza), cocheFisico.getPhysicsLocation());
            //System.out.println("Aplicando fuerza");
        }
        
    }
            
    public Ray rayoFrente(){        
        Vector3f direccion=cocheFisico.getPhysicsRotation().getRotationColumn(2);
        Ray rayo= new Ray (new Vector3f (cocheFisico.getPhysicsLocation().x, cocheFisico.getPhysicsLocation().y-0.5f, cocheFisico.getPhysicsLocation().z),direccion);            
        return rayo;    
    }
    
    public Ray rayoObstaculo(){        
        Vector3f direccion=cocheFisico.getPhysicsRotation().getRotationColumn(2);
        Ray rayo= new Ray (new Vector3f (cocheFisico.getPhysicsLocation().x, cocheFisico.getPhysicsLocation().y-0.5f, cocheFisico.getPhysicsLocation().z),direccion);            
        rayo.setLimit(10f);
        return rayo;
    }

    
    public Ray rayosIzq(){        
        Matrix3f m = new Matrix3f();
        Vector3f direccion=cocheFisico.getPhysicsRotation().getRotationColumn(2);
        m.fromAngleAxis((float) (Math.PI/2),Vector3f.UNIT_Y);
        direccion=m.mult(direccion);
        Ray rayo= new Ray (new Vector3f (cocheFisico.getPhysicsLocation().x, cocheFisico.getPhysicsLocation().y, cocheFisico.getPhysicsLocation().z),direccion);                                           
        
        return rayo;
    }
    
    public Ray rayosDer(){        
        Matrix3f m = new Matrix3f();
        Vector3f direccion=cocheFisico.getPhysicsRotation().getRotationColumn(2);
        m.fromAngleAxis((float) -(Math.PI/2),Vector3f.UNIT_Y);
        direccion=m.mult(direccion);
        Ray rayo= new Ray (new Vector3f (cocheFisico.getPhysicsLocation().x, cocheFisico.getPhysicsLocation().y, cocheFisico.getPhysicsLocation().z),direccion);                   
        return rayo;
    }

    
    
}

