package com.leaf.job.controller;

import com.leaf.job.dto.SysUserDTO;
import com.leaf.job.dto.common.ResponseDTO;
import com.leaf.job.service.PasswordResetRequestService;
import com.leaf.job.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

/**
 * @author : rusiru on 7/6/19.
 */

@Controller
public class ProfileController {


    private ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @RequestMapping(path = "/profile", method = RequestMethod.GET)
    public ModelAndView profile() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("profile");
        return mv;
    }

    @RequestMapping(path = "/profileReference", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDTO<HashMap<String, Object>> loadSystemUserReferenceData() {
        return profileService.getReferenceDataForProfile();
    }

    @RequestMapping(path = "/saveProfile", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDTO<?> saveProfile(@RequestBody SysUserDTO sysUserDTO) {
        return profileService.saveProfile(sysUserDTO);
    }
}