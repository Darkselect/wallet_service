package uz.wallet.wallet_service.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractCacheWarmup<T, E> {
    @Value("${cache.redis-batch-size}")
    private int batchSize;

    @Value("${cache.warmup.enabled:true}")
    private boolean warmupEnabled;

    @Value("${cache.warmup.timeout:30000}")
    private int warmupTimeout;

    private final ThreadPoolTaskExecutor taskExecutor;

    protected AbstractCacheWarmup(ThreadPoolTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onAppReady() {
        if (warmupEnabled) {
            log.info("Запускается прогрев кэша [{}]", getCacheName());
            CompletableFuture.runAsync(this::warmUpCache, taskExecutor)
                    .orTimeout(warmupTimeout, TimeUnit.MILLISECONDS)
                    .exceptionally(ex -> {
                        log.error("Ошибка при прогреве кэша [{}]", getCacheName(), ex);
                        return null;
                    });
        } else {
            log.info("Прогрев кэша [{}] отключён", getCacheName());
        }
    }

    protected void warmUpCache() {
        UUID lastId = new UUID(0L, 0L);
        boolean hasMore;
        int maxAttempts = 3;
        int attempt = 0;

        do {
            try {
                List<T> entities = fetchBatch(lastId, batchSize);
                hasMore = !entities.isEmpty();

                if (hasMore) {
                    saveToCache(entities);
                    lastId = getLastId(entities);
                    log.debug("Сохранено в кэш {} записей для [{}], последний ID: {}", entities.size(), getCacheName(), lastId);
                    attempt = 0;
                }
            } catch (Exception e) {
                attempt++;
                if (attempt >= maxAttempts) {
                    log.error("Достигнуто максимальное число попыток прогрева кэша [{}], остановка", getCacheName(), e);
                    hasMore = false;
                } else {
                    log.warn("Ошибка при прогреве кэша [{}], попытка {}/{}", getCacheName(), attempt, maxAttempts, e);
                    hasMore = true;
                }
            }
        } while (hasMore);

        log.info("Прогрев кэша [{}] завершён", getCacheName());
    }

    protected void saveToCache(List<T> entities) {
        List<E> cacheEntities = entities.stream()
                .map(this::mapToCacheEntity)
                .collect(Collectors.toList());
        saveCache(cacheEntities);
    }

    protected abstract List<T> fetchBatch(UUID lastId, int batchSize);
    protected abstract UUID getLastId(List<T> entities);
    protected abstract E mapToCacheEntity(T entity);
    protected abstract void saveCache(List<E> cacheEntities);
    protected abstract String getCacheName();
}
