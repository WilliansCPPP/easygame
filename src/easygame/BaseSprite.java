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

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

/**
 * 
 * Essa é classe base para a criação de objetos para desenho de imagens na tela.
 * @author Wiliams Magalhães Primo
 */

@SuppressLint("WrongCall")
public abstract class BaseSprite extends GameElement implements DrawableElement, TouchableElement{
	//Estas variáveis são usadas por todas a instancias para o método onDraw.
	protected static Rect source = new Rect(0,0,0,0),
	                      destination = new Rect(0,0,0,0);
	protected static Paint paint = new Paint();
	protected static Vector2D touchPosition = new Vector2D(0.0f, 0.0f),
							  elementPosition = new Vector2D(0.0f, 0.0f);
	protected static int minimumTransparenceTouchable = 25;
	
	//Propriedades privadas de cada instância.
	private Bitmap bitmap = null;
	private SpriteAnimation animation = null;
	
	private float frame = 0.0f;
	
	private int paddingLeft = 0,
			    paddingRight = 0,
			    paddingTop = 0,
			    paddingBottom = 0;
	
	private int rows = 0,
            collumns = 0,
            numberOfFrames = 0,
            frameWidth = 0,
            frameHeight = 0;

	private boolean fixed = false,
		            visible = true,
		            imageTouchable = true;
	
	private TouchListener touchListener = null;
	
	//Construtores
	public BaseSprite(Bitmap bitmap, int rows, int columns){
		this.setBitmap(bitmap, rows, columns);
	}
	
	public BaseSprite(Bitmap bitmap){
		this.setBitmap(bitmap, 1, 1);
	}

	public BaseSprite(String imageName, int rows, int columns){
		this.setBitmap(ImageBuffer.getImage(imageName), rows, columns);
	}
	
	public BaseSprite(String imageName){
		this.setBitmap(ImageBuffer.getImage(imageName), 1, 1);
	}
	
	/**
	 * Retorna a margem interna esquerda da sprite.
	 */
	public  int getPaddingLeft() {
		return paddingLeft;
	}

	/**
	 * Seta a margem interna esquerda da sprite.
	 */
	public  void setPaddingLeft(int paddindLeft) {
		this.paddingLeft = paddindLeft;
	}

	/**
	 * Retorna a margem interna direita da sprite.
	 */
	public  int getPaddingRight() {
		return paddingRight;
	}

	/**
	 * Seta a margem interna direita da sprite.
	 */
	public  void setPaddingRight(int paddingRight) {
		this.paddingRight = paddingRight;
	}

	/**
	 * Retorna a margem interna superior da sprite.
	 */
	public  int getPaddingTop() {
		return paddingTop;
	}

	/**
	 * Seta a margem interna superior da sprite.
	 */
	public  void setPaddingTop(int paddingTop) {
		this.paddingTop = paddingTop;
	}

	/**
	 * Retorna a margem interna inferior da sprite.
	 */
	public  int getPaddingBottom() {
		return paddingBottom;
	}

	/**
	 * Seta a margem interna inferior da sprite.
	 */
	public  void setPaddingBottom(int paddingBottom) {
		this.paddingBottom = paddingBottom;
	}
	
	/**
	 * Seta as margens internas da sprite.
	 */
	public  void setPadding(int padding){
		this.paddingLeft = padding;
		this.paddingRight = padding;
		this.paddingTop = padding;
		this.paddingBottom = padding;
	}
	
	/**
	 * Seta as margens internas da sprite.
	 */
	public  void setPadding(int paddingLeft, int paddingRight, int paddingTop, int paddingBottom){
		this.paddingLeft = paddingLeft;
		this.paddingRight = paddingRight;
		this.paddingTop = paddingTop;
		this.paddingBottom = paddingBottom;
	}
	
	/**
	 * Retorna se a sprite é visível ou não.
	 */
	public  boolean isVisible() {
		return visible;
	}

	/**
	 * Seta se a sprite é visível ou não.
	 */
	public  void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	/**
	 * Retorna o bitmap associado à sprite.
	 */
	public  Bitmap getBitmap() {
		return bitmap;
	}
	
