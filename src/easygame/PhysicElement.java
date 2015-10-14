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

import easygame.PhysicSimulator.Shape;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Essa classe serve como base para a criação de elementos físicos do jogo.
 * Entidades físicas podem sofrer ação da gravidade e receber tratamento de colisão.
 * @author Wiliams Magalhães Primo
 */
@SuppressLint("WrongCall") 
public class PhysicElement extends GameElement implements DrawableElement, TouchableElement, UpdatableElement{
	//propriedades privadas de cada instancia
	private double z = 0;
	private double width = 0.0f,
			       height = 0.0f,
			       rotation = 0.0f,
			       weight = 0.0f,
			       densit = 1.0f,
			       radio = 0.0f,
			       elasticity = 0.0f,
			       friction = 0.0f,
			       rotationVelocity = 0.0f;
	
	private Vector2D velocity = new Vector2D(0.0f, 0.0f),
			         aceleration = new Vector2D(0.0f, 0.0f),
					 position = new Vector2D(0.0f, 0.0f);
	
	private PhysicSimulator.Shape shape = PhysicSimulator.Shape.RECTANGLE;
	private PhysicElementSprite sprite = null;
	private boolean colidable = true,
			        estatic = false,
			        rigidBody = true,
			        fixed = false,
			        imageTouchable = true;
	
	private TouchListener touchListener = null;
	
	//Construtores
	public PhysicElement() {}
	
	public PhysicElement(String imageName){
		setSprite(imageName);
	}
	
	public PhysicElement(String imageName, int rows, int columns){
		setSprite(imageName, rows, columns);
	}
	
	/**
	 * Retorna a sprite, imagem de representação, para  elemento.
	 */
	public  PhysicElementSprite getSprite() {
		return sprite;
	}

	/**
	 * Seta a sprite, imagem de representação, para o elemento.
	 */
	public  void setSprite(PhysicElementSprite sprite) {
		this.sprite = sprite;
		if(sprite != null){
			this.sprite.setElement(this);
		}
	}
	
	/**
	 * Seta a sprite, imagem de representação, para o elemento.
	 */
	public  void setSprite(String imageName, int rows, int columns) {
		this.sprite = new PhysicElementSprite(imageName, rows, columns);
		this.sprite.setElement(this);
	}
	
	/**
	 * Seta a sprite, imagem de representação, para o elemento.
	 */
	public  void setSprite(Bitmap bitmap, int rows, int columns) {
		this.sprite = new PhysicElementSprite(bitmap, rows, columns);
		this.sprite.setElement(this);
	}
	
	/**
	 * Seta a sprite, imagem de representação, para o elemento.
	 */
	public  void setSprite(Bitmap bitmap) {
		this.sprite = new PhysicElementSprite(bitmap, 1, 1);
		this.sprite.setElement(this);
	}
	
	public  void setSprite(String imageName) {
		this.sprite = new PhysicElementSprite(imageName, 1, 1);
		this.sprite.setElement(this);
	}

	/**
	 * Retorna a densidade do corpo do elemento.
	 */
	public  double getDensit() {
		return densit;
	}

	/**
	 * Seta a densidade do corpo do elemento.
	 */
	public  void setDensit(double densit) {
		this.densit = densit;
		change();
	}
	
	/**
	 * Adiciona à posiçãoX  do elemento.
	 */
	public  void addToPositionX(double x){
		this.position.addTo(x, 0);
	}
	
