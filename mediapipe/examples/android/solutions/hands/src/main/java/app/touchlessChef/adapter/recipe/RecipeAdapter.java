package app.touchlessChef.adapter.recipe;

import android.content.Context;
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

    private List<Recipe> recipeList;
    private Context myContext;
    private RecipeListener recipeListener;

    public RecipeAdapter(Context myContext, List<Recipe> recipeList) {
        this.myContext = myContext;
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
        ImageView overflow;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            titleLabel = itemView.findViewById(R.id.titleLabel);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            overflow = itemView.findViewById(R.id.overflow);

            itemView.setOnClickListener(v -> {
                if (recipeListener != null)
                    recipeListener.onShowRecipe(recipeList.get(getAdapterPosition()), new Pair[]{
                            Pair.create(thumbnail, "image_shared")
                    });
            });
            overflow.setOnClickListener(this::showPopupMenu);
        }

        public void bind(Recipe recipe) {
            titleLabel.setText(recipe.getName());
            thumbnail.setImageURI(Uri.fromFile(new File(recipe.getImagePath())));
        }

        private void showPopupMenu(View view) {
            PopupMenu popup = new PopupMenu(view.getContext(), view);
            popup.inflate(R.menu.menu_grid_layout);
            popup.setOnMenuItemClickListener(
                    new MyMenuItemClickListener(recipeList.get(getAdapterPosition())));
            popup.show();
        }

        class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

            private Recipe recipe;

            public MyMenuItemClickListener(Recipe recipe) {
                this.recipe = recipe;
            }

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_edit_recipe:
                        if (recipeListener != null)
                            recipeListener.onEditRecipe(recipe);
                        break;
                    case R.id.action_delete_recipe:
                        if (recipeListener != null)
                            recipeListener.onDeleteRecipe(recipe.getId());
                        return true;
                    default:
                }
                return false;
            }
        }

    }
}
