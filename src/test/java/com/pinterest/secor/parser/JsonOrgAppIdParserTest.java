package com.pinterest.secor.parser;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;

import com.pinterest.secor.common.SecorConfig;
import com.pinterest.secor.message.Message;

/**
 * Created by stuart on 10/20/15.
 * Test the JSON App Id Parser
 */

@RunWith(PowerMockRunner.class)
public class JsonOrgAppIdParserTest extends TestCase {

    private SecorConfig mConfig;
    private Message mCorrect1;
    private Message mCorrect2;
    private Message mMissingAppId;
    private Message mMissingOrgId;
    private Message mMissingDate;
    private Message mInvalidDate;

    @Override
    public void setUp() throws Exception {
        mConfig = Mockito.mock(SecorConfig.class);
        Mockito.when(mConfig.getMessageTimestampName()).thenReturn("timestamp");

        byte correct1[] = "{\"created_at\":1728000,\"conversation_id\":\"11543295\",\"label\":\"local#app#purchase\",\"app_id\":\"1234\",\"org_id\":\"abcd\"}"
                .getBytes("UTF-8");
        mCorrect1 = new Message("test", 0, 0, correct1);

        byte correct2[] = "{\"created_at\":1727999,\"conversation_id\":\"11543295\",\"label\":\"local#app#purchase\",\"app_id\":\"1234\",\"org_id\":\"abcd\"}"
                .getBytes("UTF-8");
        mCorrect2 = new Message("test", 0, 0, correct2);

        byte missingAppId[] = "{\"created_at\":1728000,\"conversation_id\":\"11543295\",\"label\":\"local#app#purchase\",\"org_id\":\"abcd\"}"
                .getBytes("UTF-8");
        mMissingAppId = new Message("test", 0, 0, missingAppId);

        byte missingOrgId[] = "{\"created_at\":1728000,\"conversation_id\":\"11543295\",\"label\":\"local#app#purchase\",\"app_id\":\"1234\"}"
                .getBytes("UTF-8");
        mMissingOrgId = new Message("test", 0, 0, missingOrgId);

        byte missingDate[] = "{\"conversation_id\":\"11543295\",\"label\":\"local#app#purchase\",\"app_id\":\"1234\",\"org_id\":\"abcd\"}"
                .getBytes("UTF-8");
        mMissingDate = new Message("test", 0, 0, missingDate);

        byte invalidDate[] = "{\"created_at\":\"dog\",\"conversation_id\":\"11543295\",\"label\":\"local#app#purchase\",\"app_id\":\"1234\",\"org_id\":\"abcd\"}"
                .getBytes("UTF-8");
        mInvalidDate = new Message("test", 0, 0, invalidDate);
    }

    @Test
    public void testExtractCorrect() throws Exception {
        String[] path = new JsonAppIdParser(mConfig).extractPartitions(mCorrect1);
        assertEquals("app", path[0]);
        assertEquals("1234", path[1]);
        assertEquals("date", path[2]);
        assertEquals("1970-01-21", path[3]);

        path = new JsonAppIdParser(mConfig).extractPartitions(mCorrect2);
        assertEquals("app", path[0]);
        assertEquals("1234", path[1]);
        assertEquals("date", path[2]);
        assertEquals("1970-01-20", path[3]);
    }

    @Test
    public void testExtractMissingData() throws Exception {
        String[] path = new JsonAppIdParser(mConfig).extractPartitions(mMissingAppId);
        assertEquals("app", path[0]);
        assertEquals("None", path[1]);
        assertEquals("date", path[2]);
        assertEquals("1970-01-21", path[3]);

        path = new JsonAppIdParser(mConfig).extractPartitions(mMissingOrgId);
        assertEquals("app", path[0]);
        assertEquals("1234", path[1]);
        assertEquals("date", path[2]);
        assertEquals("1970-01-21", path[3]);

        path = new JsonAppIdParser(mConfig).extractPartitions(mMissingDate);
        assertEquals("app", path[0]);
        assertEquals("1234", path[1]);
        assertEquals("date", path[2]);
        assertEquals("0000-00-00", path[3]);

    }

    @Test
    public void testExtractInvalidData() throws Exception {
        String[] path = new JsonAppIdParser(mConfig).extractPartitions(mInvalidDate);
        assertEquals("app", path[0]);
        assertEquals("1234", path[1]);
        assertEquals("date", path[2]);
        assertEquals("0000-00-00", path[3]);
    }
}
