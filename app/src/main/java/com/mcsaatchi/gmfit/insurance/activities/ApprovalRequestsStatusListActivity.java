package com.mcsaatchi.gmfit.insurance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.mcsaatchi.gmfit.R;
import com.mcsaatchi.gmfit.common.activities.BaseActivity;
import com.mcsaatchi.gmfit.insurance.adapters.StatusAdapter;
import com.mcsaatchi.gmfit.insurance.models.MedicalInformationModel;
import com.mcsaatchi.gmfit.insurance.models.ReimbursementModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ApprovalRequestsStatusListActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    StatusAdapter statusAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_requests_status_list);
        ButterKnife.bind(this);
        setupToolbar(toolbar, "Approval Requests Status", true);

        List<MedicalInformationModel> medicines = new ArrayList<>();
        medicines.add(new MedicalInformationModel("Panadol Extra Tab 500mg", "Approved", "2 tablets",
                "3 times daily", "15 days"));
        medicines.add(new MedicalInformationModel("Panadol Extra Tab 500mg", "Approved", "2 tablets",
                "3 times daily", "15 days"));
        medicines.add(new MedicalInformationModel("Panadol Extra Tab 500mg", "Approved", "2 tablets",
                "3 times daily", "15 days"));
        medicines.add(new MedicalInformationModel("Panadol Extra Tab 500mg", "Approved", "2 tablets",
                "3 times daily", "15 days"));
        medicines.add(new MedicalInformationModel("Panadol Extra Tab 500mg", "Approved", "2 tablets",
                "3 times daily", "15 days"));

        List<ReimbursementModel> mock = new ArrayList<>();
        mock.add(
                new ReimbursementModel("232323", "OUT", "Dental", "17 Aug 2016", "LBP 550,000", "Rejected",
                        "Reimbursement", medicines));
        mock.add(
                new ReimbursementModel("232323", "OUT", "Dental", "17 Aug 2016", "LBP 550,000", "Rejected",
                        "Reimbursement", medicines));
        mock.add(
                new ReimbursementModel("232323", "OUT", "Dental", "17 Aug 2016", "LBP 550,000", "Rejected",
                        "Reimbursement", medicines));
        mock.add(
                new ReimbursementModel("232323", "OUT", "Dental", "17 Aug 2016", "LBP 550,000", "Rejected",
                        "Reimbursement", medicines));

        statusAdapter = new StatusAdapter(mock, new StatusAdapter.OnClickListener() {
            @Override
            public void onClick(ReimbursementModel reimbursementModel, int index) {
                Intent intent =
                        new Intent(ApprovalRequestsStatusListActivity.this, ApprovalRequestStatusActivity.class);
                intent.putExtra(ApprovalRequestStatusActivity.REIMBURSEMENT_MODEL_KEY, reimbursementModel);
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(statusAdapter);
    }
}