package cn.coolcollege.fast.model;

import lombok.Data;

import java.util.List;

@Data
public class UserDTO extends BaseDTO{
    List<User> users;
}
