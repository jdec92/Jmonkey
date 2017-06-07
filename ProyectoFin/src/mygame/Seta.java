/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.Random;

/**
 *
 * @author JD
 */
public class Seta {
    Spatial seta;    
    RigidBodyControl setaFisico;    
    int id=0;
    Vector3f[] posSetas = new Vector3f[]{
        new Vector3f(-47, -4f, 45f),
        new Vector3f(-25, -4f, -35f),        
        new Vector3f(35, -4f, 85f),
        new Vector3f(0, -4f, 85),
        new Vector3f(25, -4f, 0),
        new Vector3f(85, -4f, 60),};
    
    public Seta(Spatial c,String name){
        seta=c;
        seta.setName(name);
        setaFisico= new RigidBodyControl(1f);                
    }
    
    public void propiedades(){
        setaFisico.setGravity(Vector3f.ZERO);        
    }
    
    public Vector3f posicionActual(){
       return posSetas[id];
   }
    
     public void cambiarPos(int id_otra){
       Random r=new Random();
       int num=r.nextInt(posSetas.length);       
       while(num==id_otra || num==id){
           num=r.nextInt(posSetas.length);
       }       
       id=num;
       setaFisico.setLinearVelocity(Vector3f.ZERO);
       setaFisico.setPhysicsLocation(posicionActual());
   }
}
