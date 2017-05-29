/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.state.RootNodeAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.RigidBodyControl;

/**
 *
 * @author JD
 */
public class Colision implements PhysicsCollisionListener{
    boolean cambio;
    boolean cambio1;
    Ruta obj;
    Arma misilJ,misilE;
    Seta seta1,seta2;
    
    public Colision(Ruta r,Arma mj,Arma me,Seta s1,Seta s2){
        obj=r;
        misilJ=mj;
        misilE=me;
        seta1=s1;
        seta2=s2;
        cambio=true;
        cambio1=true;
    }        
    @Override
    public void collision(PhysicsCollisionEvent event) {
        //si colision es diferente al suelo
        if(!event.getNodeB().getName().equals("Suelo")){
            if(cambio && event.getNodeB().getName().equals("Bola") && event.getNodeA().getName().equals("Enemigo")){
                cambio=false;
                obj.cambiarPos();
                //System.out.println("Colision++: "+event.getNodeA().getName()+" contra "+event.getNodeB().getName());   
            }else if(cambio1 && event.getNodeA().getName().equals("Enemigo") && event.getNodeB().getName().equals("MisilJ")){
                cambio1=false;
                misilJ.posOrigen();
                //System.out.println("Colision--: "+event.getNodeA().getName()+" contra "+event.getNodeB().getName());       
            }else if(cambio1 && event.getNodeA().getName().equals("Jugador") && event.getNodeB().getName().equals("MisilE")){
                cambio1=false;
                misilE.usar=false;
                misilE.posOrigen();
                    //System.out.println("Colision: "+event.getNodeA().getName()+" contra "+event.getNodeB().getName());       
            }else if(cambio1 && event.getNodeB().getName().substring(0,4).equals("Seta")){
                cambio1=false;
                if(seta1.seta.getName().equals(event.getNodeB().getName())){
                    seta1.cambiarPos();
                    if(event.getNodeA().getName().equals("Enemigo")){
                        misilE.usar=true;
                    }
                }else if(seta2.seta.getName().equals(event.getNodeB().getName())){
                    seta2.cambiarPos();
                    if(event.getNodeA().getName().equals("Enemigo")){
                        misilE.usar=true;
                    }
                }
            }
                
                   
                              
        }
        
    }
    
    
}
