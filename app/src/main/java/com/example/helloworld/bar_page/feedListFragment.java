package com.example.helloworld.bar_page;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.helloworld.FeedContentActivity;
import com.example.helloworld.R;

import java.util.Random;

/**
 * Created by Administrator on 2016/12/8.
 */

public class FeedListFragment extends Fragment {
    View view;
    ListView listView;
    String[] data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_page_feed_list, null);

            listView = (ListView) view.findViewById(R.id.bar_feeds_list);
            listView.setAdapter(listAdapter);

            Random random = new Random();
            //Math.abs(int)返回绝对值
            data = new String[10 + Math.abs(random.nextInt() % 20)];

            for (int i =0; i < data.length;i++) {
                data[i] = "THIS ROW IS" + random.nextInt();
            }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    onItemClicked(position);
                }
            });
        }
        return  view;
    }

    private void onItemClicked(int position) {
        String text = data[position];

        Intent intent = new Intent(getActivity(), FeedContentActivity.class);
        intent.putExtra("text", text);
        startActivity(intent);

    }

    BaseAdapter listAdapter = new BaseAdapter() {
        @Override
        //计数器，这里设置传入数据数组的长度
        public int getCount() {
            return (data == null ? 0 : data.length);
        }

        @Override
        //想要返回的子项内容
        public Object getItem(int position) {
            return data[position];
        }

        @Override
        //子项位置
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("InflateParams")
        @Override
        //convertView为listview的items子项，parent为listview
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;

            if (convertView == null) {
                //getcontext()获取parent这个类,
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                //listview的items布局
                view = inflater.inflate(android.R.layout.simple_list_item_1, null);
            } else {
                view = convertView;
            }

            TextView textView = (TextView) view.findViewById(android.R.id.text1);

            textView.setText(data[position]);
            return view;
        }
    };
}
