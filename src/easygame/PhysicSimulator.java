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
import java.util.HashMap;

/**
 * Essa classe tem como função detectar e tratar colisões entre elmentos do jogo e também simular reações físicas como gravidade
 * e velocidade.
 * @author Wiliams Magalhães Primo
 */

public class PhysicSimulator {
	//Variáveis globais e estáticas para serem usadas na simulação com a finalidade de não realocar a cada interação:
	protected Vector2D 	 colisionPoint = null,
						 onePosition = null,
						 otherPosition = null,
						 oneVelocity = null,
						 otherVelocity = null,
						 normal = null,
						 intersection = null,
						 maximumIntersection = null,
						 onePoint = null,
						 otherPoint = null;
	
	protected double   angle = 0.0f,
					   normalAngle = 0.0f,
					   normalSignalX = 0.0f,
					   normalSignalY = 0.0f,
					   timeX = 0.0f,
					   timeY = 0.0f,
					   velocityX = 0.0f,
					   velocityY = 0.0f,
					   velocity = 0.0f,
					   weight = 0.0f,
					   friction = 0.0f,
					   elasticity = 0.0f,
					   separationX,
					   separationY;
			
	protected boolean colided = false;
	protected GameElement oneElement = null,
			              otherElement = null,
			              element = null;
	
	protected ColisionDetector colisionDetector = null;
	
	/**
	 * Tipos de formas para tratamento de colisão.
	 * @author Wiliams Magalhães Primo
	 *
	 */
	public enum Shape{ELIPSE, RECTANGLE}
	
	/**
	 * Vetor com ordem de acesso as cordenados de um retângulo.
	 */
	public int[][] rectanglePointAcess = {{-1,-1}, {-1,1}, {1,1}, {1,-1}};
	
	/**
	 * Valor x tendendo a zero.
	 */
	public static double EPSILON = 0.0001f;
	public static double separation = 0.0001f;
	
	/**
	 * Interface que determina a função de detecção de colisões.
	 * @author Wiliams Magalhães Primo
	 *
	 */
	private interface ColisionDetector {
		public boolean detectColision(PhysicElement one, PhysicElement other);
	}
	
	/**
	 * Hash com as funções com tratamento de colisões.
	 */
	private HashMap<String , ColisionDetector> colisionDetectors = new HashMap<String, ColisionDetector>();
	
	//Sena na qual o simulador foi instnciado
	private Scene scene = null;
	
	//Gravidade
	private Vector2D gravity = new Vector2D(0.0f, 0.0f);
	
	//Construtores
	public PhysicSimulator(Scene scene){
		this.scene = scene;
		Initialize();
	}
	/**
	 * Função responsável por chamar as funções de tratamento de colisões e funções de atualização.
	 */
	public void simulate(){
		
		sortElementsByPosition();
		
		if(this.scene == null){
			return;
		}
		
		for(int i = 0; i < scene.getPhysicElements().size(); i++){
			update((PhysicElement)scene.getPhysicElements().get(i));
		}
		
		for(int i = 0; i < scene.getPhysicElements().size(); i++){
			oneElement = scene.getPhysicElements().get(i);
			if(!((PhysicElement)oneElement).isStatic()){
				for(int j = 0; j < scene.getPhysicElements().size(); j++){
					otherElement = scene.getPhysicElements().get(j);
					detectColision((PhysicElement)oneElement, (PhysicElement)otherElement);
				}
			}
		}
	}
	/**
	 * Esta função ordena os elementos a serem simulados com base no valor Y da posição de cada elemento.
	 */
	public void sortElementsByPosition(){
		int j = 0;
		for(int i = 1; i < scene.getPhysicElements().size(); i++){
			j = i - 1;
			oneElement = scene.getPhysicElements().get(i);
			while(j >= 0 && ((PhysicElement)oneElement).getPosition().getY() > ((PhysicElement)scene.getPhysicElements().get(j)).getPosition().getY()){
				scene.getPhysicElements().set(j + 1, scene.getPhysicElements().get(j));
				j--;
			}
			scene.getPhysicElements().set(j + 1, oneElement);			
		}
	}
	
	/**
	 * Essa função aplica as alterações causadas pela velocidade e acelaração.
	 */
	private void update(PhysicElement element){
		if(!element.isStatic() && !element.isDeleted() && element.isRigidBody()){
			element.getVelocity().addTo(element.getAceleration());
			element.getVelocity().addTo(getGravity());
			element.addToPosition(element.getVelocity());
			element.addToRotationInRadians(element.getRotationVelocity());
		}
	}
	
