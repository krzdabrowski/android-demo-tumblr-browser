package com.example.trubul.tumblrbrowser;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by krzysiek
 * On 4/9/18.
 */

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private final int IMAGE = 0, TEXT = 1, OTHER = 2;
    private Context mContext;
    private List<Object> mPostList;


    public RecyclerViewAdapter(Context context, List<Object> postList) {
        mContext = context;
        mPostList = postList;
    }


    // 0) ViewHolders - no need to call findViewById() all the time
    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnailPhoto;

        public ImageViewHolder(View itemView) {
            super(itemView);
            this.thumbnailPhoto = itemView.findViewById(R.id.thumbnail_photo);
        }
    }

    static class TextViewHolder extends RecyclerView.ViewHolder {
        TextView thumbnailText;

        public TextViewHolder(View itemView) {
            super(itemView);
            this.thumbnailText = itemView.findViewById(R.id.thumbnail_text);
        }
    }

//    static class OtherViewHolder extends RecyclerView.ViewHolder {
//        ImageView thumbnailOther;
//
//        public OtherViewHolder(View itemView) {
//            super(itemView);
//            this.thumbnailOther = itemView.findViewById(R.id.thumbnail_photo);
//        }
//    }


    // 1) getItemViewType - returns the view type of the item at position for the purposes of view recycling
    @Override
    public int getItemViewType(int position) {

        if((mPostList == null) || (mPostList.size() == 0)) {
            return -1;  // initialization
        } else {
            if (mPostList.get(position) instanceof PostPhoto) {
                return IMAGE;
            } else if (mPostList.get(position) instanceof PostText) {
                return TEXT;
            } else if (mPostList.get(position) instanceof PostOther) {
                return OTHER;
            }
        }

        return -1;
    }


    // 2) onCreateViewHolder - create layout object from XML and then ViewHolder; called by LayoutManager when it needs a new view
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case TEXT:
                View v1 = inflater.inflate(R.layout.text, parent, false);
                viewHolder = new TextViewHolder(v1);
                break;
            case IMAGE:
                View v2 = inflater.inflate(R.layout.photo, parent, false);
                viewHolder = new ImageViewHolder(v2);
                break;
            case OTHER:
                View v3 = inflater.inflate(R.layout.photo, parent, false);
                viewHolder = new ImageViewHolder(v3);
                break;
            default:
                Log.d(TAG, "onCreateViewHolder: default (init) mode");
                View v = inflater.inflate(R.layout.text, parent, false);
                viewHolder = new TextViewHolder(v);
                break;
        }
        return viewHolder;
    }



    // 3) onBindViewHolder - fill single element with data; called by LayoutManager when it wants new data to be stored in a ViewHolder to display it
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        switch (viewHolder.getItemViewType()) {

            case TEXT:
                TextViewHolder textHolder = (TextViewHolder) viewHolder;

                PostText singleText = (PostText) mPostList.get(position);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    textHolder.thumbnailText.setText(Html.fromHtml(singleText.getText(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    textHolder.thumbnailText.setText(Html.fromHtml(singleText.getText()));
                }
                break;

            case IMAGE:
                ImageViewHolder imageHolder = (ImageViewHolder) viewHolder;

                if((mPostList == null) || (mPostList.size() == 0)) {
                    imageHolder.thumbnailPhoto.setImageResource(R.drawable.placeholder);

                } else {
                    PostPhoto singlePhoto = (PostPhoto) mPostList.get(position);

                    // Picasso = singleton but no gif support -> Glide but sizing glitches -> Picasso here, PhotoDetail has Glide
                    Picasso.with(mContext)
                            .load(singlePhoto.getSmallImage())
                            .error(R.drawable.placeholder)
                            .placeholder(R.drawable.placeholder)
                            .into(imageHolder.thumbnailPhoto);
                }
                break;

            case OTHER:
                ImageViewHolder otherHolder = (ImageViewHolder) viewHolder;

                if((mPostList == null) || (mPostList.size() == 0)) {
                    otherHolder.thumbnailPhoto.setImageResource(R.drawable.placeholder);

                } else {
                    PostOther singleOther = (PostOther) mPostList.get(position);

                    if (singleOther.getType().equals("audio")) {
                        Picasso.with(mContext)
                                .load(singleOther.getURL())  // calculated
                                .error(R.drawable.audio)  // it'll be an error every time
                                .placeholder(R.drawable.placeholder)
                                .into(otherHolder.thumbnailPhoto);

                    } else if (singleOther.getType().equals("video")) {
                        Picasso.with(mContext)
                                .load(singleOther.getURL())  // calculated
                                .error(R.drawable.movie)  // it'll be an error every time
                                .placeholder(R.drawable.placeholder)
                                .into(otherHolder.thumbnailPhoto);
                    }
                }
                break;

            default:
                Log.d(TAG, "onBindViewHolder: default mode");
                TextViewHolder initHolder = (TextViewHolder) viewHolder;

                String message = mContext.getString(R.string.message_no_data);
                initHolder.thumbnailText.setText(message);

                break;
        }
    }

    // 4) getItemCount - number of all elements
    @Override
    public int getItemCount() {
        if ((mPostList != null) && (mPostList.size() != 0)) {
            return mPostList.size();
        } else {
            return 1;  // 1 = placeholder
        }
    }


    // update data when new one is downloaded
    void loadNewData(List<Object> newSinglePosts) {
        mPostList = newSinglePosts;
        notifyDataSetChanged();  // tell it to "registered observers" (like RecyclerView) = refresh display
    }

    public PostPhoto getPhoto(int position) {
        if ((mPostList != null) && (mPostList.size() != 0)) {
            return (PostPhoto) mPostList.get(position);
        } else {
            return null;
        }
    }

    public PostText getText(int position) {
        if ((mPostList != null) && (mPostList.size() != 0)) {
            return (PostText) mPostList.get(position);
        } else {
            return null;
        }
    }

    public PostOther getOther(int position) {
        if ((mPostList != null) && (mPostList.size() != 0)) {
            return (PostOther) mPostList.get(position);
        } else {
            return null;
        }
    }

}
