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
 * Essa classe é a base para os elementos do jogo, ou seja, o elemento para ser reconhecido pelo jogo deve herdar dessa classe.
 *@author Wiliams Magalhães Primo
 */
public abstract class GameElement {
	
	//Variável que controla a geração de novos códigos
	private static final int INVALID_CODE = -1;
	private static int actualCode = INVALID_CODE;
	
	//Gera um código para cada instancia dessa classe
	private int code = newCode();
	private boolean deleted = false;
	private Scene scene = null;
	
	/**
	 * Gera um novo código para o objeto.
	 */
	public static  int newCode(){
		actualCode++;
		return actualCode;
	}

	/**
	 * Retorna o código do objeto.
	 */
	public  int getCode() {
		return code;
	}
	
	/**
	 * Seta o objeto como deletado, em outras palavras, deleta o objeto
	 */
	public  void delete(){
		this.setDeleted(true);
	}

	/**
	 * Retorna se o objeto foi deletado ou não.
	 */
	public  boolean isDeleted() {
		return deleted;
	}

	/**
	 * Seta o objeto como deletado.
	 */
	public  void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	/**
	 * Retorna a sena à qual o objeto pertence.
	 */
	public  Scene getScene() {
		return scene;
	}

	/**
	 * Seta a sena à qual o objeto pertence.
	 */
	public  void setScene(Scene scene) {
		this.scene = scene;
	}
	
	
	/**
	 * Retorna a posição do elemento.
	 */
	public abstract Vector2D getPosition();
	
	/**
	 * Retorna a posição considerando a base supeior esquerda.
	 */
	public abstract Vector2D getBasePosition();
	
	/**
	 * Seta a posição do elemento.
	 */
	public abstract void setPosition(Vector2D position);
	
	/**
	 * Seta a posição do elemento.
	 */
	public abstract void setPosition(double x, double y);
	
	/**
	 * Seta a posição considerando a base supeior esquerda.
	 */
	public abstract void setBasePosition(double x, double y);
	
	/**
	 * Seta a posição considerando a base supeior esquerda.
	 */
	public abstract void setBasePosition(Vector2D position);
	
	/**
	 * Retorna a largura do elemento.
	 */
	public abstract double getWidth();
	
	/**
	 * Seta a largura do elemento.
	 */
	public abstract void setWidth(double width);
	
	/**
	 * Retorna a altura do elemento.
	 */
	public abstract double getHeight();
	
	/**
	 * Seta a altura do elemento.
	 */
	public abstract void  setHeight(double height);
	
	/**
	 * Seta a altura e largura de um elemento.
	 */
	public abstract void setSize(double width, double height);
	
	/**
	 * Retorna a rotação do elemento.
	 */
	public abstract double getRotation();
	
	/**
	 * Seta a rotação do elemento.
	 */
	public abstract void setRotation(double rotation);
	
	/**
	 * Retorna a profundidade de um elemento com relação aos outros.
	 */
	public abstract double getZ();
	
	/**
	 * Seta a profundidade de um elemento com relação aos outros.
	 */
	public abstract void setZ(double z);
	
	/**
	 * Retorna se o elemento está visível.
	 */
	public abstract boolean isVisible();
	
	/**
	 * Seta se o elemento está visível na tela.
	 */
	public abstract void setVisible(boolean visible);
	
	/**
	 * Retorna se o elemento tem posição fixa na tela.
	 */
	public abstract boolean isFixed();
	
	/**
	 * Seta se o elemento tem posição fixa na tela.
	 */
	public abstract void setFixed(boolean fixed);
	
	/**
	 * Adiciona à posição do elemento.
	 */
	public abstract void addToPosition(Vector2D velocity);
	
	/**
	 * Adiciona à posição do elemento.
	 */
	public abstract void addToPosition(double x, double y);
}
