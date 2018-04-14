package com.exercise.tiger.mylearnapplication.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.exercise.tiger.mylearnapplication.utils.MoveGestureDetector;

import java.io.IOException;
import java.io.InputStream;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 查看大图自定义控件
 * create by hzj on 2018/4/14
 **/
public class LargeImageView extends View {
    private BitmapRegionDecoder mDecoder;
    /**
     * 图片的宽度和高度
     */
    private int mImageWidth, mImageHeight;
    /**
     * 绘制的区域
     */
    private volatile Rect mRect = new Rect();

    private MoveGestureDetector mDetector;
    private Bitmap tempBitmap;
    private boolean inited = false;


    private static final BitmapFactory.Options options = new BitmapFactory.Options();

    static {
        options.inPreferredConfig = Bitmap.Config.RGB_565;
    }

    public void setInputStream(InputStream is) {
        try {
            mDecoder = BitmapRegionDecoder.newInstance(is, false);
            mImageWidth = mDecoder.getWidth();
            mImageHeight = mDecoder.getHeight();
//            is.reset();
//            BitmapFactory.Options tmpOptions = new BitmapFactory.Options();
//            // Grab the bounds for the scene dimensions
//            tmpOptions.inJustDecodeBounds = true;
//            BitmapFactory.decodeStream(is, null, tmpOptions);
//            mImageWidth = tmpOptions.outWidth;
//            mImageHeight = tmpOptions.outHeight;
            requestLayout();
            invalidate();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                if (is != null) is.close();
            } catch (Exception e) {
            }
        }
    }


    public void init() {
        mDetector = new MoveGestureDetector(getContext(), new MoveGestureDetector.SimpleMoveGestureDetector() {
            @Override
            public boolean onMove(MoveGestureDetector detector) {
                final int moveX = (int) detector.getMoveX();
                int moveY = (int) detector.getMoveY();

                if (mImageWidth > getWidth()) {
                    Observable<String> observable = new Observable<String>() {
                        @Override
                        protected void subscribeActual(Observer<? super String> observer) {
                            mRect.offset(-moveX, 0);
                            checkWidth();
                            tempBitmap = mDecoder.decodeRegion(mRect, options);
                        }
                    };
                    Observer<String> observer = new Observer<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(String bitmap) {
                            invalidate();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("onError","log",e);
                        }

                        @Override
                        public void onComplete() {

                        }
                    };
                    observable.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
//                    mRect.offset(-moveX, 0);
//                    checkWidth();
//                    invalidate();
                }
                if (mImageHeight > getHeight()) {
                    mRect.offset(0, -moveY);
                    checkHeight();
                    invalidate();
                }

                return true;
            }
        });
    }


    private void checkWidth() {


        Rect rect = mRect;
        int imageWidth = mImageWidth;
        int imageHeight = mImageHeight;

        if (rect.right > imageWidth) {
            rect.right = imageWidth;
            rect.left = imageWidth - getWidth();
        }

        if (rect.left < 0) {
            rect.left = 0;
            rect.right = getWidth();
        }
    }


    private void checkHeight() {

        Rect rect = mRect;
        int imageWidth = mImageWidth;
        int imageHeight = mImageHeight;

        if (rect.bottom > imageHeight) {
            rect.bottom = imageHeight;
            rect.top = imageHeight - getHeight();
        }

        if (rect.top < 0) {
            rect.top = 0;
            rect.bottom = getHeight();
        }
    }


    public LargeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onToucEvent(event);
        return true;
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        if (!inited){
            tempBitmap = mDecoder.decodeRegion(mRect, options);
            inited = true;
        }
        Log.d("bitmaplog","" + tempBitmap);
        canvas.drawBitmap(tempBitmap, 0, 0, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        int imageWidth = mImageWidth;
        int imageHeight = mImageHeight;

        //默认直接显示图片的中心区域，可以自己去调节
        mRect.left = imageWidth / 2 - width / 2;
        mRect.top = imageHeight / 2 - height / 2;
        mRect.right = mRect.left + width;
        mRect.bottom = mRect.top + height;

    }

}
