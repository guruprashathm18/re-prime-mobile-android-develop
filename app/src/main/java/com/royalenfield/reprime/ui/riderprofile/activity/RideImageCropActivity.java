package com.royalenfield.reprime.ui.riderprofile.activity;

import androidx.exifinterface.media.ExifInterface;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.naver.android.helloyako.imagecrop.view.ImageCropView;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.io.FileOutputStream;
import java.io.IOException;

import com.royalenfield.reprime.utils.RELog;

public class RideImageCropActivity extends REBaseActivity {

    String filePath, filePathCropped, filename;
    ImageCropView cropImage;
    TextView mTvCrop, mTvCancel;
    int orientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_image_crop);
        initViews();
        Intent intent;
        if (getIntent() != null) {
            intent = getIntent();
            intent.getParcelableExtra("imageUri");
            String filepath = intent.getStringExtra("filepath");
            Bitmap thumb = (Bitmap) intent.getExtras().get("thumbnail");
            String imageMode = intent.getStringExtra("imagemode");
            Uri myUri = intent.getParcelableExtra("imageuri");
            if (cropImage != null) {
                if (imageMode.equalsIgnoreCase("camera")) {
                    orientation = getExifOrientation(filepath);
                    Bitmap bitmap = thumb;
                    //rotate bitmap
                    Matrix matrix = new Matrix();
                    matrix.postRotate(orientation);
                    //create new rotated bitmap
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    cropImage.setImageBitmap(bitmap);
                } else if (imageMode.equalsIgnoreCase("gallery")) {
                    orientation = getExifOrientation(filepath);
                    Bitmap bitmap = BitmapFactory.decodeFile(filepath);
                    //rotate bitmap
                    Matrix matrix = new Matrix();
                    matrix.postRotate(orientation);
                    //create new rotated bitmap
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    cropImage.setImageBitmap(bitmap);
                }
                cropImage.setGridInnerMode(ImageCropView.GRID_ON);
                cropImage.setGridOuterMode(ImageCropView.GRID_ON);
                cropImage.setAspectRatio(1, 1);
                cropImage.setScaleEnabled(false);
                mTvCrop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (cropImage != null && cropImage.getCroppedImage() != null) {
                                cropImage.setImageBitmap(cropImage.getCroppedImage());
                                cropImage.setGridInnerMode(ImageCropView.GRID_OFF);
                                cropImage.setGridOuterMode(ImageCropView.GRID_OFF);
                                mTvCrop.setVisibility(View.GONE);
                                Bitmap cropBitmap;
                                int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                                cropImage.measure(spec, spec);
                                cropImage.layout(0, 0, cropImage.getMeasuredWidth(), cropImage.getMeasuredHeight());
                                cropBitmap = Bitmap.createBitmap(cropImage.getMeasuredWidth(), cropImage.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
                                if (cropImage.getCroppedImage().getHeight() > 0 && cropImage.getCroppedImage().getWidth() > 0) {
                                    cropBitmap = cropImage.getCroppedImage();
                                    Uri uri = REUtils.getImageUri(getApplicationContext(), cropImage.getCroppedImage());
                                    filePathCropped = REUtils.getRealPathFromURI(uri, RideImageCropActivity.this);

                                    //Write file
                                    filename = "bitmap.png";
                                    FileOutputStream stream = RideImageCropActivity.this.openFileOutput(filename, Context.MODE_PRIVATE);
                                    cropBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                                    //Cleanup
                                    stream.close();
                                    cropBitmap.recycle();

                                    //Pop intent
                                    Intent cropIntent = new Intent();
                                    cropIntent.putExtra("filepathcropped", filePathCropped);
                                    cropIntent.putExtra("BitmapImage", filename);
                                    REConstants.IMAGE_FILE_NAME_CONSTANT = filePathCropped;
                                    setResult(RESULT_OK, cropIntent);
                                    finish();
                                    overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);

                                }
                            }
                        } catch (Exception e) {
                            RELog.e(e);
                            //Toast.makeText(getApplicationContext(), "Zoom not supported", Toast.LENGTH_LONG).show();
                            finish();
                            overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
                        }
                    }
                });
                mTvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent backIntent = new Intent();
                        if (null != REConstants.IMAGE_FILE_NAME_CONSTANT) {
                            backIntent.putExtra("filepathcropped", REConstants.IMAGE_FILE_NAME_CONSTANT);
                        } else backIntent.putExtra("filepathcropped", "");
                        setResult(RESULT_CANCELED, backIntent);
                        finish();
                        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
                        //onBackPressed();
                    }
                });
            }
        }
    }

    private void initViews() {
        cropImage = findViewById(R.id.share_ride_image);
        mTvCrop = findViewById(R.id.tv_crop);
        mTvCancel = findViewById(R.id.tv_cancel);
    }

    @Override
    public void onBackPressed() {
        Intent backIntent = new Intent();
        if (null != REConstants.IMAGE_FILE_NAME_CONSTANT) {
            backIntent.putExtra("filepathcropped", REConstants.IMAGE_FILE_NAME_CONSTANT);
        } else backIntent.putExtra("filepathcropped", "");
        setResult(RESULT_CANCELED, backIntent);
        finish();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
    }

    public static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
           RELog.e(ex);
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                // We only recognise a subset of orientation tag values.
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }

            }
        }

        return degree;
    }
}
