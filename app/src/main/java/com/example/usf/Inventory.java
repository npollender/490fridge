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
import android.widget.EditText;
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
        viewData();

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

        dialog.show();
    }
}


