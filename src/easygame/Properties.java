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
 * Essa classe é usada para salvar propriedades atuais dos elementos 
 * 
 * @author Wiliams Magalhães Primo
 */

public class Properties {
	private Vector2D position = new Vector2D();
	private double width = 0.0f,
			       height = 0.0f,
			       rotation = 0.0f,
			       z = 0.0f,
				   scale = 1.0f;
	
	private boolean fixed = false;

	/**
	 * Retorna a posição atual do elemento.
	 */
	public Vector2D getPosition() {
		return position;
	}

	/**
	 * Seta a posição atual do elemento.
	 */
	public void setPosition(Vector2D position) {
		this.position.set(position);
	}

	/**
	 * Retorna a largura atual do elemento.
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * Seta a largura atual do elemento.
	 */
	public void setWidth(double width) {
		this.width = width;
	}

	/**
	 * Retorna a largura atual do elemento.
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * Seta a altura atual do elemento.
	 */
	public void setHeight(double height) {
		this.height = height;
	}

	/**
	 * Retorna o z atual do elemento.
	 */
	public double getZ() {
		return z;
	}

	/**
	 * Seta o z atual do elemento.
	 */
	public void setZ(int z) {
		this.z = z;
	}

	/**
	 * Retorna a rotação atual do elemento
	 */
	public double getRotation() {
		return rotation;
	}

	/**
	 * Retorna a rotação atual do elemento
	 */
	public void setRotation(double rotation) {
		this.rotation = rotation;
	}
	
	/**
	 * Seta as propriedades do elemento aos campos apropriados
	 */
	public void set(GameElement element){
		this.setHeight(element.getHeight());
		this.setWidth(element.getWidth());;
		this.setRotation(element.getRotation());
		this.setPosition(element.getPosition());
		this.setFixed(element.isFixed());
	}

	/**
	 * Retorna se o elemento é fixo na tela.
	 */
	public boolean isFixed() {
		return fixed;
	}

	
	/**
	 * Seta se o elementoé fixo na tela.
	 */
	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}

	/**
	 * Retorna a escala atual do objeto.
	 */
	public double getScale() {
		return scale;
	}

	/**
	 * Retorna a escala atual do objeto. 
	 */
	public void setScale(double scale) {
		this.scale = scale;
	}
			
}
