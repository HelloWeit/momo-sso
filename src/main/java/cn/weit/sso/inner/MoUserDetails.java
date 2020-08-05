package cn.weit.sso.inner;

import cn.weit.sso.dto.UserDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @author weitong
 */
@Getter
@Setter
public class MoUserDetails extends User {

    private String mail;

    private String userName;

    private String display;

    private String roleName;


    public MoUserDetails(UserDto userDto, Collection<? extends GrantedAuthority> authorities) {
        super(userDto.getUserName(), userDto.getPassword(), authorities);
        this.mail = userDto.getMail();
        this.userName = userDto.getUserName();
        this.display = userDto.getDisplay();
        this.roleName = userDto.getRoleName();
    }

}
