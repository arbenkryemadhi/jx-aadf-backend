package com.arben.jxaadf.tender;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


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

}
