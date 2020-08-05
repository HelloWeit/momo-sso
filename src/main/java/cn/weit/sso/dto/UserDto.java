package cn.weit.sso.dto;

import lombok.Data;

/**
 * @author weitong
 */
@Data
public class UserDto {
    private String mail;

    private String userName;

    private String display;

    private String roleName;

    private String password;

    public static UserDto gen() {
        UserDto userDto = new UserDto();
        userDto.setMail("demo@126.com");
        userDto.setUserName("demo");
        userDto.setDisplay("魏童");
        userDto.setRoleName("管理员");
        userDto.setPassword("123456");
        return userDto;
    }
}
