package journey.forjobs.akazoo_project.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import java.util.ArrayList;
import journey.forjobs.akazoo_project.R;
import journey.forjobs.akazoo_project.activities.TracksActivity;
import journey.forjobs.akazoo_project.application.AkazooApplication;
import journey.forjobs.akazoo_project.database.DBTableHelper;
import journey.forjobs.akazoo_project.database.PlaylistContentProvider;
import journey.forjobs.akazoo_project.listadapters.PlaylistsListAdapter;
import journey.forjobs.akazoo_project.model.Playlist;


public class PlaylistsFragment extends Fragment {

  CheckIfDbIsEmpty mCallback;
  Cursor mCursor;

  // Container Activity must implement this interface
  public interface CheckIfDbIsEmpty {
    void onDbChecked(Cursor mCursor);
  }

  @InjectView(R.id.playlist_list)
  ListView mPlaylistsList;
  @InjectView(R.id.swipe_to_refresh)
  SwipeRefreshLayout swipeRefreshLayout;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    View v = inflater.inflate(R.layout.fragment_playlists, container, false);
    ButterKnife.inject(this, v);
    swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
      @Override
      public void onRefresh() {
        AkazooApplication.getInstance().getmController().getPlaylists();
      }
    });
    return v;

  }

  public void updatePlaylistList() {
    ArrayList<Playlist> allPlaylists = new ArrayList<>();

    //used to know which columns name we need to retrieve
    String[] mProjection = {
        DBTableHelper.COLUMN_PLAYLISTS_NAME,
        DBTableHelper.COLUMN_PLAYLISTS_TRACK_COUNT,
        DBTableHelper.COLUMN_PLAYLISTS_IMAGE_URL,
        DBTableHelper.COLUMN_PLAYLISTS_PLAYLIST_ID
    };

    //queries the database
    mCursor = getActivity().getContentResolver().query(
        PlaylistContentProvider.CONTENT_URI,
        mProjection,
        null,
        null,
        null);

    if (mCursor != null) {
      while (mCursor.moveToNext()) {
        //create empty object
        Playlist playlist = new Playlist();

        String playlistName = mCursor
            .getString(mCursor.getColumnIndex(DBTableHelper.COLUMN_PLAYLISTS_NAME));
        int numberOfTracks = mCursor
            .getInt(mCursor.getColumnIndex(DBTableHelper.COLUMN_PLAYLISTS_TRACK_COUNT));
        String imageURL = mCursor
            .getString(mCursor.getColumnIndex(DBTableHelper.COLUMN_PLAYLISTS_IMAGE_URL));
        String playlistId = mCursor
            .getString(mCursor.getColumnIndex(DBTableHelper.COLUMN_PLAYLISTS_PLAYLIST_ID));

        playlist.setName(playlistName);
        playlist.setItemCount(numberOfTracks);
        playlist.setPhotoUrl(imageURL);
        playlist.setPlaylistId(playlistId);

        Log.d("MA TAG", "the track name is " + playlist.getName());
        Log.d("MA TAG", "the track artist name is  " + Integer.toString(playlist.getItemCount()));

        allPlaylists.add(playlist);
      }

      final PlaylistsListAdapter mPlaylistsListAdapter = new PlaylistsListAdapter(getActivity(),
          allPlaylists);
      mPlaylistsList.setAdapter(mPlaylistsListAdapter);

      mPlaylistsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

        @TargetApi(VERSION_CODES.LOLLIPOP)
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          Intent intent = new Intent(getActivity(), TracksActivity.class);
          intent.putExtra("playlistId", mPlaylistsListAdapter.getPlaylists().get(position)
              .getPlaylistId());
          intent.putExtra("playlistObject", mPlaylistsListAdapter.getPlaylists().get(position));

          String transitionName = getString(R.string.transition_playlist_name);

          ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, transitionName);
          startActivity(intent, transitionActivityOptions.toBundle());
        }

      });
    }
  }

  protected void showSnackbar(String message) {
    Snackbar mySnackbar = Snackbar.make(getActivity().findViewById(R.id.root),
        message, Snackbar.LENGTH_LONG);
    mySnackbar.show();
  }

  public void stopSwipeRefresh() {
    swipeRefreshLayout.setRefreshing(false);
  }

}

