package com.example.capstone42_sancheck.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.capstone42_sancheck.R;
import com.example.capstone42_sancheck.object.CartMountain;
import com.example.capstone42_sancheck.object.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartListViewAdapter extends BaseAdapter {
    private ArrayList<CartMountain> arrayList = new ArrayList<>();
    private ImageView iv_picture;
    private TextView tv_name;
    private TextView tv_level;
    private TextView tv_pname;
    private ImageView iv_deleteheart;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.activity_cart_listview_item, viewGroup, false);
        }

        iv_picture = (ImageView) view.findViewById(R.id.iv_picture);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_pname = (TextView) view.findViewById(R.id.tv_pname);
        tv_level = (TextView) view.findViewById(R.id.tv_level);
        iv_deleteheart = (ImageView) view.findViewById(R.id.deleteheart);

        CartMountain item = arrayList.get(i);

        tv_name.setText(item.getMNTN_NM());
        tv_pname.setText(item.getPMNTN_NM());
        tv_level.setText(item.getPMNTN_DFFL());

        database = FirebaseDatabase.getInstance("https://capstone42-sancheck-96817-default-rtdb.firebaseio.com/");
        databaseReference = database.getReference("Users");

        iv_deleteheart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth = FirebaseAuth.getInstance();
                String uid = auth.getCurrentUser().getUid();
                databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue(User.class) != null){
                            User user = snapshot.getValue(User.class);
                            user.setTrailPlanDelete(i, user.trailPlan);
                            Toast.makeText(view.getContext(), "찜 목록에서 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                            databaseReference.child(uid).setValue(user);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        return view;
    }

    public void addItem(String MN_Name, String PMNTN_Name, String PMNTN_DFFL, int drawableId, int index, double PMNTN_LT, String START_PNT, String END_PNT) {
        CartMountain item = new CartMountain();
        item.setMNTN_NM(MN_Name);
        item.setPMNTN_NM(PMNTN_Name);
        item.setPMNTN_DFFL(PMNTN_DFFL);
        item.setDrawableId(drawableId);
        item.setIndex(index);
        item.setPMNTN_LT(PMNTN_LT);
        item.setSTART_PNT(START_PNT);
        item.setEND_PNT(END_PNT);

        arrayList.add(item);
    }

    public void clear() {
        arrayList.clear();
    }
}
