package com.leaf.job.serviceImpl;

import com.leaf.job.dao.MasterDataDAO;
import com.leaf.job.dao.PasswordResetRequestDAO;
import com.leaf.job.dao.StatusDAO;
import com.leaf.job.dao.SysUserDAO;
import com.leaf.job.dto.PasswordResetRequestDTO;
import com.leaf.job.dto.SysUserDTO;
import com.leaf.job.dto.common.DataTableRequestDTO;
import com.leaf.job.dto.common.DataTableResponseDTO;
import com.leaf.job.dto.common.ResponseDTO;
import com.leaf.job.entity.MasterDataEntity;
import com.leaf.job.entity.PasswordResetRequestEntity;
import com.leaf.job.entity.StatusEntity;
import com.leaf.job.entity.SysUserEntity;
import com.leaf.job.enums.DeleteStatusEnum;
import com.leaf.job.enums.MasterDataEnum;
import com.leaf.job.enums.PasswordResetRequestStatusEnum;
import com.leaf.job.enums.ResponseCodeEnum;
import com.leaf.job.service.PasswordResetRequestService;
import com.leaf.job.utility.CommonConstant;
import com.leaf.job.utility.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PasswordResetRequestServiceImpl implements PasswordResetRequestService {

    private SysUserDAO sysUserDAO;
    private StatusDAO statusDAO;
    private MasterDataDAO masterDataDAO;
    private PasswordResetRequestDAO passwordResetRequestDAO;

    private CommonMethod commonMethod;

    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public PasswordResetRequestServiceImpl(SysUserDAO sysUserDAO, StatusDAO statusDAO, MasterDataDAO masterDataDAO, PasswordResetRequestDAO passwordResetRequestDAO, CommonMethod commonMethod, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.sysUserDAO = sysUserDAO;
        this.statusDAO = statusDAO;
        this.masterDataDAO = masterDataDAO;
        this.passwordResetRequestDAO = passwordResetRequestDAO;
        this.commonMethod = commonMethod;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    @Transactional
    public ResponseDTO<?> createPasswordResetRequest(SysUserDTO sysUserDTO) {
        String code = ResponseCodeEnum.FAILED.getCode();
        String description = "Reset password is  Faield";
        SysUserEntity sysUserEntity;
        try {
            sysUserEntity = sysUserDAO.getSysUserEntityByUsername(sysUserDTO.getUsername());
            if (sysUserEntity == null || DeleteStatusEnum.DELETE.getCode().equalsIgnoreCase(sysUserEntity.getStatusEntity().getCode()))
                description = "User is Not Found";

            if (!Optional.ofNullable(passwordResetRequestDAO.findAllPasswordResetRequestsBySysUserAndStatus(sysUserEntity.getUsername(), PasswordResetRequestStatusEnum.REQUEST.getCode())).orElse(Collections.EMPTY_LIST).isEmpty())
                description = "Password Reset Request is pending";

            else {
                sysUserEntity.setResetRequest(true);
                commonMethod.getPopulateEntityWhenUpdate(sysUserEntity, sysUserDTO.getUsername());
                sysUserDAO.saveSysUserEntity(sysUserEntity);

                StatusEntity statusEntity = statusDAO.findStatusEntityByCode(PasswordResetRequestStatusEnum.REQUEST.getCode());

                PasswordResetRequestEntity passwordResetRequestEntity = new PasswordResetRequestEntity();
                passwordResetRequestEntity.setSysUserEntity(sysUserEntity);
                passwordResetRequestEntity.setStatusEntity(statusEntity);

                commonMethod.getPopulateEntityWhenInsert(passwordResetRequestEntity, sysUserDTO.getUsername());
                passwordResetRequestDAO.savePasswordResetRequestEntity(passwordResetRequestEntity);

                code = ResponseCodeEnum.SUCCESS.getCode();


                description = "Password Reset Request is Sent";
            }

        } catch (Exception e) {
            System.err.println("Reset password Issue");
        }
        return new ResponseDTO<>(code, description);
    }


    @Override
    @Transactional
    public ResponseDTO<?> resetPassword(Long requestId) {
        String code = ResponseCodeEnum.FAILED.getCode();
        String description = "Reset password is  Faield";

        try {

            PasswordResetRequestEntity passwordResetRequestEntity = passwordResetRequestDAO.findPasswordResetRequest(requestId);
            if (PasswordResetRequestStatusEnum.PRESET.getCode().equalsIgnoreCase(passwordResetRequestEntity.getStatusEntity().getCode()))
                description = "Password Reset Already";

            else {
                if (DeleteStatusEnum.DELETE.getCode().equalsIgnoreCase(passwordResetRequestEntity.getSysUserEntity().getStatusEntity().getCode()))
                    description = "User is Not Found";
                else {
                    SysUserEntity sysUserEntity = passwordResetRequestEntity.getSysUserEntity();

                    MasterDataEntity defaultPasswordMasterDataEntity = Optional.ofNullable(masterDataDAO.loadMasterDataEntity(MasterDataEnum.DEFAULT_PASSWORD.getCode())).orElse(new MasterDataEntity());

                    sysUserEntity.setResetRequest(false);
                    sysUserEntity.setPassword(bCryptPasswordEncoder.encode(Optional.ofNullable(defaultPasswordMasterDataEntity.getValue()).orElse("")));
                    commonMethod.getPopulateEntityWhenUpdate(sysUserEntity);
                    sysUserDAO.saveSysUserEntity(sysUserEntity);

                    StatusEntity statusEntity = statusDAO.findStatusEntityByCode(PasswordResetRequestStatusEnum.PRESET.getCode());

                    passwordResetRequestEntity.setStatusEntity(statusEntity);

                    commonMethod.getPopulateEntityWhenUpdate(passwordResetRequestEntity);
                    passwordResetRequestDAO.updatePasswordResetRequestEntity(passwordResetRequestEntity);

                    code = ResponseCodeEnum.SUCCESS.getCode();


                    description = "Password Reset is success";
                }
            }


        } catch (Exception e) {
            System.err.println("Reset password Issue");
        }
        return new ResponseDTO<>(code, description);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public DataTableResponseDTO getPasswordResetRequestForDataTable(DataTableRequestDTO dataTableRequestDTO) {
        List<PasswordResetRequestDTO> list;
        DataTableResponseDTO responseDTO = new DataTableResponseDTO();
        Long numOfRecord;
        try {
            list = passwordResetRequestDAO.<List<PasswordResetRequestEntity>>getDataForGrid(dataTableRequestDTO, CommonConstant.GRID_SEARCH_LIST)
                    .stream().map(entity -> {
                        PasswordResetRequestDTO dto = new PasswordResetRequestDTO();
                        dto.setId(entity.getId());
                        dto.setUsername(entity.getSysUserEntity().getUsername());
                        dto.setTitleDescription(entity.getSysUserEntity().getTitleEntity().getDescription());
                        dto.setName(entity.getSysUserEntity().getName());
                        dto.setStatusCode(entity.getStatusEntity().getCode());
                        dto.setStatusDescription(entity.getStatusEntity().getDescription());
                        dto.setCreatedBy(entity.getCreatedBy());
                        dto.setCreatedOn(entity.getCreatedOn());
                        dto.setUpdatedBy(entity.getUpdatedBy());
                        dto.setUpdatedOn(entity.getUpdatedOn());
                        return dto;
                    }).collect(Collectors.toList());

            numOfRecord = passwordResetRequestDAO.<Long>getDataForGrid(dataTableRequestDTO, CommonConstant.GRID_SEARC_COUNT);

            responseDTO.setData(list);
            responseDTO.setRecordsTotal(numOfRecord);
            responseDTO.setRecordsFiltered(numOfRecord);
            responseDTO.setDraw(dataTableRequestDTO.getDraw());

        } catch (Exception e) {
            System.err.println("Password Reset Request Data Table Issue");
        }

        return responseDTO;
    }

}
