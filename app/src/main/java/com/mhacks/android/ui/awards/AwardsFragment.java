package com.mhacks.android.ui.awards;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.mhacks.android.ui.MainActivity;
import com.mhacks.android.ui.common.parse.ParseAdapter;
import com.mhacks.android.ui.common.parse.ViewHolder;
import com.mhacks.iv.android.R;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 7/24/14.
 */
public class AwardsFragment extends Fragment implements ParseAdapter.ListCallbacks<Award> {
  public static final String TAG = "AwardsFragment";

  private ListView mListView;
  private SwipeRefreshLayout mLayout;
  private ParseAdapter<Award> mAdapter;

  public AwardsFragment() {
    super();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ParseQueryAdapter.QueryFactory<Award> factory = new ParseQueryAdapter.QueryFactory<Award>() {
      @Override
      public ParseQuery<Award> create() {
        return Award.query().orderByDescending(Award.VALUE);
      }
    };
    mAdapter = new ParseAdapter<>(getActivity(), R.layout.adapter_award, this, factory)
      .setSectioning(Award.equivalentOn(Award.SPONSOR))
      .load();

    setHasOptionsMenu(true);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_award, container, false);
    mLayout = (SwipeRefreshLayout) view.findViewById(R.id.awards_swipe_container);

    mListView = (ListView) view.findViewById(R.id.awards_list);
    mListView.setAdapter(mAdapter);

    mAdapter.bindSync(mLayout);
    if (getArguments().getBoolean(MainActivity.SHOULD_SYNC, false)) mAdapter.onRefresh();

    return view;
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    if (User.getCurrentUser().isAdmin()) {
      inflater.inflate(R.menu.fragment_awards, menu);
    }
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public void populateView(ViewHolder holder, Award award, boolean hasSectionHeader, boolean hasSectionFooter) {
    View header = holder.get(R.id.award_card_header);
    View footer = holder.get(R.id.award_card_footer);
    TextView title = holder.get(R.id.award_title);
    TextView details = holder.get(R.id.award_details);
    TextView sponsor = holder.get(R.id.award_sponsor);
    TextView prize = holder.get(R.id.award_prize);

    header.setVisibility(hasSectionHeader ? View.VISIBLE : View.GONE);
    footer.setVisibility(hasSectionFooter ? View.VISIBLE : View.GONE);

    title.setText(award.getTitle());
    details.setText(award.getDetails());
    sponsor.setText(award.getSponsor().getTitle());
    prize.setText(award.getPrize());
  }

}
