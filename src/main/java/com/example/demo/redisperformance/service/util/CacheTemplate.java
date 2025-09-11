package com.example.demo.redisperformance.service.util;

import reactor.core.publisher.Mono;

// Template pattern: định nghĩa sẵn luồng xử lý chung cho cache + source (DB)
// Các subclass sẽ implement cụ thể (Redis, DB, API,...)
public abstract class CacheTemplate<KEY, ENTITY> {

    // Lấy dữ liệu theo key
    public Mono<ENTITY> get(KEY key) {
        return getFromCache(key) // 1. Thử lấy từ cache (nhanh)
                .switchIfEmpty( // 2. Nếu cache không có
                        getFromSource(key) // 3. Lấy từ source (DB, API,...)
                                .flatMap(e -> updateCache(key, e)) // 4. Sau khi lấy được -> ghi lại vào cache
                );
    }

    // Cập nhật dữ liệu
    public Mono<ENTITY> update(KEY key, ENTITY entity) {
        return updateSource(key, entity) // 1. Update vào source trước (DB,...)
                .flatMap(e -> deleteFromCache(key) // 2. Xóa cache (tránh dữ liệu cũ)
                        .thenReturn(e) // 3. Trả về entity đã update
                );
    }

    // Xóa dữ liệu
    public Mono<Void> delete(KEY key) {
        return deleteFromSource(key) // 1. Xóa ở source (DB,...)
                .then(deleteFromCache(key)); // 2. Sau đó xóa luôn cache
    }

    // ---------------- Abstract methods ----------------
    // Các subclass sẽ hiện thực tùy theo công nghệ dùng (Redis, DB,...)

    // Lấy từ source (DB, API,...)
    abstract protected Mono<ENTITY> getFromSource(KEY key);

    // Lấy từ cache (Redis,...)
    abstract protected Mono<ENTITY> getFromCache(KEY key);

    // Update vào source (DB,...)
    abstract protected Mono<ENTITY> updateSource(KEY key, ENTITY entity);

    // Update vào cache (Redis,...)
    abstract protected Mono<ENTITY> updateCache(KEY key, ENTITY entity);

    // Xóa khỏi source
    abstract protected Mono<Void> deleteFromSource(KEY key);

    // Xóa khỏi cache
    abstract protected Mono<Void> deleteFromCache(KEY key);
}
