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
import android.graphics.Canvas;

/**
 * Essa classe é usada como Loop de jogo.
 * @author Wiliams Magalhães Primo
 */
public class GameLoop extends Thread {
	
	//Propriedades do GameLoop
	GameView gameView;
	boolean running = false;
	
	//Construtores
	public GameLoop(GameView gameView){
		//Recebe o GameView a ser executado no Loop
		this.gameView = gameView;
	}
	
	/**
	 * Seta o loop como ativo ou desativado.
	 */
	public void setRunning(boolean state){
		this.running = state;
	}
	
	/**
	 * Esse é o metodo que será executado na thread do Loop do Jogo.
	 */
	@SuppressLint("WrongCall")
	public void run(){
		long ticksPS = 1000/gameView.getFPS();//Duvida
		long remaningTime, startTime;
		
		while(running){
			//Trava a edição do canvas do SufaceView e pega o Canvas deste para edição
			Canvas canvas = gameView.getHolder().lockCanvas();
			
			startTime = System.currentTimeMillis();
			//Essa parte serve para impedir que outra parte da aplicação, no caso os event listeners, acessem
			//O surface Holder enquanto os metos onDraw e onUpdade sejam executados
			try{
				gameView.update();
				gameView.onDraw(canvas);
			}finally{
				if(canvas != null){
					//Finalisa a edição do Canvas
					gameView.getHolder().unlockCanvasAndPost(canvas);
				}
			}
			
			//Faz a Thread dormir por o tempo restante
			remaningTime = ticksPS - (System.currentTimeMillis() - startTime);
			if(remaningTime > 0){
				try {
					sleep(remaningTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		/**
		 * Finaliza o gameView
		 */
		gameView.finalize();
	}
}
