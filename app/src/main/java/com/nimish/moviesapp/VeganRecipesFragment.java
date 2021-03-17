package com.nimish.moviesapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.nimish.moviesapp.TrendingRecipesFragment.DOC_ID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VeganRecipesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VeganRecipesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VeganRecipesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VeganRecipesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VeganRecipesFragment newInstance(String param1, String param2) {
        VeganRecipesFragment fragment = new VeganRecipesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    RecyclerView veganRecyclerview;
    private TrendingRecipesAdapter trendingRecipesAdapter;
    private ArrayList<RecipeItems> recipeItems;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference recipeRef;
    RecipeItems complete_item_details;
    String rid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_vegan_recipes, container, false);

        veganRecyclerview = rootView.findViewById(R.id.vegan_recyclerview);

        recipeRef = database.collection("receipes");
        veganRecyclerview.setHasFixedSize(true);
        veganRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recipeItems = new ArrayList<>();
        loadRecipes();

        return rootView;
    }

    private void loadRecipes() {
        recipeRef.orderBy("recipeName").whereEqualTo("recipeType","Vegan").get()
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

                        trendingRecipesAdapter = new TrendingRecipesAdapter(getContext(), recipeItems);
                        veganRecyclerview.setAdapter(trendingRecipesAdapter);

                        trendingRecipesAdapter.setOnItemClickListener(new TrendingRecipesAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                String recipeSteps ="";
                                complete_item_details = recipeItems.get(position);
                                for ( String steps : complete_item_details.getPreparation()){
                                    recipeSteps += steps + "\n\n";
                                }
                                Intent i = new Intent(getContext(), RecipeDetails.class);
                                i.putExtra("name",complete_item_details.getRecipeName());
                                i.putExtra("desc",complete_item_details.getRecipeDescription());
                                i.putExtra("ingredients",complete_item_details.getIngredients());
                                i.putExtra("imgUrl",complete_item_details.getImgUrl());
                                i.putExtra("preparation",recipeSteps);
                                i.putExtra("recipeType",complete_item_details.getRecipeType());
                                i.putStringArrayListExtra("preparationSteps",(ArrayList<String>)complete_item_details.getPreparation());
                                i.putExtra("eatenAt",complete_item_details.getEatenAt());
                                i.putExtra("trending",complete_item_details.getTrending());
                                i.putExtra(DOC_ID,rid);
                                startActivity(i);
                            }
                        });

                    }
                });
    }
}