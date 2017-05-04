package me.citrafa.asistenkuliahku.ActivityClass.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import me.citrafa.asistenkuliahku.ActivityClass.Dashboard;
import me.citrafa.asistenkuliahku.ActivityClass.frmDaftar;
import me.citrafa.asistenkuliahku.ActivityClass.frmJadwalKuliah;
import me.citrafa.asistenkuliahku.AdapterRecycleView.AdapterJadwalKuliahNew;
import me.citrafa.asistenkuliahku.AdapterRecycleView.AdapterJadwalKuliahRV;
import me.citrafa.asistenkuliahku.Jadwal;
import me.citrafa.asistenkuliahku.ModelClass.JadwalKuliahModel;
import me.citrafa.asistenkuliahku.OperationRealm.RealmController;
import me.citrafa.asistenkuliahku.R;

/**
 * Created by SENSODYNE on 13/04/2017.
 */

public class fragment_jadwalkuliah extends Fragment{
    private static RecyclerView recyclerView;
    private Realm realm;
    private AdapterJadwalKuliahNew adapter;
    private Context mContex;
    private FloatingActionButton fab;
    private Paint p = new Paint();
    private OrderedRealmCollection<JadwalKuliahModel> data;
    private ArrayList<Jadwal> dataJadwal;
    String TAG = "";



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    protected RecyclerView.LayoutManager mLayoutManager;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private fragment_jadwalkuliah.OnFragmentInteractionListener mListener;

    public fragment_jadwalkuliah() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_jadwalkuliah.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_jadwalkuliah newInstance(String param1, String param2) {
        fragment_jadwalkuliah fragment = new fragment_jadwalkuliah();
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
        this.realm = RealmController.with(getActivity()).getRealm();

        realm.getDefaultInstance();
        View rootView = inflater.inflate(R.layout.fragment_jadwal_kuliah, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerjk);
        data = realm.where(JadwalKuliahModel.class).findAll();
        RealmResults<JadwalKuliahModel> jkm = realm.where(JadwalKuliahModel.class).findAll().sort("nohari", Sort.ASCENDING);
        adapter = new AdapterJadwalKuliahNew(data,jkm);
        final LinearLayoutManager layout = new LinearLayoutManager(getActivity());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layout);
        recyclerView.setAdapter(adapter);
        fab = (FloatingActionButton)rootView.findViewById(R.id.fabAddJadwalKuliah);
        fab.show();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        frmJadwalKuliah.class);
                startActivity(intent);
            }
        });
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                @Override
//                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                    if (dy > 0 || dy<0 && fab.isShown()){
//                        fab.hide();
//                    }
//                }
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                if (newState == RecyclerView.SCROLL_STATE_IDLE){
//                    fab.show();
//                }
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//        });


//        if (adapter == null){
//            for (int i = 0; i<data.size(); i++){
//                inputData(data.get(i));
//            }
//            adapter = new AdapterJadwalKuliahRV(dataJadwal,mContex);
//            recyclerView.setAdapter(adapter);
//        }else {
//            recyclerView.setAdapter(adapter);
//        }


                //recyclerView.setAdapter(adapter);
                recyclerView.setHasFixedSize(true);
        Log.d(TAG, "TAG : OnCreateView Fragment");
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContex = context;
//        dataJadwal = new ArrayList<>();
        Log.d(TAG, "tag : OnAttach Fragment");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

//    private int inputData(JadwalKuliahModel jadwalKuliahModel){
//        for (int i = 0; i<this.dataJadwal.size(); i++){
//            if(this.dataJadwal.size()!=0) {
//                if (this.dataJadwal.get(i).getTittle().equals(jadwalKuliahModel.getHari_jk())) {
//                    //realm.where(JadwalKuliahModel.class).findAllSorted("nohari", Sort.ASCENDING);
//                    Jadwal jd = dataJadwal.get(i);
//                    removeData(jadwalKuliahModel.getHari_jk());
//                    jd.addData(jadwalKuliahModel);
//                    this.dataJadwal.add(jd);
//                    return 1;
//                }
//            }
//        }
//        Jadwal jk = new Jadwal();
//        jk.setTittle(jadwalKuliahModel.getHari_jk());
//        jk.addData(jadwalKuliahModel);
//        this.dataJadwal.add(jk);
//        Log.d(TAG,"log input data");
//        return 1;
//    }
//
//    private void removeData(String hari){
//        for(int i = 0;i<this.dataJadwal.size();i++){
//            if(this.dataJadwal.get(i).getTittle().equals(hari)){
//                this.dataJadwal.remove(i);
//            }
//        }
//    }


//    private void initSwipe(){
//        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                int position = viewHolder.getAdapterPosition();
//
//                if (direction == ItemTouchHelper.LEFT){
//                    //swipe ke kiri
//                } else {
//                    //
//                }
//            }
//
//            @Override
//            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//
//                Bitmap icon;
//                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
//
//                    View itemView = viewHolder.itemView;
//                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
//                    float width = height / 3;
//
//                    if(dX > 0){
//                        p.setColor(Color.parseColor("#388E3C"));
//                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
//                        c.drawRect(background,p);
//                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.rename_box);
//                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
//                        c.drawBitmap(icon,null,icon_dest,p);
//                    } else {
//                        p.setColor(Color.parseColor("#D32F2F"));
//                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
//                        c.drawRect(background,p);
//                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.delete);
//                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
//                        c.drawBitmap(icon,null,icon_dest,p);
//                    }
//                }
//                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//            }
//        };
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
//        itemTouchHelper.attachToRecyclerView(recyclerView);
//    }


}
