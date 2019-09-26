package tec.findmyrestaurant.view.schedule;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tec.findmyrestaurant.R;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder>{

    private Context mContext;
    private List<String> mDays;
    private String[][] times;
    private View view;
    String shorts[]={"Sun","Mon","Tue","Wed", "Thu","Fri","Sat"};

    public ScheduleAdapter(Context mContext, List<String> mDays) {
        this.mContext = mContext;
        this.mDays = mDays;
        times = new String[mDays.size()][2];
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.schedule_item,parent,false);
        return new ScheduleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        String day = mDays.get(position);
        holder.txtDay.setText(day);
        ArrayAdapter arrayAdapter1 = ArrayAdapter.createFromResource(mContext,R.array.times,R.layout.spinner_item);
        ArrayAdapter arrayAdapter2 = ArrayAdapter.createFromResource(mContext,R.array.times,R.layout.spinner_item);
        holder.spinnerBegin.setAdapter(arrayAdapter1);
        holder.spinnerEnd.setAdapter(arrayAdapter2);
        times[position][0]="Closed";
        times[position][1]="-";
        holder.chbDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    times[position][0] = (String) holder.spinnerBegin.getSelectedItem();
                    times[position][1] = "-"+(String) holder.spinnerEnd.getSelectedItem();
                    holder.lytTimes.setVisibility(View.VISIBLE);
                    holder.txtInfo.setPaintFlags(holder.txtInfo.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                }else{
                    times[position][0]="Closed";
                    times[position][1]="-";
                    holder.lytTimes.setVisibility(View.GONE);
                    holder.txtInfo.setPaintFlags(holder.txtInfo.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
            }
        });
        holder.spinnerBegin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                times[position][0] = (String) holder.spinnerBegin.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        holder.spinnerEnd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                times[position][1] = "-"+(String) holder.spinnerEnd.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mDays.size();
    }

    public String getSchedule(){
        String schedule = "";
        String day = mDays.get(0);
        String last = mDays.get(0);
        String state="";
        if(times[0].equals("Closed"))
            state = "Closed";
        else{
            state = times[0][0]+times[0][1];
        }
        for(int i=1;i<mDays.size();i++){
            if(!state.equals(times[i][0]+times[i][1])){
                if(day.equals(last)){
                    schedule += day+": "+state+"\n";
                }else{
                    schedule+= day+"-"+last+": "+state+"\n";
                }
                day = mDays.get(i);
                state = times[i][0]+times[i][1];
            }
            last = mDays.get(i);
        }

        if(day.equals(last)){
            schedule += day+": "+state;
        }else{
            schedule+= day+"-"+last+": "+state;
        }

        return schedule.replace("Closed-","Closed");
    }



    class ViewHolder extends RecyclerView.ViewHolder{
        public CheckBox chbDay;
        public TextView txtDay;
        public TextView txtInfo;
        public LinearLayout lytTimes;
        public Spinner spinnerBegin;
        public Spinner spinnerEnd;

        public ViewHolder(View view){
            super(view);
            chbDay = view.findViewById(R.id.chbDay);
            txtDay = view.findViewById(R.id.txtDay);
            txtInfo = view.findViewById(R.id.txtInfo);
            lytTimes = view.findViewById(R.id.lytTimes);
            spinnerBegin = view.findViewById(R.id.spinnerBegin);
            spinnerEnd = view.findViewById(R.id.spinnerEnd);
        }
    }
}
