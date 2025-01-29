package com.example.finalproject;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


public class CourseAdapter extends RecyclerView.Adapter<MyViewHolderCourse> {
    private Context context;
    private List<DataCourseClass> courseList;
    public void setSearchList(List<DataCourseClass> dataSearchList){
        this.courseList = dataSearchList;
        notifyDataSetChanged();
    }
    public CourseAdapter(Context context, List<DataCourseClass> dataList){
        this.context = context;
        this.courseList = dataList;
    }
    @NonNull
    @Override
    public MyViewHolderCourse onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_recycler_item, parent, false);
        return new MyViewHolderCourse(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolderCourse holder, int position) {
        holder.recImage.setImageResource(courseList.get(position).getCourseImage());
        holder.recTitle.setText(courseList.get(position).getCourseTitle());
        holder.recDesc.setText(courseList.get(position).getCourseDesc());
        holder.recLang.setText(courseList.get(position).getCourseLang());
        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CourseDetail.class);
                intent.putExtra("Image", courseList.get(holder.getAdapterPosition()).getCourseImage());
                intent.putExtra("Title", courseList.get(holder.getAdapterPosition()).getCourseTitle());
                intent.putExtra("Desc", courseList.get(holder.getAdapterPosition()).getCourseDesc());
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return courseList.size();
    }
}
class MyViewHolderCourse extends RecyclerView.ViewHolder{
    ImageView recImage;
    TextView recTitle, recDesc, recLang;
    CardView recCard;
    public MyViewHolderCourse(@NonNull View itemView) {
        super(itemView);
        recImage = itemView.findViewById(R.id.couImage);
        recTitle = itemView.findViewById(R.id.couTitle);
        recDesc = itemView.findViewById(R.id.couDesc);
        recLang = itemView.findViewById(R.id.couLang);
        recCard = itemView.findViewById(R.id.couCard);
    }
}