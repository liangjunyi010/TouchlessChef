package app.touchlessChef.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Instruction implements Parcelable {
    private long id;
    private String content;
    private long recipeId;

    public Instruction(String content) {
        this.content = content;
    }

    public Instruction(long id, String content, long recipeId) {
        this.id = id;
        this.content = content;
        this.recipeId = recipeId;
    }

    protected Instruction(Parcel in) {
        id = in.readLong();
        content = in.readString();
        recipeId = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeLong(id);
        dest.writeString(content);
        dest.writeLong(recipeId);
    }

    public static final Creator<Instruction> CREATOR = new Creator<Instruction>() {
        @Override
        public Instruction createFromParcel(Parcel in) {
            return new Instruction(in);
        }

        @Override
        public Instruction[] newArray(int size) {
            return new Instruction[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }

    @NonNull
    @Override
    public String toString() {
        return "Instruction{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", recipeId=" + recipeId +
                '}';
    }
}
