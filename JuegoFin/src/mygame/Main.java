package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Spatial;

public class Main extends SimpleApplication {

    private BulletAppState estadosFisicos;
    private ControladorFisico objetivoFisico;
    private ControladorFisico cocheFisico;
    private RigidBodyControl sueloFisico;
    private RigidBodyControl cajaFisico;
    private AudioNode audio_seta;
    private AudioNode audio_fin;
    private AudioNode audio_base;
    private boolean mute=false;
    private BitmapText texto;
    float alturaVision = 1.6f;
    float tiempo = 0;
    private int cont;
    Vector3f posIniC = new Vector3f(-10, -3.5f, 7.75f);
    Vector3f[] posSetas = new Vector3f[]{
        new Vector3f(-42, -4, 8.4f), new Vector3f(-13, -4, -37),
         new Vector3f(41, -4, 35), new Vector3f(50, -4, 84)};

    GhostControl ghostControl;
    
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

        //Crear coche y dar propiedades fisicas
        Spatial coche = assetManager.loadModel("Models/coche/CAMARO.j3o");
        coche.setName("Coche");        
        //peso del coche 20kg * fuerza gravedad 9.8        
        cocheFisico = new ControladorFisico(196f, estadosFisicos, coche);
        integrarObjeto(coche, cocheFisico, estadosFisicos, posIniC, 1);

        cocheFisico.setRestitution(0.8f);
        cocheFisico.setFriction(0.8f);

        //crear Seta                
        for (int i = 0; i < posSetas.length; i++) {
            Spatial seta = assetManager.loadModel("Models/Seta/untitled.j3o");
            seta.setName("Seta" + i);
            objetivoFisico = new ControladorFisico(9.8f, estadosFisicos, seta);
            integrarObjeto(seta, objetivoFisico, estadosFisicos, posSetas[i], 2);
        }

        /*
        //coche de prueba ------------------
         Box  c = new Box(0.3f, 0.3f, 0.5f);
        Geometry coche = new Geometry("Coche", c);
         */
        /*
        //crear Fantasma Coche        
        Vector3f halfExtents = new Vector3f(3, -4f, 1);
        ghostControl = new GhostControl(new BoxCollisionShape(halfExtents));
        Node node=new Node("Fantasma");
        node.addControl(ghostControl);
        rootNode.attachChild(node);
        getPhysicsSpace().add(ghostControl);
        */
        
