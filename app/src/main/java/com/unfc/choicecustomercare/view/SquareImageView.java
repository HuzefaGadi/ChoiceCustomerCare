/**
 *
 */
package com.unfc.choicecustomercare.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

/**
 * @author ubuntu
 * 
 */
public class SquareImageView extends CustomImageButton {

	public SquareImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Drawable d = getDrawable();

		if (d != null) {

			setMeasuredDimension(heightMeasureSpec, heightMeasureSpec);
		} else {

			super.onMeasure(heightMeasureSpec, heightMeasureSpec);
		}
	}
}
