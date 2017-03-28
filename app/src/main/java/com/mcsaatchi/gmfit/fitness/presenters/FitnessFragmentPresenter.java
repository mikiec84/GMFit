package com.mcsaatchi.gmfit.fitness.presenters;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.mcsaatchi.gmfit.R;
import com.mcsaatchi.gmfit.architecture.data_access.DataAccessHandler;
import com.mcsaatchi.gmfit.architecture.rest.ChartMetricBreakdownResponse;
import com.mcsaatchi.gmfit.architecture.rest.ChartMetricBreakdownResponseDatum;
import com.mcsaatchi.gmfit.architecture.rest.DefaultGetResponse;
import com.mcsaatchi.gmfit.architecture.rest.SlugBreakdownResponse;
import com.mcsaatchi.gmfit.architecture.rest.SlugBreakdownResponseInnerData;
import com.mcsaatchi.gmfit.architecture.rest.UserGoalMetricsResponse;
import com.mcsaatchi.gmfit.architecture.rest.WidgetsResponse;
import com.mcsaatchi.gmfit.architecture.rest.WidgetsResponseDatum;
import com.mcsaatchi.gmfit.common.models.DataChart;
import com.mcsaatchi.gmfit.common.presenters.BaseFragmentPresenter;
import com.mcsaatchi.gmfit.fitness.models.FitnessWidget;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class FitnessFragmentPresenter extends BaseFragmentPresenter {
  private FitnessFragmentView view;
  private RuntimeExceptionDao<FitnessWidget, Integer> fitnessWidgetsDAO;
  private DataAccessHandler dataAccessHandler;

  public FitnessFragmentPresenter(FitnessFragmentView view,
      RuntimeExceptionDao<FitnessWidget, Integer> fitnessWidgetsDAO,
      DataAccessHandler dataAccessHandler) {
    this.view = view;
    this.fitnessWidgetsDAO = fitnessWidgetsDAO;
    this.dataAccessHandler = dataAccessHandler;
  }

  public void getUserGoalMetrics(String date, String type, final boolean requestingPreviousData) {
    view.showMetricsLoadingBar();

    dataAccessHandler.getUserGoalMetrics(date, type, new Callback<UserGoalMetricsResponse>() {
      @Override public void onResponse(Call<UserGoalMetricsResponse> call,
          Response<UserGoalMetricsResponse> response) {

        switch (response.code()) {
          case 200:
            String maxValue =
                response.body().getData().getBody().getMetrics().getStepsCount().getMaxValue();

            String currentValue =
                response.body().getData().getBody().getMetrics().getStepsCount().getValue();

            view.displayUserGoalMetrics(maxValue, currentValue, requestingPreviousData);

            break;
        }
      }

      @Override public void onFailure(Call<UserGoalMetricsResponse> call, Throwable t) {
      }
    });
  }

  public void getPeriodicalChartData(final DataChart chartObject, String desiredDate,
      String fromDate) {
    dataAccessHandler.getPeriodicalChartData(fromDate, desiredDate, "fitness",
        chartObject.getType(), new Callback<ChartMetricBreakdownResponse>() {
          @Override public void onResponse(Call<ChartMetricBreakdownResponse> call,
              Response<ChartMetricBreakdownResponse> response) {
            if (response.code() == 200) {
              List<ChartMetricBreakdownResponseDatum> rawChartData =
                  response.body().getData().getBody().getData();

              view.getBreakDownDataForChart(rawChartData, chartObject);
            }
          }

          @Override public void onFailure(Call<ChartMetricBreakdownResponse> call, Throwable t) {
          }
        });
  }

  public void getBreakdownDataForChart(DataChart chartObject) {
    view.callDisplayWaitingDialog(R.string.grabbing_breakdown_data_dialog_title);

    dataAccessHandler.getSlugBreakdownForChart(chartObject.getType(),
        new Callback<SlugBreakdownResponse>() {
          @Override public void onResponse(Call<SlugBreakdownResponse> call,
              Response<SlugBreakdownResponse> response) {
            switch (response.code()) {
              case 200:
                view.openSlugBreakDownActivity(response.body().getData().getBody().getData(),
                    chartObject);
                break;
            }

            view.callDismissWaitingDialog();
          }

          @Override public void onFailure(Call<SlugBreakdownResponse> call, Throwable t) {
          }
        });
  }

  public void getWidgetsWithDate(String finalDate) {
    dataAccessHandler.getWidgetsWithDate("fitness", finalDate, new Callback<WidgetsResponse>() {
      @Override
      public void onResponse(Call<WidgetsResponse> call, Response<WidgetsResponse> response) {
        switch (response.code()) {
          case 200:
            List<WidgetsResponseDatum> widgetsFromResponse =
                response.body().getData().getBody().getData();

            view.callSetupWidgetViews(widgetsFromResponse);

            break;
        }
      }

      @Override public void onFailure(Call<WidgetsResponse> call, Throwable t) {
      }
    });
  }

  public void deleteUserChart(DataChart chartObject, String todayDate) {
    view.callDisplayWaitingDialog(R.string.deleting_chart_dialog_title);

    dataAccessHandler.deleteUserChart(String.valueOf(chartObject.getChart_id()),
        new Callback<DefaultGetResponse>() {
          @Override public void onResponse(Call<DefaultGetResponse> call,
              Response<DefaultGetResponse> response) {
            switch (response.code()) {
              case 200:
                view.callSetupChartViews(chartObject, todayDate);
                break;
            }

            view.callDismissWaitingDialog();
          }

          @Override public void onFailure(Call<DefaultGetResponse> call, Throwable t) {
          }
        });
  }

  public void updateUserCharts(int[] chartIds, int[] chartPositions) {
    dataAccessHandler.updateUserCharts(chartIds, chartPositions,
        new Callback<DefaultGetResponse>() {
          @Override public void onResponse(Call<DefaultGetResponse> call,
              Response<DefaultGetResponse> response) {
            switch (response.code()) {
              case 200:
                Timber.d("onResponse: User's charts updated successfully");
                break;
            }
          }

          @Override public void onFailure(Call<DefaultGetResponse> call, Throwable t) {

          }
        });
  }

  public ArrayList<FitnessWidget> getWidgetsFromDB() {
    try {
      return (ArrayList<FitnessWidget>) fitnessWidgetsDAO.queryBuilder()
          .orderBy("position", true)
          .query();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;
  }

  public interface FitnessFragmentView extends BaseFragmentView {
    void showMetricsLoadingBar();

    void displayUserGoalMetrics(String maxValue, String currentValue,
        boolean requestingPreviousData);

    void getBreakDownDataForChart(List<ChartMetricBreakdownResponseDatum> rawChartData,
        DataChart chartObject);

    void openSlugBreakDownActivity(SlugBreakdownResponseInnerData breakDownData,
        DataChart chartObject);

    void callSetupWidgetViews(List<WidgetsResponseDatum> widgetsFromResponse);

    void callSetupChartViews(DataChart chartObject, String todayDate);
  }
}
