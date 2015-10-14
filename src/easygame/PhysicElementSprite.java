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
import android.graphics.Bitmap;

/**
 * Essa classe é uma implementação da classe Sprite específica para objetos do tipo PhysicElement.
 * @author Wiliams Magalhães Primo
 */

public class PhysicElementSprite extends BaseSprite{
	
	//Propriedades privadas de cada instancia
	private PhysicElement element = null;
	
	//Construtores
	public PhysicElementSprite(Bitmap bitmap, int rows, int columns){
		super(bitmap, rows, columns);
	}
	
	public PhysicElementSprite(Bitmap bitmap){
		super(bitmap);
	}
	
	public PhysicElementSprite(String imageName, int rows, int columns){
		super(imageName, rows, columns);
	}
	
	public PhysicElementSprite(String imageName){
		super(imageName);
	}

	/**
	 * Retorna o elemento relacionado a essa sprite.
	 */
	public PhysicElement getElement() {
		return element;
	}

	/**
	 * Seta o elemento relacionado a essa sprite.
	 */
	public void setElement(PhysicElement element) {
		this.element = element;
	}

	/**
	 * Retorna a rotação do elemento relacionado.
	 */
	@Override
	public double getRotation() {
		return element.getRotation();
	}

	/**
	 * Retorna a propriedade Z do elemento relacionado.
	 */
	@Override
	public double getZ() {
		return this.element.getZ();
	}

	/**
	 * etorna a posição do elemento relacionado.
	 */
	@Override
	public Vector2D getPosition() {
		return element.getPosition();
	}

	/**
	 * Seta a posição do elemento relacionado.
	 */
	public void setPosition(Vector2D position) {
		element.setPosition(position);
	}
	
	/**
	 * Seta a largura do elemento relacionado.
	 */
	public void setWidth(double width) {
		this.element.setWidth(width);
	}

	/**
	 * Retorna a largura do elemento relacionado.
	 */
	@Override
	public double getWidth() {
		return this.element.getWidth();
	}

	/**
	 * Seta a altura do elemento relacionado.
	 */
	public void setHeight(double height) {
		this.element.setHeight(height);
	}

	/**
	 * Retorna a altura do elemento relacionado.
	 */
	@Override
	public double getHeight() {
		return this.element.getHeight();
	}
	
	/**
	 * Seta a rotação do elemento em radianos.
	 */
	@Override
	public void setRotation(double rotation) {
		this.element.setRotation(rotation);
	}

	/**
	 * Seta a profundidade com relação aos outros elementos.
	 */
	@Override
	public void setZ(double z) {
		this.element.setZ(z);
	}

	/**
	 * Seta a posição do elemento.
	 */
	@Override
	public void setPosition(double x, double y) {
		this.element.setPosition(x, y);
	}
	
	/**
	 * Adiciona à posição do elemento.
	 */
	public  void addToPosition(Vector2D velocity){
		this.element.addToPosition(velocity);
	}
	
	/**
	 * Adiciona à posição do elemento.
	 */
	public  void addToPosition(double x, double y){
		this.element.addToPosition(x,y);
	}
	
	/**
	 * Retorna a posição do elemento com base no ponto superior esquerdo.
	 */
	@Override
	public Vector2D getBasePosition() {
		return element.getBasePosition();
	}

	/**
	 * Seta a posição do elemento com base no ponto superior esquerdo.
	 */
	@Override
	public void setBasePosition(double x, double y) {
		element.setBasePosition(x, y);
	}

	/**
	 * Seta a posição do elemento com base no ponto superior esquerdo.
	 */
	@Override
	public void setBasePosition(Vector2D position) {
		element.setBasePosition(position);
	}
}
