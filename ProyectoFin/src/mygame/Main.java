package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
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
    private Coche enemigo,jugador;
    private Ruta nav,nav2;
    private Arma misilJD,misilED,cajaE,cajaJ;
    private Seta seta1,seta2;
    private Spatial suelo;    
    private BitmapText texto;  
    
    private RigidBodyControl sueloFisico;    
    private Colision colision;
    private ControladorTeclado cntT;    
    float tiempo=0; 
    
    
     
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
        nav=new Ruta();
        integrarObjeto(nav.objetivoGeom, nav.objFisico, estadosFisicos, nav.posicionActual(), "");
        nav.aplicarFisica();
              
        nav2=new Ruta();
        integrarObjeto(nav2.objetivoGeom, nav2.objFisico, estadosFisicos, nav2.posicionActual2(), "");
        nav2.aplicarFisica();
        
    //crear enemigo                
        enemigo=new Coche(assetManager.loadModel("Models/Mario/Kart_Mario.j3o"), "Enemigo", nav2);
        integrarObjeto(enemigo.coche, enemigo.cocheFisico, estadosFisicos, enemigo.posIniC,1);
        enemigo.aplicarFisica();        
                
    //crear Jugador
        jugador=new Coche(assetManager.loadModel("Models/Crash_Kart/Crash_Kart.j3o"),"Jugador",nav);
        integrarObjeto(jugador.coche, jugador.cocheFisico, estadosFisicos, jugador.posIniC,0);
        jugador.aplicarFisica();        
        jugador.cam=true;
                
    //crear Armas Enemigo                
            //misil dirigido
        misilED = new Arma(assetManager.loadModel("Models/misil2/AGM-114HellFire.j3o"),"MisilE",jugador);
        integrarObjeto(misilED.bala,misilED.balaFisica,estadosFisicos,misilED.posIniC, 0);
        misilED.aplicarFisica();        
            //caja Enemigo
        cajaE = new Arma("CajaE", jugador);
        integrarObjeto(cajaE.balaG, cajaE.balaFisica, estadosFisicos, cajaE.posIniC,"tnt");        
        cajaE.balaFisica.setGravity(Vector3f.ZERO);
        
    //crear armas Jugador                
            //misil dirigido
        misilJD = new Arma(assetManager.loadModel("Models/misil2/AGM-114HellFire.j3o"),"MisilJ",enemigo);
        integrarObjeto(misilJD.bala,misilJD.balaFisica,estadosFisicos,misilJD.posIniC, 0);
        misilJD.aplicarFisica();        
            //caja Jugador
        cajaJ = new Arma("CajaJ", jugador);
        integrarObjeto(cajaJ.balaG, cajaJ.balaFisica, estadosFisicos, cajaJ.posIniC,"tnt");        
        cajaJ.balaFisica.setGravity(Vector3f.ZERO);
                        
    //crear setas        
            seta1 =new Seta(assetManager.loadModel("Models/Seta/untitled.j3o"),"Seta1");                                                
            integrarObjeto(seta1.seta, seta1.setaFisico, estadosFisicos, seta1.posicionActual(),2);
            seta1.propiedades();

            seta2 =new Seta(assetManager.loadModel("Models/Seta/untitled.j3o"),"Seta2");              
            integrarObjeto(seta2.seta, seta2.setaFisico, estadosFisicos, seta2.posicionActual(), 2);            
            seta2.cambiarPos(0);
            seta2.propiedades();
        
        
    //crear colision
        colision=new Colision(jugador,enemigo,misilJD,misilED,cajaJ,cajaE,seta1,seta2);
        estadosFisicos.getPhysicsSpace().addCollisionListener(colision);                           
        
    //cargar Teclado
        cntT= new ControladorTeclado(jugador,enemigo);
        inicTeclado();
   }

    @Override
    public void simpleUpdate(float tpf) {
        tiempo=tiempo+tpf;
    //posiciones de los coches
        Vector3f posE= enemigo.cocheFisico.getPhysicsLocation();
        Vector3f dirE= enemigo.cocheFisico.getPhysicsRotation().getRotationColumn(2).normalize();
        Vector3f posJ= jugador.cocheFisico.getPhysicsLocation();
        Vector3f dirJ= jugador.cocheFisico.getPhysicsRotation().getRotationColumn(2).normalize();        
        
   //posicion de la camara______________________________________________________       
        Vector3f camP=posJ;
        Vector3f dirC=dirJ;
        if(enemigo.cam){
            camP=posE;
            dirC=dirE;
        }
        Vector3f parteTrasera = new Vector3f( camP.x-15*dirC.x, camP.y-3f*dirC.y+10f , camP.z - 15f*dirC.z );
        Vector3f parteDelantera= new Vector3f( camP.x+dirC.x, camP.y+dirC.y, camP.z + dirC.z );                                
        cam.setLocation( parteTrasera );
        cam.lookAt( parteDelantera, Vector3f.UNIT_Y);
                                        
//Rayos coche enemigo-------------------------------------------------------------------------------------------------------------
    //Detecta si pasa delante del enemigo el jugador y dispara
        CollisionResults detecJugador = new CollisionResults();
        jugador.coche.collideWith(enemigo.rayoFrente(), detecJugador);        
        misilED.dectector(detecJugador,enemigo.cocheFisico.getPhysicsLocation());
                
    //Detecta distancia con pared del coche enemigo
        CollisionResults enemigoIz = new CollisionResults();
        suelo.collideWith(enemigo.rayosIzq(),enemigoIz);
        float distEI=0;
        float distED=0;
        if (enemigoIz.getClosestCollision() != null){
            distEI=enemigoIz.getClosestCollision().getDistance();
        }
        CollisionResults enemigoDr = new CollisionResults();
        suelo.collideWith(enemigo.rayosDer(),enemigoDr);
        if(enemigoDr.getClosestCollision()!=null){
            distED=enemigoDr.getClosestCollision().getDistance();        
        }                
    //Detecta enemigo un obstaculo
        CollisionResults detecObtE = new CollisionResults();
        jugador.coche.collideWith(enemigo.rayoObstaculo(),detecObtE);
        cajaE.balaG.collideWith(enemigo.rayoObstaculo(), detecObtE);
        cajaJ.balaG.collideWith(enemigo.rayoObstaculo(), detecObtE);
        enemigo.esquivar(detecObtE,distEI,distED);
//--------------------------------------------------------------------------------------------------------------------------------------
//Rayos coche jugador+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++    
    //Detecta si pasa delante del enemigo el jugador y dispara
        CollisionResults detecEnemigo = new CollisionResults();
        enemigo.coche.collideWith(jugador.rayoFrente(), detecEnemigo);        
        misilJD.dectector(detecEnemigo,jugador.cocheFisico.getPhysicsLocation());
                
    //Detecta distancia con pared del coche enemigo
        CollisionResults jugadorIz = new CollisionResults();
        suelo.collideWith(jugador.rayosIzq(),jugadorIz);
        float distJI=0;
        float distJD=0;
        if (jugadorIz.getClosestCollision() != null){
            distJI=jugadorIz.getClosestCollision().getDistance();
        }
        CollisionResults jugadorDr = new CollisionResults();
        suelo.collideWith(jugador.rayosDer(),jugadorDr);
        if(jugadorDr.getClosestCollision()!=null){
            distJD=jugadorDr.getClosestCollision().getDistance();        
        }                
    //Detecta enemigo un obstaculo
        CollisionResults detecObtJ = new CollisionResults();
        enemigo.coche.collideWith(jugador.rayoObstaculo(),detecObtJ);
        cajaE.balaG.collideWith(jugador.rayoObstaculo(), detecObtJ);
        cajaJ.balaG.collideWith(jugador.rayoObstaculo(), detecObtJ);
        jugador.esquivar(detecObtJ,distJI,distJD);                
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    //detectores para defensa y redirrecion hacia seta            
        Vector3f posM= misilJD.balaFisica.getPhysicsLocation();
        Vector3f dirM = misilJD.balaFisica.getPhysicsRotation().getRotationColumn(2).normalize();
        Vector3f parteM = new Vector3f( posM.x+2.5f*dirM.x, posM.y, posM.z+2.5f*dirM.z );                
        float distancia = enemigo.cocheFisico.getPhysicsLocation().distance(misilJD.bala.getLocalTranslation());                         
        float dseta1 = enemigo.cocheFisico.getPhysicsLocation().distance(seta1.setaFisico.getPhysicsLocation());
        float dseta2 = enemigo.cocheFisico.getPhysicsLocation().distance(seta2.setaFisico.getPhysicsLocation());               
        Vector3f posCE = new Vector3f( posE.x-2f*dirE.x, posE.y-dirE.y, posE.z-2f*dirE.z );                
        cajaE.defensa(dseta1,dseta2,distancia, parteM, posCE);                
        
                //distancia seta con jugador
        Vector3f posME= misilED.balaFisica.getPhysicsLocation();
        Vector3f dirME= misilED.balaFisica.getPhysicsRotation().getRotationColumn(2).normalize();
        Vector3f parteME = new Vector3f( posME.x+2.5f*dirME.x, posME.y, posME.z+2.5f*dirME.z );
        float distancia2 = jugador.cocheFisico.getPhysicsLocation().distance(misilED.bala.getLocalTranslation());                        
        float djseta1 = jugador.cocheFisico.getPhysicsLocation().distance(seta1.setaFisico.getPhysicsLocation());
        float djseta2 = jugador.cocheFisico.getPhysicsLocation().distance(seta2.setaFisico.getPhysicsLocation());        
        Vector3f posCJ = new Vector3f(posJ.x-2f*dirJ.x,posJ.y-dirJ.y,posJ.z-2f*dirJ.z);
        cajaJ.defensa(djseta1,djseta2,distancia2,parteME, posCJ);
        
        
//Navegacion Jugador    
        float dobj = jugador.cocheFisico.getPhysicsLocation().distance(nav.objFisico.getPhysicsLocation());
        nav.cambiarPos(dobj);
        jugador.avanzar(djseta1, djseta2,seta1.seta.getLocalTranslation(),seta2.seta.getLocalTranslation());        
        
//Navegacion Enemigo
        float dobe= enemigo.cocheFisico.getPhysicsLocation().distance(nav2.objFisico.getPhysicsLocation());
        nav2.cambiarPos2(dobe);
        enemigo.avanzar(dseta1,dseta2,seta1.seta.getLocalTranslation(),seta2.seta.getLocalTranslation());
        
        //System.out.println("Crash:"+posJ.y+" Mario:"+posE.y);
        
        
    //actualizador semaforo para Colisiones.
        if(!colision.cambio){            
            colision.cambio=true;
        }        
                
        
    //texto en pantalla
        guiNode.detachAllChildren();
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        texto = new BitmapText(guiFont, false);
        texto.setSize(guiFont.getCharSet().getRenderedSize());
        if(jugador.cam){            
            if(misilJD.usar || cajaJ.usar || jugador.tipoA==Coche.tipoArma.Turbo){
                texto.setText("Tipo de Arma:"+jugador.tipoA);
            }else{
                texto.setText("Sin Armas");
            }
        }else{
            if(misilED.usar || cajaE.usar || enemigo.tipoA==Coche.tipoArma.Turbo){
                texto.setText("Tipo de Arma:"+enemigo.tipoA);            
            }else{
                texto.setText("Sin Armas");
            }
            
        }                        
        texto.setColor(ColorRGBA.Red);
        texto.setLocalTranslation(10, 480, 0);
        texto.setSize(25);
        texto.setName("Texto");
        guiNode.attachChild(texto);
        
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
    private void inicTeclado() {
        inputManager.addMapping("CamJ", new KeyTrigger(KeyInput.KEY_Z));
        inputManager.addMapping("CamE", new KeyTrigger(KeyInput.KEY_X));        
        inputManager.addMapping("MuteOFF", new KeyTrigger(KeyInput.KEY_N));
        inputManager.addMapping("MuteON", new KeyTrigger(KeyInput.KEY_M));                
        inputManager.addListener(cntT.analogListener, "CamE","CamJ","MuteOFF","MuteON");
    }
    
    public void integrarObjeto(Spatial objetoVisual, RigidBodyControl objetoFisico, BulletAppState estadosFisicos, Vector3f posicion, int giro) {
        rootNode.attachChild(objetoVisual);                                               //integración en el mundo visual 
        objetoVisual.addControl(objetoFisico);                                          //Asociación  objeto visual-fisico
        estadosFisicos.getPhysicsSpace().add(objetoFisico);                  //integración en el mundo físico
        if (posicion == null) {
            posicion = Vector3f.ZERO;
        }
        if (giro == 1) {
            posicion=new Vector3f(posicion.x-4f,posicion.y,posicion.z+5f);            
        } else if (giro == 2) {
            Matrix3f mat = new Matrix3f();
            mat.fromAngleAxis(1, new Vector3f(0, -90, 0));
            objetoFisico.setPhysicsRotation(mat);
        }
        objetoFisico.setPhysicsLocation(posicion);
    }

    public  void  integrarObjeto (Geometry objetoVisual, RigidBodyControl objetoFisico, BulletAppState estadosFisicos, Vector3f posicion, String textura){ 
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"); 
        if ((textura!=null)&& (textura.length()>10))  material.setTexture("ColorMap", assetManager.loadTexture( new TextureKey(textura)));        
        else if (textura.equals("tnt")){
            material = assetManager.loadMaterial("Materials/Generated/caja.j3m");            
        }
        else   {  material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");  
                    material.setBoolean("UseMaterialColors", true);
                    material.setColor("Diffuse", new ColorRGBA(0.7f, (float) Math.random(), 0.5f, 1f));  }
        objetoVisual.setMaterial(material);    
        rootNode.attachChild(objetoVisual);                                               //integración en el mundo visual 
        objetoVisual.addControl(objetoFisico );                                          //Asociación  objeto visual-fisico
        estadosFisicos.getPhysicsSpace().add( objetoFisico );                  //integración en el mundo físico
        if (posicion==null)   posicion = Vector3f.ZERO;
       objetoFisico.setPhysicsLocation(posicion);
    }
    
}
