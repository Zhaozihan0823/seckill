package nefu.zzh;

import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SeckillProjectApplicationTests {

    @Autowired
    private StringEncryptor stringEncryptor;

    @Test
    void contextLoads() {
    }

    @Test
    public void encryptPwd() {
        //加密方法
        System.out.println(stringEncryptor.encrypt(""));
    }


}
