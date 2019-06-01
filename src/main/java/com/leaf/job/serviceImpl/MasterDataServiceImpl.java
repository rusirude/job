package com.leaf.job.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leaf.job.dao.MasterDataDAO;
import com.leaf.job.dto.MasterDataDTO;
import com.leaf.job.dto.common.ResponseDTO;
import com.leaf.job.entity.MasterDataEntity;
import com.leaf.job.enums.ResponseCodeEnum;
import com.leaf.job.service.MasterDataService;
import com.leaf.job.utility.CommonMethod;

@Service
public class MasterDataServiceImpl implements MasterDataService {
	
	private MasterDataDAO masterDataDAO;
	private CommonMethod commonMethod;
	
	@Autowired
	public MasterDataServiceImpl(MasterDataDAO masterDataDAO,CommonMethod commonMethod) {		
		this.masterDataDAO = masterDataDAO;
		this.commonMethod = commonMethod;
	}	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public ResponseDTO<List<MasterDataDTO>> loadAllMasterData() {
		String code = ResponseCodeEnum.FAILED.getCode();	
		List<MasterDataDTO> masterData = null;
		try {
			masterData = masterDataDAO.findAllMasterDataEntities()
					.stream().map(entity->{
						MasterDataDTO dataDTO = new MasterDataDTO();
						dataDTO.setCode(entity.getCode());
						dataDTO.setValue(entity.getValue());
						return dataDTO;
					}).collect(Collectors.toList());
			code = ResponseCodeEnum.SUCCESS.getCode();
			
		} catch (Exception e) {
			System.err.println("Getting all master data issue");
		}
		return new ResponseDTO<List<MasterDataDTO>>(code, masterData);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public ResponseDTO<MasterDataDTO> saveOrUpdateMasterData(List<MasterDataDTO> masterDataDTOS) {
		
		String code = ResponseCodeEnum.FAILED.getCode();
		String description = "Master Data Save Faild";
		try {
			masterDataDTOS.stream().forEach(dto->{
				MasterDataEntity entity = masterDataDAO.findMasterDataEntity(dto.getCode());
				if(entity != null) {
					entity.setValue(String.valueOf(dto.getValue()));
					commonMethod.getPopulateEntityWhenUpdate(entity);
					
					masterDataDAO.updateMasterDataEntity(entity);
				}
				else {
					entity = new MasterDataEntity();
					entity.setCode(dto.getCode());
					entity.setValue(String.valueOf(dto.getValue()));
					commonMethod.getPopulateEntityWhenInsert(entity);
					
					masterDataDAO.saveMasterDataEntity(entity);
				}
			});
			
			code = ResponseCodeEnum.SUCCESS.getCode();
			description = "Master Data Saved Successfully";
			
		} catch (Exception e) {
			System.err.println("Save or Update master data issue");
		}
		return new ResponseDTO<MasterDataDTO>(code, description);
	}

}