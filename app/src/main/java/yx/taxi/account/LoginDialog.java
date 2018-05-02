package yx.taxi.account;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;

/**
 * Created by yangxiong on 2018/5/2/002.
 */

class LoginDialog extends Dialog {

    public LoginDialog(@NonNull Context context) {
        super(context);
    }

    public LoginDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected LoginDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    public LoginDialog(Context context, String mPhone) {
        super( context);

    }
}
