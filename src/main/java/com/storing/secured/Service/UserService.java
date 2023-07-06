package com.storing.secured.Service;

import com.storing.secured.Entity.User;
import java.util.List;

public interface UserService {

    User getUserByEmail(String email);

    public User createUser(User user);

    public List<User> getUsers();


}
