package co.id.datascrip.mo_sam_div4_dts1.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import co.id.datascrip.mo_sam_div4_dts1.R;
import co.id.datascrip.mo_sam_div4_dts1.object.Photo;

/**
 * Created by benny_aziz on 02/24/2015.
 */
public class Adapter_List_Photo extends ArrayAdapter<Photo> {

    private Context context;
    private int resource;
    private ArrayList<Photo> photos;
    private LayoutInflater inflater;
    private SparseBooleanArray mSelected;

    public Adapter_List_Photo(Context context, int resource, ArrayList<Photo> photos) {
        super(context, resource, photos);
        this.mSelected = new SparseBooleanArray();
        this.context = context;
        this.resource = resource;
        this.photos = photos;
        inflater = LayoutInflater.from(this.context);
    }

    public int getPosition(Photo photo) {
        return photos.indexOf(photo);
    }

    public void update(Photo photo, int pos) {
        photos.remove(pos);
        photos.add(pos, photo);
        notifyDataSetChanged();
    }

    public void remove(int pos) {
        photos.remove(pos);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return photos.size();
    }

    @Override
    public Photo getItem(int pos) {
        // TODO Auto-generated method stub
        return photos.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        // TODO Auto-generated method stub
        return pos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.adapter_list_photo, null);

            viewHolder.description = (TextView) view.findViewById(R.id.ap_description);
            viewHolder.photo = (ImageView) view.findViewById(R.id.ap_image);
            viewHolder.posted = (ImageView) view.findViewById(R.id.ap_posted);

            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.description.setText(photos.get(position).getInfo());

        /*
        Bitmap image = null;
        if ((new File(photos.get(position).getPath()).exists())) {
            image = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(photos.get(position).getPath()), 200, 200);
        }

        viewHolder.photo.setImageBitmap(image);
        viewHolder.photo.setScaleType(ImageView.ScaleType.FIT_CENTER);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        viewHolder.photo.setLayoutParams(layoutParams);
        */
        Glide.with(context)
                .load(photos.get(position).getPath())
                .into(viewHolder.photo);
        if (photos.get(position).getPosted() == 1)
            viewHolder.posted.setImageResource(R.drawable.upload);
        else
            viewHolder.posted.setImageBitmap(null);

        return view;
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelected.put(position, value);
        else
            mSelected.delete(position);
        notifyDataSetChanged();
    }

    public SparseBooleanArray getSelectedId() {
        return mSelected;
    }

    public void ToggleSelection(int position) {
        selectView(position, !mSelected.get(position));
    }

    private static class ViewHolder {
        TextView description;
        ImageView photo, posted;
    }
}
