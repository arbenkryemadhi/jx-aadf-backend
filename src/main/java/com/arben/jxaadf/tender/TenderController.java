package com.arben.jxaadf.tender;

import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/tender")
public class TenderController {

    private final TenderRepository tenderRepository;

    public TenderController(TenderRepository tenderRepository) {
        this.tenderRepository = tenderRepository;
    }

    @PostMapping("/create")
    public int createTender(@RequestBody Tender tender) {
        return tenderRepository.createTender(tender);
    }

    @PutMapping("/end")
    public void endTender(@RequestHeader(value = "tenderId") int tenderId) {
        tenderRepository.endTender(tenderId);
    }

    @GetMapping("/getallactive")
    public List<Tender> getAllActiveTenders() {
        return tenderRepository.getAllActiveTenders();
    }

    @DeleteMapping("/delete")
    public void deleteTender(@RequestHeader(value = "tenderId") int tenderId) {
        tenderRepository.deleteTender(tenderId);
    }

    @GetMapping("/search")
    public List<Tender> searchTenders(@RequestHeader(value = "searchTerm") String searchTerm) {
        return tenderRepository.searchTenders(searchTerm);
    }

    @GetMapping("/getbyid")
    public Tender getTenderById(@RequestHeader(value = "tenderId") int tenderId) {
        return tenderRepository.getTenderById(tenderId);
    }

    @PutMapping("/update")
    public void updateTender(@RequestBody Tender tender) {
        tenderRepository.updateTender(tender);
    }

    @PutMapping("/makewinner")
    public void makeWinner(@RequestHeader(value = "tenderId") int tenderId,
            @RequestHeader(value = "proposalId") int proposalId) {
        tenderRepository.makeWinner(tenderId, proposalId);
    }

    @GetMapping("/getall")
    public List<Tender> getAllTenders() {
        return tenderRepository.getAllTenders();
    }

    @PutMapping("/addlink")
    public void addLink(@RequestHeader(value = "tenderId") int tenderId, @RequestBody String link) {
        tenderRepository.addLink(tenderId, link);
    }

}
