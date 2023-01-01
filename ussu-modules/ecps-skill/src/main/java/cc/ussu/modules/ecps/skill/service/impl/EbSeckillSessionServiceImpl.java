package cc.ussu.modules.ecps.skill.service.impl;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.redis.service.RedisService;
import cc.ussu.modules.ecps.common.constants.ConstantsEcps;
import cc.ussu.modules.ecps.common.constants.ConstantsEcpsRabbit;
import cc.ussu.modules.ecps.common.constants.OrderPayStatus;
import cc.ussu.modules.ecps.common.constants.OrderStatus;
import cc.ussu.modules.ecps.item.entity.EbSku;
import cc.ussu.modules.ecps.order.entity.EbOrder;
import cc.ussu.modules.ecps.order.entity.EbOrderDetail;
import cc.ussu.modules.ecps.skill.entity.EbSeckillSession;
import cc.ussu.modules.ecps.skill.entity.EbSessionSkuRelation;
import cc.ussu.modules.ecps.skill.feign.RemoteSkuService;
import cc.ussu.modules.ecps.skill.mapper.EbSeckillSessionMapper;
import cc.ussu.modules.ecps.skill.service.IEbSeckillSessionService;
import cc.ussu.modules.ecps.skill.service.IEbSessionSkuRelationService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author liming
 * @since 2021-08-11
 */
@Service
public class EbSeckillSessionServiceImpl extends ServiceImpl<EbSeckillSessionMapper, EbSeckillSession> implements IEbSeckillSessionService {

    @Autowired
    private RedisService redisService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private IEbSessionSkuRelationService sessionSkuRelationService;
    @Autowired
    private RemoteSkuService remoteSkuService;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public static final String SKILL_SESSIONS_PREFIX = ConstantsEcps.CACHE_SKILL_SESSIONS_PREFIX;
    public static final String SKILL_SKU = ConstantsEcps.CACHE_SKILL_SKU;
    public static final String SKILL_STOCK_PREFIX = ConstantsEcps.CACHE_SKILL_STOCK_PREFIX;

    @Override
    public EbSeckillSession detail(Integer id) {
        Assert.notNull(id);
        EbSeckillSession obj = new EbSeckillSession().selectById(id);
        return obj;
    }

    @Transactional
    @Override
    public void add(EbSeckillSession p) {
        p.insert();
    }

    @Transactional
    @Override
    public void edit(EbSeckillSession p) {
        p.updateById();
    }

    @Transactional
    @Override
    public void del(String ids) {
        int[] ints = StrUtil.splitToInt(ids, StrPool.COMMA);
        Assert.isTrue(ints.length > 0);
        LambdaQueryWrapper<EbSeckillSession> qw = new LambdaQueryWrapper<>();
        qw.in(EbSeckillSession::getSessionId, Arrays.stream(ints).boxed().collect(Collectors.toList()));
        super.remove(qw);
    }

    /**
     * 上架当天的秒杀商品
     */
    @Override
    public void uploadSkillSku() {
        // 获取当天秒杀场次
        Date now = new Date();
        List<EbSeckillSession> sessionList = baseMapper.selectList(Wrappers.lambdaQuery(EbSeckillSession.class)
                .ge(EbSeckillSession::getStartTime, DateUtil.beginOfDay(now))
                .le(EbSeckillSession::getEndTime, DateUtil.endOfDay(now)));
        List<Integer> sessionIdList = sessionList.stream().map(EbSeckillSession::getSessionId).collect(Collectors.toList());
        if (CollUtil.isEmpty(sessionIdList)) return;
        // 获取秒杀商品信息
        List<EbSessionSkuRelation> relationList = sessionSkuRelationService.list(Wrappers.lambdaQuery(EbSessionSkuRelation.class).in(EbSessionSkuRelation::getSessionId, sessionIdList));
        List<Integer> skuIdList = relationList.stream().map(EbSessionSkuRelation::getSkuId).collect(Collectors.toList());
        if (CollUtil.isEmpty(skuIdList)) return;
        List<EbSku> simpleDetailSkuList = remoteSkuService.getSimpleDetailBySkuIdList(CollUtil.join(skuIdList, StrPool.COMMA));
        // ebSku设置到relation上
        for (EbSessionSkuRelation relation : relationList) {
            relation.setEbSku(filterSkuListBySkuId(simpleDetailSkuList, relation.getSkuId()));
        }
        // 给session设置relationList
        for (EbSeckillSession seckillSession : sessionList) {
            seckillSession.setRelationList(filterRelationListBySessionId(relationList, seckillSession.getSessionId()));
        }
        // 保存到redis
        saveSkillDataToRedis(sessionList);
    }

