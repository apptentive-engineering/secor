package com.pinterest.secor.parser;

import com.pinterest.secor.common.SecorConfig;
import com.pinterest.secor.message.Message;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by stuart on 10/19/15.
 * Implementation of AppIdParser for JSON events
 */
public class JsonAppIdParser extends AppIdParser {
    private DateFormat formatter;

    public JsonAppIdParser(SecorConfig config) {
        super(config);
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public AppIdDate extractAppIdAndDate(final Message message) {
        JSONObject obj = (JSONObject) JSONValue.parse(message.getPayload());
        return new AppIdDate(
                extractString(obj, "app_id"),
                extractDateString(obj, "created_at"));
    }

    private String extractString(JSONObject obj, String key) {
        try {
            Object appIdValue = obj.get(key);
            return appIdValue.toString();
        } catch (Exception e) {}
        return "None";
    }

    private String extractDateString(JSONObject obj, String key) {
        try {
            Object appIdValue = obj.get(key);
            Date date = new Date(Double.valueOf(appIdValue.toString()).longValue() * 1000);
            return formatter.format(date);
        } catch (Exception e) {}
        return "0000-00-00";
    }
}