	/**
	 * Método responsável pelo desenho da sprite na tela.
	 */
	@Override
	public void draw(Properties properties, Canvas canvas, GameCamera camera){
		
		if(animation != SpriteAnimation.NONE){
			this.frame = animation.getNextFrame(frame);
			if(frame >= numberOfFrames){
				frame -= numberOfFrames;
			}
		}
		
		if(isVisible() && bitmap != null){
			calcRectFrame(properties);
			canvas.save();
			canvas.rotate((float)Math.toDegrees(properties.getRotation()), (float)properties.getPosition().getX(), (float)properties.getPosition().getY());
			canvas.drawBitmap(bitmap, source,destination, paint);
			canvas.restore();
			onDraw(canvas);
		}
	}
	
	/**
	 * Calcula os retângulos a serem utilizados no desenho da sprite.
	 */
	protected  void calcRectFrame(Properties properties){
		source.left = (int)(((int)getFrame() % (int)getCollumns())*getFrameWidth());
		source.top = (int)(((int)(getFrame()/getCollumns()))*getFrameHeight());
		source.right = (int)(source.left + getFrameWidth());
		source.bottom = (int)(source.top + getFrameHeight());
		
		destination.left = (int)(properties.getPosition().getX() - properties.getWidth()/2.0f + paddingLeft*properties.getScale());
		destination.top = (int)(properties.getPosition().getY() - properties.getHeight()/2.0f + paddingTop*properties.getScale());
		destination.right = (int)(properties.getPosition().getX() + properties.getWidth()/2.0f - paddingRight*properties.getScale());
		destination.bottom = (int)(properties.getPosition().getY() + properties.getHeight()/2.0f - paddingBottom*properties.getScale());
	}
	
	/**
	 * Método que deve ser reescrito no caso de o usuário desejar tratar o desenhar da view.
	 */
	public void onDraw(Canvas canvas){}
	
