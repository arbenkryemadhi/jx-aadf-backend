package com.arben.jxaadf.proposal;

import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/proposal")

public class ProposalController {

    final ProposalRepository proposalRepository;

    public ProposalController(ProposalRepository proposalRepository) {
        this.proposalRepository = proposalRepository;
    }

    @PostMapping("/create")
    public String createProposal(@RequestBody Proposal proposal) {
        return proposalRepository.createProposal(proposal);
    }

    @PutMapping("/update")
    public String updateProposal(@RequestBody Proposal proposal) {
        return proposalRepository.updateProposal(proposal);
    }

    @DeleteMapping("/delete")
    public String deleteProposal(@RequestParam int proposalId) {
        return proposalRepository.deleteProposal(proposalId);
    }

    @GetMapping("/getalluser")
    public List<Proposal> getAllUserProposals(@RequestParam String userId) {
        return proposalRepository.getAllUserProposals(userId);
    }

    @GetMapping("/getalltender")
    public List<Proposal> getAllTenderProposals(@RequestParam int tenderId) {
        return proposalRepository.getAllTenderProposals(tenderId);
    }

    @GetMapping("/getbyid")
    public Proposal getProposalById(@RequestParam int proposalId) {
        return proposalRepository.getProposalById(proposalId);
    }

    @PutMapping("/addlink")
    public String addDocumentLink(@RequestParam int proposalId, @RequestBody String link) {
        return proposalRepository.addDocumentLink(proposalId, link);
    }

    @PutMapping("/removelink")
    public String removeDocumentLink(@RequestParam int proposalId, @RequestBody String link) {
        return proposalRepository.removeDocumentLink(proposalId, link);
    }

    @PutMapping("/addaiscore")
    public String addAiScore(@RequestParam int proposalId, @RequestParam int aiScore) {
        return proposalRepository.updateAiScore(proposalId, aiScore);
    }

    @GetMapping("/averagescrore")
    public double getAverageScore(@RequestParam int proposalId) {
        return proposalRepository.getAverageScore(proposalId);
    }

}
