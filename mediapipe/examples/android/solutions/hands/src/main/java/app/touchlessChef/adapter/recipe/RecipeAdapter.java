package app.touchlessChef.adapter.recipe;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

import app.touchlessChef.R;
import app.touchlessChef.model.Recipe;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>{
    public interface RecipeListener {
        void onShowRecipe(Recipe recipe, Pair<View, String>[] pairs);
        void onEditRecipe(Recipe recipe);
        void onDeleteRecipe(long recipeId);
    }

    private final List<Recipe> recipeList;
    private RecipeListener recipeListener;

    public RecipeAdapter(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    public void setRecipeListener(RecipeListener recipeListener) {
        this.recipeListener = recipeListener;
    }

    @NonNull
    @Override
    public RecipeAdapter.RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_recipe_items, parent, false);
        return new RecipeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.bind(recipe);
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView titleLabel;
        ImageView thumbnail;
        TextView time;
        TextView mealType;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            titleLabel = itemView.findViewById(R.id.titleLabel);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            time = itemView.findViewById(R.id.time);
            mealType = itemView.findViewById(R.id.mealType);

            itemView.setOnClickListener(v -> {
                if (recipeListener != null)
                    recipeListener.onShowRecipe(recipeList.get(getAdapterPosition()), new Pair[]{
                            Pair.create(thumbnail, "image_shared")
                    });
            });
        }

        public void bind(Recipe recipe) {
            titleLabel.setText(recipe.getName());
            thumbnail.setImageURI(Uri.fromFile(new File(recipe.getImagePath())));
//            time.setText(recipe.getTime());
//            mealType.setText(recipe.getMealType());
        }

    }
}
