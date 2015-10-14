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
import android.view.MotionEvent;

/**
 * Essa classe é utilizada para verificar se houve toque em algum elemento.
 * @author Willians Magalhães Primo
 */

public class TouchVerifier {
	protected static Vector2D touchPosition = new Vector2D(0.0f, 0.0f),
			                  elementPosition = new Vector2D(0.0f, 0.0f);
	/**
	 * Verifica se o elemento foi tocado.
	 */
	public static boolean touched(MotionEvent event, Properties properties){
		touchPosition.set(event.getX(), event.getY());
		elementPosition.set(properties.getPosition());
		
		if(properties.getRotation() != 0){
			touchPosition.rotate(-properties.getRotation());
			elementPosition.rotate(-properties.getRotation());
		}
		
		
		if(touchPosition.getX() < (elementPosition.getX() - properties.getWidth()/2)){
			return false;
		}
		
		if(touchPosition.getX() > (elementPosition.getX() + properties.getWidth()/2)){
			return false;
		}
		
		if(touchPosition.getY() < (elementPosition.getY() - properties.getHeight()/2)){
			return false;
		}
		
		if(touchPosition.getY() > (elementPosition.getY() + properties.getHeight()/2)){
			return false;
		}
		return true;
	}
}
