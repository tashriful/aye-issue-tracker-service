package com.aye.issueTracker.service;

import com.aye.issueTracker.model.User;
import com.aye.issuetrackerdto.entityDto.UserDto;

import java.util.List;


public interface UserService {

    UserDto saveUsers(UserDto userDto);

    List<UserDto> getAllUsers();

    UserDto updateUser(UserDto userDto, Long id);


    UserDto getUserById(Long id);

    void deleteUserById(Long id);


    User getUserIdByUserName(String name);
}
