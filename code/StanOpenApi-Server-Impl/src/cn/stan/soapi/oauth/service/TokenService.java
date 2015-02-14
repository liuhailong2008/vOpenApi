package cn.stan.soapi.oauth.service;

import org.springframework.stereotype.Service;

import cn.stan.soapi.oauth.model.TokenValidateBo;

@Service
public class TokenService {

	public TokenValidateBo getTokenValidateBo(String clientId) {
		return new TokenValidateBo();
	}
	
}
