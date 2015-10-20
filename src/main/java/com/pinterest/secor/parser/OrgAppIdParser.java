package com.pinterest.secor.parser;

import com.pinterest.secor.common.SecorConfig;
import com.pinterest.secor.message.Message;

/**
 * Created by stuart on 10/19/15.
 * Abstract App ID parser - extracts App ID and Date (YYYY-MM-DD) to use as partition names.
 */
public abstract class OrgAppIdParser extends MessageParser {

    public OrgAppIdParser(SecorConfig config) {
        super(config);
    }

    public class OrgAppIdDate {
        private String orgId;
        private String appId;
        private String date;

        public OrgAppIdDate(String orgId, String appId, String date) {
            this.orgId = orgId;
            this.appId = appId;
            this.date = date;
        }

        public String[] toArray() {
            String[] array = {"org", orgId, "app", appId, "date", date};
            return array;
        }
    }

    public abstract OrgAppIdDate extractAppIdAndDate(final Message message);

    public String[] extractPartitions(final Message message) {
        return extractAppIdAndDate(message).toArray();
    }

}
