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
    Arma misilJD,misilED,cajaJ,cajaE;
    Seta seta1,seta2;
    
    public Colision(Coche cochej,Coche cochee,Arma mjd,Arma med,Arma cj,Arma ce,Seta s1,Seta s2){
        jugador=cochej;
        enemigo=cochee;        
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
        
    //Colisiones del Enemigo-------------------------------------------------------------------------------------------------------------
        //si el enemigo cocha con la esfera, cambia la posicion de la esfera
            if(cambio && event.getNodeB().getName().equals("Bola") && event.getNodeA().getName().equals("Enemigo")){
                cambio=false;
    //            obj.cambiarPos();                
            }
            
        //si el enemigo le impacta algún misil, el enemigo se queda parado y el misil va a la posicion oculta
            else if(cambio && event.getNodeA().getName().equals("Enemigo") && event.getNodeB().getName().equals("MisilJ")){
                cambio=false;                
                misilJD.posOrigen();
                enemigo.penalizacion();
                System.out.println("MisilJ choco contra Enemigo");
            }
    //----------------------------------------------------------------------------------------------------------------------------------
    //Colisiones del Jugador++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++        
        //si el jugador le impacta algún misil, el enemigo se queda parado y el misil va a la posicion oculta
            else if(cambio && event.getNodeA().getName().equals("Jugador") && event.getNodeB().getName().equals("MisilE")){
                cambio=false;                
                misilED.posOrigen();
                jugador.penalizacion();
                System.out.println("MisilE choco contra Jugador");
            }
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //SETAS_________________________________________________________________________________
        //si alguien colisiona con una seta1 o seta2 tiene que activa el arma y mover la seta
            else if(cambio && event.getNodeB().getName().substring(0,4).equals("Seta")){
                cambio=false;
                Random r=new Random();          
                int tipoA=r.nextInt(3);                                
                if(event.getNodeB().getName().equals("Seta1")){                    
                    seta1.cambiarPos(seta2.id);
                    if(event.getNodeA().getName().equals("Enemigo")){
                        switch(tipoA){
                            case 0:
                                if(!cajaE.usar){
                                    misilED.usar=true;
                                    enemigo.tipoA=Coche.tipoArma.Misil;
                                }
                                break;
                            case 1:                           
                                if(!misilED.usar){
                                    cajaE.usar=true;
                                    enemigo.tipoA=Coche.tipoArma.TNT;
                                }                                
                                break;
                            case 2:                           
                                if(!misilED.usar && !cajaE.usar){                                    
                                    enemigo.tipoA=Coche.tipoArma.Turbo;
                                    enemigo.inicioTurbo();
                                }                                
                                break;
                        }
                    }
                    if(event.getNodeA().getName().equals("Jugador")){
                        switch(tipoA){
                            case 0:
                                if(!cajaJ.usar){
                                    misilJD.usar=true;
                                    jugador.tipoA=Coche.tipoArma.Misil;
                                }
                                break;
                            case 1:                           
                                if(!misilJD.usar){
                                    cajaJ.usar=true;
                                    jugador.tipoA=Coche.tipoArma.TNT;
                                }                                
                                break;
                            case 2:                           
                                if(!misilJD.usar && !cajaJ.usar){                                    
                                    jugador.tipoA=Coche.tipoArma.Turbo;
                                    jugador.inicioTurbo();                                                                                                            
                                }                                
                                break;
                        }
                    }
                }else if(event.getNodeB().getName().equals("Seta2")){
                    seta2.cambiarPos(seta1.id);
                    if(event.getNodeA().getName().equals("Enemigo")){
                        switch(tipoA){
                            case 0:
                                if(!cajaE.usar){
                                    misilED.usar=true;
                                    enemigo.tipoA=Coche.tipoArma.Misil;
                                }
                                break;
                            case 1:                           
                                if(!misilED.usar){
                                    cajaE.usar=true;
                                    enemigo.tipoA=Coche.tipoArma.TNT;
                                }                                
                                break;
                            case 2:                           
                                if(!misilED.usar && !cajaE.usar){                                    
                                    enemigo.tipoA=Coche.tipoArma.Turbo;
                                    enemigo.inicioTurbo();                                                                        
                                }                                
                                break;
                        }
                    }
                    if(event.getNodeA().getName().equals("Jugador")){
                        switch(tipoA){
                            case 0:
                                if(!cajaJ.usar){
                                    misilJD.usar=true;
                                    jugador.tipoA=Coche.tipoArma.Misil;
                                }
                                break;
                            case 1:                           
                                if(!misilJD.usar){
                                    cajaJ.usar=true;
                                    jugador.tipoA=Coche.tipoArma.TNT;
                                }                                
                                break;
                            case 2:                           
                                 if(!misilJD.usar && !cajaJ.usar){                                    
                                    jugador.tipoA=Coche.tipoArma.Turbo;
                                    jugador.inicioTurbo();                                                                        
                                }                                
                                break;
                        }
                    }
                }
                //System.out.println("Arma seleccionada: "+tipoA);
            }
    //CAJAS_____________________________________________________________
        //si algo cocha contra la caja del enemigo, si es coche paraliza y si es misil vuelve a posicion oculta            
            else if(cambio && event.getNodeB().getName().equals("CajaE")){
                cambio=false;                
                if(event.getNodeA().getName().equals("MisilJ")){
                    System.out.println("Choco el misil con la caja");
                    misilJD.posOrigen();
                    cajaE.posOrigen();
                }else if(event.getNodeA().getName().equals("Jugador")){
                    cajaE.posOrigen();
                    jugador.penalizacion();
                    System.out.println("Choco contra el Jugador");
                }else if(event.getNodeA().getName().equals("Enemigo")){
                    cajaE.posOrigen();
                    enemigo.penalizacion();
                    System.out.println("Choco contra el Enemigo");
                }
            }
        //si algo cocha contra la caja del jugador, si es coche paraliza y si es misil vuelve a posicion oculta            
            else if(cambio && event.getNodeB().getName().equals("CajaJ")){
                cambio=false;                
                if(event.getNodeA().getName().equals("MisilE")){
                    System.out.println("Choco el misil con la caja");
                    misilJD.posOrigen();
                    cajaE.posOrigen();
                }else if(event.getNodeA().getName().equals("Jugador")){
                    cajaJ.posOrigen();
                    jugador.penalizacion();
                    System.out.println("Choco contra el Jugador");
                }else if(event.getNodeA().getName().equals("Enemigo")){
                    cajaJ.posOrigen();
                    enemigo.penalizacion();
                    System.out.println("Choco contra el Enemigo");
                }
            }
        }
        //si no cocha con el suelo
//        System.out.println("Colision: "+event.getNodeA().getName()+" contra "+event.getNodeB().getName());       
    }
    
    
}
