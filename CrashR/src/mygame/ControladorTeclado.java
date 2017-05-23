/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.input.controls.AnalogListener;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;

/**
 *
 * @author JD
 */
public class ControladorTeclado {    
    Coche coche;
    public ControladorTeclado(Coche obj){
        coche=obj;        
    }
    
    AnalogListener analogListener = new AnalogListener(){
        @Override
        public void onAnalog(String name, float value, float tpf) {

            Vector3f dirFrente = coche.cocheFisico.getPhysicsRotation().getRotationColumn(2);

            float fuerza = coche.cocheFisico.getMass() * 10f;

            if (name.equals("Derecha")) {
                coche.cocheFisico.applyTorque(new Vector3f(0, -(fuerza/10), 0));
                //System.out.println("Nodo Coche: "+cocheFisico);                
            }
            if (name.equals("Izquierda")) {
                coche.cocheFisico.applyTorque(new Vector3f(0, (fuerza/10), 0));
            }
            if (name.equals("Avanzar")) {
                coche.cocheFisico.applyCentralForce(dirFrente.normalize().mult(fuerza));
                //System.out.println("Angulo "+dirFrente.toString());
            }
            if (name.equals("Atras")) {
                //cocheFisico.applyCentralForce(new Vector3f(0,0,fuerza));
                coche.cocheFisico.applyCentralForce(dirFrente.normalize().mult(-fuerza));
            }                       
            if (name.equals("Depie")) {
                coche.cocheFisico.clearForces();
                Matrix3f mat = new Matrix3f();
                mat.fromAngleAxis(1, new Vector3f(0, -90, 0));
                coche.cocheFisico.setPhysicsRotation(mat);
                coche.cocheFisico.setPhysicsLocation(coche.posIniC);                
            }
        }
    };   
}
