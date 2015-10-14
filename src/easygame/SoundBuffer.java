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
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * Essa clcasse serve como buffer e player de audios.
 * @author Willians Magalhães Primo
 */

public class SoundBuffer {
	
	/**
	 * "Lista" com todos os sons adicionados.
	 */
    private static ConcurrentHashMap <String, Integer> sounds = new ConcurrentHashMap <String, Integer>();
	
	private static SoundPool soundPlayer = new SoundPool(1000, AudioManager.STREAM_MUSIC, 100);
	
	/**
	 * Retorna todos os sons carregados até o momento.
	 */
	public ConcurrentHashMap <String, Integer> getSongs() {
		return sounds;
	}

	/**
	 * Seta um conjunto do soundBufeer.
	 */
	public static void setSounds(ConcurrentHashMap <String, Integer> sounds) {
		removeAll();
		SoundBuffer.sounds = sounds;
	}
	
	/**
	 * Carrega um novo son.
	 */
	public static void addSound(String soundName, int soundId, Context context){
		int sound = soundPlayer.load(context, soundId, 1);
		sounds.put(soundName, sound);
	}
	
	/**
	 * Remove um som baseando-se em uma String de nome.
	 */
	public static void removeSound(String soundName){
		Integer sound = sounds.get(soundName);
		if(sound != null){
			soundPlayer.unload(sound);
			sounds.remove(soundName);
		}
	}
	
	/**
	 * Remove todos os sons.
	 */
	public static  void removeAll(){
		sounds.clear();
		soundPlayer.release();
		soundPlayer = new SoundPool(1000, AudioManager.STREAM_MUSIC, 100);
	}
	
	/**
	 * Toca um son.
	 */
	public static void playSound(String soundName, Context context){
		playSound(soundName, 1, context);
	}
	
	/**
	 * Toca um son.
	 */
	public static void playSound(String soundName, int loop, Context context){
		playSound(soundName, 1.0f, 1.0f, loop, context);
	}
	
	/**
	 * Toca um son.
	 */
	public static void playSound(String soundName, float volume, float velocity, int loop, Context context){
		Integer sound = sounds.get(soundName);
		if(sound != null){
			soundPlayer.play(sound, volume, volume, 0, loop, velocity);
		}
	}
	
	/**
	 * Para a execução de um son.
	 */
	public void stopSound(String soundName){
		Integer sound = sounds.get(soundName);
		if(sound != null){
			soundPlayer.stop(sound);
		}
	}
	
	/**
	 * Para a execução de todos os sons.
	 */
	public void stopAllSounds(){
		soundPlayer.autoPause();
	}
}
