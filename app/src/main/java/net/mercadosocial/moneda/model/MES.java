package net.mercadosocial.moneda.model;

import android.text.TextUtils;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessaging;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.api.common.ApiClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MES {

    public static final String CODE_MADRID = "mad";
    public static final String CODE_REG_MURCIA = "mur";

    private static String cityCode;

    private String name;
    private String code;
    private MESData mesData;

    public static List<MES> mesList = new ArrayList<>();

    static {
        mesList.add(new MES("Madrid", CODE_MADRID, MESData.madrid()));
//        mesList.add(new MES("Aragón", "ara"));
        mesList.add(new MES("Región de Murcia", CODE_REG_MURCIA, MESData.murcia()));
    }

    public MES(String name, String code, MESData mesData) {
        this.name = name;
        this.code = code;
        this.mesData = mesData;
    }

    public static int getMESPositionByCode(String codeMES) {
        for (int i = 0; i < mesList.size(); i++) {
            if (TextUtils.equals(mesList.get(i).getCode(), codeMES)) {
                return i;
            }
        }

        return 0;
    }

    public static String getMESNameByCode(String codeMES) {
        if (TextUtils.isEmpty(codeMES)) {
            return null;
        }

        return mesList.get(getMESPositionByCode(codeMES)).getName();
    }

    public static MES getMESbyCode(String code) {
        return mesList.get(getMESPositionByCode(code));
    }

    public static void setCityCode(String cityCode) {
        if (cityCode == MES.cityCode) {
            return;
        }

        updateFirebaseChannelsSubscriptions(cityCode);

        updateBaseUrlApi(cityCode);

    }

    private static void updateFirebaseChannelsSubscriptions(String cityCode) {
        if (TextUtils.equals(cityCode, CODE_MADRID)) {
            FirebaseMessaging.getInstance().subscribeToTopic(App.TOPIC_NEWS_MADRID);
            FirebaseMessaging.getInstance().subscribeToTopic(App.TOPIC_OFFERS_MADRID);
            FirebaseMessaging.getInstance().unsubscribeFromTopic(App.TOPIC_NEWS_MURCIA);
            FirebaseMessaging.getInstance().unsubscribeFromTopic(App.TOPIC_OFFERS_MURCIA);
        } else if (TextUtils.equals(cityCode, CODE_REG_MURCIA)) {
            FirebaseMessaging.getInstance().subscribeToTopic(App.TOPIC_NEWS_MURCIA);
            FirebaseMessaging.getInstance().subscribeToTopic(App.TOPIC_OFFERS_MURCIA);
            FirebaseMessaging.getInstance().unsubscribeFromTopic(App.TOPIC_NEWS_MADRID);
            FirebaseMessaging.getInstance().unsubscribeFromTopic(App.TOPIC_OFFERS_MADRID);
        }
    }

    public static String getCityCodeHeaderParam() {
        return CODE_MADRID;
    }

    private static void updateBaseUrlApi(String cityCode) {
        Map<String, String> baseUrls = new HashMap() {{
            put(CODE_MADRID, ApiClient.BASE_URL);
            put(CODE_REG_MURCIA, ApiClient.BASE_URL_REGION_MURCIA);
        }};

        ApiClient.setBaseUrl(baseUrls.get(cityCode));
    }

    public static String getCityCode() {
        return cityCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public MESData getMesData() {
        return mesData;
    }

    public void setMesData(MESData mesData) {
        this.mesData = mesData;
    }
}
