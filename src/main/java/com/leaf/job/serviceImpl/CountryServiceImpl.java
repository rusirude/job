package com.leaf.job.serviceImpl;

import com.leaf.job.dao.StatusCategoryDAO;
import com.leaf.job.dao.StatusDAO;
import com.leaf.job.dao.CountryDAO;
import com.leaf.job.dto.CountryDTO;
import com.leaf.job.dto.common.DataTableRequestDTO;
import com.leaf.job.dto.common.DataTableResponseDTO;
import com.leaf.job.dto.common.DropDownDTO;
import com.leaf.job.dto.common.ResponseDTO;
import com.leaf.job.entity.StatusEntity;
import com.leaf.job.entity.CountryEntity;
import com.leaf.job.enums.DeleteStatusEnum;
import com.leaf.job.enums.ResponseCodeEnum;
import com.leaf.job.enums.StatusCategoryEnum;
import com.leaf.job.service.CountryService;
import com.leaf.job.utility.CommonConstant;
import com.leaf.job.utility.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CountryServiceImpl implements CountryService {

	private CountryDAO countryDAO;
	private StatusDAO statusDAO;
	private StatusCategoryDAO statusCategoryDAO;

	private CommonMethod commonMethod;

	@Autowired
	public CountryServiceImpl(CountryDAO countryDAO, StatusDAO statusDAO, StatusCategoryDAO statusCategoryDAO,
							  CommonMethod commonMethod) {
		this.countryDAO = countryDAO;
		this.statusDAO = statusDAO;
		this.statusCategoryDAO = statusCategoryDAO;
		this.commonMethod = commonMethod;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public ResponseDTO<CountryDTO> saveCountry(CountryDTO countryDTO) {
		String code = ResponseCodeEnum.FAILED.getCode();
		String description = "Country Save Faield";

		CountryEntity countryEntity;
		try {
			countryEntity = countryDAO.findCountryEntityByCode(countryDTO.getCode());
			if (countryEntity == null) {
				StatusEntity statusEntity = statusDAO.findStatusEntityByCode(countryDTO.getStatusCode());

				countryEntity = new CountryEntity();
				countryEntity.setCode(countryDTO.getCode());
				countryEntity.setDescription(countryDTO.getDescription());
				countryEntity.setStatusEntity(statusEntity);

				commonMethod.getPopulateEntityWhenInsert(countryEntity);

				countryDAO.saveCountryEntity(countryEntity);
				code = ResponseCodeEnum.SUCCESS.getCode();
				description = "Country Save Successfully";
			} else if (DeleteStatusEnum.DELETE.getCode().equals(countryEntity.getStatusEntity().getCode())) {

				StatusEntity statusEntity = statusDAO.findStatusEntityByCode(countryDTO.getStatusCode());

				countryEntity.setCode(countryDTO.getCode());
				countryEntity.setDescription(countryDTO.getDescription());
				countryEntity.setStatusEntity(statusEntity);

				commonMethod.getPopulateEntityWhenInsert(countryEntity);

				countryDAO.updateCountryEntity(countryEntity);
				code = ResponseCodeEnum.SUCCESS.getCode();
				description = "Country Save Successfully";
			} else {
				description = "Country Code is Already Used ";
			}

		} catch (Exception e) {
			System.err.println("Country Save Issue");
		}
		return new ResponseDTO<CountryDTO>(code, description);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public ResponseDTO<CountryDTO> updateCountry(CountryDTO countryDTO) {
		String code = ResponseCodeEnum.FAILED.getCode();
		String description = "Country Update Faield";
		try {
			StatusEntity statusEntity = statusDAO.findStatusEntityByCode(countryDTO.getStatusCode());

			CountryEntity countryEntity = countryDAO.findCountryEntityByCode(countryDTO.getCode());
			countryEntity.setCode(countryDTO.getCode());
			countryEntity.setDescription(countryDTO.getDescription());
			countryEntity.setStatusEntity(statusEntity);

			commonMethod.getPopulateEntityWhenUpdate(countryEntity);

			countryDAO.updateCountryEntity(countryEntity);
			code = ResponseCodeEnum.SUCCESS.getCode();
			description = "Country Update Successfully";
		} catch (Exception e) {
			System.err.println("Country Update Issue");
		}
		return new ResponseDTO<CountryDTO>(code, description);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public ResponseDTO<CountryDTO> deleteCountry(CountryDTO countryDTO) {
		String code = ResponseCodeEnum.FAILED.getCode();
		String description = "Country Delete Faield";
		try {
			StatusEntity statusEntity = statusDAO.findStatusEntityByCode(DeleteStatusEnum.DELETE.getCode());

			CountryEntity countryEntity = countryDAO.findCountryEntityByCode(countryDTO.getCode());

			countryEntity.setStatusEntity(statusEntity);

			commonMethod.getPopulateEntityWhenUpdate(countryEntity);

			countryDAO.updateCountryEntity(countryEntity);
			code = ResponseCodeEnum.SUCCESS.getCode();
			description = "Country Delete Successfully";
		} catch (Exception e) {
			System.err.println("Country Delete Issue");
		}
		return new ResponseDTO<CountryDTO>(code, description);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public ResponseDTO<CountryDTO> findCountry(CountryDTO countryDTO) {
		String code = ResponseCodeEnum.FAILED.getCode();
		String description = "Country is Not Found";
		CountryDTO dto = new CountryDTO();
		try {

			CountryEntity countryEntity = countryDAO.findCountryEntityByCode(countryDTO.getCode());

			if (countryEntity != null
					&& !DeleteStatusEnum.DELETE.getCode().equals(countryEntity.getStatusEntity().getCode())) {
				code = ResponseCodeEnum.SUCCESS.getCode();
				description = "Country is Found Successfully";

				dto.setCode(countryEntity.getCode());
				dto.setDescription(countryEntity.getDescription());
				dto.setStatusCode(countryEntity.getStatusEntity().getCode());
				dto.setStatusDescription(countryEntity.getStatusEntity().getDescription());
				dto.setCreatedBy(countryEntity.getCreatedBy());
				dto.setCreatedOn(countryEntity.getCreatedOn());
				dto.setUpdatedBy(countryEntity.getUpdatedBy());
				dto.setUpdatedOn(countryEntity.getUpdatedOn());

			}

		} catch (Exception e) {
			System.err.println("Country Find Issue");
		}
		return new ResponseDTO<CountryDTO>(code, description, dto);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public ResponseDTO<HashMap<String, Object>> getReferenceDataForCountry() {

		HashMap<String, Object> map = new HashMap<>();
		String code = ResponseCodeEnum.FAILED.getCode();
		try {
			List<DropDownDTO> status = statusCategoryDAO.findStatusCategoryByCode(StatusCategoryEnum.DEFAULT.getCode())
					.getStatusEntities().stream().map(s -> new DropDownDTO(s.getCode(), s.getDescription()))
					.collect(Collectors.toList());

			map.put("status", status);

			code = ResponseCodeEnum.SUCCESS.getCode();
		} catch (Exception e) {
			System.err.println("Country Ref Data Issue");
		}
		return new ResponseDTO<HashMap<String, Object>>(code, map);
	}

	@Override
	@Transactional
	public DataTableResponseDTO getCountrysForDataTable(DataTableRequestDTO dataTableRequestDTO) {
		List<CountryDTO> list = new ArrayList<>();
		DataTableResponseDTO responseDTO = new DataTableResponseDTO();
		Long numOfRecord = Long.valueOf(0);
		try {
			list = countryDAO.<List<CountryEntity>>getDataForGrid(dataTableRequestDTO, CommonConstant.GRID_SEARCH_LIST)
					.stream().map(entity -> {
						CountryDTO dto = new CountryDTO();
						dto.setCode(entity.getCode());
						dto.setDescription(entity.getDescription());
						dto.setStatusDescription(entity.getStatusEntity().getDescription());
						dto.setCreatedBy(entity.getCreatedBy());
						dto.setCreatedOn(entity.getCreatedOn());
						dto.setUpdatedBy(entity.getUpdatedBy());
						dto.setUpdatedOn(entity.getUpdatedOn());
						return dto;
					}).collect(Collectors.toList());

			numOfRecord = countryDAO.<Long>getDataForGrid(dataTableRequestDTO, CommonConstant.GRID_SEARC_COUNT);

			responseDTO.setData(list);
			responseDTO.setRecordsTotal(numOfRecord);
			responseDTO.setRecordsFiltered(numOfRecord);
			responseDTO.setDraw(dataTableRequestDTO.getDraw());

		} catch (Exception e) {
		}

		return responseDTO;
	}

}
