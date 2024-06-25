package com.example.openfeatureharness.service;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.openfeatureharness.provider.HarnessProvider;

import dev.openfeature.sdk.Client;
import dev.openfeature.sdk.EvaluationContext;
import dev.openfeature.sdk.ImmutableContext;
import dev.openfeature.sdk.OpenFeatureAPI;
import dev.openfeature.sdk.Value;

@Service
public class FeatureFlagService {

	private static final Logger log = Logger.getLogger(FeatureFlagService.class.getName());

	@Autowired
	HarnessProvider harnessProvider;

	public boolean fetchFlagValues(String flagId) {

		log.info("fetchFlagValues() -- start");
		// configure a provider
		OpenFeatureAPI api = OpenFeatureAPI.getInstance();
		api.setProviderAndWait(harnessProvider);

		// create a client
		Client client = api.getClient("harness");

		Map<String, Value> requestAttrs = new HashMap<>();
		requestAttrs.put("name", new Value("testOF"));
		requestAttrs.put("identifier", new Value("test_open_feature"));

		EvaluationContext clientCtx = new ImmutableContext(requestAttrs);

		// get a bool flag value
		boolean flagValue = client.getBooleanValue(flagId, false, clientCtx);
		log.info("Flag value " + flagValue);
		log.info("fetchFlagValues() -- end");
		return flagValue;
	}

}
