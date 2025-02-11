package com.example.myapplication;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.firestore.DocumentSnapshot;
import com.ortiz.touchview.TouchImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;

public class PhotosPagerAdapter extends PagerAdapter {
    private Context context;
    private List<DocumentSnapshot> photosList;

    public PhotosPagerAdapter(Context context, List<DocumentSnapshot> photosList) {
        this.context = context;
        this.photosList = photosList;
    }

    @Override
    public int getCount() {
        return photosList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photo_with_text, container, false);

//        TextView textView = view.findViewById(R.id.photoText);
        TouchImageView touchImageView = view.findViewById(R.id.photoImageView);
        Button downloadButton = view.findViewById(R.id.downloadButton);
        Button shareButton = view.findViewById(R.id.shareButton);

        DocumentSnapshot document = photosList.get(position);
        String imageUrl = document.getString("url");
        String imageName = document.getString("name"); // Adjust based on your document fields

//        textView.setText(imageName);
        Glide.with(context).load(imageUrl).into(touchImageView);

        downloadButton.setOnClickListener(v -> downloadImage(imageUrl));
        shareButton.setOnClickListener(v -> downloadAndShareImage(imageUrl));

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    private void downloadImage(String imageUrl) {
        Glide.with(context)
                .asBitmap()
                .load(imageUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        saveImageToGallery(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Do nothing
                    }
                });
    }

    private void saveImageToGallery(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveImageToGalleryQ(bitmap);
        } else {
            saveImageToGalleryLegacy(bitmap);
        }
    }

    private void saveImageToGalleryQ(Bitmap bitmap) {
        ContentResolver resolver = context.getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "image_" + System.currentTimeMillis() + ".jpg");
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

        Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        try (OutputStream out = resolver.openOutputStream(Objects.requireNonNull(imageUri))) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            Toast.makeText(context, "Image saved to Gallery", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImageToGalleryLegacy(Bitmap bitmap) {
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file = new File(directory, "image_" + System.currentTimeMillis() + ".jpg");

        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            Toast.makeText(context, "Image saved to Gallery", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadAndShareImage(String imageUrl) {
        Glide.with(context)
                .asBitmap()
                .load(imageUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        try {
                            File imageFile = saveImageToFile(resource);
                            shareImage(imageFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Failed to download image", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Do nothing
                    }
                });
    }

    private File saveImageToFile(Bitmap bitmap) throws IOException {
        File storageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "SharedImages");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        File imageFile = new File(storageDir, "shared_image_" + System.currentTimeMillis() + ".jpg");
        try (FileOutputStream out = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        }

        return imageFile;
    }
    private void shareImage(File imageFile) {
        Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", imageFile);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/jpeg");

        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);

        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(shareIntent, "Share Image"));
    }
}
