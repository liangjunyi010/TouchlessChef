package app.touchlessChef.adapter.recipe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.touchlessChef.model.Ingredient;
import app.touchlessChef.R;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>{
    private final List<Ingredient> ingredientList;
    private boolean isEditable = true;
    private IngredientListener ingredientListener;

    public IngredientAdapter(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public IngredientAdapter(List<Ingredient> ingredientList, boolean isEditable) {
        this.ingredientList = ingredientList;
        this.isEditable = isEditable;
    }

    @Override
    public int getItemViewType(int position) {
        return isEditable ? 0 : 1;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(viewType == 0 ? R.layout.adapter_recipe_ingredient_item_row_editable
                                : R.layout.adapter_recipe_ingredient_item_row_non_editable,
                        parent, false);
        return new IngredientViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final IngredientViewHolder holder, int position) {
        Ingredient ingredient = ingredientList.get(position);
        holder.bind(ingredient);
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        TextView ingredientText;
        ImageView wasteBin;

        public IngredientViewHolder(View itemView) {
            super(itemView);

            ingredientText = itemView.findViewById(R.id.ingredientText);
            if (isEditable) {
                wasteBin = itemView.findViewById(R.id.wasteBin);
                wasteBin.setOnClickListener(v -> {
                    if (ingredientListener != null)
                        ingredientListener.onDeleteIngredient(getAdapterPosition());
                });
            }
        }

        public void bind(Ingredient ingredient) {
            ingredientText.setText(ingredient.getName());
        }
    }

    public void setIngredientListener(IngredientListener ingredientListener) {
        this.ingredientListener = ingredientListener;
    }

    public interface IngredientListener {
        void onDeleteIngredient(int position);
    }
}
