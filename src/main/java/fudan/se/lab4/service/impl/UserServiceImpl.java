package fudan.se.lab4.service.impl;

import fudan.se.lab4.service.UserService;

public class UserServiceImpl implements UserService {
    @Override
    //简化了 本应该与数据库做交互 获取客户类型 是高级客户还是普通客户一类
    public int getType(int userId) {
        if(userId==0) {
            return 0;
        }
        return 0;
    }
}