    private void saveSkillDataToRedis(List<EbSeckillSession> sessionList) {
        // 1.场次的信息 key是时间段 value是skuId集合
        for (EbSeckillSession seckillSession : sessionList) {
            String key = SKILL_SESSIONS_PREFIX + seckillSession.getStartTime().getTime() + StrPool.UNDERLINE + seckillSession.getEndTime().getTime();
            if (!redisService.hasKey(key)) {
                List<Integer> value = seckillSession.getRelationList().stream().map(EbSessionSkuRelation::getSkuId).collect(Collectors.toList());
                if (CollUtil.isEmpty(value)) continue;
                redisService.setCacheList(key, value);
            }
            // 设置随机码
            for (EbSessionSkuRelation relation : seckillSession.getRelationList()) {
                // 2.商品的信息 key是 skuId value是relation
                BoundHashOperations<String, String, String> ops = stringRedisTemplate.boundHashOps(SKILL_SKU);
                if (!ops.hasKey(relation.getSkuId().toString())) {
                    relation.setStartTime(seckillSession.getStartTime().getTime())
                            .setEndTime(seckillSession.getEndTime().getTime())
                            .setRandomCode(UUID.randomUUID().toString(true));
                    // redisService.setCacheMapValue(SKILL_SKU, relation.getSkuId().toString(), relation);
                    ops.put(relation.getSkuId().toString(), JSON.toJSONString(relation));
                    // 3.信号量 key是randomCode value是秒杀库存
                    String stockKey = SKILL_STOCK_PREFIX + relation.getRandomCode();
                    if (!redisService.hasKey(stockKey)) {
                        RSemaphore semaphore = redissonClient.getSemaphore(stockKey);
                        semaphore.trySetPermits(relation.getSeckillCount());
                    }
                }
            }
        }
    }

    /**
     * 根据skuId过滤Sku实体
     *
     * @param skuList
     * @param skuId
     * @return
     */
    private EbSku filterSkuListBySkuId(List<EbSku> skuList, Integer skuId) {
        for (EbSku ebSku : skuList) {
            if (ebSku.getSkuId().equals(skuId)) {
                return ebSku;
            }
        }
        return null;
    }

    /**
     * 根据sessionId过滤relationList
     */
    private List<EbSessionSkuRelation> filterRelationListBySessionId(List<EbSessionSkuRelation> sessionSkuRelations, Integer sessionId) {
        return sessionSkuRelations.stream().filter(item -> item.getSessionId().equals(sessionId)).collect(Collectors.toList());
    }

    @Override
    public JsonResult seckillSku(Integer skuId, String randomCode, Integer num) {
        String NOT = "没有抢到商品";
        // 缺少参数
        if (skuId == null || StrUtil.isBlank(randomCode)) {
            return JsonResult.error(NOT);
        }
        BoundHashOperations<String, String, String> ops = stringRedisTemplate.boundHashOps(SKILL_SKU);
        // 去除sku详情
        String s = ops.get(skuId.toString());
        if (StrUtil.isBlank(s)) {
            return JsonResult.error(NOT);
        }
        EbSessionSkuRelation relation = JSON.parseObject(s, EbSessionSkuRelation.class);
        // 对比随机码
        if (!randomCode.equals(relation.getRandomCode())) {
            return JsonResult.error(NOT);
        }
        // 购买数量
        if (num == null || num < 0) {
            num = 1;
        }
        if (num > relation.getSeckillLimit()) {
            return JsonResult.error(NOT);
        }
        String stockKey = SKILL_STOCK_PREFIX + relation.getRandomCode();
        RSemaphore semaphore = redissonClient.getSemaphore(stockKey);
        if (semaphore.tryAcquire(num)) {
            // 库存扣减成功 准备向mq中存放消息。构建秒杀订单的类。转换成json，向mq发送json。
            EbOrder skillOrder = createSkillOrder(relation, num);
            String skillOrderStr = JSON.toJSONString(skillOrder);
            // 把mq换成同步的方式
            Boolean invoke = rabbitTemplate.invoke(opss -> {
                // 发送消息
                rabbitTemplate.convertAndSend(ConstantsEcpsRabbit.EXCHANGE_SKILL_ORDER, ConstantsEcpsRabbit.ROUTING_KEY_SKILL_ORDER, skillOrderStr);
                // 确认机制
                return rabbitTemplate.waitForConfirms(500);
            });
            if (invoke) {
                // 如果是true，表示消息成功进入到队列中 秒杀成功
                System.out.println("====秒杀成功====");
                return JsonResult.ok(skillOrder);
            } else {
                // 消息没有进入队列中，被抛弃了 信号量补偿
                semaphore.release(num);
                return JsonResult.error(NOT);
            }
        } else {
            return JsonResult.error(NOT);
        }
    }

    /**
     * 创建秒杀订单
     */
    private EbOrder createSkillOrder(EbSessionSkuRelation relation, Integer num) {
        EbSku sku = relation.getEbSku();
        EbOrderDetail orderDetail = new EbOrderDetail()
                .setSkuId(sku.getSkuId())
                .setSkuName(sku.getSkuName())
                .setSkuImg(sku.getSkuImg())
                .setSkuPrice(relation.getSeckillPrice())
                .setPrice(NumberUtil.mul(relation.getSeckillPrice(), num));
        List<EbOrderDetail> orderDetailList = new ArrayList<>();
        EbOrder skillOrder = new EbOrder().setSeckillId(relation.getRelationId())
                .setOrderNum(IdWorker.getIdStr())
                .setOrderTime(new Date())
                .setOrderSum(orderDetail.getPrice())
                .setShipFee(new BigDecimal("0"))
                .setIsDelete(0)
                // .setEbUserId(SecurityUtil.getUserId())
                // .setUsername(SecurityUtil.getUsername())
                .setIsPaid(OrderPayStatus.NOT_PAID.getCode())
                .setOrderState(OrderStatus.NOT_PAID.getCode())
                .setOrderDetailList(orderDetailList);
        return skillOrder;
    }

}
