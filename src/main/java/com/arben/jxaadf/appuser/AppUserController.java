package com.arben.jxaadf.appuser;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/api/appuser")
public class AppUserController {

    private final AppUserRepository appUserRepository;

    public AppUserController(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @PostMapping("/create")
    public void createAppUser(@RequestBody AppUser appUser) {

        appUserRepository.createAppUser(appUser);
    }


}
