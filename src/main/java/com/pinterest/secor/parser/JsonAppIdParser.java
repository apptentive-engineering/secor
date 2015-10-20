package com.pinterest.secor.parser;

import com.pinterest.secor.common.SecorConfig;
import com.pinterest.secor.message.Message;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by stuart on 10/19/15.
 * Implementation of AppIdParser for JSON events
 */
public class JsonAppIdParser extends AppIdParser{
    private DateFormat formatter;

    public JsonAppIdParser(SecorConfig config) {
        super(config);
        formatter = new SimpleDateFormat("yyyy-MM-dd");
    }

    public AppIdDate extractAppIdAndDate(final Message message) {
        JSONObject obj = (JSONObject) JSONValue.parse(message.getPayload());
        return new AppIdDate(extractAppId(obj), extractDate(obj));
    }

    private String extractAppId(JSONObject obj) {
        if (obj != null) {
            Object appIdValue = obj.get(mConfig.getAppIdName());
            if (appIdValue != null) {
                return appIdValue.toString();
            }
        }
        return "None";
    }

    private String extractDate(JSONObject obj) {
        if (obj != null) {
            Object appIdValue = obj.get(mConfig.getCreatedAt());
            if (appIdValue != null) {
                Date date = new Date(Double.valueOf(appIdValue.toString()).longValue());
                return formatter.format(date);
            }
        }
        return "0000-00-00";
    }
}
