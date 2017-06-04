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
       new Vector3f(-46, -4.5f, 89.5f),new Vector3f(90f, -4.5f, 84),             
       new Vector3f(82, -4.5f, 30),new Vector3f(18.5f, -4.5f, 36f),         
       new Vector3f(22, -4.5f, -42.5f),new Vector3f(-53, -4.5f, -37)};  
   Vector3f[] pos2 = new Vector3f[]{
       new Vector3f(-48, -4.5f, 89.5f),new Vector3f(90f, -4.5f, 82),
       new Vector3f(84, -4.5f, 30),new Vector3f(18.5f, -4.5f, 34f),
       new Vector3f(24, -4.5f, -42.5f),new Vector3f(-53, -4.5f, -35)};  
   int id=0;      
   
   Geometry objetivoGeom;   
   RigidBodyControl objFisico;
   
   public Ruta(){
       Sphere malla = new Sphere(32,32,0.4f,true,false);
       objetivoGeom=new Geometry("Bola", malla);       
       objFisico = new RigidBodyControl(0f);
   }
   
   public void aplicarFisica(){
       objetivoGeom.setCullHint(Spatial.CullHint.Always);
       objFisico.setGravity(Vector3f.ZERO);
       
   }
 
   public Vector3f posicionActual(){
       return pos[id];
   }
   
   public void cambiarPos(float dist){
       if(dist<5f){
                id++;
            if(id==pos.length){
                id=0;
            }
            objFisico.setPhysicsLocation(posicionActual());
       }
       
   }
      
   
//Metodo para navegacion 2------------------------------------------------------------------------------
   
   public Vector3f posicionActual2(){
       return pos2[id];
   }
      
   
   public void cambiarPos2(float dist){
       if(dist<5f){
                id++;
            if(id==pos2.length){
                id=0;
            }
            objFisico.setPhysicsLocation(posicionActual2());            
            
       }
       
   }
   
}
