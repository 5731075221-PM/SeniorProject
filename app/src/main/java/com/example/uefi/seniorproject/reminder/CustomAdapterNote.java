package com.example.uefi.seniorproject.reminder;

import android.app.PendingIntent;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.example.uefi.seniorproject.R;
import com.example.uefi.seniorproject.alert.AlertAddFragment;
import com.example.uefi.seniorproject.alert.AlertFragment;
import com.example.uefi.seniorproject.alert.AppointmentAddFragment;
import com.example.uefi.seniorproject.alert.AppointmentFragment;
import com.example.uefi.seniorproject.alert.AppointmentItem;
import com.example.uefi.seniorproject.databases.InternalDatabaseHelper;

import java.util.ArrayList;

/**
 * Created by palida on 31-Mar-18.
 */

public class CustomAdapterNote  extends RecyclerSwipeAdapter<RecyclerView.ViewHolder> {
    private ArrayList mItems;
    private Context mContext;
    private FragmentManager fm;
    private final int NOTE_DAY_ITEM = 0;
    private final int NOTE_ITEM = 1;
    private final int EMPTY_ITEM = 2;
    public InternalDatabaseHelper internalDatabaseHelper;
    public Fragment fragment;

    public CustomAdapterNote(Context context, ArrayList dataset, FragmentManager fm,Fragment fragment) {
        mContext = context;
        mItems = dataset;
        this.fm = fm;
        this.fragment = fragment;
    }

