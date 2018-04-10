package example.android.com.admin;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import example.android.com.admin.Entities.Item;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RemoveItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RemoveItemFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public RemoveItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RemoveItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RemoveItemFragment newInstance(String param1, String param2) {
        RemoveItemFragment fragment = new RemoveItemFragment();
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
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_remove_item, container, false);

        final EditText edt=v.findViewById(R.id.item_name);

        Button ba=v.findViewById(R.id.item_delete);
        ba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String txt=edt.getText().toString();
                if(txt!=null) {

                    Query q = FirebaseDatabase.getInstance().getReference("dishes").orderByChild("name").equalTo(txt);
                    q.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Item item = dataSnapshot.getValue(Item.class);


                                FirebaseDatabase.getInstance().getReference("dishes/" + dataSnapshot.getKey()).removeValue();
                                edt.setText("");
                                Toast.makeText(getActivity(), "removed successfully", Toast.LENGTH_SHORT).show();

                        }

                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
                }
        });

        return v;
    }

}
