package app.touchlessChef;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import app.touchlessChef.activity.hands.HandsActivity;
import app.touchlessChef.activity.standard.home.MainActivity;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button startHandsButton = findViewById(R.id.hands_tracking_mode_button);
        startHandsButton.setOnClickListener(
                v -> startActivity(
                        new Intent(StartActivity.this, HandsActivity.class))
        );

        Button startRecipeButton = findViewById(R.id.standard_mode_button);
        startRecipeButton.setOnClickListener(
                v -> startActivity(
                        new Intent(StartActivity.this, MainActivity.class))
        );
    }


}