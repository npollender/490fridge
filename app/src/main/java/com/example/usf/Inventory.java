package com.example.usf;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Inventory extends AppCompatActivity {

    InventoryDBHelper IDB;
    TextView pAtxt, pBtxt, pCtxt, pDtxt, pEtxt, pFtxt;
    Button pAbtn, pBbtn, pCbtn, pDbtn, pEbtn, pFbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        IDB = new InventoryDBHelper(this);
        IDB.initData();

        pAbtn = (Button)findViewById(R.id.partitionAbtn);
        pBbtn = (Button)findViewById(R.id.partitionBbtn);
        pCbtn = (Button)findViewById(R.id.partitionCbtn);
        pDbtn = (Button)findViewById(R.id.partitionDbtn);
        pEbtn = (Button)findViewById(R.id.partitionEbtn);
        pFbtn = (Button)findViewById(R.id.partitionFbtn);
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
        viewData();
    }

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
        builder.show();
    }

    private void viewData() {
        Cursor cursor = IDB.viewData();
        pAtxt = (TextView)findViewById(R.id.pAtxt);
        pBtxt = (TextView)findViewById(R.id.pBtxt);
        pCtxt = (TextView)findViewById(R.id.pCtxt);
        pDtxt = (TextView)findViewById(R.id.pDtxt);
        pEtxt = (TextView)findViewById(R.id.pEtxt);
        pFtxt = (TextView)findViewById(R.id.pFtxt);

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data to show", Toast.LENGTH_SHORT).show();
        }
        else {
            int count = 0;
            String view[] = new String[6];
            while (cursor.moveToNext()) {
                String iname = cursor.getString(1);
                String iamnt = cursor.getString(2);
                String toAdd = "Item: " + iname + "\nWeight: " + iamnt;
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
}


