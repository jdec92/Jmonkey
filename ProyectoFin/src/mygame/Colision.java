/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import java.util.Random;

/**
 *
 * @author JD
 */
public class Colision implements PhysicsCollisionListener{
    boolean cambio;
    boolean cambio1;
    Ruta obj;
    Arma misilJD,misilED,cajaJ,cajaE;
    Seta seta1,seta2;
    int tipoA;
    
    public Colision(Ruta r,Arma mjd,Arma med,Arma cj,Arma ce,Seta s1,Seta s2){
        obj=r;
        misilJD=mjd;
        misilED=med;        
        cajaJ=cj;
        cajaE=ce;
        seta1=s1;
        seta2=s2;
        cambio=true;
        cambio1=true;
    }        
    @Override
    public void collision(PhysicsCollisionEvent event) {
        //si colision es diferente al suelo
        if(!event.getNodeB().getName().equals("Suelo")){
            //si el enemigo cocha con la esfera
            if(cambio && event.getNodeB().getName().equals("Bola") && event.getNodeA().getName().equals("Enemigo")){
                cambio=false;
                obj.cambiarPos();
                //System.out.println("Colision++: "+event.getNodeA().getName()+" contra "+event.getNodeB().getName());   
            }
            //si el enemigo le impacta algún misil
            else if(cambio1 && event.getNodeA().getName().equals("Enemigo") && event.getNodeB().getName().equals("MisilJ")){
                cambio1=false;                
                misilJD.posOrigen();                
                //System.out.println("Colision--: "+event.getNodeA().getName()+" contra "+event.getNodeB().getName());       
            }
            //si al jugador le impacta algún misil
            else if(cambio1 && event.getNodeA().getName().equals("Jugador") && event.getNodeB().getName().equals("MisilE")){
                cambio1=false;                
                misilED.posOrigen();
                    //System.out.println("Colision: "+event.getNodeA().getName()+" contra "+event.getNodeB().getName());       
            }
            //si alguien colisiona con una seta
            else if(cambio1 && event.getNodeB().getName().substring(0,4).equals("Seta")){
                cambio1=false;
                Random r=new Random();
                tipoA=1;                
                if(event.getNodeB().getName().equals("Seta1")){
                    seta1.cambiarPos();
                    if(event.getNodeA().getName().equals("Enemigo")){
                        switch(tipoA){
                            case 0:
                                if(!cajaE.usar){
                                misilED.usar=true;
                                }
                                break;
                            case 1:                           
                                if(!misilED.usar){
                                    cajaE.usar=true;
                                }                                
                                break;
                        }
                    }
                    if(event.getNodeA().getName().equals("Jugador")){
                        switch(tipoA){
                            case 0:
                                if(!cajaJ.usar){
                                misilJD.usar=true;
                                }
                                break;
                            case 1:                           
                                if(!misilJD.usar){
                                    cajaJ.usar=true;
                                }                                
                                break;
                        }
                    }
                }else if(event.getNodeB().getName().equals("Seta2")){
                    seta2.cambiarPos();
                    if(event.getNodeA().getName().equals("Enemigo")){
                        switch(tipoA){
                            case 0:
                                if(!cajaE.usar){
                                misilED.usar=true;
                                }
                                break;
                            case 1:                           
                                if(!misilED.usar){
                                    cajaE.usar=true;
                                }                                
                                break;
                        }
                    }
                    if(event.getNodeA().getName().equals("Jugador")){
                        switch(tipoA){
                            case 0:
                                if(!cajaJ.usar){
                                misilJD.usar=true;
                                }
                                break;
                            case 1:                           
                                if(!misilJD.usar){
                                    cajaJ.usar=true;
                                }                                
                                break;
                        }
                    }
                }
                System.out.println("Arma seleccionada: "+tipoA);
            }
            //si el misil cocha con la caja
            else if(cambio1 && event.getNodeA().getName().equals("MisilJ") && event.getNodeB().getName().equals("MisilE")){
                cambio1=false;                
                misilJD.posOrigen();                
                //System.out.println("Colision--: "+event.getNodeA().getName()+" contra "+event.getNodeB().getName());       
            }
            
        }
        //si no cocha con el suelo
//        System.out.println("Colision: "+event.getNodeA().getName()+" contra "+event.getNodeB().getName());       
    }
    
    
}