	/**
	 * Adiciona à posição Y do elemento.
	 */
	public  void addToPositionY(double y){
		this.position.addTo(0, y);
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
	 * Seta a posição do elemento.
	 */
	public  void setPosition(double x, double y){
		this.position.set(x, y);
	}
	
	/**
	 * Retorna a posição do elemento.
	 */
	@Override
	public  Vector2D getPosition(){
		return this.position;
	}
	
	/**
	 * Seta a posição do elemento.
	 */
	public  void setPosition(Vector2D position){
		this.position = position;
	}
	
	/**
	 * Muda algumas propriedades do elemento com base na mudança de outras
	 */
	private  void change(){		
		if(shape == Shape.ELIPSE){
			setRadio(Math.max(width, height)/2.0f);
			setWeight((int)((Math.pow(radio,2)*Math.PI)*getDensit()));
		}else if(getShape() == Shape.RECTANGLE){
			setRadio(Math.sqrt((width/2.0f)*(width/2.0f) + (height/2.0f)*(height/2.0f)));
			setWeight((int)(width*height*getDensit()));
		}
	}

	/**
	 * Retorna se o elemento é vsível ou não.
	 */
	public  boolean isVisible() {
		if(sprite == null){
			return false;
		}
		return this.sprite.isVisible();
	}

	/**
	 * Seta se o elemento é vsível ou não.
	 */
	public  void setVisible(boolean visible) {
		if(sprite != null){
			this.sprite.setVisible(visible);
		}
	}

	/**
	 * Retorna de o elemento é colidível.
	 */
	public  boolean isColidable() {
		return colidable;
	}

	/**
	 * Seta de o elemento é colidível.
	 */
	public  void setColidable(boolean colidable) {
		this.colidable = colidable;
	}

	/**
	 * Retorna a rotação do elemento em radianos.
	 */
	public  double getRotation() {
		return rotation;
	}
	
	/**
	 * Seta a rotação do elemento em graus celcios.
	 */
	public  void setRotationInDegree(double rotation) {
		this.setRotationInRadians(Math.toRadians(rotation));
	}
	
	/**
	 * Seta a rotação do elemento em radianos.
	 */
	public  void setRotationInRadians(double rotation) {
		this.rotation = rotation;
		if(this.rotation > 2*Math.PI){
			this.rotation = 2*Math.PI - this.rotation;
		}else if(this.rotation < -2*Math.PI){
			this.rotation = this.rotation + 2*Math.PI;
		}
	}

	/**
	 * Retorna o peso do elemento.
	 */
	public  double getWeight() {
		return weight;
	}

	/**
	 * Seta o peso do elemento.
	 * @param weight
	 */
	public  void setWeight(double weight) {
		this.weight = weight;
	}

	/**
	 * Retorna a largura do elemento.
	 */
	public  double getWidth() {
		return width;
	}
	
	/**
	 * Seta a largura do elemento.
	 */
	public  void setWidth(double width){
		this.width = width;
		change();
	}
	
	/**
	 * Retorna a altura do elemento.
	 */
	public  double getHeight() {
		return height;
	}

	/**
	 * Seta a altura do elemento.
	 */
	public  void setHeight(double height) {
		this.height = height;
		change();
	}
	
	/**
	 * Método de tratamento de colisão do elemento.
	 */
	public  void colide(PhysicElement other, Vector2D colisionPoint){
		this.onColide(other, colisionPoint);
	}
	
	/**
	 * Método que deve ser sobreescrito caso o usuári desejar tratar as colisões do elemento.
	 */
	protected void onColide(PhysicElement other, Vector2D colisionPoint){}
	
	/**
	 * Método de desenho do elemento.
	 */
	@Override
	public  void draw(Properties properties, Canvas canvas, GameCamera camera){
		if(this.sprite != null){
			this.sprite.draw(properties, canvas, camera);
		}
		onDraw(canvas, camera);
	}
	
	/**
	 * Evento que deve ser sobreescrito no caso de o usuário desejar tratar o desenhar do elemento.
	 */
	protected void onDraw(Canvas canvas, GameCamera camera){}
	
	/**
	 * Método de atualização do elemento
	 */
	@Override
	public  void update() {
		onUpdate();
	}
	
	/**
	 * Evento que deve ser sobreescrito para tratar o evento de atualização do elemento.
	 */
	protected void onUpdate(){}
	
	/**
	 * Método de tratamento de eventos de toque.
	 */
	@Override
	public  boolean touchEvents(Properties properties, MotionEvent event, GameCamera camera){
		if(!imageTouchable || sprite == null || sprite.touched(properties, event, camera)){
			onTouchEvent(event, camera);
			if(touchListener != null){
				touchListener.onTouch(this, event, camera);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Evento que deve ser sobreescrito no caso de o usuário desejar tratar os eventos de toques do elemento.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
	 */
	protected  void onTouchEvent(MotionEvent event, GameCamera camera){}
	

	/**
	 * Retorna o formato de colisão para o corpo do elemento.
	 */
	public  PhysicSimulator.Shape getShape() {
		return shape;
	}

	/**
	 * Seta o formato de colisão para o corpo do elemento.
	 */
	public  void setShape(PhysicSimulator.Shape shape) {
		this.shape = shape;
		change();
	}

	/**
	 * Retorna a atual aceleração do elemento.
	 */
	public  Vector2D getVelocity() {
		return velocity;
	}

	/**
	 * Seta a uma velocidade ao elemento.
	 */
	public  void setVelocity(Vector2D velocity) {
		this.velocity.set(velocity);
	}
	
	/**
	 * Seta a uma velocidade o elemento.
	 */
	public  void setVelocity(double x, double y) {
		this.velocity.set(x,y);
	}

	/**
	 * Retorna a acelação do elemento.
	 */
	public Vector2D getAceleration() {
		return aceleration;
	}

	/**
	 * Seta a uma aceleração o elemento.
	 */
	public  void setAceleration(Vector2D aceleration) {
		this.aceleration.set(aceleration);
	}
	
	/**
	 * Seta a Animação para a Sprite do elemento.
	 */
	public  void setAnimation(SpriteAnimation animation){
		if(sprite != null){
			sprite.setAnimation(animation);
		}
	}
	
	/**
	 * Retorna a animação da sprite.
	 */
	public SpriteAnimation getAnimation(){
		if(sprite != null){
			return sprite.getAnimation();
		}
		return null;
	}

	/**
	 * Retorna o rio de colisão do elemento.
	 */
	public  double getRadio() {
		return radio;
	}
	
	/**
	 * Seta o raio de colisão do elemento.
	 */
	protected  void setRadio(double radio){
		this.radio = radio;
	}

	/**
	 * Retorna se o objeto é estático, ou seja, o corpo deve ficar parado na tela.
	 */
	public  boolean isStatic() {
		return estatic;
	}

	/**
	 * Seta o objeto como estático, ou seja, o corpo deve ficar parado na tela.
	 */
	public  void setStatic(boolean estatic) {
		this.estatic = estatic;
	}

	/**
	 * Retorna a elasticidade de colisão do corpo do elemento.
	 */
	public  double getElasticity() {
		return elasticity;
	}

	/**
	 * Seta a elasticidade de colisão do corpo do elemento.
	 */
	public  void setElasticity(double elasticity) {
		this.elasticity = elasticity;
	}

	/**
	 * etorna se o corpo é simulável fisicamente.
	 */
	public  boolean isRigidBody() {
		return rigidBody;
	}

	/**
	 * Seta o corpo como simulável fisicamente.
	 */
	public  void setRigidBody(boolean rigidBody) {
		this.rigidBody = rigidBody;
	}

	/**
	 * Retorna à velocidade do elemento celcios.
	 */
	public  double getRotationVelocity() {
		return rotationVelocity;
	}

	/**
	 * Seta à velocidade do elemento em graus celcios.
	 */
	public  void setRotationVelocityInDegree(double rotationVelocity) {
		this.rotationVelocity = Math.toRadians(rotationVelocity);
	}

	/**
	 * Seta à velocidade do elemento em radianos.
	 */
	public  void setRotationVelocityInRadians(double rotationVelocity) {
		this.rotationVelocity = rotationVelocity;
	}
	
	/**
	 * Adiciona à rotação do elemento.
	 */
	public  void addToRotationInDegree(double rotation){
		this.setRotationInRadians(this.rotation += Math.toRadians(rotation));
	}
	
	/**
	 * Adiciona à velocidade do elemento.
	 */
	public  void addToRotationInRadians(double rotation){
		this.setRotationInRadians(this.rotation + rotation);
	}
	
	/**
	 * Seta a velocidade do elemento.
	 */
	public  void addToVelocity(Vector2D velocity){
		this.velocity.addTo(velocity);
	}
	
	/**
	 * Retorna o atrito da superficie do elemento representado pelo elemento.
	 */
	public  double getFriction() {
		return friction;
	}

	/**
	 * Seta o atrito da superficie do elemento representado pelo elemento.
	 */
	public  void setFriction(double friction) {
		this.friction = friction;
	}

	/**
	 * Retorna o Z do elemento, ou seja, o quão profundo, em relação aos outros, o elemento está.
	 */
	@Override
	public  double getZ() {
		return z;
	}

	/**
	 * Seta o Z do elemento, ou seja, o quão profundo, em relação aos outros, o elemento está.
	 */
	@Override
	public  void setZ(double z) {
		this.z = z;
	}
	
	/**
	 * Seta se o elemento possui posição fixa com relação a tela.
	 */
	public  void setFixed(boolean fixed) {
		this.fixed = fixed;
	}

	/**
	 * Retorna se o elemento possui posição fixa com relação a tela.
	 */
	@Override
	public  boolean isFixed() {
		return this.fixed;
	}
	
	/**
	 * Seta o tamanho do elemento, Altura e largura.
	 * OBS: 0 para altura ou largura indica que essa será calculada proporcionalmente.
	 */
	public  void setSize(double width, double height){
		if(sprite != null){
			sprite.setSize(width, height);
		}else{
			this.setWidth(width);
			this.setHeight(height);
		}
	}
	
	/**
	 * Seta a margem interna da sprite relacionada com o elemento.
	 */
	public  void setPadding(int padding){
		if(sprite != null){
			this.sprite.setPadding(padding);
		}
	}
	
	/**
	 * Seta a margem interna da sprite relacionada com o elemento.
	 */
	public  void setPadding(int paddingLeft, int paddingRight, int paddingTop, int paddingBottom){
		if(sprite != null){
			this.sprite.setPadding(paddingLeft, paddingRight, paddingTop, paddingBottom);
		}
	}
	
	/**
	 * Retorna o TouchListener do elemento.
	 * @return
	 */
	public TouchListener getTouchListener() {
		return touchListener;
	}

	/**
	 * Seta o TouchListener para o elemento.
	 */
	public void setTouchListener(TouchListener touchListener) {
		this.touchListener = touchListener;
	}

	/**
	 * Retorna se a verificação de toque do elemento é feito com base em sua sprite.
	 */
	public boolean isImageTouchable() {
		return imageTouchable;
	}

	/**
	 * Seta se a verificação de toque do elemento é feito com base em sua sprite.
	 */
	public void setImageTouchable(boolean imageTouchable) {
		this.imageTouchable = imageTouchable;
	}

	/**
	 * Seta a rotação do elemento em radianso.
	 */
	@Override
	public void setRotation(double rotation) {
		this.setRotationInRadians(rotation);
	}
	
	/**
	 * Retorna a posição do elemento com base no ponto superior esquerdo.
	 */
	@Override
	public Vector2D getBasePosition() {
		return new Vector2D(position.getX() - width/2, position.getY() - height/2);
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
}
