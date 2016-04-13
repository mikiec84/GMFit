package com.mcsaatchi.gmfit.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.andreabaccega.widget.FormEditText;
import com.mcsaatchi.gmfit.R;
import com.mcsaatchi.gmfit.classes.Helpers;
import com.mcsaatchi.gmfit.logger.Log;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignIn_Activity extends Base_Activity {

    @Bind(R.id.emailET)
    FormEditText emailET;
    @Bind(R.id.passwordET)
    FormEditText passwordET;
    @Bind(R.id.signInBTN)
    Button signInBTN;
    @Bind(R.id.forgotPasswordTV)
    TextView forgotPasswordTV;

    private ArrayList<FormEditText> allFields = new ArrayList<>();
    private Helpers helpers = Helpers.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle bundle = new Bundle();
        bundle.putInt("activity_title", R.string.sign_in_activity_title);

        super.onCreate(bundle);

        setContentView(R.layout.signin_activity);

        ButterKnife.bind(this);

        passwordET.setTypeface(Typeface.DEFAULT);

        allFields.add(emailET);
        allFields.add(passwordET);

        signInBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helpers.validateFields(allFields)) {
                    Log.toaster(SignIn_Activity.this, "All fields check out!");
                }
            }
        });

        SpannableString ss = new SpannableString(getString(R.string.forgot_password_button));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(SignIn_Activity.this, ForgotPassword_Activity.class));
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };

        ss.setSpan(clickableSpan, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        forgotPasswordTV.setText(ss);
        forgotPasswordTV.setMovementMethod(LinkMovementMethod.getInstance());
        forgotPasswordTV.setHighlightColor(Color.TRANSPARENT);
    }
}