        initAudio();        
        inicTeclado();
        
    }

    @Override
    public void simpleUpdate(float tpf) {
        tiempo = tiempo + tpf;
        
        Vector3f pos = cocheFisico.getPhysicsLocation();
        Vector3f dir = cocheFisico.getPhysicsRotation().getRotationColumn(2).normalize();
        
        actualizarDato();
        
        //System.out.println("Overlapping objects: " + ghostControl.getOverlappingObjects().toString());
        
        Vector3f parteTrasera = new Vector3f(pos.x - (4f * dir.x), pos.y + 1f, pos.z - 4f * dir.z);
        Vector3f parteDelantera = new Vector3f(pos.x + dir.x, pos.y + alturaVision, pos.z + dir.z);
        cam.setLocation(parteTrasera);
        cam.lookAt(parteDelantera, Vector3f.UNIT_Y);

        
        //Texto de pantalla      
        guiNode.detachAllChildren();
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        texto = new BitmapText(guiFont, false);
        texto.setSize(guiFont.getCharSet().getRenderedSize());
        if(cont!=posSetas.length){
            texto.setText("Setas Cogidas:" + cont + " de " + posSetas.length);
        }else{
            audio_base.stop();
            audio_fin.play();
            texto.setText("Felicidades!!! Has Cogido todas las Setas");
        }
        texto.setColor(ColorRGBA.Red);
        texto.setLocalTranslation(10, 600, 0);
        texto.setSize(25);
        texto.setName("Texto");
        guiNode.attachChild(texto);
    }

    @Override
    public void simpleRender(RenderManager rm) {
    }

    private void inicTeclado() {
        inputManager.addMapping("Avanzar", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Atras", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Izquierda", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Derecha", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("UCam", new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("DCam", new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping("Mute", new KeyTrigger(KeyInput.KEY_M));
        inputManager.addMapping("Depie", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(analogListener, "Izquierda", "Derecha", "Avanzar", "Atras", "UCam", "DCam","Mute", "Depie");
    }

    private AnalogListener analogListener = new AnalogListener() {
        @Override
        public void onAnalog(String name, float value, float tpf) {

            Vector3f dirFrente = cocheFisico.getPhysicsRotation().getRotationColumn(2);

            float fuerza = cocheFisico.getMass() * 10f;

            if (name.equals("Derecha")) {
                cocheFisico.applyTorque(new Vector3f(0, -(fuerza/10), 0));
                //System.out.println("Nodo Coche: "+cocheFisico);                
            }
            if (name.equals("Izquierda")) {
                cocheFisico.applyTorque(new Vector3f(0, (fuerza/10), 0));
            }
            if (name.equals("Avanzar")) {
                cocheFisico.applyCentralForce(dirFrente.normalize().mult(fuerza));
                //System.out.println("Angulo "+dirFrente.toString());
            }
            if (name.equals("Atras")) {
                //cocheFisico.applyCentralForce(new Vector3f(0,0,fuerza));
                cocheFisico.applyCentralForce(dirFrente.normalize().mult(-fuerza));
            }
            if (name.equals("UCam")) {
                alturaVision = alturaVision + 0.01f;
                alturaVision = Math.min(50, alturaVision);
            }
            if (name.equals("DCam")) {
                alturaVision = alturaVision - 0.01f;
                alturaVision = Math.max(1, alturaVision);
            }
            if (name.equals("Mute")) {                                
                if(mute){                    
                    audio_base.setVolume(3);
                    audio_seta.setVolume(2);
                    audio_fin.setVolume(2);
                    mute=false;
                }else{
                    audio_base.setVolume(0);
                    audio_seta.setVolume(0);
                    audio_fin.setVolume(0);
                    mute=true;
                }
                
            }            
            if (name.equals("Depie")) {
                cocheFisico.clearForces();
                Matrix3f mat = new Matrix3f();
                mat.fromAngleAxis(1, new Vector3f(0, -90, 0));
                cocheFisico.setPhysicsRotation(mat);
                cocheFisico.setPhysicsLocation(posIniC);
            }
        }
    };

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

    private class ControladorFisico extends RigidBodyControl implements PhysicsTickListener, PhysicsCollisionListener {

        boolean semaforo = false;
        BulletAppState estadosFisicos;
        RigidBodyControl objetivoFisico;
        Spatial thisGeometria = null;

        public ControladorFisico(float masa, BulletAppState estadosFisicos_, Spatial obj) {
            super(masa);
            estadosFisicos = estadosFisicos_;
            estadosFisicos.getPhysicsSpace().addCollisionListener(this);
            thisGeometria = obj;
        }
        
        @Override
        public void prePhysicsTick(PhysicsSpace space, float tpf) {
            //aqui para mover supuestamente esto
        }

        @Override
        public void physicsTick(PhysicsSpace space, float tpf) {
        }

        @Override
        public void collision(PhysicsCollisionEvent event) {
            if (!event.getNodeB().getName().equals("Suelo")) {            
                //if(event.getNodeB().getName().equalsIgnoreCase("Fantasma")){
                   //System.out.println(event.getNodeB().getName() + " colisionó con " + event.getNodeA().getName());                                              
                    rootNode.detachChildNamed(event.getNodeB().getName());
                    int id = Integer.parseInt(event.getNodeB().getName().substring(4));
                    posSetas[id] = null;                    
                //}
                
            }

        }

    }
    
    public void actualizarDato(){
        int var=cont;
        cont=0;
        for (Vector3f posSeta : posSetas) {
            if (posSeta == null) {
                cont++;
            }
        }
        if(cont>var){
            //System.out.println("Correcto");
            audio_seta.playInstance();
        }
    }
    
    private void initAudio() {
        
    /* Al chocar con la seta. */        
    audio_seta = new AudioNode(assetManager,"Sounds/1up.wav", DataType.Buffer);
    audio_seta.setPositional(false);
    audio_seta.setLooping(false);
    audio_seta.setVolume(2);
    rootNode.attachChild(audio_seta);

    // Al terminar el juego
    audio_fin = new AudioNode(assetManager,"Sounds/completado.wav", DataType.Buffer);
    audio_fin.setPositional(false);
    audio_fin.setLooping(true);
    audio_fin.setVolume(2);
    rootNode.attachChild(audio_fin);
    
    /* Base del juego. */
    audio_base = new AudioNode(assetManager, "Sounds/base.wav", DataType.Stream);
    audio_base.setPositional(false);
    audio_base.setLooping(true);  // activate continuous playing    
    audio_base.setVolume(3);    
    rootNode.attachChild(audio_base);
    audio_base.play();
  }
    

}
