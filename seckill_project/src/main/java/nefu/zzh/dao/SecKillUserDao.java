package nefu.zzh.dao;

import nefu.zzh.vo.SecKillUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SecKillUserDao {
    @Select("select * from seckill_user where id = #{id}")
    public SecKillUser getById(@Param("id") long id);

    @Update("update seckill_user set password = #{password} where id = #{id} ")
    void update(SecKillUser toBeUpdate);
}
