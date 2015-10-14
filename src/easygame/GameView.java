/*The MIT License (MIT)

Copyright (c) 2015 Willians Magalhães Primo

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE. */

package easygame;

import java.util.concurrent.ConcurrentHashMap;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Essa é a classe que representa tanto o jogo como a tela onde os objetos serão desenhados.
 * @author Wiliams Magalhães Primo
 */
@SuppressLint("ViewConstructor") 
public class GameView extends SurfaceView implements OnGestureListener, OnScaleGestureListener{
	
	//Propriedades do GameView
	protected int lastEvent = MotionEvent.ACTION_CANCEL;
	private int FPS = 30;
	private GameLoop gameLoop;
	private SurfaceHolder holder;
	private GestureDetector gestureDetector = null;
	private ScaleGestureDetector ScaleGestureDetector = null;
	
	private Scene scene = null;
	private PhysicSimulator physicSimulator = new PhysicSimulator(null);
	private ConcurrentHashMap <String , Object> objects = new ConcurrentHashMap <String, Object>();

	//Construtores
	public GameView(Context context, Scene scene) {
		super(context);
		this.scene = scene;
		initialize();
	}
	
	public GameView(Context context,Scene scene, int FPS) {
		super(context);
		this.scene = scene;
		this.setFPS(FPS);
		initialize();
	}
	
