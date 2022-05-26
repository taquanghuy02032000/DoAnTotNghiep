package online.javalab.poly.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import online.javalab.poly.R;
import online.javalab.poly.model.rank.Datum;
import online.javalab.poly.model.rank.Ratings;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder> {
    private List<Datum> mRatingsList;

    public void setData(List<Datum> mRatingsList) {
        this.mRatingsList = mRatingsList;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ratings,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int st = position+1;
        if (mRatingsList == null) return;
        Picasso.get().load(mRatingsList.get(position).getImageUrl()).into(holder.mAvata);
        holder.mName.setText(mRatingsList.get(position).getUsername());
        holder.mPoint.setText(mRatingsList.get(position).getMark()+" score");
        if(st == 1){
            holder.mST.setTextColor(Color.RED);
        }else if (st == 2){
            holder.mST.setTextColor(Color.BLUE);
        }else if (st == 3){
            holder.mST.setTextColor(Color.GREEN);
        }
        holder.mST.setText(""+st);
    }

    @Override
    public int getItemCount() {
        if (mRatingsList == null){
            return 0;
        }

        return mRatingsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mAvata;
        private TextView mName;
        private TextView mPoint;
        private TextView mST;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mAvata = itemView.findViewById(R.id.item_ratins_avt_img);
            mName = itemView.findViewById(R.id.item_ratings_name_txt);
            mPoint = itemView.findViewById(R.id.item_ratings_point_txt);
            mST = itemView.findViewById(R.id.item_ratings_stt_txt);
        }
    }
}
