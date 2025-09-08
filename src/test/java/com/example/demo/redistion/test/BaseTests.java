// Đây là class BaseTests để setup / teardown Redis client cho tất cả test class
package com.example.demo.redistion.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.redisson.api.RedissonReactiveClient;

import com.example.demo.redistion.test.config.RedissonConfig;

// @TestInstance(PER_CLASS) => để dùng được @BeforeAll, @AfterAll trong method non-static
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTests {
    private final RedissonConfig redissonConfig = new RedissonConfig();
    protected RedissonReactiveClient client;

    // Trước khi chạy tất cả test trong class, khởi tạo client
    @BeforeAll
    public void setClient() {
        this.client = redissonConfig.getReactiveClient();
    }

    // Sau khi chạy xong tất cả test, shutdown client
    @AfterAll
    public void shutdown() {
        this.client.shutdown();
    }

    // --------------------------------
    protected void sleep(long seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    // -------------
}
