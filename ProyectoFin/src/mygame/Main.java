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
import com.jme3.math.Ray;
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
    private Coche enemigo;
    private Coche jugador;
    private Ruta objetivo;
    private Arma misilJD;
    private Arma misilED;        
    private Arma cajaE;
    private Arma cajaJ;
    
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
        
        //camara por defecto
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
        //mundo.setLocalScale(mundo.getLocalScale().x*3,mundo.getLocalScale().y*3,mundo.getLocalScale().z*3);
        rootNode.attachChild(mundo);

        //carga esqueleto mapa y da propiedades
        Spatial suelo = assetManager.loadModel("Scenes/colision.j3o");
        //suelo.setLocalScale(suelo.getLocalScale().x*3,suelo.getLocalScale().y*3,suelo.getLocalScale().z*3);
        suelo.setName("Suelo");
        sueloFisico = new RigidBodyControl(0.0f);                
        integrarObjeto(suelo, sueloFisico, estadosFisicos, new Vector3f(0f, -4.1f,0f), 0);
        sueloFisico.setRestitution(0.9f);
        sueloFisico.setFriction(0.5f);
                                                
        //crear objeto Volador
        objetivo=new Ruta();
        integrarObjeto(objetivo.objetivoGeom, objetivo.objFisico, estadosFisicos, objetivo.posicionActual(), "");
        objetivo.aplicarFisica();
        
        
        //crear enemigo
        
        //enemigo=new Coche("Enemigo", objetivo);
        //integrarObjeto(enemigo.geomBox, enemigo.cocheFisico, estadosFisicos, enemigo.posIniC,"");
        enemigo=new Coche(assetManager.loadModel("Models/Mario/Kart_Mario.j3o"), "Enemigo", objetivo);
        integrarObjeto(enemigo.coche, enemigo.cocheFisico, estadosFisicos, enemigo.posIniC,0);
        enemigo.cocheFisico.setLinearDamping(0.5f);
        
        
        //crear coche Jugador
        jugador=new Coche(assetManager.loadModel("Models/Crash_Kart/Crash_Kart.j3o"),"Jugador");
        integrarObjeto(jugador.coche, jugador.cocheFisico, estadosFisicos, jugador.posIniC2,0);
        jugador.aplicarFisica();        
                
        //crear Armas Enemigo
        //misilE = new Arma("MisilE",jugador);
        //integrarObjeto(misilED.balaG,misilED.balaFisica,estadosFisicos,misilED.posIniC, "");
        
        //misil dirigido
        misilED = new Arma(assetManager.loadModel("Models/misil2/AGM-114HellFire.j3o"),"MisilE",jugador);
        integrarObjeto(misilED.bala,misilED.balaFisica,estadosFisicos,misilED.posIniC, 0);
        misilED.aplicarFisica();        
        //caja Enemigo
        cajaE = new Arma("MisilE", jugador);
        integrarObjeto(cajaE.balaG, cajaE.balaFisica, estadosFisicos, cajaE.posIniC,"tnt");        
        cajaE.balaFisica.setGravity(Vector3f.ZERO);
        
        //crear armas
        //misilJ = new Arma("MisilJ",enemigo);
        //integrarObjeto(misilJD.balaG,misilJD.balaFisica,estadosFisicos,misilJD.posIniC, "");
        
        //misil dirigido
        misilJD = new Arma(assetManager.loadModel("Models/misil2/AGM-114HellFire.j3o"),"MisilJ",enemigo);
        integrarObjeto(misilJD.bala,misilJD.balaFisica,estadosFisicos,misilJD.posIniC, 0);
        misilJD.aplicarFisica();        
        //caja Enemigo
        cajaJ = new Arma("MisilJ", jugador);
        integrarObjeto(cajaJ.balaG, cajaJ.balaFisica, estadosFisicos, cajaJ.posIniC,"");        
        cajaJ.balaFisica.setGravity(Vector3f.ZERO);
                
        
        //crear setas        
            Seta seta1 =new Seta(assetManager.loadModel("Models/Seta/untitled.j3o"),"Seta1");                                                
            integrarObjeto(seta1.seta, seta1.setaFisico, estadosFisicos, seta1.posicionActual(),2);
            seta1.propiedades();
            Seta seta2 =new Seta(assetManager.loadModel("Models/Seta/untitled.j3o"),"Seta2");  
            seta2.id=2;
            integrarObjeto(seta2.seta, seta2.setaFisico, estadosFisicos, seta2.posicionActual(), 2);
            seta2.propiedades();
        
        
        //crear colision
        colision=new Colision(objetivo,misilJD,misilED,cajaJ,cajaE,seta1,seta2);
        estadosFisicos.getPhysicsSpace().addCollisionListener(colision);
        
        
        
        
        //cargar Teclado
        cntT= new ControladorTeclado(jugador,misilJD,enemigo);
        inicTeclado();
   }

    @Override
    public void simpleUpdate(float tpf) {
        tiempo=tiempo+tpf;
        
        //posicion de la camara
        Vector3f pos= enemigo.cocheFisico.getPhysicsLocation();
        Vector3f dir = enemigo.cocheFisico.getPhysicsRotation().getRotationColumn(2).normalize();
        Vector3f parteTrasera = new Vector3f( pos.x-15*dir.x, pos.y-3f*dir.y+10f , pos.z - 15f*dir.z );
        Vector3f parteDelantera= new Vector3f( pos.x+dir.x, pos.y+dir.y, pos.z + dir.z );
        cam.setLocation( parteTrasera );
        cam.lookAt( parteDelantera, Vector3f.UNIT_Y);
        
        //mover enemigo
        enemigo.avanzar();
        
        //Rayo detector enemigo a mi coche
        Ray rayo = new Ray (new Vector3f (enemigo.cocheFisico.getPhysicsLocation().x, enemigo.cocheFisico.getPhysicsLocation().y-0.5f, enemigo.cocheFisico.getPhysicsLocation().z),enemigo.mirahacia());            
        CollisionResults results1 = new CollisionResults();
        jugador.coche.collideWith(rayo, results1);
        misilED.dectector(results1,enemigo.cocheFisico.getPhysicsLocation());
        
        //detector caja enemigo al misil
        Vector3f posM= misilJD.balaFisica.getPhysicsLocation();
        Vector3f dirM = misilJD.balaFisica.getPhysicsRotation().getRotationColumn(2).normalize();
        Vector3f parteM = new Vector3f( posM.x+1f*dirM.x, posM.y, pos.z+1f*dir.z );
        float distancia = enemigo.cocheFisico.getPhysicsLocation().distance(misilJD.balaFisica.getPhysicsLocation());        
        cajaE.defensa(distancia, parteM);
        
        
        misilJD.avanzarMD();
        
        if(!colision.cambio){            
            colision.cambio=true;
        }
        if(!colision.cambio1){
            colision.cambio1=true;            
            
        }
        /*
        //texto en pantalla
        guiNode.detachAllChildren();
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        texto = new BitmapText(guiFont, false);
        texto.setSize(guiFont.getCharSet().getRenderedSize());
        switch(colision.tipoA){
            case 0:
                texto.setText("Tipo de Arma: Misil Dirigido");
                break;
            case 1:
                texto.setText("Tipo de Arma: Caja");
                break;
        }        
        texto.setColor(ColorRGBA.Red);
        texto.setLocalTranslation(10, 480, 0);
        texto.setSize(25);
        texto.setName("Texto");
        guiNode.attachChild(texto);
        */
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
    private void inicTeclado() {
        inputManager.addMapping("Avanzar", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Atras", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Izquierda", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Derecha", new KeyTrigger(KeyInput.KEY_D));        
        inputManager.addMapping("Mute", new KeyTrigger(KeyInput.KEY_M));
        inputManager.addMapping("Depie", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(cntT.analogListener, "Izquierda", "Derecha", "Avanzar", "Atras","Mute", "Depie");
    }
    
    public void integrarObjeto(Spatial objetoVisual, RigidBodyControl objetoFisico, BulletAppState estadosFisicos, Vector3f posicion, int giro) {
        rootNode.attachChild(objetoVisual);                                               //integración en el mundo visual 
        objetoVisual.addControl(objetoFisico);                                          //Asociación  objeto visual-fisico
        estadosFisicos.getPhysicsSpace().add(objetoFisico);                  //integración en el mundo físico
        if (posicion == null) {
            posicion = Vector3f.ZERO;
        }
        if (giro == 1) {
            Matrix3f mat = new Matrix3f();
            mat.fromAngleAxis(1, new Vector3f(0, -90, 0));
            objetoFisico.setPhysicsRotation(mat);
        } else if (giro == 2) {
            Matrix3f mat = new Matrix3f();
            mat.fromAngleAxis(1, new Vector3f(-90, 0, 0));
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
