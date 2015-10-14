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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.view.MotionEvent;

/**
 * Classe que serve de layout para agrupar um conjunto de elementos. Serve, por exemplo, para criar uma caixa de dialogo.
 * @author Wiliams Magalhães Primo.
 */
public class LayoutView extends GameElement implements DrawableElement, TouchableElement, UpdatableElement{
	
	//Propriedades do LayoutView
	private List<GameElement> elements = new ArrayList<GameElement>();
	private Paint paint = new Paint();
	private Rect rect = new Rect();
	private Rect clipRect = new Rect();
	private Vector2D position = new Vector2D(0.0f, 0.0f);
	private double width = 0.0f,
			       height = 0.0f;
	private int backGroundColor = Color.TRANSPARENT;
	private int strokeColor = Color.TRANSPARENT;
	private double strokeWidth = 1;
	
	private double z = 0;
	private boolean fixed = true,
			        visible = true;
	
	private TouchListener touchListener = null;
	protected GameElement element = null;
	protected boolean elementTouched = false;
	protected Properties elementDrawProperties = new Properties();
	protected Properties elementTouchProperties = new Properties();
	
	/**
	 * Método resposável por desenhar o LayoutView e todos o seus elementos.
	 */
	@Override
	public void draw(Properties properties, Canvas canvas, GameCamera camera) {
		if(visible && width > 0 && height > 0){
			calcRect(properties);
			
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(backGroundColor);
			paint.setStrokeWidth(0.0f);
			canvas.drawRect(rect, paint);
			
			canvas.getClipBounds(clipRect);
			canvas.clipRect(rect);
			
			for(int i = 0; i < elements.size(); i++){
				element = elements.get(i);
				if(!element.isDeleted() && element instanceof DrawableElement){
					elementDrawProperties.set(element);
					camera.changePropertiesToScreem(elementDrawProperties);
					
					elementDrawProperties.getPosition().addTo(properties.getPosition().getX() - properties.getWidth()/2.0f,
												   properties.getPosition().getY() - properties.getHeight()/2.0f);
		
					((DrawableElement)element).draw(elementDrawProperties, canvas, camera);
				}
			}
			
			canvas.clipRect(clipRect, Region.Op.UNION);
			
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth((float)(strokeWidth*camera.zoomScale()));
			paint.setColor(strokeColor);
			canvas.drawRect(rect, paint);
		}
	}
	
	/**
	 * Verifica se algum dos objetos do LayoutView foi tocado.
	 */
	@Override
	public boolean touchEvents(Properties properties, MotionEvent event, GameCamera camera) {
		
		for(int i = elements.size() - 1; i >= 0; i--){
			element = elements.get(i);
			if(!element.isDeleted() && element instanceof TouchableElement){
				elementTouchProperties.set(element);
				camera.changePropertiesToScreem(elementTouchProperties);
				
				elementTouchProperties.getPosition().addTo(properties.getPosition().getX() - properties.getWidth()/2.0f,
											   properties.getPosition().getY() - properties.getHeight()/2.0f);
	
				elementTouched = TouchVerifier.touched(event, elementTouchProperties);
				
				if(elementTouched){
					if(((TouchableElement)element).touchEvents(elementTouchProperties, event, camera)){
						break;
					}
				}
			}
		}
		if(touchListener != null){
			touchListener.onTouch(this, event, camera);
		}
		onTouchEvent(event, camera);
		return true;
	}
	
	/**
	 * Método de atualização de todos os elementos do layoutView.
	 */
	@Override
	public  void update() {
		
		for(int i = 0; i < elements.size(); i++){
			element = elements.get(i);
			if(!element.isDeleted() && element instanceof UpdatableElement){
				((UpdatableElement)element).update();
			}
		}
	}
	
	/**
	 * Método que deve ser sobreescrito no caso de ousuário desejar tratar a atualização do LayoutView.
	 */
	public void onUpdate(){}
	
	/**
	 * Método de que calcula o retangulo de desenho do LayoutView.
	 */
	protected  void calcRect(Properties properties){
		rect.left = (int)(properties.getPosition().getX() - properties.getWidth()/2);
		rect.top = (int)(properties.getPosition().getY() - properties.getHeight()/2);
		rect.right = (int)(rect.left + properties.getWidth());
		rect.bottom = (int)(rect.top + properties.getHeight());
	}
	
