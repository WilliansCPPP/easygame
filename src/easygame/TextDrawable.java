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
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.view.MotionEvent;

/**
 * Essa classe serve para a criação de textos desenháveis na tela, tanto texto corrido como de paragrafo.
 * @author Willians Magalhães Primo
 */

public class TextDrawable extends GameElement implements DrawableElement, TouchableElement{
	
	private String text = "";
	private  List<String> lines = new ArrayList<String>();
	private double z = 0;
	private double width = 0.0f,
			      height = 0.0f,
			      rotation = 0.0f,
			      textHeight = 0.0f,
			      lineSpace = 0.5f;
	
	private boolean visible = true,
			        fixed = false,
			        widthSized = false,
			        heightSized = false;
	
	private Vector2D position = new Vector2D(0.0f, 0.0f);
	private Paint paint = new Paint();
	private TouchListener touchListener = null;
	
	//Construtores
	public TextDrawable(String text) {
		setText(text);
		makeParagraph();
	}
	
	public TextDrawable(String text, double width, double height) {
		this.width = width;
		this.height = height;
		paint.setTextAlign(Paint.Align.LEFT);
		setText(text);
	}
	
	/**
	 * Retorna o z, ou seja a profundidade do texto com relação aos outros elementos.
	 */
	@Override
	public double getZ() {
		return this.z;
	}

	/**
	 * Retorna a largura da caixa de texto.
	 */
	@Override
	public double getWidth() {
		return this.width;
	}
	
	/**
	 * Seta a largura da caixa de texto.
	 */
	public void setWidth(double width){
		this.width = width;
		widthSized = (width != 0);
		makeParagraph();
	}

	/**
	 * Retorna altura da caixa de texto.
	 */
	@Override
	public double getHeight() {
		return this.height;
	}
	
	/**
	 * Seta a altura da caixa de texto.
	 */
	public void setHeight(double height) {
		this.height = height;
		heightSized = (height != 0);
		makeParagraph();
	}

	/**
	 * Retorna a posição do texto.
	 */
	@Override
	public Vector2D getPosition() {
		return this.position;
	}
	
	/**
	 * Seta a posição do texto.
	 */
	public void setPosition(Vector2D position){
		this.position.set(position);
	}
	
	/**
	 * Seta a posição do texto.
	 */
	public void setPosition(double x, double y){
		this.position.set(x, y);
	}

	/**
	 *Retorna se o texto tem posição fixa na tela.
	 */
	@Override
	public boolean isFixed() {
		return this.fixed;
	}
	
	/**
	 * Seta se o texto tem posição fixa na tela.
	 */
	public void setFixed(boolean fixed){
		this.fixed = fixed;
	}

	/**
	 * Retorna a rotação do texto em radianos.
	 */
	@Override
	public double getRotation() {
		return this.rotation;
	}
	
	/**
	 * Seta a rotação do texto em graus celcios.
	 */
	public void setRotationInDegree(double rotation) {
		this.setRotationInRadians(Math.toRadians(rotation));
	}
	
	/**
	 * Seta a rotação em radianos.
	 */
	public void setRotationInRadians(double rotation) {
		this.rotation = rotation;
		if(this.rotation > 2*Math.PI){
			this.rotation = 2*Math.PI - this.rotation;
		}else if(this.rotation < -2*Math.PI){
			this.rotation = this.rotation + 2*Math.PI;
		}
	}

	/**
	 * Função resposável por desenhar o texto na tela.
	 */
	@Override
	public void draw(Properties properties, Canvas canvas, GameCamera _camera) {
		if(visible){
			float textSize = getTextSize();
			paint.setTextSize((float)(textSize*properties.getScale()));
			canvas.save();
			canvas.rotate((float)-properties.getRotation(), (float)properties.getPosition().getX(), (float)properties.getPosition().getY());
			int size = lines.size();
			double actualHeight = 0.0f;
			properties.getPosition().subtractTo(properties.getWidth()/2.0f, properties.getHeight()/2.0f - textHeight*properties.getScale());
			for(int i = 0; i < size; i++){
				canvas.drawText(lines.get(i), (float)(properties.getPosition().getX()), (float)(properties.getPosition().getY() + actualHeight), paint);
				actualHeight += ((textHeight + lineSpace*textHeight)*properties.getScale());
			}		
			paint.setTextSize(textSize);
			canvas.restore();
		}
	}

