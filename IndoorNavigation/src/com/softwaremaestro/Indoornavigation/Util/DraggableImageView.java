package com.softwaremaestro.Indoornavigation.Util;

import kr.softwaremaestro.indoor.wrm.vo.Point;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class DraggableImageView extends ImageView implements OnTouchListener {

	private float xCur = 0;
	private float yCur = 0;
	private static final String TAG = "ViewTouchImage";
	// ///////////////////////////////////////////////////
	private static final boolean D = false;

	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	private Matrix savedMatrix2 = new Matrix();
	private DisplayMetrics dmetric;

	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int ZOOM = 2;
	private static final int CLICK = 3;
	private int mode = NONE;

	private PointF start = new PointF();
	private PointF mid = new PointF();
	private float oldDist = 1f;

	private static final int WIDTH = 0;
	private static final int HEIGHT = 1;

	private boolean isInit = false;
	boolean enable;

	private Handler handler;

	float scale;

	public DraggableImageView(Context context) {
		this(context, null);
	}

	public DraggableImageView(Context context, int height, int width) {
		this(context, null);
	}

	public DraggableImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnTouchListener(this);
		setScaleType(ScaleType.MATRIX);
		enable = true;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		if (D)
			Log.e(TAG, "onLayout");
		super.onLayout(changed, left, top, right, bottom);
		if (isInit == false) {
			Log.e(TAG, "onLayout : isInit");
			init();
			isInit = true;
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if (enabled)
			enable = true;
		else
			enable = false;
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		Message msg = handler.obtainMessage();
		msg.what = StaticValue.MSG_INIT_IMAGEVIEW;
		int[] info = new int[4];
		info[0] = bm.getWidth();
		info[1] = bm.getHeight();
		info[2] = getDrawable().getIntrinsicWidth();
		info[3] = getDrawable().getIntrinsicHeight();
		msg.obj = info;
		handler.sendMessage(msg);
		scale = getDrawable().getIntrinsicWidth() / bm.getWidth();
		init();
	}

	protected void init() {
		float[] value = new float[9];
		value[0] = (float) 0.67;
		value[1] = (float) 0.0;
		value[2] = (float) -236.55458;

		value[3] = (float) 0.0;
		value[4] = (float) 0.67;
		value[5] = (float) 3.0;

		value[6] = (float) 0.0;
		value[7] = (float) 0.0;
		value[8] = (float) 1.0;

		matrix.setValues(value);

		matrixTurning(matrix, this);

		syncMatrix();

		// setImagePit();
	}

	public void SetImagePos(float xPos, float yPos) {
		float[] value = new float[9];

		value[0] = (float) 1.8331604;
		value[1] = (float) 0.0;
		value[2] = xPos;

		value[3] = (float) 0.0;
		value[4] = (float) 1.8331604;
		value[5] = yPos;

		value[6] = (float) 0.0;
		value[7] = (float) 0.0;
		value[8] = (float) 1.0;

		Log.e("setimage", "xPos/yPos : " + xPos + "/" + yPos);

		matrix.setValues(value);

		matrixTurning(matrix, this);

		syncMatrix();
	}

	private void matrixTurning(Matrix matrix, ImageView view) {
		// ��Ʈ���� ��
		float[] value = new float[9];
		matrix.getValues(value);
		float[] savedValue = new float[9];
		savedMatrix2.getValues(savedValue);

		// �� ũ��
		int width = view.getWidth();
		int height = view.getHeight();

		// �̹��� ũ��
		Drawable d = view.getDrawable();
		if (d == null)
			return;
		int imageWidth = d.getIntrinsicWidth();
		int imageHeight = d.getIntrinsicHeight();
		int scaleWidth = (int) (imageWidth * value[0]);
		int scaleHeight = (int) (imageHeight * value[4]);

		// �̹����� �ٱ����� ������ �ʵ���.
		if (value[2] < width - scaleWidth) {
			value[2] = width - scaleWidth;
		}
		if (value[5] < height - scaleHeight) {
			value[5] = height - scaleHeight;
		}
		if (value[2] > 0) {
			value[2] = 0;
		}
		if (value[5] > 0) {
			value[5] = 0;
		}

		// 3�� �̻� Ȯ�� ���� �ʵ���
		if (value[0] > 5 || value[4] > 5) {
			value[0] = savedValue[0];
			value[4] = savedValue[4];
			value[2] = savedValue[2];
			value[5] = savedValue[5];
		}
		// �ּҹ��� ����
		float minScaleX = (float) width / (float) imageWidth;
		float minScaleY = (float) height / (float) imageHeight;
		float minScale = minScaleX > minScaleY ? minScaleX : minScaleY;
		if (value[0] < minScale) {
			value[0] = minScale;
			value[4] = minScale;
			value[2] = savedValue[2];
			value[5] = savedValue[5];
		}

		// �׸��� ��� ��ġ�ϵ��� �Ѵ�.
		scaleWidth = (int) (imageWidth * value[0]);
		scaleHeight = (int) (imageHeight * value[4]);
		if (scaleWidth < width) {
			value[2] = (float) width / 2 - (float) scaleWidth / 2;
		}
		if (scaleHeight < height) {
			value[5] = (float) height / 2 - (float) scaleHeight / 2;
		}

		matrix.setValues(value);
		savedMatrix2.set(matrix);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction() & MotionEvent.ACTION_MASK;
		if (!enable)
			return false;
		xCur = event.getX();
		yCur = event.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			savedMatrix.set(matrix);
			start.set(event.getX(), event.getY());

			mode = DRAG;

			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			oldDist = spacing(event);
			if (oldDist > 10f) {
				savedMatrix.set(matrix);
				midPoint(mid, event);
				mode = ZOOM;
			}
			break;
		case MotionEvent.ACTION_UP:
			float x = xCur - start.x;
			float y = yCur - start.y;
			float dist = FloatMath.sqrt(x * x + y * y);
			if (dist < 10f) {
				float[] value = new float[9];
				matrix.getValues(value);
				Message msg = handler.obtainMessage();
				msg.what = StaticValue.MSG_CLICK_VIEW;
				Bundle bundle = new Bundle();
				bundle.putFloat("X", (float)(Math.abs(value[2]) + xCur)/ value[0]/scale/dmetric.density );
				bundle.putFloat("Y", (float)(Math.abs(value[5]) + yCur)/ value[0]/scale/dmetric.density);
				msg.setData(bundle);
				handler.sendMessage(msg);
			}
			mode = NONE;
			break;
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			break;
		case MotionEvent.ACTION_MOVE:

			if (mode == DRAG) {
				matrix.set(savedMatrix);
				matrix.postTranslate(event.getX() - start.x, event.getY()
						- start.y);
			} else if (mode == ZOOM) {
				float newDist = spacing(event);
				if (newDist > 10f) {
					matrix.set(savedMatrix);
					float scale = newDist / oldDist;
					matrix.postScale(scale, scale, mid.x, mid.y);
				}
			}

			break;
		}

		matrixTurning(matrix, this);

		syncMatrix();

		return true;
	}

	public void onDraw(Canvas c) {
		super.onDraw(c);
	}

	public void focusToPoint(Point position) {
		float[] value = new float[9];
		matrix.getValues(value);
		matrix.set(savedMatrix);
		value[0] = 1.0f;
		value[4] = 1.0f;
		value[2] = -1 * position.getX().floatValue() * scale * value[0]
				* dmetric.density + getWidth() / 2;
		value[5] = -1 * position.getY().floatValue() * scale * value[0]
				* dmetric.density + getHeight() / 2;
		matrix.setValues(value);
		matrixTurning(matrix, this);
		syncMatrix();
	}

	/** Show an event in the LogCat view, for debugging */
	private void dumpEvent(MotionEvent even� ũ��
		int width = view.getWidth();
		int height = view.getHeight();

		// �̹��� ũ��
		Drawable d = view.getDrawable();
		if (d == null)
			return;
		int imageWidth = d.getIntrinsicWidth();
		int imageHeight = d.getIntrinsicHeight();
		int scaleWidth = (int) (imageWidth * value[0]);
		int scaleHeight = (int) (imageHeight * value[4]);

		// �̹����� �ٱ����� ������ �ʵ���.
		if (value[2] < width - scaleWidth) {
			value[2] = width - scaleWidth;
		}
		if (value[5] < height - scaleHeight) {
			value[5] = height - scaleHeight;
		}
		if (value[2] > 0) {
			value[2] = 0;
		}
		if (value[5] > 0) {
			value[5] = 0;
		}

		// 3�� �̻� Ȯ�� ���� �ʵ���
		if (value[0] > 5 || value[4] > 5) {
			value[0] = savedValue[0];
			value[4] = savedValue[4];
			value[2] = savedValue[2];
			value[5] = savedValue[5];
		}
		// �ּҹ��� ����
		float minScaleX = (float) width / (float) imageWidth;
		float minScaleY = (float) height / (float) imageHeight;
		float minScale = minScaleX > minScaleY ? minScaleX : minScaleY;
		if (value[0] < minScale) {
			value[0] = minScale;
			value[4] = minScale;
			value[2] = savedValue[2];
			value[5] = savedValue[5];
		}

		// �׸��� ��� ��ġ�ϵ��� �Ѵ�.
		scaleWidth = (int) (imageWidth * value[0]);
		scaleHeight = (int) (imageHeight * value[4]);
		if (scaleWidth < width) {
			value[2] = (float) width / 2 - (float) scaleWidth / 2;
		}
		if (scaleHeight < height) {
			value[5] = (float) height / 2 - (float) scaleHeight / 2;
		}

		matrix.setValues(value);
		savedMatrix2.set(matrix);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction() & MotionEvent.ACTION_MASK;
		if (!enable)
			return false;
		xCur = event.getX();
		yCur = event.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			savedMatrix.set(matrix);
			start.set(event.getX(), event.getY());

			mode = DRAG;

			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			oldDist = spacing(event);
			if (oldDist > 10f) {
