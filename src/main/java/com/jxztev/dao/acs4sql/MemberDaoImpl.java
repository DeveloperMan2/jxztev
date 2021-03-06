package com.jxztev.dao.acs4sql;

import java.util.ArrayList;
import java.util.List;

import com.jxztev.entity.acs4sql.Member;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;


@Repository(value="memberDao")
public class MemberDaoImpl extends RedisGeneratorDao<String, Member> implements IMemberDao{
    /**
     * 添加对象
     */
    @Override
    public boolean add(final Member member) {
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {
                RedisSerializer<String> serializer = getRedisSerializer();
                byte[] key  = serializer.serialize(member.getId());
                byte[] jsonString = serializer.serialize(member.getJsonData());
                return connection.setNX(key, jsonString);
            }
        });
        return result;
    }

    /**
     * 添加集合
     */
    @Override
    public boolean add(final List<Member> list) {
        Assert.notEmpty(list);
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {
                RedisSerializer<String> serializer = getRedisSerializer();
                for (Member member : list) {
                    byte[] key  = serializer.serialize(member.getId());
                    byte[] jsonString = serializer.serialize(member.getJsonData());
                    connection.setNX(key, jsonString);
                }
                return true;
            }
        }, false, true);
        return result;
    }

    /**
     * 删除对象 ,依赖key
     */
    public void delete(String key) {
        List<String> list = new ArrayList<String>();
        list.add(key);
        delete(list);
    }

    /**
     * 删除集合 ,依赖key集合
     */
    public void delete(List<String> keys) {
        redisTemplate.delete(keys);
    }

    /**
     * 修改对象
     */
    public boolean update(final Member member) {
        String key = member.getId();
        if (get(key) == null) {
            throw new NullPointerException("数据行不存在, key = " + key);
        }
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {
                RedisSerializer<String> serializer = getRedisSerializer();
                byte[] key  = serializer.serialize(member.getId());
                byte[] name = serializer.serialize(member.getJsonData());
                connection.set(key, name);
                return true;
            }
        });
        return result;
    }

    /**
     * 根据key获取对象
     */
    public Member get(final String keyId) {
        Member result = redisTemplate.execute(new RedisCallback<Member>() {
            public Member doInRedis(RedisConnection connection)
                    throws DataAccessException {
                RedisSerializer<String> serializer = getRedisSerializer();
                byte[] key = serializer.serialize(keyId);
                byte[] value = connection.get(key);
                if (value == null) {
                    return null;
                }
                String jsonString = serializer.deserialize(value);
                return new Member(keyId, jsonString);
            }
        });
        return result;
    }

}