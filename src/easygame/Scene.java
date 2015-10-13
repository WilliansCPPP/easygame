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

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

/**
 * Essa classe é a base para a criação de Cenas ou fases do jogo.
 * É na cena que todos os elementos devem ser adicionados para que apareçam na tela
 * sejam simulados fisicamente escutem eventos de toque.
 * @author Willians Magalhães Primo
 */

@SuppressLint("WrongCall") 
public abstract class Scene{
	//Variáveis auxiliares
	protected boolean elementTouched = false;
	protected Properties drawProperties = new Properties();
	protected Properties touchProperties = new Properties();
	
	//Propriedades da cena
	private List <GameElement> elements = new ArrayList<GameElement>();
	private List <GameElement> physicElements = new ArrayList<GameElement>();
	private GameView gameView = null;
	private GameCamera camera = new GameCamera();
	private int backGroundColor = Color.TRANSPARENT;
	private MotionEvent lastEvent1 = null;
	private MotionEvent lastEvent2 = null;
	
	/**
	 * Função de carregamento da Cena.
	 */
	public void load(){
		onLoad();
	}
	
	/**
	 * Método que deve ser implementado no momento da definição de uma nova cena
	 * É neste método que deve ser feita toda inicialização da cena: carregar imagens, carrgar sons, 
	 * adicionar elementos de cenário todas as outras atividades de inicialização.
	 * 
	 */
	
	protected abstract void onLoad();
	
	/**
	 * Carrega uma cena no  GameView relacionado.
	 */
	public void loadScene(Scene scene){
		unload();
		gameView.loadScene(scene);
	}
	
	/**
	 * Desaloca todos os recursos da cena.
	 */
	public void unload(){
		//Remove todos os elementos da Cena
		elements.clear();
		physicElements.clear();
		removeAllImages();
		removeAllSounds();
		Runtime.getRuntime().gc();
	}
	
	/**
	 * Finaliza o jogo.
	 */
	public void finish(){
		((Activity)getContext()).finish();
	}
	
	/**
	 * Método de atualização da Cena, chamado a cada tempo de atualização.
	 */
	public void update(){
		removeDeletedElements();
		
		sortElementsByZ();
		
		getPhysicSimulator().simulate();
		for(GameElement element: elements){
			if(!element.isDeleted() && element instanceof UpdatableElement){
				((UpdatableElement)element).update();
			}
		}
		
		onUpdate();
	}
	
	/**
	 * Método de desenho da cena, chamado a cada tempo de atualização.
	 */
	public void draw(Canvas canvas){
		if(canvas != null){
			camera.update();
			
			canvas.drawColor(getBackGroundColor());
			GameElement element = null;
			for(int i = 0; i < elements.size(); i++){
				element = elements.get(i);
				if(!element.isDeleted() && element instanceof DrawableElement){
					drawProperties.set(element);
					getCamera().changePropertiesToScreem(drawProperties);
					((DrawableElement)element).draw(drawProperties, canvas, camera);
				}
			}
			onDraw(canvas, camera);
		}
	}
	
	/**
	 * Método que deve ser sobreescrito caso o usuário desejar tratar as atualizações da cena.
	 */
	protected void onUpdate(){}
	
	/**
	 * Método que deve ser sobreescrito caso o usuário desejar tratar o desenhar da cena.
	 */
	protected void onDraw(Canvas canvas, GameCamera camera){}
	
	/**
	 * Método de tratamento de eventos de toques.
	 */
	public void touchEvents(MotionEvent event){
		GameElement element = null;
		for(int i = elements.size() - 1; i >= 0; i--){
			element = elements.get(i);
			if(!element.isDeleted() && element instanceof TouchableElement){
				touchProperties.set(element);
				getCamera().changePropertiesToScreem(touchProperties);
				elementTouched = TouchVerifier.touched(event, touchProperties);
				
				if(elementTouched){
					if(((TouchableElement)element).touchEvents(touchProperties, event, camera)){
						break;
					}
				}
			}
		}
		
		onTouchEvent(event, camera);
	}
	
	/**
	 * Método de remoção de objetos deletados.
	 */
	public void removeDeletedElements(){
		GameElement element = null;
		for(int i = 0; i < elements.size(); i++){
			element = elements.get(i);
			if(element.isDeleted()){
				elements.remove(element);
			}
		}
		
		element = null;
		for(int i = 0; i < physicElements.size(); i++){
			element = physicElements.get(i);
			if(element.isDeleted()){
				physicElements.remove(element);
			}
		}
	}
	
	
	/**
	 * Adiciona um novo elemento na cena.
	 */
	public void addElement(GameElement element){
		element.setScene(this);
		elements.add(element);
		
		if(element instanceof PhysicElement){
			this.physicElements.add(element);
		}
	}
	
