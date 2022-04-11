package app.touchlessChef.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable {
    private long id;
    private String name;
    private String category;
    private String description;
    private List<Ingredient> ingredients;
    private List<Instruction> instructions;
    private String imagePath;
    private String mealType;
    private String time;

    public Recipe() {
        ingredients = new ArrayList<>();
        instructions = new ArrayList<>();
    }

    public Recipe(String category) {
        this();
        this.category = category;
    }

    public Recipe(String name, String category, String description, String imagePath, String time,
                  String mealType) {
        this(category);
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
        this.time = time;
        this.mealType = mealType;
    }

    public Recipe(long id, String name, String category, String description, String imagePath,
                  String time, String mealType) {
        this(name, category, description, imagePath, time, mealType);
        this.id = id;
    }

    public Recipe(String name, String category, String description,
                  List<Ingredient> ingredients, List<Instruction> instructions, String imagePath,
                  String mealType, String time) {
        this(name, category, description, imagePath, time, mealType);
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.time = time;
        this.mealType = mealType;
    }

    public Recipe(long id, String name, String category, String description,
                  List<Ingredient> ingredients, List<Instruction> instructions, String imagePath,
                  String mealType, String time) {
        this(name, category, description, ingredients, instructions, imagePath, mealType, time);
        this.id = id;
    }

    protected Recipe(Parcel in) {
        ingredients = new ArrayList<>();
        instructions = new ArrayList<>();

        id = in.readLong();
        name = in.readString();
        category = in.readString();
        description = in.readString();
        in.readTypedList(ingredients, Ingredient.CREATOR);
        in.readTypedList(instructions, Instruction.CREATOR);
        imagePath = in.readString();
        time = in.readString();
        mealType = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(category);
        dest.writeString(description);
        dest.writeTypedList(ingredients);
        dest.writeTypedList(instructions);
        dest.writeString(imagePath);
        dest.writeString(time);
        dest.writeString(mealType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<Instruction> instructions) {
        this.instructions = instructions;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @NonNull
    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", ingredients=" + ingredients +
                ", instructions=" + instructions +
                ", imagePath='" + imagePath + '\'' +
                ", mealType='" + mealType + '\'' +
                ", time=" + time +
                '}';
    }
}
