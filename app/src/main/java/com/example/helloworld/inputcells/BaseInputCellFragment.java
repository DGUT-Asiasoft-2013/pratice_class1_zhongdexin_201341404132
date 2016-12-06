package com.example.helloworld.inputcells;

import android.app.Fragment;

/**
 * Created by Administrator on 2016/12/5.
 */

public abstract class BaseInputCellFragment extends Fragment {

    public abstract void setLabelText(String s);
    public abstract void setEditHint(String s);
}
