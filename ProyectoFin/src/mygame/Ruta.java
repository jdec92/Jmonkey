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
        new Vector3f(-47, -4f, 85f), new Vector3f(85, -4f, 84),
         new Vector3f(85, -4f, 35), new Vector3f(22, -4f, 35),         
         new Vector3f(23, -4f, -38), new Vector3f(-49, -4f, -38)};  
   int id=0;
   
   Geometry objetivoGeom;
   Spatial objbola;    
   RigidBodyControl objFisico;
   
   public Ruta(){
       Sphere malla = new Sphere(32,32,0.4f,true,false);
       objetivoGeom=new Geometry("Bola", malla);       
       objFisico = new RigidBodyControl(1f);
   }
   
   public void aplicarFisica(){
       objetivoGeom.setCullHint(Spatial.CullHint.Always);
       objFisico.setRestitution(0f);
   }
   
   public Vector3f posicionActual(){
       return pos[id];
   }
   
   public void cambiarPos(){
       id++;
       if(id==pos.length){
           id=0;
       }
       objFisico.setPhysicsLocation(posicionActual());
   }
}
