package nefu.zzh.service;

import nefu.zzh.dao.UserDao;
import nefu.zzh.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    UserDao userDao;

    public User getUserById(Integer id){
        return userDao.getById(id);
    }

    @Transactional
    public boolean Tx() {
        User u1 = new User();
        u1.setId(2);
        u1.setName("Jean");
        userDao.insert(u1);

        User u2 = new User();
        u2.setId(1);
        u2.setName("Tom");
        userDao.insert(u2);

        return true;
    }
}
