package app.touchlessChef.activity.standard.recipe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.mediapipe.formats.proto.LandmarkProto;
import com.google.mediapipe.solutioncore.CameraInput;
import com.google.mediapipe.solutioncore.SolutionGlSurfaceView;
import com.google.mediapipe.solutions.hands.HandLandmark;
import com.google.mediapipe.solutions.hands.Hands;
import com.google.mediapipe.solutions.hands.HandsOptions;
import com.google.mediapipe.solutions.hands.HandsResult;

import java.io.File;
import java.util.Objects;

import app.touchlessChef.activity.hands.HandsResultGlRenderer;
import app.touchlessChef.constants.RecipeConstants;
import app.touchlessChef.fragment.recipe.RecipeViewFragment;
import app.touchlessChef.model.Recipe;
import app.touchlessChef.R;
import app.touchlessChef.activity.standard.MenuActivity;
import app.touchlessChef.constants.RecipeEditConstants;
import android.widget.ScrollView;

public class ViewRecipeActivity extends MenuActivity {
    private Recipe recipe;

    private ImageView mRecipeImage;
    private TextView mRecipeDescription;
    private TextView mRecipeTime;
    private TextView mRecipeType;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    //new
    private static final String TAG = "MainActivity";
    private Hands hands;
    // Run the pipeline and the model inference on GPU or CPU.
    private static final boolean RUN_ON_GPU = true;
    private enum InputSource {
        UNKNOWN,
        CAMERA,
    }
    private InputSource inputSource = InputSource.UNKNOWN;
    // Live camera demo UI and camera components.
    private CameraInput cameraInput;
    public static SolutionGlSurfaceView<HandsResult> glSurfaceView;

    private static boolean stopTracking = false;
    ScrollView nestedScrollView;
//  TextView curTextView = findViewById(R.id.textView0);

    int curY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        recipe = getIntent().getParcelableExtra("recipe");
        findViewsById();

        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(recipe.getName());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        String imgPath = recipe.getImagePath();
        if (!imgPath.equals("default")) {
            mRecipeImage.setImageURI(Uri.fromFile(new File(imgPath)));
        } else {
            @SuppressLint("UseCompatLoadingForDrawables")
            Drawable mDrawable = mRecipeImage.getResources().getDrawable(RecipeConstants.DEFAULT_IMAGE);
            mRecipeImage.setImageDrawable(mDrawable);
        }
        mRecipeDescription.setText(recipe.getDescription());
        mRecipeTime.setText(recipe.getTime());
        mRecipeType.setText(recipe.getMealType());

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");
        mCollapsingToolbarLayout.setCollapsedTitleTypeface(font);
        mCollapsingToolbarLayout.setExpandedTitleTypeface(font);

        getSupportFragmentManager().beginTransaction().replace(R.id.recipe_view_holder,
                RecipeViewFragment.newInstance(recipe.getIngredients(),
                        recipe.getInstructions())).commit();

