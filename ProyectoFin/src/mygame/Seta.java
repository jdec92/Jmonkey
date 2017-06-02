/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 *
 * @author JD
 */
public class Seta {
    Spatial seta;    
    RigidBodyControl setaFisico;    
    int id=0;
    Vector3f[] posSetas = new Vector3f[]{
        new Vector3f(-47, -4, 60f), new Vector3f(-13, -4, -37),
         new Vector3f(41, -4, 35), new Vector3f(50, -4, 84)};
    
    public Seta(Spatial c,String name){
        seta=c;
        seta.setName(name);
        setaFisico= new RigidBodyControl(1f);                
    }
    
    public void propiedades(){
        setaFisico.setGravity(new Vector3f(0,0,0));
    }
    
    public Vector3f posicionActual(){
       return posSetas[id];
   }
    
     public void cambiarPos(){
       id++;
       if(id==posSetas.length){
           id=0;
       }         
       setaFisico.setPhysicsLocation(posicionActual());
       //System.out.println("Cambia pos seta");
   }
}
