package com.codepath.nytimessearch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kemleynieva on 6/21/16.
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    public void clearAll() {
        mArticles = new ArrayList<>();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder  {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        //public TextView nameTextView;
        //public Button messageButton;
        ImageView imageView;
        TextView tvTitle;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            //nameTextView = (TextView) itemView.findViewById(R.id.contact_name);
            //essageButton = (Button) itemView.findViewById(R.id.message_button);

            imageView = (ImageView) itemView.findViewById(R.id.ivImage);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);


        }


    }

    //private List<Article> mContacts;
    // Store the context for easy access
    private Context mContext;

    ArrayList<Article> mArticles;
    //private Article mArticle;

    // Pass in the contact array into the constructor
    public ArticleAdapter(Context context, ArrayList<Article> articles) {
        //mContacts = contacts;
        mArticles =articles;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_article_results, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ArticleAdapter.ViewHolder viewHolder, int position) {

        Article article = mArticles.get(position);

        ImageView imageView = viewHolder.imageView;
        imageView.setImageResource(0);

        TextView tvTitle = viewHolder.tvTitle;
        tvTitle.setText(article.getHeadlines());

        String thumbnail = article.getThumbnail();

        if(!TextUtils.isEmpty(thumbnail)){
            Glide.with(getContext()).load(thumbnail).placeholder(R.drawable.ic_action_name).fitCenter().into(imageView);
        }
        else{
            Glide.with(getContext()).load(R.mipmap.ic_placehold).fitCenter().into(imageView);
        }



    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        if(mArticles != null) {
            return mArticles.size();
        }
        else{
            return 0;
        }
    }
}
