/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.RigidBodyControl;

/**
 *
 * @author JD
 */
public class Colision implements PhysicsCollisionListener{

    public Colision(){
        
    }
    @Override
    public void collision(PhysicsCollisionEvent event) {
        //si colision es diferente al suelo
        if(!event.getNodeB().getName().equals("Suelo")){
            System.out.println("Colision: "+event.getNodeA().getName()+" contra "+event.getNodeB().getName());   
        }
        
    }
    
    
}
