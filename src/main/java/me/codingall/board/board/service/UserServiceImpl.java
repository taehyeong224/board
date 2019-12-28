package me.codingall.board.board.service;

import lombok.AllArgsConstructor;
import me.codingall.board.board.dto.UserDto;
import me.codingall.board.board.entity.UserEntity;
import me.codingall.board.board.repository.UserRepository;
import me.codingall.board.common.CustomUserDetail;
import me.codingall.board.common.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Transactional
    public Long joinUser(UserDto userDto) {
        // 비밀번호 암호화
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        return userRepository.save(userDto.toEntity()).getId();
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        Optional<UserEntity> userEntityWrapper = userRepository.findByEmail(userEmail);
        UserEntity userEntity = userEntityWrapper.orElseThrow(NoSuchElementException::new);
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (("admin@codingall.me").equals(userEmail)) {
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
        }

        CustomUserDetail userDetail = new CustomUserDetail();
        userDetail.setEmail(userEntity.getEmail());
        userDetail.setPassword(userEntity.getPassword());
        userDetail.setName(userEntity.getName());
        userDetail.setAuthorities(authorities);
        return userDetail;
    }
}
