package yx.taxi.account;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yangxiong.mytaxi.R;

import yx.taxi.common.util.FormatUtil;

/**
 * Created by yangxiong on 2018/5/2/002.
 */

public class PhoneInputDialog extends Dialog {

    private Button btnNext;

    public PhoneInputDialog(@NonNull Context context) {
        super(context);
    }
    public PhoneInputDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected PhoneInputDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater( ).inflate(R.layout.dialog_phone_input, null);
        setContentView(view);
        initView();
    }

    private void initView() {
        final EditText etPhone = (EditText) findViewById(R.id.phone);
        etPhone.addTextChangedListener(new TextWatcher( ) {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkInput(etPhone.getText().toString());
            }
        });

        btnNext = (Button) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                dismiss();
                SmsCodeDialog smsCodeDialog = new SmsCodeDialog(PhoneInputDialog.this.getContext(),etPhone.getText().toString());
                smsCodeDialog.show();
            }
        });
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneInputDialog.this.dismiss();
            }
        });
    }

    private void checkInput(String phone) {
        boolean b = FormatUtil.checkMobile(phone);
        btnNext.setEnabled(b);
    }
}
