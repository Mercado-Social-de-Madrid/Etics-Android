package net.mercadosocial.moneda;

public class DebugHelper {

    public static Class SHORTCUT_ACTIVITY = null;

    // === SWITCHES DEBUG ===

    // Trues in production apk (automatic)
    private static final boolean ACRA_ENABLED = true;
    private static final boolean FINAL_TTF = true;
    private static final boolean PROD_ENVIRONMENT = true;


    // Falses
    private static final boolean SKIP_SPLASH = false;
    private static final boolean COMPLETE_EDIT_TEXTS = true;


    private static final boolean TEST_FUNCTION = false;



    public static final boolean DEBUG_MESSAGES = false;
    private static final boolean DEBUG_TOKEN = false;

    private static final boolean FORZE_MASTER = false;
    private static final boolean FORZE_ALARM = true;

    private static final boolean FORZE_BETA_ENV_APK = false;

    public static final boolean BILLING_TESTING = false;

    public static final boolean MOCK_DATA = false;

    // ----------------------------------------------

    public static final boolean SWITCH_FINAL_TTF = BuildConfig.DEBUG
            || FORZE_BETA_ENV_APK ? FINAL_TTF : true;

    public static final boolean SWITCH_ACRA_ENABLED = BuildConfig.DEBUG
            || FORZE_BETA_ENV_APK ? ACRA_ENABLED : true;

    public static final boolean SWITCH_SKIP_SPLASH = BuildConfig.DEBUG
            || FORZE_BETA_ENV_APK ? SKIP_SPLASH : false;

    public static final boolean SWITCH_COMPLETE_EDIT_TEXTS = BuildConfig.DEBUG
            || FORZE_BETA_ENV_APK ? COMPLETE_EDIT_TEXTS : false;

    public static final boolean SWITCH_PROD_ENVIRONMENT = BuildConfig.DEBUG
            || FORZE_BETA_ENV_APK ? PROD_ENVIRONMENT : true;

    public static final boolean SWITCH_DEBUG_MESSAGES = BuildConfig.DEBUG
            || FORZE_BETA_ENV_APK ? DEBUG_MESSAGES : false;

    public static final boolean SWITCH_FORZE_MASTER = BuildConfig.DEBUG
            || FORZE_BETA_ENV_APK ? FORZE_MASTER : false;

    public static final boolean SWITCH_FORZE_ALARM = BuildConfig.DEBUG
            || FORZE_BETA_ENV_APK ? FORZE_ALARM : false;

    public static final boolean SWITCH_DEBUG_TOKEN = BuildConfig.DEBUG
            || FORZE_BETA_ENV_APK ? DEBUG_TOKEN : false;

    public static final boolean SWITCH_TEST_FUNCTION = BuildConfig.DEBUG
            || FORZE_BETA_ENV_APK ? TEST_FUNCTION : false;

}