	/**
	 * Adiciona uma lista de elementos na cena.
	 */
	public void addElements(List<GameElement> elements){
		GameElement element = null;
		for(int i = 0; i < elements.size(); i++){
			element = elements.get(i);
			element.setScene(this);
			
			if(element instanceof PhysicElement){
				physicElements.add(element);
			}
			addElement(element);
		}
	}
	
	/**
	 * Ordena todos os objetos manipulaveis pelo com base na propriedade Z.
	 */
	public void sortElementsByZ(){
		GameElement element = null;
		int j = 0;
		for(int i = 1; i < elements.size(); i++){
			j = i - 1;
			element = elements.get(i);
			while(j >= 0 && element.getZ() < elements.get(j).getZ()){
				elements.set(j + 1, elements.get(j));
				j--;
			}
			elements.set(j + 1, element);			
		}
	}
	
	/**
	 * Método que deve ser sobreescrito caso o usuário desejar tratar os eventos de toque.
	 */
	protected void onTouchEvent(MotionEvent event, GameCamera camera){}
	
	/**
	 * Método chamdo quando o evento down ocorrer.
	 */
	public boolean down(MotionEvent event) {
		return onDown(event);
	}

	/**
	 * Método chamdo quando o evento fling ocorrer.
	 */
	public boolean fling(MotionEvent firstEvent, MotionEvent secondEvent, float velocityX, float velocityY) {
		if(camera.isAutoZoomable()){
			getCamera().scroll(secondEvent.getX() - firstEvent.getX(), secondEvent.getY() - firstEvent.getY());
		}
		return onFling(firstEvent, secondEvent, velocityX, velocityY);
	}

	/**
	 * Método chamdo quando o evento longPress ocorrer .
	 */
	public void longPress(MotionEvent event) {
		onLongPress(event);
	}

	/**
	 * Método chamdo quando o evento scroll ocorrer. 
	 */
	public boolean scroll(MotionEvent firstEvent, MotionEvent secondEvent, float distanceX, float distanceY) {
		return onScroll(firstEvent, secondEvent, distanceX, distanceY);
	}

	/**
	 * Método chamdo quando o evento showPress ocorrer. 
	 */
	public void showPress(MotionEvent event) {
		onShowPress(event);
	}

	/**
	 * Método chamdo quando o evento sigleTapUp ocorrer. 
	 */
	public boolean singleTapUp(MotionEvent event) {
		return onSingleTapUp(event);
	}

	
	/**
	 *  Método chamdo quando o evento scale ocorrer .
	 */
	public boolean scale(ScaleGestureDetector detector) {
		getCamera().onZoomScale(detector.getScaleFactor());
		return onScale(detector);
	}

	/**
	 * Método chamdo quando o evento scaleBegin ocorrer.
	 */
	public boolean scaleBegin(ScaleGestureDetector detector) {
		return onScaleBegin(detector);
	}

	/**
	 * Método chamdo quando o evento scaleEnd ocorrer. 
	 */
	public void scaleEnd(ScaleGestureDetector detector) {
		onScaleEnd(detector);
	}

	/**
	 * Método que deve ser sobreescrito caso o usuário desejar tratar o evento ao Tocar na tela.
	 */
	public boolean onDown(MotionEvent event) {return true;}

	/**
	 * Método que deve ser sobreescrito caso o usuário desejar tratar o evento ao arrastar na tela.
	 */
	public boolean onFling(MotionEvent firstEvent, MotionEvent secondEvent, float velocityX, float velocityY) {return true;}

	/**
	 * Método que deve ser sobreescrito caso o usuário desejar tratar o evento de pressionar a tela por um longo periodo de tempo.
	 */
	public void onLongPress(MotionEvent event) {}

	/**
	 * Método que deve ser sobreescrito caso o usuário desejar tratar o evento clicar e arrastar.
	 */
	public boolean onScroll(MotionEvent firstEvent, MotionEvent secondEvent, float distanceX, float distanceY) {return true;}

	/**
	 * Método que deve ser sobreescrito caso o usuário desejar tratar o evento onShowPress.
	 */
	public void onShowPress(MotionEvent event) {}

	/**
	 * Método que deve ser sobreescrito caso o usuário desejar tratar o evento ao tirar o toque da tela.
	 */
	public boolean onSingleTapUp(MotionEvent event) {return true;}

	
	/**
	 *  Método que deve ser sobreescrito caso o usuário desejar tratar o evento ao escalonar.
	 */
	public boolean onScale(ScaleGestureDetector detector) {return true;}

	/**
	 * Método que deve ser sobreescrito caso o usuário desejar tratar o evento ao iniciar escalonar.
	 */
	public boolean onScaleBegin(ScaleGestureDetector detector) {return true;}

