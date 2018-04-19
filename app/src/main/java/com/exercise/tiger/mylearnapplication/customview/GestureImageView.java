package com.exercise.tiger.mylearnapplication.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;

import com.exercise.tiger.mylearnapplication.R;
import com.exercise.tiger.mylearnapplication.utils.GlobalConstant;

/**
 * create by hzj on 2018/4/18
 **/
public class GestureImageView extends AppCompatImageView implements ViewTreeObserver.OnGlobalLayoutListener {

    private static final String TAG = GestureImageView.class.getSimpleName();
    private boolean isOnce = true;
    //初始缩放值
    private float currentScale;
    private static int mWidth = GlobalConstant.getDeviceWidth();
    private static int mHeight = GlobalConstant.getDeviceHeight();
    private static int mDistance = (int) Math.sqrt((Math.pow(mWidth, 2) + Math.pow(mHeight, 2)));
    private float MAX_SCALE = 4.0f; // 修改这个值可以修改缩放最大值
    private float MIN_SCALE;
    private float[] mMatrixValues = new float[9];
    private float[] initMatrixValues = new float[9];
    private Matrix mMatrix = new Matrix();
    private PointF scalePointF;
    private PointF dragPointF;
    private float preDistance = 0;
    private static final int SCALE_RATE = 4;//修改此值用以修改缩放速率

    public GestureImageView(Context context) {
        super(context);
        initImageAttribute();
    }

