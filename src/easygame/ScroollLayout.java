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

import android.view.MotionEvent;

/**
 * Classe usada para criação de layouts com rolagem.
 * @author Willians Magalhães Primo.
 */
public class ScroollLayout extends LayoutView{
	private double scrollFactor = 10;
	private double contentHeight = 0;
	private double space = 10;

	protected double scroll = 0;

	/**
	 * Tratamento dos eventos de cliqque.
	 */
	@Override
	public boolean touchEvents(Properties properties, MotionEvent event, GameCamera camera) {
		if(event.getAction() == MotionEvent.ACTION_MOVE && contentHeight > getHeight() && getScene().getLastEvent2() != null){
			if(getScene().getLastEvent2().getY() > getScene().getLastEvent1().getY() && scroll > 0){
				for(GameElement element:getElements()){
					element.getPosition().addTo(0.0f, Math.min(scroll, scrollFactor));
				}
				scroll -= Math.min(scroll, scrollFactor);
			}else if(scroll < contentHeight - getHeight()){
				for(GameElement element:getElements()){
					element.getPosition().addTo(0.0f, -Math.min(Math.abs(scroll - contentHeight - getHeight()), scrollFactor));
				}
				scroll += Math.min(Math.abs(scroll - contentHeight - getHeight()), scrollFactor);
			}
		}
		return super.touchEvents(properties, event, camera);
	}
	
	/**
	 * Adição de um novo elemento.
	 */
	@Override
	public void addElement(GameElement element) {
		if(getElements().size() == 0){
			element.setBasePosition(0, space);
			contentHeight = element.getHeight() + space;
		}else{
			GameElement lastElement = getElements().get(getElements().size() - 1);
			element.setBasePosition(0, lastElement.getBasePosition().getY() + lastElement.getHeight() + space);
			contentHeight += element.getHeight() + space;
		}
		super.addElement(element);
	}
	
	/**
	 * Adiciona um conjunto de elementos.
	 */
	@Override
	public void setElements(List<GameElement> elements) {
		this.getElements().clear();
		scroll = 0;
		for(GameElement element:elements){
			addElement(element);
		}
	}
	
	/**
	 * Atualiza o layout de todos os elementos.
	 */
	public void refresh(){
		List<GameElement> elements = new ArrayList<GameElement>(getElements());
		setElements(elements);
	}

	/**
	 * Retorna a altura do conteudo do layout.
	 */
	public double getContentHeight() {
		return contentHeight;
	}

	/**
	 * Seta a altura do conteudo do layout.
	 */
	public void setContentHeight(double contentHeight) {
		this.contentHeight = contentHeight;
	}

	/**
	 * Retorna o fator de rolagem.
	 */
	public double getScrollFactor() {
		return scrollFactor;
	}

	/**
	 * Seta o fator de rolagem.
	 */
	public void setScrollFactor(double scrollFactor) {
		this.scrollFactor = scrollFactor;
	}

	/**
	 * Retorna o espaço entre os elementos da lista.
	 */
	public double getSpace() {
		return space;
	}

	/**
	 * Seta o espaço entre os elementos da lista.
	 */
	public void setSpace(double space) {
		this.space = space;
	}
}
