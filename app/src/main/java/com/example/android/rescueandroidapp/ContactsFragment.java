package com.example.android.rescueandroidapp;


import android.Manifest;
import static android.Manifest.permission.CALL_PHONE;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {

    Button button1;

    private View parentView;


    public ContactsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        parentView = inflater.inflate(R.layout.fragment_contacts, container, false);
        setUpViews();


        return parentView;

    }



    private void setUpViews() {
        parentView.findViewById(R.id.btnsetEmergency).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("Do you want to call?");
                alertDialog.setMessage("+911");
                alertDialog.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // Write your code here to execute after dialog
                                dialog.cancel();
                            }
                        });

                alertDialog.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // Write your code here to execute after dialog
                                Intent i = new Intent(Intent.ACTION_CALL);
                                i.setData(Uri.parse("tel:911"));

                                if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                    startActivity(i);
                                    //Toast.makeText(getActivity().getApplicationContext(), "Making a call", Toast.LENGTH_LONG).show();
                                } else {
                                    requestPermissions(new String[]{CALL_PHONE}, 1);
                                }


                            }
                        });
                alertDialog.show();
            }

        });


    }
}
