package thiennh23.potholehunter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import thiennh23.potholehunter.ml.PotholeDetectionModel;

public class PotholeImagesDetectionActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PICK_IMAGE = 2;
    private ImageView capturedImage;
    private TextView resultText;
    private PotholeDetectionModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pothole_images_detection);

        capturedImage = findViewById(R.id.captured_image);
        resultText = findViewById(R.id.result_text);
        Button captureImageButton = findViewById(R.id.capture_image_button);
        Button selectImageButton = findViewById(R.id.select_image_button);

        try {
            model = PotholeDetectionModel.newInstance(this);
        } catch (IOException e) {
            Log.e("PotholeDetection", "Error loading model: " + e.getMessage());
            e.printStackTrace();
            resultText.setText("Error: Model loading failed");
        }

        captureImageButton.setOnClickListener(v -> captureImage());
        selectImageButton.setOnClickListener(v -> selectImageFromGallery());
    }

    private void captureImage() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void selectImageFromGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Bitmap imageBitmap = null;
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                imageBitmap = (Bitmap) data.getExtras().get("data");
            } else if (requestCode == PICK_IMAGE) {
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (imageBitmap != null) {
                capturedImage.setImageBitmap(imageBitmap);
                processImage(imageBitmap);
            }
        }
    }

    private void processImage(Bitmap bitmap) {
        // Convert Bitmap to TensorBuffer
        TensorBuffer inputFeature0 = getInputTensorFromBitmap(bitmap);

        // Run inference
        PotholeDetectionModel.Outputs outputs = model.process(inputFeature0);
        TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

        // Extract result
        float[] result = outputFeature0.getFloatArray();
        String resultTextStr = result[0] < 0.5 ? "Potholes" : "Normal";
        resultText.setText(resultTextStr);

    }

    private TensorBuffer getInputTensorFromBitmap(Bitmap bitmap) {
        Bitmap resizedBitmap = resizeBitmap(bitmap);
        int[] intValues = new int[224 * 224];
        resizedBitmap.getPixels(intValues, 0, resizedBitmap.getWidth(), 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight());

        // Convert the int values into a float array (normalize to [0, 1])
        float[] floatValues = new float[224 * 224 * 3];
        for (int i = 0; i < intValues.length; i++) {
            int pixelValue = intValues[i];
            floatValues[i * 3] = ((pixelValue >> 16) & 0xFF) / 255.0f;  // Red
            floatValues[i * 3 + 1] = ((pixelValue >> 8) & 0xFF) / 255.0f;   // Green
            floatValues[i * 3 + 2] = (pixelValue & 0xFF) / 255.0f;          // Blue
        }

        TensorBuffer inputBuffer = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
        inputBuffer.loadArray(floatValues);
        return inputBuffer;
    }

    private Bitmap resizeBitmap(Bitmap original) {
        int targetWidth = 224;
        int targetHeight = 224;
        return Bitmap.createScaledBitmap(original, targetWidth, targetHeight, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (model != null) {
            model.close();
        }
    }
}
