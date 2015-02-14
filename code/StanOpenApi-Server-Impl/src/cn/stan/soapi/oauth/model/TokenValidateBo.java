package cn.stan.soapi.oauth.model;

public class TokenValidateBo {
	
	protected String name;
    protected String clientId;
    protected String clientSecret;
    protected String redirectUri;
    protected String clientUri;
    protected String description;
    protected String iconUri;
    protected Long issuedAt;
    protected Long expiresIn;
    
    protected String authCode;
    protected String username;
    protected String password;
    
	public boolean validateClientSecret(String clientSecret) {
		boolean ret = clientSecret!=null && clientSecret.equals(this.clientSecret);
		return ret;
	}
	
	public boolean validateAuthCode(String param) {
		boolean ret = param!=null && param.equals(this.authCode);
		return ret;
	}
	
	public boolean validateUsernameAndPassword(String username, String password) {
		boolean ret1 = username!=null && username.equals(this.username);
		boolean ret2 = password!=null && password.equals(this.password);
		return ret1 && ret2;
	}
	
    public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getRedirectUri() {
		return redirectUri;
	}

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	public String getClientUri() {
		return clientUri;
	}

	public void setClientUri(String clientUri) {
		this.clientUri = clientUri;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIconUri() {
		return iconUri;
	}

	public void setIconUri(String iconUri) {
		this.iconUri = iconUri;
	}

	public Long getIssuedAt() {
		return issuedAt;
	}

	public void setIssuedAt(Long issuedAt) {
		this.issuedAt = issuedAt;
	}

	public Long getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Long expiresIn) {
		this.expiresIn = expiresIn;
	}

	



	

}
