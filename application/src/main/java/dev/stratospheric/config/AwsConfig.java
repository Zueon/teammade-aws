package dev.stratospheric.config;


import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderAsyncClientBuilder;
import io.awspring.cloud.core.region.RegionProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {
  @Bean
  @ConditionalOnProperty(prefix = "custom", name = "use-cognito-as-identity-provider", havingValue = "true")
  public AWSCognitoIdentityProvider awsCognitoIdentityProvider(
    RegionProvider regionProvider,
    AWSCredentialsProvider awsCredentialsProvider) {
    return AWSCognitoIdentityProviderAsyncClientBuilder.standard()
      .withCredentials(awsCredentialsProvider)
      .withRegion(regionProvider.getRegion().getName())
      .build();
  }
}
