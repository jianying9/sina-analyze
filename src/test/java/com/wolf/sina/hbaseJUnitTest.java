package com.wolf.sina;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.hfile.Compression;
import org.apache.hadoop.hbase.regionserver.StoreFile;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jianying9
 */
public class hbaseJUnitTest {

    public hbaseJUnitTest() {
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

//    @Test
    public void deleteTableTest() {
        System.out.println("delete");
        String tableName = "SinaUserInfo";
        Configuration config = HBaseConfiguration.create();
        try {
            HBaseAdmin hbaseAdmin = new HBaseAdmin(config);
            hbaseAdmin.disableTable(tableName);
            hbaseAdmin.deleteTable(tableName);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

//    @Test
    public void createTableTest() {
        byte[] columnFamily = Bytes.toBytes("INFO");
        int numRegions = 64;
        int split = 65536 / numRegions;
        int start = split;
        String startKey;
        List<byte[]> startKeyList = new ArrayList<byte[]>(64);
        while (start < 65536) {
            startKey = Integer.toHexString(start);
            switch (startKey.length()) {
                case 1:
                    startKey = "000" + startKey;
                    break;
                case 2:
                    startKey = "00" + startKey;
                    break;
                case 3:
                    startKey = "0" + startKey;
                    break;
                case 4:
                    break;
                default:
                    throw new RuntimeException("错误的startKey:" + startKey);
            }
            System.out.println(startKey);
            startKeyList.add(Bytes.toBytes(startKey));
            start += split;
        }
        byte[][] splitKeys = startKeyList.toArray(new byte[startKeyList.size()][]);
        String tableName = "SinaUserInfo";
        HTableDescriptor hTableDescriptor = new HTableDescriptor(tableName);
        HColumnDescriptor columnFamilyDescriptor = new HColumnDescriptor(columnFamily);
        columnFamilyDescriptor.setBlockCacheEnabled(true).setBloomFilterType(StoreFile.BloomType.ROWCOL)
                .setCompressionType(Compression.Algorithm.SNAPPY);
        hTableDescriptor.addFamily(columnFamilyDescriptor);
        Configuration config = HBaseConfiguration.create();
        try {
            HBaseAdmin hbaseAdmin = new HBaseAdmin(config);
            hbaseAdmin.createTable(hTableDescriptor, splitKeys);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
//    @Test
    public void isTableExistTest() {
        String tableName = "SinaUserInfo";
        Configuration config = HBaseConfiguration.create();
        try {
            HBaseAdmin hbaseAdmin = new HBaseAdmin(config);
            boolean result = hbaseAdmin.tableExists(tableName);
            System.out.println(result);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
//    @Test
    public void insertTest() {
        String tableName = "SinaUserInfo";
        Configuration config = HBaseConfiguration.create();
        Put put = new Put(Bytes.toBytes("2400_1234556678"));
        byte[] columnFamily = Bytes.toBytes("INFO");
        byte[] columnName = Bytes.toBytes("name");
        byte[] columnValue = Bytes.toBytes("1222");
        put.add(columnFamily, columnName, columnValue);
        try {
            HTablePool hTablePool = new HTablePool(config, 1);
            HTableInterface hTableInterface = hTablePool.getTable(tableName);
            hTableInterface.put(put);
            hTableInterface.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
//    @Test
    public void deleteTest() {
        String tableName = "SinaUserInfo";
        Configuration config = HBaseConfiguration.create();
        Delete delete = new Delete(Bytes.toBytes("0400_1234556678"));
        try {
            HTablePool hTablePool = new HTablePool(config, 1);
            HTableInterface hTableInterface = hTablePool.getTable(tableName);
            hTableInterface.delete(delete);
            hTableInterface.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}