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
    Coche jugador,enemigo;
    Ruta obj;
    Arma misilJD,misilED,cajaJ,cajaE;
    Seta seta1,seta2;
    int tipoA;
    
    public Colision(Coche cochej,Coche cochee,Ruta r,Arma mjd,Arma med,Arma cj,Arma ce,Seta s1,Seta s2){
        jugador=cochej;
        enemigo=cochee;
        obj=r;
        misilJD=mjd;
        misilED=med;        
        cajaJ=cj;
        cajaE=ce;
        seta1=s1;
        seta2=s2;
        cambio=true;
    }        
    @Override
    public void collision(PhysicsCollisionEvent event) {
        //si colision es diferente al suelo
        if(!event.getNodeB().getName().equals("Suelo")){
            
            //si el enemigo cocha con la esfera, cambia la posicion de la esfera
            if(cambio && event.getNodeB().getName().equals("Bola") && event.getNodeA().getName().equals("Enemigo")){
                cambio=false;
                obj.cambiarPos();                
            }
            
            //si el enemigo le impacta algún misil, el enemigo se queda parado y el misil va a la posicion oculta
            else if(cambio && event.getNodeA().getName().equals("Enemigo") && event.getNodeB().getName().equals("MisilJ")){
                cambio=false;                
                misilJD.posOrigen();
                enemigo.tiempoParado.reset();                
            }
            
            //si al jugador le impacta algún misil, el jugador se queda parado y el misil va a la posicion oculta
            else if(cambio && event.getNodeA().getName().equals("Jugador") && event.getNodeB().getName().equals("MisilE")){
                cambio=false;                
                misilED.posOrigen();                    
            }
            
            //si alguien colisiona con una seta1 o seta2 tiene que activa el arma y mover la seta
            else if(cambio && event.getNodeB().getName().substring(0,4).equals("Seta")){
                cambio=false;
                Random r=new Random();
                tipoA=r.nextInt(2);
                System.out.println("Tipo de Arma:"+tipoA);
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
            
            //si algo cocha contra la caja del enemigo, si es coche paraliza y si es misil vuelve a posicion oculta            
            else if(cambio && event.getNodeB().getName().equals("CajaE")){
                cambio=false;                
                if(event.getNodeA().getName().equals("MisilJ")){
                    System.out.println("Choco el misil con la caja");
                    misilJD.posOrigen();
                    cajaE.posOrigen();
                }else if(event.getNodeA().getName().equals("Jugador")){
                    System.out.println("Choco contra el Jugador");
                }else if(event.getNodeA().getName().equals("Enemigo")){
                    System.out.println("Choco contra el Enemigo");
                }
            }
            
        }
        //si no cocha con el suelo
//        System.out.println("Colision: "+event.getNodeA().getName()+" contra "+event.getNodeB().getName());       
    }
    
    
}
