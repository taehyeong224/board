package me.codingall.board.board.service;

import me.codingall.board.board.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    Long joinUser(UserDto userDto);
    UserDetails loadUserByUsername(String userEmail);
}
