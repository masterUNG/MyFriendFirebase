package masterung.androidthai.in.th.myfriendfirebase.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import masterung.androidthai.in.th.myfriendfirebase.R;
import masterung.androidthai.in.th.myfriendfirebase.utility.ReadAllUserAdapter;
import masterung.androidthai.in.th.myfriendfirebase.utility.UserModel;

public class ShowAllUserFragment extends Fragment {

    private List<String> displayNameStringList,
            emailStringList, iconStringList;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Create RecircleView
        createRecircleView();

    }

    private void createRecircleView() {

//        Read Value From Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference()
                .child("User");

        final int[] timesInt = {0};

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int amountUser = (int) dataSnapshot.getChildrenCount();

                displayNameStringList = new ArrayList<>();
                emailStringList = new ArrayList<>();
                iconStringList = new ArrayList<>();

                List list = new ArrayList();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    UserModel userModel = dataSnapshot1.getValue(UserModel.class);
                    list.add(userModel);

                    UserModel userModel1 = (UserModel) list.get(timesInt[0]);
                    displayNameStringList.add(userModel1.getDisplayNameString());
                    emailStringList.add(userModel1.getEmailString());
                    iconStringList.add(userModel1.getPhotoUrlString());

                    timesInt[0] += 1;
                }   // for

                RecyclerView recyclerView = getView().findViewById(R.id.recyclerViewReadAllUser);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                        LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);

                ReadAllUserAdapter readAllUserAdapter = new ReadAllUserAdapter(getActivity(),
                        displayNameStringList, emailStringList, iconStringList);
                recyclerView.setAdapter(readAllUserAdapter);

//                Add Event From Click




            }   // onDataChange

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_all_user, container, false);
        return view;
    }
}