	/**
	 * Essa função é referente a etapa genéria da detecção de colisão.
	 */
	private void detectColision(PhysicElement one, PhysicElement other){
		normal = Vector2D.subtract(one.getPosition(), other.getPosition());

		if(normal.module() < one.getRadio() + other.getRadio() && one != other && one.isColidable() && other.isColidable()){
			colisionDetector  = colisionDetectors.get(getHashCode(one.getShape(), other.getShape()));
			if(colisionDetector != null){
				if(colisionDetector.detectColision(one, other) == true){
					return;
				}
			}
			
			if(one.isStatic() || other.isStatic()){
				colisionDetector  = colisionDetectors.get(getHashCode(other.getShape(), one.getShape()));
				if(colisionDetector != null){
					colisionDetector.detectColision(other, one);
				}
			}
		}
	}
	/**
	 * Essa função inicialisa todos os objetos tratadores de colisão.
	 */
	private void Initialize(){
		
		//ELIPSE x ELIPSE
		colisionDetectors.put(getHashCode(Shape.ELIPSE, Shape.ELIPSE), new ColisionDetector() {
			@Override
			public boolean detectColision(PhysicElement one, PhysicElement other) {
				if(one.isRigidBody() && other.isRigidBody()){
					normal = Vector2D.subtract(other.getPosition(), one.getPosition());
					
					oneVelocity = one.getVelocity();
					otherVelocity = other.getVelocity();
					
					angle = normal.angle();
					intersection = new Vector2D(Math.abs((one.getRadio() + other.getRadio() - normal.module())), 0);
					intersection.rotate(angle);
					
					separationX = separation*signum(intersection.getX());
					separationY = separation*signum(intersection.getY());
					
					velocityX = Math.abs(oneVelocity.getX()) + Math.abs(otherVelocity.getX());
					if(velocityX != 0){
						if(!one.isStatic())one.addToPositionX(-intersection.getX()*Math.abs(oneVelocity.getX()/velocityX) - separationX);
						if(!other.isStatic())other.addToPositionX(intersection.getX()*Math.abs(otherVelocity.getX()/velocityX) + separationX);
					}else{
						if(!one.isStatic())one.addToPositionX(-intersection.getX()/2 - separationX);
						else other.addToPositionX(intersection.getX()/2 + separationX);
						if(!other.isStatic())other.addToPositionX(intersection.getX()/2 + separationX);
						else one.addToPositionX(-intersection.getX()/2 - separationX);
					}
					
					velocityY = Math.abs(oneVelocity.getY()) + Math.abs(otherVelocity.getY());
					if(velocityY != 0){
						if(!one.isStatic())one.addToPositionY(-intersection.getY()*Math.abs(oneVelocity.getY()/velocityY) - separationY);
						if(!other.isStatic())other.addToPositionY(intersection.getY()*Math.abs(otherVelocity.getY()/velocityY) + separationY);
					}else{
						if(!one.isStatic())one.addToPositionY(-intersection.getY()/2 - separationY);
						else other.addToPositionY(intersection.getY()/2);
						if(!other.isStatic())other.addToPositionY(intersection.getY()/2 + separationY);
						else one.addToPositionY(-intersection.getY()/2 - separationY);
					}
				}
				
				if(one.isRigidBody() && other.isRigidBody()){
					reacalcVelocities(one, other, angle);
				}
				
				colisionPoint = new Vector2D(other.getRadio(), 0.0f);
				colisionPoint.rotate(angle);
				colisionPoint.addTo(other.getPosition());
				
				one.colide(other, colisionPoint);
				other.colide(one, colisionPoint);
					
				return true;
			}
		});
		
		
		//ELIPSE X RETANGULO
		colisionDetectors.put(getHashCode(Shape.ELIPSE, Shape.RECTANGLE), new ColisionDetector() {
			@Override
			public boolean detectColision(PhysicElement one, PhysicElement other) {
				return colisionDetectors.get(getHashCode(other.getShape(), one.getShape())).detectColision(other, one);
			}
		});	
		
		//RETANGLULO X ELIPSE
		colisionDetectors.put(getHashCode(Shape.RECTANGLE, Shape.ELIPSE), new ColisionDetector() {
			@Override
			public boolean detectColision(PhysicElement one, PhysicElement other) {				
				angle = one.getRotation();
				
				onePosition = one.getPosition();
				otherPosition = other.getPosition();
				oneVelocity = one.getVelocity();
				otherVelocity = other.getVelocity();
				
				if(angle != 0){					
					onePosition.rotate(-angle);
					otherPosition.rotate(-angle);
					oneVelocity.rotate(-angle);
					otherVelocity.rotate(-angle);
				}

				colided = false;
				
				if(isPointInRect(onePosition, one.getWidth() + 2*other.getRadio(), one.getHeight() + 2*other.getRadio(), otherPosition)){
					colided = true;
					
					normal = Vector2D.subtract(onePosition, otherPosition);
					normalSignalX = signum(normal.getX());
					normalSignalY = signum(normal.getY());
					
					onePoint = new Vector2D(onePosition.getX() - normalSignalX*(one.getWidth()/2 + other.getRadio()),
	                                        onePosition.getY() - normalSignalY*(one.getHeight()/2 + other.getRadio()));
					
					intersection = Vector2D.subtract(onePoint, otherPosition);
						
					if(one.isRigidBody() && other.isRigidBody()){
						
						velocityX = EPSILON;
						if(Math.signum(oneVelocity.getX()) != Math.signum(otherVelocity.getX())){
							velocityX = Math.abs(oneVelocity.getX() - otherVelocity.getX()) + EPSILON;
						}
						
						velocityY = EPSILON;
						if(Math.signum(oneVelocity.getY()) != Math.signum(otherVelocity.getY())){
							velocityY = Math.abs(oneVelocity.getY() - otherVelocity.getY()) + EPSILON;
						}
						
						timeX = (Math.abs(intersection.getX()) + EPSILON)/(velocityX);
						timeY = (Math.abs(intersection.getY()) + EPSILON)/(velocityX);
						
						separationX = separation*signum(intersection.getX());
						separationY = separation*signum(intersection.getY());
						
						if(timeX < timeY){
							colisionPoint = new Vector2D(onePoint.getX(), otherPosition.getY());
							velocityX = Math.abs(oneVelocity.getX()) + Math.abs(otherVelocity.getX());
							if(velocityX != 0){
								if(!one.isStatic())one.addToPositionX(-intersection.getX()*Math.abs(oneVelocity.getX()/velocityX) - separationX);
								if(!other.isStatic())other.addToPositionX(intersection.getX()*Math.abs(otherVelocity.getX()/velocityX) + separationX);
							}else{
								if(!one.isStatic())one.addToPositionX(-intersection.getX()/2 -separationX);
								else other.addToPositionX(intersection.getX()/2 + separationX);
								if(!other.isStatic())other.addToPositionX(intersection.getX()/2 + separationX);
								else one.addToPositionX(-intersection.getX()/2 - separationX);
							}
							intersection.setY(0.0f);
							if(intersection.getX() == 0.0f){
								intersection.setX(1.0f);
							}
						}else{
							velocityY = Math.abs(oneVelocity.getY()) + Math.abs(otherVelocity.getY());
							if(velocityY != 0){
								if(!one.isStatic())one.addToPositionY(-intersection.getY()*Math.abs(oneVelocity.getY()/velocityY) - separationY);
								if(!other.isStatic())other.addToPositionY(intersection.getY()*Math.abs(otherVelocity.getY()/velocityY) + separationY);
							}else{
								if(!one.isStatic())one.addToPositionY(-intersection.getY()/2 - separationY);
								else other.addToPositionY(intersection.getY()/2 + separationX);
								if(!other.isStatic())other.addToPositionY(intersection.getY()/2 + separationY);
								else one.addToPositionY(-intersection.getY()/2 - separationY);
							}
							intersection.setX(0.0f);
							if(intersection.getY() == 0.0f){
								intersection.setY(-1.0f);
							}
						}
						
						intersection.rotate(angle);
					    normalAngle = intersection.angle();
					}
					if(Math.abs(intersection.getX()) < Math.abs(intersection.getY())){
						colisionPoint = new Vector2D(onePoint.getX(), otherPosition.getY());
					}else{
						colisionPoint = new Vector2D(otherPosition.getX(), onePoint.getY());
					}
					colisionPoint.rotate(-angle);
				}
				
				if(angle != 0){
					onePosition.rotate(angle);
					otherPosition.rotate(angle);
					oneVelocity.rotate(angle);
					otherVelocity.rotate(angle);
				}
				
				if(colided){
					one.colide(other, colisionPoint);
					other.colide(one, colisionPoint);
					if(one.isRigidBody() && other.isRigidBody()){
						reacalcVelocities(one, other, normalAngle);
					}
					return true;
				}
				return false;
			}
		});
		
		
		//RETANGULO x RETANGULO
		colisionDetectors.put(getHashCode(Shape.RECTANGLE, Shape.RECTANGLE), new ColisionDetector() {
			@Override
			public boolean detectColision(PhysicElement one, PhysicElement other) {					
				angle = one.getRotation();
				
				onePosition = one.getPosition();
				otherPosition = other.getPosition();
				oneVelocity = one.getVelocity();
				otherVelocity = other.getVelocity();
				
				if(angle != 0){					
					onePosition.rotate(-angle);
					otherPosition.rotate(-angle);
					oneVelocity.rotate(-angle);
					otherVelocity.rotate(-angle);
				}

				colided = false;
				otherPoint = new Vector2D(0.0f, 0.0f);
				for(int i=0;i<rectanglePointAcess.length;i++){
					
					otherPoint.set(rectanglePointAcess[i][0]*other.getWidth()/2, rectanglePointAcess[i][1]*other.getHeight()/2);
					if(other.getRotation() != 0){
						otherPoint.rotate(other.getRotation());
					}
					otherPoint.addTo(otherPosition);
					
					if(isPointInRect(onePosition, one.getWidth(), one.getHeight(), otherPoint)){
						colided = true;
						
						normal = Vector2D.subtract(otherPoint, otherPosition);
						onePoint = new Vector2D(onePosition.getX() - signum(normal.getX())*one.getWidth()/2,
		                                        onePosition.getY() - signum(normal.getY())*one.getHeight()/2);
						
						if(one.isRigidBody() && other.isRigidBody()){
							
							intersection = Vector2D.subtract(onePoint, otherPoint);
							
							separationX = separation*signum(intersection.getX());
							separationY = separation*signum(intersection.getY());
							
							velocityX = EPSILON;
							if(Math.signum(oneVelocity.getX()) != Math.signum(otherVelocity.getX())){
								velocityX = Math.abs(oneVelocity.getX() - otherVelocity.getX()) + EPSILON;
							}
							
							velocityY = EPSILON;
							if(Math.signum(oneVelocity.getY()) != Math.signum(otherVelocity.getY())){
								velocityY = Math.abs(oneVelocity.getY() - otherVelocity.getY()) + EPSILON;
							}
							
							timeX = Math.abs(intersection.getX() + EPSILON)/velocityX;
							timeY = Math.abs(intersection.getY() + EPSILON)/velocityY;
							
							if(timeX < timeY){
								velocityX = Math.abs(oneVelocity.getX()) + Math.abs(otherVelocity.getX());
								if(velocityX != 0){
									if(!one.isStatic())one.addToPositionX(-intersection.getX()*Math.abs(oneVelocity.getX()/velocityX) - separationX);
									if(!other.isStatic())other.addToPositionX(+intersection.getX()*Math.abs(otherVelocity.getX()/velocityX) + separationX);
								}else{
									if(!one.isStatic())one.addToPositionX(-intersection.getX()/2 - separationX);
									else other.addToPositionX(intersection.getX()/2 + separationX);
									if(!other.isStatic())other.addToPositionX(intersection.getX()/2 + separationX);
									else one.addToPositionX(-intersection.getX()/2 - separationX);
								}
								intersection.setY(0.0f);
								if(intersection.getX() == 0.0f){
									intersection.setX(1.0f);
								}
							}else{
								velocityY = Math.abs(oneVelocity.getY()) + Math.abs(otherVelocity.getY());
								if(velocityY != 0){
									if(!one.isStatic())one.addToPositionY(-intersection.getY()*Math.abs(oneVelocity.getY()/velocityY) - separationY);
									if(!other.isStatic())other.addToPositionY(intersection.getY()*Math.abs(otherVelocity.getY()/velocityY) + separationY);
								}else{
									if(!one.isStatic())one.addToPositionY(-intersection.getY()/2 - separationY);
									else other.addToPositionY(intersection.getY()/2 + separationY);
									if(!other.isStatic())other.addToPositionY(intersection.getY()/2 + separationY);
									else one.addToPositionY(-intersection.getY()/2 - separationY);
								}
								intersection.setX(0.0f);
								if(intersection.getY() == 0.0f){
									intersection.setY(-1.0f);
								}
							}
							
							intersection.rotate(angle);
						    normalAngle = intersection.angle();
						}
						
						colisionPoint = Vector2D.sum(onePoint, otherPoint);
						colisionPoint.multply(0.5);
						colisionPoint.rotate(-angle);
						break;
					}
				}
				
				if(angle != 0){
					onePosition.rotate(angle);
					otherPosition.rotate(angle);
					oneVelocity.rotate(angle);
					otherVelocity.rotate(angle);
				}
				
				if(colided){
					if(one.isRigidBody() && other.isRigidBody()){
						reacalcVelocities(one, other, normalAngle);
					}
					one.colide(other, colisionPoint);
					other.colide(one, colisionPoint);
					return true;
				}
				
				return false;
			}
		});
	}
	
