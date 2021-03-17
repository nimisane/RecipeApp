package com.nimish.moviesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.nimish.moviesapp.TrendingRecipesFragment.DOC_ID;

public class FavouriteRecipesActivity extends AppCompatActivity {

    RecyclerView favRecyclerview;
    private TrendingRecipesAdapter trendingRecipesAdapter;
    private ArrayList<RecipeItems> recipeItems;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference recipeRef;
    private FirebaseAuth mAuth;
    String rid;
    String userEmail;
    RecipeItems complete_item_details;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_recipes);

        mAuth = FirebaseAuth.getInstance();
        userEmail = mAuth.getCurrentUser().getEmail();

        favRecyclerview = findViewById(R.id.favourite_recipes_recycler);
        recipeRef = database.collection("favourite");
        favRecyclerview.setHasFixedSize(true);
        favRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        recipeItems = new ArrayList<>();
        loadRecipes();
    }

    private void loadRecipes() {
        recipeRef.orderBy("recipeName").whereEqualTo("userEmail",userEmail).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        recipeItems.clear();
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            RecipeItems recipes = documentSnapshot.toObject(RecipeItems.class);

                            rid = documentSnapshot.getId();
                            String recipeName = recipes.getRecipeName();
                            String recipeDescription = recipes.getRecipeDescription();
                            String recipeType = recipes.getRecipeType();
                            String ingredients = recipes.getIngredients();
                            String imgUrl = recipes.getImgUrl();
                            String eatenAt = recipes.getEatenAt();
                            Boolean trending = recipes.getTrending();
                            List<String> steps = recipes.getPreparation();

                            recipeItems.add(new RecipeItems(recipeName,recipeDescription,recipeType,ingredients,imgUrl,eatenAt,trending,steps));
                        }

                        trendingRecipesAdapter = new TrendingRecipesAdapter(getApplicationContext(), recipeItems);
                        favRecyclerview.setAdapter(trendingRecipesAdapter);

                        trendingRecipesAdapter.setOnItemClickListener(new TrendingRecipesAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                String recipeSteps ="";
                                complete_item_details = recipeItems.get(position);
                                for ( String steps : complete_item_details.getPreparation()){
                                    recipeSteps += steps + "\n\n";
                                }
                                Intent i = new Intent(getApplicationContext(), FavouriteRecipesDetails.class);
                                i.putExtra("name",complete_item_details.getRecipeName());
                                i.putExtra("desc",complete_item_details.getRecipeDescription());
                                i.putExtra("ingredients",complete_item_details.getIngredients());
                                i.putExtra("imgUrl",complete_item_details.getImgUrl());
                                i.putExtra("preparation",recipeSteps);
                                i.putExtra("recipeType",complete_item_details.getRecipeType());

                                i.putExtra(DOC_ID,rid);
                                startActivity(i);
                            }
                        });

                    }
                });
    }
}