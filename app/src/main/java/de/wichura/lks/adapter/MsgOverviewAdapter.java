package de.wichura.lks.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.List;

import de.wichura.lks.R;
import de.wichura.lks.http.Urls;
import de.wichura.lks.mainactivity.Constants;
import de.wichura.lks.models.GroupedMsgItem;

import static de.wichura.lks.mainactivity.Constants.UNREAD_MESSAGES;

/**
 * Created by Bernd Wichura on 20.06.2016.
 * Luftkraftsport
 */
public class MsgOverviewAdapter extends ArrayAdapter<GroupedMsgItem> {

    private Context context;

    public MsgOverviewAdapter(final Context context, final int resourceId, final List<GroupedMsgItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    private static class ViewHolder {
        TextView title;
        TextView name;
        TextView date;
        ImageView unreadMessages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.msg_overview_item, parent, false);

            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.ad_title);
            holder.name = (TextView) convertView.findViewById(R.id.user_name);
            holder.date = (TextView) convertView.findViewById(R.id.msg_date);
            holder.unreadMessages = (ImageView) convertView.findViewById(R.id.unread_message);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        // getting ad data for the row
        final GroupedMsgItem rowItem = getItem(position);

        ImageView thumbNail = (ImageView) convertView.findViewById(R.id.ad_image);
        Picasso.with(context).load(Urls.MAIN_SERVER_URL_V3 + "pictures/" + getMainImageId(rowItem) + "/thumbnail").into(thumbNail);

        holder.title.setText(rowItem.getMessage());
        holder.name.setText(rowItem.getName());
        holder.date.setText(DateFormat.getDateInstance().format(rowItem.getDate()));
        if (isUnread(rowItem.getArticleId())) {
            holder.unreadMessages.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    private boolean isUnread(Integer articleId) {
        String stack = context.getSharedPreferences(UNREAD_MESSAGES, 0).getString(Constants.UNREAD_MESSAGES, "");
        String[] ids = stack.split(",");
        for (int i = 0; i < ids.length; i++) {
            if (articleId.toString().equals(ids[i])) {
                return true;
            }
        }
        return false;
    }

    private String getMainImageId(GroupedMsgItem rowItem) {
        String id = "";
        if (rowItem.getUrl() != null) {
            String[] ids = rowItem.getUrl().split(",");
            id = ids[0];
        } else {
            //TODO show default image
        }
        return id;
    }
}
