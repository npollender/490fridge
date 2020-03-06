package com.example.usf;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.usf.PostgreSQLHelper.*;

public class Inventory extends AppCompatActivity {

    InventoryDBHelper IDB;
    TextView pAtxt, pBtxt, pCtxt, pDtxt, pEtxt, pFtxt;
    Button pAbtn, pBbtn, pCbtn, pDbtn, pEbtn, pFbtn, toExtras, refresh;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        getSupportActionBar().setTitle("Monitored Inventory");
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.background_gradient));
        TextView tv = new TextView(getApplicationContext());
        tv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/raleway.ttf"));
        tv.setText(getSupportActionBar().getTitle());
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(20);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(tv);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        IDB = new InventoryDBHelper(this);
        IDB.initData();

        pAbtn = (Button)findViewById(R.id.partitionAbtn);
        pBbtn = (Button)findViewById(R.id.partitionBbtn);
        pCbtn = (Button)findViewById(R.id.partitionCbtn);
        pDbtn = (Button)findViewById(R.id.partitionDbtn);
        pEbtn = (Button)findViewById(R.id.partitionEbtn);
        pFbtn = (Button)findViewById(R.id.partitionFbtn);
        toExtras = (Button)findViewById(R.id.to_extra_btn);
        refresh = (Button)findViewById(R.id.refresh_btn);
        pAtxt = (TextView)findViewById(R.id.pAtxt);
        pBtxt = (TextView)findViewById(R.id.pBtxt);
        pCtxt = (TextView)findViewById(R.id.pCtxt);
        pDtxt = (TextView)findViewById(R.id.pDtxt);
        pEtxt = (TextView)findViewById(R.id.pEtxt);
        pFtxt = (TextView)findViewById(R.id.pFtxt);
        pAbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImage();
            }
        });
        pBbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImage();
            }
        });
        pCbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImage();
            }
        });
        pDbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImage();
            }
        });
        pEbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImage();
            }
        });
        pFbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImage();
            }
        });

        // These two lines are really needed to solve the issue
        // --Something unusual has occurred to cause the driver to fail.--
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        viewData();

        toExtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Inventory.this, ExtraIngredients.class));
            }
        });

        pAtxt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                changeName(1);
                return true;
            }
        });
        pBtxt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                changeName(2);
                return true;
            }
        });
        pCtxt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                changeName(3);
                return true;
            }
        });
        pDtxt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                changeName(4);
                return true;
            }
        });
        pEtxt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                changeName(5);
                return true;
            }
        });
        pFtxt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                changeName(6);
                return true;
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeightFromServer();
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });
    }

    //show an image for each partition
    public void showImage() {
        Dialog builder = new Dialog(this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.pizza);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.setCanceledOnTouchOutside(true);
        builder.show();
    }

    //view the data of the table
    private void viewData() {
        Cursor cursor = IDB.viewData();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data to show", Toast.LENGTH_SHORT).show();
        }
        else {
            int count = 0;
            String view[] = new String[6];
            while (cursor.moveToNext()) {
                String iname = cursor.getString(1);
                String iamnt = cursor.getString(2);
                String toAdd = "Item: " + iname + "\nWeight: " + iamnt + " g";
                view[count] = toAdd;
                count++;
            }
            pAtxt.setText(view[0]);
            pBtxt.setText(view[1]);
            pCtxt.setText(view[2]);
            pDtxt.setText(view[3]);
            pEtxt.setText(view[4]);
            pFtxt.setText(view[5]);
        }
    }

    //change a name of a partition depending on the one the user selects
    public void changeName(final int id) {
        final Dialog dialog = new Dialog(Inventory.this);
        dialog.setContentView(R.layout.change_name_partition);
        TextView prompt = (TextView)dialog.findViewById(R.id.changenametxt);
        final EditText newname = (EditText)dialog.findViewById(R.id.newnameedit);
        Button yes = (Button)dialog.findViewById(R.id.yeschangebtn);
        Button no = (Button)dialog.findViewById(R.id.nochangebtn);

        prompt.setEnabled(true);
        newname.setEnabled(true);
        yes.setEnabled(true);
        no.setEnabled(true);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_name = newname.getText().toString();
                IDB.changeName(id, new_name);
                dialog.dismiss();
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getWeightFromServer() {
        try {
            establishDBConnection(getBaseContext().getAssets().open("db_config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(Inventory.this, "Failed to connect to server, weight unchanged...", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO - make it dynamic for all partitions
        String query = "SELECT quantity " +
                "FROM inventory_tb " +
                "WHERE partition = 'A'";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet res = pstmt.executeQuery()) {

            while (res.next()) {
                String value = res.getString(1);

                IDB.updateWeight(1, Double.valueOf(value));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        query = "SELECT quantity " +
                "FROM inventory_tb " +
                "WHERE partition = 'B'";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet res = pstmt.executeQuery()) {

            while (res.next()) {
                String value = res.getString(1);

                IDB.updateWeight(2, Double.valueOf(value));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        query = "SELECT quantity " +
                "FROM inventory_tb " +
                "WHERE partition = 'C'";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet res = pstmt.executeQuery()) {

            while (res.next()) {
                String value = res.getString(1);

                IDB.updateWeight(3, Double.valueOf(value));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        query = "SELECT quantity " +
                "FROM inventory_tb " +
                "WHERE partition = 'D'";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet res = pstmt.executeQuery()) {

            while (res.next()) {
                String value = res.getString(1);

                IDB.updateWeight(4, Double.valueOf(value));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        query = "SELECT quantity " +
                "FROM inventory_tb " +
                "WHERE partition = 'E'";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet res = pstmt.executeQuery()) {

            while (res.next()) {
                String value = res.getString(1);

                IDB.updateWeight(5, Double.valueOf(value));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        query = "SELECT quantity " +
                "FROM inventory_tb " +
                "WHERE partition = 'F'";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet res = pstmt.executeQuery()) {

            while (res.next()) {
                String value = res.getString(1);

                IDB.updateWeight(6, Double.valueOf(value));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}