	/**
	 * Função de incicialização do GameView.
	 */
	private void initialize(){
		gameLoop = new GameLoop(this);
		//Pega o holder do sufaceView
		holder = getHolder();
		
		//Determina os metodos de callBack para o holder
		holder.addCallback(new SurfaceHolder.Callback() {
			@Override
			//Método Chamdo no momento da criação da SurfaceView
			public void surfaceCreated(SurfaceHolder holder) {
				if(gameLoop.getState() == Thread.State.TERMINATED){
					//Caso a thread tenha sido finalisada é necessário criar uma nova
					gameLoop = new GameLoop(GameView.this);
				}else{
					load();
				}
				gameLoop.setRunning(true);
				gameLoop.start();
			}

			//Método chamado no momento em que a SurfaceView é destruida
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				//Finalisa a thead do GameLoop
				gameLoop.setRunning(false);
			}
			
			//Método chamdo quando ouver mudanças na SufaceView
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
				scene.getCamera().setActualHeight(getHeight());
				scene.getCamera().setActualWidth(getWidth());
			}
		});
	}
	
	public void finalize(){
		this.scene.unload();
		this.objects.clear();
	}
	
	/**
	 * Método chamdo ao ocorrer algum evento de toque na tela.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event){
		gestureDetector.onTouchEvent(event);
		ScaleGestureDetector.onTouchEvent(event);
		
		if(event.getAction() != lastEvent){
			touchEvents(event);
		}
		
		lastEvent = event.getAction();
		return true;
	}
	
	/**
	 * Método chamdo para desenhar todos os elementos da sena atual.
	 */
	@Override
	protected void onDraw(Canvas canvas){
		if(canvas != null){
			super.onDraw(canvas);
			canvas.drawRGB(0, 0, 0);
			scene.draw(canvas);
		}
	}
	
	/**
	 * Metododo para tratamento dos eventos de toque na tela.
	 * @param event
	 */
	public void touchEvents(MotionEvent event){
		this.scene.touchEvents(event);
	}
	
	/**
	 * Método que garregamento do jogo
	 */
	public void load(){
		loadScene(this.scene);
		gestureDetector = new GestureDetector(getContext(), this);
		ScaleGestureDetector = new ScaleGestureDetector(getContext(), this);
	}
	
	/**
	 * Método que carrega uma sena como atual no Jogo.
	 * @param scene
	 */
	public void loadScene(Scene scene){
		this.scene = scene;
		this.scene.setGameView(this);
		this.physicSimulator.setScene(this.scene);
		this.scene.getCamera().setActualHeight(getHeight());
		this.scene.getCamera().setActualWidth(getWidth());
		this.scene.onLoad();
	}
	
	/**
	 * Função de atualização
	 */
	protected void update(){
		this.scene.update();
	}
	
	/**
	 * Método de tratamento do evento de toque, ao tocar.
	 */
	@Override
	public boolean onDown(MotionEvent event) {
		boolean result = true;
		if(event.getAction() != lastEvent){
			scene.setLastEvents(event, null);
			result = scene.down(event);
			touchEvents(event);
		}
		lastEvent = event.getAction();
		
		return result;
	}

	/**
	 * Método de tratamento de evento ao arrastar.
	 */
	@Override
	public boolean onFling(MotionEvent firstEvent, MotionEvent secondEvent, float velocityX, float velocityY) {
		firstEvent.setAction(MotionEvent.ACTION_MOVE);
		scene.setLastEvents(firstEvent, secondEvent);
		touchEvents(firstEvent);
		return scene.fling(firstEvent, secondEvent, velocityX, velocityY);
	}

	/**
	 * Método de tratamento de evento ao precionar por um longo tempo.
	 */
	@Override
	public void onLongPress(MotionEvent event) {
		scene.setLastEvents(event, null);
		touchEvents(event);
		scene.longPress(event);
	}

	/**
	 * Método de tratamento de evento ao segurar e arratar arrastar.
	 */
	@Override
	public boolean onScroll(MotionEvent firstEvent, MotionEvent secondEvent, float distanceX, float distanceY) {
		firstEvent.setAction(MotionEvent.ACTION_MOVE);
		scene.setLastEvents(firstEvent, secondEvent);
		touchEvents(firstEvent);
		return scene.scroll(firstEvent, secondEvent, distanceX, distanceY);
	}

	/**
	 * Método de tratamento de evento onShowPress.
	 */
	@Override
	public void onShowPress(MotionEvent event) {
		scene.setLastEvents(event, null);
		touchEvents(event);
		scene.showPress(event);
	}

	/**
	 * Método de tratamento de evento clique simples.
	 */
	@Override
	public boolean onSingleTapUp(MotionEvent event) {
		boolean result = true;
		if(event.getAction() != lastEvent){
			scene.setLastEvents(event, null);
			touchEvents(event);
			result = scene.singleTapUp(event);
		}
		lastEvent = event.getAction();
		
		return result;
	}

	/**
	 * Método de tratamento de evento ao escalonar.
	 */
	@Override
	public boolean onScale(ScaleGestureDetector detector) {
		return scene.scale(detector);
	}

	/**
	 * Método de tratamento de evento ao o escalonar ser iniciado.
	 */
	@Override
	public boolean onScaleBegin(ScaleGestureDetector detector) {
		return scene.scaleBegin(detector);
	}

	/**
	 * Método de tratamento de evento ao o escalonar ser finalizado.
	 */
	@Override
	public void onScaleEnd(ScaleGestureDetector detector) {
		scene.scaleEnd(detector);
	}
	
	/**
	 * Método que remove um elemento do conjunto de lementos do GameView.
	 */
	public void removeObject(String objectName){
		this.objects.remove(objectName);
	}
	
	/**
	 * Método que adiciona um elemento ao conjunto de lementos do GameView;
	 */
	public void addObject(String objectName, Object object){
		this.objects.put(objectName, object);
	}
	
	/**
	 * Método que remove todos os elementos do GameView.
	 */
	public void removeAllObjects(){
		this.objects.clear();
	}
	
	/**
	 * Retorna um objeto baseando-se numa String de nome.
	 */
	public Object getObject(String objectName){
		return objects.get(objectName);
	}
	
	/**
	 * Retorna a taxa de atualização, ou seja, a velocidade de atualização do jogo.
	 */
	public int getFPS() {
		return FPS;
	}

	/**
	 * Seta a taxa de atualização, ou seja, a velocidade de atualização do jogo.
	 */
	public void setFPS(int fPS) {
		this.FPS = fPS;
	}
	
	/**
	 * Retorna o Simulador de física do Jogo.
	 */
	public PhysicSimulator getPhysicSimulator() {
		return physicSimulator;
	}

	/**
	 * Seta o Simulador de física do Jogo.
	 */
	public void setPhysicSimulator(PhysicSimulator physicSimulator) {
		this.physicSimulator = physicSimulator;
	}

	/**
	 * Retorna GameLoop responsável por rodar o jogo.
	 */
	public GameLoop getGameLoop() {
		return gameLoop;
	}

	/**
	 * Seta o Loope de jogo.
	 */
	public void setGameLoop(GameLoop gameLoop) {
		this.gameLoop = gameLoop;
	}

	/**
	 * Retorna o Detector de gestos.
	 */
	public GestureDetector getGestureDetector() {
		return gestureDetector;
	}

	/**
	 * Seta o Detector de Gestos.
	 */
	public void setGestureDetector(GestureDetector gestureDetector) {
		this.gestureDetector = gestureDetector;
	}

	/**
	 * Retorna o detector de gestos de escala.
	 */
	public ScaleGestureDetector getScaleGestureDetector() {
		return ScaleGestureDetector;
	}

	/**
	 * Seta o detector de gestos de escala.
	 */
	public void setScaleGestureDetector(ScaleGestureDetector scaleGestureDetector) {
		ScaleGestureDetector = scaleGestureDetector;
	}

	/**
	 * Retorna a sena atual.
	 */
	public Scene getScene() {
		return scene;
	}

	/**
	 * Seta a sena atual.
	 */
	public void setScene(Scene scene) {
		this.scene = scene;
	}

	/**
	 * Retorna uma Hash com os elementos adicionados no GameView.
	 */
	public ConcurrentHashMap <String, Object> getObjects() {
		return objects;
	}

	/**
	 * Seta um conjunto de elementos para o GameView.
	 */
	public void setObjects(ConcurrentHashMap <String, Object> objects) {
		this.objects = objects;
	}
}
