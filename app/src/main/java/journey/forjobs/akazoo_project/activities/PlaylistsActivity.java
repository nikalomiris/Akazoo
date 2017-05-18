package journey.forjobs.akazoo_project.activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import butterknife.ButterKnife;
import journey.forjobs.akazoo_project.R;
import journey.forjobs.akazoo_project.fragments.PlaylistsFragment;
import journey.forjobs.akazoo_project.utils.Const;

public class PlaylistsActivity extends AkazooActivity {

  private MyMessageReceiver mMessageReceiver = new MyMessageReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      super.onReceive(context, intent);
      if (intent.getAction().equals(Const.SERVICE_BIND)) {
        getAkazooController().getPlaylists();
      } else if (message.equals(Const.REST_PLAYLISTS_SUCCESS)) {
        mPlaylistsFragment.updatePlaylistList();
      }
    }
  };

  @Override
  protected MyMessageReceiver getmMessageReceiver() {
    return mMessageReceiver;
  }

  PlaylistsFragment mPlaylistsFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_playlists);
    ButterKnife.inject(this);

    LocalBroadcastManager.getInstance(this).registerReceiver(getmMessageReceiver(),
        new IntentFilter(Const.SERVICE_BIND));

    if (savedInstanceState == null) {
      mPlaylistsFragment = new PlaylistsFragment();
      getSupportFragmentManager().beginTransaction().add(R.id.root, mPlaylistsFragment).commit();
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    mPlaylistsFragment.updatePlaylistList();
  }
}
