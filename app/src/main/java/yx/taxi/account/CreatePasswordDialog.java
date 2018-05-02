package yx.taxi.account;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;

/**
 * Created by yangxiong on 2018/5/2/002.
 */

public class CreatePasswordDialog extends Dialog{
    public CreatePasswordDialog(@NonNull Context context) {
        super(context);
    }

    public CreatePasswordDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected CreatePasswordDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public CreatePasswordDialog(Context context, String mPhone) {
        super(context);
    }
}
