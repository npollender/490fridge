package com.example.usf;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class InteriorView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interior_view);
        getSupportActionBar().setTitle("Interior View");
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.background_gradient));
        TextView tv = new TextView(getApplicationContext());
        tv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/raleway.ttf"));
        tv.setText(getSupportActionBar().getTitle());
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(20);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(tv);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




//        AWSCredentials credentials = null;
//        try {
//            Properties prop = PostgreSQLHelper.getProperties(
//                    getBaseContext().getAssets().open("s3_config.properties"));
//            credentials = new BasicAWSCredentials(
//                    prop.getProperty("accessKey"), prop.getProperty("accessKey"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        AmazonS3Client s3Client = new AmazonS3Client(credentials);
//
////        AmazonS3 s3Client = AmazonS3ClientBuilder
////                .standard()
////                .withCredentials(new AWSStaticCredentialsProvider(credentials))
////                .withRegion(Regions.CA_CENTRAL_1)
////                .build();
//
//
//        displayImage(imageView, s3Client, "bananas.jpg", "smart-fridge-pics");


        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        Glide.with(getBaseContext())
                .load("https://smart-fridge-pics.s3.ca-central-1.amazonaws.com/image.jpg")
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView);
    }

    private void displayImage(ImageView view, AmazonS3Client s3, String imgName, String bktName ) {
        if ( this.isNewImageAvailable( s3, imgName, bktName ) ) {
            this.getRemoteImage( s3, imgName, bktName );
        }

        InputStream stream = this.getLocalImage( imgName );
        view.setImageDrawable( Drawable.createFromStream( stream, "src" ) );
    }

    private boolean isNewImageAvailable( AmazonS3Client s3, String imageName, String bucketName ) {
        File file = new File( this.getApplicationContext().getFilesDir(), imageName );
        if ( !file.exists() ) {
            return true;
        }

        ObjectMetadata metadata = s3.getObjectMetadata( bucketName,
                imageName );
        long remoteLastModified = metadata.getLastModified().getTime();

        if ( file.lastModified() < remoteLastModified ) {
            return true;
        }
        else {
            return false;
        }
    }

    private void getRemoteImage( AmazonS3Client s3, String imageName, String bucketName ) {
        S3Object object = s3.getObject( bucketName, imageName );
        this.storeImageLocally( object.getObjectContent(), imageName );
    }

    private void storeImageLocally( InputStream stream, String imageName ) {
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput( imageName,
                    Context.MODE_PRIVATE);

            int length = 0;
            byte[] buffer = new byte[1024];
            while ( ( length = stream.read( buffer ) ) > 0 ) {
                outputStream.write( buffer, 0, length );
            }

            outputStream.close();
        }
        catch ( Exception e ) {
            Log.d( "Store Image", "Can't store image : " + e );
        }
    }

    private InputStream getLocalImage(String imageName ) {
        try {
            return openFileInput( imageName );
        }
        catch ( FileNotFoundException exception ) {
            return null;
        }
    }
}
