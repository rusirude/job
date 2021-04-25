package com.leaf.job.service;

import com.leaf.job.dto.EmailBodyDTO;
import com.leaf.job.dto.common.ResponseDTO;

import java.util.List;

public interface EmailBodyService {
    /**
     * Update EmailBody
     *
     * @param emailBodyDTO
     * @return {@link ResponseDTO}
     */
    ResponseDTO<EmailBodyDTO> updateEmailBody(EmailBodyDTO emailBodyDTO);

    /**
     * Find All EmailBody
     */
    ResponseDTO<List<EmailBodyDTO>> findAllEmailBody();
}
