package com.leaf.job.service;

import com.leaf.job.dto.SysRoleDTO;
import com.leaf.job.dto.SysUserDTO;
import com.leaf.job.dto.common.DataTableRequestDTO;
import com.leaf.job.dto.common.DataTableResponseDTO;
import com.leaf.job.dto.common.ResponseDTO;

public interface PasswordResetRequestService {




    /**
     * Create Password Reset Request
     * @param sysUserDTO
     * @return {@link ResponseDTO}
     */
    ResponseDTO<?> createPasswordResetRequest(SysUserDTO sysUserDTO);

    /**
     * Reset Password
     * @param requestId
     * @return {@link ResponseDTO}
     */
    ResponseDTO<?> resetPassword(Long requestId);

    /**
     * Get password Reset Requests For Data Table
     *
     * @param dataTableRequestDTO
     * @return {@link DataTableResponseDTO}
     */
    DataTableResponseDTO getPasswordResetRequestForDataTable(DataTableRequestDTO dataTableRequestDTO);

}
