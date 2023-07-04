package com.sztus.unicorn.lib.cache.config;

//@ConfigurationProperties(prefix = "spring.redis.primary.writer")
//@Configuration
public class RedissonConfig {

//    @Bean(destroyMethod = "shutdown",name = "primaryRedissonClient")
//    RedissonClient redisson() {
//        Config config = new Config();
//        config.setCodec(new FastJsonCodec());
////        config.useSingleServer().setAddress("rediss://"+"develop-aze8.redis.singapore.rds.aliyuncs.com" + ":" + port)
//        config.useSingleServer().setAddress("rediss://"+"develop-aze8.redis.singapore.rds.aliyuncs.com" + ":" + port)
//                .setDatabase(database);
//        return Redisson.create(config);
//    }

    private String hostName;
    private String port;
    private int database = 0;

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }
}