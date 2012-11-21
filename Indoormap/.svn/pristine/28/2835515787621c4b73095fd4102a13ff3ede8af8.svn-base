package com.softwaremaestro.indoormap.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

public class GridOverlayView extends ImageView {
	public GridOverlayView(Context context) {
		this(context, null);
	}

	public GridOverlayView(Context context, int height, int width) {
		this(context, null);
	}

	public GridOverlayView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBackgroundColor(Color.TRANSPARENT);
		setScaleType(ScaleType.MATRIX);
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom)
	{
		super.onLayout(changed, left, top, right, bottom);
	}

	public void onDraw(Canvas c) {
		super.onDraw(c);
		float[] value = new float[9];
		getImageMatrix().getValues(value);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		paint.setAlpha(70);
		int width = getDrawable().getIntrinsicWidth();
		int height = getDrawable().getIntrinsicHeight();
		for(float i=value[2]+StaticValue.GRID_SIZE*0.5f*value[0];i<width;i+=StaticValue.GRID_SIZE*value[0])
				c.drawLine(i, 0, i, height, paint);
		for(float i=value[5]+StaticValue.GRID_SIZE*0.5f*value[0];i<height;i+=StaticValue.GRID_SIZE*value[0])
			c.drawLine(0, i, width, i, paint);
	}

}
