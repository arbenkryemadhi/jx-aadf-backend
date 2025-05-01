package com.arben.jxaadf.proposalreview;

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
@RequestMapping("/api/proposalreview")
public class ProposalReviewController {
    final ProposalReviewRepository proposalReviewRepository;

    public ProposalReviewController(ProposalReviewRepository proposalReviewRepository) {
        this.proposalReviewRepository = proposalReviewRepository;
    }

    @PostMapping("/create")
    public String createProposalReview(@RequestBody ProposalReview proposalReview) {
        return proposalReviewRepository.createProposalReview(proposalReview);
    }

    @PutMapping("/update")
    public String updateProposalReview(@RequestBody ProposalReview proposalReview) {
        return proposalReviewRepository.updateProposalReview(proposalReview);
    }

    @DeleteMapping("/delete")
    public String deleteProposalReview(@RequestParam int proposalReviewId) {
        return proposalReviewRepository.deleteProposalReview(proposalReviewId);
    }

    @GetMapping("/getreview")
    public ProposalReview getProposalReviewById(@RequestParam int proposalReviewId) {
        return proposalReviewRepository.getProposalReviewById(proposalReviewId);
    }

    @GetMapping("/getbyproposalid")
    public List<ProposalReview> getAllProposalReviewsOfProposal(@RequestParam int proposalId) {
        return proposalReviewRepository.getAllProposalReviewsOfProposal(proposalId);
    }

    @GetMapping("/getbyuserid")
    public List<ProposalReview> getAllProposalReviewsOfUser(@RequestParam String userId) {
        return proposalReviewRepository.getAllProposalReviewsOfUser(userId);
    }
}
