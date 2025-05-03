package com.arben.jxaadf.tender;

import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.arben.jxaadf.appuser.AppUser;
import com.arben.jxaadf.appuser.AppUserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/tender")
public class TenderController {

    private final TenderRepository tenderRepository;
    private final AppUserRepository appUserRepository;

    public TenderController(TenderRepository tenderRepository,
            AppUserRepository appUserRepository) {

        this.tenderRepository = tenderRepository;
        this.appUserRepository = appUserRepository;
    }

    @PostMapping("/create")
    public int createTender(@RequestBody Tender tender) {
        return tenderRepository.createTender(tender);
    }

    @PutMapping("/end")
    public void endTender(@RequestHeader int tenderId) {
        tenderRepository.endTender(tenderId);
    }

    @GetMapping("/getallactive")
    public List<Tender> getAllActiveTenders() {
        return tenderRepository.getAllActiveTenders();
    }

    @DeleteMapping("/delete")
    public void deleteTender(@RequestHeader int tenderId) {
        tenderRepository.deleteTender(tenderId);
    }

    @GetMapping("/search")
    public List<Tender> searchTenders(@RequestHeader String searchTerm) {
        return tenderRepository.searchTenders(searchTerm);
    }

    @GetMapping("/getbyid")
    public Tender getTenderById(@RequestHeader int tenderId) {
        return tenderRepository.getTenderById(tenderId);
    }

    @PutMapping("/update")
    public void updateTender(@RequestBody Tender tender) {
        tenderRepository.updateTender(tender);
    }

    @PutMapping("/makewinner")
    public void makeWinner(@RequestHeader int tenderId, @RequestHeader int proposalId) {
        tenderRepository.makeWinner(tenderId, proposalId);
    }

    @GetMapping("/getall")
    public List<Tender> getAllTenders() {
        return tenderRepository.getAllTenders();
    }

    @PutMapping("/addlink")
    public void addLink(@RequestHeader int tenderId, @RequestBody String link) {
        tenderRepository.addLink(tenderId, link);
    }

    @PutMapping("/addstaff")
    public void addStaffToTender(@RequestHeader int tenderId, @RequestHeader String staffId) {
        tenderRepository.addStaffToTender(tenderId, staffId);
    }

    // EMAIL
    @PutMapping("/addstaffemal")
    public void addStaffToTenderByEmail(@RequestHeader int tenderId, @RequestHeader String email) {
        String staffId = appUserRepository.getIdFromEmail(email);
        tenderRepository.addStaffToTender(tenderId, staffId);
    }

    @PutMapping("/removestaff")
    public void removeStaffFromTender(@RequestHeader int tenderId, @RequestHeader String staffId) {
        tenderRepository.removeStaffFromTender(tenderId, staffId);
    }

    @GetMapping("/getbystaff")
    public List<Tender> getTendersByStaffId(@RequestParam String staffId) {
        return tenderRepository.getTendersByStaffId(staffId);
    }


    // DASHBOARD

    @GetMapping("/totatenderslvalue")
    public Double getTotalTendersValue() {
        return tenderRepository.getTotalTendersValue();
    }


    @GetMapping("/getnumberofactivetenders")
    public int getNumberOfActiveTenders() {
        return tenderRepository.getNumberOfActiveTenders();
    }

    @GetMapping("/getnumberofendedtenders")
    public int getNumberOfEndedTenders() {
        return tenderRepository.getNumberOfEndedTenders();
    }


}
