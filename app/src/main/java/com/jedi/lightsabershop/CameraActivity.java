package com.jedi.lightsabershop;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;

import net.coobird.thumbnailator.Thumbnails;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CameraActivity extends BaseActivity {
  private static final String TAG = "CameraXApp";
  private static final String FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";
  private static final String[] REQUIRED_PERMISSIONS = new String[]{
      Manifest.permission.CAMERA,
  };
  private ImageCapture imageCapture;
  private ExecutorService cameraExecutor;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_camera);

    if (allPermissionsGranted()) {
      startCamera();
    } else {
      requestPermissions();
    }

    Button imageCaptureButton = findViewById(R.id.image_capture_button);
    imageCaptureButton.setOnClickListener(v -> takePhoto());

    cameraExecutor = Executors.newSingleThreadExecutor();
  }

  private void takePhoto() {
    if (imageCapture == null) return;

    String name = new SimpleDateFormat(FILENAME_FORMAT, Locale.US)
        .format(System.currentTimeMillis());

    ContentValues contentValues = new ContentValues();
    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
      contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image");
    }

    ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions
        .Builder(getContentResolver(),
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        contentValues)
        .build();

    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(this),
        new ImageCapture.OnImageSavedCallback() {
          @Override
          public void onError(@NonNull ImageCaptureException exc) {
            Log.e(TAG, "Photo capture failed: " + exc.getMessage(), exc);
          }

          @Override
          public void onImageSaved(@NonNull ImageCapture.OutputFileResults output) {
            //onImageSaved(output);
              Uri savedUri = output.getSavedUri();
              if (savedUri != null) {
                Intent resultIntent = new Intent();
                resultIntent.setData(savedUri); // Set the Uri as data
                setResult(RESULT_OK, resultIntent);
                finish();
              }
            String msg = "Photo capture succeeded: " + output.getSavedUri();
            Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
            Log.d(TAG, msg);
          }
        }
    );
  }

  private void startCamera() {
    ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

    cameraProviderFuture.addListener(() -> {
      try {
        ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

        Preview preview = new Preview.Builder().build();

        PreviewView viewFinder = findViewById(R.id.viewFinder);
        preview.setSurfaceProvider(viewFinder.getSurfaceProvider());

        imageCapture = new ImageCapture.Builder().build();

        CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

        cameraProvider.unbindAll();

        cameraProvider.bindToLifecycle(
            this, cameraSelector, preview, imageCapture);

      } catch (ExecutionException | InterruptedException e) {
        Log.e(TAG, "Use case binding failed", e);
      }
    }, ContextCompat.getMainExecutor(this));
  }

  private void requestPermissions() {
    activityResultLauncher.launch(REQUIRED_PERMISSIONS);
  }

  private boolean allPermissionsGranted() {
    for (String permission : REQUIRED_PERMISSIONS) {
      if (ContextCompat.checkSelfPermission(
          getBaseContext(), permission) != PackageManager.PERMISSION_GRANTED) {
        return false;
      }
    }
    return true;
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    cameraExecutor.shutdown();
  }

  private byte[] convertImageToByteArray(File imageFile) {
    try (FileInputStream fis = new FileInputStream(imageFile)) {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      byte[] buffer = new byte[1024];
      int bytesRead;
      while ((bytesRead = fis.read(buffer)) != -1) {
        bos.write(buffer, 0, bytesRead);
      }
      return bos.toByteArray();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public void onImageSaved(@NonNull ImageCapture.OutputFileResults output) {
    Uri savedUri = output.getSavedUri();
    if (savedUri != null) {
      try {
        File imageFile = new File(savedUri.getPath());
        Bitmap originalBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        if (originalBitmap != null) {
          Bitmap compressedBitmap = Bitmap.createScaledBitmap(originalBitmap, 640, 480, true);
          File compressedImageFile = new File(imageFile.getParent(), ".thumbnail_" + imageFile.getName());

          try (OutputStream outputStream = new FileOutputStream(compressedImageFile)) {
            compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream); // Adjust quality as needed
          }

          byte[] imageByteArray = convertImageToByteArray(compressedImageFile);
          // ... rest of your code to send imageByteArray to the database ...

          originalBitmap.recycle();
          compressedBitmap.recycle();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    // ... other code ...
  }

  private final ActivityResultLauncher<String[]> activityResultLauncher =
      registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
          result -> {
            boolean permissionGranted = true;
            for (String permission : REQUIRED_PERMISSIONS) {
              if (result.containsKey(permission) && !result.get(permission)) {
                permissionGranted = false;
                break;
              }
            }
            if (!permissionGranted) {
              Toast.makeText(getBaseContext(),
                  "Permission request denied",
                  Toast.LENGTH_SHORT).show();
            } else {
              startCamera();
            }
          });
}