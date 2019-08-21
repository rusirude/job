package com.leaf.job.controller;

import com.leaf.job.dto.CountryDTO;
import com.leaf.job.dto.common.DataTableRequestDTO;
import com.leaf.job.dto.common.DataTableResponseDTO;
import com.leaf.job.dto.common.ResponseDTO;
import com.leaf.job.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

@Controller
@RequestMapping(path = "/country")
public class CountryController {

	private CountryService countryService;

	@Autowired
	public CountryController(CountryService countryService) {
		this.countryService = countryService;
	}

	@PreAuthorize("hasRole('ROLE_COUNTRY')")
	@RequestMapping(path = "/", method = RequestMethod.GET)
	public ModelAndView viewCountry() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("country");
		return mv;
	}

	@PreAuthorize("hasRole('ROLE_COUNTRY')")
	@RequestMapping(path = "/loadRefDataForCountry", method = RequestMethod.POST)
	@ResponseBody
	public ResponseDTO<HashMap<String, Object>> loadCountryReferenceData() {
		return countryService.getReferenceDataForCountry();
	}

	@PreAuthorize("hasRole('ROLE_COUNTRY')")
	@RequestMapping(path = "/save", method = RequestMethod.POST)
	@ResponseBody
	public ResponseDTO<?> saveCountry(@RequestBody CountryDTO countryDTO) {
		return countryService.saveCountry(countryDTO);
	}

	@PreAuthorize("hasRole('ROLE_COUNTRY')")
	@RequestMapping(path = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ResponseDTO<?> updateCountry(@RequestBody CountryDTO countryDTO) {
		return countryService.updateCountry(countryDTO);
	}

	@PreAuthorize("hasRole('ROLE_COUNTRY')")
	@RequestMapping(path = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public ResponseDTO<?> deleteCountry(@RequestBody CountryDTO countryDTO) {
		return countryService.deleteCountry(countryDTO);
	}

	@PreAuthorize("hasRole('ROLE_COUNTRY')")
	@RequestMapping(path = "/loadCountries", method = RequestMethod.POST)
	@ResponseBody
	public DataTableResponseDTO loadCountryDataGrid(@RequestBody DataTableRequestDTO dataTableRequestDTO) {
		return countryService.getCountrysForDataTable(dataTableRequestDTO);
	}

	@PreAuthorize("hasRole('ROLE_COUNTRY')")
	@RequestMapping(path = "/loadCountryByCode", method = RequestMethod.POST)
	@ResponseBody
	public ResponseDTO<CountryDTO> loadCountryByCode(@RequestBody CountryDTO countryDTO) {
		return countryService.findCountry(countryDTO);
	}
}
