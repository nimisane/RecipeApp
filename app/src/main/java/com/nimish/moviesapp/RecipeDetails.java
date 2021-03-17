package com.nimish.moviesapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.List;

import static com.nimish.moviesapp.TrendingRecipesFragment.DOC_ID;

public class RecipeDetails extends AppCompatActivity {

    ImageView recipeImg;
    TextView description;
    TextView ingredients;
    TextView recipe;
    TextView name;
    TextView type;
    String recipeDec;
    String recipeName;
    String recipeIngredients;
    String recipeSteps;
    String recipeImgUrl;
    String recipeType;
    List<String> recipeStepsList;
    String eatenAt;
    Boolean trending;
    String userEmail;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    CollectionReference favouriteRef = database.collection("favourite");
    private FirebaseAuth mAuth;
    private DocumentReference recipeRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        name = findViewById(R.id.name);
        type = findViewById(R.id.type);
        recipeImg = findViewById(R.id.recipe_img);
        description = findViewById(R.id.description_details);
        ingredients = findViewById(R.id.ingredients_details);
        recipe = findViewById(R.id.preparations_details);

        mAuth = FirebaseAuth.getInstance();
        userEmail = mAuth.getCurrentUser().getEmail();

        Intent i = getIntent();
        String rid = i.getStringExtra(DOC_ID);
        recipeName = i.getStringExtra("name");
        recipeDec = i.getStringExtra("desc");
        recipeImgUrl = i.getStringExtra("imgUrl");
        recipeIngredients = i.getStringExtra("ingredients");
        recipeType = i.getStringExtra("recipeType");
        recipeSteps = i.getStringExtra("preparation");
        recipeStepsList = i.getStringArrayListExtra("preparationSteps");
        eatenAt = i.getStringExtra("eatenAt");
        trending = i.getBooleanExtra("trending",false);

        Glide.with(getApplicationContext()).load(recipeImgUrl).into(recipeImg);
        name.setText(recipeName);
        description.setText(recipeDec);
        ingredients.setText(recipeIngredients);
        recipe.setText(recipeSteps);

        if(recipeType.equals("Veg") || recipeType.equals("Vegan")){
            type.setTextColor(Color.GREEN);
        }else{
            type.setTextColor(Color.RED);
        }

        type.setText(recipeType);


        recipeRef = database.collection("receipes").document(rid);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.favourite_recipes:
                addItemToFavourite();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void addItemToFavourite(){
        FavouriteItems favouriteItems = new FavouriteItems(recipeName,recipeDec,recipeType,recipeIngredients,recipeImgUrl,eatenAt,trending,recipeStepsList,userEmail);


        favouriteRef.add(favouriteItems)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(),"Added to favourites",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
                    }
                });
    }
}