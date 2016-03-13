package com.asi.draw2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import java.lang.Math;

/**
 * Created by ashah on 3/5/2016.
 */

public class DrawView extends View {

    private static final int DEFAULT_CIRCLE_COLOR = Color.RED;
    private static final int DEFAULT_LINE_COLOR = Color.BLUE;

    private int circleColor = DEFAULT_CIRCLE_COLOR;
    private int lineColor = DEFAULT_LINE_COLOR;
    private Paint paint;

    private int num_sides=10;

    private double xscale, yscale;
    int cx, cy;

    public DrawView(Context context)
    {
        super(context);
        init(context, null);
    }

    public DrawView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs)
    {
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    public void setCircleColor(int circleColor)
    {
        this.circleColor = circleColor;
        invalidate();
    }

    public int getCircleColor()
    {
        return circleColor;
    }

    public void set_num_sides(int n)
    {
        num_sides=n;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        int w = getWidth();
        int h = getHeight();
        double angle = 0;

        int pl = getPaddingLeft();
        int pr = getPaddingRight();
        int pt = getPaddingTop();
        int pb = getPaddingBottom();

        int usableWidth = w - (pl + pr);
        int usableHeight = h - (pt + pb);

        int radius = Math.min(usableWidth, usableHeight) / 2;
        cx = pl + (usableWidth / 2);
        cy = pt + (usableHeight / 2);

        xscale=usableWidth/20/2;
        yscale=usableHeight/20/4;

        int type=1;

        if (type==0) {
            // Draw a Circle and polygon
            int i, j;

            int[] x = new int[20];
            int[] y = new int[20];

            for (i = 0; i < num_sides; i++) {
                angle = (double) i * 2.0 * Math.PI / (double) num_sides;
                x[i] = cx + (int) (radius * Math.cos(angle));
                y[i] = cy + (int) (radius * Math.sin(angle));
            }

            paint.setColor(circleColor);
            canvas.drawCircle(cx, cy, radius, paint);
            paint.setColor(lineColor);
            paint.setStrokeWidth(5);
            for (i = 0; i < num_sides - 1; i++) {
                for (j = i + 1; j < num_sides; j++) {
                    canvas.drawLine(x[i], y[i], x[j], y[j], paint);
                }
            }
            canvas.drawLine(x[1], y[1], x[2], y[2], paint);
        }

        else if (type==1) {
            // Draw a 3D graph
            double x, y;
            int xnow,ynow,xprev, yprev;

            //set line style for grid
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5);

            //Draw Space
            canvas.drawLine(X(-10,-10), Y(-10,-10,0), X(-10,10), Y(-10,10,0), paint);
            canvas.drawLine(X(-10,10), Y(-10,10,0), X(10,10), Y(10,10,0), paint);
            canvas.drawLine(X(10,10), Y(10,10,0), X(10,-10), Y(10,-10,0), paint);
            canvas.drawLine(X(10, -10), Y(10, -10,0), X(-10, -10), Y(-10, -10,0), paint);

            // set line style for graph
            paint.setColor(Color.BLUE);
            paint.setStrokeWidth(3);

            // Draw the x lines
            for (x=-10; x<=10; x+=(10.0/num_sides)) {
                xprev=X(x,-10);
                yprev=Y(x,-10,Z(x,-10));
                for (y = -10; y <= 10; y += (10.0/num_sides)) {
                    xnow=X(x,y);
                    ynow=Y(x,y,Z(x,y));
                    canvas.drawLine(xprev, yprev, xnow, ynow, paint);
                    xprev=xnow;
                    yprev=ynow;
                }
            }

            // Draw the y lines
            for (y=-10; y<=10; y+=(10.0/num_sides)) {
                xprev=X(-10,y);
                yprev=Y(-10,y,Z(-10,y));
                for (x = -10; x <= 10; x += (10.0/num_sides)) {
                    xnow=X(x,y);
                    ynow=Y(x,y,Z(x,y));
                    canvas.drawLine(xprev, yprev, xnow, ynow, paint);
                    xprev=xnow;
                    yprev=ynow;
                }
            }

        }



    }

    private int X(double x, double y)
    {
        return (int)(cx + xscale*x + xscale*y);
    }

    private int Y(double x, double y, double z) {
        return (int)(cy-yscale*x + yscale*y - yscale*z);
    }

    private double Z(double x, double y){
        double r = Math.sqrt(x*x+y*y);
        return 15*Math.exp(-(r*r/20))*Math.cos(1.5 * r);
    }
}