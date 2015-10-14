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

/**
 * Essa classe representa um componente de layout com características de tabela, organizando os elementos em linhas e colunas.
 * @author Willians Magalhães Primo.
 */
public class TableLayout extends LayoutView{
	private double columnWidth = 0.0f,
				   lineHeight = 0.0f;
	private int columns = 0;
	
	public TableLayout(double columnWidth, double lineHeight, int columns){
		this.columnWidth = columnWidth;
		this.lineHeight = lineHeight;
		this.columns = columns;
	}
	
	public TableLayout(){
		this.columns = 1;
	}
	
	/**
	 * Retorna o número de colunas do layout.
	 */
	public int getColumns() {
		return columns;
	}
	
	/**
	 * Seta o número de colunas do Layout.
	 */
	public void setColumns(int columns) {
		this.columns = columns;
	}

	/**
	 * Retorna a largura das colunas do layout.
	 */
	public double getColumnWidth() {
		return columnWidth;
	}

	/**
	 * Seta a largura das colunas do lauout.
	 */
	public void setColumnWidth(double columnWidth) {
		this.columnWidth = columnWidth;
	}

	/**
	 * Retorna a altura das linahs do layout.
	 */
	public double getLineHeight() {
		return lineHeight;
	}

	/**
	 * Seta a altura das linhas do layout.
	 */
	public void setLineHeight(double lineHeight) {
		this.lineHeight = lineHeight;
	}
	
	/**
	 * Seta a largura do layout.
	 */
	@Override
	public void setWidth(double width) {
		super.setWidth(width);
	}
	
	/**
	 * Adiciona um elemento no layout.
	 */
	@Override
	public void addElement(GameElement element) {
		int numberOfElements = getElements().size();
		double x = (numberOfElements % columns)*columnWidth + columnWidth/2;
		double y = (numberOfElements / columns)*lineHeight + lineHeight/2;
		element.setPosition(x, y);
		super.addElement(element);
	}
	
	/**
	 * Atualiza o layout de todos os elementos.
	 */
	public void refresh(){
		List<GameElement> elements = new ArrayList<GameElement>(getElements());
		setElements(elements);
	}
	
}
