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
 * Esta classe tem como função permitir o usuário desenhar, ou inseir, imagens na tela.
 * @author Wiliams Magalhães Primo
 */

public class SimpleSprite extends BaseSprite{
	//Propriedades da Sprite
	private double z = 0;
	private double width = 0.0f,
	              height = 0.0f,
				  rotation = 0.0f;
	
	private Vector2D position = new Vector2D(0.0f, 0.0f);
	
	//Construtores
	public SimpleSprite(Bitmap bitmap, int rows, int columns){
		super(bitmap, rows, columns);
	}
	
	public SimpleSprite(String imageName, int rows, int columns){
		super(imageName, rows, columns);
	}
	
	public SimpleSprite(Bitmap bitmap){
		super(bitmap);
	}
	
	public SimpleSprite(String imageName){
		super(imageName);
	}
	
	/**
	 * Retorna a rotação da Sprite em radianos.
	 */
	public double getRotation() {
		return rotation;
	}

	/**
	 * Retorna a largura da sprite.
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * Seta a largura da sprite.
	 */
	public void setWidth(double width) {
		this.width = width;
	}
	
	/**
	 * Retorna a altura da sprite.
	 */
	public double getHeight() {
		return height;
	}
	
	/**
	 * Seta a altura da sprite.
	 */
	public void setHeight(double height) {
		this.height = height;
	}
	/**
	 * Retorna a posição da sprite.
	 */
	@Override
	public Vector2D getPosition() {
		return this.position;
	}
	/**
	 * Seta posição da sprite.
	 */
	public void setPosition(Vector2D position) {
		this.position.set(position);
	}
	/**
	 * Seta a posição da sprite.
	 */
	public void setPosition(double x, double y) {
		this.position.set(x,y);
	}
	/**
	 * Retorna a propriedade z da sprite, que diz respeito a profundidade em relação aos outros elementos.
	 */
	@Override
	public double getZ() {
		return z;
	}
	/**
	 * Seta a propriedade z da sprite, que diz respeito a profundidade em relação aos outros elementos.
	 */
	public void setZ(double z) {
		this.z = z;
	}
	
	/**
	 * Seta a rotação da Sprite em graus celcios.
	 */
	public void setRotationInDegree(double rotation) {
		this.setRotationInRadians(Math.toRadians(rotation));
	}
	
	/**
	 * Seta a rotação da Sprite em graus celcios.
	 */
	public void setRotationInRadians(double rotation) {
		this.rotation = rotation;
		if(this.rotation > 2*Math.PI){
			this.rotation = 2*Math.PI - this.rotation;
		}else if(this.rotation < -2*Math.PI){
			this.rotation = this.rotation + 2*Math.PI;
		}
	}

	@Override
	public void setRotation(double rotation) {
		this.rotation = rotation;
	}
	
	/**
	 * Adiciona à posição do elemento.
	 */
	public void addToPosition(Vector2D velocity){
		this.position.addTo(velocity);
	}
	
	/**
	 * Adiciona à posição do elemento.
	 */
	public void addToPosition(double x, double y){
		this.position.addTo(x,y);
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
