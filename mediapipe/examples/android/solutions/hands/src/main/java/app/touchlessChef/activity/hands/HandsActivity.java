// Copyright 2021 The MediaPipe Authors.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package app.touchlessChef.activity.hands;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import app.touchlessChef.R;
import com.google.mediapipe.formats.proto.LandmarkProto.Landmark;
import com.google.mediapipe.formats.proto.LandmarkProto.NormalizedLandmark;
import com.google.mediapipe.solutioncore.CameraInput;
import com.google.mediapipe.solutioncore.SolutionGlSurfaceView;
import com.google.mediapipe.solutions.hands.HandLandmark;
import com.google.mediapipe.solutions.hands.Hands;
import com.google.mediapipe.solutions.hands.HandsOptions;
import com.google.mediapipe.solutions.hands.HandsResult;

/** Main activity of MediaPipe Hands app. */
public class HandsActivity extends AppCompatActivity {
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
  private SolutionGlSurfaceView<HandsResult> glSurfaceView;

  private static boolean stopTracking = false;
  ScrollView scrollView ;
//  TextView curTextView = findViewById(R.id.textView0);

  int curY = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
//    setContentView(R.layout.activity_hands);
    setupLiveDemoUiComponents();
    scrollView = findViewById(R.id.scrollView);


  }

  @Override
  protected void onResume() {
    super.onResume();
//    if (inputSource == InputSource.CAMERA) {
//      // Restarts the camera and the opengl surface rendering.
//      cameraInput = new CameraInput(this);
//      cameraInput.setNewFrameListener(textureFrame -> hands.send(textureFrame));
//      glSurfaceView.post(this::startCamera);
//      glSurfaceView.setVisibility(View.VISIBLE);
//    }
  }

  @Override
  protected void onPause() {
    super.onPause();
//    if (inputSource == InputSource.CAMERA) {
//      glSurfaceView.setVisibility(View.GONE);
//      cameraInput.close();
//    }
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
    //relativelayout.removeAllViewsInLayout();

    relativelayout.addView(glSurfaceView);
    glSurfaceView.setVisibility(View.VISIBLE);
//    relativelayout.requestLayout();
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
    NormalizedLandmark index_finger_tipLandmark =
            result.multiHandLandmarks().get(0).getLandmarkList().get(HandLandmark.INDEX_FINGER_TIP);
    // pos 7
    NormalizedLandmark index_finger_dipLandmark =
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
        scrollView.post(new Runnable() {
          public void run() {
            curY-=200;
            scrollView.smoothScrollTo(0, curY);
          }
        });
        Log.i(
                TAG,
                "UP"
        );
      }
      else{
        scrollView.post(new Runnable() {
          public void run() {
            curY+=200;
            scrollView.smoothScrollTo(0, curY);
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
    Landmark index_finger_tipWorldLandmark =
            result.multiHandWorldLandmarks().get(0).getLandmarkList().get(HandLandmark.INDEX_FINGER_TIP);
    Log.i(
            TAG,
            String.format(
                    "MediaPipe Hand index_finger_tipWorldLandmark world coordinates (in meters with the origin at the hand's"
                            + " approximate geometric center): x=%f m, y=%f m, z=%f m",
                    index_finger_tipWorldLandmark.getX(), index_finger_tipWorldLandmark.getY(), index_finger_tipWorldLandmark.getZ()));
  }
}
