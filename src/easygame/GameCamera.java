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
 * Essa classe é responsável pela parte de auto redimensionamento, zoom assim como determinar o foco, ou seja, a parte da cena que
 * deve ser exibida.
 * @author Wiliams Magalhães Primo
 */
public  class GameCamera{
	
	//Propriedades do objeto Camera
	private GameElement element = null;
	private double baseHeight = 724.0f,
				   actualHeight = 724.0f,
				   actualWidth = 1000.0f,
				   scale = 1.0f,
				   zoom = 1.0f,
				   minimumZoom = 0.25f,
				   maximumZoom = 4.0f;
				  
	private Vector2D position = new Vector2D(0.0f, 0.0f);
	private Vector2D positionDiference = new Vector2D(0.0f, 0.0f);
	
	private boolean autoZoomable = false;

	/**
	 * Método chamado a cada tempo de atualização e reponsável pela atualização da posição da Camera.
	 */
	public void update() {
		if(this.element != null){
			if(((GameElement)element).isDeleted()){
				element = null;
				return;
			}
			this.position.set(element.getPosition());
			this.position.subtractTo((actualWidth/(zoomScale()*2) + positionDiference.getX()),(actualHeight/(zoomScale()*2) + positionDiference.getY()));
		}
	}
	
	
	/**
	 * Converte uma posição da tela para posição na dimensão do jogo, ou seja, aquela determinada pela altura base (baseHeight).
	 */
	public void toRealPosition(Vector2D position){
		position.set(position.getX()/zoomScale(), position.getY()/zoomScale());
		position.addTo(this.position);
	}
	
	/**
	 * Converte uma posição da dimensão do jogo para a tela.
	 */
	public void toScreemPosition(Vector2D position){
		position.subtractTo(this.position);
		position.multply(zoomScale());
	}
	
	/**
	 * Converte uma largura da tela para a dimensão do jogo.
	 */
	public double toRealHeight(double height){
		return height/zoomScale();
	}
	
	/**
	 * Converte uma altura da dimensão do jogo para a tela.
	 */
	public double toScreemHeight(double height){
		return height*zoomScale();
	}
	
	/**
	 * Converte uma altura da tela para a dimensão do jogo.
	 */
	public double toRealWidth(double width){
		return width/zoomScale();
	}
	
	/**
	 * Converte uma largura da dimensão do jogo para a tela.
	 */
	public double toScreemWidth(double width){
		return width*zoomScale();
	}
	
	/**
	 * Altera as propriedades do objeto para se ajustar as dimensões da tela.
	 */
	public void changePropertiesToScreem(Properties properties){
		if(properties.isFixed()){
			properties.setHeight(properties.getHeight()*scale);
			properties.setWidth(properties.getWidth()*scale);
			properties.getPosition().multply(scale);
			properties.setScale(scale);
		}else{
			properties.setHeight(properties.getHeight()*zoomScale());
			properties.setWidth(properties.getWidth()*zoomScale());
			properties.getPosition().addTo(position);
			properties.getPosition().multply(zoomScale());
			properties.setScale(zoomScale());
		}
	}
	
	/**
	 * Metodo de rolagem de tela.
	 */
	public void scroll(Vector2D scroll){
		this.position.addTo(scroll);
	}
	
	/**
	 * Metodo de rolagem de tela.
	 */
	public void scroll(double x, double y){
		this.position.addTo(x, y);
	}
	
	/**
	 * Método de zoom automáico.
	 */
	public void onZoomScale(double scale){
		if(autoZoomable){
			this.zoom = Math.min(maximumZoom, Math.max(minimumZoom, scale*zoom));
		}
	}
	
	/**
	 * Retorna o minimo zoom aceito com relação ao auto-zoom.
	 */
	public double getMinimumZoom() {
		return minimumZoom;
	}

	/**
	 * Seta o zoom minimo aceito com relação ao auto-zoom.
	 */
	public void setMinimumZoom(double minimumZoom) {
		this.minimumZoom = minimumZoom;
	}

	/**
	 * Retorna o zoom minimo aceito com relação ao auto-zoom.
	 */
	public double getMaximumZoom() {
		return maximumZoom;
	}

	/**
	 * Seta o zoom máximo aceito com relação ao auto-zoom.
	 */
	public void setMaximumZoom(double maximumZoom) {
		this.maximumZoom = maximumZoom;
	}
	
	/**
	 * Seta se a sena receberá zoom automático.
	 */
	public void setAutoZoomable(boolean autoZoomable){
		this.autoZoomable = autoZoomable;
	}
	
	/**
	 * Retorna se a sena recebe zoom automático.
	 */
	public boolean isAutoZoomable(){
		return this.autoZoomable;
	}
	
	/**
	 * Retorna o atual fator de zoom.
	 */
	public double getZoom() {
		return zoom;
	}

	/**
	 * Seta um fator de zoom.
	 */
	public void setZoom(double zoom) {
		this.zoom = zoom;
	}
	
	
	/**
	 * Retorna a escala com o zoom.
	 */
	public double zoomScale(){
		return this.scale*this.zoom;
	}
	
	/**
	 * Método que o usuário pode sobre-escrever para tratar a atualização da Camera.
	 */
	protected void onUpdate(){}

	/**
	 * Retorna a altura base da sena.
	 */
	public double getBaseHeight() {
		return baseHeight;
	}

	/**
	 * Seta a altura base da sena.
	 */
	public void setBaseHeight(double baseHeight) {
		this.baseHeight = baseHeight;
		setScale(actualHeight/baseHeight);
	}

	/**
	 * Retorna a largura atual da tela.
	 */
	public double getActualHeight() {
		return actualHeight;
	}

	/**
	 * Seta a altura atual da tela.
	 */
	public void setActualHeight(double actualHeight) {
		this.actualHeight = actualHeight;
		setScale(actualHeight/baseHeight);
	}

	/**
	 * Retorna um vetor com a posição atual da Camera.
	 */
	public Vector2D getPosition() {
		return position;
	}

	/**
	 * Seta a posição atual da Camera.
	 */
	public void setPosition(Vector2D position) {
		this.position.set(position);
	}

	/**
	 * Retorna a escala, calculada com base na altura base e na altura atual.
	 */
	public double getScale() {
		return scale;
	}

	/**
	 * Seta a escala atual.
	 */
	public void setScale(double scale) {
		this.scale = scale;
	}

	/**
	 * Retorna a largura atual da tela.
	 */
	public double getActualWidth() {
		return actualWidth;
	}

	/**
	 * Seta a largura atual da tela.
	 */
	public void setActualWidth(double actualWidth) {
		this.actualWidth = actualWidth;
	}

	/**
	 * Retorna um vetor com a posição de diferença, ou seja, o quão a camera está afastada do ponto (0,0) da tela.
	 */
	public Vector2D getPositionDiference() {
		return positionDiference;
	}
	
	/**
	 * Seta a posição de diferença.
	 */
	public void setPositionDiference(Vector2D positionDiference) {
		this.positionDiference.set(positionDiference);
	}
	
	/**
	 * Retorna o elemento que está sendo seguido pela camera.
	 */
	public GameElement getElement() {
		return element;
	}

	/**
	 * Seta o elemento a ser seguido pela camera.
	 */
	public void setElement(GameElement element) {
		this.element = element;
	}
}
