package org.andresoviedo.android_3d_model_engine.animation;


import android.annotation.SuppressLint;
import android.util.Log;

/**
 * 
 * Represents an animation that can applied to an {@link org.andresoviedo.android_3d_model_engine.model.AnimatedModel} . It
 * contains the length of the animation in seconds, and a list of
 * {@link KeyFrame}s.
 * 
 * @author Karl
 * 
 *
 */
public class Animation {

	private final float length; //in seconds
	private final KeyFrame[] keyFrames;
	private boolean initialized;

	/**
	 * @param lengthInSeconds
	 *            - the total length of the animation in seconds.
	 * @param frames
	 *            - all the keyframes for the animation, ordered by time of
	 *            appearance in the animation.
	 */

	//mengandung animasi yang akan digunakan
	public Animation(float lengthInSeconds, KeyFrame[] frames) {
		this.keyFrames = frames;
		this.length = lengthInSeconds;

		Log.d("length", String.valueOf(lengthInSeconds));
	}

	public void setInitialized(boolean initialized){
		this.initialized = initialized;
	}

	public boolean isInitialized(){
		return initialized;
	}

	/**
	 * @return The length of the animation in seconds.
	 */
	public float getLength() {
		return length;
	}

	/**
	 * @return An array of the animation's keyframes. The array is ordered based
	 *         on the order of the keyframes in the animation (first keyframe of
	 *         the animation in array position 0).
	 */
	public KeyFrame[] getKeyFrames() {
		return keyFrames;
	}

}
