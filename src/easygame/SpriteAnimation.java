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
 * Essa classe é usada para criar animações para sprites.
 * @author Willians Magalhães Primo
 */

public class SpriteAnimation {
	
	/**
	 * Sem animação
	 */
	public static final SpriteAnimation NONE = null;
	
	private float firstFrame = 0.0f;
	private float lastFrame = 0.0f;
	private float transitionVelocity = 0.0f;
	
	public SpriteAnimation(float firstFrame, float lastFrame, float transitionVelocity){
		this.firstFrame = firstFrame;
		this.lastFrame = lastFrame;
		this.transitionVelocity = transitionVelocity;
	}
	
	/**
	 * Retorna o proximo frame da animação baseando-se no frame atual.
	 */
	public float getNextFrame(float frame){
		frame += transitionVelocity;
		if(frame > lastFrame || frame < firstFrame){
			frame = firstFrame;
		}
		return frame;
	}
	
	/**
	 * Retorna o primeiro frame da animação.
	 */
	public float getFirstFrame() {
		return firstFrame;
	}
	
	/**
	 * Seta o primeiro frame da animação.
	 */
	public void setFirstFrame(float firstFrame) {
		this.firstFrame = firstFrame;
	}
	
	/**
	 * Retorna o primeiro o ultimo frama da animação.
	 */
	public float getLastFrame() {
		return lastFrame;
	}
	
	/**
	 * Seta o último frame da animação.
	 */
	public void setLastFrame(float lastFrame) {
		this.lastFrame = lastFrame;
	}
	
	/**
	 * Retorna a velocidade de transição da animação.
	 */
	public float getTransitionVelocity() {
		return transitionVelocity;
	}
	
	/**
	 * Seta a velocidade de transição da animação.
	 */
	public void setTransitionVelocity(float transitionVelocity) {
		this.transitionVelocity = transitionVelocity;
	}
}
