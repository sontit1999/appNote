package com.example.notesmasteer.fragment.bottomsheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesmasteer.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class BackgroundBottomSheetFragment extends BottomSheetDialogFragment {
    BgBottomSheetCallBack listner;
    RecyclerView rvImage;
    public BackgroundBottomSheetFragment() {
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottomsheet_background,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init view
        rvImage = (RecyclerView) view.findViewById(R.id.rvBackground);
        initRecyclerView();
    }



    private void initRecyclerView() {
        rvImage.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(getContext(),3,RecyclerView.VERTICAL,false);
        rvImage.setLayoutManager(manager);

        BgAdapter bgAdapter = new BgAdapter();
        bgAdapter.setCallBack(new BgCallBack() {
            @Override
            public void onBgClick(int ImageCode) {
                dismiss();
                if(listner!=null){
                    listner.onBgClick(ImageCode);
                }
            }
        });
        rvImage.setAdapter(bgAdapter);
    }
    public void setListner(BgBottomSheetCallBack listner) {
        this.listner = listner;
    }
    public class BgAdapter extends RecyclerView.Adapter<BgAdapter.BgHodler>{
        List<Integer> listBg = new ArrayList<>();
        BgCallBack callBack;

        public void setCallBack(BgCallBack callBack) {
            this.callBack = callBack;
        }

        public BgAdapter() {
            listBg = getListBackGround();
        }

        @NonNull
        @Override
        public BgHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_bg,parent,false);
            return new BgHodler(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BgHodler holder, final int position) {
             holder.binData(listBg.get(position));
             holder.itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     callBack.onBgClick(listBg.get(position));
                 }
             });
        }

        @Override
        public int getItemCount() {
            return listBg.size();
        }

        public class BgHodler extends RecyclerView.ViewHolder{
            ImageView ivBackground;
            public BgHodler(@NonNull View itemView) {
                super(itemView);
                ivBackground = (ImageView) itemView.findViewById(R.id.ivBackground);
            }
            public void binData(int imageResource){
                ivBackground.setImageResource(imageResource);
            }
        }
    }
    public static List<Integer> getListBackGround(){
        List<Integer> arr = new ArrayList<>();
        arr.add(R.drawable.bg1);
        arr.add(R.drawable.bg2);
        arr.add(R.drawable.bg3);
        arr.add(R.drawable.bg4);
        arr.add(R.drawable.bg5);
        arr.add(R.drawable.bg6);
        arr.add(R.drawable.bg7);
        arr.add(R.drawable.bg8);
        arr.add(R.drawable.bg9);
        arr.add(R.drawable.bg10);
        arr.add(R.drawable.bg11);
        arr.add(R.drawable.bg12);
        arr.add(R.drawable.bg13);
        arr.add(R.drawable.bg14);
        arr.add(R.drawable.bg15);
        return arr;
    }
    public interface BgCallBack{
        void onBgClick(int ImageCode);
    }
    public interface BgBottomSheetCallBack{
        void onBgClick(int ImageCode);
    }
}
