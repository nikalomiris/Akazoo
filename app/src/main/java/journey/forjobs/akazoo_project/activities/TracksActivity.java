package journey.forjobs.akazoo_project.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import butterknife.ButterKnife;
import journey.forjobs.akazoo_project.R;
import journey.forjobs.akazoo_project.fragments.TracksFragment;
import journey.forjobs.akazoo_project.model.Playlist;
import journey.forjobs.akazoo_project.utils.Const;

public class TracksActivity extends AkazooActivity {

  TracksFragment mTracksFragment;
  String playlistId;
  Playlist playlistObject;

  private MyMessageReceiver mMessageReceiver = new MyMessageReceiver() {

    /**
     * Broadcast receiver implementation that performs additional actions specifically useful
     * to the tracks activity.
     *
     * When REST_TRACKS_SUCCESS message is received, the tracks list is ready to be displayed
     * in the list view of the fragment.
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
      super.onReceive(context, intent);
      if (message.equals(Const.REST_TRACKS_SUCCESS)) {
        mTracksFragment.updateTracksList(playlistObject.getPhotoUrl(), playlistObject.getName(),
            playlistObject.getItemCount());
      }
    }
  };

  @Override
  protected MyMessageReceiver getmMessageReceiver() {
    return mMessageReceiver;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tracks);
    ButterKnife.inject(this);

    if (savedInstanceState == null) {
      mTracksFragment = new TracksFragment();
      getSupportFragmentManager().beginTransaction().add(R.id.root, mTracksFragment).commit();
    }

    /**
     * Extract playlist id from the received intent and use it to get the tracks list of that
     * playlist.
     *
     * Also extract the Playlist that was clicked and display some of its attributes at the top
     * of the tracks list.
     */
    playlistId = getIntent().getStringExtra("playlistId");
    playlistObject = (Playlist) getIntent().getSerializableExtra("playlistObject");
    getAkazooController().getTracks(playlistId);
  }
}
