package dev.stratospheric.todoapp.cdk;

import java.util.HashMap;
import java.util.Map;

import dev.stratospheric.cdk.ApplicationEnvironment;
import dev.stratospheric.cdk.Network;
import dev.stratospheric.cdk.Service;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

import static dev.stratospheric.todoapp.cdk.Validations.requireNonEmpty;

public class ServiceApp {

  public static void main(final String[] args) {
    App app = new App();

    String environmentName = (String) app.getNode().tryGetContext("environmentName");
    requireNonEmpty(environmentName, "context variable 'environmentName' must not be null");

    String applicationName = (String) app.getNode().tryGetContext("applicationName");
    requireNonEmpty(applicationName, "context variable 'applicationName' must not be null");

    String accountId = (String) app.getNode().tryGetContext("accountId");
    requireNonEmpty(accountId, "context variable 'accountId' must not be null");

    String springProfile = (String) app.getNode().tryGetContext("springProfile");
    requireNonEmpty(springProfile, "context variable 'springProfile' must not be null");

    String dockerRepositoryName = (String) app.getNode().tryGetContext("dockerRepositoryName");
    Validations.requireNonEmpty(dockerRepositoryName, "context variable 'dockerRepositoryName' must not be null");

    String dockerImageTag = (String) app.getNode().tryGetContext("dockerImageTag");
    Validations.requireNonEmpty(dockerImageTag, "context variable 'dockerImageTag' must not be null");


    String region = (String) app.getNode().tryGetContext("region");
    requireNonEmpty(region, "context variable 'region' must not be null");

    Environment awsEnvironment = makeEnv(accountId, region);

    ApplicationEnvironment applicationEnvironment = new ApplicationEnvironment(
      applicationName,
      environmentName
    );

    Stack serviceStack = new Stack(app, "ServiceStack", StackProps.builder()
      .stackName(applicationEnvironment.prefix("Service"))
      .env(awsEnvironment)
      .build());

    Service.DockerImageSource dockerImageSource = new Service.DockerImageSource(dockerRepositoryName, dockerImageTag);
    Network.NetworkOutputParameters networkOutputParameters = Network.getOutputParametersFromParameterStore(serviceStack, applicationEnvironment.getEnvironmentName());
    Service.ServiceInputParameters serviceInputParameters = new Service.ServiceInputParameters(dockerImageSource, environmentVariables(springProfile))
      .withHealthCheckIntervalSeconds(30);

    Service service = new Service(
      serviceStack,
      "Service",
      awsEnvironment,
      applicationEnvironment,
      serviceInputParameters,
      networkOutputParameters);

    app.synth();
  }

  static Map<String, String> environmentVariables(String springProfile) {
    Map<String, String> vars = new HashMap<>();
    vars.put("SPRING_PROFILES_ACTIVE", springProfile);
    return vars;
  }

  static Environment makeEnv(String account, String region) {
    return Environment.builder()
      .account(account)
      .region(region)
      .build();
  }
}