    @Override
    public  int getItemViewType(int position){
        if(mItems.get(position) instanceof DayItem){
            return NOTE_DAY_ITEM;
        }else if (mItems.get(position) instanceof NoteItem){
            return NOTE_ITEM;
        }else if (mItems.get(position) instanceof EmptyItem){
            return EMPTY_ITEM;
        }else if (mItems.get(position) instanceof AppointmentItem){
            return NOTE_ITEM;
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if(viewType == NOTE_DAY_ITEM){
            final View v = inflater.inflate(R.layout.singlerow_note_day,parent,false);
            final DayHolder vHolder = new DayHolder(v);
            return vHolder;
        } else if(viewType == NOTE_ITEM){
            final View v = inflater.inflate(R.layout.singlerow_note_item,parent,false);
            final NoteHolder vHolder = new NoteHolder(v);
            if(fragment instanceof AlertFragment){
                vHolder.imageView2.setImageResource(R.drawable.icons_pill);
            }else if(fragment instanceof AppointmentFragment){
                vHolder.imageView2.setImageResource(R.drawable.icons_calendar);
            }
            return vHolder;
        }else if(viewType == EMPTY_ITEM){
            final View v = inflater.inflate(R.layout.singlerow_note_empty,parent,false);
            final EmptyHolder vHolder = new EmptyHolder(v);
            return vHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position) {
        int type = getItemViewType(position);
        if(type == NOTE_DAY_ITEM){
            DayItem item = (DayItem) mItems.get(position);
            DayHolder detailHolder = (DayHolder) holder;
            detailHolder.name.setText(item.text);
        }else if(type == NOTE_ITEM){
            final NoteItem item = (NoteItem) mItems.get(position);
            final NoteHolder detailHolder = (NoteHolder) holder;
            detailHolder.name.setText(item.text);
            detailHolder.note_id = item.note_id;
            if(item.getNumber() == 0){
                if(item.getDay()!=0){
                    String hour = item.getHour()+"";
                    String minute = item.getMinute()+"";
                    if(item.getHour()<9){
                        hour = "0"+hour;
                    }
                    if(item.getMinute()<9){
                        minute = "0" +minute;
                    }
                    detailHolder.medicine_num.setText(hour+":"+ minute +" น.");
                }
            }else if( item.getNumber() >0){
                detailHolder.medicine_num.setText(item.getNumber()+" เม็ด");
            }

            detailHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            //drag from right
            detailHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, detailHolder.swipeLayout.findViewById(R.id.bottom_wraper));

            //handling different event when swiping
            detailHolder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
                @Override
                public void onStartOpen(SwipeLayout layout) {
                    //when the SurfaceView totally cover the BottomView.
                }

                @Override
                public void onOpen(SwipeLayout layout) {
                    //when the BottomView totally show.
                }

                @Override
                public void onStartClose(SwipeLayout layout) {
                }

                @Override
                public void onClose(SwipeLayout layout) {
                    //when the SurfaceView totally cover the BottomView.
                }

                @Override
                public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                    //you are swiping.
                }

                @Override
                public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                    //when user's hand released.
                }
            });

            detailHolder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(fragment instanceof NotesFragment) {
                        fm.beginTransaction()
                                .replace(R.id.container_fragment, NoteAddFragment.newInstance("edit", item.note_id))
                                .addToBackStack("แก้ไขบันทึกสุขภาพ")
                                .commit();
                    }else if(fragment instanceof  AlertFragment){
                        fm.beginTransaction()
                                .replace(R.id.container_fragment, AlertAddFragment.newInstance("edit", item.note_id))
                                .addToBackStack("แก้ไขรายการยา")
                                .commit();
                    }else if(fragment instanceof AppointmentFragment){
                        fm.beginTransaction()
                                .replace(R.id.container_fragment, AppointmentAddFragment.newInstance("edit", item.note_id))
                                .addToBackStack("แก้ไขรายการนัดคุณหมอ")
                                .commit();
                    }
                }
            });

            detailHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    internalDatabaseHelper = InternalDatabaseHelper.getInstance(mContext);
                    internalDatabaseHelper.open();
                    mItemManger.removeShownLayouts(detailHolder.swipeLayout);

                    int prepType = getItemViewType(position-1);

                    if(fragment instanceof AlertFragment){
                        //alert
                        deleteDayItem(position);
                        ArrayList old = internalDatabaseHelper.readAlert(detailHolder.note_id);
                        int breakfast = Integer.parseInt(old.get(2).toString());
                        int lunch = Integer.parseInt(old.get(3).toString());
                        int dinner = Integer.parseInt(old.get(4).toString());
                        int bed = Integer.parseInt(old.get(5).toString());

                        if(item.getTime().equals("breakfast")) breakfast = 0;
                        else if (item.getTime().equals("lunch")) lunch = 0;
                        else if (item.getTime().equals("dinner")) dinner = 0;
                        else if (item.getTime().equals("bed")) bed = 0;

                        internalDatabaseHelper.deleteAlert(detailHolder.note_id,
                                old.get(0).toString(),
                                Integer.parseInt(old.get(1).toString()),
                                breakfast,
                                lunch,
                                dinner,
                                bed,
                                Integer.parseInt(old.get(6).toString()),
                                Integer.parseInt(old.get(7).toString()),
                                Integer.parseInt(old.get(8).toString())
                                );
                        ((AlertFragment)fragment).setupRecy();
                    }else {
                        if (prepType == NOTE_DAY_ITEM) {
                            if (position == getItemCount() - 1) {
                                deleteDayItem(position - 1); // day
                                deleteDayItem(position - 1); // note
                            } else {
                                int nextType = getItemViewType(position + 1);
                                if (nextType == NOTE_DAY_ITEM) {
                                    deleteDayItem(position - 1); // day
                                    deleteDayItem(position - 1); // note
                                } else {
                                    deleteDayItem(position); // note
                                }
                            }
                        } else {
                            deleteDayItem(position); // note
                        }
                        if(fragment instanceof NotesFragment) {
                            ((NotesFragment) fragment).showPic();
                            internalDatabaseHelper.deleteNote(detailHolder.note_id);
                        } else if(fragment instanceof AppointmentFragment) {
                            ((AppointmentFragment) fragment).showPic();
                            internalDatabaseHelper.deleteAppointment(detailHolder.note_id);
                        }
                    }

                        mItemManger.closeAllItems();
                    notifyDataSetChanged();

                }
            });
            mItemManger.bindView(detailHolder.itemView, position);
        }else if(type == EMPTY_ITEM){
            EmptyItem item = (EmptyItem) mItems.get(position);
            EmptyHolder detailHolder = (EmptyHolder) holder;
        }
    }

    public void deleteDayItem(int position){
        mItems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mItems.size());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    private class DayHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView name;

        public DayHolder(View itemView){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.day);

        }

        public void onClick(View view){

        }

    }

    private class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView name,tvDelete,medicine_num;
        private int note_id;
        public SwipeLayout swipeLayout;
        public ImageView imageView2;


        public NoteHolder (View itemView){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.note);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            tvDelete = (TextView) itemView.findViewById(R.id.tvDelete);
            imageView2 = (ImageView) itemView.findViewById(R.id.imageView2);
            medicine_num = (TextView) itemView.findViewById(R.id.medicine_num);
        }
        public void onClick(View view){
        }

    }

    private class EmptyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public EmptyHolder(View itemView){
            super(itemView);

        }

        public void onClick(View view){

        }

    }


}