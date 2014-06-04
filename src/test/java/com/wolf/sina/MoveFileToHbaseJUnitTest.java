package com.wolf.sina;

import com.wolf.framework.utils.SecurityUtils;
import com.wolf.framework.utils.StringUtils;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jianying9
 */
public class MoveFileToHbaseJUnitTest {

    public MoveFileToHbaseJUnitTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    //

    @Test
    public void hello() throws IOException {
        String tableName = "SinaUserInfo";
        Configuration config = HBaseConfiguration.create();
        byte[] columnFamily = Bytes.toBytes("INFO");
        HTablePool hTablePool = new HTablePool(config, 1);
        String dir = "/data/file/sina-user-info";
        File fileDir = new File(dir);
        File[] files = fileDir.listFiles();
        int totalNum = files.length;
        File file;
        String json;
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode;
        Map.Entry<String, JsonNode> entry;
        String name;
        String value;
        //
        String rowKey;
        String userId;
        String fieldValue;
        String[] fileds = {"gender", "nickName", "empName", "location", "tag", "follow", "lastUpdateTime"};
        //
        Put put;
        Map<String, String> fileResultMap;
        HTableInterface hTableInterface;
        for (int index = 0; index < totalNum; index++) {
            if (index % 1000 == 0) {
                System.out.println(index + "/" + totalNum);
            }
            file = files[index];
            json = FileUtils.readFileToString(file);
            if (json.isEmpty() == false) {
                rootNode = mapper.readValue(json, JsonNode.class);
                if (rootNode != null) {
                    //读数据
                    fileResultMap = new HashMap<String, String>(rootNode.size(), 1);
                    Iterator<Map.Entry<String, JsonNode>> iterator = rootNode.getFields();
                    while (iterator.hasNext()) {
                        entry = iterator.next();
                        name = entry.getKey();
                        value = entry.getValue().getTextValue();
                        value = StringUtils.trim(value);
                        fileResultMap.put(name, value);
                    }
                    //
                    userId = fileResultMap.get("userId");
                    if (userId != null) {
                        rowKey = SecurityUtils.encryptByMd5(userId);
                        rowKey = rowKey.toLowerCase().substring(0, 4);
                        rowKey = rowKey + '_' + userId;
                        put = new Put(Bytes.toBytes(rowKey));
                        put.setWriteToWAL(false);
                        //
                        for (String filed : fileds) {
                            fieldValue = fileResultMap.get(filed);
                            if (fieldValue != null) {
                                put.add(columnFamily, Bytes.toBytes(filed), Bytes.toBytes(fieldValue));
                            }
                        }
                        //
                        hTableInterface = hTablePool.getTable(tableName);
                        hTableInterface.put(put);
                        hTableInterface.close();
                    }
                }
            }
            //
            file.delete();
        }
    }
}