package com.leaf.job.dao;

import com.leaf.job.entity.EmailBodyEntity;
import com.leaf.job.entity.SysRoleEntity;

import java.util.List;

public interface EmailBodyDAO {
    /**
     * Find Entity from DB By Code
     *
     * @param code
     * @return {@link SysRoleEntity}
     */
    EmailBodyEntity findEmailBodyEntityByCode(String code);

    /**
     * Save EmailBody Entity
     *
     * @param emailBodyEntity
     */
    void saveEmailBodyEntity(EmailBodyEntity emailBodyEntity);

    /**
     * Update EmailBody Entity
     *
     * @param emailBodyEntity
     */
    void updateEmailBodyEntity(EmailBodyEntity emailBodyEntity);

    /**
     * Find All EmailBody Entity
     *
     */
    List<EmailBodyEntity> findAllEmailBodyEntities();
}
