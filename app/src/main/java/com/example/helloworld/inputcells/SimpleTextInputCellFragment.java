package com.example.helloworld.inputcells;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.helloworld.R;

/**
 * Created by Administrator on 2016/12/5.
 */

public class SimpleTextInputCellFragment extends BaseInputCellFragment {

    TextView label;
    EditText edit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inputcell_simpletext, container);

        label = (TextView) view.findViewById(R.id.label);
        edit = (EditText) view.findViewById(R.id.text);

        return view;
    }

    public void setLabelText(String s) {
        label.setText(s);
    }

    public void setEditHint(String s) {
        edit.setHint(s);
    }

    public void setEditInputType(boolean b) {
        if (b) {
            edit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            edit.setInputType(InputType.TYPE_CLASS_TEXT);
        }
    }
}
