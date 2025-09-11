package com.example.demo.redisperformance.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.redisson.api.BatchOptions;
import org.redisson.api.RBatchReactive;
import org.redisson.api.RScoredSortedSetReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.client.codec.IntegerCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
public class ProductVisitService {

    @Autowired
    private RedissonReactiveClient client; // Redisson reactive client (kết nối Redis)
    private Sinks.Many<Integer> sink; // reactive sink để nhận event visit (productId)

    public ProductVisitService() {
        // unicast sink: chỉ có 1 consumer, dùng để nhận các event "product visit"
        this.sink = Sinks.many().unicast().onBackpressureBuffer();
    }

    @PostConstruct
    private void init() {
        // xử lý stream từ sink khi service khởi động
        this.sink
                .asFlux() // chuyển sink thành Flux (stream)
                .buffer(Duration.ofSeconds(3)) // gom event lại mỗi 3 giây -> list (1,2,1,1,3,5,1...)
                .map(l -> l.stream().collect(
                        // gom nhóm theo productId, đếm số lần xuất hiện
                        // ví dụ: {1:4, 5:1, 3:1}
                        Collectors.groupingBy(
                                Function.identity(), // key = productId
                                Collectors.counting() // value = số lần visit
                        )))
                .flatMap(this::updateBatch) // batch update vào Redis
                .subscribe(); // subscribe để kích hoạt pipeline
    }

    // Gọi method này mỗi lần có người visit product
    public void addVisit(int productId) {
        this.sink.tryEmitNext(productId); // emit productId vào sink
    }

    // Batch update visit count vào Redis
    private Mono<Void> updateBatch(Map<Integer, Long> map) {
        // Tạo batch request để gửi nhiều lệnh cùng lúc (tối ưu performance)
        RBatchReactive batch = this.client.createBatch(BatchOptions.defaults());

        // Key Redis sẽ theo ngày: "product:visit:20250911"
        String format = DateTimeFormatter.ofPattern("YYYYMMdd").format(LocalDate.now());

        // Lấy sorted set (Redis ZSet) lưu visit count
        RScoredSortedSetReactive<Integer> set = batch.getScoredSortedSet(
                "product:visit:" + format,
                IntegerCodec.INSTANCE);

        // Thêm score cho mỗi productId (tăng số visit)
        return Flux.fromIterable(map.entrySet())
                .map(e -> set.addScore(e.getKey(), e.getValue())) // tăng score theo số lần visit
                .then(batch.execute()) // execute batch 1 lần duy nhất
                .then(); // trả về Mono<Void>
    }

}
