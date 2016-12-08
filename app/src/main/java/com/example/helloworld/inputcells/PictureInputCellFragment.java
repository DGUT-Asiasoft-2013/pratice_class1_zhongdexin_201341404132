package com.example.helloworld.inputcells;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.helloworld.R;

import java.io.IOException;

/**
 * Created by Administrator on 2016/12/5.
 */

public class PictureInputCellFragment extends Fragment {

    final int REQUESTCODE_CAMERA = 1;
    final int REQUESTCODE_ALBUNM = 2;


    ImageView picture;
    TextView tittle,tips;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inputcell_picture, container);

        picture = (ImageView)view.findViewById(R.id.headimage);
        tittle = (TextView) view.findViewById(R.id.tittle);
        tips = (TextView) view.findViewById(R.id.tips);

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPictureClick();
            }
        });

        return view;
    }

    private void onPictureClick() {
        String[] items = {
                "拍照",
                "相册"
        };

        new AlertDialog.Builder(getActivity())
                .setTitle(tittle.getText().toString())
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                takePhoto();
                                break;

                            case 1:
                                pickFromAlbunm();
                                break;

                            default:
                                break;
                        }
                    }
                }).setNegativeButton("取消",null).show();
    }

    void takePhoto() {
        Intent itnt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(itnt,REQUESTCODE_CAMERA);
    }

    void pickFromAlbunm() {
        Intent itnt = new Intent(Intent.ACTION_GET_CONTENT);
        itnt.setType("image/*");
        startActivityForResult(itnt,REQUESTCODE_ALBUNM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_CANCELED) {
            return ;
        }
        if (requestCode == REQUESTCODE_CAMERA) {

            Bitmap bmp = (Bitmap) data.getExtras().get("data");
            picture.setImageBitmap(bmp);
        } else if (requestCode == REQUESTCODE_ALBUNM) {
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                picture.setImageBitmap(bmp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setTittleText(String s) {
        tittle.setText(s);
    }


    public void setTipsText(String s) {
        tips.setText(s);
    }
}
