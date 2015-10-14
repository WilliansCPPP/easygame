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

/**
 * Essa classe é usada para representar os vetores de duas dimensões da geometria com suas principais operações. 
 * @author Willians Magalhães Primo
 */
public class Vector2D {
	private double x = 0.0f, 
			       y = 0.0f;
	
	//Construtores
	public Vector2D(){
		this.x = 0.0f;
		this.y = 0.0f;
	}
	
	public Vector2D(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public Vector2D(Vector2D other){
		this.x = other.x;
		this.y = other.y;
	}
	
	/**
	 * Calcula o módulo do vetor.
	 */
	public double module(){
		return Math.sqrt(x*x + y*y);
	}
	
	/**
	 * Calcula o ângulo do vetor.
	 */
	public double angle(){
		double angle = 0.0f;
		if(x == 0 && y > 0){
			angle =  Math.toRadians(90);
		}else if(x == 0 && y < 0){
			angle =  Math.toRadians(270);
		}else{
			angle =  Math.atan(Math.abs(y/x));
			if(x < 0 && y >= 0){
				angle = Math.toRadians(180) - angle;
			}else if(x < 0 && y < 0){
				angle = Math.toRadians(180) + angle;
			}else if(x > 0 && y < 0){
				angle = Math.toRadians(360) - angle;
			}
		}
		return angle;
	}
	
	/**
	 * Aplica um rotação no vetor.
	 */
	public void rotate(double angle){
		double oldX = x;
		double oldY = y;
		this.x = oldX*Math.cos(angle) - oldY*Math.sin(angle);
		this.y = oldX*Math.sin(angle) + oldY*Math.cos(angle);
	}
	
	/**
	 * Multiplica o vetor por um escalar.
	 */
	public void multply(double scale){
		this.x = x*scale;
		this.y = y*scale;
	}
	
	/**
	 * Adiciona às coordenadas do vetor.
	 */
	public void addTo(Vector2D other){
		this.x += other.x;
		this.y += other.y;
	}
	
	/**
	 * Adiciona às coordenadas do vetor.
	 */
	public void addTo(double x, double y){
		this.x += x;
		this.y += y;
	}
	
	/**
	 * Subtrai das coordenadas do vetor.
	 */
	public void subtractTo(Vector2D other){
		this.x -= other.x;
		this.y -= other.y;
	}
	
	/**
	 * Subtrai das coordenadas do vetor.
	 */
	public void subtractTo(double x, double y){
		this.x -= x;
		this.y -= y;
	}
	
	/**
	 * Seta as coordenadas do vetor.
	 */
	public void set(Vector2D other){
		this.x = other.x;
		this.y = other.y;
	}
	
	/**
	 * Seta as coordenadas do vetor.
	 */
	public void set(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Calcula a soma entre dois vetores.
	 */
	public static Vector2D sum(Vector2D one, Vector2D other){
		return new Vector2D(one.x + other.x, one.y + other.y);
	}
	
	/**
	 * Calcula a subtração entre dois vetores.
	 */
	public static Vector2D subtract(Vector2D one, Vector2D other){
		return new Vector2D(one.x - other.x, one.y - other.y);
	}

	/**
	 * Retorna a coordenada x do vetor.
	 */
	public double getX() {
		return x;
	}

	/**
	 * Seta a coordenada x do vetor.
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Retorna a coordenada y do vetor.
	 */
	public double getY() {
		return y;
	}

	/**
	 * Seta a coordenada Y do vetor.
	 */
	public void setY(double y) {
		this.y = y;
	}
}
