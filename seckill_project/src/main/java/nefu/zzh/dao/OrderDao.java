package nefu.zzh.dao;

import nefu.zzh.vo.OrderInfo;
import nefu.zzh.vo.SecKillOrder;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OrderDao {

    @Select("select * from seckill_order where user_id = #{userId} and goods_id = #{goodsId} ")
    public SecKillOrder getSeckillOrderByUserIdGoodsId(@Param("userId") long userId, @Param("goodsId") long goodsId);

    @Insert("insert into order_info (user_id, goods_id, delivery_addr_id, goods_name, goods_count, goods_price, order_channel, status, create_date) values(" +
            "#{userId}, #{goodsId}, #{deliveryAddrId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel}, #{status}, #{createDate})")
    @SelectKey(keyColumn = "id", keyProperty = "id", resultType = long.class, before = false, statement = "select last_insert_id()")
    public long insert(OrderInfo orderInfo);

    @Insert("insert into seckill_order (user_id, goods_id, order_id)values(#{userId}, #{goodsId}, #{orderId})")
    public void insertSecKillOrder(SecKillOrder secKillOrder);
}
