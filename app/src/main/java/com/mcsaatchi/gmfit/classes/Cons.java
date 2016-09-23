package com.mcsaatchi.gmfit.classes;

import okhttp3.MediaType;

public class Cons {
    public static final String SHARED_PREFS_TITLE = "GMFIT_PREFS";
    public static final String TAG = "GMFIT_DEBUG";

    public static final String EXTRAS_USER_LOGGED_IN = "user_logged_in";
    public static final String EXTRAS_USER_FULL_NAME = "user_full_name";
    public static final String EXTRAS_USER_EMAIL = "user_email_address";
    public static final String EXTRAS_USER_PASSWORD = "user_password";
    public static final String EXTRAS_USER_DISPLAY_PHOTO = "user_display_photo";
    public static final String EXTRAS_USER_POLICY = "user_policy";
    public static final String EXTRAS_USER_FACEBOOK_TOKEN = "user_facebook_token";

    public static final String EXTRAS_FIRST_APP_LAUNCH = "first_app_launch";

    public static final int WAIT_TIME_BEFORE_CHECKING_METRICS_SERVICE = 1200000;
    public static final int WAIT_TIME_BEFORE_SERVER_SYNC = 60000;

    public static final String EXTRAS_USER_PROFILE_MEASUREMENT_SYSTEM = "user_profile_measurement_system";
    public static final String EXTRAS_USER_PROFILE_NATIONALITY = "user_profile_nationality";
    public static final String EXTRAS_USER_PROFILE_GOAL = "user_profile_goal";
    public static final String EXTRAS_USER_PROFILE_DATE_OF_BIRTH = "user_profile_date_of_birth";
    public static final String EXTRAS_USER_PROFILE_WEIGHT = "user_profile_weight";
    public static final String EXTRAS_USER_PROFILE_HEIGHT = "user_profile_height";
    public static final String EXTRAS_USER_PROFILE_BLOOD_TYPE = "user_profile_blood_type";

    public static final String EXTRAS_FITNESS_FRAGMENT = "FITNESS";
    public static final String EXTRAS_NUTRITION_FRAGMENT = "NUTRITION";
    public static final String EXTRAS_HEALTH_FRAGMENT = "HEALTH";

    public static final String EVENTBUS_NUTRITION_ALREADY_REGISTERED = "eventbus_nutrition_already_registered";

    public static final String EXTRAS_ADD_CHART_WHAT_TYPE = "add_chart_what_type";
    public static final String EXTRAS_CHART_TYPE_SELECTED = "chart_type_selected";
    public static final String EXTRAS_CHART_FULL_NAME = "chart_full_name";
    public static final String EXTRAS_CHART_OBJECT = "chart_object";
    public static final String EXTRAS_MEAL_OBJECT_DETAILS = "meal_object_details";
    public static final String EXTRAS_MEAL_ITEM_PURPOSE_EDITING = "meal_item_purpose_editing";

    public static final String EXTRAS_FITNESS_WIDGETS_ORDER_ARRAY = "fitness_widgets_order_array";
    public static final String EXTRAS_FITNESS_CHART_DELETED = "fitness_chart_deleted";
    public static final String EXTRAS_FITNESS_WIDGETS_ORDER_ARRAY_CHANGED = "fitness_widgets_order_array_changed";
    public static final String EXTRAS_NUTRITION_WIDGETS_ORDER_ARRAY = "nutrition_widgets_order_array";
    public static final String EXTRAS_NUTRITION_WIDGETS_ORDER_ARRAY_CHANGED = "nutrition_widgets_order_array_changed";
    public static final String EXTRAS_HEALTH_WIDGETS_ORDER_ARRAY = "health_widgets_order_array";
    public static final String EXTRAS_HEALTH_WIDGETS_ORDER_ARRAY_CHANGED = "health_widgets_order_array_changed";
    public static final String EXTRAS_FITNESS_CHARTS_ORDER_ARRAY_CHANGED = "fitness_charts_order_array_changed";
    public static final String EXTRAS_NUTRITION_CHARTS_ORDER_ARRAY_CHANGED = "nutrition_charts_order_array_changed";
    public static final String EXTRAS_HEALTH_CHARTS_ORDER_ARRAY_CHANGED = "health_charts_order_array_changed";
    public static final String EXTRAS_CUSTOMIZE_WIDGETS_CHARTS_FRAGMENT_TYPE = "customize_widgets_charts_fragment_type";
    public static final String EXTRAS_ALL_DATA_CHARTS = "all_data_charts";
    public static final String EXTRAS_ADD_FITNESS_CHART = "add_fitness_chart";
    public static final String EXTRAS_ADD_NUTRIITION_CHART = "add_nutrition_chart";
    public static final String EXTRAS_MAIN_MEAL_NAME = "main_meal_name";
    public static final String EXTRAS_PICKED_MEAL_ENTRY = "picked_meal_entry";

    public static final String BUNDLE_ACTIVITY_TITLE = "activity_title";
    public static final String BUNDLE_ACTIVITY_BACK_BUTTON_ENABLED = "activity_back_button_enabled";
    public static final String BUNDLE_FITNESS_WIDGETS_MAP = "fitness_widgets_map";
    public static final String BUNDLE_NUTRITION_WIDGETS_MAP = "nutrition_widgets_map";

    public static final String BUNDLE_SLUG_BREAKDOWN_DATA = "slug_breakdown_data";
    public static final String BUNDLE_SLUG_BREAKDOWN_DATA_DAILY = "slug_breakdown_data_daily";
    public static final String BUNDLE_SLUG_BREAKDOWN_DATA_MONTHLY = "slug_breakdown_data_monthly";
    public static final String BUNDLE_SLUG_BREAKDOWN_DATA_YEARLY = "slug_breakdown_data_yearly";
    public static final String BUNDLE_SLUG_BREAKDOWN_YEARLY_TOTAL = "slug_breakdown_yearly_total";
    public static final String BUNDLE_SLUG_BREAKDOWN_MEASUREMENT_UNIT = "slug_breakdown_measurement_unit";

    public static final String BASE_URL_ADDRESS = "http://gmfit.mcsaatchi.me/api/v1/";
    public static final MediaType JSON_FORMAT_IDENTIFIER
            = MediaType.parse("application/json; charset=utf-8");

    public static final String EVENT_USER_FINALIZE_SETUP_PROFILE = "user_finalize_setup_profile";
    public static final String EVENT_STEP_COUNTER_INCREMENTED = "step_counter_incremented";
    public static final String EVENT_CALORIES_COUNTER_INCREMENTED = "calories_counter_incremented";
    public static final String EVENT_DISTANCE_COUNTER_INCREMENTED = "distance_counter_incremented";
    public static final String EVENT_FINISHED_SETTING_UP_PROFILE_SUCCESSFULLY = "finished_setting_up_profile_successfully";

    public static final String EVENT_CHART_ADDED_FROM_SETTINGS = "event_chart_added_from_settings";
    public static final String EVENT_CHART_METRICS_RECEIVED = "event_chart_metrics_received";
    public static final String EVENT_SIGNNED_UP_SUCCESSFULLY_CLOSE_LOGIN_ACTIVITY = "signned_up_successfully_close_login_activity";

    //REGISTERATION API
    public static final String NO_ACCESS_TOKEN_FOUND_IN_PREFS = "no_access_token_in_prefs";
    public static final String PREF_USER_ACCESS_TOKEN = "user_access_token";
    public static final String USER_ACCESS_TOKEN_HEADER_PARAMETER = "Authorization";
}
