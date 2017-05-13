package journey.forjobs.akazoo_project.activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import butterknife.ButterKnife;
import journey.forjobs.akazoo_project.R;
import journey.forjobs.akazoo_project.fragments.TracksFragment;
import journey.forjobs.akazoo_project.utils.Const;

public class TracksActivity extends AkazooActivity {

    private MyMessageReceiver mMessageReceiver = new MyMessageReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
            if (intent.getAction().equals(Const.SERVICE_BIND)) {
                getAkazooController().getTracks("768f797f-ce35-4515-b6d8-56dcf91b8253");
            } else if (message.equals(Const.REST_TRACKS_SUCCESS)) {
                mTracksFragment.updateTracksList();
            }
        }
    };

    @Override
    protected MyMessageReceiver getmMessageReceiver() {
        return mMessageReceiver;
    }

    TracksFragment mTracksFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracks);
        ButterKnife.inject(this);

        LocalBroadcastManager.getInstance(this).registerReceiver(getmMessageReceiver(),
                new IntentFilter(Const.SERVICE_BIND));

        if (savedInstanceState == null) {
            mTracksFragment = new TracksFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.root, mTracksFragment).commit();
        }

        //TODO Receive intent and get playlist id
    }
}
