package com.example.helloworld.bar_page;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.helloworld.FeedContentActivity;
import com.example.helloworld.R;
import com.example.helloworld.api.Service;
import com.example.helloworld.entity.Article;
import com.example.helloworld.entity.Page;
import com.example.helloworld.fragments.widgets.AvatarView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/8.
 */

public class FeedListFragment extends Fragment {
    View view;
    ListView listView;
    View btmloadMore;
    TextView textLoadMore;

//    String[] data;

    List<Article> data;
    int page;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_page_feed_list, null);

            btmloadMore = inflater.inflate(R.layout.widget_load_more_buttom, null);
            textLoadMore = (TextView) btmloadMore.findViewById(R.id.load_more_btm);

            listView = (ListView) view.findViewById(R.id.bar_feeds_list);
            listView.addFooterView(btmloadMore);
            listView.setAdapter(listAdapter);

//            Random random = new Random();
//            //Math.abs(int)返回绝对值
//            data = new String[10 + Math.abs(random.nextInt() % 20)];
//
//            for (int i =0; i < data.length;i++) {
//                data[i] = "THIS ROW IS" + random.nextInt();
//            }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    onItemClicked(position);
                }
            });

            btmloadMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadmore();
                }
            });
        }
        return view;
    }

    private void loadmore() {
        btmloadMore.setEnabled(false);
        textLoadMore.setText("加载中");

        Request request = Service.requestBuilderWithApi("feeds/" + (page + 1)).get().build();

        Service.getShareClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btmloadMore.setEnabled(true);
                        textLoadMore.setText("加载更多");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btmloadMore.setEnabled(true);
                        textLoadMore.setText("加载更多");
                    }
                });

                try {
                    final Page<Article> feeds = new ObjectMapper().readValue(response.body().string(), new TypeReference<Page<Article>>() {
                    });
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (feeds.getNumber() > page) {
                                if (data == null) {
                                    data = feeds.getContent();
                                } else {
                                    data.addAll(feeds.getContent());
                                }
                                page = feeds.getNumber();

                                listAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    //重新加载的时候重新加载列表数据，等于刷新新内容
    public void onResume() {
        super.onResume();
        reload();
    }

    private void reload() {
        Request request = Service.requestBuilderWithApi("feeds")
                .get().build();

        Service.getShareClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(getActivity())
                                .setMessage(e.getMessage()).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    //读取服务器返回的数据，保存在data中
                    final Page<Article> data = new ObjectMapper()
                            .readValue(response.body().string(), new TypeReference<Page<Article>>() {
                            });
//                    FeedListFragment.this.page = data.getNumber();
//                    FeedListFragment.this.data = data.getContent();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //重置listAdapter回到初始状态
                            FeedListFragment.this.page = data.getNumber();
                            FeedListFragment.this.data = data.getContent();

                            listAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (final Exception e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(getActivity())
                                    .setMessage(e.getMessage()).show();
                        }
                    });
                }
            }
        });
    }

    private void onItemClicked(int position) {
//        String text = data[position];

        Article text = data.get(position);

        Intent intent = new Intent(getActivity(), FeedContentActivity.class);
        intent.putExtra("data", text);
        startActivity(intent);

    }

    BaseAdapter listAdapter = new BaseAdapter() {
        @Override
        //计数器，这里设置传入数据数组的长度
        public int getCount() {
            return (data == null ? 0 : data.size());
        }

        @Override
        //想要返回的子项内容
        public Object getItem(int position) {
            return data.get(position);
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
//                view = inflater.inflate(android.R.layout.simple_list_item_1, null);
                view = inflater.inflate(R.layout.widget_feed_item, null);
            } else {
                view = convertView;
            }

//            TextView textView = (TextView) view.findViewById(android.R.id.text1);
//            textView.setText(data[position]);

            TextView textContent = (TextView) view.findViewById(R.id.feed_item_text);
            TextView textTitle = (TextView) view.findViewById(R.id.feed_item_title);
            TextView textAuthorName = (TextView) view.findViewById(R.id.feed_item_username);
            TextView textDate = (TextView) view.findViewById(R.id.feed_item_date);
            AvatarView avatar = (AvatarView) view.findViewById(R.id.feed_item_avatar);

            Article article = data.get(position);

            textContent.setText(article.getText());
            textTitle.setText(article.getTitle());
//            textAuthorName.setText(article.getAuthorName());
//            avatar.load(Service.serverAddress + article.getAuthorAvatar());
            textAuthorName.setText(article.getAuthor().getName());
            avatar.load(article.getAuthor());

            String dateStr = DateFormat.format("yyyy-MM-dd hh:mm", article.getCreateDate()).toString();
            textDate.setText(dateStr);

            return view;
        }
    };
}
