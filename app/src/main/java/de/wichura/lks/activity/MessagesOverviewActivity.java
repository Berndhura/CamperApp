package de.wichura.lks.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import de.wichura.lks.R;
import de.wichura.lks.adapter.MsgOverviewAdapter;
import de.wichura.lks.http.Service;
import de.wichura.lks.http.Urls;
import de.wichura.lks.mainactivity.Constants;
import de.wichura.lks.models.GroupedMsgItem;
import de.wichura.lks.presentation.MsgOverviewPresenter;

import static de.wichura.lks.mainactivity.Constants.SHARED_PREFS_USER_INFO;

/**
 * Created by Bernd Wichura on 20.06.2016.
 * Luftkraftsport
 */
public class MessagesOverviewActivity extends AppCompatActivity {

    private ListView listView;

    private static final String LIST_STATE = "listState";
    private Parcelable mListState = null;

    private AVLoadingIndicatorView mMessagesProgressBar;

    private MsgOverviewPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Service service = new Service();
        presenter = new MsgOverviewPresenter(this, service);

        setContentView(R.layout.message_overview_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.msg_overview_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener((view) -> finish());
        }

        mMessagesProgressBar = (AVLoadingIndicatorView) findViewById(R.id.msg_overview_ProgressBar);

        listView = (ListView) findViewById(R.id.message_overview_list);

        //getMessagesForUser(getUserId(), mMessagesProgressBar);
        presenter.loadAllMessages(getUserToken());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mListState != null) listView.onRestoreInstanceState(mListState);
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        mListState = listView.onSaveInstanceState();
        state.putParcelable(LIST_STATE, mListState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        mListState = state.getParcelable(LIST_STATE);
    }

    public void updateMsgList(List<GroupedMsgItem> messageList) {
        List<GroupedMsgItem> rowItems = new ArrayList<>();

        for (GroupedMsgItem e : messageList) {
            rowItems.add(e);
        }

        getSupportActionBar().setTitle("Messages: " + rowItems.size());
        getSupportActionBar().setSubtitle(getUserName());


        MsgOverviewAdapter adapter = new MsgOverviewAdapter(
                getApplicationContext(), R.layout.msg_overview_item, rowItems);
        listView.setAdapter(adapter);
        listView.setSelection(listView.getCount() - 1);
        adapter.notifyDataSetChanged();
        listView.setSelectionAfterHeaderView();
        listView.setOnItemClickListener((adapterView, view, position, l) -> {
            final GroupedMsgItem rowItem = (GroupedMsgItem) listView.getItemAtPosition(position);
            //open message threat
            final Intent intent = new Intent(getApplicationContext(), MessagesActivity.class);
            intent.putExtra(Constants.ARTICLE_ID, rowItem.getArticleId());
            intent.putExtra(Constants.SENDER_ID, rowItem.getIdTo());
            intent.putExtra(Constants.ID_FROM, rowItem.getIdFrom());
            intent.putExtra(Constants.CHAT_PARTNER, rowItem.getChatPartner());
            intent.putExtra(Constants.ID_TO, rowItem.getIdTo());
            intent.putExtra(Constants.SENDER_NAME, rowItem.getName());
            //TODO weg nutzlos?
            //intent.putExtra(Constants.AD_URL, Urls.MAIN_SERVER_URL_V3 + "pictures/" + rowItem.getUrl() + "/thumbnail");
            startActivityForResult(intent, Constants.REQUEST_ID_FOR_MESSAGES);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_ID_FOR_MESSAGES) {
            presenter.loadAllMessages(getUserToken());
        }
    }

    private String getUserToken() {
        return getSharedPreferences(SHARED_PREFS_USER_INFO, 0).getString(Constants.USER_TOKEN, "");
    }

    private String getUserName() {
        return getSharedPreferences(SHARED_PREFS_USER_INFO, 0).getString(Constants.USER_NAME, "");
    }

    public void hideProgressBar() {
        mMessagesProgressBar.setVisibility(ProgressBar.GONE);
    }
}