	@Override
	public boolean touchEvents(Properties properties, MotionEvent event, GameCamera camera) {
		if(!imageTouchable  || touched(properties, event, camera)){
			onTouchEvent(event, camera);
			if(touchListener != null){
				touchListener.onTouch(this, event, camera);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Verifica se houve toque na imagem da sprite.
	 */
	protected boolean touched(Properties properties, MotionEvent event, GameCamera camera){
		touchPosition.set(event.getX(), event.getY());
		
		if(properties.getRotation() != 0){
			touchPosition.rotate(-properties.getRotation());
			properties.getPosition().rotate(-properties.getRotation());
		}
		
		elementPosition.set((properties.getPosition().getX() - properties.getWidth()/2.0f + paddingLeft*properties.getScale()),
		                    (properties.getPosition().getY() - properties.getHeight()/2.0f + paddingTop*properties.getScale()));
		
		touchPosition.subtractTo(elementPosition);
		
		if(touchPosition.getX() > 0 && touchPosition.getX() < (properties.getWidth() - (paddingLeft + paddingRight)*properties.getScale())
		   && touchPosition.getY() > 0 && touchPosition.getY() < (properties.getHeight() - (paddingTop + paddingBottom)*properties.getScale())){
			
			int x = (int)(touchPosition.getX()*(frameWidth/(properties.getWidth() - (paddingLeft + paddingRight)))) 
				  + (int)(((int)getFrame() % (int)getCollumns())*getFrameWidth());
			
			int y = (int)(touchPosition.getY()*(frameHeight/(properties.getHeight() - (paddingTop + paddingBottom)))) 
					  + (int)(((int)(getFrame()/getCollumns()))*getFrameHeight());
			
			if(((bitmap.getPixel(x, y) >> 0x12) & 0xff) > minimumTransparenceTouchable){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Esse método deve ser reescrito caso o usuário desejar tratar os eventos de toque.
	 */
	protected void onTouchEvent(MotionEvent event, GameCamera camera){}

	/**
	 * Seta o bitmap a ser associado à Sprite.
	 */
	public  void setBitmap(Bitmap bitmap, int rows, int columns) {
		this.bitmap = bitmap;
		this.setRows(rows);
		this.setCollumns(columns);
		this.setFrameWidth(bitmap.getWidth()/columns);
		this.setFrameHeight(bitmap.getHeight()/rows);
		this.setNumberOfFrames(rows*columns);
	}

	/**
	 * Retorna o frame atual.
	 */
	public  float getFrame() {
		return frame;
	}

	/**
	 * Seta o frame atual.
	 */
	public  void setFrame(float frame) {
		this.frame = frame;
	}

	/**
	 * Retorna o número de linhas da spritesheet.
	 */
	public  int getRows() {
		return rows;
	}

	/**
	 * Seta o número de linhas da spritesheet.
	 */
	public  void setRows(int rows) {
		this.rows = rows;
	}

	/**
	 * Retorna o número de colunas da spritesheet.
	 */
	public  int getCollumns() {
		return collumns;
	}

	/**
	 * Seta o número de colunas da spritesheet.
	 */
	public  void setCollumns(int collumns) {
		this.collumns = collumns;
	}

	/**
	 * Retorna o número total de frames da spritesheet.
	 */
	public  int getNumberOfFrames() {
		return numberOfFrames;
	}

	/**
	 * Seta o número total de frames da spritesheet.
	 */
	public  void setNumberOfFrames(int numberOfFrames) {
		this.numberOfFrames = numberOfFrames;
	}

	/**
	 * Retorna a largura dos frames.
	 */
	public  int getFrameWidth() {
		return frameWidth;
	}
	
	/**
	 * Seta a largura dos frames.
	 */
	protected  void setFrameWidth(int frameWidth){
		this.frameWidth = frameWidth;
	}
	
	/**
	 * Retorna a altura dos frames.
	 */
	public  int getFrameHeight() {
		return frameHeight;
	}

	/**
	 * Seta a altura dos frames.
	 */
	protected  void setFrameHeight(int frameHeight) {
		this.frameHeight = frameHeight;
	}
	
	/**
	 * Método que deve ser reescrito para retornar a rotação da sprite dependendo do seu uso.
	 */
	public abstract double getRotation();
	
	/**
	 * Método que deve ser reescrito para retornar a posição da sprite dependendo do seu uso.
	 */
	public abstract Vector2D getPosition();

	/**
	 * Retorna se a sprite tem uma posição fixa na tela.
	 */
	public  boolean isFixed() {
		return fixed;
	}

	/**
	 * Retorna se a sprite tem uma posição fixa na tela.
	 */
	public  void setFixed(boolean fixed) {
		this.fixed = fixed;
	}

	/**
	 * Retorna a animação da sprite.
	 */
	public  SpriteAnimation getAnimation() {
		return animation;
	}

	/**
	 * Seta a animação para a sprite.
	 */
	public  void setAnimation(SpriteAnimation animation) {
		this.animation = animation;
	}
	
	/**
	 * Retorna o TouchListener da sprite.
	 */
	public TouchListener getTouchListener() {
		return touchListener;
	}

	/**
	 * Seta o TouchListener para a sprite.
	 */
	public void setTouchListener(TouchListener touchListener) {
		this.touchListener = touchListener;
	}
	
	/**
	 * Retorna se o toque na esprite será verificado com base na imagem.
	 */
	public boolean isImageTouchable() {
		return imageTouchable;
	}

	/**
	 * Seta se o toque na sprite será verificado com base na imagem.
	 */
	public void setImageTouchable(boolean imageTouchable) {
		this.imageTouchable = imageTouchable;
	}
	
	/**
	 * Seta o tamanho do elemento, altura e largura.
	 * OBS: em caso de altura ou largura igual a Zero ( 0 ) indica que essa será calculada proporcionalmente.
	 */
	@Override
	public void setSize(double width, double height) {
		if(width == 0.0f && height == 0.0f){
			setWidth(bitmap.getWidth());
			setHeight(bitmap.getHeight());
		}else if(width == 0.0f){
			setHeight(height);
			setWidth(((double)frameWidth/(double)frameHeight)*height);
		}else if(height == 0){
			setWidth(width);
			setHeight(((double)frameHeight/(double)frameWidth)*width);
		}else{
			setWidth(width);
			setHeight(height);
		}
	}
	
	/**
	 * Seta o menor valor de transparencia aceito na detecção de toques.
	 */
	public void setMinimumTransparenceTouchable(int minimumTransparenceTouchable){
		BaseSprite.minimumTransparenceTouchable = minimumTransparenceTouchable;
	}
}
