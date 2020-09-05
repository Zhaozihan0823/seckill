package nefu.zzh.service;

import nefu.zzh.commons.Redis.RedisUtil;
import nefu.zzh.commons.Redis.SeckillKey;
import nefu.zzh.commons.Util.MD5Util;
import nefu.zzh.commons.Util.UUIDUtil;
import nefu.zzh.controller.LoginController;
import nefu.zzh.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

@Service
public class SeckillService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    RedisUtil redisUtil;

    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Transactional
    public OrderInfo seckill(SecKillUser user, GoodsVo goodsVo) {
        //减库存
        boolean success = goodsService.reduceStock(goodsVo);
        if (success){
            //下订单
            return orderService.createOrder(user, goodsVo);
        }else {
            setGoodsOver(goodsVo.getId());
            return null;
        }

    }

    public long getSeckillResult(Long userId, long goodsId) {
        SecKillOrder order = orderService.getSeckillOrderByUserIdGoodsId(userId, goodsId);
        if (order != null){ //秒杀成功
            return order.getOrderId();
        }else {
            boolean isOver = getGoodsOver(goodsId);
            if (isOver){
                return -1;
            }else {
                return 0;
            }
        }
    }

    private boolean getGoodsOver(long goodsId) {
        return redisUtil.exists(SeckillKey.isGoodsOver, ""+goodsId);
    }

    private void setGoodsOver(Long goodsId) {
        redisUtil.set(SeckillKey.isGoodsOver, ""+goodsId, true);
    }

    public void reset(List<GoodsVo> goodsVoList){
        goodsService.resetStock(goodsVoList);
        orderService.deleteOrders();
    }

    public String createSeckillPath(SecKillUser user, long goodsId) {
        if (user == null || goodsId < 0){
            return null;
        }
        String str = MD5Util.md5(UUIDUtil.uuid()+123456);
        redisUtil.set(SeckillKey.getSeckillPath, ""+user.getId()+"_"+goodsId, str);
        return str;
    }

    public boolean checkPath(SecKillUser user, long goodsId, String path) {
        String pathOld= redisUtil.get(SeckillKey.getSeckillPath, ""+user.getId()+"_"+goodsId, String.class);

        if (path == null || pathOld == null) {
            return false;
        }
        return path.equals(pathOld);
    }

    //生成验证图片
    public BufferedImage createVerifyCode(SecKillUser user, long goodsId) {
        if (user == null || goodsId < 0){
            return null;
        }
        int width = 80;
        int height = 32;
        // 创建一个图像create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // 设置背景颜色 set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // 画边框 draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // 创建随机数 create a random instance to generate the codes
        Random random = new Random();
        // 添加一些干扰噪点 make some confusion
        for (int i = 0; i < 50; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // 生成验证码 generate a random code
        String verifyCode = generateVerifyCode(random);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //把验证码存到redis中
        int code = calc(verifyCode);
        log.info("验证码：" + code);
        redisUtil.set(SeckillKey.getVerifyCode, user.getId()+","+goodsId, code);
        //输出图片
        return image;
    }

    private static char[] ops = new char[]{'+','-','*'};
    //生成验证码 可做+ - * 运算,避免/可能出现的除0操作
    private String generateVerifyCode(Random random) {
        int num1 = random.nextInt(10);
        int num2 = random.nextInt(10);
        int num3 = random.nextInt(10);
        char operate1 = ops[random.nextInt(3)];
        char operate2 = ops[random.nextInt(3)];

        String exp = "" + num1 + operate1 + num2 + operate2 + num3;
        return exp;
    }

    private int calc(String verifyCode) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");

            return (Integer)engine.eval(verifyCode);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public boolean checkVerifyCode(SecKillUser user, long goodsId, int verifyCode) {
        if (user == null || goodsId < 0){
            return false;
        }
        Integer codeOld = redisUtil.get(SeckillKey.getVerifyCode, user.getId()+","+goodsId, Integer.class);
        log.info("codeOld: " + codeOld);
        if (codeOld == null || (codeOld - verifyCode) != 0){
            return false;
        }
        redisUtil.delete(SeckillKey.getVerifyCode, user.getId()+","+goodsId);
        return true;
    }
}