	/**
	 * Seta se o texto deve estar visível.
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * Retorna se o texto está visivel.
	 */
	@Override
	public boolean isVisible() {
		return this.visible;
	}

	/**
	 * Retorna o uma string que representa o texto a ser desenhado.
	 */
	public String getText() {
		return text;
	}

	/**
	 * Seta uma string que representa o texto a ser desenhado.
	 */
	public void setText(String text) {
		this.text = text;
		makeParagraph();
	}
	
	/**
	 * Retorna uma lista com todas as linhas to texto.
	 */
	public List<String> getLines() {
		return lines;
	}

	/**
	 * Setá as linhas do texto.
	 */
	public void setLines(List<String> lines) {
		this.lines = lines;
	}

	/**
	 * Retorna a altura da letra.
	 */
	public double getTextHeight() {
		return textHeight;
	}

	/**
	 * Seta a altura da letra.
	 */
	protected void setTextHeight(double textHeight) {
		this.textHeight = textHeight;
	}
	
	/**
	 * Gera as linhas do texto a ser desenhado.
	 */
	protected void makeParagraph(){
		String[] words = text.split("[ ]");
		if(!widthSized){
			this.width = paint.measureText(text + "  ");
		}
		
		this.textHeight = Math.abs((paint.descent() + paint.ascent()));
		
		lines.clear();
		boolean start = true;
		String line = "";
		for(int i = 0; i < words.length; i++){
			if(paint.measureText(line + " " + words[i]) <= width || start || !widthSized){
				if(!start){
					line += " ";
				}
				line += words[i];
				if(start){
					start = false;
				}
			}else{
				lines.add(line);
				line = "";
				start = true;
				i --;
			}
		}
		
		if(line != ""){
			lines.add(line);
		}
		
		if(!heightSized){
			this.height = textHeight*lines.size() + (lines.size() - 1)*textHeight*lineSpace;
		}
	}
	
	/**
	 * Seta o tamanho da caixa de texto.
	 */
	public void setSize(double width, double height){
		this.width = width;
		this.height = height;
		widthSized = (width != 0);
		heightSized = (height != 0);
		makeParagraph();
	}
	
	/**
	 * Retorna o tamanho da fonto do texto.
	 */
	public float getTextSize(){
		return this.paint.getTextSize();
	}
	
	/**
	 * Seta o tamanho da fonte.
	 */
	public void setTextSize(float textSize){
		this.paint.setTextSize(textSize);
		makeParagraph();
	}
	
	/**
	 * Retorna a cor do texto.
	 */
	public int getTextColor(){
		return this.paint.getColor();
	}
	
	/**
	 * Seta a cor do texto.
	 */
	public void setTextColor(int color){
		this.paint.setColor(color);
	}
	
	/**
	 * Seta a fonte do texto.
	 */
	public void setTextTypeface(Typeface typeface){
		this.paint.setTypeface(typeface);
		makeParagraph();
	}
	
	/**
	 * Seta o estilo de texto.
	 */
	public void setTextStyle(Style style){
		this.paint.setStyle(style);
		makeParagraph();
	}
	
	/**
	 * Retorna o estilo de texto.
	 */
	public Style getTextStyle(){
		return this.paint.getStyle();
	}

	/**
	 * Retorna o espaço entre as linhas.
	 */
	public double getLineSpace() {
		return lineSpace;
	}

	/**
	 * Seta o espaço entre as linhas.
	 */
	public void setLineSpace(double lineSpace) {
		this.lineSpace = lineSpace;
	}

	@Override
	public void setRotation(double rotation) {
		this.rotation = rotation;
	}
	
	@Override
	public boolean touchEvents(Properties properties, MotionEvent event, GameCamera camera) {
		if(touchListener != null){
			touchListener.onTouch(this, event, camera);
		}
		
		return true;
	}
	
	public void onTouchEvent(MotionEvent event, GameCamera camera){}

	@Override
	public void setZ(double z) {
		this.z = z;
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
	 * Seta o TouchListener para a elemento.
	 */
	@Override
	public void setTouchListener(TouchListener touchListener) {
		this.touchListener = touchListener;
	}

	/**
	 * Retorna o TouchListener do a elemento.
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
