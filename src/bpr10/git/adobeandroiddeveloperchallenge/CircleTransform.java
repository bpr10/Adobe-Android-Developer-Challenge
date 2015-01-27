package bpr10.git.adobeandroiddeveloperchallenge;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.squareup.picasso.Transformation;

/**
 * Created by julian on 13/6/21.
 */
public class CircleTransform implements Transformation {
	@Override
	public Bitmap transform(Bitmap source) {
		return null;
	}

	@Override
	public String key() {
		return "circle";
	}
}