package com.nimish.moviesapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class TrendingRecipesAdapter extends RecyclerView.Adapter<TrendingRecipesAdapter.TrendingRecipesViewHolder> {

    private Context context;
    private ArrayList<RecipeItems> recipeItems;
    private OnItemClickListener mlistener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mlistener = listener;
    }


    public TrendingRecipesAdapter(Context context, ArrayList<RecipeItems> recipeItems){
        this.context = context;
        this.recipeItems = recipeItems;
    }

    @NonNull
    @Override
    public TrendingRecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout,parent,false);
        return new TrendingRecipesViewHolder(view,mlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull TrendingRecipesViewHolder holder, int position) {
        RecipeItems currentItem = recipeItems.get(position);
        String recipeType = currentItem.getRecipeType();
        holder.cardRecipeName.setText(currentItem.getRecipeName());
        holder.cardRecipeDes.setText(currentItem.getRecipeDescription());
        holder.cardRecipeType.setText(currentItem.getRecipeType());
        String imgUrl = currentItem.getImgUrl();
        Glide.with(context).load(imgUrl).into(holder.cardRecipeImg);
        if(recipeType.equals("Veg") || recipeType.equals("Vegan")){
            holder.cardRecipeType.setTextColor(Color.GREEN);
        }else{
            holder.cardRecipeType.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return recipeItems.size();
    }

    public static class TrendingRecipesViewHolder extends RecyclerView.ViewHolder{

        ImageView cardRecipeImg;
        TextView cardRecipeName;
        TextView cardRecipeDes;
        TextView cardRecipeType;
        public TrendingRecipesViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            cardRecipeImg = itemView.findViewById(R.id.card_recipe_img);
            cardRecipeName = itemView.findViewById(R.id.card_recipe_title);
            cardRecipeDes = itemView.findViewById(R.id.card_recipe_desc);
            cardRecipeType = itemView.findViewById(R.id.card_recipe_type);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }
}
