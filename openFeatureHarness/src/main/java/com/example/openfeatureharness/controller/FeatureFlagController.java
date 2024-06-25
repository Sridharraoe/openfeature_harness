package com.example.openfeatureharness.controller;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.openfeatureharness.service.FeatureFlagService;

@RestController
public class FeatureFlagController {

	private static final Logger log = Logger.getLogger(FeatureFlagController.class.getName());
	@Autowired
	FeatureFlagService featureFlagService;

	@GetMapping("/openfeature")
	public ResponseEntity<Boolean> getFlagValues(@RequestParam("flagId") String flagId) {
		log.info("getFlagValues() -- start");
		boolean flagValue = featureFlagService.fetchFlagValues(flagId);
		return new ResponseEntity<Boolean>(flagValue, HttpStatus.OK);
	}

}
