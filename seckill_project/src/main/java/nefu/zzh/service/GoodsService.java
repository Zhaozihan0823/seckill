package nefu.zzh.service;
import nefu.zzh.vo.SecKillGoods;
import nefu.zzh.dao.GoodsDao;
import nefu.zzh.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {

    @Autowired
    GoodsDao goodsDao;

    public List<GoodsVo> goodsVoList(){
        return goodsDao.goodsVoList();
    }

    public GoodsVo getGoodVoByGoodsId(long googdId) {
        return goodsDao.getGoodsVoByGoodsId(googdId);
    }

    public boolean reduceStock(GoodsVo goodsVo) {
        SecKillGoods seckillGoods = new SecKillGoods();
        seckillGoods.setGoodsId(goodsVo.getId());
        int ret = goodsDao.reduceStock(seckillGoods);
        return ret > 0;
    }

    public void resetStock(List<GoodsVo> goodsVoList) {
        for (GoodsVo goods : goodsVoList){
            SecKillGoods secKillGoods = new SecKillGoods();
            secKillGoods.setGoodsId(goods.getId());
            secKillGoods.setStockCount(goods.getStockCount());
            goodsDao.resetStock(secKillGoods);
        }
    }
}
