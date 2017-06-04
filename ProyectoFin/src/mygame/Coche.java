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
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author JD
 */
public class Coche{
    int segundos,turboseg;
    class Contador extends TimerTask{
        public void run(){
            segundos++;                        
        }
    }    
    class Contador2 extends TimerTask{
        public void run(){            
            turboseg++;
        }
    }    
    
    enum tipoArma{Misil,TNT,Turbo,OFF};        
    Ruta objetivo;
    RigidBodyControl cocheFisico;      
    Geometry geomBox;
    Spatial coche;    
    Vector3f posIniC = new Vector3f(-46, -4.5f, 8.4f);
    Timer tiempoParado=new Timer();
    Timer tiempoTurbo=new Timer();
    float velocidad=5f;        
    tipoArma tipoA;    
    boolean cam;
                           
    public Coche(String name,Ruta obj){
        objetivo=obj;
        Box box=new Box(0.6f,0.6f,1f);
        geomBox=new Geometry(name, box);
        cocheFisico=new RigidBodyControl(1f);        
        cam=false;        
    }
    
    public Coche(Spatial c,String name,Ruta obj){
        objetivo=obj;
        coche=c;               
        coche.setName(name);             
        cocheFisico= new RigidBodyControl(1f);                        
        cam=false;        
        segundos=6;
        turboseg=6;
    }
        
    public void aplicarFisica(){           
        cocheFisico.setFriction(0.85f);
        cocheFisico.setLinearDamping(0.5f);
    }
         
    public void penalizacion(){
        segundos=0;
        tiempoParado=new Timer();
        tiempoParado.schedule(new Contador(),0,1000);
    }
    
    public void inicioTurbo(){
        turboseg=0;        
        tiempoTurbo=new Timer();
        tiempoTurbo.schedule(new Contador2(),0,1000);        
    }
    
    public void turbo(){
        if(turboseg<4){                       
            velocidad=10f;                                  
        }else{
            if(velocidad==10){
                tipoA=tipoArma.OFF;
                tiempoTurbo.cancel();            
                velocidad=5f;            
            }
        }        
    }    

    public void avanzar(float ds1,float ds2,Vector3f pos1,Vector3f pos2){                                                
        if(segundos>5){                                               
                //tiempoParado.cancel();
                turbo();                
                if(ds1<8f){
                    coche.lookAt(pos1, Vector3f.UNIT_Y);
                }else if(ds2<8f){
                    coche.lookAt(pos2, Vector3f.UNIT_Y);
                }else{
                    coche.lookAt(objetivo.objetivoGeom.getLocalTranslation(), Vector3f.UNIT_Y);                                
                }
                cocheFisico.setPhysicsRotation(coche.getLocalRotation());
                Vector3f dirFrente= cocheFisico.getPhysicsRotation().getRotationColumn(2);            
                cocheFisico.setLinearVelocity(new Vector3f(-velocidad*dirFrente.normalize().x,-velocidad*dirFrente.normalize().y,-velocidad*dirFrente.normalize().z));                  
        }
    }
    
    public void esquivar(CollisionResults detec,float iz,float dr){                
        Matrix3f m = new Matrix3f();
        Vector3f direccion=cocheFisico.getPhysicsRotation().getRotationColumn(2);
        if(detec.size()>0){            
            if(iz>dr){                  
                m.fromAngleAxis((float) (Math.PI/2),Vector3f.UNIT_Y);                                            
                System.out.println("Desplazando "+iz+" izquierda "+coche.getName());
            }else{                
                m.fromAngleAxis((float) (Math.PI/2),Vector3f.UNIT_Y);                                            
                System.out.println("Desplazando "+dr+" derecha "+coche.getName());
            }
            direccion=m.mult(direccion);
            cocheFisico.setLinearVelocity(direccion.mult(velocidad*20f));            
        }
    }
            
    public Ray rayoFrente(){        
        Vector3f direccion=cocheFisico.getPhysicsRotation().getRotationColumn(2);        
        Ray rayo= new Ray (new Vector3f (cocheFisico.getPhysicsLocation().x, cocheFisico.getPhysicsLocation().y+0.1f, cocheFisico.getPhysicsLocation().z),direccion);                    
        return rayo;    
    }
    
    public Ray rayoObstaculo(){        
        Vector3f direccion=cocheFisico.getPhysicsRotation().getRotationColumn(2);
        Ray rayo= new Ray (new Vector3f (cocheFisico.getPhysicsLocation().x, cocheFisico.getPhysicsLocation().y+0.1f, cocheFisico.getPhysicsLocation().z),direccion);            
        rayo.setLimit(4f);
        return rayo;
    }

    
    public Ray rayosIzq(){        
        Matrix3f m = new Matrix3f();
        Vector3f direccion=cocheFisico.getPhysicsRotation().getRotationColumn(2);
        m.fromAngleAxis((float) (Math.PI/2),Vector3f.UNIT_Y);
        direccion=m.mult(direccion);
        Ray rayo= new Ray (new Vector3f (cocheFisico.getPhysicsLocation().x, cocheFisico.getPhysicsLocation().y-0.5f, cocheFisico.getPhysicsLocation().z),direccion);                                           
        
        return rayo;
    }
    
    public Ray rayosDer(){        
        Matrix3f m = new Matrix3f();
        Vector3f direccion=cocheFisico.getPhysicsRotation().getRotationColumn(2);
        m.fromAngleAxis((float) -(Math.PI/2),Vector3f.UNIT_Y);
        direccion=m.mult(direccion);
        Ray rayo= new Ray (new Vector3f (cocheFisico.getPhysicsLocation().x, cocheFisico.getPhysicsLocation().y-0.5f, cocheFisico.getPhysicsLocation().z),direccion);                   
        return rayo;
    }

    
    
}

