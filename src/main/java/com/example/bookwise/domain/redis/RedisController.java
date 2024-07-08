package com.example.bookwise.domain.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/redis/test")
@RequiredArgsConstructor
public class RedisController {

    private final RedisTemplate<String, String> redisTemplate;

//    @PostMapping("")
//    public ResponseEntity<?> addRedisKey(@RequestBody ViewInfoModel vo) {
//        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
//        valueOperations.set(vo.getCallId(), vo.getOpenedAt());
//        return new ResponseEntity<>(HttpStatus.CREATED);
//    }
    @GetMapping("/info")
    public ResponseEntity<?> getRedisKey(@RequestHeader("Authorization") String accessToken) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String value = valueOperations.get(accessToken);
        return new ResponseEntity<>(value, HttpStatus.OK);
    }
}