package Exmpl.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class redisService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    //存缓存
    public void setCache(String key,String value, Long timeout){
        redisTemplate.opsForValue().set(key,value,timeout, TimeUnit.SECONDS);
    }
    //取缓存
    public String getCache(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }
    //清除缓存
    public void delCache(String key) {
        redisTemplate.delete(key);
    }

}
