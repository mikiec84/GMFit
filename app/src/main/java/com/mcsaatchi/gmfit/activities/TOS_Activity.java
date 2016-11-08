package com.mcsaatchi.gmfit.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.mcsaatchi.gmfit.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TOS_Activity extends Base_Activity {
  @Bind(R.id.termsOfUseTV) TextView termsOfUseTV;
  @Bind(R.id.toolbar) Toolbar toolbar;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_terms_of_use);

    ButterKnife.bind(this);

    termsOfUseTV.setMovementMethod(LinkMovementMethod.getInstance());

    setupToolbar(toolbar, R.string.tos_activity_title, true);
    addTopPaddingToolbar(toolbar);
  }
}
