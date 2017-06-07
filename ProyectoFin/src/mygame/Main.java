package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix3f;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import java.awt.BorderLayout;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    private BulletAppState estadosFisicos;
    private Geometry meta;   
    private RigidBodyControl metaFisica;
    private Coche mario,crash;
    private Ruta nav,nav2,navMC,navMM;
    private Arma misilCrash,misilMario,cajaMario,cajaCrash;
    private Seta seta1,seta2;
    private Spatial suelo;    
    private BitmapText texto,texto2;  
    private Audio audio;
    private RigidBodyControl sueloFisico;    
    private Colision colision;
    private ControladorTeclado cntT;    
    float tiempo=0; 
    boolean cargando=true;
    boolean semaforo=false;
    boolean semaforo2=true;
    int cntC=-1;
    int cntM=-1;
    int ganador=0;
    int vueltas=2;
    
     
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        estadosFisicos = new BulletAppState();
        stateManager.attach(estadosFisicos);

        setDisplayFps(false);
        setDisplayStatView(false);                
        flyCam.setEnabled(false);                        
    
    //luz direccional
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(-0.5f, -0.5f, -0.5f)).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);

    //carga la escena del mapa           
        Spatial mundo = assetManager.loadModel("Scenes/mapCity.j3o");        
        rootNode.attachChild(mundo);

    //carga esqueleto mapa y da propiedades
        suelo = assetManager.loadModel("Scenes/colision.j3o");
        suelo.setName("Suelo");
        sueloFisico = new RigidBodyControl(0.0f);                
        integrarObjeto(suelo, sueloFisico, estadosFisicos, new Vector3f(0f, -4.1f,0f), 0);
        sueloFisico.setRestitution(0.9f);
        sueloFisico.setFriction(0.5f);
        
    //crear meta
        Box bal=new Box(0.4f,0.4f,0.4f);
        meta = new Geometry("Meta", bal);
        metaFisica = new RigidBodyControl(0f);
        meta.setCullHint(Spatial.CullHint.Always);
        integrarObjeto(meta, metaFisica, estadosFisicos, null, "");        
        
        
    //crear Audios
        AudioNode audio_seta = new AudioNode(assetManager,"Sounds/1up.wav", AudioData.DataType.Buffer);
        AudioNode audio_fin = new AudioNode(assetManager,"Sounds/completado.wav", AudioData.DataType.Buffer);
        AudioNode audio_base = new AudioNode(assetManager, "Sounds/base.wav", AudioData.DataType.Stream);
        AudioNode audio_cturbo = new AudioNode(assetManager,"Sounds/crash_turbo.wav", AudioData.DataType.Buffer);
        AudioNode audio_mturbo = new AudioNode(assetManager,"Sounds/mario_turbo.wav", AudioData.DataType.Buffer);
        AudioNode audio_boom = new AudioNode(assetManager, "Sounds/bomba.wav", AudioData.DataType.Buffer);
        audio=new Audio(audio_seta, audio_fin, audio_base,audio_cturbo,audio_mturbo,audio_boom);
        rootNode.attachChild(audio_seta);
        rootNode.attachChild(audio_fin);
        rootNode.attachChild(audio_base);        
        
    //crear ruta para coches
        nav=new Ruta(0);
        integrarObjeto(nav.objetivoGeom, nav.objFisico, estadosFisicos, nav.posicionActual(), "");
        nav.aplicarFisica();
              
        nav2=new Ruta(1);
        integrarObjeto(nav2.objetivoGeom, nav2.objFisico, estadosFisicos, nav2.posicionActual(), "");
        nav2.aplicarFisica();
        
        navMC=new Ruta(2);
        integrarObjeto(navMC.objetivoGeom, navMC.objFisico, estadosFisicos, navMC.posicionActual(), "");
        navMC.aplicarFisica();
        
        navMM=new Ruta(3);
        integrarObjeto(navMM.objetivoGeom, navMM.objFisico, estadosFisicos, navMM.posicionActual(), "");
        navMM.aplicarFisica();
        
    //crear Jugador
        crash=new Coche(assetManager.loadModel("Models/Crash_Kart/Crash_Kart.j3o"),"Crash",nav);
        integrarObjeto(crash.coche, crash.cocheFisico, estadosFisicos, crash.posIniC[0],0);
        integrarObjeto(crash.geomBola,crash.bolaFisica, estadosFisicos,crash.posIniC[2],"");
        crash.aplicarFisica();        
        crash.cam=true;
        
    //crear enemigo                
        mario=new Coche(assetManager.loadModel("Models/Mario/Kart_Mario.j3o"), "Mario", nav2);
        integrarObjeto(mario.coche, mario.cocheFisico, estadosFisicos, mario.posIniC[1],0);
        integrarObjeto(mario.geomBola,mario.bolaFisica, estadosFisicos,mario.posIniC[3],"");
        mario.aplicarFisica();                            
                
    //crear armas Jugador                
            //misil dirigido
        misilCrash = new Arma(assetManager.loadModel("Models/misil2/AGM-114HellFire.j3o"),"MisilCrash",0,mario,navMC);
        integrarObjeto(misilCrash.bala,misilCrash.balaFisica,estadosFisicos,misilCrash.posIniC[0], 0);
        misilCrash.aplicarFisicaM();        
            //caja Jugador
        cajaCrash = new Arma("CajaCrash",0);
        integrarObjeto(cajaCrash.balaG, cajaCrash.balaFisica, estadosFisicos, cajaCrash.posIniC[0],"tnt");        
        cajaCrash.aplicarFisicaC();
        
    //crear Armas Enemigo                
            //misil dirigido
        misilMario = new Arma(assetManager.loadModel("Models/misil2/AGM-114HellFire.j3o"),"MisilMario",1,crash,navMM);
        integrarObjeto(misilMario.bala,misilMario.balaFisica,estadosFisicos,misilMario.posIniC[1], 0);
        misilMario.aplicarFisicaM();        
            //caja Enemigo
        cajaMario = new Arma("CajaMario",1);
        integrarObjeto(cajaMario.balaG, cajaMario.balaFisica, estadosFisicos, cajaMario.posIniC[1],"tnt");        
        cajaMario.aplicarFisicaC();
                        
    //crear setas        
        seta1 =new Seta(assetManager.loadModel("Models/Seta/untitled.j3o"),"Seta1");                                                
        integrarObjeto(seta1.seta, seta1.setaFisico, estadosFisicos, seta1.posicionActual(),0);
        seta1.propiedades();

        seta2 =new Seta(assetManager.loadModel("Models/Seta/untitled.j3o"),"Seta2");              
        integrarObjeto(seta2.seta, seta2.setaFisico, estadosFisicos, seta2.posicionActual(), 0);            
        seta2.cambiarPos(0);
        seta2.propiedades();
                
    //crear colision    
        colision=new Colision(crash,mario,misilCrash,misilMario,cajaCrash,cajaMario,seta1,seta2,audio);
        estadosFisicos.getPhysicsSpace().addCollisionListener(colision);                           
        
    //cargar Teclado
        cntT= new ControladorTeclado(crash,mario,audio);
        inicTeclado();
   }

    @Override
    public void simpleUpdate(float tpf) {
        tiempo=tiempo+tpf;

       
    //posiciones de los coches
        Vector3f posMario= mario.cocheFisico.getPhysicsLocation();        
        Vector3f posCrash= crash.cocheFisico.getPhysicsLocation();        
        
   //posicion de la camara______________________________________________________       
        Vector3f camP=posCrash;        
        Vector3f parteTrasera = posicionCamara(camP, nav.id);
        if(mario.cam){
            camP=posMario;            
            parteTrasera = posicionCamara(camP, nav2.id);
        }                                                
        cam.setLocation( parteTrasera );
        cam.lookAt( camP, Vector3f.UNIT_Y);
    
       
//Rayos coche Crash+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++    
    //Detecta si pasa delante del enemigo el jugador y dispara misil
        CollisionResults detecta_Mario = new CollisionResults();
        mario.coche.collideWith(crash.rayoFrente(), detecta_Mario);        
        misilCrash.dectector(detecta_Mario,posCrash);
        
    //Detecta distancia con pared del coche enemigo
        CollisionResults rayoI_Crash = new CollisionResults();
        suelo.collideWith(crash.rayosIzq(),rayoI_Crash);
        float distancia_CrashI=0;        
        if (rayoI_Crash.getClosestCollision() != null){
            distancia_CrashI=rayoI_Crash.getClosestCollision().getDistance();
        }
        CollisionResults rayoD_Crash = new CollisionResults();
        suelo.collideWith(crash.rayosDer(),rayoD_Crash);
        float distancia_CrashD=0;
        if(rayoD_Crash.getClosestCollision()!=null){
            distancia_CrashD=rayoD_Crash.getClosestCollision().getDistance();        
        }                
        
    //Detecta enemigo un obstaculo
   
        CollisionResults detecta_Crash_M = new CollisionResults();        
        mario.coche.collideWith(crash.rayoObstaculo(),detecta_Crash_M);
        CollisionResults detecta_Crash_CC = new CollisionResults();
        cajaCrash.balaG.collideWith(crash.rayoObstaculo(), detecta_Crash_CC);
        CollisionResults detecta_Crash_CM = new CollisionResults();
        cajaMario.balaG.collideWith(crash.rayoObstaculo(), detecta_Crash_CM);
        
        crash.esquivo(detecta_Crash_M,detecta_Crash_CC,detecta_Crash_CM,posMario,cajaCrash.balaG.getLocalTranslation(),
                                                cajaMario.balaG.getLocalTranslation(),distancia_CrashI,distancia_CrashD);        
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++        
        
//Rayos coche Mario-------------------------------------------------------------------------------------------------------------
    //Detecta si pasa delante del enemigo el jugador y dispara
        CollisionResults detecta_Crash = new CollisionResults();
        crash.coche.collideWith(mario.rayoFrente(), detecta_Crash);        
        misilMario.dectector(detecta_Crash,posMario);
                
    //Detecta distancia con pared del coche enemigo
        CollisionResults rayoI_mario= new CollisionResults();
        suelo.collideWith(mario.rayosIzq(),rayoI_mario);
        float distancia_marioI=0;        
        if (rayoI_mario.getClosestCollision() != null){
            distancia_marioI=rayoI_mario.getClosestCollision().getDistance();
        }        
        CollisionResults rayoD_mario = new CollisionResults();
        suelo.collideWith(mario.rayosDer(),rayoD_mario);
        float distancia_marioD=0;
        if(rayoD_mario.getClosestCollision()!=null){
            distancia_marioD=rayoD_mario.getClosestCollision().getDistance();        
        }                
        
    //Detecta enemigo un obstaculo
        CollisionResults detecta_Mario_C = new CollisionResults();        
        crash.coche.collideWith(mario.rayoObstaculo(),detecta_Mario_C);
        CollisionResults detecta_Mario_CC = new CollisionResults();
        cajaCrash.balaG.collideWith(mario.rayoObstaculo(), detecta_Mario_CC);
        CollisionResults detecta_Mario_CM = new CollisionResults();
        cajaMario.balaG.collideWith(mario.rayoObstaculo(), detecta_Mario_CM);
        
        mario.esquivo(detecta_Mario_C,detecta_Mario_CC,detecta_Mario_CM,posCrash,cajaCrash.balaG.getLocalTranslation(),
                                                cajaMario.balaG.getLocalTranslation(),distancia_marioI,distancia_marioD);        
        
        
//--------------------------------------------------------------------------------------------------------------------------------------

    //Lanzar caja para defensa Crash
        Vector3f pos_MMario= misilMario.balaFisica.getPhysicsLocation();
        Vector3f dir_MMario= misilMario.balaFisica.getPhysicsRotation().getRotationColumn(2).normalize();
        Vector3f pos_CCrash = new Vector3f( pos_MMario.x+2.5f*dir_MMario.x, pos_MMario.y, pos_MMario.z+2.5f*dir_MMario.z );
        float misilMario_crash = crash.cocheFisico.getPhysicsLocation().distance(misilMario.bala.getLocalTranslation());                        
        float seta1_Crash = crash.cocheFisico.getPhysicsLocation().distance(seta1.setaFisico.getPhysicsLocation());
        float seta2_Crash = crash.cocheFisico.getPhysicsLocation().distance(seta2.setaFisico.getPhysicsLocation());        
        Vector3f pos_CCrash2 = cajaCrash.orientarCaja(posCrash, nav.id);
        cajaCrash.defensa(seta1_Crash,seta2_Crash,misilMario_crash,pos_CCrash,pos_CCrash2);        
        

    //Lanzar caja para defensa Mario
        Vector3f pos_MCrash= misilCrash.balaFisica.getPhysicsLocation();
        Vector3f dir_MCrash = misilCrash.balaFisica.getPhysicsRotation().getRotationColumn(2).normalize();
        Vector3f pos_CMario = new Vector3f( pos_MCrash.x+2.5f*dir_MCrash.x, pos_MCrash.y, pos_MCrash.z+2.5f*dir_MCrash.z );                
        float misilCrash_mario = mario.cocheFisico.getPhysicsLocation().distance(misilCrash.bala.getLocalTranslation());                         
        float seta1_Mario = mario.cocheFisico.getPhysicsLocation().distance(seta1.setaFisico.getPhysicsLocation());
        float seta2_Mario = mario.cocheFisico.getPhysicsLocation().distance(seta2.setaFisico.getPhysicsLocation());                                       
        Vector3f pos_CMario2= cajaMario.orientarCaja(posMario,nav2.id);
        cajaMario.defensa(seta1_Mario,seta2_Mario,misilCrash_mario,pos_CMario, pos_CMario2);                
       
    //Lanzar caja al tener a distancia las seta
        float distancia_NavMC = misilCrash.balaFisica.getPhysicsLocation().distance(navMC.objFisico.getPhysicsLocation());
        float distancia_MC_Mario=misilCrash.balaFisica.getPhysicsLocation().distance(mario.coche.getLocalTranslation());
        navMC.cambiarPos(distancia_NavMC);
        misilCrash.lanzar(seta1_Crash,seta2_Crash,distancia_MC_Mario,nav.id,posCrash);
        
        float distancia_NavMM = misilMario.balaFisica.getPhysicsLocation().distance(navMM.objFisico.getPhysicsLocation());
        float distancia_MM_Crash=misilMario.balaFisica.getPhysicsLocation().distance(crash.coche.getLocalTranslation());
        navMM.cambiarPos(distancia_NavMM);
        misilMario.lanzar(seta1_Mario,seta2_Mario,distancia_MM_Crash,nav2.id,posMario);
        
        
        if(!cargando && ganador==0){
            audio.inicio();
    //Navegacion Jugador    
            float distancia_NavCrash = crash.cocheFisico.getPhysicsLocation().distance(nav.objFisico.getPhysicsLocation());
            nav.cambiarPos(distancia_NavCrash);
            crash.avanzar(seta1_Crash, seta2_Crash,seta1.seta.getLocalTranslation(),seta2.seta.getLocalTranslation());        

    //Navegacion Enemigo
            float distancia_NavMario= mario.cocheFisico.getPhysicsLocation().distance(nav2.objFisico.getPhysicsLocation());
            nav2.cambiarPos(distancia_NavMario);
            mario.avanzar(seta1_Mario,seta2_Mario,seta1.seta.getLocalTranslation(),seta2.seta.getLocalTranslation());                   
        }
                
    //controlador meta
        float dist_metaC=crash.cocheFisico.getPhysicsLocation().distance(meta.getLocalTranslation());
        float dist_metaM=mario.cocheFisico.getPhysicsLocation().distance(meta.getLocalTranslation());
        if(vueltas==crash.vueltas || vueltas==mario.vueltas){
            audio.fin();
            if(vueltas==crash.vueltas){
                ganador=1;
            }else{
                ganador=2;
            }
        }else{
            if(dist_metaC<10f){
                if(cntC==crash.vueltas){
                    crash.vueltas++;                      
                }
            }else{  
                cntC=crash.vueltas;
            }
            if(dist_metaM<10f){
                if(cntM==mario.vueltas){
                    mario.vueltas++;                      
                }
            }else{
                cntM=mario.vueltas;
            }
        }                                    
        
    //actualizador semaforo para Colisiones.
        if(!colision.cambio){            
            colision.cambio=true;
        }        
                
        
    //texto en pantalla
        guiNode.detachAllChildren();
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        texto = new BitmapText(guiFont, false);
        texto2 = new BitmapText(guiFont, false);
        texto.setSize(guiFont.getCharSet().getRenderedSize());
        texto2.setSize(guiFont.getCharSet().getRenderedSize());                
        if(tiempo<10){            
            int t=(int) tiempo;
            t=10-t;
            texto.setText(""+t);
            texto.setLocalTranslation(300,300,0);
            texto.setSize(100);            
        }else{        
            cargando=false;
            if(crash.cam){            
                if(misilCrash.usar || cajaCrash.usar || crash.tipoA==Coche.tipoArma.Turbo){
                    texto.setText("Tipo de Arma:"+crash.tipoA);
                }else{
                    texto.setText("Sin Armas");
                }
                if(crash.vueltas<=0){
                    texto2.setText("Vuelta 0/"+vueltas);
                }else if(ganador!=0){
                    if(ganador==1){
                        texto2.setText("Ganador");
                    }else{
                        texto2.setText("Perdedor");
                    }
                }else{
                    texto2.setText("Vuelta "+crash.vueltas+"/"+vueltas);
                }                
            }else{
                if(misilMario.usar || cajaMario.usar || mario.tipoA==Coche.tipoArma.Turbo){
                    texto.setText("Tipo de Arma:"+mario.tipoA);            
                }else{
                    texto.setText("Sin Armas");
                }            
                if(mario.vueltas<=0){
                    texto2.setText("Vuelta 0/"+vueltas);
                }else if(ganador!=0){
                    if(ganador==2){
                        texto2.setText("Ganador");
                    }else{
                        texto2.setText("Perdedor");
                    }
                }else{
                    texto2.setText("Vuelta "+mario.vueltas+"/"+vueltas);
                }
            }    
            texto2.setLocalTranslation(10,480,0);
            texto2.setSize(25);
            texto.setLocalTranslation(400, 480, 0);
            texto.setSize(25);
        }
        
        texto2.setColor(ColorRGBA.Red);        
        texto2.setName("Vueltas");
        guiNode.attachChild(texto2);
        texto.setColor(ColorRGBA.Red);        
        texto.setName("Texto");
        guiNode.attachChild(texto);
        
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
    private void inicTeclado() {
        inputManager.addMapping("CamC", new KeyTrigger(KeyInput.KEY_Z));
        inputManager.addMapping("CamM", new KeyTrigger(KeyInput.KEY_X));        
        inputManager.addMapping("MuteOFF", new KeyTrigger(KeyInput.KEY_N));
        inputManager.addMapping("MuteON", new KeyTrigger(KeyInput.KEY_M));                
        inputManager.addListener(cntT.analogListener, "CamC","CamM","MuteOFF","MuteON");
    }
    
    public void integrarObjeto(Spatial objetoVisual, RigidBodyControl objetoFisico, BulletAppState estadosFisicos, Vector3f posicion, int giro) {
        rootNode.attachChild(objetoVisual);                                               //integración en el mundo visual 
        objetoVisual.addControl(objetoFisico);                                          //Asociación  objeto visual-fisico
        estadosFisicos.getPhysicsSpace().add(objetoFisico);                  //integración en el mundo físico        
        objetoFisico.setPhysicsLocation(posicion);
    }

    public  void  integrarObjeto (Geometry objetoVisual, RigidBodyControl objetoFisico, BulletAppState estadosFisicos, Vector3f posicion, String textura){ 
        Material material;
        if (textura.equals("tnt")){
            material = assetManager.loadMaterial("Materials/Generated/caja.j3m");            
        }else{  
            material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");  
            material.setBoolean("UseMaterialColors", true);
            material.setColor("Diffuse", new ColorRGBA(0.7f, (float) Math.random(), 0.5f, 1f));  
        }        
        objetoVisual.setMaterial(material);    
        rootNode.attachChild(objetoVisual);                                               //integración en el mundo visual 
        objetoVisual.addControl(objetoFisico );                                          //Asociación  objeto visual-fisico
        estadosFisicos.getPhysicsSpace().add( objetoFisico );                  //integración en el mundo físico        
        if(posicion==null){
            posicion=new Vector3f(-40,-4,14);
            Matrix3f mat = new Matrix3f();
            mat.fromAngleAxis((float) -(Math.PI/2),Vector3f.UNIT_Y);
            objetoFisico.setPhysicsRotation(mat);
        }
       objetoFisico.setPhysicsLocation(posicion);
    }
    
    public Vector3f posicionCamara(Vector3f pos,int id){    
        Vector3f v=pos;
        float dist=15f;
        float altura=10f;
        switch(id){
            case 0:
                v=new Vector3f(v.x,v.y+altura,v.z-dist);
                break;
            case 1:
                v=new Vector3f(v.x-dist,v.y+altura,v.z);
                break;
            case 2:
                v=new Vector3f(v.x,v.y+altura,v.z+dist);
                break;            
            case 4:
                v=new Vector3f(v.x,v.y+altura,v.z+dist);
                break;
            default:
                v=new Vector3f(v.x+dist,v.y+altura,v.z);
                break;                
        }
        return v;
    }
    
}
