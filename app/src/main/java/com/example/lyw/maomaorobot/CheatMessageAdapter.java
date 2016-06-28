package com.example.lyw.maomaorobot;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lyw.maomaorobot.Activity.SecondActivity;
import com.example.lyw.maomaorobot.Bean.BaseResponse;
import com.example.lyw.maomaorobot.Bean.CaiPuResponse;
import com.example.lyw.maomaorobot.Bean.CheatMessage;

import com.example.lyw.maomaorobot.Bean.LinkResponse;
import com.example.lyw.maomaorobot.Bean.NewsResponse;
import com.example.lyw.maomaorobot.Bean.TextResponse;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener
        .SimpleImageLoadingListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by LYW on 2016/5/28.
 */
public class CheatMessageAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList mDates;
    private static final String TAG = "CheatMessageAdapter";
    private Context mContext;

    public CheatMessageAdapter(Context context, ArrayList list) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDates = list;
    }

    public ArrayList getmDates() {
        return mDates;
    }

    public void setmDates(ArrayList mDates) {
        this.mDates = mDates;
    }

    @Override
    public int getCount() {
        return mDates.size();

    }

    @Override
    public int getItemViewType(int position) {
        if (mDates.get(position) instanceof CheatMessage) {
            return 0;
        } else
            return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public java.lang.Object getItem(int i) {
        return mDates.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        Log.d(TAG, "getView: ...");
        if (getItemViewType(position) == 0) {
            View itemView = mInflater.inflate(R.layout.cheat_me_layout, null);
            new CheatMessageViewHodler(itemView, (CheatMessage) mDates.get(position));
            return itemView;
        } else {
            BaseResponse response = (BaseResponse) mDates.get(position);
            return initViewHolder(response.getCode(), position);
        }
//        ViewHolder mHolder = null;
//        if (convertView == null) {
//            if (getItemViewType(position) == 0) {
//                convertView = mInflater.inflate(R.layout.cheat_robot_layout,
//                        null);
//                mHolder = new ViewHolder();
//                mHolder.msgText = (TextView) convertView.findViewById(R.id
//                        .id_tvt_cheat_robot);
//                mHolder.showTimeText = (TextView) convertView.findViewById(R
//                        .id.id_tvt_timeshow);
//
//            } else {
//                convertView = mInflater.inflate(R.layout.cheat_me_layout,
// null);
//                mHolder = new ViewHolder();
//                mHolder.msgText = (TextView) convertView.findViewById(R.id
//                        .id_tvt_cheat_me);
//                mHolder.showTimeText = (TextView) convertView.findViewById(R
//                        .id.id_tvt_timeshow);
//
//            }
//            convertView.setTag(mHolder);
//        } else {
//            mHolder = (ViewHolder) convertView.getTag();
//        }
//        if (message instanceof ReturnMessage) {
//            setDate(mDates.get(position), mHolder);
//        } else if (message instanceof CheatMessage) {
//            setDate(mDates.get(position), mHolder);
//        }
//        return convertView;
    }

    private View initViewHolder(int code, int position) {
        View itemView = null;
        switch (code) {
            case BaseResponse.RESPONSE_TYPE_TEXT:

                itemView = mInflater.inflate(R.layout
                        .cheat_robot_layout, null);
                new TextResponseViewHodler(itemView, (BaseResponse) mDates
                        .get(position));
                break;

            case BaseResponse.RESPONSE_TYPE_LINK:
                itemView = mInflater.inflate(R.layout
                        .layout_item_link_message, null);
                new LinkResponseViewHolder(itemView, (BaseResponse) mDates
                        .get(position));
                break;
            case BaseResponse.RESPONSE_TYPE_NEWS:
                itemView = mInflater.inflate(R.layout
                        .layout_item_news_message, null);
                new NewsResponseViewHolder(itemView, (BaseResponse) mDates
                        .get(position));
                break;
            case BaseResponse.RESPONSE_TYPE_CAIPU:
                itemView = mInflater.inflate(R.layout
                        .layout_item_caipu_message, null);
                new CaiPuResponseViewHolder(itemView, (BaseResponse) mDates
                        .get(position));
                break;
            default:
                break;

        }
        itemView.setTag(mDates.get(position));
        return itemView;
    }


    class TextResponseViewHodler {

        View parent;
        public TextView mShowTime;
        public TextView mTextView;

        public TextResponseViewHodler(View parent, BaseResponse response) {
            this.parent = parent;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            TextResponse textResponse = (TextResponse) response;
            mTextView = (TextView) parent.findViewById(R.id
                    .id_tvt_cheat_robot);
            mTextView.setText(textResponse.getText());
            mShowTime = (TextView) parent.findViewById(R.id.id_tvt_timeshow);
            mShowTime.setText(sdf.format(new Date(System.currentTimeMillis())));
        }
    }

    class LinkResponseViewHolder extends TextResponseViewHodler {
        public TextView mUrl;

        public LinkResponseViewHolder(View parent, BaseResponse response) {
            super(parent, response);
            LinkResponse linkResponse = (LinkResponse) response;
            mUrl = (TextView) parent.findViewById(R.id
                    .txv_list_view_message_url_link);

            mUrl.setText(linkResponse.getUrl());
            jumpTo(mUrl,response);
        }

    }


    class NewsResponseViewHolder extends TextResponseViewHodler {
        public List<NewsItemViewHolder> mItems;

        class NewsItemViewHolder {
            ImageView mIcon;
            TextView mTextViewTitle;

        }

        public NewsResponseViewHolder(View parent, BaseResponse response) {
            super(parent, response);
            NewsResponse newsResponse = (NewsResponse) response;
            LinearLayout container = (LinearLayout) parent.findViewById(R.id
                    .layout_news_container);
            List<NewsResponse.ListBean> list = newsResponse.getList();
            for (final NewsResponse.ListBean bean : list) {
                View item = mInflater.inflate(R.layout
                        .layout_item_news_item, null);
                ImageView igv = (ImageView) item.findViewById(R.id
                        .igv_news_item_icon);
                TextView txv = (TextView) item.findViewById(R.id
                        .txv_news_item_title);
                // TODO: 2016/6/7  设定image
                igv.setImageResource(R.mipmap.ic_launcher);
                txv.setText(bean.getArticle());
                // TODO: 2016/6/7 设定跳转
                jumpTo(txv,bean);

                LinearLayout.LayoutParams params = new LinearLayout
                        .LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                container.addView(item, params);
            }
        }
    }

    private void jumpTo(TextView textview,final Object o) {

        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final  String jumpUrl;
                if (o instanceof LinkResponse) {
                    jumpUrl   = ((LinkResponse)o).getUrl();
                }else if (o instanceof CaiPuResponse.ListBean){
                    jumpUrl   = ((CaiPuResponse.ListBean)o)
                            .getDetailurl();
                }else {
                    jumpUrl   = ((NewsResponse.ListBean)o)
                            .getDetailurl();
                }
                Uri url = Uri.parse(jumpUrl);
                Intent i = new Intent(mContext, SecondActivity.class);
                i.setData(url);
                mContext.startActivity(i);
            }
        });
    }

    class CaiPuResponseViewHolder extends TextResponseViewHodler {

        public List<CaiPuItemViewHolder> mItems;

        class CaiPuItemViewHolder {
            ImageView mIcon;
            TextView mTextViewTitle;
            TextView mTextViewInfo;
        }

        public CaiPuResponseViewHolder(View parent, BaseResponse response) {
            super(parent, response);
            // TODO: 2016/6/7 可以参考上面
            CaiPuResponse caiPuResponse = (CaiPuResponse) response;
            LinearLayout contanner = (LinearLayout) parent.findViewById(R.id.layout_caipu_container);
            List<CaiPuResponse.ListBean> list = caiPuResponse.getList();


            //你仔细看看我上面是怎么写的。 你这句话不就相当于new了一个新的view
            // 你给这个view赋值有什么用？他又不会添加到listview中去，真正添加进去的是parent吧！
            // 另外，我上面是动态添加item的吧，因为你并不知道服务器会给你返回多少的item，so，还是仿照上面写吧

            // TODO: 2016/6/14 设置图标
            for (CaiPuResponse.ListBean bean:list
                 ) {
                View item = mInflater.inflate(R.layout.layout_item_caipi_item,null);
                final ImageView mIcon = (ImageView) item.findViewById(R.id.igv_caipu_icon);
                TextView mTitle = (TextView) item.findViewById(R.id.txv_caipu_item_title);
                TextView mInfo = (TextView) item.findViewById(R.id.txv_caipu_item_info);
                String imageUrl = bean.getIcon();
                setImageIcon(imageUrl,mIcon);
              CaiPuResponse.ListBean data = list.get(0);
                //你仔细看看我上面是怎么写的。 你这句话不就相当于new了一个新的view
            // 你给这个view赋值有什么用？他又不会添加到listview中去，真正添加进去的是parent吧！
            // 另外，我上面是动态添加item的吧，因为你并不知道服务器会给你返回多少的item，so，还是仿照上面写吧
                 CaiPuItemViewHolder caiPuItemViewHolder =new CaiPuItemViewHolder();
                caiPuItemViewHolder.mIcon = (ImageView) parent.findViewById(R.id.igv_caipu_icon);
               caiPuItemViewHolder.mTextViewTitle = (TextView) parent.findViewById(R.id.txv_caipu_item_title);
               caiPuItemViewHolder.mTextViewInfo = (TextView) parent.findViewById(R.id.txv_caipu_item_info);
                mIcon.setImageResource(R.mipmap.ic_launcher);
                mTitle.setText(bean.getName());
                mInfo.setText(bean.getInfo());

                LinearLayout.LayoutParams params = new LinearLayout
                        .LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                contanner.addView(item,params);
            }

        }
    }


    class CheatMessageViewHodler {

        View parent;
        public TextView mShowTime;
        public TextView mTextView;

        public CheatMessageViewHodler(View parent, CheatMessage message) {
            this.parent = parent;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mTextView = (TextView) parent.findViewById(R.id
                    .id_tvt_cheat_me);
            mTextView.setText(message.getMsg());
            mShowTime = (TextView) parent.findViewById(R.id.id_tvt_timeshow);
            mShowTime.setText(sdf.format(new Date(System.currentTimeMillis())));
        }
    }
   private void setImageIcon(String url, final ImageView image){
       ImageSize size = new ImageSize(70, 70);
       ImageLoader.getInstance().loadImage(url, size, new SimpleImageLoadingListener() {

           @Override
           public void onLoadingComplete(String imageUri, View
                   view, Bitmap loadedImage) {
               super.onLoadingComplete(imageUri, view, loadedImage);
               image.setImageBitmap(loadedImage);
           }
       });

   }
}
