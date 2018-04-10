package example.android.com.admin;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddChefFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddChefFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public AddChefFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddChefFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddChefFragment newInstance(String param1, String param2) {
        AddChefFragment fragment = new AddChefFragment();
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
        View v=inflater.inflate(R.layout.fragment_add_chef, container, false);

         final TextView ed=v.findViewById(R.id.token);




         DatabaseReference dbref= FirebaseDatabase.getInstance().getReference("token");
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String tt=dataSnapshot.getValue()+" ";
                ed.setText(tt);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Button set=v.findViewById(R.id.set);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed.getText()==null || ed.getText().toString().trim().compareTo("")==0)
                    Toast.makeText(getActivity(), "please give some valid token", Toast.LENGTH_SHORT).show();
                else
                FirebaseDatabase.getInstance().getReference("token").setValue(Integer.parseInt(ed.getText().toString()));
            }
        });


        return v;
    }

}
