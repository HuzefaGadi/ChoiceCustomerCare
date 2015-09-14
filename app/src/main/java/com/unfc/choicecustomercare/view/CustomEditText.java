package com.unfc.choicecustomercare.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * 1/19/15.
 */
public class CustomEditText extends EditText {

	public CustomEditText(Context context) {
		super(context);
		setFont();
	}

	public CustomEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFont();
	}

	public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		setFont();
	}

	public void setFont() {

		if (isInEditMode()) {

			return;
		}

		int style = 0;
		Typeface typeFace = getTypeface();
		if (typeFace != null) {

			style = typeFace.getStyle();
		}

		switch (style) {
			case 1 :

				typeFace = Typeface.createFromAsset(getContext().getAssets(), "font/SansSerifFLF-Bold.otf");
				break;
			case 2 :

				typeFace = Typeface.createFromAsset(getContext().getAssets(), "font/SansSerifFLF-Italic.otf");
				break;
			case 3 :

				typeFace = Typeface.createFromAsset(getContext().getAssets(), "font/SansSerifFLF-BoldItalic.otf");
				break;
			default :

				typeFace = Typeface.createFromAsset(getContext().getAssets(), "font/SansSerifFLF.otf");
				break;
		}

		setTypeface(typeFace);
	}
}
