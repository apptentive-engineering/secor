package com.pinterest.secor.parser;

import com.pinterest.secor.common.SecorConfig;
import com.pinterest.secor.message.Message;

/**
 * Created by stuart on 10/19/15.
 * Abstract App ID parser - extracts App ID and Date (YYYY-MM-DD) to use as partition names.
 */
public abstract class AppIdParser extends MessageParser {

    public AppIdParser(SecorConfig config) {
        super(config);
    }

    public class AppIdDate {
        private String appId;
        private String date;

        public AppIdDate(String appId, String date) {
            this.appId = appId;
            this.date = date;
        }

        public String[] toArray() {
            String[] array = {"app", appId, "date", date};
            return array;
        }
    }

    public abstract AppIdDate extractAppIdAndDate(final Message message);

    public String[] extractPartitions(final Message message) {
        return extractAppIdAndDate(message).toArray();
    }

}