        setupLiveDemoUiComponents();
        nestedScrollView = findViewById(R.id.nestedScrollView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_recipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_recipe:
                Intent intent = new Intent();
                intent.putExtra("recipe", recipe);
                setResult(RecipeEditConstants.RECIPE_SHOULD_BE_EDITED, intent);
                finish();
                break;
            case R.id.delete_recipe:
                Intent data = new Intent();
                data.putExtra("recipeId", recipe.getId());
                setResult(RecipeEditConstants.RECIPE_SHOULD_BE_DELETED, data);
                finish();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void findViewsById() {
        mRecipeImage = findViewById(R.id.recipe_image);
        mRecipeDescription = findViewById(R.id.recipe_description);
        mRecipeTime = findViewById(R.id.time);
        mRecipeType = findViewById(R.id.mealType);
        mToolbar = findViewById(R.id.toolbar);
        mCollapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
    }
    /** Sets up the UI components for the live demo with camera input. */
    private void setupLiveDemoUiComponents() {
        Button startCameraButton = findViewById(R.id.button_start_camera);
        startCameraButton.setOnClickListener(
                v -> {
                    if (inputSource == InputSource.CAMERA) return;
                    if (!stopTracking) stopCurrentPipeline();
                    setupStreamingModePipeline(InputSource.CAMERA);
                });

        Button stopCameraButton = findViewById(R.id.button_stop_camera);
        stopCameraButton.setOnClickListener(
                v -> {
                    if (inputSource == InputSource.UNKNOWN) return;
                    inputSource = InputSource.UNKNOWN;
                    stopCurrentPipeline();
                    stopTracking = true;
                });
    }

    /** Sets up core workflow for streaming mode. */
    private void setupStreamingModePipeline(InputSource inputSource) {
        this.inputSource = inputSource;
        // Initializes a new MediaPipe Hands solution instance in the streaming mode.
        hands =
                new Hands(
                        this,
                        HandsOptions.builder()
                                .setStaticImageMode(false)
                                .setMaxNumHands(2)
                                .setRunOnGpu(RUN_ON_GPU)
                                .build());
        hands.setErrorListener((message, e) -> Log.e(TAG, "MediaPipe Hands error:" + message));

        if (inputSource == InputSource.CAMERA) {
            cameraInput = new CameraInput(this);
            cameraInput.setNewFrameListener(textureFrame -> hands.send(textureFrame));
        }

        // Initializes a new Gl surface view with a user-defined HandsResultGlRenderer.
        glSurfaceView =
                new SolutionGlSurfaceView<>(this, hands.getGlContext(), hands.getGlMajorVersion());
        glSurfaceView.setSolutionResultRenderer(new HandsResultGlRenderer());
        glSurfaceView.setRenderInputImage(true);
        hands.setResultListener(
                handsResult -> {
                    logWristLandmark(handsResult, /*showPixelValues=*/ false);
                    glSurfaceView.setRenderData(handsResult);
                    glSurfaceView.requestRender();
                });

        // The runnable to start camera after the gl surface view is attached.
        // For video input source, videoInput.start() will be called when the video uri is available.
        if (inputSource == InputSource.CAMERA) {
            glSurfaceView.post(this::startCamera);
        }

//     Updates the preview layout.
        RelativeLayout relativelayout = findViewById(R.id.preview_display_layout);
        relativelayout.removeAllViewsInLayout();

        relativelayout.addView(glSurfaceView);
        glSurfaceView.setVisibility(View.VISIBLE);
        relativelayout.requestLayout();
    }

    private void startCamera() {
        cameraInput.start(
                this,
                hands.getGlContext(),
                CameraInput.CameraFacing.FRONT,
                glSurfaceView.getWidth(),
                glSurfaceView.getHeight());
    }

    private void stopCurrentPipeline() {
        if (cameraInput != null) {
            cameraInput.setNewFrameListener(null);
            cameraInput.close();
        }
        if (glSurfaceView != null) {
            glSurfaceView.setVisibility(View.GONE);
        }
        if (hands != null) {
            hands.close();
        }
    }

    private void logWristLandmark(HandsResult result, boolean showPixelValues) {
        if (result.multiHandLandmarks().isEmpty()) {
            return;
        }
        // pos 8
        LandmarkProto.NormalizedLandmark index_finger_tipLandmark =
                result.multiHandLandmarks().get(0).getLandmarkList().get(HandLandmark.INDEX_FINGER_TIP);
        // pos 7
        LandmarkProto.NormalizedLandmark index_finger_dipLandmark =
                result.multiHandLandmarks().get(0).getLandmarkList().get(HandLandmark.INDEX_FINGER_DIP);

        // For Bitmaps, show the pixel values. For texture inputs, show the normalized coordinates.
        if (showPixelValues) {
            int width = result.inputBitmap().getWidth();
            int height = result.inputBitmap().getHeight();

            Log.i(
                    TAG,
                    String.format(
                            "MediaPipe Hand index_finger_tipLandmark coordinates (pixel values): x=%f, y=%f \n MediaPipe Hand index_finger_dipLandmark coordinates (pixel values): x=%f, y=%f",
                            index_finger_tipLandmark.getX() * width, index_finger_tipLandmark.getY() * height,index_finger_dipLandmark.getX() * width, index_finger_dipLandmark.getY() * height));
        } else {

            if(index_finger_dipLandmark.getY() -index_finger_tipLandmark.getY()>0){
                nestedScrollView.post(new Runnable() {
                    public void run() {

                        curY-=200;
                        nestedScrollView.smoothScrollTo(0, curY);
                    }
                });
                Log.i(
                        TAG,
                        "UP"
                );
            }
            else{
                nestedScrollView.post(new Runnable() {
                    public void run() {

                        curY+=200;
                        nestedScrollView.smoothScrollTo(0, curY);
                    }
                });
                Log.i(
                        TAG,
                        "DOWN"
                );
            }
            Log.i(
                    TAG,
                    String.format(
                            "MediaPipe Hand index_finger_tipLandmark normalized coordinates (value range: [0, 1]): x=%f, y=%f \n MediaPipe Hand index_finger_dipLandmark normalized coordinates (value range: [0, 1]): x=%f, y=%f",
                            index_finger_tipLandmark.getX(), index_finger_tipLandmark.getY(),index_finger_dipLandmark.getX(),index_finger_dipLandmark.getY()));
        }
        if (result.multiHandWorldLandmarks().isEmpty()) {
            return;
        }
        LandmarkProto.Landmark index_finger_tipWorldLandmark =
                result.multiHandWorldLandmarks().get(0).getLandmarkList().get(HandLandmark.INDEX_FINGER_TIP);
        Log.i(
                TAG,
                String.format(
                        "MediaPipe Hand index_finger_tipWorldLandmark world coordinates (in meters with the origin at the hand's"
                                + " approximate geometric center): x=%f m, y=%f m, z=%f m",
                        index_finger_tipWorldLandmark.getX(), index_finger_tipWorldLandmark.getY(), index_finger_tipWorldLandmark.getZ()));
    }


}