package com.arben.jxaadf.staff;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/staff")
public class StaffController {

    private final StaffRepository staffRepository;

    public StaffController(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @GetMapping("/api/isaadfstaff")
    public boolean isStaff(@RequestParam String email) {
        return staffRepository.isStaff(email);
    }


}
