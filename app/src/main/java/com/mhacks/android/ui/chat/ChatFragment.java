package com.mhacks.android.ui.chat;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.mhacks.android.R;
import com.mhacks.android.data.firebase.ChatMessage;
import com.mhacks.android.data.firebase.ChatRoom;
import com.mhacks.android.data.model.User;
import com.mhacks.android.ui.common.CircleTransform;
import com.mhacks.android.ui.common.FirebaseListAdapter;
import com.squareup.picasso.Picasso;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 7/24/14.
 */
public class ChatFragment extends Fragment implements ActionBar.OnNavigationListener {
  public static final String TAG = "ChatFragment";

  public static final String CHAT = "chat";
  public static final String ROOMS = "rooms";
  public static final String MESSAGES = "messages";

  private String mFirebaseUrl;
  private Firebase mFirebase;
  private RelativeLayout mLayout;
  private ListView mListView;
  private RoomsAdapter mRoomsAdapter;
  private ChatAdapter mChatAdapter;

  private int mPriorNavigationMode;

  public ChatFragment() {
    super();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);

    mFirebaseUrl = getString(R.string.firebase_url);
    mFirebase = new Firebase(mFirebaseUrl).child(CHAT);
    mRoomsAdapter = new RoomsAdapter(mFirebase.child(ROOMS).limit(50), getActivity());
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    mLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_chat, null);
    mListView = (ListView) mLayout.findViewById(R.id.chat_list);

    return mLayout;
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    setNavigationMode();
    if (User.canAdmin()) {
      inflater.inflate(R.menu.fragment_chat, menu);
    }
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.chat_new:
        // TODO: do stuff
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void setNavigationMode() {
    ActionBar actionBar = getActivity().getActionBar();
    mPriorNavigationMode = actionBar.getNavigationMode();
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
    actionBar.setListNavigationCallbacks(mRoomsAdapter, this);
    actionBar.setDisplayShowTitleEnabled(false);
  }

  private void revertNavigationMode() {
    ActionBar actionBar = getActivity().getActionBar();
    actionBar.setNavigationMode(mPriorNavigationMode);
    actionBar.setDisplayShowTitleEnabled(true);
  }

  @Override
  public void onDestroyView() {
    revertNavigationMode();
    super.onDestroyView();
  }

  @Override
  public boolean onNavigationItemSelected(int i, long l) {
    mChatAdapter = new ChatAdapter(mFirebase.child(MESSAGES).child(mRoomsAdapter.getItem(i).getTitle()).limit(50), getActivity());
    mListView.setAdapter(mChatAdapter);
    return true;
  }

  private static class RoomsAdapter extends FirebaseListAdapter<ChatRoom> {

    public RoomsAdapter(Query ref, Activity activity) {
      super(ref, ChatRoom.class, android.R.layout.simple_spinner_dropdown_item, activity);
    }

    @Override
    protected void populateView(ViewHolder holder, ChatRoom chatRoom) {
      TextView title = holder.get(android.R.id.text1);
      title.setText(chatRoom.getTitle());
    }

  }

  private static class ChatAdapter extends FirebaseListAdapter<ChatMessage> {
    private final Activity mmActivity;

    public ChatAdapter(Query ref, Activity activity) {
      super(ref, ChatMessage.class, R.layout.adapter_chat_message, activity);
      mmActivity = activity;
    }

    @Override
    protected void populateView(ViewHolder holder, ChatMessage message) {
      ImageView image = holder.get(R.id.chat_message_image);
      TextView name = holder.get(R.id.chat_message_user_name);
      TextView text = holder.get(R.id.chat_message_text);

      Picasso.with(mmActivity)
        .load(message.getImage())
        .transform(new CircleTransform())
        .into(image);

      name.setText(message.getUser());
      text.setText(message.getMessage());
    }

  }

}
