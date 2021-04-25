package com.leaf.job.serviceImpl;

import com.leaf.job.dao.EmailBodyDAO;
import com.leaf.job.dto.EmailBodyDTO;
import com.leaf.job.dto.common.ResponseDTO;
import com.leaf.job.entity.EmailBodyEntity;
import com.leaf.job.enums.ResponseCodeEnum;
import com.leaf.job.service.EmailBodyService;
import com.leaf.job.utility.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmailBodyServiceImpl implements EmailBodyService {

    private EmailBodyDAO emailBodyDAO;
    private CommonMethod commonMethod;

    @Autowired
    public EmailBodyServiceImpl(EmailBodyDAO emailBodyDAO, CommonMethod commonMethod) {
        this.emailBodyDAO = emailBodyDAO;
        this.commonMethod = commonMethod;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public ResponseDTO<EmailBodyDTO> updateEmailBody(EmailBodyDTO emailBodyDTO) {
        String code = ResponseCodeEnum.FAILED.getCode();
        String description = "Email Content Update is Faield";
        try {

            EmailBodyEntity emailEntity = emailBodyDAO.findEmailBodyEntityByCode(emailBodyDTO.getCode());
            emailEntity.setCode(emailBodyDTO.getCode());
            emailEntity.setSubject(emailBodyDTO.getSubject());
            emailEntity.setContent(emailBodyDTO.getContent());
            emailEntity.setEnable(Optional.ofNullable(emailBodyDTO.getEnable()).orElse(false));

            commonMethod.getPopulateEntityWhenUpdate(emailEntity);

            emailBodyDAO.updateEmailBodyEntity(emailEntity);
            code = ResponseCodeEnum.SUCCESS.getCode();
            description = "Email Content Update Successfully";
        } catch (Exception e) {
            System.err.println("Email Content Update Issue");
        }
        return new ResponseDTO<>(code, description);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public ResponseDTO<List<EmailBodyDTO>> findAllEmailBody() {
        String code = ResponseCodeEnum.FAILED.getCode();
        List<EmailBodyDTO> list = null;
        try {

            list = emailBodyDAO.findAllEmailBodyEntities()
                    .stream()
                    .map(emailBodyEntity -> {
                        EmailBodyDTO emailBodyDTO = new EmailBodyDTO();
                        emailBodyDTO.setCode(emailBodyEntity.getCode());
                        emailBodyDTO.setSubject(emailBodyEntity.getSubject());
                        emailBodyDTO.setContent(emailBodyEntity.getContent());
                        emailBodyDTO.setEnable(Optional.ofNullable(emailBodyEntity.getEnable()).orElse(false));

                        return emailBodyDTO;
                    }).collect(Collectors.toList());

            code = ResponseCodeEnum.SUCCESS.getCode();
        } catch (Exception e) {
            System.err.println("Find All Email Content  Issue");
        }
        return new ResponseDTO<>(code, list);
    }
}
