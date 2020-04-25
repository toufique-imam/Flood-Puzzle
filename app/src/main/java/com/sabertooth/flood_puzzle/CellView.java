package com.sabertooth.flood_puzzle;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.annotation.Nullable;

//color position(x,y) given
//fixed square size for now

public class CellView extends androidx.appcompat.widget.AppCompatButton{
    int X,Y,CellColor,X1,Y1;
    private OnClickListener onClickListener;
    Rect CellRect;
    Paint CellPaint;
    public CellView(Context context) {
        super(context);
        init(null);
    }

    public CellView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CellView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }
    private void init(@Nullable AttributeSet set){
        X = Y = 0;
        CellColor = Color.GREEN;

        CellRect = new Rect();
        CellPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        CellRect.left = X ;
        CellRect.top = Y ;
        CellRect.right = X+10;
        CellRect.bottom = Y+10 ;

        CellPaint.setColor(Color.RED);
    }
    public void updateCellColor(int color){
        CellPaint.setColor(color);
        postInvalidate();
    }
    public void redraw(int X_st,int Y_st,int X_en,int Y_en,int color){
        //Log.e("XCOR", String.valueOf(X_st));
        //Log.e("XCOR", String.valueOf(Y_st));
        //Log.e("XCOR", String.valueOf(X_en));
        //Log.e("XCOR", String.valueOf(Y_en));
        Log.e("XCOR",X_st+" "+Y_st+" "+X_en+" "+Y_en+" "+color);
        X = X_st;
        Y = Y_st;
        X1 = X_en;
        Y1 = Y_en;
        CellColor = color;
        CellRect.left = X;
        CellRect.top = Y;
        CellRect.right = X1;
        CellRect.bottom = Y1;
        CellPaint.setColor(color);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        canvas.drawColor(CellColor);
        canvas.drawRect(CellRect,CellPaint);
    }

    public int getCellColor() {
        return CellColor;
    }

    //for onCLick

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getAction()==KeyEvent.ACTION_UP &&
                (event.getKeyCode()==KeyEvent.KEYCODE_DPAD_CENTER || event.getKeyCode() == KeyEvent.KEYCODE_ENTER)){
            if(onClickListener!=null)onClickListener.onClick(this);
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            setPressed(true);
        }
        else if(event.getAction()==MotionEvent.ACTION_UP){
            if(onClickListener!=null){
                onClickListener.onClick(this);
                setPressed(false);
            }
        }
        else{
            setPressed(false);
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        //super.setOnClickListener(l);
        onClickListener = l;
        //Log.e("DEBUG","CAME HERE");
    }

}
