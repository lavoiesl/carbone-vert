/* ===========================================================
 * AFreeChart : a free chart library for Android(tm) platform.
 *              (based on JFreeChart and JCommon)
 * ===========================================================
 *
 * (C) Copyright 2010, by ICOMSYSTECH Co.,Ltd.
 * (C) Copyright 2000-2008, by Object Refinery Limited and Contributors.
 *
 * Project Info:
 *    AFreeChart: http://code.google.com/p/afreechart/
 *    JFreeChart: http://www.jfree.org/jfreechart/index.html
 *    JCommon   : http://www.jfree.org/jcommon/index.html
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * [Android is a trademark of Google Inc.]
 *
 * -----------------
 * ChartView.java
 * -----------------
 * (C) Copyright 2010, by ICOMSYSTECH Co.,Ltd.
 *
 * Original Author:  Niwano Masayoshi (for ICOMSYSTECH Co.,Ltd);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 19-Nov-2010 : Version 0.0.1 (NM);
 * 14-Jan-2011 : renamed method name
 * 14-Jan-2011 : Updated API docs
 */

package org.afree.chart.demo;

import java.util.EventListener;
import java.util.concurrent.CopyOnWriteArrayList;

import org.afree.chart.AFreeChart;
import org.afree.chart.ChartRenderingInfo;
import org.afree.chart.ChartTouchEvent;
import org.afree.chart.ChartTouchListener;
import org.afree.chart.entity.ChartEntity;
import org.afree.chart.entity.EntityCollection;
import org.afree.chart.event.ChartChangeEvent;
import org.afree.chart.event.ChartChangeListener;
import org.afree.chart.event.ChartProgressEvent;
import org.afree.chart.event.ChartProgressListener;
import org.afree.chart.plot.Movable;
import org.afree.chart.plot.Plot;
import org.afree.chart.plot.PlotOrientation;
import org.afree.chart.plot.PlotRenderingInfo;
import org.afree.chart.plot.Zoomable;
import org.afree.graphics.SolidColor;
import org.afree.graphics.geom.Dimension;
import org.afree.graphics.geom.RectShape;
import org.afree.ui.RectangleInsets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class DemoView extends View implements ChartChangeListener,
		ChartProgressListener {

	/** The user interface thread handler. */
	private final Handler mHandler;

	public DemoView(final Context context) {
		super(context);
		mHandler = new Handler();
		initialize();
	}

	public DemoView(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		mHandler = new Handler();
		initialize();
	}

	/**
	 * initialize parameters
	 */
	private void initialize() {
		chartMotionListeners = new CopyOnWriteArrayList<ChartTouchListener>();
		info = new ChartRenderingInfo();
		minimumDrawWidth = DEFAULT_MINIMUM_DRAW_WIDTH;
		minimumDrawHeight = DEFAULT_MINIMUM_DRAW_HEIGHT;
		maximumDrawWidth = DEFAULT_MAXIMUM_DRAW_WIDTH;
		maximumDrawHeight = DEFAULT_MAXIMUM_DRAW_HEIGHT;
		moveTriggerDistance = DEFAULT_MOVE_TRIGGER_DISTANCE;
		new SolidColor(Color.BLUE);
		new SolidColor(Color.argb(0, 0, 255, 63));
	}

	/**
	 * Default setting for buffer usage. The default has been changed to
	 * <code>true</code> from version 1.0.13 onwards, because of a severe
	 * performance problem with drawing the zoom RectShape using XOR (which now
	 * happens only when the buffer is NOT used).
	 */
	public static final boolean DEFAULT_BUFFER_USED = true;

	/** The default panel width. */
	public static final int DEFAULT_WIDTH = 680;

	/** The default panel height. */
	public static final int DEFAULT_HEIGHT = 420;

	/** The default limit below which chart scaling kicks in. */
	public static final int DEFAULT_MINIMUM_DRAW_WIDTH = 10;

	/** The default limit below which chart scaling kicks in. */
	public static final int DEFAULT_MINIMUM_DRAW_HEIGHT = 10;

	/** The default limit above which chart scaling kicks in. */
	public static final int DEFAULT_MAXIMUM_DRAW_WIDTH = 1024;

	/** The default limit above which chart scaling kicks in. */
	public static final int DEFAULT_MAXIMUM_DRAW_HEIGHT = 1000;

	/** The minimum size required to perform a zoom on a RectShape */
	public static final int DEFAULT_ZOOM_TRIGGER_DISTANCE = 10;

	/** The minimum size required to perform a move on a RectShape */
	public static final int DEFAULT_MOVE_TRIGGER_DISTANCE = 10;

	/** The chart that is displayed in the panel. */
	private AFreeChart chart;

	/** Storage for registered (chart) touch listeners. */
	private transient CopyOnWriteArrayList<ChartTouchListener> chartMotionListeners;

	/** The drawing info collected the last time the chart was drawn. */
	private ChartRenderingInfo info;

	/** The scale factor used to draw the chart. */
	private double scaleX;

	/** The scale factor used to draw the chart. */
	private double scaleY;

	/** The plot orientation. */
	private PlotOrientation orientation = PlotOrientation.VERTICAL;

	/**
	 * The zoom RectShape starting point (selected by the user with touch). This
	 * is a point on the screen, not the chart (which may have been scaled up or
	 * down to fit the panel).
	 */
	private final PointF zoomPoint = null;

	/** Controls if the zoom RectShape is drawn as an outline or filled. */
	// private boolean fillZoomRectShape = true;

	private int moveTriggerDistance;

	/** The last touch position during panning. */
	// private Point panLast;

	private RectangleInsets insets = null;

	/**
	 * The minimum width for drawing a chart (uses scaling for smaller widths).
	 */
	private int minimumDrawWidth;

	/**
	 * The minimum height for drawing a chart (uses scaling for smaller
	 * heights).
	 */
	private int minimumDrawHeight;

	/**
	 * The maximum width for drawing a chart (uses scaling for bigger widths).
	 */
	private int maximumDrawWidth;

	/**
	 * The maximum height for drawing a chart (uses scaling for bigger heights).
	 */
	private int maximumDrawHeight;

	private Dimension size = null;

	/** The chart anchor point. */
	private PointF anchor;

	/** A flag that controls whether or not domain moving is enabled. */
	private boolean domainMovable = false;

	/** A flag that controls whether or not range moving is enabled. */
	private boolean rangeMovable = false;

	private double accelX, accelY;
	private final double friction = 0.8;
	private boolean inertialMovedFlag = false;
	private PointF lastTouch;

	private float mScale = 1.0f;

	private long mPrevTimeMillis = 0;
	private long mNowTimeMillis = System.currentTimeMillis();

	/**
	 * touch event
	 */
	@Override
	public boolean onTouchEvent(final MotionEvent ev) {

		super.onTouchEvent(ev);
		final int action = ev.getAction();
		final int count = ev.getPointerCount();

		anchor = new PointF(ev.getX(), ev.getY());

		if (info != null) {
			final EntityCollection entities = info.getEntityCollection();
			if (entities != null) {
			}
		}

		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_POINTER_DOWN:
			Log.i("TouchEvent", "ACTION_DOWN");
			if (count == 2 && multiTouchStartInfo == null) {
				setMultiTouchStartInfo(ev);
			} else if (count == 1 && singleTouchStartInfo == null) {
				setSingleTouchStartInfo(ev);
			}

			touched(ev);

			break;
		case MotionEvent.ACTION_MOVE:
			Log.i("TouchEvent", "ACTION_MOVE");
			if (count == 1 && singleTouchStartInfo != null) {
				moveAdjustment(ev);
			} else if (count == 2 && multiTouchStartInfo != null) {
				// scaleAdjustment(ev);
				zoomAdjustment(ev);
			}

			inertialMovedFlag = false;

			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			Log.i("TouchEvent", "ACTION_UP");
			if (count <= 2) {
				multiTouchStartInfo = null;
				singleTouchStartInfo = null;
			}
			if (count <= 1) {
				singleTouchStartInfo = null;
			}

			// double click check
			if (count == 1) {
				mNowTimeMillis = System.currentTimeMillis();
				if (mNowTimeMillis - mPrevTimeMillis < 400) {
					if (chart.getPlot() instanceof Movable) {
						restoreAutoBounds();
						mScale = 1.0f;
						inertialMovedFlag = false;
					}
				} else {
					inertialMovedFlag = true;
				}
				mPrevTimeMillis = mNowTimeMillis;
			}
			break;
		default:
			break;
		}

		return true;
	}

	/**
	 * MultiTouchStartInfo setting
	 * 
	 * @param ev
	 */
	private void setMultiTouchStartInfo(final MotionEvent ev) {

		if (multiTouchStartInfo == null) {
			multiTouchStartInfo = new MultiTouchStartInfo();
		}

		// distance
		final double distance = Math.sqrt(Math.pow(ev.getX(0) - ev.getX(1), 2)
				+ Math.pow(ev.getY(0) - ev.getY(1), 2));
		multiTouchStartInfo.setDistance(distance);
	}

	/**
	 * SingleTouchStartInfo setting
	 * 
	 * @param ev
	 */
	private void setSingleTouchStartInfo(final MotionEvent ev) {

		if (singleTouchStartInfo == null) {
			singleTouchStartInfo = new SingleTouchStartInfo();
		}

		// start point
		singleTouchStartInfo.setX(ev.getX(0));
		singleTouchStartInfo.setY(ev.getY(0));
	}

	/**
	 * Translate MotionEvent as TouchEvent
	 * 
	 * @param ev
	 */
	private void moveAdjustment(final MotionEvent ev) {

		boolean hMove = false;
		boolean vMove = false;
		if (orientation == PlotOrientation.HORIZONTAL) {
			hMove = rangeMovable;
			vMove = domainMovable;
		} else {
			hMove = domainMovable;
			vMove = rangeMovable;
		}

		final boolean moveTrigger1 = hMove
				&& Math.abs(ev.getX(0) - singleTouchStartInfo.getX()) >= moveTriggerDistance;
		final boolean moveTrigger2 = vMove
				&& Math.abs(ev.getY(0) - singleTouchStartInfo.getY()) >= moveTriggerDistance;
		if (moveTrigger1 || moveTrigger2) {

			final RectShape dataArea = info.getPlotInfo().getDataArea();

			double moveBoundX;
			double moveBoundY;
			final double dataAreaWidth = dataArea.getWidth();
			final double dataAreaHeight = dataArea.getHeight();

			// for touchReleased event, (horizontalZoom || verticalZoom)
			// will be true, so we can just test for either being false;
			// otherwise both are true

			if (!vMove) {
				moveBoundX = singleTouchStartInfo.getX() - ev.getX(0);
				moveBoundY = 0;
			} else if (!hMove) {
				moveBoundX = 0;
				moveBoundY = singleTouchStartInfo.getY() - ev.getY(0);
			} else {
				moveBoundX = singleTouchStartInfo.getX() - ev.getX(0);
				moveBoundY = singleTouchStartInfo.getY() - ev.getY(0);
			}
			accelX = moveBoundX;
			accelY = moveBoundY;

			lastTouch = new PointF(ev.getX(0), ev.getY(0));
			move(lastTouch, moveBoundX, moveBoundY, dataAreaWidth,
					dataAreaHeight);

		}

		setSingleTouchStartInfo(ev);
	}

	/**
	 * 
	 * @param moveBoundX
	 * @param moveBoundY
	 * @param dataAreaWidth
	 * @param dataAreaHeight
	 */
	private void move(final PointF source, final double moveBoundX,
			final double moveBoundY, final double dataAreaWidth,
			final double dataAreaHeight) {

		if (source == null) {
			throw new IllegalArgumentException("Null 'source' argument");
		}

		final double hMovePercent = moveBoundX / dataAreaWidth;
		final double vMovePercent = -moveBoundY / dataAreaHeight;

		final Plot p = chart.getPlot();
		if (p instanceof Movable) {
			final PlotRenderingInfo info = this.info.getPlotInfo();
			// here we tweak the notify flag on the plot so that only
			// one notification happens even though we update multiple
			// axes...
			// boolean savedNotify = p.isNotify();
			// p.setNotify(false);
			final Movable z = (Movable) p;
			if (z.getOrientation() == PlotOrientation.HORIZONTAL) {
				z.moveDomainAxes(vMovePercent, info, source);
				z.moveRangeAxes(hMovePercent, info, source);
			} else {
				z.moveDomainAxes(hMovePercent, info, source);
				z.moveRangeAxes(vMovePercent, info, source);
			}
			// p.setNotify(savedNotify);

			// repaint
			invalidate();
		}

	}

	/**
	 * Restores the auto-range calculation on both axes.
	 */
	public void restoreAutoBounds() {
		final Plot plot = chart.getPlot();
		if (plot == null) {
			return;
		}
		// here we tweak the notify flag on the plot so that only
		// one notification happens even though we update multiple
		// axes...
		// boolean savedNotify = plot.isNotify();
		// plot.setNotify(false);
		restoreAutoDomainBounds();
		restoreAutoRangeBounds();
		// plot.setNotify(savedNotify);
	}

	/**
	 * Restores the auto-range calculation on the domain axis.
	 */
	public void restoreAutoDomainBounds() {
		final Plot plot = chart.getPlot();
		if (plot instanceof Zoomable) {
			final Zoomable z = (Zoomable) plot;
			// here we tweak the notify flag on the plot so that only
			// one notification happens even though we update multiple
			// axes...
			// boolean savedNotify = plot.isNotify();
			// plot.setNotify(false);
			// we need to guard against this.zoomPoint being null
			final PointF zp = zoomPoint != null ? zoomPoint : new PointF();
			z.zoomDomainAxes(0.0, info.getPlotInfo(), zp);
			// plot.setNotify(savedNotify);
		}
	}

	/**
	 * Restores the auto-range calculation on the range axis.
	 */
	public void restoreAutoRangeBounds() {
		final Plot plot = chart.getPlot();
		if (plot instanceof Zoomable) {
			final Zoomable z = (Zoomable) plot;
			// here we tweak the notify flag on the plot so that only
			// one notification happens even though we update multiple
			// axes...
			// boolean savedNotify = plot.isNotify();
			// plot.setNotify(false);
			// we need to guard against this.zoomPoint being null
			final PointF zp = zoomPoint != null ? zoomPoint : new PointF();
			z.zoomRangeAxes(0.0, info.getPlotInfo(), zp);
			// plot.setNotify(savedNotify);
		}
	}

	@Override
	protected void onSizeChanged(final int w, final int h, final int oldw,
			final int oldh) {
		insets = new RectangleInsets(0, 0, 0, 0);
		size = new Dimension(w, h);
	}

	private RectangleInsets getInsets() {
		return insets;
	}

	/**
	 * Returns the X scale factor for the chart. This will be 1.0 if no scaling
	 * has been used.
	 * 
	 * @return The scale factor.
	 */
	public double getScaleX() {
		return scaleX;
	}

	/**
	 * Returns the Y scale factory for the chart. This will be 1.0 if no scaling
	 * has been used.
	 * 
	 * @return The scale factor.
	 */
	public double getScaleY() {
		return scaleY;
	}

	/**
	 * Sets the chart that is displayed in the panel.
	 * 
	 * @param chart
	 *            the chart (<code>null</code> permitted).
	 */
	public void setChart(final AFreeChart chart) {

		// stop listening for changes to the existing chart
		if (this.chart != null) {
			this.chart.removeChangeListener(this);
			this.chart.removeProgressListener(this);
		}

		// add the new chart
		this.chart = chart;
		if (chart != null) {
			this.chart.addChangeListener(this);
			this.chart.addProgressListener(this);
			final Plot plot = chart.getPlot();
			if (plot instanceof Zoomable) {
				final Zoomable z = (Zoomable) plot;
				z.isRangeZoomable();
				orientation = z.getOrientation();
			}

			domainMovable = false;
			rangeMovable = false;
			if (plot instanceof Movable) {
				final Movable z = (Movable) plot;
				domainMovable = z.isDomainMovable();
				rangeMovable = z.isRangeMovable();
				orientation = z.getOrientation();
			}
		} else {
			domainMovable = false;
			rangeMovable = false;
		}
		// if (this.useBuffer) {
		// this.refreshBuffer = true;
		// }
		repaint();

	}

	/**
	 * Returns the minimum drawing width for charts.
	 * <P>
	 * If the width available on the panel is less than this, then the chart is
	 * drawn at the minimum width then scaled down to fit.
	 * 
	 * @return The minimum drawing width.
	 */
	public int getMinimumDrawWidth() {
		return minimumDrawWidth;
	}

	/**
	 * Sets the minimum drawing width for the chart on this panel.
	 * <P>
	 * At the time the chart is drawn on the panel, if the available width is
	 * less than this amount, the chart will be drawn using the minimum width
	 * then scaled down to fit the available space.
	 * 
	 * @param width
	 *            The width.
	 */
	public void setMinimumDrawWidth(final int width) {
		minimumDrawWidth = width;
	}

	/**
	 * Returns the maximum drawing width for charts.
	 * <P>
	 * If the width available on the panel is greater than this, then the chart
	 * is drawn at the maximum width then scaled up to fit.
	 * 
	 * @return The maximum drawing width.
	 */
	public int getMaximumDrawWidth() {
		return maximumDrawWidth;
	}

	/**
	 * Sets the maximum drawing width for the chart on this panel.
	 * <P>
	 * At the time the chart is drawn on the panel, if the available width is
	 * greater than this amount, the chart will be drawn using the maximum width
	 * then scaled up to fit the available space.
	 * 
	 * @param width
	 *            The width.
	 */
	public void setMaximumDrawWidth(final int width) {
		maximumDrawWidth = width;
	}

	/**
	 * Returns the minimum drawing height for charts.
	 * <P>
	 * If the height available on the panel is less than this, then the chart is
	 * drawn at the minimum height then scaled down to fit.
	 * 
	 * @return The minimum drawing height.
	 */
	public int getMinimumDrawHeight() {
		return minimumDrawHeight;
	}

	/**
	 * Sets the minimum drawing height for the chart on this panel.
	 * <P>
	 * At the time the chart is drawn on the panel, if the available height is
	 * less than this amount, the chart will be drawn using the minimum height
	 * then scaled down to fit the available space.
	 * 
	 * @param height
	 *            The height.
	 */
	public void setMinimumDrawHeight(final int height) {
		minimumDrawHeight = height;
	}

	/**
	 * Returns the maximum drawing height for charts.
	 * <P>
	 * If the height available on the panel is greater than this, then the chart
	 * is drawn at the maximum height then scaled up to fit.
	 * 
	 * @return The maximum drawing height.
	 */
	public int getMaximumDrawHeight() {
		return maximumDrawHeight;
	}

	/**
	 * Sets the maximum drawing height for the chart on this panel.
	 * <P>
	 * At the time the chart is drawn on the panel, if the available height is
	 * greater than this amount, the chart will be drawn using the maximum
	 * height then scaled up to fit the available space.
	 * 
	 * @param height
	 *            The height.
	 */
	public void setMaximumDrawHeight(final int height) {
		maximumDrawHeight = height;
	}

	/**
	 * Returns the chart rendering info from the most recent chart redraw.
	 * 
	 * @return The chart rendering info.
	 */
	public ChartRenderingInfo getChartRenderingInfo() {
		return info;
	}

	@Override
	protected void onDraw(final Canvas canvas) {
		super.onDraw(canvas);

		inertialMove();

		paintComponent(canvas);
	}

	@Override
	protected void onMeasure(final int widthMeasureSpec,
			final int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * Paints the component by drawing the chart to fill the entire component,
	 * but allowing for the insets (which will be non-zero if a border has been
	 * set for this component). To increase performance (at the expense of
	 * memory), an off-screen buffer image can be used.
	 * 
	 * @param canvas
	 *            the graphics device for drawing on.
	 */
	public void paintComponent(final Canvas canvas) {

		// first determine the size of the chart rendering area...
		final Dimension size = getSize();
		final RectangleInsets insets = getInsets();
		final RectShape available = new RectShape(insets.getLeft(),
				insets.getTop(), size.getWidth() - insets.getLeft()
						- insets.getRight(), size.getHeight() - insets.getTop()
						- insets.getBottom());

		double drawWidth = available.getWidth();
		double drawHeight = available.getHeight();
		scaleX = 1.0;
		scaleY = 1.0;

		if (drawWidth < minimumDrawWidth) {
			scaleX = drawWidth / minimumDrawWidth;
			drawWidth = minimumDrawWidth;
		} else if (drawWidth > maximumDrawWidth) {
			scaleX = drawWidth / maximumDrawWidth;
			drawWidth = maximumDrawWidth;
		}

		if (drawHeight < minimumDrawHeight) {
			scaleY = drawHeight / minimumDrawHeight;
			drawHeight = minimumDrawHeight;
		} else if (drawHeight > maximumDrawHeight) {
			scaleY = drawHeight / maximumDrawHeight;
			drawHeight = maximumDrawHeight;
		}

		final RectShape chartArea = new RectShape(0.0, 0.0, drawWidth,
				drawHeight);

		// are we using the chart buffer?
		// if (this.useBuffer) {
		//
		// // do we need to resize the buffer?
		// if ((this.chartBuffer == null)
		// || (this.chartBufferWidth != available.getWidth())
		// || (this.chartBufferHeight != available.getHeight())) {
		// this.chartBufferWidth = (int) available.getWidth();
		// this.chartBufferHeight = (int) available.getHeight();
		// GraphicsConfiguration gc = canvas.getDeviceConfiguration();
		// this.chartBuffer = gc.createCompatibleImage(
		// this.chartBufferWidth, this.chartBufferHeight,
		// Transparency.TRANSLUCENT);
		// this.refreshBuffer = true;
		// }
		//
		// // do we need to redraw the buffer?
		// if (this.refreshBuffer) {
		//
		// this.refreshBuffer = false; // clear the flag
		//
		// RectShape bufferArea = new RectShape(
		// 0, 0, this.chartBufferWidth, this.chartBufferHeight);
		//
		// Graphics2D bufferG2 = (Graphics2D)
		// this.chartBuffer.getGraphics();
		// RectShape r = new RectShape(0, 0, this.chartBufferWidth,
		// this.chartBufferHeight);
		// bufferG2.setPaint(getBackground());
		// bufferG2.fill(r);
		// if (scale) {
		// AffineTransform saved = bufferG2.getTransform();
		// AffineTransform st = AffineTransform.getScaleInstance(
		// this.scaleX, this.scaleY);
		// bufferG2.transform(st);
		// this.chart.draw(bufferG2, chartArea, this.anchor,
		// this.info);
		// bufferG2.setTransform(saved);
		// }
		// else {
		// this.chart.draw(bufferG2, bufferArea, this.anchor,
		// this.info);
		// }
		//
		// }
		//
		// // zap the buffer onto the panel...
		// canvas.drawImage(this.chartBuffer, insets.left, insets.top, this);
		//
		// }

		// TODO:AffineTransform
		// or redrawing the chart every time...
		// else {

		// AffineTransform saved = canvas.getTransform();
		// canvas.translate(insets.left, insets.top);
		// if (scale) {
		// AffineTransform st = AffineTransform.getScaleInstance(
		// this.scaleX, this.scaleY);
		// canvas.transform(st);
		// }
		// this.chart.draw(canvas, chartArea, this.anchor, this.info);
		// canvas.setTransform(saved);

		// }
		chart.draw(canvas, chartArea, anchor, info);

		// Iterator iterator = this.overlays.iterator();
		// while (iterator.hasNext()) {
		// Overlay overlay = (Overlay) iterator.next();
		// overlay.paintOverlay(canvas, this);
		// }

		// redraw the zoom RectShape (if present) - if useBuffer is false,
		// we use XOR so we can XOR the RectShape away again without redrawing
		// the chart
		// drawZoomRectShape(canvas, !this.useBuffer);

		// canvas.dispose();

		anchor = null;
		// this.verticalTraceLine = null;
		// this.horizontalTraceLine = null;

	}

	public Dimension getSize() {
		return size;
	}

	/**
	 * Returns the anchor point.
	 * 
	 * @return The anchor point (possibly <code>null</code>).
	 */
	public PointF getAnchor() {
		return anchor;
	}

	public ChartRenderingInfo getInfo() {
		return info;
	}

	/**
	 * Sets the anchor point. This method is provided for the use of subclasses,
	 * not end users.
	 * 
	 * @param anchor
	 *            the anchor point (<code>null</code> permitted).
	 */
	protected void setAnchor(final PointF anchor) {
		this.anchor = anchor;
	}

	/**
	 * Information for multi touch start
	 * 
	 * @author ikeda
	 * 
	 */
	private class MultiTouchStartInfo {
		private double distance = 0;

		public double getDistance() {
			return distance;
		}

		public void setDistance(final double distance) {
			this.distance = distance;
		}
	}

	private MultiTouchStartInfo multiTouchStartInfo = null;

	/**
	 * Information for Single touch start
	 * 
	 * @author ikeda
	 * 
	 */
	private class SingleTouchStartInfo {
		private double x = 0;
		private double y = 0;

		public double getX() {
			return x;
		}

		public void setX(final double x) {
			this.x = x;
		}

		public double getY() {
			return y;
		}

		public void setY(final double y) {
			this.y = y;
		}
	}

	private SingleTouchStartInfo singleTouchStartInfo = null;

	/**
	 * Zoom
	 * 
	 * @param ev
	 */
	private void zoomAdjustment(final MotionEvent ev) {
		final PointF point = new PointF((ev.getX(0) + ev.getX(1)) / 2,
				(ev.getY(0) + ev.getY(1)) / 2);
		// end distance
		final double endDistance = Math.sqrt(Math.pow(ev.getX(0) - ev.getX(1),
				2) + Math.pow(ev.getY(0) - ev.getY(1), 2));

		// zoom process
		zoom(point, multiTouchStartInfo.getDistance(), endDistance);

		// reset start point
		setMultiTouchStartInfo(ev);
	}

	/**
	 * zoom
	 * 
	 * @param startDistance
	 * @param endDistance
	 */
	private void zoom(final PointF source, final double startDistance,
			final double endDistance) {

		final Plot plot = chart.getPlot();
		final PlotRenderingInfo info = this.info.getPlotInfo();

		if (plot instanceof Zoomable) {
			final float scaleDistance = (float) (startDistance / endDistance);

			if (mScale * scaleDistance < 10.0f && mScale * scaleDistance > 0.1f) {
				mScale *= scaleDistance;
				final Zoomable z = (Zoomable) plot;
				z.zoomDomainAxes(scaleDistance, info, source, false);
				z.zoomRangeAxes(scaleDistance, info, source, false);
			}
		}

		// repaint
		invalidate();
	}

	private void inertialMove() {
		if (inertialMovedFlag == true) {
			final RectShape dataArea = info.getPlotInfo().getDataArea();

			accelX *= friction;
			accelY *= friction;

			final double dataAreaWidth = dataArea.getWidth();
			final double dataAreaHeight = dataArea.getHeight();

			if (lastTouch != null) {
				move(lastTouch, accelX, accelY, dataAreaWidth, dataAreaHeight);
			}

			if (accelX < 0.1 && accelX > -0.1) {
				accelX = 0;
			}

			if (accelY < 0.1 && accelY > -0.1) {
				accelY = 0;
			}

			if (accelX == 0 && accelY == 0) {
				inertialMovedFlag = false;
			}
		}
	}

	/**
	 * Receives notification of touch on the panel. These are translated and
	 * passed on to any registered {@link ChartTouchListener}s.
	 * 
	 * @param event
	 *            Information about the touch event.
	 */
	public void touched(final MotionEvent event) {

		final int x = (int) (event.getX() / scaleX);
		final int y = (int) (event.getY() / scaleY);

		anchor = new PointF(x, y);
		if (chart == null) {
			return;
		}
		chart.setNotify(true); // force a redraw

		chart.handleClick((int) event.getX(), (int) event.getY(), info);
		inertialMovedFlag = false;

		// new entity code...
		if (chartMotionListeners.size() == 0) {
			return;
		}

		ChartEntity entity = null;
		if (info != null) {
			final EntityCollection entities = info.getEntityCollection();
			if (entities != null) {
				entity = entities.getEntity(x, y);
			}
		}
		final ChartTouchEvent chartEvent = new ChartTouchEvent(getChart(),
				event, entity);
		for (int i = chartMotionListeners.size() - 1; i >= 0; i--) {
			chartMotionListeners.get(i).chartTouched(chartEvent);
		}

	}

	/**
	 * Returns the chart contained in the panel.
	 * 
	 * @return The chart (possibly <code>null</code>).
	 */
	public AFreeChart getChart() {
		return chart;
	}

	/**
	 * Adds a listener to the list of objects listening for chart touch events.
	 * 
	 * @param listener
	 *            the listener (<code>null</code> not permitted).
	 */
	public void addChartTouchListener(final ChartTouchListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException("Null 'listener' argument.");
		}
		chartMotionListeners.add(listener);
	}

	/**
	 * Removes a listener from the list of objects listening for chart touch
	 * events.
	 * 
	 * @param listener
	 *            the listener.
	 */
	public void removeChartTouchListener(final ChartTouchListener listener) {
		chartMotionListeners.remove(listener);
	}

	/**
	 * Returns an array of the listeners of the given type registered with the
	 * panel.
	 * 
	 * @param listenerType
	 *            the listener type.
	 * 
	 * @return An array of listeners.
	 */
	public EventListener[] getListeners() {
		return chartMotionListeners.toArray(new ChartTouchListener[0]);
	}

	/**
	 * Schedule a user interface repaint.
	 */
	public void repaint() {
		mHandler.post(new Runnable() {
			public void run() {
				invalidate();
			}
		});
	}

	/**
	 * Receives notification of changes to the chart, and redraws the chart.
	 * 
	 * @param event
	 *            details of the chart change event.
	 */
	public void chartChanged(final ChartChangeEvent event) {
		// this.refreshBuffer = true;
		final Plot plot = chart.getPlot();
		if (plot instanceof Zoomable) {
			final Zoomable z = (Zoomable) plot;
			orientation = z.getOrientation();
		}
		repaint();
	}

	/**
	 * Receives notification of a chart progress event.
	 * 
	 * @param event
	 *            the event.
	 */
	public void chartProgress(final ChartProgressEvent event) {
		// does nothing - override if necessary
	}
}
