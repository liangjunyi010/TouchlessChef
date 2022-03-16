package app.touchlessChef;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import app.touchlessChef.hands.HandsActivity;
import app.touchlessChef.template.MainActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button startHandsButton = findViewById(R.id.hands_tracking_mode_button);
        startHandsButton.setOnClickListener(
                v -> startActivity(
                        new Intent(HomeActivity.this, HandsActivity.class))
        );

        Button startRecipeButton = findViewById(R.id.standard_mode_button);
        startRecipeButton.setOnClickListener(
                v -> startActivity(
                        new Intent(HomeActivity.this, MainActivity.class))
        );
    }


}