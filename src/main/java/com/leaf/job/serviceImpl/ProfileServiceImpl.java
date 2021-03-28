package com.leaf.job.serviceImpl;

import com.leaf.job.dao.StatusDAO;
import com.leaf.job.dao.SysUserDAO;
import com.leaf.job.dto.SysUserDTO;
import com.leaf.job.dto.common.ResponseDTO;
import com.leaf.job.entity.MasterDataEntity;
import com.leaf.job.entity.StatusEntity;
import com.leaf.job.entity.SysUserEntity;
import com.leaf.job.entity.TitleEntity;
import com.leaf.job.enums.MasterDataEnum;
import com.leaf.job.enums.ResponseCodeEnum;
import com.leaf.job.service.ProfileService;
import com.leaf.job.utility.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
public class ProfileServiceImpl implements ProfileService {

    private SysUserDAO sysUserDAO;
    private StatusDAO statusDAO;

    private CommonMethod commonMethod;

    BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public ProfileServiceImpl(SysUserDAO sysUserDAO, StatusDAO statusDAO, CommonMethod commonMethod, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.sysUserDAO = sysUserDAO;
        this.statusDAO = statusDAO;
        this.commonMethod = commonMethod;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public ResponseDTO<HashMap<String, Object>> getReferenceDataForProfile() {
        HashMap<String, Object> map = new HashMap<>();
        String code = ResponseCodeEnum.FAILED.getCode();
        try {

            map.put("username", "");

            code = ResponseCodeEnum.SUCCESS.getCode();
        } catch (Exception e) {
            System.err.println("Profile Ref Data Issue");
        }
        return new ResponseDTO<>(code, map);
    }

    @Override
    public ResponseDTO<?> saveProfile(SysUserDTO sysUserDTO) {
        String code = ResponseCodeEnum.FAILED.getCode();
        String description = "Password Change is Faild";
        SysUserEntity sysUserEntity;
        try {
            sysUserEntity = sysUserDAO.getSysUserEntityByUsername(commonMethod.getUsername());

            if(sysUserEntity.getPassword().equals(bCryptPasswordEncoder.encode(sysUserDTO.getPassword()))){
                sysUserEntity = new SysUserEntity();
                sysUserEntity.setPassword(bCryptPasswordEncoder.encode(sysUserDTO.getNewPassword()));

                commonMethod.getPopulateEntityWhenUpdate(sysUserEntity);

                sysUserDAO.saveSysUserEntity(sysUserEntity);

                code = ResponseCodeEnum.SUCCESS.getCode();
                description = "Password is changed Successfully";

            }

            else {
                description = "Current Password Invalid";
            }
        }
        catch(Exception e) {
            System.err.println("Profile Change Issue");
        }
        return new ResponseDTO<SysUserDTO>(code,description);
    }
}