	/**
	 * Método que deve ser sobreescrito caso o usuário desejar tratar o evento ao finalizar escalonar.
	 */
	public void onScaleEnd(ScaleGestureDetector detector) {}
	
	
	/**
	 * Carrega um som.
	 */
	public void loadSound(String soundName, int soundId){
		SoundBuffer.addSound(soundName, soundId, getContext());
	}
	
	/**
	 * Executa um som.
	 */
	public void removeSound(String soundName){
		SoundBuffer.removeSound(soundName);
	}
	
	/**
	 * Executa um som.
	 */
	public void playSound(String soundName){
		SoundBuffer.playSound(soundName, getContext());
	}
	
	/**
	 * Executa um som.
	 */
	public void playSound(String soundName, int loop){
		SoundBuffer.playSound(soundName,loop, getContext());
	}
	
	/**
	 * Executa um som.
	 */
	public void playSound(String soundName, float volume, float velocity, int loop){
		SoundBuffer.playSound(soundName, volume, velocity, loop, getContext());
	}
	
	public void removeAllSounds(){
		SoundBuffer.removeAll();
	}
	
	/**
	 * Carrega uma imagem.
	 */
	public void loadImage(String imageName, Bitmap image){
		ImageBuffer.addImage(imageName, image);
	}
	
	/**
	 * Carrega uma imagem.
	 */
	public void loadImage(String imageName, int drawableId){
		ImageBuffer.addImage(imageName, BitmapFactory.decodeResource(getContext().getResources(), drawableId));
	}
	
	/**
	 * Remove uma imagem.
	 */
	public void removeImage(String imageName){
		ImageBuffer.removeImage(imageName);
	}
	
	/**
	 * Remove todas as imagens.
	 */
	public void removeAllImages(){
		ImageBuffer.removeAllImages();
	}

	/**
	 * Retorna uma lista de todos os elemtos do Tipo PhysicElements.
	 */
	public List <GameElement> getPhysicElements() {
		return physicElements;
	}
	
	/**
	 * Seta o elemento a ser seguido pela camera.
	 */
	public void setCameraElement(GameElement element){
		getCamera().setElement(element);
	}
	
	/**
	 * Retorna o seguido pela camera.
	 */
	public GameElement getCameraElement(){
		return getCamera().getElement();
	}
	
	/**
	 * Seta a gravidade.
	 */
	public void setGravity(Vector2D gravity){
		getPhysicSimulator().setGravity(gravity);
	}
	
	/**
	 * Seta a gravidade.
	 */
	public void setGravity(double x, double y){
		getPhysicSimulator().setGravity(x, y);
	}
	
	/**
	 * Retorna a gravidade.
	 */
	public Vector2D getGravity(){
		return getPhysicSimulator().getGravity();
	}
	
	/**
	 * Retorna o simulador para que o usuário possa alterar algumas propriedades.
	 */
	public PhysicSimulator getPhysicSimulator(){
		return gameView.getPhysicSimulator();
	}
	
	/**
	 * Seta uma escala de zoom.
	 */
	public void setZoom(double zoom){
		getCamera().setZoom(zoom);
	}
	
	/**
	 * Retorna a atual escala de zoom.
	 */
	public double getZoom(){
		return getCamera().getZoom();
	}
	
	/**
	 * Aplica um scrool, rolagem, na tela.
	 */
	public void scrollSene(Vector2D scroll){
		getCamera().scroll(scroll);
	}
	
	/**
	 * Aplica um scrool, rolagem, na tela.
	 */
	public void scrollSene(double x, double y){
		getCamera().scroll(x, y);
	}
	
	/**
	 * Seta se o zoom será aplicado de forma altomática a cena.
	 */
	public void setAutoZoomable(boolean autoZoomable){
		getCamera().setAutoZoomable(autoZoomable);
	}
	
	/**
	 * Retorna uma lista com todos os elementos da cena.
	 */
	public List<GameElement> getElements(){
		return elements;
	}

	/**
	 * Retorna o GameView no qual a cena está sendo execultada.
	 */
	public GameView getGameView() {
		return gameView;
	}

	/**
	 * Seta o GameView no qual a cena está sendo execultada.
	 */
	public void setGameView(GameView gameView) {
		this.gameView = gameView;
	}

	/**
	 * Retorna a Camera atual.
	 */
	public GameCamera getCamera() {
		return camera;
	}

	/**
	 * Retorna o Context da Aplicação.
	 */
	public Context getContext() {
		return getGameView().getContext();
	}

	/**
	 * Retorna a cor de fundo da cena.
	 */
	public int getBackGroundColor() {
		return backGroundColor;
	}

