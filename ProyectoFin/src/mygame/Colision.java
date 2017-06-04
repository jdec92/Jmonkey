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
    Coche crash,mario;
    Arma misilCrash,misilMario,cajaCrash,cajaMario;
    Seta seta1,seta2;
    
    public Colision(Coche c,Coche m,Arma mc,Arma mm,Arma cc,Arma cm,Seta s1,Seta s2){
        crash=c;
        mario=m;
        misilCrash=mc;
        misilMario=mm;
        cajaCrash=cc;
        cajaMario=cm;
        seta1=s1;
        seta2=s2;
        cambio=true;
    }        
    @Override
    public void collision(PhysicsCollisionEvent event) {
    //si colision es diferente al suelo
        if(!event.getNodeB().getName().equals("Suelo")){
            
    //Colisiones del Jugador++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++                
            if(cambio && event.getNodeA().getName().equals("Crash") && event.getNodeB().getName().equals("MisilMario")){
                cambio=false;                
                misilMario.posOrigen();
                crash.penalizacion();
                System.out.println(event.getNodeB().getName()+" choco contra "+event.getNodeA().getName());
            }
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++            
    
    //Colisiones del Enemigo-------------------------------------------------------------------------------------------------------------                        
            else if(cambio && event.getNodeA().getName().equals("Mario") && event.getNodeB().getName().equals("MisilCrash")){
                cambio=false;                
                misilCrash.posOrigen();
                mario.penalizacion();
                System.out.println(event.getNodeB().getName()+" choco contra "+event.getNodeA().getName());
            }
    //----------------------------------------------------------------------------------------------------------------------------------                
            
    //SETAS_________________________________________________________________________________
        //si alguien colisiona con una seta1 o seta2 tiene que activa el arma y mover la seta
            else if(cambio && event.getNodeB().getName().substring(0,4).equals("Seta")){
                cambio=false;
                Random r=new Random();          
                int tipoA=r.nextInt(3);                                
                if(event.getNodeB().getName().equals("Seta1")){                    
                    seta1.cambiarPos(seta2.id);
    //Seta1 cogida por CRASH
                    if(event.getNodeA().getName().equals("Crash")){
                        switch(tipoA){
                            case 0:
                                if(!cajaCrash.usar){
                                    misilCrash.usar=true;
                                    crash.tipoA=Coche.tipoArma.Misil;
                                }
                                break;
                            case 1:                           
                                if(!misilCrash.usar){
                                    cajaCrash.usar=true;
                                    crash.tipoA=Coche.tipoArma.TNT;
                                }                                
                                break;
                            case 2:                           
                                if(!misilCrash.usar && !cajaCrash.usar){                                                                        
                                    crash.inicioTurbo();                                                                                                            
                                    crash.tipoA=Coche.tipoArma.Turbo;
                                }                                
                                break;
                            }
                    }
    //Seta1 cogida por MARIO
                    if(event.getNodeA().getName().equals("Mario")){
                        switch(tipoA){
                            case 0:
                                if(!cajaMario.usar){
                                    misilMario.usar=true;
                                    mario.tipoA=Coche.tipoArma.Misil;
                                }
                                break;
                            case 1:                           
                                if(!misilMario.usar){
                                    cajaMario.usar=true;
                                    mario.tipoA=Coche.tipoArma.TNT;
                                }                                
                                break;
                            case 2:                           
                                if(!misilMario.usar && !cajaMario.usar){                                                                        
                                    mario.inicioTurbo();
                                    mario.tipoA=Coche.tipoArma.Turbo;
                                }                                
                                break;
                        }
                    }                   
                }else if(event.getNodeB().getName().equals("Seta2")){
                    seta2.cambiarPos(seta1.id);
    //Seta2 cogida por CRASH
                    if(event.getNodeA().getName().equals("Crash")){
                        switch(tipoA){
                            case 0:
                                if(!cajaCrash.usar){
                                    misilCrash.usar=true;
                                    crash.tipoA=Coche.tipoArma.Misil;
                                }
                                break;
                            case 1:                           
                                if(!misilCrash.usar){
                                    cajaCrash.usar=true;
                                    crash.tipoA=Coche.tipoArma.TNT;
                                }                                
                                break;
                            case 2:                           
                                 if(!misilCrash.usar && !cajaCrash.usar){                                                                        
                                    crash.inicioTurbo();                                                     
                                    crash.tipoA=Coche.tipoArma.Turbo;
                                }                                
                                break;
                        }
                    }
    //Seta2 cogida por MARIO
                    if(event.getNodeA().getName().equals("Mario")){
                        switch(tipoA){
                            case 0:
                                if(!cajaMario.usar){
                                    misilMario.usar=true;
                                    mario.tipoA=Coche.tipoArma.Misil;
                                }
                                break;
                            case 1:                           
                                if(!misilMario.usar){
                                    cajaMario.usar=true;
                                    mario.tipoA=Coche.tipoArma.TNT;
                                }                                
                                break;
                            case 2:                           
                                if(!misilMario.usar && !cajaMario.usar){                                                                        
                                    mario.inicioTurbo();                                                                        
                                    mario.tipoA=Coche.tipoArma.Turbo;
                                }                                
                                break;
                        }
                    }
                }            
            }
            
    //CAJAS_____________________________________________________________
            
    //si algo choca contra la caja del CRASH, si es coche paraliza y si es misil vuelve a posicion oculta            
            else if(cambio && event.getNodeB().getName().equals("CajaCrash")){
                cambio=false;                
                if(event.getNodeA().getName().equals("MisilMario")){                    
                    misilMario.posOrigen();
                    cajaCrash.posOrigen();                                                            
                    System.out.println(event.getNodeB().getName()+" choco contra "+event.getNodeA().getName());                    
                    
                }else if(event.getNodeA().getName().equals("Crash")){
                    cajaCrash.posOrigen();
                    crash.penalizacion();
                    System.out.println(event.getNodeB().getName()+" choco contra "+event.getNodeA().getName());
                    
                }else if(event.getNodeA().getName().equals("Mario")){
                    cajaCrash.posOrigen();
                    mario.penalizacion();
                    System.out.println(event.getNodeB().getName()+" choco contra "+event.getNodeA().getName());                    
                }
            }        
    //si algo choca contra la caja del MARIO, si es coche paraliza y si es misil vuelve a posicion oculta            
            else if(cambio && event.getNodeB().getName().equals("CajaMario")){
                cambio=false;                
                if(event.getNodeA().getName().equals("MisilCrash")){                    
                    misilCrash.posOrigen();
                    cajaMario.posOrigen();
                    System.out.println(event.getNodeB().getName()+" choco contra "+event.getNodeA().getName());
                    
                }else if(event.getNodeA().getName().equals("Crash")){
                    cajaMario.posOrigen();
                    crash.penalizacion();
                    System.out.println(event.getNodeB().getName()+" choco contra "+event.getNodeA().getName());
                    
                }else if(event.getNodeA().getName().equals("Mario")){
                    cajaMario.posOrigen();
                    mario.penalizacion();
                    System.out.println(event.getNodeB().getName()+" choco contra "+event.getNodeA().getName());
                    
                }
            }        
        //si no cocha con el suelo
        //System.out.println("Nodo A: "+event.getNodeA().getName()+" contra "+event.getNodeB().getName());       
        }
    }
    
    
}
