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
import java.util.concurrent.ConcurrentHashMap;

import android.graphics.Bitmap;

/**
 * Classe responsável por bufferizar imagens a serem usados no jogo.
 * @author Wiliams Magalhães Primo
 */

public class ImageBuffer {
	protected static Bitmap bitmap;
	
	/**
	 * Hash com as imagens carregadas no jogo.
	 */
	private static ConcurrentHashMap <String , Bitmap> images = new ConcurrentHashMap <String, Bitmap>();
	
	/**
	 * Carrega uma nova imagem.
	 */
	public static void addImage(String imageName, Bitmap image){
		images.put(imageName, image);
	}
	
	/**
	 * Remove uma imagem.
	 */
	public static void removeImage(String imageName){
		images.remove(imageName);
	}
	
	/**
	 * Remove todas as imagens.
	 */
	public static void removeAllImages(){
		images.clear();
	}
	
	/**
	 * Retorna uma imagens baseando-se em uma String de nome.
	 */
	public static Bitmap getImage(String imageName){
		for(int i = 0; i < 100; i++){
			if((bitmap = images.get(imageName)) != null){
				return bitmap;
			}
		}
		return images.get(imageName);
	}
	
}
