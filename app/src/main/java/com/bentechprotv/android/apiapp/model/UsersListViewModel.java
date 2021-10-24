package com.bentechprotv.android.apiapp.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bentechprotv.android.apiapp.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersListViewModel extends ArrayAdapter<GitUser> {
    private List<GitUser> users;
    private int resource;

    public UsersListViewModel(@NonNull Context context, int resource, List<GitUser> data) {
        super(context, resource,data);
        this.users = data;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listViewItem = convertView;
        if(listViewItem == null) {
            listViewItem = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }
            CircleImageView imageViewAvatar = (CircleImageView) listViewItem.findViewById(R.id.imageViewUser);
            TextView txtLogin = listViewItem.findViewById(R.id.txtLoginList);
            TextView txtScore = listViewItem.findViewById(R.id.txtScore);
            txtLogin.setText(users.get(position).login);
            txtScore.setText(String.valueOf(users.get(position).score));
        try {
            URL url = new URL(users.get(position).avatarUrl);
            Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
            imageViewAvatar.setImageBitmap(bitmap);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



            return  listViewItem ;

    }
}
