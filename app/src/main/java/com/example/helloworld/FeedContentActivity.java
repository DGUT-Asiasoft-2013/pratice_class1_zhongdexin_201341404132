package com.example.helloworld;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.helloworld.api.Service;
import com.example.helloworld.entity.Article;
import com.example.helloworld.entity.Comment;
import com.example.helloworld.entity.Page;
import com.example.helloworld.fragments.widgets.AvatarView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/8.
 */

public class FeedContentActivity extends Activity {

    private Article article;
    private View loadMoreView;

    private Button btnLikes;
    private boolean isLiked;

    List<Comment> comments;
    int page = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_feed_content);

        article = (Article) getIntent().getSerializableExtra("data");

        //评论列表
        ListView list = (ListView) findViewById(R.id.feed_content_list);

        //文章详情+点赞、喜欢、评论按钮的自定义view
        View headerView = LayoutInflater.from(this).inflate(R.layout.widget_article_detail, null);

        TextView textContent = (TextView) headerView.findViewById(R.id.article_detail_text);
        TextView textTitle = (TextView) headerView.findViewById(R.id.article_detail_title);
        TextView textAuthorName = (TextView) headerView.findViewById(R.id.article_detail_username);
        TextView textDate = (TextView) headerView.findViewById(R.id.article_detail_date);
        AvatarView avatar = (AvatarView) headerView.findViewById(R.id.article_detail_avatar);

        textContent.setText(article.getText());
        textTitle.setText(article.getTitle());
        textAuthorName.setText(article.getAuthor().getName());
        avatar.load(article.getAuthor());

        btnLikes = (Button) headerView.findViewById(R.id.article_detail_like);

        String dateStr = DateFormat.format("yyyy-MM-dd hh:mm", article.getCreateDate()).toString();
        textDate.setText(dateStr);

        headerView.findViewById(R.id.article_detail_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeComment();
            }
        });

        btnLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleLikes();
            }
        });

        //加载更多按钮
        loadMoreView = LayoutInflater.from(this).inflate(R.layout.widget_load_more_buttom, null);
        loadMoreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMore();
            }
        });


        list.addHeaderView(headerView, null, false);
        list.addFooterView(loadMoreView);

        list.setAdapter(adapter);
    }

    //点赞后，刷新数据
    private void toggleLikes() {
        MultipartBody body = new MultipartBody.Builder()
                .addFormDataPart("likes", String.valueOf(!isLiked))
                .build();

        Request request = Service.requestBuilderWithApi("article/" + article.getId() + "likes")
                .post(body)
                .build();

        Service.getShareClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        reload();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        reload();
                    }
                });
            }
        });
    }

    private void loadMore() {

        Request request = Service.requestBuilderWithApi("article/" + article.getId() + "/comment/" + (page + 1)).get().build();

        Service.getShareClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FeedContentActivity.this.onFailure(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final Page<Comment> data = new ObjectMapper().readValue(response.body().string(), new TypeReference<Page<Comment>>() {
                    });

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            FeedContentActivity.this.appendData(data);
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            FeedContentActivity.this.onFailure(e);
                        }
                    });
                }
            }
        });
    }

    private void appendData(Page<Comment> data) {
        if (data.getNumber() > page) {
            //移动页码至下一页
            page = data.getNumber();

            if (comments == null) {
                comments = data.getContent();
            } else {
                comments.addAll(data.getContent());
            }
        }
        adapter.notifyDataSetChanged();
    }

    BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return (comments == null ? 0 : comments.size());
        }

        @Override
        public Object getItem(int position) {
            return comments.get(position);
        }

        @Override
        public long getItemId(int position) {
            return comments.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //因为在listview的属性加入了tools指向一个items布局
            //所以不同于FeedListActivity，不用View view 来创建新布局返回
            //设置items子项的style
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_comment_item, null);
            }

            Comment comment = comments.get(position);

            TextView textContent = (TextView) convertView.findViewById(R.id.comment_item_text);
            TextView textAuthorName = (TextView) convertView.findViewById(R.id.comment_item_username);
            TextView textDate = (TextView) convertView.findViewById(R.id.comment_item_date);
            AvatarView avatar = (AvatarView) convertView.findViewById(R.id.comment_item_avatar);

            textContent.setText(comment.getText());
            textAuthorName.setText(comment.getAuthor().getName());
            avatar.load(comment.getAuthor());

            String dateStr = DateFormat.format("yyyy-MM-dd hh:mm", comment.getCreateDate()).toString();
            textDate.setText(dateStr);

            return convertView;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        reload();
    }

    private void reload() {
        reloadLikes();
        checkLiked();

        Request request = Service.requestBuilderWithApi("article/" + article.getId() + "/comments")
                .get().build();

        Service.getShareClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FeedContentActivity.this.onFailure(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final Page<Comment> data = new ObjectMapper().readValue(response.body().string(), new TypeReference<Page<Comment>>() {
                    });

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            FeedContentActivity.this.reloadData(data);
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            FeedContentActivity.this.onFailure(e);
                        }
                    });
                }
            }
        });

    }

    private void checkLiked() {
        Request request = Service.requestBuilderWithApi("article/" + article.getId() + "/isliked")
                .get().build();

        Service.getShareClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onCheckLikedResult(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final String responseString = response.body().string();
                    final Boolean result = new ObjectMapper().readValue(responseString, Boolean.class);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onCheckLikedResult(result);
                        }
                    });
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onCheckLikedResult(false);
                        }
                    });
                }
            }
        });

    }

    private void onCheckLikedResult(Boolean result) {
        isLiked = result;
        btnLikes.setTextColor(result ? Color.BLUE : Color.BLACK);
    }

    private void reloadLikes() {
        Request request = Service.requestBuilderWithApi("article/" + article.getId() + "/likes")
                .get().build();

        Service.getShareClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onReloadLikesResult(0);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String responseString = response.body().string();

                    //第二个参数为数据类型
                    final Integer count = new ObjectMapper().readValue(responseString, Integer.class);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onReloadLikesResult(count);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onReloadLikesResult(0);
                        }
                    });
                }
            }
        });
    }

    private void onReloadLikesResult(int i) {
        if (i > 0) {
            btnLikes.setText("喜欢(" + i + ")");
        } else {
            btnLikes.setText("喜欢");
        }
    }

    private void onFailure(Exception e) {
        new AlertDialog.Builder(this).setMessage(e.getMessage()).show();
    }

    private void reloadData(Page<Comment> data) {

        page = data.getNumber();
        comments = data.getContent();
        adapter.notifyDataSetChanged();
    }

    private void makeComment() {
        Intent itnt = new Intent(this, NewCommentActivity.class);
        itnt.putExtra("data", article);
        startActivity(itnt);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.none);

    }
}
