package nefu.zzh.dao;

import nefu.zzh.vo.Goods;
import nefu.zzh.vo.GoodsVo;
import nefu.zzh.vo.SecKillGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface GoodsDao {

    @Select("select g.*,sg.seckill_price,sg.stock_count,sg.start_date,sg.end_date from seckill_goods sg left join goods g on  sg.goods_id = g.id")
    public List<GoodsVo> goodsVoList();

    @Select("select g.*,sg.seckill_price,sg.stock_count,sg.start_date,sg.end_date from seckill_goods sg left join goods g on  sg.goods_id = g.id where g.id = #{goodsId}")
    public GoodsVo getGoodsVoByGoodsId(@Param("goodsId")long googsId);

    @Update("update seckill_goods set stock_count = stock_count - 1 where goods_id = #{goodsId} ")
    public int reduceStock(SecKillGoods goods);
}
