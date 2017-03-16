package playment.scrollanddrag.adapter;


import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import playment.scrollanddrag.R;
import playment.scrollanddrag.commons.ImageDragCallback;

/**
 * Created by ravi.kumar on 15-03-2017.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ReceiptViewHolder> {

    List<Integer> imagesList;
    Context ctx;
    ImageDragCallback imageDragCallback;
    private static final int SWIPE_MIN_DISTANCE = 50;
    private static final int SWIPE_MAX_OFF_PATH = 400;
    private final int INVALID_POINTER_ID = 99999;
    private int mActivePointerId;
    private float mLastTouchX;
    private float mLastTouchY;


    public ImageAdapter(Context context, List<Integer> imagesList, ImageDragCallback imageDragCallback) {
        this.imagesList = imagesList;
        this.ctx = context;
        this.imageDragCallback = imageDragCallback;
    }

    @Override
    public ReceiptViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.list_image_item, parent, false);
        ReceiptViewHolder holder = new ReceiptViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ReceiptViewHolder holder, final int position) {
        holder.itemView.setImageResource(imagesList.get(position));
        holder.itemActionLayout.setTag(imagesList.get(position));
        holder.itemActionLayout.setOnTouchListener(new dragTouchListener());
        holder.itemActionLayout.setOnDragListener(new dropListener());
        holder.itemActionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageDragCallback.onImageSelected(position);
            }
        });

    }

    public void updateList(List<Integer> list){
        this.imagesList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    protected class ReceiptViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout itemActionLayout;
        public ImageView itemView;

        public ReceiptViewHolder(View v) {
            super(v);
            itemActionLayout = (RelativeLayout) v.findViewById(R.id.item_action_layout);
            itemView = (ImageView) v.findViewById(R.id.item_image);
        }

    }

    private final class dragTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            ClipData.Item item = new ClipData.Item(v.getTag().toString());
            ClipData data = new ClipData(v.getTag().toString(), new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},item);
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);

            if (event.getAction() == MotionEvent.ACTION_POINTER_DOWN) {
                final int pointerIndex = MotionEventCompat.getActionIndex(event);
                final float x = MotionEventCompat.getX(event, pointerIndex);
                final float y = MotionEventCompat.getY(event, pointerIndex);
                // Remember where we started (for dragging)
                mLastTouchX = x;
                mLastTouchY = y;
                // Save the ID of this pointer (for dragging)
                mActivePointerId = MotionEventCompat.getPointerId(event, 0);
                return true;
            } else if (event.getAction() == MotionEvent.ACTION_MOVE){
                final int pointerIndex =
                        MotionEventCompat.findPointerIndex(event, mActivePointerId);

                final float x = MotionEventCompat.getX(event, pointerIndex);
                final float y = MotionEventCompat.getY(event, pointerIndex);
                // Calculate the distance moved
                final float dx = x - mLastTouchX;
                final float dy = y - mLastTouchY;
                // Remember this touch position for the next move event
                mLastTouchX = x;
                mLastTouchY = y;

                if( (dy == 0 && dx ==0) || Math.abs(dy)/Math.abs(dx) < 1.5 ) {  // Condition for drag or swipe.
                    return true;
                }
                else{
                    v.startDrag(data,shadowBuilder, v, 0);
                    return true;
                }
            }
            else
                return false;
        }

    }


    private class dropListener implements View.OnDragListener {

        View draggedView;
        RelativeLayout dropped;

        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    draggedView = (View) event.getLocalState();
                    dropped = (RelativeLayout) draggedView;
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    RelativeLayout dropTarget = (RelativeLayout) v;
                    imageDragCallback.onImageSelected((int)dropTarget.getTag());
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    imageDragCallback.onImageSelected((int)dropped.getTag());
                    break;
                default:
                    break;
            }
            return false;
        }
    }



}
