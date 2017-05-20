package journey.forjobs.akazoo_project.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import journey.forjobs.akazoo_project.R;
import journey.forjobs.akazoo_project.application.AkazooApplication;
import journey.forjobs.akazoo_project.controllers.AkazooController;
import journey.forjobs.akazoo_project.utils.Const;


public abstract class AkazooActivity extends AppCompatActivity {

  public class MyMessageReceiver extends BroadcastReceiver {

    protected String message;

    /**
     * Broadcast receiver implementation that performs actions that any child activity should
     * when receiving certain messages.
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
      if (intent.getAction().equals(Const.CONTROLLER_SUCCESSFULL_CALLBACK)) {
        message = intent.getStringExtra(Const.CONTROLLER_SUCCESSFULL_CALLBACK_MESSAGE);
        if (message.equals(Const.SHOW_SPINNER)) {
          showProgressDialog(getString(R.string.progress_dialog_message));
        } else {
          hideProgressDialog();
        }
      } else if (intent.getAction().equals(Const.CONTROLLER_FAILURE_CALLBACK)) {
        message = intent.getStringExtra(Const.CONTROLLER_FAILURE_CALLBACK_MESSAGE);
        hideProgressDialog();
        showErrorDialog(message);
      }
    }
  }

  protected abstract MyMessageReceiver getmMessageReceiver();

  /**
   * Error dialog that appears when CONTROLLER_FAILURE_CALLBACK_MESSAGE is received
   * @param message String variable with the message that should be displayed in the dialog
   */
  protected void showErrorDialog(String message) {
    final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
    alertDialog.setMessage(message);
    alertDialog.setTitle(getString(R.string.popup_error_title));
    alertDialog.setIcon(R.drawable.ic_error_outline_red_400_24dp);
    alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.popup_neutral_button),
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            alertDialog.dismiss();
          }
        });
    alertDialog.show();
  }

  ProgressDialog dialog;

  /**
   * Progress dialog that appears while loading data from the server.
   * @param message
   */
  protected void showProgressDialog(String message) {
    dialog = new ProgressDialog(this);
    dialog.setMessage(message);
    dialog.show();
  }

  /**
   * Hides the progress dialog when loading data is finished.
   */
  protected void hideProgressDialog() {
    if (dialog != null) {
      dialog.dismiss();
    }
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    /**
     * Create broadcast receiver for the CONTROLLER_SUCCESSFULL_CALLBACK messages.
     */
    LocalBroadcastManager.getInstance(this).registerReceiver(getmMessageReceiver(),
        new IntentFilter(Const.CONTROLLER_SUCCESSFULL_CALLBACK));
  }

  @Override
  protected void onResume() {
    super.onResume();

    /**
     * Create broadcast receiver for the CONTROLLER_FAILURE_CALLBACK messages.
     */
    LocalBroadcastManager.getInstance(this).registerReceiver(getmMessageReceiver(),
        new IntentFilter(Const.CONTROLLER_FAILURE_CALLBACK));
  }

  @Override
  protected void onPause() {
    super.onPause();

    /**
     * Stops activity from listening for broadcast messages when not active.
     */
    LocalBroadcastManager.getInstance(this).unregisterReceiver(getmMessageReceiver());
  }

  /**
   * Method used by child activities to get access to the controller.
   * @return Akazoo controller.
   */
  protected AkazooController getAkazooController() {
    AkazooController mController = AkazooApplication.getInstance().getmController();
    return mController;
  }
}