	/**
	 * Seta a cor de fundo da cena.
	 */
	public void setBackGroundColor(int backGroundColor) {
		this.backGroundColor = backGroundColor;
	}
	
	/**
	 * Retorna a largura da cena nas dimensões do Jogo.
	 */
	public double getWidth() {
		return getCamera().toRealWidth(gameView.getWidth());
	}

	/**
	 * Retorna a altura da cena nas dimensões do Jogo.
	 */
	public double getHeight() {
		return getCamera().toRealHeight(gameView.getHeight());
	}
	
	/**
	 * Retorna a largura da tela.
	 */
	public double getScreamWidth() {
		return gameView.getWidth();
	}

	/**
	 * Retorna a altura da tela.
	 */
	public double getScreamHeight() {
		return gameView.getHeight();
	}
	
	/**
	 * Retorna uma imagem baseando-se em uma String de nome.
	 */
	public Bitmap getImage(String imageName){
		return ImageBuffer.getImage(imageName);
	}
	
	/**
	 * Cria uma imagem baseando se no ID de recurso.
	 */
	public Bitmap getImageResource(int drawableId){
		return BitmapFactory.decodeResource(getContext().getResources(), drawableId);
	}
	
	/**
	 * Converte uma posição da tela para posição na dimensão do jogo, ou seja, aquela determinada pela altura base (baseHeight).
	 */
	public void toRealPosition(Vector2D position){
		camera.toRealPosition(position);
	}
	
	/**
	 * Converte uma posição da dimensão do jogo para a tela.
	 */
	public void toScreemPosition(Vector2D position){
		camera.toScreemPosition(position);
	}
	
	/**
	 * Converte uma largura da tela para a dimensão do jogo.
	 */
	public double toRealHeight(double height){
		return camera.toRealHeight(height);
	}
	
	/**
	 * Converte uma altura da dimensão do jogo para a tela.
	 */
	public double toScreemHeight(double height){
		return camera.toScreemHeight(height);
	}
	
	/**
	 * Converte uma altura da tela para a dimensão do jogo.
	 */
	public double toRealWidth(double width){
		return camera.toRealWidth(width);
	}
	
	/**
	 * Converte uma largura da dimensão do jogo para a tela.
	 */
	public double toScreemWidth(double width){
		return camera.toScreemWidth(width);
	}

	/**
	 * Retorna o z do maior elemento da cena.
	 * O(n)
	 */
	public double greaterZ() {
		if(elements.size() == 0){
			return 0;
		}
		sortElementsByZ();
		return elements.get(elements.size() - 1).getZ();
	}
	
	/**
	 * Retorna um objeto que permite salvar dados.
	 */
	public SharedPreferences.Editor getPreferencesEditor(){
		return ((Activity)getContext()).getSharedPreferences("preferences", Context.MODE_PRIVATE).edit();
	}
	
	/**
	 * Retorna um objeto que permite recuperar.
	 */
	public SharedPreferences getPreferences(){
		return ((Activity)getContext()).getSharedPreferences("preferences", Context.MODE_PRIVATE);
	}
	
	/**
	 * Retorna a Activity Atual.
	 */
	public Activity getActivity(){
		return ((Activity)getContext());
	}
	
	/**
	 * Converte as coordenadas de um evento para um vetor.
	 */
	public Vector2D eventToPosition(MotionEvent event){
		Vector2D position = new Vector2D(event.getX(), event.getY());
		toRealPosition(position);
		return position;
	}
	
	/**
	 * Converte as coordenadas de um evento para um vetor.
	 */
	public void eventToPosition(MotionEvent event, Vector2D position){
		position.set(event.getX(), event.getY());
		toRealPosition(position);
	}
	
	/**
	 * Retorna a posição do centro da tela.
	 */
	public Vector2D getCenterPosition(){
		return new Vector2D(getWidth()/2, getHeight()/2);
	}

	//Retorna o primeiro ultimo evento de toque.
	public MotionEvent getLastEvent1() {
		return lastEvent1;
	}

	//Seta o primeiro ultimo evento de toque.
	public void setLastEvent1(MotionEvent lastEvent1) {
		this.lastEvent1 = lastEvent1;
	}

	//Retorna o segundo ultimo evento de toque.
	public MotionEvent getLastEvent2() {
		return lastEvent2;
	}

	//Seta o segunto ultimo evento de toque.
	public void setLastEvent2(MotionEvent lastEvent2) {
		this.lastEvent2 = lastEvent2;
	}
	
	//Seta os dois ultimos movimentos de toque.
	public void setLastEvents(MotionEvent first, MotionEvent second){
		lastEvent1 = first;
		lastEvent2 = second;
	}
}
