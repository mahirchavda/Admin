package example.android.com.admin;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import example.android.com.admin.Entities.Item;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddItemFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Uri filePath;
    private StorageReference storageReference;
    private DatabaseReference mDatabase;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String url="";
    private TextView imagetext;
    //private Item item=new Item();
    //private EditText name,price,rating,catagory,time;
    public AddItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddItemFragment newInstance(String param1, String param2) {
        AddItemFragment fragment = new AddItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_add_item, container, false);
        Button b1=v.findViewById(R.id.choose);
        storageReference = FirebaseStorage.getInstance().getReference();

        final EditText name=v.findViewById(R.id.item_name);
        Toast.makeText(getActivity(), name.getText(), Toast.LENGTH_SHORT).show();
        //item.setName(name.getText().toString());
        final EditText catagory=v.findViewById(R.id.item_catagory);
        //item.setCatagory(catagory.getText().toString());
        final EditText price=v.findViewById(R.id.item_price);
        //item.setPrice(price.getText().toString());
        //final EditText rating=v.findViewById(R.id.item_rating);
        //item.setRating(rating.getText().toString());
        final EditText time=v.findViewById(R.id.item_avg_time);
        //item.setAverage_making_time(time.getText().toString());
         imagetext=v.findViewById(R.id.item_image);

        mDatabase= FirebaseDatabase.getInstance().getReference("dishes");
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 234);

            }
        });


        Button b2=v.findViewById(R.id.upload);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filePath != null) {
                    //displaying progress dialog while image is uploading
                    final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setTitle("Uploading");
                    progressDialog.show();

                    //getting the storage reference
                    StorageReference sRef = storageReference.child("dishes/" + System.currentTimeMillis() + "." + getFileExtension(filePath));

                    //adding the file to reference
                    sRef.putFile(filePath)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    //dismissing the progress dialog

                                    if (name.getText()!=null && catagory.getText()!=null && price.getText()!=null && time.getText()!=null && imagetext.getText().toString().compareTo("choose image")!=0) {

                                        progressDialog.dismiss();
                                        //Toast.makeText(getActivity(), name.getText(), Toast.LENGTH_SHORT).show();
                                        Item item = new Item();
                                        //EditText name=v.findViewById(R.id.item_name);
                                        item.setName(name.getText().toString());
                                        //EditText catagory=v.findViewById(R.id.item_name);
                                        item.setCatagory(catagory.getText().toString());
                                        //EditText price=v.findViewById(R.id.item_price);
                                        item.setPrice(price.getText().toString());
                                        //EditText rating=v.findViewById(R.id.item_rating);
                                        //item.setRating(rating.getText().toString());
                                        //EditText time=v.findViewById(R.id.item_avg_time);
                                        item.setAverage_making_time(time.getText().toString());


                                        //displaying success toast
                                        //Toast.makeText(WelcomeActivity.getContextOfApplication(), "File Uploaded ", Toast.LENGTH_LONG).show();
                                        url = taskSnapshot.getDownloadUrl().toString();
                                        item.setImage(url);
                                        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("dishes");
                                        dbref.push().setValue(item);
                                        name.setText("");
                                        price.setText("");
                                        // rating.setText("");
                                        time.setText("");
                                        catagory.setText("");
                                        imagetext.setText("choose image");
                                        Toast.makeText(WelcomeActivity.getContextOfApplication(), "uploaded successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    progressDialog.dismiss();
                                    Toast.makeText(WelcomeActivity.getContextOfApplication(), exception.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    //displaying the upload progress
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                    progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                                }
                            });
                } else {
                    //display an error if no file is selected
                }



            }
        });



        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==234 && data!=null && data.getData()!=null)
        {
            filePath=data.getData();
            File f = new File(filePath.getPath());
            imagetext.setText(f.getName());
        }

    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = WelcomeActivity.getContextOfApplication().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private String uploadFile() {
        //checking if file is available

        return url;

    }


}



