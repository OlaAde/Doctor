package com.works.adeogo.doctor;


import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.works.adeogo.doctor.adapters.SearchAdapter;
import com.works.adeogo.doctor.model.DoctorProfile;


/**
 * A simple {@link Fragment} subclass.
 */
public class CollaborateFragment extends Fragment implements SearchAdapter.SearchAdapterOnclickHandler {

    private final String TAG = "Collaborate Fragment";

    private LinearLayoutManager mLayoutManager;
    private SearchAdapter mSearchAdapter;

    private RecyclerView mRecyclerView;
    private SearchView mSearchView;

    private List<String> mDoctorNameList = new ArrayList<>();
    private List<DoctorProfile> mDoctorProfileList = new ArrayList<>();
    private List<DoctorProfile> mDoctorSearchProfileList = new ArrayList<>();
    private Context mContext;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;

    private String mUserId;

    public CollaborateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_collaborate, container, false);
        mContext = getActivity();
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        mSearchView = (SearchView) rootView.findViewById(R.id.searchview);
        mSearchAdapter = new SearchAdapter(mContext, this);
        mLayoutManager = new LinearLayoutManager(mContext);

        mRecyclerView.setAdapter(mSearchAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("new_doctors").child("all_profiles");
        setFakeData();

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        mSearchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        mSearchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mSearchAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mSearchAdapter.getFilter().filter(query);
                return false;
            }

        });
        mSearchAdapter.swapData(mDoctorProfileList);
        mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        return rootView;
    }

//    private void updateStringArray(DoctorProfile doctorProfile){
//        mDoctorNameList.add(doctorProfile.getName());
//    }
//
//    private List<String> sortRemainderArray(List<DoctorProfile> doctorProfiles, List<String> doctorNames){
//        List<String> list = new ArrayList<>();
//
//        for (int i = 0; i<doctorProfiles.size(); i++ ){
//
//            list.add(doctorProfiles.get(i).getName());
//        }
//        return list;
//    }
//
//
//    private void iterator(CharSequence constraint){
//        final int count = 100;
//
//
//        if(!TextUtils.isEmpty(constraint)){
//            String constraintString = constraint.toString().toLowerCase(Locale.ROOT);
//
//            Iterator<String> iter = mDoctorNameList.iterator();
//            while(iter.hasNext()){
//                if(!iter.next().toLowerCase(Locale.ROOT).contains(constraintString))
//                {
//                    iter.remove();
//                }
//            }
//        }
//
//
//        checkList();
//        sortList(mDoctorNameList);
//        mSearchAdapter.swapData(mDoctorSearchProfileList);
//
//
//    }
//
//    private void sortList(List<String> list){
//        Collections.sort(list, new Comparator<String>() {
//            @Override
//            public int compare(String s1, String s2) {
//                return s1.compareToIgnoreCase(s2);
//            }
//        });
//    }
//
//    private void checkList(){
//        if (mDoctorNameList.size()!= 0){
//            Toast.makeText(mContext, "Tested" , Toast.LENGTH_SHORT).show();
//            for (int i = 0; i< mDoctorNameList.size(); i++){
//
////                for (int j = 0; j < mDoctorProfileList.size(); i++){
////                    if (TextUtils.equals(mDoctorNameList.get(i), mDoctorProfileList.get(j).getName())){
////                        Toast.makeText(mContext, i +"  " + j , Toast.LENGTH_SHORT).show();
////                        Log.e(TAG,i +"  " + j );
////                        mDoctorSearchProfileList.add(mDoctorProfileList.get(j));
////                    }
////                }
//            }
//        }
//
//    }

    @Override
    public void voidMethod(List<DoctorProfile> list, int adapterPosition) {

    }

    private void setFakeData() {
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DoctorProfile doctorProfile = dataSnapshot.getValue(DoctorProfile.class);
                if (!TextUtils.equals(doctorProfile.getDoctorId(), mUserId)){
                    mDoctorProfileList.add(doctorProfile);
                    mSearchAdapter.swapData(null);
                    mSearchAdapter.swapData(mDoctorProfileList);
                }
            }

            @Override
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
        };
        mDatabaseReference.addChildEventListener(mChildEventListener);
        mSearchAdapter.swapData(mDoctorProfileList);
    }
}
