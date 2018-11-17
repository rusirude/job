package com.leaf.job.serviceImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leaf.job.dao.SysRoleAuthorityDAO;
import com.leaf.job.dao.SysUserDAO;
import com.leaf.job.entity.SysUserEntity;
import com.leaf.job.entity.SysRoleEntity;

@Service
public class CustomUserDetailService implements UserDetailsService{
	
	private SysUserDAO sysUserDAO;
	private SysRoleAuthorityDAO sysRoleAuthorityDAO;
	

	@Autowired
	public CustomUserDetailService(SysUserDAO sysUserDAO,SysRoleAuthorityDAO sysRoleAuthorityDAO) {		
		this.sysUserDAO = sysUserDAO;
		this.sysRoleAuthorityDAO = sysRoleAuthorityDAO;
	}



	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			SysUserEntity sysUserEntity = sysUserDAO.getSysUserEntityByUsername(username);	
			return new User(sysUserEntity.getUsername(),sysUserEntity.getPassword(),getGrantedAuthoritiesForUser(sysUserEntity));
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	private Set<GrantedAuthority> getGrantedAuthoritiesForUser(SysUserEntity user) {
        Set<GrantedAuthority> grantedAuthoritySet = new HashSet<>();
        List<SysRoleEntity> sysRoles = new ArrayList<>();
        user.getSysUserSysRoleEntities()
                .forEach(sysUserSysRole -> {
                    sysRoles.add(sysUserSysRole.getSysRoleEntity());
                });
        if (! sysRoles.isEmpty()) {
            sysRoleAuthorityDAO.getSysRoleAuthorityEntitiesBySysRoles(sysRoles)
                    .stream()
                    .forEach(roleAuthority -> {
                        grantedAuthoritySet.add(new SimpleGrantedAuthority(roleAuthority.getAuthorityEntity().getAuthCode()));
                    });
        }
        return grantedAuthoritySet;
}

}
