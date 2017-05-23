/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;

/**
 *
 * @author JD
 */
public class Ruta {
   Vector3f[] pos = new Vector3f[]{
        new Vector3f(-47, -3.5f, 80f), new Vector3f(81, -3.5f, 84),
         new Vector3f(84, -3.5f, 38), new Vector3f(25, -3.5f, 33),         
         new Vector3f(23, -3.5f, -35), new Vector3f(-47, -3.5f, -35)};  
   int id=0;
   Spatial objbola;    
   RigidBodyControl objFisico;
   
   public Ruta(Spatial c,String name){
        objbola=c;
        objbola.setName(name);
        objbola.setCullHint(Spatial.CullHint.Always);
        objFisico= new RigidBodyControl(1f);  
   }
   
   public void propiedades(){
       objFisico.setGravity(new Vector3f(0,0,0));
       
   }
   
   public Vector3f posicionActual(){
       return pos[id];
   }
   
   public void cambiarPos(){
       id++;
       if(id==pos.length){
           id=0;
       }
       
   }
}
