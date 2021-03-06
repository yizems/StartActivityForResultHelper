package cn.yzl.startactivityforesult.library;

import android.app.Activity;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import android.util.SparseArray;

public class StartActivityForResultHelperFragment extends Fragment {
    private final SparseArray<ActivityHelper.Callback> callbacks = new SparseArray();

    public final void startActivityForResult(Intent intent, int requestCode, ActivityHelper.Callback callback) {
        if (this.getActivity() != null) {
            this.callbacks.put(requestCode, callback);
            this.startActivityForResult(intent, requestCode);
        } else {
            throw new IllegalStateException("This fragment must be attached to an activity.");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!this.isDetached()) {
            ActivityHelper.Callback callback = this.callbacks.get(requestCode);
            if (callback != null) {
                ActivityResult activityResult = new ActivityResult(requestCode, resultCode, data);
                if (callback.isSimple()) {
                    if (resultCode == Activity.RESULT_OK) {
                        callback.onResult(activityResult);
                    }
                    return;
                }
                callback.onResult(activityResult);
            }
        }
    }

    public void onDetach() {
        super.onDetach();
        this.callbacks.clear();
    }
}
