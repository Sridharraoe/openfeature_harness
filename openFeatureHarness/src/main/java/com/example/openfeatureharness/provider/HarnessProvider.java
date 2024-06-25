package com.example.openfeatureharness.provider;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import dev.openfeature.sdk.EvaluationContext;
import dev.openfeature.sdk.FeatureProvider;
import dev.openfeature.sdk.Metadata;
import dev.openfeature.sdk.OpenFeatureAPI;
import dev.openfeature.sdk.ProviderEvaluation;
import dev.openfeature.sdk.ProviderState;
import io.harness.cf.client.api.CfClient;
import io.harness.cf.client.dto.Target;

@Component
public class HarnessProvider implements FeatureProvider {

	private static final Logger log = Logger.getLogger(HarnessProvider.class.getName());

	@Value("${harness.sdk.key}")
	String sdkKey;

	CfClient cfClient = null;

	@Override
	public Metadata getMetadata() {
		return () -> "HarnessProvider";
	}

	@Override
	public ProviderState getState() {
		// TODO Auto-generated method stub
		return ProviderState.NOT_READY;
	}

	@Override
	public void initialize(EvaluationContext evaluationContext) throws Exception {
		// start up your provider
		log.info("initialize() -- start");
		try {
			// Create a Feature Flag Client
			log.info("sdkKey " + sdkKey);
			cfClient = new CfClient(sdkKey);
			cfClient.waitForInitialization();

		} catch (Exception e) {
			log.info("Exception "+e.getMessage());
		} finally {
			// Close the SDK
//			CfClient.getInstance().close();
		}
	}

	@Override
	public ProviderEvaluation<Boolean> getBooleanEvaluation(String key, Boolean defaultValue, EvaluationContext ctx) {
		log.info("getBooleanEvaluation() -- start");

		log.info("Context " + ctx.getValue("name").asString());
		log.info("Context " + ctx.getValue("identifier").asString());

		Target target = Target.builder()
                .identifier(ctx.getValue("identifier").asString())
                .name(ctx.getValue("name").asString())
                .build();
		
		Boolean value = cfClient.boolVariation(key, target, false);
		ProviderEvaluation<Boolean> pe = ProviderEvaluation.<Boolean>builder().value(value).build();
		cfClient.close();
		shutdown();
		log.info("getBooleanEvaluation() -- end");
		return pe;
	}

	@Override
	public ProviderEvaluation<String> getStringEvaluation(String key, String defaultValue, EvaluationContext ctx) {
		String value = cfClient.stringVariation("", null, "");
		ProviderEvaluation<String> pe = ProviderEvaluation.<String>builder().value(value).build();

		return pe;
	}

	@Override
	public ProviderEvaluation<Integer> getIntegerEvaluation(String key, Integer defaultValue, EvaluationContext ctx) {

		return null;
	}

	@Override
	public ProviderEvaluation<Double> getDoubleEvaluation(String key, Double defaultValue, EvaluationContext ctx) {
		return null;
	}

	@Override
	public void shutdown() {
		// shut down provider
		OpenFeatureAPI.getInstance().shutdown();
	}

	@Override
	public ProviderEvaluation<dev.openfeature.sdk.Value> getObjectEvaluation(String key,
			dev.openfeature.sdk.Value defaultValue, EvaluationContext ctx) {
		return null;
	}

}
