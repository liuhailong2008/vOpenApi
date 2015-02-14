package cn.stan.soapi.oauth;

public enum OAuthProviderType{
	
	BJAIC_DJ(
			"bjaic_dj", 
			"https://wsdj.bjaic.gov.cn/oauth2/authorize", 
			"https://wsdj.bjaic.gov.cn/oauth2/access_token");
	
	private String providerName;
	
	private String authzEndpoint; 
	
	private String tokenEndpoint;
	
	/**
	 * Get the provider name
	 * 
	 * @return Returns the provider name
	 */
	public String getProviderName() {
		return providerName;
	}
	
	/**
	 * Get the authorization endpoint
	 * 
	 * @return Returns the authorization endpoint
	 */
	public String getAuthzEndpoint() {
		return authzEndpoint;
	}
	
	/**
	 * Get the access token endpoint
	 * 
	 * @return Returns the access token endpoint
	 */
	public String getTokenEndpoint() {
		return tokenEndpoint;
	}
	
	/**
	 * Full private constructor
	 * 
	 * @param providerName The provider name
	 * @param authzEndpoint The authorization endpoint
	 * @param tokenEndpoint The token endpoint
	 */
	private OAuthProviderType(
			final String providerName, 
			final String authzEndpoint, 
			final String tokenEndpoint) {
		
		this.providerName = providerName;
		this.authzEndpoint = authzEndpoint;
		this.tokenEndpoint = tokenEndpoint;
	}
}
