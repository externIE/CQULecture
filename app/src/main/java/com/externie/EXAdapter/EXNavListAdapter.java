package com.externie.EXAdapter;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.externie.EXHelper.EXSQLiteHelper;
import com.externie.EXViews.EXNavItem;
import com.externie.cqulecture.PostActivity;
import com.externie.cqulecture.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by externIE on 16/5/19.
 */
public class EXNavListAdapter extends BaseAdapter {
    private final String TAG = "EXNavListAdapter:";
    private LayoutInflater mInflater;
    private Context mCtx;
    private List<EXNavItem> mItems;

    public EXNavListAdapter(Context ctx){
        mInflater = LayoutInflater.from(ctx);
        mItems = new ArrayList<>();
        mCtx = ctx;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        EXNavItem item = mItems.get(position);
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.nav_item,parent,false);
        }
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                TextView tv_title = (TextView)v.findViewById(R.id.nav_title);
                TextView tv_date = (TextView)v.findViewById(R.id.nav_date);
                TextView tv_time = (TextView)v.findViewById(R.id.nav_time);
                TextView tv_address = (TextView)v.findViewById(R.id.nav_address);
                String title = tv_title.getText().toString();
                String date = tv_date.getText().toString();
                String time = tv_time.getText().toString();
                String address = tv_address.getText().toString();
//                Log.i(TAG,tv_title.getText().toString());
                showDeleteDialog(position,title,date,time,address);
                return true;
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, PostActivity.class);
                intent.putExtra("url", mItems.get(position).getUrl());
                mCtx.startActivity(intent);
            }
        });
        TextView tv_title = (TextView)convertView.findViewById(R.id.nav_title);
        TextView tv_date = (TextView)convertView.findViewById(R.id.nav_date);
        TextView tv_time = (TextView)convertView.findViewById(R.id.nav_time);
        TextView tv_address = (TextView)convertView.findViewById(R.id.nav_address);

        tv_title.setText(item.getTitle());
        tv_date.setText(item.getDate());
        tv_time.setText(item.getTime());
        tv_address.setText(item.getAddress());
        return convertView;
    }

    public void setItems(List<EXNavItem> mItems) {
        this.mItems = mItems;
    }

    public void addItem(EXNavItem item){
        this.mItems.add(item);
    }

    public void addItem(String url,String title,String date,String time,String address){
        this.mItems.add(new EXNavItem(title,date,time,address,url));
    }

    public void removeItem(int position){
        mItems.remove(position);
        notifyDataSetChanged();
    }

    private void showDeleteDialog(final int position,final String title,final String date,final String time,final String address){
        AlertDialog.Builder buidler = new AlertDialog.Builder(mCtx);
        buidler.setTitle("删除收藏");
        buidler.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EXNavListAdapter.this.removeItem(position);
                EXSQLiteHelper.removeData(mCtx, title,date,time,address);
            }
        });
        buidler.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        buidler.show();
    }
}
