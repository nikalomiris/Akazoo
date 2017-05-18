package journey.forjobs.akazoo_project.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import journey.forjobs.akazoo_project.R;
import journey.forjobs.akazoo_project.database.DBTableHelper;
import journey.forjobs.akazoo_project.database.TracksContentProvider;
import journey.forjobs.akazoo_project.listadapters.TracksListAdapter;
import journey.forjobs.akazoo_project.model.Track;

public class TracksFragment extends Fragment {

  @InjectView(R.id.tracks_list)
  ListView mTracksList;
  @InjectView(R.id.playlist_title_tv)
  TextView mPlaylistTitle;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    View v = inflater.inflate(R.layout.fragment_tracks, container, false);
    ButterKnife.inject(this, v);
    return v;

  }

  public void updateTracksList(String playlistTitle) {
    ArrayList<Track> allTracks = new ArrayList<>();
    Cursor mCursor;

    //used to know which columns name we need to retrieve
    String[] mProjection = {
        DBTableHelper.COLUMN_TRACK_NAME,
        DBTableHelper.COLUMN_TRACK_ARTIST,
        DBTableHelper.COLUMN_TRACK_IMAGE_URL
    };

    //queries the database
    mCursor = getActivity().getContentResolver().query(
        TracksContentProvider.CONTENT_URI,
        // The content URI of the words table - You need to use TracksContentProvider.CONTENT_URI to test yours
        mProjection,                       // The columns to return for each row
        null,
        null,
        null);

    if (mCursor != null) {
      while (mCursor.moveToNext()) {
        //create empty object
        Track track = new Track();

        String trackName = mCursor
            .getString(mCursor.getColumnIndex(DBTableHelper.COLUMN_TRACK_NAME));
        String trackArtist = mCursor
            .getString(mCursor.getColumnIndex(DBTableHelper.COLUMN_TRACK_ARTIST));
        String trackImageUrl = mCursor
            .getString(mCursor.getColumnIndex(DBTableHelper.COLUMN_TRACK_IMAGE_URL));

        track.setTrackName(trackName);
        track.setArtistName(trackArtist);
        track.setImageUrl(trackImageUrl);

        Log.d("MA TAG", "the track name is " + track.getTrackName());
        Log.d("MA TAG", "the track artist name is  " + track.getArtistName());

        allTracks.add(track);
      }

      final TracksListAdapter mTracksListAdapter = new TracksListAdapter(getActivity(), allTracks);
      mTracksList.setAdapter(mTracksListAdapter);
      mPlaylistTitle.setText(playlistTitle);

      mTracksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          showSnackbar("(Fragment) You have clicked on: " + mTracksListAdapter.getTracks().
              get(position).getTrackName());
        }

      });

      mCursor.close();
    }
  }

  protected void showSnackbar(String message) {
    Snackbar mySnackbar = Snackbar.make(getActivity().findViewById(R.id.root),
        message, Snackbar.LENGTH_LONG);
    mySnackbar.show();
  }

}

