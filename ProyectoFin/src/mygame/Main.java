package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    private BulletAppState estadosFisicos;
    private Coche mario,crash;
    private Ruta nav,nav2;
    private Arma misilCrash,misilMario,cajaMario,cajaCrash;
    private Seta seta1,seta2;
    private Spatial suelo;    
    private BitmapText texto;  
    
    private RigidBodyControl sueloFisico;    
    private Colision colision;
    private ControladorTeclado cntT;    
    float tiempo=0; 
    boolean cargando=true;
    
    
     
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
        
        this.flyCam.setMoveSpeed(this.flyCam.getMoveSpeed()*10f);
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        cam.setLocation(new Vector3f(0, 30f, 0f));
        cam.lookAt(new Vector3f(0, 0, 0), Vector3f.UNIT_Y);
    
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
                                                
    //crear ruta para coches
        nav=new Ruta(0);
        integrarObjeto(nav.objetivoGeom, nav.objFisico, estadosFisicos, nav.posicionActual(), "");
        nav.aplicarFisica();
              
        nav2=new Ruta(1);
        integrarObjeto(nav2.objetivoGeom, nav2.objFisico, estadosFisicos, nav2.posicionActual(), "");
        nav2.aplicarFisica();
        
    //crear Jugador
        crash=new Coche(assetManager.loadModel("Models/Crash_Kart/Crash_Kart.j3o"),"Crash",nav);
        integrarObjeto(crash.coche, crash.cocheFisico, estadosFisicos, crash.posIniC,0);
        crash.aplicarFisica();        
        crash.cam=true;
        
    //crear enemigo                
        mario=new Coche(assetManager.loadModel("Models/Mario/Kart_Mario.j3o"), "Mario", nav2);
        integrarObjeto(mario.coche, mario.cocheFisico, estadosFisicos, mario.posIniC,1);
        mario.aplicarFisica();                            
                
    //crear armas Jugador                
            //misil dirigido
        misilCrash = new Arma(assetManager.loadModel("Models/misil2/AGM-114HellFire.j3o"),"MisilCrash",mario);
        integrarObjeto(misilCrash.bala,misilCrash.balaFisica,estadosFisicos,misilCrash.posIniC, 0);
        misilCrash.aplicarFisica();        
            //caja Jugador
        cajaCrash = new Arma("CajaCrash");
        integrarObjeto(cajaCrash.balaG, cajaCrash.balaFisica, estadosFisicos, cajaCrash.posIniC,"tnt");        
        cajaCrash.balaFisica.setGravity(Vector3f.ZERO);
        
    //crear Armas Enemigo                
            //misil dirigido
        misilMario = new Arma(assetManager.loadModel("Models/misil2/AGM-114HellFire.j3o"),"MisilMario",crash);
        integrarObjeto(misilMario.bala,misilMario.balaFisica,estadosFisicos,misilMario.posIniC, 0);
        misilMario.aplicarFisica();        
            //caja Enemigo
        cajaMario = new Arma("CajaMario");
        integrarObjeto(cajaMario.balaG, cajaMario.balaFisica, estadosFisicos, cajaMario.posIniC,"tnt");        
        cajaMario.balaFisica.setGravity(Vector3f.ZERO);            
                        
    //crear setas        
            seta1 =new Seta(assetManager.loadModel("Models/Seta/untitled.j3o"),"Seta1");                                                
            integrarObjeto(seta1.seta, seta1.setaFisico, estadosFisicos, seta1.posicionActual(),0);
            seta1.propiedades();

            seta2 =new Seta(assetManager.loadModel("Models/Seta/untitled.j3o"),"Seta2");              
            integrarObjeto(seta2.seta, seta2.setaFisico, estadosFisicos, seta2.posicionActual(), 0);            
            seta2.cambiarPos(0);
            seta2.propiedades();
                
    //crear colision    
        colision=new Colision(crash,mario,misilCrash,misilMario,cajaCrash,cajaMario,seta1,seta2);
        estadosFisicos.getPhysicsSpace().addCollisionListener(colision);                           
        
    //cargar Teclado
        cntT= new ControladorTeclado(crash,mario);
        inicTeclado();
   }

    @Override
    public void simpleUpdate(float tpf) {
        tiempo=tiempo+tpf;

       
    //posiciones de los coches
        Vector3f posMario= mario.cocheFisico.getPhysicsLocation();
        Vector3f dirMario= mario.cocheFisico.getPhysicsRotation().getRotationColumn(2).normalize();
        Vector3f posCrash= crash.cocheFisico.getPhysicsLocation();
        Vector3f dirCrash= crash.cocheFisico.getPhysicsRotation().getRotationColumn(2).normalize();        
        
   //posicion de la camara______________________________________________________       
        Vector3f camP=posCrash;
        Vector3f dirC=dirCrash;
        if(mario.cam){
            camP=posMario;
            dirC=dirMario;
        }
        Vector3f parteTrasera = new Vector3f( camP.x-15*dirC.x, camP.y-3f*dirC.y+10f , camP.z - 15f*dirC.z );
        Vector3f parteDelantera= new Vector3f( camP.x+dirC.x, camP.y+dirC.y, camP.z + dirC.z );                                
        cam.setLocation( parteTrasera );
        cam.lookAt( parteDelantera, Vector3f.UNIT_Y);
                                        
//Rayos coche Crash+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++    
    //Detecta si pasa delante del enemigo el jugador y dispara
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
   
        CollisionResults detecta_ObtCrash = new CollisionResults();
        mario.coche.collideWith(crash.rayoObstaculo(),detecta_ObtCrash);
        cajaMario.balaG.collideWith(crash.rayoObstaculo(),detecta_ObtCrash);
        cajaCrash.balaG.collideWith(crash.rayoObstaculo(), detecta_ObtCrash);
        crash.esquivar(detecta_ObtCrash,distancia_CrashI,distancia_CrashD);                
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
        CollisionResults detecta_ObtMario = new CollisionResults();
        crash.coche.collideWith(mario.rayoObstaculo(),detecta_ObtMario);
        cajaMario.balaG.collideWith(mario.rayoObstaculo(), detecta_ObtMario);
        cajaCrash.balaG.collideWith(mario.rayoObstaculo(), detecta_ObtMario);
                
        mario.esquivar(detecta_ObtMario,distancia_marioI,distancia_marioD);
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
       
        if(!cargando){
    //Navegacion Jugador    
            float distancia_NavCrash = crash.cocheFisico.getPhysicsLocation().distance(nav.objFisico.getPhysicsLocation());
            nav.cambiarPos(distancia_NavCrash);
            crash.avanzar(seta1_Crash, seta2_Crash,seta1.seta.getLocalTranslation(),seta2.seta.getLocalTranslation());        

    //Navegacion Enemigo
            float distancia_NavMario= mario.cocheFisico.getPhysicsLocation().distance(nav2.objFisico.getPhysicsLocation());
            nav2.cambiarPos(distancia_NavMario);
            mario.avanzar(seta1_Mario,seta2_Mario,seta1.seta.getLocalTranslation(),seta2.seta.getLocalTranslation());                   
        }
        
    //actualizador semaforo para Colisiones.
        if(!colision.cambio){            
            colision.cambio=true;
        }        
                
        
    //texto en pantalla
        guiNode.detachAllChildren();
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        texto = new BitmapText(guiFont, false);
        texto.setSize(guiFont.getCharSet().getRenderedSize());
        if(tiempo<10){            
            int t=(int) tiempo;
            t=10-t;
            texto.setText(""+t);
            texto.setLocalTranslation(320,240,0);
            texto.setSize(150);            
        }else{        
            cargando=false;
            if(crash.cam){            
                if(misilCrash.usar || cajaCrash.usar || crash.tipoA==Coche.tipoArma.Turbo){
                    texto.setText("Tipo de Arma:"+crash.tipoA);
                }else{
                    texto.setText("Sin Armas");
                }
            }else{
                if(misilMario.usar || cajaMario.usar || mario.tipoA==Coche.tipoArma.Turbo){
                    texto.setText("Tipo de Arma:"+mario.tipoA);            
                }else{
                    texto.setText("Sin Armas");
                }            
            }    
            texto.setLocalTranslation(10, 480, 0);
            texto.setSize(25);
        }
        
        
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
        if (posicion == null) {
            posicion = Vector3f.ZERO;
        }
        if (giro == 1) {
            posicion=new Vector3f(posicion.x-4f,posicion.y,posicion.z-3f);            
        }
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
        if (posicion==null)   posicion = Vector3f.ZERO;
       objetoFisico.setPhysicsLocation(posicion);
    }
    
}