	/**
	 * Ordena os elementos interno com base em na coordenada z de cada elemento.
	 */
	public  void sortElementsByZ(){
		int size = elements.size();
		int j = 0;
		for(int i = 1; i < size; i++){
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
	 * Método que deve ser sobreescrito no caso de ousuário desejar tratar os eventos de toque do LayoutView.
	 */
	protected void onTouchEvent(MotionEvent event, GameCamera camera){}
	
	/**
	 * Retorna o Z do Elemento, ou seja, o quão profundo em relação aos outros, o elemento se encontra.
	 */
	@Override
	public  double getZ() {
		return z;
	}

	/**
	 * Seta o Z do Elemento, ou seja, o quão profundo em relação aos outros, o elemento se encontra.
	 */
	public  void setZ(double z) {
		this.z = z;
	}

	/**
	 * Retorna uma lista com todos os elementos internos do LayoutView.
	 */
	public  List<GameElement> getElements() {
		return elements;
	}

	/**
	 * Seta o conjunto de elementos internos do LayoutView.
	 */
	public  void setElements(List<GameElement> elements) {
		this.elements.clear();
		for(GameElement element:elements){
			addElement(element);
		}
	}
	
	/**
	 * Adiciona um novo elemento ao LayoutView.
	 */
	public  void addElement(GameElement element){
		element.setFixed(true);
		element.setScene(getScene());
		this.elements.add(element);
	}

	/**
	 * Retorna o objeto Paint do LayoutView.
	 */
	public  Paint getPaint() {
		return paint;
	}

	/**
	 * Seta o objeto Paint do LayoutView.
	 */
	public  void setPaint(Paint paint) {
		this.paint = paint;
	}

	/**
	 * Retorna o Rect, retangulo de desenho, do LayoutView.
	 */
	public  Rect getRect() {
		return rect;
	}

	/**
	 * Seta o Rect, retangulo de desenho, do LayoutView
	 */
	public  void setRect(Rect rect) {
		this.rect = rect;
	}

	/**
	 * Retorna cor de fundo do LayoutView.
	 */
	public  int getBackGroundColor() {
		return backGroundColor;
	}

	/**
	 * Seta cor de fundo do LayoutView.
	 */
	public  void setBackGroundColor(int backGroundColor) {
		this.backGroundColor = backGroundColor;
	}

	/**
	 * Seta a larguea do LayoutView.
	 */
	public  void setWidth(double width) {
		this.width = width;
	}

	/**
	 * Retorna a largura do LayoutView.
	 */
	@Override
	public  double getWidth() {
		return this.width;
	}

	/**
	 * Retorna a altura do LayoutView.
	 */
	public  void setHeight(double height) {
		this.height = height;
	}

	/**
	 * Seta a altura do LayoutView.
	 */
	@Override
	public  double getHeight() {
		return this.height;
	}
	
	/**
	 * Seta se o LayoutView tem posição fixa na tela.
	 */
	public  void setFixed(boolean fixed) {
		this.fixed = fixed;
	}

	/**
	 * Retorna se o LayoutView tem posição fixa na tela.
	 */
	@Override
	public  boolean isFixed() {
		return fixed;
	}

	/**
	 * Set como visível ou não o laoutView.
	 */
	public  void setVisible(boolean visible) {
		this.visible = visible;		
	}

	/**
	 * Retorna se o LayoutView está visível.
	 */
	@Override
	public  boolean isVisible() {
		return this.visible;
	}

	/**
	 * Não permite rotação.
	 */
	public  double getRotation() {
		return 0.0f;
	}
	
	/**
	 * Retorna a posição do layoutView.
	 */
	@Override
	public  Vector2D getPosition() {
		return this.position;
	}

	/**
	 * Seta a posição do layoutView.
	 */
	@Override
	public  void setPosition(Vector2D position) {
		this.position.set(position);
	}

	/**
	 * Não é possível rotacionar um elemento do tipo LayoutView.
	 */
	@Override
	public void setRotation(double rotation) {}

	/**
	 * Seta o tamanho do layoutView.
	 */
	@Override
	public void setSize(double width, double height) {
		this.setWidth(width);
		this.setHeight(height);
	}

	/**
	 * Seta a posição do layoutView.
	 */
	@Override
	public void setPosition(double x, double y) {
		this.position.set(x, y);
	}
	
	/**
	 * Adiciona à posição do elemento.
	 */
	public  void addToPosition(Vector2D velocity){
		this.position.addTo(velocity);
	}
	
	/**
	 * Adiciona à posição do elemento.
	 */
	public  void addToPosition(double x, double y){
		this.position.addTo(x,y);
	}

	/**
	 * Retorna a cor do contorno do layoutView.
	 */
	public int getStrokeColor() {
		return strokeColor;
	}

	/**
	 * Seta a cor do contorno do layoutView.
	 */
	public void setStrokeColor(int strokeColor) {
		this.strokeColor = strokeColor;
	}

	/**
	 * Retorna a espessura do contorno do layoutView.
	 */
	public double getStrokeWidth() {
		return strokeWidth;
	}

	/**
	 * Seta a espessura do contorno do layoutView.
	 */
	public void setStrokeWidth(double strokeWidth) {
		this.strokeWidth = strokeWidth;
	}

	/**
	 * Retorna o TtouchListenaer do layoutView.
	 */
	@Override
	public void setTouchListener(TouchListener touchListener) {
		this.touchListener = touchListener;
	}

	/**
	 * Seta o TtouchListenaer do layoutView.
	 */
	@Override
	public TouchListener getTouchListener() {
		return touchListener;
	}

	/**
	 * Retorna a posição do elemento com base no ponto superior esquerdo.
	 */
	@Override
	public Vector2D getBasePosition() {
		return new Vector2D(position.getX() - width/2, position.getY() - width/2);
	}

	/**
	 * Seta a posição do elemento com base no ponto superior esquerdo.
	 */
	@Override
	public void setBasePosition(double x, double y) {
		this.position.set(x + width/2, y + height/2);
	}

	/**
	 * Seta a posição do elemento com base no ponto superior esquerdo.
	 */
	@Override
	public void setBasePosition(Vector2D position) {
		this.position.set(position.getX() + width/2, position.getY() + height/2);
	}
	
	/**
	 * Seta a cena atual.
	 */
	@Override
	public void setScene(Scene scene) {
		for(GameElement element:elements){
			element.setScene(scene);
		}
		super.setScene(scene);
	}
}