	public double signum(double value){
		if(value >= 0.0f){
			return 1.0f;
		}else{
			return -1.0f;
		}
	}
	
	
	/**
	 * Essa função verifica se um dado ponto se encontra dentro de um determinado retângulo.
	 */
	public boolean isPointInRect(Vector2D center, double width, double height, Vector2D point){
		if(point.getX() < (center.getX() - width/2)){
			return false;
		}
		
		if(point.getX() > (center.getX() + width/2)){
			return false;
		}
		
		if(point.getY() < (center.getY() - height/2)){
			return false;
		}
		
		if(point.getY() > (center.getY() + height/2)){
			return false;
		}
		return true;
	}
	
	/**
	 * Essa função aplica as novas velocidades após a colisão de dois elementos.
	 */
	private void reacalcVelocities(PhysicElement one, PhysicElement other, double normalAngle){
		oneVelocity = one.getVelocity();
		otherVelocity = other.getVelocity();
		
		oneVelocity.rotate(-normalAngle);
		otherVelocity.rotate(-normalAngle);
		
		elasticity = one.getElasticity() + other.getElasticity();
		if(one.isStatic()){
			otherVelocity.setX(-otherVelocity.getX()*elasticity);
		}else if(other.isStatic()){
			oneVelocity.setX(-oneVelocity.getX()*elasticity);
		}else{
			oneVelocity.setX(((one.getWeight() - other.getWeight())/(one.getWeight() + other.getWeight())*oneVelocity.getX()
					+ (2*other.getWeight())/(one.getWeight() + other.getWeight())*otherVelocity.getX())*elasticity);
			
			otherVelocity.setX(((other.getWeight() - one.getWeight())/(one.getWeight() + other.getWeight())*other.getVelocity().getX()
					+ (2*one.getWeight())/(one.getWeight() + other.getWeight())*oneVelocity.getX())*elasticity);
		}
		
		//Apesar de não seguir corretamente a formula do atritito é mais rápido que esta.
		friction = one.getFriction() + other.getFriction();
		if(friction > 0){
			if(!one.isStatic() && friction > 0){
				oneVelocity.set(oneVelocity.getX(), oneVelocity.getY()*(1 - friction));
			}
			
			if(!other.isStatic() && friction > 0){
				otherVelocity.set(otherVelocity.getX(), otherVelocity.getY()*(1 - friction));
			}
		}
			
		oneVelocity.rotate(normalAngle);
		otherVelocity.rotate(normalAngle);
	}
	
	/**
	 * Retorna a sena na qual o Simulador está sendo utilizado.
	 */
	public Scene getScene() {
		return scene;
	}
	
	/**
	 * Seta a sena na qual o Simulador está sendo utilizado.
	 */
	public void setScene(Scene scene) {
		this.scene = scene;
	}
	
	/**
	 * Retorna a sena na qual o Simulador está sendo utilizado.
	 */
	private String getHashCode(Shape one, Shape other){
		return one.toString() + other.toString();
	}
	
	/**
	 * Retorna a gravidade que está sendo aplicada.
	 */
	public Vector2D getGravity() {
		return gravity;
	}
	
	/**
	 * Seta a gravidade que deve ser aplicada.
	 */
	public void setGravity(Vector2D gravity) {
		this.gravity.set(gravity);
	}
	
	/**
	 * Seta a gravidade que deve ser aplicada.
	 */
	public void setGravity(double x, double y) {
		this.gravity.set(x, y);
	}
}
