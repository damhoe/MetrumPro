package com.stho.metrum;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static com.stho.metrum.MainActivity.POINTER_VIEW_KEY;
import static com.stho.metrum.ViewModel.DEFAULT_BPM;

public class ClassicModeFragment extends Fragment implements IUpdateableFragment, INotifyListener {

    private MainActivity mainActivity;

    private class ViewHolder {
        PointerView pointerView;
        RelativeLayout rootLayout;
        TextView tempoView;
    }

    private ViewHolder viewHolder = new ViewHolder();
    private ViewModel viewModel;

    private double omega;
    private double amplitude;
    private boolean animationRunState = false;
    private long startTime;
    private double phase;
    private static float maxAngle = 60;

    private float pivotX;
    private float pivotY;

    private static long DELAY = 10;
    private static int DEFAULT_TEMPO = 60;
    private static float STOP_ANGLE = 10;

    private float angle = 0;

    private Handler handler = new Handler();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.classic_mode_fragment, container,false);
        mainActivity = (MainActivity) this.getActivity();

        viewHolder.rootLayout = (RelativeLayout) rootView.findViewById(R.id.classis_mode_root_layout);
        viewHolder.rootLayout.setOnTouchListener(new View.OnTouchListener() {

            private double alpha;
            private boolean pointerIsTouched = false;

            private double getAngle(float x, float y) {     // x, y relative to pivotX, pivotY
                return Math.atan2(x, y) * 180 / Math.PI;                  // returns angle in degree
            }

            private boolean inRange(int x, int y) {
                float dx = x - pivotX;
                float dy = y - pivotY;
                alpha = getAngle(dx, -dy);
                return (Math.abs(alpha) <= maxAngle);
            }

            private boolean inStopRange(int x, int y) {
                float dx = x - pivotX;
                float dy = y - pivotY;
                alpha = getAngle(dx, -dy);
                return (Math.abs(alpha) <= STOP_ANGLE );
            }

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                pivotX = viewHolder.rootLayout.getWidth() / 2;
                pivotY = viewHolder.rootLayout.getHeight() / 2;

                int x = (int) event.getX();
                int y = (int) event.getY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {

                        if (inRange(x, y)) {
                            pointerIsTouched = true;
                            angle = (float) alpha;
                            rotateFragment();
                            stopAnimationThread();
                        }
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {

                        if (inRange(x, y)) {
                            pointerIsTouched = true;
                            angle = (float) alpha;
                            rotateFragment();
                            stopAnimationThread();
                        }
                        else if (pointerIsTouched) {
                            pointerIsTouched = false;
                            Log.d("cl1", "now");
                            startAnimationThread();

                        }
                        break;
                    }
                    case MotionEvent.ACTION_UP: {

                        if (inStopRange(x, y)) {
                            angle = 0;
                            rotateFragment();
                            stopAnimationThread();
                        }
                        else if (pointerIsTouched) {
                            Log.d("cl2","now");
                            startAnimationThread();
                        }

                        pointerIsTouched = false;
                        break;
                    }
                }
                Log.d("TouchTestC", Integer.toString(event.getAction())+ " " + Boolean.toString(animationRunState));
                return true;
            }
        });

        viewHolder.pointerView = (PointerView) rootView.findViewById(R.id.pointer_view);
        viewHolder.pointerView.setNotifyListener(this);
        viewHolder.tempoView = rootView.findViewById(R.id.tempo_view_classic);
        viewHolder.tempoView.setText(Integer.toString(DEFAULT_BPM));

        if (!(viewModel == null)) {
            viewModel.setBeatsPerMinute(DEFAULT_TEMPO);
            updateData();
            updateUI();
        }

        return rootView;
    }

    private void startAnimationThread() {
        animationRunState = true;
        phase = Math.asin(angle/amplitude);
        if (angle < 0) { omega = Math.abs(omega); }
        else if (angle >= 0) { omega = -Math.abs(omega); }
        startTime = System.currentTimeMillis();

        if (!viewModel.getRunState()) {
            mainActivity.onNotifyPlayerStateChanging(true);
        }

        ClassicModeFragment.this.handler.post(new Runnable() {
            @Override
            public void run() {
                if (animationRunState) {
                    angle = (float) (amplitude * Math.sin(omega * (System.currentTimeMillis() - startTime) + (float) phase));
                    Log.d("TouchTest", Float.toString((float) phase)+ " " + Boolean.toString(animationRunState));
                    updateUI();
                    handler.postDelayed(this, DELAY);
                }
            }
        });
    }

    private void stopAnimationThread() {
        animationRunState = false;
        mainActivity.onNotifyPlayerStateChanging(false);
        if (handler != null) {
            this.handler.removeCallbacks(null);
        }
    }

    private void rotateFragment() {

        if (angle > maxAngle) { angle = maxAngle; }
        else if (angle < -maxAngle) { angle = -maxAngle; }

        viewHolder.pointerView.setRotation(angle);
    }

    private void updateUI() {
        if (viewHolder.pointerView != null) {
            rotateFragment();
        }
        if (viewHolder.tempoView != null) {
            viewHolder.tempoView.setText(Integer.toString(viewModel.getBeatsPerMinute()));
        }
    }

    private void updateData() {
        amplitude = maxAngle; // in degree
        omega = 2 * Math.PI * viewModel.getBeatsPerMinute() * 0.5 /60000; // in rad / ms
    }

    private float getAngle() { return angle; }

    private void setActionDownEvent() {
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();
        int action = MotionEvent.ACTION_DOWN;
        int x = 100;
        int y = 100;
        int metaState = 0;

        // dispatch the event
        MotionEvent event = MotionEvent.obtain(downTime, eventTime, action, x, y, metaState);

        viewHolder.rootLayout.dispatchTouchEvent(event);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (handler != null) {
            this.handler.removeCallbacks(null);
        }
    }

    @Override
    public void updateViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
        updateUI();
        updateData();
        // pass
    }

    @Override
    public void onNotifyAngle(double angle) {
        // pass
    }

    @Override
    public void onNotifyMillis(long millis) {
        // pass
    }

    @Override
    public void onNotifyPlayerStateChanging(boolean bool) {
        // pass
    }

    @Override
    public void onNotifyMotionEvent(String key, MotionEvent e) {
        if (key.equals(POINTER_VIEW_KEY)) {
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    stopAnimationThread();
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    setActionDownEvent();
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    if (Math.abs(angle) > STOP_ANGLE) {
                        Log.d("cl", "now");
                        startAnimationThread();
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void onNotifyTempoChanging(int newTempo) {
        mainActivity.onNotifyTempoChanging(newTempo);
    }
}
