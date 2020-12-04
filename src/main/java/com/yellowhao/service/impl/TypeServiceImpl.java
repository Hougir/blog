package com.yellowhao.service.impl;


import com.yellowhao.NotFoundException;
import com.yellowhao.dao.TypeRepository;
import com.yellowhao.po.Type;
import com.yellowhao.service.TypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private RedisTemplate redisTemplate;


    @Transactional
    @Override
    public Type saveType(Type type) {

        return typeRepository.save(type);
    }

    @Transactional
    @Override
    public Type getType(Long id) {
        return typeRepository.findOne(id);
    }

    @Transactional
    @Override
    public Page<Type> ListType(Pageable pageable) {

        //设置redisTemplate的key序列化方式
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        //从redis中获取
        Page<Type> types = (Page<Type>) redisTemplate.opsForValue().get("types");

        //判断是否有值
        if (null == types) {
            synchronized (this) {
                types = (Page<Type>) redisTemplate.opsForValue().get("types");
                if (null == types) {
                    //去数据库查询
                    types = typeRepository.findAll(pageable);
                    //存放到redis缓存中
                    redisTemplate.opsForValue().set("types", types, 10, TimeUnit.SECONDS);
                }
            }

        }
        return types;
    }

    @Override
    public List<Type> listType() {
        return typeRepository.findAll();
    }

    @Override
    public List<Type> listTypeTop(Integer size) {
        //设置redisTemplate的key序列化方式
        /*redisTemplate.setKeySerializer(new StringRedisSerializer());

        //从redis中获取
        List<Type> repositoryTop = (List<Type>) redisTemplate.opsForValue().get("repositoryTop");



        String  message = "redis";



        //判断是否有值
        if (null == repositoryTop || repositoryTop.size() == 0){
            synchronized (this) {
                repositoryTop = (List<Type>) redisTemplate.opsForValue().get("repositoryTop");
                if (null == repositoryTop || repositoryTop.size() == 0){
                    //去数据库查询
                    Sort sort = new Sort(Sort.Direction.DESC, "blogs.size");
                    Pageable pageable = new PageRequest(0, size, sort);
                    repositoryTop = typeRepository.findTop(pageable);


                    message = "数据库";


                    //存放到redis缓存中
                    redisTemplate.opsForValue().set("repositoryTop",repositoryTop,1000,TimeUnit.MICROSECONDS);

                }
            }

        }
        System.out.println("++++++++++++++++++++++");
        System.out.println("length === " + repositoryTop.size());
        System.out.println(message + "=====>" + repositoryTop);
        System.out.println("++++++++++++++++++++++");*/
        //去数据库查询
        Sort sort = new Sort(Sort.Direction.DESC, "blogs.size");
        Pageable pageable = new PageRequest(0, size, sort);
        List<Type> repositoryTop = typeRepository.findTop(pageable);
        return repositoryTop;
    }

    @Transactional
    @Override
    public Type updateType(Long id, Type type) {
        Type t = typeRepository.findOne(id);
        if (t == null) {
            throw new NotFoundException("不存在该类型");
        }
        BeanUtils.copyProperties(type, t);
        return typeRepository.save(t);
    }

    @Transactional
    @Override
    public void deleteType(Long id) {
        typeRepository.delete(id);
    }

    @Override
    public Type getTypeByName(String name) {
        return typeRepository.findByName(name);
    }
}
