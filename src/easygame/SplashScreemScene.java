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

import java.io.IOException;
import java.io.InputStream;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

/**
 * Essa classe representa a tela de carregamento de todo jogo criado com o auxilio da EasyGame.
 * @author Willians Magalhães Primo.
 */
public class SplashScreemScene extends Scene{
	private Scene mainScene;
	private int timeOfLife;
	
	//Construtor
	public SplashScreemScene(Scene mainScene){
		this.mainScene = mainScene;
		this.timeOfLife = 100;
	}
	
	/**
	 * Função de carregamento.
	 */
	@Override
	protected void onLoad() {
		loadImage("img_easygame_logo", getEasyGameLogo());
		
		setBackGroundColor(Color.WHITE);
		
		SimpleSprite easyGameLogo = new SimpleSprite("img_easygame_logo");
		easyGameLogo.setSize(350.0f , 0.0f);
		easyGameLogo.setPosition(getWidth()/2, getHeight()/2);
		
		TextDrawable autor = new TextDrawable("Willians Magalhães Primo");
		autor.setTextSize(25);
		autor.setPosition(getWidth()/2, easyGameLogo.getBasePosition().getY() + easyGameLogo.getHeight() + autor.getHeight()/2.0f + 10.0f);
				
		addElement(easyGameLogo);
		addElement(autor);
	}
	
	/**
	 * Retorna a imagem logo da EasyGame.
	 */
	private Bitmap getEasyGameLogo(){
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("easygame/img_easygame_logo.png");
		Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
		try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	
	/**
	 * Função de atualização.
	 */
	@Override
	protected void onUpdate() {
		super.onUpdate();
		this.timeOfLife--;
		if(timeOfLife < 0){
			loadScene(mainScene);
		}
	}
	
	/**
	 * Retorna a cena principal.
	 */
	public Scene getMainScene() {
		return mainScene;
	}

	/**
	 * Seta cena principal.
	 */
	public void setMainScene(Scene mainScene) {
		this.mainScene = mainScene;
	}

	public int getTimeOfLife() {
		return timeOfLife;
	}

	public void setTimeOfLife(int timeOfLife) {
		this.timeOfLife = timeOfLife;
	}
}
