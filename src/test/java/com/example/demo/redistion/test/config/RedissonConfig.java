// Đây là config để khởi tạo Redisson client (kết nối Redis)
package com.example.demo.redistion.test.config;

import java.util.Objects;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.config.Config;

public class RedissonConfig {
    private RedissonClient redissonClient;

    // Hàm khởi tạo client đồng bộ
    public RedissonClient getClient() {
        if (Objects.isNull(this.redissonClient)) {
            Config config = new Config();
            config.useSingleServer()
                    .setAddress("redis://127.0.0.1:6379"); // kết nối Redis local

            redissonClient = Redisson.create(config);
        }
        return redissonClient;
    }

    // Lấy client reactive (hỗ trợ Project Reactor, dùng trong WebFlux / reactive
    // programming)
    public RedissonReactiveClient getReactiveClient() {
        return getClient().reactive();
    }
}