    public GestureImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initImageAttribute();
    }

    public GestureImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initImageAttribute();
    }

    /**
     * 初始化图像属性
     */
    private void initImageAttribute() {
        setScaleType(ScaleType.MATRIX);
        setFocusable(true);// 触摸事件开启此参数用以搞事情
        catchTouchEvent(true);
    }

    /**
     * 是否捕获touch事件
     *
     * @param flag true捕获touch事件，false不捕获
     */
    public void catchTouchEvent(boolean flag) {
        if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(flag);
        }
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getViewTreeObserver().removeOnGlobalLayoutListener(this);
        } else {
            getViewTreeObserver().addOnGlobalLayoutListener(null);
        }
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        adjustImageMatrix();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        adjustImageMatrix();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                catchTouchEvent(true);
                scalePointF = null;
                preDistance = 0;
                dragPointF = null;
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == 2) { //两点触摸事件处理
                    handleScaleEvent(event);
                }
                if (event.getPointerCount() == 1) { //处理移动事件
                    handleDragEvent(event);
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                if (event.getPointerCount() != 1) {
                    scalePointF = null;
                    preDistance = 0;
                    dragPointF = null;
                }
                break;
            case MotionEvent.ACTION_UP:
                dragPointF = null;
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 处理拖拽手势
     *
     * @param event 触摸Event
     */
    private void handleDragEvent(MotionEvent event) {
        scalePointF = null;
        if (dragPointF == null) {
            dragPointF = new PointF(event.getX(), event.getY());
        } else {
            float x = event.getX();
            float y = event.getY();
            drag(x - dragPointF.x,
                    y - dragPointF.y);
            dragPointF = new PointF(x, y);
        }
    }

    /**
     * 处理缩放手势
     *
     * @param event 事件
     */
    private void handleScaleEvent(MotionEvent event) {
        dragPointF = null;
        float fX = event.getX(0);
        float fY = event.getY(0);
        float sX = event.getX(1);
        float sY = event.getY(1);
        float distance = (float) Math.sqrt((Math.pow(fX - sX, 2) + Math.pow(fY - sY, 2)));
        if (scalePointF == null) {
            scalePointF = new PointF((fX + sX) / 2.0f, (fY + sY) / 2.0f);
            preDistance = distance;
        } else {
            float scale = (distance - preDistance) / mDistance * SCALE_RATE;//修改这个值可以更改缩放速率
            float judge = currentScale + scale;
            scale = judge > MAX_SCALE || judge < MIN_SCALE ?
                    (judge > MAX_SCALE ? MAX_SCALE - currentScale : MIN_SCALE - currentScale) :
                    scale;
            currentScale += scale;
            scale(currentScale / (currentScale - scale), scalePointF);
        }
        preDistance = distance;
    }

    /**
     * 根据图像矩形处理拖拽的X值
     *
     * @param rect  图像矩形
     * @param dragX 拖拽的X值
     * @return 处理以后dragX值
     */
    private float handleDragX(RectF rect, float dragX) {
        if (rect.left < 0 || rect.right > mWidth) { // 放大时，如果内容宽度大于屏幕，左右边界不能出现黑边
            if (rect.left + dragX > 0) {
                dragX = 0 - rect.left;
            } else if (rect.right + dragX < mWidth) {
                dragX = mWidth - rect.right;
            }
        } else {
            dragX = 0;
        }
        return dragX;
    }

    /**
     * 根据图像矩形处理拖拽的Y值
     *
     * @param rect  图像矩形
     * @param dragY 拖拽的Y值
     * @return 处理以后dragY值
     */
    private float handleDragY(RectF rect, float dragY) {
        if (rect.top <= 0 && rect.bottom >= mHeight) { // 放大时，如果内容宽度大于屏幕，左右边界不能出现黑边
            if (rect.top + dragY > 0) {
                dragY = 0 - rect.top;
            } else if (rect.bottom + dragY < mHeight) {
                dragY = mHeight - rect.bottom;
            }
        } else {
            dragY = 0;
        }
        return dragY;
    }

    /**
     * 拖拽imageView
     *
     * @param dragX X轴的移动距离
     * @param dragY Y轴的移动距离
     */
    private void drag(float dragX, float dragY) {
        RectF rect = getImageRectF();
        catchTouchEvent(canDrag(rect));
        dragX = handleDragX(rect, dragX);
        dragY = handleDragY(rect, dragY);
        mMatrix.postTranslate(dragX, dragY);
        setImageMatrix(mMatrix);
        mMatrix.getValues(mMatrixValues);
    }

    /**
     * 根据ImageView当前位置判断是否可以拖拽
     *
     * @param currentRect 当前的矩形
     * @return true可以拖拽，false不可以
     */
    private boolean canDrag(RectF currentRect) {
        RectF preRect = getInitialRectF();
        boolean canDrag;
        if (preRect.left > 0) {
            canDrag = !(currentRect.left >= 0 || currentRect.right <= mWidth || (currentRect.left > 0 && currentScale == MAX_SCALE));
        } else {
            canDrag = !(currentRect.left == 0 || currentRect.right == mWidth);
        }
        return canDrag;
    }

    /**
     * 获取初始的图像矩形
     *
     * @return
     */
    private RectF getInitialRectF() {
        Matrix preMatrix1 = new Matrix();
        preMatrix1.setValues(initMatrixValues);
        return getImageRectF(preMatrix1);
    }

    /**
     * 缩放时，处理边缘不过界
     *
     * @param matrix 拷贝的矩阵
     * @param scale  缩放比例
     * @param f      缩放中心点
     * @return 返回X坐标的点
     */
    private float handleScaleDragX(Matrix matrix, float scale, PointF f) {
        matrix.postScale(scale, scale, f.x, mHeight / 2.0f);
        RectF rect = getImageRectF(matrix); // 根据拷贝过后的矩阵计算得出的矩形
        RectF preRect = getInitialRectF();
        float x = f.x;
        if (preRect.left > 0) { // 图片初始矩形宽度小于屏幕宽度
            x = rect.left > 0 || rect.right < mWidth ? mWidth / 2.0f : x;
        } else {
            x = rect.left > 0 ? 0 : (rect.right < mWidth ? mWidth : x);
        }
        return x;
    }

    /**
     * 按照缩放比例和点进行缩放
     *
     * @param scale 缩放比例
     * @param f     包含缩放中心点的PointF
     */
    private void scale(float scale, PointF f) {
        Log.d("缩放的比率：scale", scale + "");
        if (scale <= (float) 1.00) {
//            scale=(float) 1.00;
        }
        Matrix matrix = new Matrix(mMatrix);
        mMatrix.postScale(scale, scale, handleScaleDragX(matrix, scale, f), mHeight / 2.0f);
        mMatrix.getValues(mMatrixValues);
//        getImageRectF();
        if (currentScale == MIN_SCALE) {// 缩放到初始位置，将图片设置为初始位置,此处可以添加动画，请随意发挥。
            mMatrix.setValues(initMatrixValues);
            setImageMatrix(mMatrix);
        }
        Log.d(TAG, "currentScale = " + currentScale);
        setImageMatrix(mMatrix);

        //对缩放完成后的黑边处理，如果当前图片宽度或高度大于屏幕，则不留黑边，否则在两端留下相同宽度的黑边
        RectF imgRect = getImageRectF();
        float translateX = 0f;
        float translateY = 0f;
        if (imgRect.width() > mWidth) {
            translateX = (imgRect.left > 0 ? -imgRect.left : imgRect.right < mWidth ? mWidth - imgRect.right : 0f)/2;
        }else {
            translateX = (mWidth - imgRect.right -imgRect.left) /2;
        }

        if (imgRect.height() > mHeight){
            translateY = (imgRect.top > 0 ? -imgRect.top : imgRect.bottom < mHeight ? mHeight - imgRect.bottom : 0f)/2;
        }else {
            translateY = (mHeight - imgRect.bottom - imgRect.top)/2;
        }

        mMatrix.postTranslate(translateX,translateY);
        setImageMatrix(mMatrix);
    }

    /**
     * 将图像调整到适合的位置
     */
    private void adjustImageMatrix() {
        Drawable d = getDrawable();
        if (d == null) {
            setImageResource(R.mipmap.ic_launcher);
            return;
        }
        int width = d.getIntrinsicWidth();
        int height = d.getIntrinsicHeight();
        float scale = 1.0f;
        // 为了图片完全显示，判断加载图片与屏幕的宽高比，如果图片的大，则按照图片与屏幕的宽度比例得到初始缩放比例，反之按照高度的比例得到初始缩放
        float imgRatio = width * 1.0f / height;
        float screenRatio = mWidth * 1.0f / mHeight;
        if (imgRatio > screenRatio) {
            scale = mWidth * 1.0f / width;
        } else {
            scale = mHeight * 1.0f / height;
        }
        if (scale > 1) {
            MAX_SCALE = scale * 2;
        } else {
            MAX_SCALE = 4.0f;
        }
//        if (width > mWidth && height < mHeight) {
//            scale = mWidth * 1.0f / width;
//        }
//        if (height > mHeight && width < mWidth) {
//            scale = mHeight * 1.0f / height;
//        }
//        if (height > mHeight && width > mWidth) {
//            scale = Math.min(mWidth * 1.0f / width, mHeight * 1.0f / height);
//        }
        currentScale = scale;
        MIN_SCALE = scale;
        mMatrix = new Matrix();
        mMatrix.postTranslate((mWidth - width) / 2.0f, (mHeight - height) / 2.0f);
        mMatrix.postScale(currentScale, currentScale, mWidth / 2.0f, mHeight / 2.0f);
        mMatrix.getValues(mMatrixValues);
        mMatrix.getValues(initMatrixValues);
        setImageMatrix(mMatrix);
        getImageRectF();

    }

    /**
     * 返回Drawable的矩形
     *
     * @param matrix 拷贝出来的矩阵
     * @return imageView矩形数据
     */
    private RectF getImageRectF(Matrix matrix) {
        RectF rect = new RectF();
        Drawable d = getDrawable();
        if (d != null) {
            rect.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            matrix.mapRect(rect);
        }
        return rect;
    }

    /**
     * 返回Drawable的矩形
     *
     * @return imageView矩形数据
     */
    private RectF getImageRectF() {
        RectF rect = new RectF();
        Drawable d = getDrawable();
        if (d != null) {
            rect.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            mMatrix.mapRect(rect);
        }
        return rect;
    }

    @Override
    public void onGlobalLayout() {
        if (isOnce) {
            isOnce = false;
            adjustImageMatrix();
        }
    }
}
