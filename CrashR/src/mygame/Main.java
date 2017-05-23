package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Spatial;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    private RigidBodyControl sueloFisico;
    private BulletAppState estadosFisicos;
    private Colision colision=new Colision();
    private ControladorTeclado cntT;
    private Coche player1;
    Vector3f[] posSetas = new Vector3f[]{
        new Vector3f(-47, -4, 60f), new Vector3f(-13, -4, -37),
         new Vector3f(41, -4, 35), new Vector3f(50, -4, 84)};
    
     
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        estadosFisicos = new BulletAppState();
        stateManager.attach(estadosFisicos);

        flyCam.setEnabled(false);

        this.setDisplayFps(false);
        this.setDisplayStatView(false);

        //camara
        cam.setLocation(new Vector3f(0,220,0));
        cam.lookAt(new Vector3f(0,0,0), Vector3f.UNIT_Y);
        
        //luz direccional
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(-0.5f, -0.5f, -0.5f)).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);

        //carga la escena del mapa           
        Spatial mundo = assetManager.loadModel("Scenes/mapCity.j3o");
        rootNode.attachChild(mundo);

        //carga esqueleto mapa y da propiedades
        Spatial suelo = assetManager.loadModel("Scenes/colision.j3o");
        suelo.setName("Suelo");
        rootNode.attachChild(suelo);
        sueloFisico = new RigidBodyControl(0.0f);
        integrarObjeto(suelo, sueloFisico, estadosFisicos, new Vector3f(0, -4.1f, 0), 0);
        sueloFisico.setRestitution(0.05f);
        sueloFisico.setFriction(0.85f);
                                
        
        //crear coche        
        player1=new Coche(assetManager.loadModel("Models/coche/CAMARO.j3o"), "Coche");                                
        integrarObjeto(player1.coche, player1.cocheFisico, estadosFisicos, player1.posIniC, 1);
        player1.propiedades(0.8f,0.85f);
                        
        //propiedades colision
        estadosFisicos.getPhysicsSpace().addCollisionListener(colision);
        
        //crear setas
        for (int i = 0; i < posSetas.length; i++) {
            Seta seta =new Seta(assetManager.loadModel("Models/Seta/untitled.j3o"),"Seta"+i);                        
            integrarObjeto(seta.seta, seta.setaFisico, estadosFisicos, posSetas[i], 2);
            seta.propiedades();
        }

        Ruta esfera=new Ruta(assetManager.loadModel("Models/Seta/untitled.j3o"),"Objetivo");       
        integrarObjeto(esfera.objbola, esfera.objFisico, estadosFisicos, esfera.posicionActual(),0);
        esfera.propiedades();
        
        
        //cargar Teclado
        cntT= new ControladorTeclado(player1);
        inicTeclado();
   }

    @Override
    public void simpleUpdate(float tpf) {
        
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

}
