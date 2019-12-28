package me.codingall.board.board.dto;

import lombok.*;
import me.codingall.board.board.entity.UserEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String name;
    private String password;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public UserEntity toEntity(){
        return UserEntity.builder()
                .id(id)
                .email(email)
                .name(name)
                .password(password)
                .build();
    }

    @Builder
    public UserDto(Long id, String email, String name, String password) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
    }
}