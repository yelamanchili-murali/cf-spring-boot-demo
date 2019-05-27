package cf.demos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
	
	@Autowired
	private StringRedisTemplate redisTemplate;
	
	private static final String KEY = "redis.counter";
	
	@GetMapping(path="/")
	public String index() {
		return "CF Spring boot demo!";
	}
	
	@GetMapping(path = "/hello")
	public HelloWorldBean hello() {
		int counter = 0;
		ValueOperations<String, String> ops = redisTemplate.opsForValue();
		
		if(redisTemplate.hasKey(KEY)) {
			counter = Integer.parseInt(ops.get(KEY));
			counter++;
		}
		
		ops.set(KEY, Integer.toString(counter));
		
		return new HelloWorldBean("Hello World!", ops.get(KEY));
	}
}
