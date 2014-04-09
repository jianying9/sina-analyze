package com.wolf.sina.spider.localservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author aladdin
 */
public class HttpClientManager {

    private final List<DefaultHttpClient> clientList = new ArrayList<DefaultHttpClient>(20);
    private int currentIndex = 0;
    private final Map<DefaultHttpClient, Long> lastUseTimeMap = new HashMap<DefaultHttpClient, Long>(20, 1);

    public void add(DefaultHttpClient client) {
        this.clientList.add(client);
    }

    public DefaultHttpClient getClient() {
        DefaultHttpClient client;
        Long lastTime;
        long currentTime;
        synchronized (this) {
            client = this.clientList.get(this.currentIndex);
            this.currentIndex++;
            if (this.currentIndex >= this.clientList.size()) {
                this.currentIndex = 0;
            }
            lastTime = this.lastUseTimeMap.get(client);
            currentTime = System.currentTimeMillis();
            if (lastTime == null) {
                lastTime = currentTime;
            }
            this.lastUseTimeMap.put(client, currentTime);
        }
        if (currentTime - lastTime < 500) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
            }
        }
        return client;
    }
}
