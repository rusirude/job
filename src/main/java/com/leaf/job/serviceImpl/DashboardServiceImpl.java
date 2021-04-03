package com.leaf.job.serviceImpl;

import com.leaf.job.dao.SysRoleAuthorityDAO;
import com.leaf.job.dao.SysUserAuthorityDAO;
import com.leaf.job.dao.SysUserDAO;
import com.leaf.job.dto.common.MainMenuDTO;
import com.leaf.job.dto.common.MenuDTO;
import com.leaf.job.dto.common.MenuSectionDTO;
import com.leaf.job.entity.AuthorityEntity;
import com.leaf.job.entity.SysRoleEntity;
import com.leaf.job.entity.SysUserEntity;
import com.leaf.job.enums.DefaultStatusEnum;
import com.leaf.job.service.DashboardService;
import com.leaf.job.utility.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author : rusiru on 7/6/19.
 */

@Service
public class DashboardServiceImpl implements DashboardService {

    private CommonMethod commonMethod;
    private SysRoleAuthorityDAO sysRoleAuthorityDAO;
    private SysUserAuthorityDAO sysUserAuthorityDAO;
    private SysUserDAO sysUserDAO;


    @Autowired
    public DashboardServiceImpl(CommonMethod commonMethod, SysRoleAuthorityDAO sysRoleAuthorityDAO, SysUserAuthorityDAO sysUserAuthorityDAO, SysUserDAO sysUserDAO) {
        this.commonMethod = commonMethod;
        this.sysRoleAuthorityDAO = sysRoleAuthorityDAO;
        this.sysUserAuthorityDAO = sysUserAuthorityDAO;
        this.sysUserDAO = sysUserDAO;
    }

    public MainMenuDTO loadMainMenu() {

        Map<String, Set<AuthorityEntity>> menuMap = new HashMap<>();

        SysUserEntity user = sysUserDAO.getSysUserEntityByUsername(commonMethod.getUsername());

        List<SysRoleEntity> sysRoles = new ArrayList<>();
        user.getSysUserSysRoleEntities()
                .forEach(sysUserSysRole -> {
                    sysRoles.add(sysUserSysRole.getSysRoleEntity());
                });
        if (!sysRoles.isEmpty()) {
            sysRoleAuthorityDAO.getSysRoleAuthorityEntitiesBySysRolesAndAnuthorityStatusAndSysRoleStatus(sysRoles, DefaultStatusEnum.ACTIVE.getCode(), DefaultStatusEnum.ACTIVE.getCode())
                    .stream()
                    .filter(sysRoleAuthorityEntity -> DefaultStatusEnum.ACTIVE.getCode().equalsIgnoreCase(sysRoleAuthorityEntity.getAuthorityEntity().getSectionEntity().getStatusEntity().getCode()))
                    .forEach(roleAuthority -> {
                        String key = roleAuthority.getAuthorityEntity().getSectionEntity().getCode() + "-" + roleAuthority.getAuthorityEntity().getSectionEntity().getDescription();
                        if (!menuMap.containsKey(key)) {
                            Set<AuthorityEntity> set = new HashSet<>();
                            set.add(roleAuthority.getAuthorityEntity());
                            menuMap.put(key, set);
                        } else {
                            menuMap.get(key).add(roleAuthority.getAuthorityEntity());
                        }
                    });
        }

        sysUserAuthorityDAO.getSysUserAuthorityEntitiesBySysUser(user.getUsername())
                .stream()
                .filter(userAuthority -> DefaultStatusEnum.ACTIVE.getCode().equalsIgnoreCase(userAuthority.getAuthorityEntity().getSectionEntity().getStatusEntity().getCode()))
                .forEach(userAuthority -> {
                    String key = userAuthority.getAuthorityEntity().getSectionEntity().getCode() + "-" + userAuthority.getAuthorityEntity().getSectionEntity().getDescription();

                    long isEnabled = userAuthority.getIsGrant();
                    if (isEnabled == 1) {
                        if (!menuMap.containsKey(key)) {
                            Set<AuthorityEntity> set = new HashSet<>();
                            set.add(userAuthority.getAuthorityEntity());
                            menuMap.put(key, set);
                        } else {
                            menuMap.get(key).add(userAuthority.getAuthorityEntity());
                        }
                    } else {
                        if (menuMap.containsKey(key)) {
                            menuMap.get(key).remove(userAuthority.getAuthorityEntity());
                        }
                    }

                });


        List<MenuSectionDTO> sectionDTOs = menuMap.entrySet()
                .stream()
                .map(stringSetEntry -> {
                    MenuSectionDTO section = new MenuSectionDTO();
                    section.setDescription(stringSetEntry.getKey().split("-")[1]);
                    List<MenuDTO> menu = stringSetEntry.getValue()
                            .stream()
                            .sorted(Comparator.comparing(AuthorityEntity::getDescription))
                            .map(authorityEntity -> {
                                MenuDTO menuItem = new MenuDTO();
                                menuItem.setDescription(authorityEntity.getDescription());
                                menuItem.setUrl(authorityEntity.getUrl());
                                return menuItem;
                            }).collect(Collectors.toList());
                    section.setMenuDTOs(menu);
                    return section;
                }).collect(Collectors.toList());

        return new MainMenuDTO(sectionDTOs);
    }
}
