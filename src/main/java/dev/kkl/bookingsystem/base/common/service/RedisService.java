package dev.kkl.bookingsystem.base.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public boolean checkAndReserveSlot(String classScheduleId) {
        String key = "class:" + classScheduleId + ":slots";
        int slots = Integer.parseInt(Objects.requireNonNull(redisTemplate.opsForValue().get(key)));

        if (slots > 0) {
            redisTemplate.opsForValue().set(key, String.valueOf(slots - 1));
            return true;
        } else {
            return false;
        }
    }

    public void releaseSlot(String classScheduleId) {
        String key = "class:" + classScheduleId + ":slots";
        int slots = Integer.parseInt(Objects.requireNonNull(redisTemplate.opsForValue().get(key)));
        redisTemplate.opsForValue().set(key, String.valueOf(slots + 1));
    }
}
